package com.innee.czyhInterface.service.v1.eventBargaining;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.RequestUrl;
import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.dao.CustomerBargainingDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.OrderAmountChangeDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.OrderGoodsDAO;
import com.innee.czyhInterface.dao.ShoppingAddressDAO;
import com.innee.czyhInterface.dao.TimingTaskDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.OrderRecipientDTO;
import com.innee.czyhInterface.dto.m.CartGoodsDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.goods.CartDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerBargaining;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TOrderAmountChange;
import com.innee.czyhInterface.entity.TOrderGoods;
import com.innee.czyhInterface.entity.TShoppingAddress;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TTimingTask;
import com.innee.czyhInterface.impl.bargainImpl.EventBargainsService;
import com.innee.czyhInterface.impl.couponImpl.CouponsService;
import com.innee.czyhInterface.service.v1.category.CategoryService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.ConfigurationUtil;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.NumberUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderSendSmsBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderUpdateCustomerInfoBean;

/**
 * 砍一砍service
 * 
 * @author jinshengzhi
 *
 */
@Component
@Transactional
public class EventBargainingService {

	private static final Logger logger = LoggerFactory.getLogger(EventBargainingService.class);

	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();

	@Autowired
	private CommonService commonService;

	@Autowired
	private EventBargainingDAO eventBargainingDAO;

	@Autowired
	private CustomerBargainingDAO customerBargainingDAO;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private OrderGoodsDAO orderGoodsDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private TimingTaskDAO timingTaskDAO;

	@Autowired
	private OrderAmountChangeDAO orderAmountChangeDAO;

	@Autowired
	private ShoppingAddressDAO shoppingAddressDAO;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private EventBargainsService eventBargainsService;

	@Transactional(readOnly = true)
	public ResponseDTO eventBargainingList(Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		responseDTO = eventBargainsService.eventBargainingList(page.getPageSize(), page.getOffset());
		return responseDTO;
	}

	public ResponseDTO getMyBargain(String eventBargainingId, String ticket, Integer clientType,
			String customerBargainingId) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		responseDTO = eventBargainsService.getMyBargain(eventBargainingId, customerId, customerBargainingId);
		return responseDTO;
	}

	public ResponseDTO helpBargain(String ticket, Integer clientType, String customerBargainingId,String sign, String addressIP) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		/**
		 * 加密
		 */
		if(StringUtils.isBlank(sign)){
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
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
		/******/
		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		responseDTO = eventBargainsService.helpBargain(customerId, customerBargainingId);
		return responseDTO;
	}

	/**
	 * 砍价英雄榜
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getbargainList(String ticket,  String eventBargainingId,
			Integer pageSize, Integer offset, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventBargainingId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("eventBargainingId参数不能为空，请检查eventBargainingId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		responseDTO = eventBargainsService.getbargainList(customerId, eventBargainingId, page.getPageSize(), page.getOffset());
		return responseDTO;
	}

	/**
	 * 帮砍列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getbargainHelpList(String ticket, String customerBargainingId, Integer pageSize, Integer offset,
			Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(customerBargainingId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("customerBargainingId参数不能为空，请检查customerBargainingId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		responseDTO = eventBargainsService.getbargainHelpList(customerId, customerBargainingId, page.getPageSize(), page.getOffset());
		return responseDTO;
	}

	/**
	 * 弹幕列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getbarrage(String ticket, String customerBargainingId, Integer pageSize, Integer offset,
			Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(customerBargainingId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("customerBargainingId参数不能为空，请检查customerBargainingId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		responseDTO = eventBargainsService.getbarrage(customerId, customerBargainingId, page.getPageSize(), page.getOffset());
		
		return responseDTO;
	}

	/**
	 * 分享当前砍一砍
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getbargainShare(String customerBargainingId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(customerBargainingId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("customerBargainingId参数不能为空，请检查customerBargainingId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> shareBargaining = new HashMap<String, Object>();
		TCustomerBargaining tCustomerBargaining = customerBargainingDAO.getOne(customerBargainingId);
		TEventBargaining tEventBargaining = eventBargainingDAO.getOne(tCustomerBargaining.getFbargainingId());
		TEvent event = eventDAO.findOne(tEventBargaining.getFeventId());
		// String url =
		// fxlService.getImageUrl(String.valueOf(tEventBargaining.getFimage()),
		// true);

		shareBargaining.put("title", "我要你砍他！");
		shareBargaining.put("brief", "我正在[查找优惠]参与["+tEventBargaining.getFinputText()+"]火拼砍至0元活动！快来帮我砍呀~");
		shareBargaining.put("imageUrl", fxlService.getImageUrl(event.getFimage1().toString(), false));
		StringBuilder shareUrl = new StringBuilder();
		shareUrl.append(request.getScheme()).append("://").append(request.getServerName()).append(":")
				.append(request.getServerPort()).append(request.getContextPath())
				.append("/api/system/share/shareBargain/").append(tCustomerBargaining.getFbargainingId()).append("/")
				.append(customerBargainingId);
		shareBargaining.put("url", shareUrl.toString());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", shareBargaining);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO bargainTFillOrder(Integer clientType, String ticket, String customerBargainingId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(customerBargainingId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("customerBargainingId参数不能为空，请检查customerBargainingId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		TCustomerBargaining tCustomerBargaining = customerBargainingDAO.findOne(customerBargainingId);
		TEventBargaining tEventBargaining = eventBargainingDAO.findOne(tCustomerBargaining.getFbargainingId());
		if (tEventBargaining == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO
					.setMsg("您输入的customerBargainingId参数有误，customerBargainingId=“" + customerBargainingId + "”的活动不存在！");
			return responseDTO;
		}
		if (tEventBargaining.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("该砍一砍活动已结束");
			return responseDTO;
		}
		// 限购规则器判断是否可以购买
		int count = 0;
		if (tCustomerBargaining.getFdefaultLevel() == 1) {
			count = tEventBargaining.getFremainingStock1();
		} else if (tCustomerBargaining.getFdefaultLevel() == 2) {
			count = tEventBargaining.getFremainingStock2();
		} else if (tCustomerBargaining.getFdefaultLevel() == 3) {
			count = tEventBargaining.getFremainingStock3();
		}
		if (count <= 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("本活动已被抢完，请留意参加抢购其它活动！");
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(tEventBargaining.getFeventId());
		BigDecimal total = tEventBargaining.getFstartPrice().setScale(2, RoundingMode.HALF_UP);
		BigDecimal maxAmount = BigDecimal.ZERO;
		// 获取出该订单可使用最优优惠券
		maxAmount = tCustomerBargaining.getFstartPrice().subtract(tCustomerBargaining.getFendPrice());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		List<CartGoodsDTO> cartGoodsDTOList = Lists.newArrayList();
		CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
		cartGoodsDTO.setGoodsId(tCustomerBargaining.getFbargainingId());
		cartGoodsDTO.setGoodsTitle(tEventBargaining.getFtitle());
		cartGoodsDTO.setImageUrl(fxlService.getImageUrl(tEvent.getFimage1().toString(), false));
		cartGoodsDTO.setPresentPrice(tCustomerBargaining.getFendPrice().toString());
		cartGoodsDTO.setSpec(tEvent.getFspec());
		cartGoodsDTO.setCount(1);
		cartGoodsDTO.setOriginalPrice(tCustomerBargaining.getFstartPrice().toString());
		cartGoodsDTO.setPromotionModel("无促销");
		cartGoodsDTO.setLimitation(-1);
		// 先判断商品是否有下架
		cartGoodsDTOList.add(cartGoodsDTO);
	
		returnData.put("ifChange", false);
		returnData.put("changeMsg", "");
		

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

		String orderRange = ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_ORDER_RANGE);
		String transportationRange = ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_TRANSPORTATION_RANGE);
		BigDecimal freight = BigDecimal.ZERO;
		if(new BigDecimal(orderRange).compareTo(total)<=0){
			freight = new BigDecimal(transportationRange);
		}
		CartDTO cartDTO = new CartDTO();
		cartDTO.setCartGoodsDTOs(cartGoodsDTOList);
		cartDTO.setChangeAmount(maxAmount);
		cartDTO.setReceivableTotal(total.add(freight).subtract(maxAmount));
		cartDTO.setTotal(total.add(freight).subtract(maxAmount));
		cartDTO.setFreight(freight);
		cartDTO.setGoodsTotal(total.subtract(maxAmount));
		returnData.put("cartDTO", cartDTO);

		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 翻译接口
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getGamesType(String eventId) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> saleTypeMap = new HashMap<String, Object>();

		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}

		TEvent tEvent = eventDAO.getOne(eventId);
		if (tEvent.getFsalesType().intValue() == 1) {
			TEventBargaining tEventBargaining = eventBargainingDAO.getByEventId(eventId);
			if (tEventBargaining != null && tEventBargaining.getFstatus().intValue() == 20) {
				saleTypeMap.put("saleType", "1");
				saleTypeMap.put("saleTitle", DictionaryUtil.getCode(DictionaryUtil.EventSalesType, 1));
				saleTypeMap.put("eventBargainingId", tEventBargaining.getId());
			} else {
				saleTypeMap.put("saleType", StringUtils.EMPTY);
			}
		} else {
			saleTypeMap.put("saleType", StringUtils.EMPTY);
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("saleTypeMap", saleTypeMap);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO bargainToPayOrder(Integer clientType, String ticket, Integer payClientType, String addressId,
			String remark, Integer payType, String ip, Integer channel, String gps, String customerBargainingId,
			String deviceId,String sign) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (payType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("payType参数不能为空，请检查payType的传递参数值！");
			return responseDTO;
		}
		/**
		 * 加密
		 */
		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("sign参数不能为空，请检查sign的传递参数值！");
			return responseDTO;
		}
		
		String signStr = fxlService.httpEncrypt(ip, clientType);
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的sign不正确，请检查后再输入！");
			return responseDTO;
		}
		/****************************/
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		// 限购规则器判断是否可以购买
		TCustomerBargaining tCustomerBargaining = customerBargainingDAO.findOne(customerBargainingId);
		TEventBargaining tEventBargaining = eventBargainingDAO.findOne(tCustomerBargaining.getFbargainingId());
		if (tEventBargaining.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("该砍一砍活动已结束");
			return responseDTO;
		}
		int count = 0;
		if (tCustomerBargaining.getFdefaultLevel() == 1) {
			count = tEventBargaining.getFremainingStock1();
		} else if (tCustomerBargaining.getFdefaultLevel() == 2) {
			count = tEventBargaining.getFremainingStock2();
		} else if (tCustomerBargaining.getFdefaultLevel() == 3) {
			count = tEventBargaining.getFremainingStock3();
		}
		if (count <= 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("亲，该商品已经被别人抢光啦，后续我们还会推出更多的好玩活动，敬请关注~！");
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(tEventBargaining.getFeventId());

		String orderNum = NumberUtil.getOrderNum(tEvent.getFcity() != null ? tEvent.getFcity() : 0);
		TSponsor tSponsor = tEvent.getTSponsor();
		TOrder tOrder = new TOrder();

		tOrder.setTEvent(tEvent);
		tOrder.setFcityId(tEvent.getFcity());
		tOrder.setFeventTitle(tEvent.getFtitle());
		// 获取到活动类目缓存对象
		String type = categoryService.getCategoryA(tEvent.getFtypeA());
		tOrder.setFtypeA(type != null ? type : StringUtils.EMPTY);
		tOrder.setTSponsor(tSponsor);
		tOrder.setFsponsorName(tSponsor.getFname());
		tOrder.setFsponsorFullName(tSponsor.getFfullName());
		tOrder.setFsponsorPhone(tSponsor.getFphone());
		tOrder.setFsponsorNumber(tSponsor.getFnumber());
		tOrder.setForderNum(orderNum);
		tOrder.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		tOrder.setFcustomerName(customerDTO.getName());
		if (StringUtils.isNotBlank(customerDTO.getPhone())) {
			tOrder.setFcustomerPhone(customerDTO.getPhone());
		} else {
			tOrder.setFcustomerPhone(customerDTO.getPhone());
		}
		tOrder.setFcustomerSex(DictionaryUtil.getString(DictionaryUtil.Sex, customerDTO.getSex()));
		BigDecimal total = tEventBargaining.getFstartPrice().setScale(2, RoundingMode.HALF_UP);
		tOrder.setFtotal(total);
		tOrder.setFreceivableTotal(total);
		tOrder.setFremark(remark);
		TShoppingAddress shoppingAddress = null;
		if (StringUtils.isNotBlank(addressId)) {
			shoppingAddress = shoppingAddressDAO.findOne(addressId);
		} else {
			shoppingAddress = shoppingAddressDAO.findByDefault(customerDTO.getCustomerId());
		}

		OrderRecipientDTO recipientDTO = new OrderRecipientDTO();
		recipientDTO.setRecipient(shoppingAddress.getFname());
		recipientDTO.setPhone(shoppingAddress.getFphone());
		recipientDTO.setAddress(shoppingAddress.getFaddress());
		String recipientJson = mapper.toJson(recipientDTO);
		tOrder.setFrecipient(recipientJson);
		Date now = new Date();
		tOrder.setFcreateTime(now);
		Date fdate = null;
		if (Constant.getUnPayFailureMinute() != 0) {
			fdate = DateUtils.addMinutes(now, 5);
			tOrder.setFunPayFailureTime(fdate);
		}
		tOrder.setFlockFlag(0);

		tOrder.setFchannel(channel);
		tOrder.setFgps(gps);
		tOrder.setFdeviceId(deviceId);
		tOrder.setFsource(20);
		tOrder.setFstatus(10);
		tOrder = orderDAO.save(tOrder);

		TOrderGoods tOrderGoods = new TOrderGoods();
		tOrderGoods.setFcreateTime(now);
		tOrderGoods.setFeventId(tEvent.getId());
		tOrderGoods.setFeventTitle(tEvent.getFtitle());
		tOrderGoods.setForderId(tOrder.getId());
		tOrderGoods.setFprice(tEvent.getFpriceMoney());
		tOrderGoods.setFspec(tEvent.getFspec());
		tOrderGoods.setFpromotionModel(tEvent.getFpromotionModel());
		tOrderGoods.setFgoodsSubTitle(tEvent.getFsubTitle());
		tOrderGoods.setFcount(1);
		tOrderGoods.setFtotalPrice(tOrderGoods.getFprice().multiply(new BigDecimal(tOrderGoods.getFcount())));
		tOrderGoods = orderGoodsDAO.save(tOrderGoods);
		// 修改砍一砍
		tCustomerBargaining.setFstatus(30);// 砍价已下单
		tCustomerBargaining.setForderId(tOrder.getId());
		tCustomerBargaining.setForderTime(tOrder.getFcreateTime());
		customerBargainingDAO.save(tCustomerBargaining);
		// 修改砍一砍活动库存
		if (tCustomerBargaining.getFdefaultLevel() == 1) {
			tEventBargaining.setFremainingStock1(tEventBargaining.getFremainingStock1() - 1);
		} else if (tCustomerBargaining.getFdefaultLevel() == 2) {
			tEventBargaining.setFremainingStock2(tEventBargaining.getFremainingStock2() - 1);
		} else if (tCustomerBargaining.getFdefaultLevel() == 3) {
			tEventBargaining.setFremainingStock3(tEventBargaining.getFremainingStock3() - 1);
		}
		tEventBargaining = eventBargainingDAO.save(tEventBargaining);
		
		String orderRange = ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_ORDER_RANGE);
		String transportationRange = ConfigurationUtil
				.getPropertiesValue(ResponseConfigurationDict.RESPONSE_TRANSPORTATION_RANGE);
		BigDecimal freight = BigDecimal.ZERO;
		if(new BigDecimal(orderRange).compareTo(total)<=0){
			freight = new BigDecimal(transportationRange);
		}
		tOrder.setFpostage(freight);

		// 如果用户选择了优惠券进行付款，则进行优惠券抵扣
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());

		BigDecimal maxAmount = BigDecimal.ZERO;
		// 获取出该订单可使用最优优惠券
		maxAmount = tCustomerBargaining.getFstartPrice().subtract(tCustomerBargaining.getFendPrice());
		total = tOrder.getFreceivableTotal().subtract(maxAmount).add(freight);
		if (total.compareTo(BigDecimal.ZERO) < 0) {
			total = BigDecimal.ZERO;
		}
		tOrder.setFgoodsImage(tEvent.getFimage1());
		tOrder.setFtotal(total);
		tOrder.setFsellModel(tEvent.getFsellModel());
		tOrder.setFchangeAmount(maxAmount);
		tOrder.setFchangeAmountInstruction("参加砍一砍活动抵扣了" + maxAmount.toString() + "元");
		tOrder = orderDAO.save(tOrder);

		// 保存优惠金额
		TOrderAmountChange tOrderAmountChange = new TOrderAmountChange();
		tOrderAmountChange.setFbonusChange(0);
		tOrderAmountChange.setFbargainChange(maxAmount);
		tOrderAmountChange.setFcouponChange(BigDecimal.ZERO);
		tOrderAmountChange.setFcreateTime(tOrder.getFcreateTime());
		tOrderAmountChange.setForderId(tOrder.getId());
		tOrderAmountChange.setFotherChange(BigDecimal.ZERO);
		tOrderAmountChange.setFspellChange(BigDecimal.ZERO);
		orderAmountChangeDAO.save(tOrderAmountChange);

		Map<String, Object> returnData = Maps.newHashMap();
		// 如果订单是则返回支付成功信息，如果是非零元单则返回支付信息
		if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
			// 如果是零元单，则直接记录订单状态变更为已支付状态
			fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 20);
			// 变更订单状态为已支付和支付类型是零元单支付
			orderDAO.updateOrderStatusAndPayType(20, 90,clientType, tOrder.getId());
			// 更改用户附加信息表
			OrderUpdateCustomerInfoBean ouci = new OrderUpdateCustomerInfoBean();
			ouci.setCreateTime(now);
			ouci.setCustomerId(customer.getId());
			ouci.setOrderId(tOrder.getId());
			ouci.setTotal(tOrder.getFtotal());
			ouci.setTaskType(2);
			AsynchronousTasksManager.put(ouci);

			/// 添加线程任务发送购买成功通知短信
			OrderSendSmsBean oss = new OrderSendSmsBean();
			oss.setCreateTime(now);
			oss.setCustomerId(customerDTO.getCustomerId());
			oss.setOrderId(tOrder.getId());
			oss.setOrderNum(tOrder.getForderNum());
			oss.setTaskType(3);
			AsynchronousTasksManager.put(oss);

			// 修改用户砍一砍状态
			tCustomerBargaining.setFstatus(40);// 砍价已支付
			tCustomerBargaining.setFpayTime(tOrder.getFpayTime());
			customerBargainingDAO.save(tCustomerBargaining);

			returnData.put("zero", true);
			returnData.put("orderId", tOrder.getId());
			responseDTO.setMsg("订单支付成功！");
		} else {
			// 如果是非零元单，则记录订单状态变更为未支付状态
			fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 10);
			// 变更订单状态为待支付和支付类型是相应支付类型
			orderDAO.updateOrderStatusAndPayType(10, payType,clientType, tOrder.getId());
			// 将添加订单超时未支付取消定时任务
			if (fdate != null) {
				TTimingTask timingTask = new TTimingTask();
				timingTask.setEntityId(tOrder.getId());
				timingTask.setTaskTime(fdate.getTime());
				timingTask.setTaskType(7);
				timingTaskDAO.save(timingTask);
			}

			returnData.put("zero", false);
			returnData.put("orderId", tOrder.getId());
			responseDTO.setMsg("订单已生成，快去支付吧！");
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 分享当前砍一砍
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getbargainShareApp(String goodsBargainingId, String ticket, Integer clientType,
			HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(goodsBargainingId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(107);
			responseDTO.setMsg("goodsBargainingId参数不能为空，请检查goodsBargainingId的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		ResponseDTO dto = new ResponseDTO();
		dto = eventBargainsService.getMyBargain(goodsBargainingId, customerId, null);
		

		Map<String, Object> shareBargaining = new HashMap<String, Object>();
		TEventBargaining tEventBargaining = eventBargainingDAO.getOne(goodsBargainingId);
		TEvent event = eventDAO.findOne(tEventBargaining.getFeventId());

		shareBargaining.put("title", "我要你砍他！");
		shareBargaining.put("brief", "我正在[查找优惠]参与["+tEventBargaining.getFinputText()+"]火拼砍至0元活动！快来帮我砍呀~");
		shareBargaining.put("imageUrl", fxlService.getImageUrl(event.getFimage1().toString(), false));
		StringBuilder shareUrl = new StringBuilder();
		shareUrl.append(request.getScheme()).append("://").append(request.getServerName()).append(":")
				.append(request.getServerPort()).append(request.getContextPath())
				.append("/api/system/share/shareBargain/").append(goodsBargainingId).append("/")
				.append(dto.getData().get("customerBargainingId"));
		shareBargaining.put("url", shareUrl.toString());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appShare", shareBargaining);
		responseDTO.setData(returnData);
		return responseDTO;
	}
	
	/**
	 * 帮砍列表
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getbargaining(String ticket, Integer pageSize, Integer offset,
			Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		responseDTO = eventBargainsService.getbargaining(customerId, page.getPageSize(), page.getOffset());
		return responseDTO;
	}
}