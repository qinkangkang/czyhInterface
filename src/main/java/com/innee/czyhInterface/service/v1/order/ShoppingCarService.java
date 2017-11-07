package com.innee.czyhInterface.service.v1.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.ColumnBannerDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.GoodsSkuDAO;
import com.innee.czyhInterface.dao.SeckillModuleDAO;
import com.innee.czyhInterface.dao.ShoppingAddressDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.CartGoodsDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.goods.CartDTO;
import com.innee.czyhInterface.entity.TColumnBanner;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TGoodsSku;
import com.innee.czyhInterface.entity.TSeckillModule;
import com.innee.czyhInterface.entity.TShoppingAddress;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.service.v1.goods.GoodsService;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.DateUtil;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.redis.RedisMoudel;

/**
 * 购物车
 * 
 * @author jinshengzhi
 *
 */
@Component("ShoppingCarService_V1")
@Transactional
public class ShoppingCarService {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingCarService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private ColumnBannerDAO columnBannerDAO;

	@Autowired
	private RedisService redisService;

	@Autowired
	private ShoppingAddressDAO shoppingAddressDAO;
	
	@Autowired
	private SeckillModuleDAO seckillModuleDAO;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private GoodsSkuDAO goodsSkuDAO;

	/**
	 * 购物车
	 * 
	 * @param ticket
	 * @param goodsList
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO shoppingcart(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		CustomerDTO customerDTO = null;
		customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			Map<String, Object> returnData = Maps.newHashMap();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			returnData.put("ticket", Lists.newArrayList());
			responseDTO.setData(returnData);
			return responseDTO;
		}
		/*List<TGoodsSku> sku = goodsSkuDAO.findAll();
		for(TGoodsSku t : sku){
			String goodsSkuCache = mapper.toJson(t);
			redisService.putCache(RedisMoudel.goodsSku, t.getId(), goodsSkuCache);
		}*/
		
		Map<String, Object> returnData = Maps.newHashMap();
		String shoppingcar = null;
		try {
			shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
		Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
		goodsMap = mapper.fromJson(shoppingcar, jt);

		BigDecimal total = BigDecimal.ZERO;
		int msg = 0;
		List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
		TSponsor tSponsor = null;
		boolean allCheck = true;
		if (goodsMap != null) {
			String goodsSku = null;
			TGoodsSku tGoodsSku  = null;
			for (Map.Entry entry : goodsMap.entrySet()) {
				CartGoodsDTO cartGoodsDTO = (CartGoodsDTO) entry.getValue();
				try {
					goodsSku = redisService.getValue(cartGoodsDTO.getGoodsSkuId(), RedisMoudel.goodsSku);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(StringUtils.isNotBlank(goodsSku)){
					tGoodsSku = mapper.fromJson(goodsSku, TGoodsSku.class);
					TEvent tEvent = eventDAO.findOne(tGoodsSku.getFgoodsId());
					tSponsor = tEvent.getTSponsor();
					
					cartGoodsDTO.setSdealsModel(tEvent.getFsdealsModel());
					cartGoodsDTO.setPresentPrice(tGoodsSku.getFpriceMoney().toString());
					cartGoodsDTO.setOriginalPrice(tGoodsSku.getFprice().toString());
					
					Map<String, Object> map = this.checkGoods(tEvent, tGoodsSku, cartGoodsDTO, msg, customerDTO);
					msg = (Integer) map.get("num");
					cartGoodsDTO.setCount((Integer) map.get("count"));
					if(cartGoodsDTO.isChecked()){
						total = total.add(new BigDecimal(cartGoodsDTO.getPresentPrice())
								.multiply(new BigDecimal(cartGoodsDTO.getCount()))).setScale(2, RoundingMode.HALF_UP);
					}else{
						allCheck = false;
					}
					if (cartGoodsDTO.getCount() > 0) {
						cartGoodsDTOList.add(cartGoodsDTO);
					}
				}else{
					msg = 1;
				}
			}
		}
		if (msg == 1) {
			returnData.put("ifChange", true);
			returnData.put("changeMsg", "您购买的部分商品库存不足");
		} else if (msg == 2) {
			returnData.put("ifChange", true);
			returnData.put("changeMsg", "您购买的部分商品限购");
		} else {
			returnData.put("ifChange", false);
			returnData.put("changeMsg", "");
		}
		BigDecimal freight = BigDecimal.ZERO;
		BigDecimal freePostage = BigDecimal.ZERO;
		if (tSponsor != null) {
			freight = this.getFreight(total, tSponsor.getFpinkage(), tSponsor.getFrange());
			if(freight.compareTo(BigDecimal.ZERO)>0){
				freePostage = tSponsor.getFpinkage().subtract(total);
			}
			if (cartGoodsDTOList == null || cartGoodsDTOList.size() == 0) {
				freight = BigDecimal.ZERO;
			}
		}
		CartDTO cartDTO = new CartDTO();
		cartDTO.setCartGoodsDTOs(cartGoodsDTOList);
		cartDTO.setChangeAmount(new BigDecimal(0));
		cartDTO.setReceivableTotal(total.add(freight));
		cartDTO.setTotal(total);
		cartDTO.setFreight(freight);
		cartDTO.setGoodsTotal(total);
		cartDTO.setFreePostage(freePostage.toString());
		returnData.put("cartDTO", cartDTO);
		returnData.put("allCheck", allCheck);
		redisService.removeCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId());
		Map<String, CartGoodsDTO> cartMap = new HashMap<String, CartGoodsDTO>();
		for (CartGoodsDTO cartGoodsDTO : cartGoodsDTOList) {
			cartMap.put(cartGoodsDTO.getGoodsSkuId(), cartGoodsDTO);
		}
		if (cartGoodsDTOList != null && cartGoodsDTOList.size() > 0) {
			String goodsCache = mapper.toJson(cartMap);
			redisService.putCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId(), goodsCache);
		}

		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("加载购物车信息成功！");

		return responseDTO;
	}

	public BigDecimal getFreight(BigDecimal total, BigDecimal pinkage, BigDecimal range) {
		BigDecimal freight = BigDecimal.ZERO;
		if (pinkage == null || total == null || range == null) {
			return freight;
		}
		if (total.compareTo(pinkage) < 0) {
			freight = range;
		}
		return freight;
	}

	public ResponseDTO modifyGoods(String ticket,String goodsSkuId, Integer status, Integer type,
			Integer num, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(goodsSkuId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("goodsSkuId参数不能为空，请检查goodsSkuId的传递参数值！");
			return responseDTO;
		}
		if (status == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("status参数不能为空，请检查status的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}
		}
		
		try {
			String shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
			CartGoodsDTO cartGoodsDto = null;
			Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
			if (StringUtils.isNotBlank(shoppingcar)) {
				JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
				goodsMap = mapper.fromJson(shoppingcar, jt);
				if (goodsMap.containsKey(goodsSkuId)) {
					cartGoodsDto = goodsMap.get(goodsSkuId);
					if (status.intValue() == 0) {// 添加商品
						cartGoodsDto.setCount(num);
						goodsMap.put(cartGoodsDto.getGoodsSkuId(), cartGoodsDto);
					} else if (status.intValue() == 1) {// 添加x件商品
						cartGoodsDto.setCount(cartGoodsDto.getCount()+num);
						goodsMap.put(cartGoodsDto.getGoodsSkuId(), cartGoodsDto);
					} else if (status.intValue() == 2) {// 删除商品
						goodsMap.remove(goodsSkuId);
					} else if (status.intValue() == 3) {// 添加一件商品
						cartGoodsDto.setCount(cartGoodsDto.getCount()+1);
						goodsMap.put(cartGoodsDto.getGoodsSkuId(), cartGoodsDto);
					} else if (status.intValue() == 4) {// 减去一件商品
						cartGoodsDto.setCount(cartGoodsDto.getCount()-1);
						goodsMap.put(cartGoodsDto.getGoodsSkuId(), cartGoodsDto);
					}
				} else {
					cartGoodsDto = new CartGoodsDTO();
					cartGoodsDto.setGoodsSkuId(goodsSkuId);
					if (status.intValue() == 0) {// 添加商品
						cartGoodsDto.setCount(num);
					}else if (status.intValue() == 1) {//添加一件商品
						cartGoodsDto.setCount(num);
					}else if (status.intValue() == 3) {//添加一件商品
						cartGoodsDto.setCount(1);
					} 
					goodsMap.put(cartGoodsDto.getGoodsSkuId(), cartGoodsDto);
				}
			} else {
				cartGoodsDto = new CartGoodsDTO();
				cartGoodsDto.setGoodsSkuId(goodsSkuId);
				if (status.intValue() == 0) {// 添加商品
					cartGoodsDto.setCount(num);
				}else if (status.intValue() == 1) {//添加一件商品
					cartGoodsDto.setCount(num);
				}else if (status.intValue() == 3) {//添加一件商品
					cartGoodsDto.setCount(1);
				}
				goodsMap.put(cartGoodsDto.getGoodsSkuId(), cartGoodsDto);
			}

			BigDecimal total = BigDecimal.ZERO;
			int msg = 0;
			List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
			CartGoodsDTO cartGoodsDTO = null;
			TSponsor tSponsor = null;
			boolean allCheck = true;
			TGoodsSku tGoodsSku = null;
			for (Map.Entry<String, CartGoodsDTO> entry : goodsMap.entrySet()) {
				tGoodsSku = goodsService.getGoodsSkuCache(entry.getKey());
				TEvent tEvent = eventDAO.findOne(tGoodsSku.getFgoodsId());
				tSponsor = tEvent.getTSponsor();
			    cartGoodsDTO= entry.getValue();
				if(type.intValue()==1&&entry.getKey().toString().equals(goodsSkuId)){
					cartGoodsDTO.setChecked(true);
				}
				cartGoodsDTO.setSdealsModel(tEvent.getFsdealsModel());
				cartGoodsDTO.setGoodsId(tEvent.getId());
				cartGoodsDTO.setGoodsTitle(tEvent.getFtitle());
				cartGoodsDTO.setGoodsSkuId(tGoodsSku.getId());
				cartGoodsDTO.setImageUrl(fxlService.getImageUrl(tGoodsSku.getFimage(), false));
				cartGoodsDTO.setPresentPrice(tGoodsSku.getFpriceMoney().toString());
				cartGoodsDTO.setSpec(goodsService.getGoodsSku(
						tGoodsSku.getFclassTypeValue1()!=null ? tGoodsSku.getFclassTypeValue1():0,
						tGoodsSku.getFclassTypeValue2()!=null ? tGoodsSku.getFclassTypeValue2():0));
				cartGoodsDTO.setCount(((CartGoodsDTO)entry.getValue()).getCount());
				cartGoodsDTO.setOriginalPrice(tGoodsSku.getFprice().toString());
				cartGoodsDTO.setPromotionModel(
						DictionaryUtil.getString(DictionaryUtil.PromotionModel, tEvent.getFpromotionModel()));
				cartGoodsDTO.setLimitation(tGoodsSku.getFlimitation());
				// 先判断商品是否有下架
				Map<String, Object> map = this.checkGoods(tEvent, tGoodsSku, cartGoodsDTO, msg, customerDTO);
				msg = (Integer) map.get("num");
				cartGoodsDTO.setCount((Integer) map.get("count"));
				if(cartGoodsDTO.isChecked()){
					total = total.add(new BigDecimal(cartGoodsDTO.getPresentPrice())
						.multiply(new BigDecimal(cartGoodsDTO.getCount()))).setScale(2, RoundingMode.HALF_UP);
				}else{
					allCheck = false;
				}
				if (cartGoodsDTO.getCount() > 0) {
					cartGoodsDTOList.add(cartGoodsDTO);
				}
			}
			if (msg == 1) {
				returnData.put("ifChange", true);
				returnData.put("changeMsg", "您购买的部分商品库存不足");
			} else if (msg == 2) {
				returnData.put("ifChange", true);
				returnData.put("changeMsg", "您购买的部分商品限购");
			} else {
				returnData.put("ifChange", false);
				returnData.put("changeMsg", "");
			}
			BigDecimal freight = BigDecimal.ZERO;
			BigDecimal freePostage = BigDecimal.ZERO;
			if (tSponsor != null) {
				freight = this.getFreight(total, tSponsor.getFpinkage(), tSponsor.getFrange());
				if(freight.compareTo(BigDecimal.ZERO)>0){
					freePostage = tSponsor.getFpinkage().subtract(total);
				}
				if (cartGoodsDTOList == null || cartGoodsDTOList.size() == 0) {
					freight = BigDecimal.ZERO;
				}
			}
			CartDTO cartDTO = new CartDTO();
			cartDTO.setCartGoodsDTOs(cartGoodsDTOList);
			cartDTO.setChangeAmount(new BigDecimal(0));
			cartDTO.setReceivableTotal(total.add(freight));
			cartDTO.setTotal(total);
			cartDTO.setFreight(freight);
			cartDTO.setGoodsTotal(total);
			cartDTO.setFreePostage(freePostage.toString());
			returnData.put("cartDTO", cartDTO);
			returnData.put("allCheck", allCheck);

			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			if (status == 0 || status == 3 || status == 1) {
				responseDTO.setMsg("成功加入购物车");
			} else {
				responseDTO.setMsg("成功从购物车删除");
			}
			responseDTO.setData(returnData);

			redisService.removeCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId());
			Map<String, Object> cartMap = new HashMap<String, Object>();
			for (CartGoodsDTO cartGoods : cartGoodsDTOList) {
				cartMap.put(cartGoods.getGoodsSkuId(), cartGoods);
			}
			if (cartGoodsDTOList != null && cartGoodsDTOList.size() > 0) {
				String goodsCache = mapper.toJson(cartMap);
				redisService.putCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId(), goodsCache);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO goSettlement(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}
		}
		Map<String, Object> returnData = Maps.newHashMap();
		String shoppingcar = null;
		try {
			shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
		Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
		goodsMap = mapper.fromJson(shoppingcar, jt);

		BigDecimal total = BigDecimal.ZERO;
		int msg = 0;
		List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
		TSponsor tSponsor = null;
		if (goodsMap != null) {
			String goodsSku = null;
			TGoodsSku tGoodsSku = null;
			for (Map.Entry entry : goodsMap.entrySet()) {
				CartGoodsDTO cartGoodsDTO = (CartGoodsDTO) entry.getValue();
				try {
					goodsSku = redisService.getValue(cartGoodsDTO.getGoodsSkuId(), RedisMoudel.goodsSku);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(StringUtils.isNotBlank(goodsSku)){
					tGoodsSku = mapper.fromJson(goodsSku, TGoodsSku.class);
					TEvent tEvent = eventDAO.findOne(cartGoodsDTO.getGoodsId());
					tSponsor = tEvent.getTSponsor();
					
					cartGoodsDTO.setPresentPrice(tGoodsSku.getFpriceMoney().toString());
					cartGoodsDTO.setOriginalPrice(tGoodsSku.getFprice().toString());
					
					Map<String, Object> map = this.checkGoods(tEvent, tGoodsSku, cartGoodsDTO, msg, customerDTO);
					msg = (Integer) map.get("num");
					cartGoodsDTO.setCount((Integer) map.get("count"));
					if(cartGoodsDTO.isChecked()){
						total = total.add(new BigDecimal(cartGoodsDTO.getPresentPrice())
								.multiply(new BigDecimal(cartGoodsDTO.getCount()))).setScale(2, RoundingMode.HALF_UP);
					}
					if (cartGoodsDTO.getCount() > 0) {
						cartGoodsDTOList.add(cartGoodsDTO);
					}
				}else{
					msg = 1;
				}
			}
		}
		if (msg == 1) {
			returnData.put("ifChange", true);
			returnData.put("changeMsg", "您购买的部分商品库存不足");
		} else if (msg == 2) {
			returnData.put("ifChange", true);
			returnData.put("changeMsg", "您购买的部分商品限购");
		} else {
			returnData.put("ifChange", false);
			returnData.put("changeMsg", "");
		}

		TShoppingAddress shoppingAddress = shoppingAddressDAO.findByDefault(customerDTO.getCustomerId());
		if (shoppingAddress != null) {
			returnData.put("ifAddress", true);
			returnData.put("customerName", shoppingAddress.getFname());
			returnData.put("customerPhone", shoppingAddress.getFphone());
			returnData.put("address", shoppingAddress.getFaddress());

		} else {
			returnData.put("ifAddress", false);
		}

		BigDecimal freight = BigDecimal.ZERO;
		if (tSponsor != null) {
			freight = this.getFreight(total, tSponsor.getFpinkage(), tSponsor.getFrange());
		}

		redisService.removeCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId());
		Map<String, CartGoodsDTO> cartMap = new HashMap<String, CartGoodsDTO>();
		List<CartGoodsDTO> cartGoodsList = Lists.newArrayList();
		for (CartGoodsDTO cartGoodsDTO : cartGoodsDTOList) {
			cartMap.put(cartGoodsDTO.getGoodsSkuId(), cartGoodsDTO);
			if(cartGoodsDTO.isChecked()){
				cartGoodsList.add(cartGoodsDTO);
			}
		}
		if (cartGoodsDTOList != null && cartGoodsDTOList.size() > 0) {
			String goodsCache = mapper.toJson(cartMap);
			redisService.putCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId(), goodsCache);
		}

		CartDTO cartDTO = new CartDTO();
		cartDTO.setCartGoodsDTOs(cartGoodsList);
		cartDTO.setChangeAmount(new BigDecimal(0));
		cartDTO.setReceivableTotal(total.add(freight));
		cartDTO.setTotal(total);
		cartDTO.setFreight(freight);
		cartDTO.setGoodsTotal(total);
		returnData.put("cartDTO", cartDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单结算成功！");
		return responseDTO;
	}

	public ResponseDTO buyNow(String ticket, String goodsSkuId, Integer num, Integer clientType,String sign,String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(goodsSkuId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodsSkuId参数不能为空，请检查goodsSkuId的传递参数值！");
			return responseDTO;
		}
		if (num == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("num参数不能为空，请检查num的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("sign参数不能为空，请检查sign的传递参数值！");
			return responseDTO;
		}
		
	    String signStr = fxlService.httpEncrypt(addressIP, clientType);
	    if (!signStr.equals(sign)) {
	      responseDTO.setSuccess(false);
	      responseDTO.setStatusCode(102);
	      responseDTO.setMsg("您的sign不正确，请检查后再输入！");
	      return responseDTO;
	    }
	    
		CustomerDTO customerDTO = null;
		if (StringUtils.isNotBlank(ticket)) {
			customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
			if (!customerDTO.isEnable()) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(customerDTO.getStatusCode());
				responseDTO.setMsg(customerDTO.getMsg());
				return responseDTO;
			}
		}
	    
		try {
			BigDecimal total = BigDecimal.ZERO;
			int msg = 0;
			List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
			TSponsor tSponsor = null;
			TGoodsSku tGoodsSku = goodsService.getGoodsSkuCache(goodsSkuId);
			TEvent tEvent = eventDAO.findOne(tGoodsSku.getFgoodsId());
			tSponsor = tEvent.getTSponsor();
			CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
			cartGoodsDTO.setGoodsId(tEvent.getId());
			cartGoodsDTO.setGoodsTitle(tEvent.getFtitle());
			cartGoodsDTO.setGoodsSkuId(goodsSkuId);
			cartGoodsDTO.setImageUrl(fxlService.getImageUrl(tGoodsSku.getFimage(), false));
			cartGoodsDTO.setPresentPrice(tGoodsSku.getFpriceMoney().toString());
			cartGoodsDTO.setSpec(goodsService.getGoodsSku(
					tGoodsSku.getFclassTypeValue1()!=null ? tGoodsSku.getFclassTypeValue1():0,
					tGoodsSku.getFclassTypeValue2()!=null ? tGoodsSku.getFclassTypeValue2():0));
			cartGoodsDTO.setCount(num);
			cartGoodsDTO.setOriginalPrice(tGoodsSku.getFprice().toString());
			cartGoodsDTO.setPromotionModel(
					DictionaryUtil.getString(DictionaryUtil.PromotionModel, tEvent.getFpromotionModel()));
			cartGoodsDTO.setLimitation(tGoodsSku.getFlimitation());
			// 先判断商品是否有下架
			Map<String, Object> map = this.checkGoods(tEvent, tGoodsSku, cartGoodsDTO, msg, customerDTO);
			msg = (Integer) map.get("num");
			cartGoodsDTO.setCount((Integer) map.get("count"));
			total = total.add(
					new BigDecimal(cartGoodsDTO.getPresentPrice()).multiply(new BigDecimal(cartGoodsDTO.getCount())))
					.setScale(2, RoundingMode.HALF_UP);
			if (cartGoodsDTO.getCount() > 0) {
				cartGoodsDTOList.add(cartGoodsDTO);
			}

			if (msg == 1) {
				returnData.put("ifChange", true);
				returnData.put("changeMsg", "您购买的部分商品库存不足");
			} else if (msg == 2) {
				returnData.put("ifChange", true);
				returnData.put("changeMsg", "您购买的部分商品限购");
			} else {
				returnData.put("ifChange", false);
				returnData.put("changeMsg", "");
			}

			if (tEvent.getFsellModel().intValue() == 0) {
				TShoppingAddress shoppingAddress = shoppingAddressDAO.findByDefault(customerDTO.getCustomerId());
				if (shoppingAddress != null) {
					returnData.put("sellModel", 0);
					returnData.put("ifAddress", true);
					returnData.put("userName", shoppingAddress.getFname());
					returnData.put("userPhone", shoppingAddress.getFphone());
					returnData.put("address", shoppingAddress.getFaddress());
				} else {
					returnData.put("sellModel", 0);
					returnData.put("ifAddress", false);
					returnData.put("userName", "");
					returnData.put("userPhone", "");
					returnData.put("address", "");
				}
			} else {
				returnData.put("sellModel", 1);
				returnData.put("ifAddress", true);
				returnData.put("userName", tEvent.getTSponsor().getFname());
				returnData.put("userPhone", tEvent.getTSponsor().getFphone());
				returnData.put("address", tEvent.getTSponsor().getFaddress());
			}

			BigDecimal freight = BigDecimal.ZERO;
			if (tSponsor != null) {
				freight = this.getFreight(total, tSponsor.getFpinkage(), tSponsor.getFrange());
			}
			CartDTO cartDTO = new CartDTO();
			cartDTO.setCartGoodsDTOs(cartGoodsDTOList);
			cartDTO.setChangeAmount(new BigDecimal(0));
			cartDTO.setReceivableTotal(total.add(freight));
			cartDTO.setTotal(total);
			cartDTO.setFreight(freight);
			cartDTO.setGoodsTotal(total);
			returnData.put("cartDTO", cartDTO);

			responseDTO.setData(returnData);

			responseDTO.setSuccess(true);
			responseDTO.setMsg("立即购买成功");
			responseDTO.setStatusCode(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseDTO;
	}

	/**
	 * 购物车
	 * 
	 * @param ticket
	 * @param goodsList
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO emptyShoppingcart(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		CustomerDTO customerDTO = null;
		customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			Map<String, Object> returnData = Maps.newHashMap();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			returnData.put("ticket", Lists.newArrayList());
			responseDTO.setData(returnData);
			return responseDTO;
		}
		String shoppingcar = null;
		try {
			shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(shoppingcar)) {
			redisService.removeCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId());
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("清空购物车成功");
		return responseDTO;
	}

	public ResponseDTO deleteShoppingcart(String ticket, Integer clientType, String goodsSkuId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(goodsSkuId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("goodsSkuId参数不能为空，请检查goodsSkuId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = null;
		customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		JavaType jtList = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> goodsSkulist = new ArrayList<>();
		goodsSkulist = mapper.fromJson(goodsSkuId, jtList);
		try {
			String shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
			Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
			if (StringUtils.isNotBlank(shoppingcar)) {
				JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
				goodsMap = mapper.fromJson(shoppingcar, jt);
				for (String id : goodsSkulist) {
					if (goodsMap.containsKey(id)) {
						goodsMap.remove(id);
					}
				}
			}

			if (goodsMap == null || goodsMap.isEmpty()) {
				redisService.removeCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId());
			} else {
				String goodsCache = mapper.toJson(goodsMap);
				redisService.putCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId(), goodsCache);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		responseDTO.setStatusCode(0);
		responseDTO.setSuccess(true);
		responseDTO.setMsg("删除购物车商品成功");
		return responseDTO;
	}

	private Map<String, Object> checkGoods(TEvent tEvent, TGoodsSku tGoodsSku, CartGoodsDTO cartGoodsDTO, int msg, CustomerDTO customerDTO
			) {
		Integer num = cartGoodsDTO.getCount();
		Map<String, Object> map = new HashMap<String, Object>();
		cartGoodsDTO.setCount(num);
		Integer status = tEvent.getFstatus();
		if(tEvent.getFsdealsModel().intValue()==3){
			TColumnBanner banner = columnBannerDAO.findColumnBanner(2, 1);
			TSeckillModule tSeckillModule = seckillModuleDAO.findByGoodsId(tEvent.getId());
			status = tSeckillModule.getFgoodstatus();
			if(banner.getFseckillTime().compareTo(new Date())<=0||tSeckillModule.getFgoodstatus().intValue()!=20){
				status = 90;
			}
		}
		if (status.intValue() != 20) {
			msg = 1;
			cartGoodsDTO.setCount(0);
		} else {
			// 判断商品库存是否还够
			if (tGoodsSku.getFstock() <= 0) {
				msg = 1;
				cartGoodsDTO.setCount(0);
			} else if ((tGoodsSku.getFstock().intValue() - (Integer) num) < 0) {
				msg = 1;
				cartGoodsDTO.setCount(tGoodsSku.getFstock().intValue());
				// 判断商品是否限购
				if (tGoodsSku.getFlimitation().intValue() >= 0) {
					if (tGoodsSku.getFlimitation().intValue() < tGoodsSku.getFstock().intValue()) {
						msg = 2;
						cartGoodsDTO.setCount(tGoodsSku.getFlimitation().intValue());
					}
					Integer count = this.todayOrder(customerDTO.getCustomerId(), tGoodsSku.getId());
					if (count.intValue() > 0) {
						if (tGoodsSku.getFlimitation().intValue() - count < num) {
							msg = 2;
							cartGoodsDTO.setCount(tGoodsSku.getFlimitation().intValue() - count);
							if (tGoodsSku.getFlimitation().intValue() - count < tGoodsSku.getFstock().intValue()) {
								msg = 2;
								cartGoodsDTO.setCount(tGoodsSku.getFstock());
							}
						}
					} else {
						if (tGoodsSku.getFlimitation().intValue() < num.intValue()) {
							msg = 2;
							cartGoodsDTO.setCount(tGoodsSku.getFlimitation());
						}
					}

				}
			} else {
				// 判断商品是否限购
				if (tGoodsSku.getFlimitation() >= 0) {
					Integer count = this.todayOrder(customerDTO.getCustomerId(), tGoodsSku.getId());
					if (count.intValue() > 0) {
						if (tGoodsSku.getFlimitation().intValue() - count < num) {
							msg = 2;
							cartGoodsDTO.setCount(tGoodsSku.getFlimitation().intValue() - count);
							if (tGoodsSku.getFlimitation().intValue() - count > tGoodsSku.getFstock().intValue()) {
								msg = 2;
								cartGoodsDTO.setCount(tGoodsSku.getFstock());
							}
						}
					} else {
						if (tGoodsSku.getFlimitation().intValue() < num.intValue()) {
							msg = 2;
							cartGoodsDTO.setCount(tGoodsSku.getFlimitation());
						}
					}

				}
			}
		}
		map.put("num", msg);
		map.put("count", cartGoodsDTO.getCount());
		return map;
	}

	private Integer todayOrder(String customerId, String goodsSkuId) {
		Integer limition = 0;
		StringBuilder hql = new StringBuilder();
		Date now = new Date();
		hql.append("select o.fcount as count from ").append(" TOrder t left join TOrderGoods o on t.id = o.forderId ")
				.append(" where o.fgoodsSkuId = :goodsSkuId and t.TCustomer.id = :customerId and t.fstatus < 100");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("goodsSkuId", goodsSkuId);
		hqlMap.put("customerId", customerId);
		hql.append(" and t.fcreateTime >= :fcreateTimeStart and t.fcreateTime <= :fcreateTimeEnd");
		hqlMap.put("fcreateTimeStart", DateUtil.getDay(now));
		hqlMap.put("fcreateTimeEnd", now);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> bmap : list) {
				limition += Integer.parseInt(bmap.get("count").toString());
			}
		}
		return limition;
	}
	
	/**
	 * 购物车
	 * 
	 * @param ticket
	 * @param goodsList
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO showGoodsNum(String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		CustomerDTO customerDTO = null;
		customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			Map<String, Object> returnData = Maps.newHashMap();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			returnData.put("ticket", Lists.newArrayList());
			responseDTO.setData(returnData);
			return responseDTO;
		} 
		Map<String, Object> returnData = Maps.newHashMap();
		String shoppingcar = null;
		try {
			shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
		Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
		goodsMap = mapper.fromJson(shoppingcar, jt);

		BigDecimal total = BigDecimal.ZERO;
		int num = 0;
		int msg = 0;
		List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
		TSponsor tSponsor = null;
		if (goodsMap != null) {
			for (Map.Entry entry : goodsMap.entrySet()) {
				String goodsSku = null;
				CartGoodsDTO cartGoodsDTO = (CartGoodsDTO) entry.getValue();
				try {
					goodsSku = redisService.getValue(cartGoodsDTO.getGoodsSkuId(), RedisMoudel.goodsSku);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(StringUtils.isNotBlank(goodsSku)){
					TGoodsSku tGoodsSku = mapper.fromJson(goodsSku, TGoodsSku.class);
					TEvent tEvent = eventDAO.findOne(tGoodsSku.getFgoodsId());
					tSponsor = tEvent.getTSponsor();
					
					cartGoodsDTO.setPresentPrice(tGoodsSku.getFpriceMoney().toString());
					cartGoodsDTO.setOriginalPrice(tGoodsSku.getFprice().toString());
					
					Map<String, Object> map = this.checkGoods(tEvent, tGoodsSku, cartGoodsDTO, msg, customerDTO);
					msg = (Integer) map.get("num");
					cartGoodsDTO.setCount((Integer) map.get("count"));
					total = total.add(new BigDecimal(cartGoodsDTO.getPresentPrice())
							.multiply(new BigDecimal(cartGoodsDTO.getCount()))).setScale(2, RoundingMode.HALF_UP);
					if (cartGoodsDTO.getCount() > 0) {
						num = num +cartGoodsDTO.getCount();
					}
				}else{
					msg = 1;
				}
			}
		}

		returnData.put("num", num);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("成功获取购物车商品数量");
		responseDTO.setData(returnData);
		return responseDTO;
	}
	
	public ResponseDTO checkShoppingcart(String ticket, Integer clientType, String goodsSkuId, Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (type==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("type参数不能为空，请检查type的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = null;
		customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		Map<String, Object> returnData = Maps.newHashMap();
		CartGoodsDTO cartGoodsDTO = null;
		BigDecimal total = BigDecimal.ZERO;
		int msg = 0;
		List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
		TSponsor tSponsor = null;
		TGoodsSku tGoodsSku = null;
		boolean allCheck = true;
		try {
			String shoppingcar = redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar);
			Map<String, CartGoodsDTO> goodsMap = Maps.newHashMap();
			if (StringUtils.isNotBlank(shoppingcar)) {
				JavaType jt = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
				goodsMap = mapper.fromJson(shoppingcar, jt);
				for (Map.Entry entry : goodsMap.entrySet()) {
					cartGoodsDTO = (CartGoodsDTO) entry.getValue();
					tGoodsSku = goodsService.getGoodsSkuCache(cartGoodsDTO.getGoodsSkuId());
					if(type==1){
						if (goodsSkuId.equals(entry.getKey().toString())) {
							cartGoodsDTO.setChecked(true);
						}
					}else if(type==2){
						if (goodsSkuId.equals(entry.getKey().toString())) {
							cartGoodsDTO.setChecked(false);
						}
					}else if(type==3){
						cartGoodsDTO.setChecked(true);
					}else if(type==4){
						cartGoodsDTO.setChecked(false);
					}
					TEvent tEvent = eventDAO.findOne(cartGoodsDTO.getGoodsId());
					tSponsor = tEvent.getTSponsor();
					
					cartGoodsDTO.setPresentPrice(tGoodsSku.getFpriceMoney().toString());
					cartGoodsDTO.setOriginalPrice(tGoodsSku.getFprice().toString());
					
					Map<String, Object> map = this.checkGoods(tEvent, tGoodsSku, cartGoodsDTO, msg, customerDTO);
					msg = (Integer) map.get("num");
					cartGoodsDTO.setCount((Integer) map.get("count"));
					if(cartGoodsDTO.isChecked()){
						total = total.add(new BigDecimal(cartGoodsDTO.getPresentPrice())
								.multiply(new BigDecimal(cartGoodsDTO.getCount()))).setScale(2, RoundingMode.HALF_UP);
					}else{
						allCheck = false;
					}
					if (cartGoodsDTO.getCount() > 0) {
						cartGoodsDTOList.add(cartGoodsDTO);
						goodsMap.put(entry.getKey().toString(), cartGoodsDTO);
					}
				}
			}

			String goodsCache = mapper.toJson(goodsMap);
			redisService.putCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId(), goodsCache);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (msg == 1) {
			returnData.put("ifChange", true);
			returnData.put("changeMsg", "您购买的部分商品库存不足");
		} else if (msg == 2) {
			returnData.put("ifChange", true);
			returnData.put("changeMsg", "您购买的部分商品限购");
		} else {
			returnData.put("ifChange", false);
			returnData.put("changeMsg", "");
		}

		BigDecimal freight = BigDecimal.ZERO;
		BigDecimal freePostage = BigDecimal.ZERO;
		if (tSponsor != null) {
			freight = this.getFreight(total, tSponsor.getFpinkage(), tSponsor.getFrange());
			if(freight.compareTo(BigDecimal.ZERO)>0){
				freePostage = tSponsor.getFpinkage().subtract(total);
			}
			if (cartGoodsDTOList == null || cartGoodsDTOList.size() == 0) {
				freight = BigDecimal.ZERO;
			}
		}
		CartDTO cartDTO = new CartDTO();
		cartDTO.setCartGoodsDTOs(cartGoodsDTOList);
		cartDTO.setChangeAmount(new BigDecimal(0));
		cartDTO.setReceivableTotal(total.add(freight));
		cartDTO.setTotal(total);
		cartDTO.setFreight(freight);
		cartDTO.setGoodsTotal(total);
		cartDTO.setFreePostage(freePostage.toString());
		returnData.put("cartDTO", cartDTO);
		returnData.put("allCheck", allCheck);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		responseDTO.setMsg("选中购物车商品成功");
		return responseDTO;
	}

}