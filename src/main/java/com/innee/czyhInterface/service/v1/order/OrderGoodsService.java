package com.innee.czyhInterface.service.v1.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Cryptos;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.github.cuter44.wxpay.resps.Notify;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.RequestUrl;
import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.dao.ColumnBannerDAO;
import com.innee.czyhInterface.dao.CouponDeliveryDAO;
import com.innee.czyhInterface.dao.CouponInformationDAO;
import com.innee.czyhInterface.dao.CustomerBargainingDAO;
import com.innee.czyhInterface.dao.CustomerBonusDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerLevelDAO;
import com.innee.czyhInterface.dao.CustomerSubscribeDAO;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.ExpressDAO;
import com.innee.czyhInterface.dao.GoodsSkuDAO;
import com.innee.czyhInterface.dao.OrderAmountChangeDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.OrderGoodsDAO;
import com.innee.czyhInterface.dao.OrderRefundDAO;
import com.innee.czyhInterface.dao.OrderStatusChangeDAO;
import com.innee.czyhInterface.dao.PayInfoDAO;
import com.innee.czyhInterface.dao.SeckillModuleDAO;
import com.innee.czyhInterface.dao.ShoppingAddressDAO;
import com.innee.czyhInterface.dao.TimingTaskDAO;
import com.innee.czyhInterface.dao.WxPayDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.OrderRecipientDTO;
import com.innee.czyhInterface.dto.m.CartGoodsDTO;
import com.innee.czyhInterface.dto.m.OrderDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.PayDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.express.ExpressCompamyDTO;
import com.innee.czyhInterface.dto.m.express.ExpressDTO;
import com.innee.czyhInterface.dto.m.goods.CommentGoodsDTO;
import com.innee.czyhInterface.dto.m.order.OrderGoodsDetailDTO;
import com.innee.czyhInterface.dto.m.order.OrderInfoDTO;
import com.innee.czyhInterface.dto.m.order.RefundDetailDTO;
import com.innee.czyhInterface.dto.m.order.RefundInfoDTO;
import com.innee.czyhInterface.dto.m.order.RefundReasonDTO;
import com.innee.czyhInterface.dto.m.order.ViewOrderDTO;
import com.innee.czyhInterface.entity.TColumnBanner;
import com.innee.czyhInterface.entity.TCouponDelivery;
import com.innee.czyhInterface.entity.TCouponInformation;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerBargaining;
import com.innee.czyhInterface.entity.TCustomerBonus;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerLevel;
import com.innee.czyhInterface.entity.TCustomerSubscribe;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TExpress;
import com.innee.czyhInterface.entity.TGoodsSku;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TOrderAmountChange;
import com.innee.czyhInterface.entity.TOrderGoods;
import com.innee.czyhInterface.entity.TOrderRefund;
import com.innee.czyhInterface.entity.TOrderStatusChange;
import com.innee.czyhInterface.entity.TPayInfo;
import com.innee.czyhInterface.entity.TSeckillModule;
import com.innee.czyhInterface.entity.TShoppingAddress;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TTimingTask;
import com.innee.czyhInterface.entity.TWxPay;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.impl.couponImpl.CouponsService;
import com.innee.czyhInterface.service.v1.goods.GoodsService;
import com.innee.czyhInterface.service.v1.push.PushService;
import com.innee.czyhInterface.service.v1.system.ChargeService;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.CouponService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.ConfigurationUtil;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DateUtil;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.NumberUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderSendSmsBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.OrderUpdateCustomerInfoBean;
import com.innee.czyhInterface.util.bmap.BmapUtil;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;
import com.innee.czyhInterface.util.express.Express;
import com.innee.czyhInterface.util.express.ExpressUtil;
import com.innee.czyhInterface.util.log.OutPutLogUtil;
import com.innee.czyhInterface.util.pingPay.bean.ChargeParam;
import com.innee.czyhInterface.util.pingPay.util.PingPPUtil;
import com.innee.czyhInterface.util.redis.RedisMoudel;
import com.innee.czyhInterface.util.sms.SmsUtil;
import com.innee.czyhInterface.util.wx.WxPayResult;
import com.innee.czyhInterface.util.wx.WxPayUtil;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Refund;
import com.pingplusplus.model.Webhooks;

import ch.qos.logback.core.encoder.ByteArrayUtil;

/**
 * 订单业务管理类.
 * 
 * @author jinshenzhi
 */
@Component("OrderGoodsServiceV1")
@Transactional
public class OrderGoodsService {

	private static final Logger logger = LoggerFactory.getLogger(OrderGoodsService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private OrderGoodsDAO orderGoodsDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private TimingTaskDAO timingTaskDAO;

	@Autowired
	private WxPayDAO wxPayDAO;

	@Autowired
	private ExpressDAO expressDAO;

	@Autowired
	private ShoppingAddressDAO shoppingAddressDAO;

	@Autowired
	private RedisService redisService;

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private CouponDeliveryDAO couponDeliveryDAO;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponInformationDAO couponInformationDAO;

	@Autowired
	private OrderAmountChangeDAO orderAmountChangeDAO;

	@Autowired
	private CustomerSubscribeDAO customerSubscribeDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private CustomerLevelDAO customerLevelDAO;

	@Autowired
	private CustomerBonusDAO customerBonusDAO;

	@Autowired
	private PayInfoDAO payInfoDAO;

	@Autowired
	private PushService pushService;
	
	@Autowired
	private CustomerBargainingDAO customerBargainingDAO;
	
	@Autowired
	private EventBargainingDAO eventBargainingDAO;
	
	@Autowired
	private OrderStatusChangeDAO orderStatusChangeDAO;
	
	@Autowired
	private SeckillModuleDAO seckillModuleDAO;
	
	@Autowired
	private ColumnBannerDAO columnBannerDAO;
	
	@Autowired
	private OrderRefundDAO orderRefundDAO;
	
	@Autowired
	private GoodsSkuDAO goodsSkuDAO;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private CouponsService couponsService;

	@Transactional(readOnly = true)
	public void initSerial() {
		String orderNum = orderDAO.getMaxOrderNum(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH));
		if (orderNum == null) {
			NumberUtil.setSerial(0L);
		} else {
			NumberUtil.setSerial(Long.valueOf(orderNum.substring(6, 11)));
		}
	}

	@Transactional(readOnly = true)
	public ResponseDTO getOrderList(Integer clientType, String ticket, Integer status,
			Integer source,Integer pageSize, Integer offset) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (status == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("status参数不能为空，请检查status的传递参数值！");
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

		List<OrderDTO> orderList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id ,t.TSponsor.fphone as sponsorPhone,t.TSponsor.fname as sponsorName,t.TSponsor.fsponsorModel as fsponsorModel, t.forderNum as forderNum, t.fprice as fprice, t.fcount as fcount, t.fpostage as fpostage, t.freceivableTotal as freceivableTotal, t.ftotal as ftotal, t.fchangeAmount as fchangeAmount, t.fchangeAmountInstruction as fchangeAmountInstruction, t.fsponsorNumber as fsponsorNumber, t.forderType as forderType, t.fappointment as fappointment, t.freturn as freturn, t.fstatus as fstatus, t.fusePreferential as fusePreferential, t.fverificationType as fverificationType, t.frecipient as frecipient ,t.fgoodsImage as fgoodsImage,t.fsellModel as fsellModel from TOrder t where t.TCustomer.id = :customerId and t.fstatus < 999");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		if (source!=null) {
			hql.append(" and t.fsource = :source");// 0.自营
			hqlMap.put("source", source);
		}else{
			hql.append(" and t.fsource != 20");// 0.自营
		}
		if (status.intValue() == 0) {
			hql.append(" and t.fstatus < 900 order by t.fcreateTime desc");
		} else if (status.intValue() == 110) {
			hql.append(" and (t.fstatus between 110 and 119) order by t.frefundTime desc");
		} else if (status.intValue() == 10) {
			hql.append(" and (t.fstatus between 10 and 19) order by t.fcreateTime desc");
		} else if (status.intValue() == 20) {
			hql.append(" and (t.fstatus between 20 and 29) order by t.fcreateTime desc");
		} else if (status.intValue() == 30) {
			hql.append(" and (t.fstatus between 30 and 59) order by t.fcreateTime desc");
		} else if (status.intValue() == 60) {
			hql.append(" and t.fstatus = 60 order by t.fverificationTime desc");
		} else if (status.intValue() == 70) {
			hql.append(" and t.fstatus = 70 order by t.fverificationTime desc");
		} else {
			hql.append(" and t.fstatus = :status order by t.fcreateTime desc");
			hqlMap.put("status", status);
		}

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		OrderDTO orderDTO = null;
		int statusValue = 0;
		for (Map<String, Object> amap : list) {
			orderDTO = new OrderDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				orderDTO.setOrderId(amap.get("id").toString());
			}
			if (amap.get("forderNum") != null && StringUtils.isNotBlank(amap.get("forderNum").toString())) {
				orderDTO.setOrderNum(amap.get("forderNum").toString());
				if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
					statusValue = ((Integer) amap.get("fstatus")).intValue();
					orderDTO.setStatus(statusValue);
					if (statusValue == 109) {
						orderDTO.setStatusString("超时取消");
					} else if (statusValue == 20 && ((Integer) amap.get("fsellModel")).intValue() == 1) {
						orderDTO.setStatusString("待消费");
					} else {
						orderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, statusValue));
					}
				}
			}

			if (amap.get("ftotal") != null && StringUtils.isNotBlank(amap.get("ftotal").toString())) {
				orderDTO.setTotal((BigDecimal) amap.get("ftotal"));
				orderDTO.setRefundAmount(((BigDecimal) amap.get("ftotal")).subtract((BigDecimal) amap.get("fpostage")));
			}

			if (amap.get("forderType") != null && StringUtils.isNotBlank(amap.get("forderType").toString())) {
				orderDTO.setOrderType((Integer) amap.get("forderType"));
			}
			if (amap.get("sponsorPhone") != null && StringUtils.isNotBlank(amap.get("sponsorPhone").toString())) {
				orderDTO.setSponsorPhone(amap.get("sponsorPhone").toString());
			}
			if (amap.get("sponsorName") != null && StringUtils.isNotBlank(amap.get("sponsorName").toString())) {
				orderDTO.setSponsorName(amap.get("sponsorName").toString());
			}
			if (amap.get("fsellModel") != null && StringUtils.isNotBlank(amap.get("fsellModel").toString())) {
				orderDTO.setSellModel((Integer) amap.get("fsellModel"));
			}
			if (amap.get("fsponsorModel") != null && StringUtils.isNotBlank(amap.get("fsponsorModel").toString())) {
				orderDTO.setSponsorModel((Integer) amap.get("fsponsorModel"));
			}
			if (amap.get("fgoodsImage") != null && StringUtils.isNotBlank(amap.get("fgoodsImage").toString())) {
				// 返回商品图片信息多个商品图片
				String[] imagesGoods = null;
				imagesGoods = fxlService.getImageUrls(amap.get("fgoodsImage").toString(), false);
				orderDTO.setGoodsImageUrl(imagesGoods);
				orderDTO.setTotalNum(imagesGoods.length);
			}
			if (orderDTO.getTotalNum() == 1) {
				List<TOrderGoods> orderGoodsList = orderGoodsDAO.findByOrderId(amap.get("id").toString());
				TOrderGoods orderGoods = orderGoodsList.get(0);
				orderDTO.setGoodsId(orderGoods.getFeventId());
				orderDTO.setGoodsTitle(orderGoods.getFeventTitle());
				orderDTO.setGoodsSubTitle(orderGoods.getFgoodsSubTitle());
				orderDTO.setGoodsPrice(orderGoods.getFprice().toString());
			}

			if (amap.get("fpostage") != null && StringUtils.isNotBlank(amap.get("fpostage").toString())) {
				orderDTO.setPostage((BigDecimal) amap.get("fpostage"));
			}

			orderList.add(orderDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderList", orderList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户订单列表加载成功！");
		return responseDTO;
	}

	public ResponseDTO viewOrder(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
		viewOrderDTO.setOrderId(tOrder.getId());
		viewOrderDTO.setSource(tOrder.getFsource());
		viewOrderDTO.setSellModel(tOrder.getFsellModel());
		// 订单状态 订单编号信息
		viewOrderDTO.setOrderNum(tOrder.getForderNum());
		Integer statusValue = tOrder.getFstatus();
		viewOrderDTO.setStatus(statusValue);
		if (statusValue == 109) {
			viewOrderDTO.setStatusString("超时取消");
		} else if (statusValue == 20 && tOrder.getFsellModel().intValue() == 1) {
			viewOrderDTO.setStatusString("待提货");
		} else {
			viewOrderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, statusValue));
		}
		// 收货人信息
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, String.class);
		OrderRecipientDTO recipientDTO = null;
		if (tOrder.getFrecipient() != null) {
			recipientDTO = mapper.fromJson(tOrder.getFrecipient(), OrderRecipientDTO.class);
			if (recipientDTO.getRecipient() != null) {
				viewOrderDTO.setName(recipientDTO.getRecipient());
				if (recipientDTO.getAddress() != null) {
					viewOrderDTO.setAddress(recipientDTO.getAddress());
				}
				if (recipientDTO.getPhone() != null) {
					String str = recipientDTO.getPhone();
					viewOrderDTO.setPhone(
							str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7));
				}
			}
		}
		// 订单总体信息
		if (tOrder.getForderType() != null) {
			viewOrderDTO.setOrderType(tOrder.getForderType());
		}
		if (tOrder.getFpayType() != null) {
			viewOrderDTO.setPayType(tOrder.getFpayType());
		}
		viewOrderDTO.setSellModel(tOrder.getFsellModel());
		if (tOrder.getFpromotionModel() != null) {
			viewOrderDTO.setPromotionModel(tOrder.getFpromotionModel());
		}
		viewOrderDTO.setPostage(tOrder.getFpostage());
		viewOrderDTO.setTotal(tOrder.getFtotal());
		viewOrderDTO.setSponsorName(tOrder.getTSponsor().getFname());
		viewOrderDTO.setSponsorPhone(tOrder.getTSponsor().getFphone());
		viewOrderDTO.setSponsorAddress(tOrder.getTSponsor().getFaddress());
		if(tOrder.getFstatus()<=10){
			Integer time = Seconds.secondsBetween(new DateTime(new Date()), new DateTime
					(DateUtils.addMinutes(tOrder.getFcreateTime(), 30))).getSeconds();
			if(time<=0){
				time = 0;
			}
			viewOrderDTO.setCancelTime(time.toString());
		}
		if (tOrder.getTSponsor().getFgps() != null) {
			viewOrderDTO.setSponsorGps(BmapUtil.baiduToGaoDe(tOrder.getTSponsor().getFgps()));
		}
		viewOrderDTO.setOrderTime(DateFormatUtils.format(tOrder.getFcreateTime(), "yyyy-MM-dd HH:mm"));
		if (tOrder.getFpayTime() != null) {
			viewOrderDTO.setPayTime(DateFormatUtils.format(tOrder.getFpayTime(), "yyyy-MM-dd HH:mm"));
		}
		if (tOrder.getFspec() != null) {
			viewOrderDTO.setSpec(tOrder.getFspec());
		}
		if (tOrder.getFchangeAmount() != null) {
			viewOrderDTO.setChangeAmount(tOrder.getFchangeAmount().toString());
		}

		// 物流信息 查询物流信息
		if (viewOrderDTO.getSellModel() == 0) {
			TExpress tExpress = expressDAO.findByOrderId(orderId);
			if(tOrder.getFstatus().intValue()==10){
				viewOrderDTO.setExmessage("暂无物流信息");
			}else if(tOrder.getFstatus().intValue()==20){
				viewOrderDTO.setExmessage("商品正在打包中");
			}else if(tOrder.getFstatus().intValue()>=30&&tOrder.getFstatus().intValue()<=70){
				if (tExpress == null) {
					viewOrderDTO.setExmessage("暂无物流信息");
				} else {
					if(tOrder.getFstatus().intValue()==70){
						viewOrderDTO.setExmessage("已收货");
					}else{
						viewOrderDTO.setExmessage("商品已发货");
					}
					viewOrderDTO
							.setExname(DictionaryUtil.getString(DictionaryUtil.ExpressCode, tExpress.getFexYpressType()));
					viewOrderDTO.setExnum(tExpress.getFexpressNum());
				}
			} else{
				viewOrderDTO.setExmessage("暂无物流信息");
			}
			
		} else {
			viewOrderDTO.setVerificationCode(tOrder.getFverificationCode());
			viewOrderDTO.setVerificationCodeAuto(this.ordercode(tOrder));
		}
		if(tOrder.getFpayTime()!=null){
			Integer second = Seconds.secondsBetween(new DateTime(new Date()), 
					new DateTime(DateUtils.addDays(tOrder.getFpayTime(), 15))).getSeconds();
			viewOrderDTO.setConfirmationTime("还剩"+DateUtil.formatDuring(second)+"自动确认");
		}
		if(tOrder.getTSponsor().getFsponsorModel()!=null){
			viewOrderDTO.setSponsorModel(tOrder.getTSponsor().getFsponsorModel());
		}

		StringBuilder hql = new StringBuilder();
		hql.append("select e.fimage1 as goodsImage,o.fgoodsSubTitle as fgoodsSubTitle,e.fsdealsModel as fsdealsModel, ")
				.append(" o.fpromotionModel as fpromotionModel,o.fspec as fspec,o.fspec as fspec,")
				.append(" o.forderId as orderId,o.feventId as goodsId,o.feventTitle as feventTitle,o.fprice as price,o.fspec as spec,o.fcount as count,o.ftotalPrice as totalPrice from ")
				.append(" TOrderGoods o inner join TEvent e on e.id = o.feventId ")
				.append(" where o.forderId = :orderId");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("orderId", orderId);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		List<OrderGoodsDetailDTO> orderGoodsList = Lists.newArrayList();

		OrderGoodsDetailDTO orderGoodsDetailDTO = null;
		for (Map<String, Object> amap : list) {
			// 单个商品信息
			orderGoodsDetailDTO = new OrderGoodsDetailDTO();
			if (amap.get("goodsId") != null && StringUtils.isNotBlank(amap.get("goodsId").toString())) {
				orderGoodsDetailDTO.setGoodsId(amap.get("goodsId").toString());
			}
			if (amap.get("feventTitle") != null && StringUtils.isNotBlank(amap.get("feventTitle").toString())) {
				orderGoodsDetailDTO.setGoodsName(amap.get("feventTitle").toString());
			}
			if (amap.get("fgoodsSubTitle") != null && StringUtils.isNotBlank(amap.get("fgoodsSubTitle").toString())) {
				orderGoodsDetailDTO.setGoodsSub(amap.get("fgoodsSubTitle").toString());
			}
			if (amap.get("price") != null && StringUtils.isNotBlank(amap.get("price").toString())) {
				orderGoodsDetailDTO.setPrice((BigDecimal) amap.get("price"));// 单个商品价格
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				orderGoodsDetailDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("goodsImage") != null && StringUtils.isNotBlank(amap.get("goodsImage").toString())) {
				orderGoodsDetailDTO.setGoodsImage(fxlService.getImageUrl(amap.get("goodsImage").toString(), false));
			}
			if (amap.get("fsdealsModel") != null && StringUtils.isNotBlank(amap.get("fsdealsModel").toString())) {
				orderGoodsDetailDTO.setSdealsModel((Integer)amap.get("fsdealsModel"));
			}
			if (amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				orderGoodsDetailDTO.setGoodsNum((Integer) amap.get("count"));
				orderGoodsDetailDTO.setCountPrice(
						((BigDecimal) amap.get("price")).multiply(new BigDecimal(amap.get("count").toString()))
								.setScale(2, RoundingMode.HALF_UP).toString());
			}
			orderGoodsList.add(orderGoodsDetailDTO);
		}
		viewOrderDTO.setOrderGoodsList(orderGoodsList);

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderInfo", viewOrderDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户订单详情加载成功！");
		return responseDTO;
	}


	public BigDecimal getFreight(BigDecimal total, TSponsor tSponsor,Integer cityId) {
		BigDecimal pinkage = tSponsor.getFpinkage();
		BigDecimal range = tSponsor.getFrange();
		//BigDecimal fprovincialRange = tSponsor.getFprovincialRange();
		BigDecimal freight = BigDecimal.ZERO;
		if (total.compareTo(pinkage) < 0) {
			freight = range;
		}
		return freight;
	}

	public String wxPayConfirm(Notify notify) {

		String orderNum = notify.getOutTradeNo();
		TOrder tOrder = orderDAO.getByFstatusAndForderNum(10, orderNum);
		if (tOrder == null) {
			return "<xml><return_code>FAIL</return_code><return_msg>ORDER_NOT_EXIST</return_msg></xml>";
		}
		Date now = new Date();
		String returnStr = "<xml><return_code>SUCCESS</return_code></xml>";

		TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrder.getId(), 1, 10);
		if (tWxPay == null) {
			tWxPay = new TWxPay();
			tWxPay.setFinOut(1);
			tWxPay.setFcreateTime(now);
			tWxPay.setForderId(tOrder.getId());
			tWxPay.setForderType(1);
			tWxPay.setTCustomer(tOrder.getTCustomer());
		}
		int status = 0;
		// isResultCodeSuccess()方法表示支付是否成功，是交易成功的标志
		if (notify.isResultCodeSuccess()) {
			int total = notify.getTotalFeeFen();
			if (total != 0) {
				BigDecimal payTotal = new BigDecimal(notify.getTotalFeeFen()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
				// 如果支付金额大于等于订单总价，表示是支付完成
				if (tOrder.getFtotal().compareTo(payTotal) <= 0) {
					// 支付成功，更改订单状态为20（已支付）
					status = 20;
					// 微信支付表的状态为已支付
					tWxPay.setFstatus(30);
				} else {
					// 支付金额小于订单金额，更改订单状态为15（部分支付）
					status = 15;
					// 微信支付表的状态为部分支付
					tWxPay.setFstatus(20);
				}
			} else {
				// TODO 支付零元的情况没有处理
			}
		} else {
			// 支付失败，更改订单状态为11（支付失败）
			status = 11;
			// 微信支付表的状态为支付失败
			tWxPay.setFstatus(90);
			returnStr = "<xml><return_code>FAIL</return_code><return_msg>ORDER_NOT_EXIST</return_msg></xml>";
		}
		fxlService.orderStatusChange(1, tOrder.getFcustomerName(), tOrder.getId(), null, tOrder.getFstatus(), status);
		orderDAO.updateOrderStatusAndPayTime(status, now, tOrder.getId());

		// 删除订单超时未支付取消订单任务
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 7);
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 18);

		// 添加线程任务发送购买成功通知短信
		OrderSendSmsBean oss = new OrderSendSmsBean();
		oss.setCreateTime(now);
		oss.setCustomerId(tOrder.getTCustomer().getId());
		oss.setOrderId(tOrder.getId());
		oss.setOrderNum(tOrder.getForderNum());
		oss.setTaskType(3);
		AsynchronousTasksManager.put(oss);

		tWxPay.setFconfirmPayTime(notify.getDateProperty("time_end"));
		tWxPay.setFupdateTime(now);
		if (StringUtils.isNotBlank(notify.getProperty("fee_type"))) {
			tWxPay.setFcurrencyType(notify.getProperty("fee_type"));
		}
		tWxPay.setFcpRequestInfo(notify.getReturnMsg() != null ? notify.getReturnMsg().getReturnMsg()
				: notify.getProperties().toString());
		tWxPay.setFcpResponseInfo(returnStr);
		tWxPay.setFtransactionId(notify.getTransactionId());
		wxPayDAO.save(tWxPay);
		return returnStr;
	}

	public ResponseDTO cancelPayOrder(Integer clientType, String ticket, String orderId, String refundReason) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		if (tOrder.getFstatus().intValue() >= 60) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您的订单已经消费无法取消！");
			return responseDTO;
		}

		Date now = new Date();
		// 如果是零元单，直接改变订单状态为“退款完成”
		if (tOrder.getFtotal().compareTo(BigDecimal.ZERO) == 0) {
			fxlService.orderStatusChange(1, customerDTO.getName(), orderId, refundReason, tOrder.getFstatus(), 120);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(120, now, refundReason, orderId);
			// 将订单占用的库存数量退回
			List<TOrderGoods> list = orderGoodsDAO.findByOrderId(orderId);
			goodsService.backStock(list);
			
			responseDTO = couponsService.backCoupon(orderId, tOrder.getFstatus());
			
			responseDTO.setMsg("订单取消成功！");
		} else if (tOrder.getFstatus().intValue() == 10) {
			fxlService.orderStatusChange(1, customerDTO.getName(), orderId, refundReason, tOrder.getFstatus(), 100);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(100, now, refundReason, orderId);
			// 将订单占用的库存数量退回
			List<TOrderGoods> list = orderGoodsDAO.findByOrderId(orderId);
			goodsService.backStock(list);
			
			responseDTO = couponsService.backCoupon(orderId, tOrder.getFstatus());
			responseDTO.setMsg("订单取消成功！");
		} else {
			// 非零元单则将状态改为“申请退款”，等待运维人员审核退款
			fxlService.orderStatusChange(1, customerDTO.getName(), orderId, refundReason, tOrder.getFstatus(), 110);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(110, now, refundReason, orderId);
			responseDTO.setMsg("退款申请已成功提交！");
		}
		
		//如果是砍一砍库存
		if(tOrder!=null && tOrder.getFsource()==20){
			TCustomerBargaining tCustomerBargaining = customerBargainingDAO.getByOrderId(orderId);
			customerBargainingDAO.updateStatus(orderId,100);
			TEventBargaining tEventBargaining = eventBargainingDAO.findOne(tCustomerBargaining.getFbargainingId());
			if (tCustomerBargaining.getFdefaultLevel() == 1) {
				tEventBargaining.setFremainingStock1(tEventBargaining.getFremainingStock1() + 1);
			} else if (tCustomerBargaining.getFdefaultLevel() == 2) {
				tEventBargaining.setFremainingStock2(tEventBargaining.getFremainingStock2() + 1);
			} else if (tCustomerBargaining.getFdefaultLevel() == 3) {
				tEventBargaining.setFremainingStock3(tEventBargaining.getFremainingStock3() + 1);
			}
			tEventBargaining = eventBargainingDAO.save(tEventBargaining);
		}

		// 删除订单超时未支付取消订单任务
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 7);
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 18);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}
	
	
	public ResponseDTO refundOrder(Integer clientType, String ticket, String orderId, String refundTotal,String goodsStatus,
			String refundDesc,Integer refundReason,Integer refundType) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		if (tOrder.getFstatus().intValue() < 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您的订单未付款无法退款！");
			return responseDTO;
		}

		Date now = new Date();
		TOrderRefund orderRefund = orderRefundDAO.findByOrder(orderId);
		if(orderRefund == null){
			orderRefund = new TOrderRefund();
			orderRefund.setFcreateTime(now);
			orderRefund.setFupdateTime(now);
			orderRefund.setFgoodsStatus(goodsStatus);
			orderRefund.setForderId(orderId);
			orderRefund.setfRefundDesc(refundDesc);
			orderRefund.setfRefundReson(refundReason);
			orderRefund.setFrefundStatus(10);
			orderRefund.setfRefundTotal(new BigDecimal(refundTotal));
			orderRefund.setFrefundType(refundType);
			orderRefund = orderRefundDAO.save(orderRefund);
				// 非零元单则将状态改为“申请退款”，等待运维人员审核退款
			fxlService.orderStatusChange(1, customerDTO.getName(), orderId, 
					DictionaryUtil.getString(DictionaryUtil.RefundReason, refundReason), tOrder.getFstatus(), 110);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(110, now, 
					DictionaryUtil.getString(DictionaryUtil.RefundReason, refundReason), orderId);
			
			//如果是砍一砍库存
			if(tOrder!=null && tOrder.getFsource()==20){
				TCustomerBargaining tCustomerBargaining = customerBargainingDAO.getByOrderId(orderId);
				customerBargainingDAO.updateStatus(orderId,100);
				TEventBargaining tEventBargaining = eventBargainingDAO.findOne(tCustomerBargaining.getFbargainingId());
				if (tCustomerBargaining.getFdefaultLevel() == 1) {
					tEventBargaining.setFremainingStock1(tEventBargaining.getFremainingStock1() + 1);
				} else if (tCustomerBargaining.getFdefaultLevel() == 2) {
					tEventBargaining.setFremainingStock2(tEventBargaining.getFremainingStock2() + 1);
				} else if (tCustomerBargaining.getFdefaultLevel() == 3) {
					tEventBargaining.setFremainingStock3(tEventBargaining.getFremainingStock3() + 1);
				}
				tEventBargaining = eventBargainingDAO.save(tEventBargaining);
			}

			// 删除订单超时未支付取消订单任务
			responseDTO.setMsg("退款申请已成功提交！");
			timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 7);
			timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tOrder.getId(), 18);
			
		}else{
			orderRefund.setFupdateTime(now);
			orderRefund.setFgoodsStatus(goodsStatus);
			orderRefund.setfRefundDesc(refundDesc);
			orderRefund.setfRefundReson(refundReason);
			orderRefund.setFrefundStatus(10);
			orderRefund.setfRefundTotal(new BigDecimal(refundTotal));
			orderRefund.setFrefundType(refundType);
			orderRefund = orderRefundDAO.save(orderRefund);
		}
		// 同步发送钉钉通知
		try {
			if (StringUtils.isNotBlank(DingTalkUtil.getRefundSyncingDingTalk())) {
				String msg = new StringBuilder().append("【查找优惠】：用户[").append(tOrder.getFcustomerName()).append("],")
						.append("电话[").append(tOrder.getFcustomerPhone()).append("]").append("在")
						.append(DateFormatUtils.format(now, "yyyy年MM月dd日HH时mm分")).append("提交了订单退款")
						.append("【订单号：").append(tOrder.getForderNum()).append("】").append("。快去处理").toString();
				DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getRefundSyncingDingTalk());
			}
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderId", tOrder.getId());
			map.put("customerId", customerDTO.getCustomerId());
			OutPutLogUtil.printLoggger(e, map, logger);
			logger.error("用户取消订单时调用钉钉通知时，钉钉通知接口出错。");
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}
	
	
	public ResponseDTO cancelOrderInfo(Integer clientType, String ticket, String orderId, Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (type==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("type参数不能为空，请检查type的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		if (tOrder.getFstatus().intValue() >= 60 && tOrder.getFstatus().intValue() < 100) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您的订单已经消费无法取消！");
			return responseDTO;
		}
		
		List<OrderGoodsDetailDTO> orderGoodsList = Lists.newArrayList();
		Map<Integer, String> all = DictionaryUtil.getStatueMap(DictionaryUtil.RefundReason);
		
		List<RefundReasonDTO> refundReason = Lists.newArrayList();
		RefundReasonDTO refundReasonDTO = null;
		
		RefundInfoDTO refundInfo = new RefundInfoDTO();
		//获取订单商品
		StringBuilder hql = new StringBuilder();
			hql.append("select e.fimage1 as goodsImage,o.fgoodsSubTitle as fgoodsSubTitle, ")
			.append(" o.fpromotionModel as fpromotionModel,o.fspec as fspec,o.fspec as fspec,")
			.append(" o.forderId as orderId,o.feventId as goodsId,o.feventTitle as feventTitle,o.fprice as price,o.fspec as spec,o.fcount as count,o.ftotalPrice as totalPrice from ")
			.append(" TOrderGoods o inner join TEvent e on e.id = o.feventId ")
			.append(" where o.forderId = :orderId");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("orderId", orderId);
		
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		OrderGoodsDetailDTO orderGoodsDetailDTO = null;
		for (Map<String, Object> amap : list) {
			// 单个商品信息
			orderGoodsDetailDTO = new OrderGoodsDetailDTO();
			if (amap.get("goodsId") != null && StringUtils.isNotBlank(amap.get("goodsId").toString())) {
				orderGoodsDetailDTO.setGoodsId(amap.get("goodsId").toString());
			}
			if (amap.get("feventTitle") != null && StringUtils.isNotBlank(amap.get("feventTitle").toString())) {
				orderGoodsDetailDTO.setGoodsName(amap.get("feventTitle").toString());
			}
			if (amap.get("fgoodsSubTitle") != null && StringUtils.isNotBlank(amap.get("fgoodsSubTitle").toString())) {
				orderGoodsDetailDTO.setGoodsSub(amap.get("fgoodsSubTitle").toString());
			}
			if (amap.get("price") != null && StringUtils.isNotBlank(amap.get("price").toString())) {
				orderGoodsDetailDTO.setPrice((BigDecimal) amap.get("price"));// 单个商品价格
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				orderGoodsDetailDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("goodsImage") != null && StringUtils.isNotBlank(amap.get("goodsImage").toString())) {
				orderGoodsDetailDTO.setGoodsImage(fxlService.getImageUrl(amap.get("goodsImage").toString(), false));
			}
			if (amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				orderGoodsDetailDTO.setGoodsNum((Integer) amap.get("count"));
				orderGoodsDetailDTO.setCountPrice(
						((BigDecimal) amap.get("price")).multiply(new BigDecimal(amap.get("count").toString()))
						.setScale(2, RoundingMode.HALF_UP).toString());
			}
			orderGoodsList.add(orderGoodsDetailDTO);
		}
		if(type ==1 ){//退货退款
			for (Iterator it = all.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
				if(e.getKey().intValue()<=20){
					refundReasonDTO = new RefundReasonDTO();
					refundReasonDTO.setReasonId(e.getKey());
					refundReasonDTO.setReasonName(e.getValue().toString());
					refundReason.add(refundReasonDTO);
				}
			}
		}else if(type==2 ){//未发货仅退款
			for (Iterator it = all.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
				if(e.getKey().intValue()<=40 && e.getKey().intValue()>=20){
					refundReasonDTO = new RefundReasonDTO();
					refundReasonDTO.setReasonId(e.getKey());
					refundReasonDTO.setReasonName(e.getValue().toString());
					refundReason.add(refundReasonDTO);
				}
			}
		}else if(type==3 ){//已发货仅退款
			for (Iterator it = all.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
				if(e.getKey().intValue()<=60 && e.getKey().intValue()>=40){
					refundReasonDTO = new RefundReasonDTO();
					refundReasonDTO.setReasonId(e.getKey());
					refundReasonDTO.setReasonName(e.getValue().toString());
					refundReason.add(refundReasonDTO);
				}
			}
		}
		refundInfo.setOrderGoodsList(orderGoodsList);
		refundInfo.setRefundReason(refundReason);
		if(type==2){
			refundInfo.setRefundPrice(tOrder.getFtotal().toString());
		}else{
			refundInfo.setRefundPrice(tOrder.getFtotal().subtract(tOrder.getFpostage()).toString());
		}
		refundInfo.setPostage(tOrder.getFpostage().toString());
		
		TOrderRefund orderRefund = orderRefundDAO.findByOrder(orderId);
		if(orderRefund!=null){
			refundInfo.setCheckRefundReason(orderRefund.getfRefundReson());
			refundInfo.setRefundDesc(orderRefund.getfRefundDesc());
			refundInfo.setGoodsStatus(orderRefund.getFgoodsStatus());
			refundInfo.setCheckRefundPrice(orderRefund.getfRefundTotal().toString());
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("refundInfo", refundInfo);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取退款信息成功");
		return responseDTO;
	}
	
	public ResponseDTO refundOrderDetail(Integer clientType, String ticket, String orderId) {
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		TOrderRefund orderRefund = orderRefundDAO.findByOrder(orderId);
		List<OrderGoodsDetailDTO> orderGoodsList = Lists.newArrayList();
		Map<Integer, String> all = DictionaryUtil.getStatueMap(DictionaryUtil.ExpressCode);
		List<ExpressCompamyDTO> expressCompamyList = Lists.newArrayList();
		ExpressCompamyDTO expressCompamyDTO = null;
		for (Iterator it = all.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) it.next();
			expressCompamyDTO = new ExpressCompamyDTO();
				expressCompamyDTO.setCompanyId(e.getKey());
				expressCompamyDTO.setCompanyName(e.getValue().toString());
				expressCompamyList.add(expressCompamyDTO);
		}
		
		RefundDetailDTO refundDetailDTO = new RefundDetailDTO();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select e.fimage1 as goodsImage,o.fgoodsSubTitle as fgoodsSubTitle, ")
		.append(" o.fpromotionModel as fpromotionModel,o.fspec as fspec,o.fspec as fspec,")
		.append(" o.forderId as orderId,o.feventId as goodsId,o.feventTitle as feventTitle,o.fprice as price,o.fspec as spec,o.fcount as count,o.ftotalPrice as totalPrice from ")
		.append(" TOrderGoods o inner join TEvent e on e.id = o.feventId ")
		.append(" where o.forderId = :orderId");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("orderId", orderId);
		
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		OrderGoodsDetailDTO orderGoodsDetailDTO = null;
		for (Map<String, Object> amap : list) {
			// 单个商品信息
			orderGoodsDetailDTO = new OrderGoodsDetailDTO();
			if (amap.get("goodsId") != null && StringUtils.isNotBlank(amap.get("goodsId").toString())) {
				orderGoodsDetailDTO.setGoodsId(amap.get("goodsId").toString());
			}
			if (amap.get("feventTitle") != null && StringUtils.isNotBlank(amap.get("feventTitle").toString())) {
				orderGoodsDetailDTO.setGoodsName(amap.get("feventTitle").toString());
			}
			if (amap.get("fgoodsSubTitle") != null && StringUtils.isNotBlank(amap.get("fgoodsSubTitle").toString())) {
				orderGoodsDetailDTO.setGoodsSub(amap.get("fgoodsSubTitle").toString());
			}
			if (amap.get("price") != null && StringUtils.isNotBlank(amap.get("price").toString())) {
				orderGoodsDetailDTO.setPrice((BigDecimal) amap.get("price"));// 单个商品价格
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				orderGoodsDetailDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("goodsImage") != null && StringUtils.isNotBlank(amap.get("goodsImage").toString())) {
				orderGoodsDetailDTO.setGoodsImage(fxlService.getImageUrl(amap.get("goodsImage").toString(), false));
			}
			if (amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				orderGoodsDetailDTO.setGoodsNum((Integer) amap.get("count"));
				orderGoodsDetailDTO.setCountPrice(
						((BigDecimal) amap.get("price")).multiply(new BigDecimal(amap.get("count").toString()))
						.setScale(2, RoundingMode.HALF_UP).toString());
			}
			orderGoodsList.add(orderGoodsDetailDTO);
		}
		
		if(orderRefund!=null){
			refundDetailDTO.setPayType(DictionaryUtil.getString(DictionaryUtil.PayType, tOrder.getFpayType()));
			refundDetailDTO.setOrderGoodsList(orderGoodsList);
			refundDetailDTO.setRefundReason(DictionaryUtil.getString(DictionaryUtil.RefundReason, orderRefund.getfRefundReson()));
			refundDetailDTO.setRefundPrice(orderRefund.getfRefundTotal().toString());
			if(StringUtils.isNotBlank(orderRefund.getFrefundExpress())){
				refundDetailDTO.setExpressNum(orderRefund.getFrefundExpress());
				refundDetailDTO.setExpressName(DictionaryUtil.getString(DictionaryUtil.ExpressCode, orderRefund.getFrefundExpressType()));
			}
			
			refundDetailDTO.setOrderNum(tOrder.getForderNum());
			refundDetailDTO.setRefundStatus(orderRefund.getFrefundStatus());
			refundDetailDTO.setRefundType(orderRefund.getFrefundType());
			refundDetailDTO.setRefundTypeString(DictionaryUtil.getString(DictionaryUtil.RefundType, orderRefund.getFrefundType()));
			refundDetailDTO.setExpressCompamyList(expressCompamyList);
			refundDetailDTO.setRefundTime(DateFormatUtils.format(orderRefund.getFcreateTime(), "yyyy-MM-dd"));
			Integer second = 0;
			if(orderRefund.getFrefundStatus().intValue()==10){
				second = Seconds.secondsBetween(new DateTime(new Date()), 
						new DateTime(DateUtils.addDays(orderRefund.getFupdateTime(), 4))).getSeconds();
			}else{
				second = Seconds.secondsBetween(new DateTime(new Date()), 
						new DateTime(DateUtils.addDays(orderRefund.getFupdateTime(), 10))).getSeconds();
			}
			refundDetailDTO.setRemainingTime(second.toString());
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("refundDetailDTO", refundDetailDTO);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取退款信息成功");
		return responseDTO;
	}
	
	public ResponseDTO saveRefundExpress(Integer clientType, String ticket, String orderId, String expressNum,Integer companyId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(expressNum)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("expressNum参数不能为空，请检查expressNum的传递参数值！");
			return responseDTO;
		}
		if (companyId==null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("companyId参数不能为空，请检查companyId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		TOrderRefund orderRefund = orderRefundDAO.findByOrder(orderId);
		orderRefund.setFrefundExpress(expressNum);
		orderRefund.setFrefundExpressType(companyId);
		orderRefund.setFrefundStatus(30);
		orderRefundDAO.save(orderRefund);
		
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("保存退款物流成功");
		return responseDTO;
	}

	/**
	 * 再次支付订单
	 * 
	 * @param clientType
	 * @param ticket
	 * @param orderId
	 * @return
	 */
	public ResponseDTO againToFillOrder(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() == 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("该订单已完成支付，请勿重复操作！");
			return responseDTO;
		}
		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(tOrder.getId());
		orderDTO.setOrderNum(tOrder.getForderNum());
		orderDTO.setOrderType(tOrder.getForderType());
		orderDTO.setGoodsId(tOrder.getTEvent().getId());
		orderDTO.setGoodsTitle(tOrder.getFeventTitle());
		orderDTO.setTotal(tOrder.getFtotal());
		orderDTO.setStatus(tOrder.getFstatus());
		orderDTO.setStatusString(DictionaryUtil.getString(DictionaryUtil.OrderStatus, tOrder.getFstatus()));
		// orderDTO.setRemark(tOrder.getFremark());

		// 返回活动附属信息
		// List<TOrderExtInfo> orderExtInfos =
		// orderExtInfoDAO.findByOrderId(orderId);
		List<Map<String, Object>> orderExtMaps = new ArrayList<Map<String, Object>>();

		int specPerson = 0;
		if (tOrder.getTEventSpec().getFadult() != null) {
			specPerson += tOrder.getTEventSpec().getFadult().intValue();
		}
		if (tOrder.getTEventSpec().getFchild() != null) {
			specPerson += tOrder.getTEventSpec().getFchild().intValue();
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("orderInfo", orderDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单支付成功！");
		return responseDTO;
	}

	public ResponseDTO orderTracking(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
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

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		if (tOrder.getFstatus().intValue() <= 10) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您的订单暂无物流信息！");
			return responseDTO;
		}

		TExpress tExpress = new TExpress();
		tExpress = expressDAO.findByOrderId(orderId);
		if (tExpress == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您的订单暂无物流信息！");
			return responseDTO;
		}
		Express ex = new Express();
		Map<String, Object> MapBody = new HashMap<String, Object>();

		MapBody.put("com", DictionaryUtil.getCode(DictionaryUtil.ExpressCode, tExpress.getFexYpressType()));
		MapBody.put("num", tExpress.getFexpressNum());
		MapBody.put("from", tExpress.getFstarting());
		MapBody.put("to", tExpress.getFreach());
		ex.setMapBody(MapBody);
		responseDTO = ExpressUtil.sendExPress(ex);
		ExpressDTO expressDTO = (ExpressDTO) responseDTO.getData().get("track");
		Integer state = null;
		if(expressDTO!=null&&expressDTO.getState()!=null){
			state = Integer.parseInt(expressDTO.getState());
		}
		if (state == null ) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的订单暂无物流信息！");
			return responseDTO;
		}
		expressDTO.setCom(DictionaryUtil.getString(DictionaryUtil.ExpressCode, tExpress.getFexYpressType()));
		expressDTO.setPhone(ConfigurationUtil.getExpressPhone(tExpress.getFexYpressType()));

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单物流信息加载成功！");
		return responseDTO;
	}

	/**
	 * 获取用户订单带出行，待支付，待评价总数 service
	 * 
	 * @param ticket
	 *            票
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getOrderNumByStatus(Integer clientType, String ticket, Integer sellModel) {
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
		// Status：10（待支付），20（带出行），60（待评价）,70(已完成)
		Integer toBePaidNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 10);
		Integer toBeSendlNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 20);
		Integer toBeTravelNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 30);
		//Integer toBeEvaluateNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 60, sellModel);
		Integer completedNum = orderDAO.countOrderNumByStatus(customerDTO.getCustomerId(), 70);
		Integer austinNum = orderDAO.orderNumByStatus(customerDTO.getCustomerId(), 110, 119);
		
		//
		StringBuilder hql = new StringBuilder();
		hql.append("select o.id as id from TOrder t left join TOrderGoods o on t.id = o.forderId ")
				.append(" where t.TCustomer.id = :customerId and t.fstatus = 70 and o.fstatus = 10");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("toBePaidNum", toBePaidNum);// 代付款
		returnData.put("toBeSendlNum", toBeSendlNum);// 代发货
		returnData.put("toBeTravelNum", toBeTravelNum);// 代收货
		returnData.put("toBeEvaluateNum", list.size());// 代评价
		returnData.put("completedNum", completedNum);// 已完成
		returnData.put("austinNum", austinNum);// 售后
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("订单状态总数加载成功！");
		return responseDTO;
	}

	public ResponseDTO orderfindInfo(Integer clientType, String ticket, String goodsId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(goodsId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodsId参数不能为空，请检查goodsId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(goodsId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodsId参数信息有误，系统中没有商品ID为“" + goodsId + "”的活动！");
			return responseDTO;
		}

		TShoppingAddress shoppingAddress = shoppingAddressDAO.findByDefault(customerDTO.getCustomerId());

		OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
		orderInfoDTO.setGoodsId(goodsId);
		orderInfoDTO.setGoodsName(tEvent.getFtitle());
		orderInfoDTO.setPrice(tEvent.getFpriceMoney());
		orderInfoDTO.setSpec(tEvent.getFspec());
		if (shoppingAddress == null) {
			orderInfoDTO.setAddressflag(1);
		} else {
			orderInfoDTO.setAddressflag(0);
			StringBuilder sb = new StringBuilder();
			sb.append(shoppingAddress.getFregion()).append(shoppingAddress.getFstreet())
					.append(shoppingAddress.getFaddress());
			orderInfoDTO.setAddress(sb.toString());
			orderInfoDTO.setName(shoppingAddress.getFname());
			orderInfoDTO.setPhone(shoppingAddress.getFphone());
		}

		Map<String, Object> returnData = Maps.newHashMap();

		returnData.put("orderInfo", orderInfoDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("h5订单信息加载成功！");
		return responseDTO;
	}

	public ResponseDTO toPayOrderApp(Integer clientType, String ticket, String goodsSkuList, String addressId,
			Integer payType, Integer payClientType, String ip, Integer fchannel, String gps, String deviceId,
			Integer sellModel, String couponDeliveryId, String postageCouponId,String sign,String addressIP) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		if (StringUtils.isBlank(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("sign参数不能为空，请检查sign的传递参数值！");
			return responseDTO;
		}
		
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		
		String signStr = fxlService.httpEncrypt(addressIP, clientType);
		if (!signStr.equals(sign)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的sign不正确，请检查后再输入！");
			return responseDTO;
		}

		TOrder tOrder = new TOrder();
		String orderNum = NumberUtil.getOrderNum(1);
		Date now = new Date();

		//获得用户所要购买的商品
		Map<String, CartGoodsDTO> goodsSkuMap = Maps.newHashMap();
		Map<String, CartGoodsDTO> remainCartMap = Maps.newHashMap();
		JavaType jt = mapper.contructMapType(HashMap.class, String.class, Integer.class);
		Map<String, Integer> buyNowMap = Maps.newHashMap();
		if (StringUtils.isNotBlank(goodsSkuList)) {
			buyNowMap = mapper.fromJson(goodsSkuList, jt);
		}
		JavaType jtSku = mapper.contructMapType(HashMap.class, String.class, CartGoodsDTO.class);
		Map<String, CartGoodsDTO> cartMap = Maps.newHashMap();
		try {
			if (StringUtils.isNotBlank(goodsSkuList)) {
				buyNowMap = mapper.fromJson(goodsSkuList, jt);
				CartGoodsDTO cartGoodsDTO = null;
				TGoodsSku tGoodsSku = null;
				for (Map.Entry<String, Integer> entry : buyNowMap.entrySet()) {
					tGoodsSku = goodsService.getGoodsSkuCache(entry.getKey());
					cartGoodsDTO = new CartGoodsDTO();
					cartGoodsDTO.setGoodsSkuId(entry.getKey().toString());
					cartGoodsDTO.setCount(entry.getValue());
					cartGoodsDTO.setSpec(goodsService.getGoodsSku(
					tGoodsSku.getFclassTypeValue1()!=null ? tGoodsSku.getFclassTypeValue1():0,
					tGoodsSku.getFclassTypeValue2()!=null ? tGoodsSku.getFclassTypeValue2():0));
					goodsSkuMap.put(entry.getKey().toString(), cartGoodsDTO);
				}
			} else {
				CartGoodsDTO cartGoodsDTO = null;
				cartMap = mapper.fromJson(redisService.getValue(customerDTO.getCustomerId(), RedisMoudel.shoppingcar), jtSku);
				for (Map.Entry<String, CartGoodsDTO> entry : cartMap.entrySet()) {
					cartGoodsDTO = entry.getValue();
					if(cartGoodsDTO.isChecked()){
						goodsSkuMap.put(entry.getKey().toString(), cartGoodsDTO);
					}else{
						remainCartMap.put(entry.getKey().toString(), cartGoodsDTO);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tOrder.setForderNum(orderNum);
		tOrder.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		tOrder.setFcustomerName(customerDTO.getName());
		if (StringUtils.isNotBlank(customerDTO.getPhone())) {
			tOrder.setFcustomerPhone(customerDTO.getPhone());
		}
		tOrder.setFcustomerSex(DictionaryUtil.getString(DictionaryUtil.Sex, customerDTO.getSex()));

		tOrder.setFcreateTime(now);
		Date fdate = null;
		if (Constant.getUnPayFailureMinute() != 0) {
			fdate = DateUtils.addMinutes(now, Constant.getUnPayFailureMinute());
			tOrder.setFunPayFailureTime(fdate);
		}
		tOrder.setFlockFlag(0);
		// 增加1.下单渠道 2.下单地址(下单地址定位失败则用ip定位转换经纬度存入)3.唯一标识

		tOrder.setFchannel(fchannel);
		tOrder.setFgps(gps);
		tOrder.setFdeviceId(deviceId);
		tOrder.setFsource(1);
		tOrder.setFstatus(10);

		BigDecimal total = BigDecimal.ZERO;
		List<TOrderGoods> tOrderGoodsList = Lists.newArrayList();
		TOrderGoods tOrderGoods = null;
		StringBuilder imageurls = new StringBuilder();
		String goodsTitle = "";
		TSponsor tSponsor = null;
		CartGoodsDTO cartGoodsDTO = null;
		TGoodsSku tGoodsSku = null;
		BigDecimal useCouponTotal = BigDecimal.ZERO;//商品总价
		for (Map.Entry<String, CartGoodsDTO> entry : goodsSkuMap.entrySet()) {
			cartGoodsDTO = entry.getValue();
			tGoodsSku = goodsService.getGoodsSkuCache(entry.getKey());
			TEvent tEvent = eventDAO.findOne(tGoodsSku.getFgoodsId());
			tSponsor = tEvent.getTSponsor();
			sellModel = tEvent.getFsellModel();
			goodsTitle = tEvent.getFtitle();
			if (tEvent == null) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(205);
				responseDTO.setMsg("您输入的goodsList参数有误，goodsList=“" + goodsSkuList + "”的活动不存在！");
				return responseDTO;
			}
			tOrderGoods = new TOrderGoods();
			tOrderGoods.setFcreateTime(now);
			tOrderGoods.setFeventId(tEvent.getId());
			tOrderGoods.setFgoodsSkuId(cartGoodsDTO.getGoodsSkuId());
			tOrderGoods.setFeventTitle(tEvent.getFtitle());
			tOrderGoods.setFprice(tGoodsSku.getFpriceMoney());
			tOrderGoods.setFspec(cartGoodsDTO.getSpec());
			tOrderGoods.setFpromotionModel(tEvent.getFpromotionModel());
			tOrderGoods.setFgoodsSubTitle(tEvent.getFsubTitle());
			tOrderGoods.setFstatus(10);
			//判断商品是否秒杀
			if(tEvent.getFsdealsModel()!=null&&tEvent.getFsdealsModel().intValue()==3){
				TColumnBanner banner = columnBannerDAO.findColumnBanner(2, 1);
				TSeckillModule tSeckillModule = seckillModuleDAO.findByGoodsId(tEvent.getId());
				if(banner.getFseckillTime().compareTo(new Date())<=0||tSeckillModule.getFgoodstatus().intValue()!=20){
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(101);
					responseDTO.setMsg("您购买的商品有部分已过期！");
					return responseDTO;
				}
			}else{
				// 先判断商品是否有下架
				if (tEvent.getFstatus() != 20) {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(101);
					responseDTO.setMsg("您购买的商品有部分已下架！");
					return responseDTO;
				}
			}
			// 判断商品是否限购
			if (tGoodsSku.getFlimitation() >= 0) {
				if (customerDTO != null) {
					if (this.todayOrder(customerDTO.getCustomerId(), entry.getKey().toString())) {
						responseDTO.setSuccess(false);
						responseDTO.setStatusCode(102);
						responseDTO.setMsg("您购买的部分商品超过限购数量！");
						return responseDTO;
					}
				}
			}
			// 判断商品库存是否还够
			if (tGoodsSku.getFstock() <= 0) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(103);
				responseDTO.setMsg("您购买的部分商品库存不足！");
				return responseDTO;
			} else if ((tGoodsSku.getFstock().intValue() - cartGoodsDTO.getCount()) < 0) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(104);
				responseDTO.setMsg("您购买的部分商品库存不足！");
				return responseDTO;
			} else {
				tOrderGoods.setFcount(cartGoodsDTO.getCount());
			}
			imageurls.append(tGoodsSku.getFimage()).append(";");
			tOrderGoods.setFtotalPrice(tOrderGoods.getFprice().multiply(new BigDecimal(tOrderGoods.getFcount())));
			tOrderGoodsList.add(tOrderGoods);
			total = total.add(tOrderGoods.getFprice().multiply(new BigDecimal(tOrderGoods.getFcount())));
			if(tEvent.getFusePreferential()==null || tEvent.getFusePreferential().intValue()==0){
				useCouponTotal = useCouponTotal.add(tOrderGoods.getFprice().multiply(new BigDecimal(tOrderGoods.getFcount())));
			}
		}
		
		if (tSponsor != null) {
			tOrder.setTSponsor(tSponsor);
			tOrder.setFsponsorName(tSponsor.getFname());
			tOrder.setFsponsorFullName(tSponsor.getFfullName());
			tOrder.setFsponsorPhone(tSponsor.getFphone());
			tOrder.setFsponsorNumber(tSponsor.getFnumber());
		}
		tOrder.setFsellModel(sellModel);
		if (sellModel.intValue() == 1) {
			tOrder.setFverificationCode(NumberUtil.getVerificationCode());
		}
		

		tOrder = orderDAO.save(tOrder);
		// 减去对应商品库存
		for(TOrderGoods orderGoods:tOrderGoodsList){
			orderGoods.setForderId(tOrder.getId());
			goodsService.subtractStock(tOrderGoodsList);
		}
		// 已购买商品
		tOrderGoodsList = orderGoodsDAO.save(tOrderGoodsList);
		
		tOrder.setFgoodsImage(imageurls.toString().substring(0, imageurls.toString().length() - 1));

		tOrder.setFeventTitle(goodsTitle);
		tOrder.setFtotal(total);
		tOrder.setFreceivableTotal(total);

		// 如果用户选择了优惠券进行付款，则进行优惠券抵扣
		// 判断优惠券是否可用
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		if (StringUtils.isNotBlank(couponDeliveryId)) {
			TCouponDelivery tCouponDelivery = couponDeliveryDAO.getCouponbycustomer(couponDeliveryId, customer.getId(),
					customer.getFcreateTime());
			if (!couponService.chackCouponIsOK(customer, couponDeliveryId, tCouponDelivery)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(106);
				responseDTO.setMsg("您选定的无效的优惠券，暂无法进行下单！");
				return responseDTO;
			}
			TCouponInformation tCoupon = couponInformationDAO.findOne(tCouponDelivery.getTCouponInformation().getId());
			BigDecimal discount = BigDecimal.ZERO;
			if (tCoupon.getFdiscount() == null) {
				discount = tCoupon.getFamount();
			} else {
				discount = useCouponTotal.subtract(useCouponTotal
						.multiply(tCoupon.getFdiscount()).setScale(2, RoundingMode.HALF_UP));
			}
			total = tOrder.getFreceivableTotal().subtract(discount);
			if (total.compareTo(BigDecimal.ZERO) < 0) {
				total = BigDecimal.ZERO;
			}
			tOrder.setFtotal(total);
			tOrder.setFreceivableTotal(total);
			tOrder.setFchangeAmount(discount);
			tOrder.setFchangeAmountInstruction("使用了优惠券抵扣了" + discount.toString() + "元");
			tOrder = orderDAO.save(tOrder);
			// 如果是零元单，无需支付，所以将优惠券直接变更为20（已使用状态）
			couponService.usecoupon(customer, tCouponDelivery, tOrder);

			// 保存优惠金额
			TOrderAmountChange tOrderAmountChange = new TOrderAmountChange();
			tOrderAmountChange.setFbonusChange(0);
			tOrderAmountChange.setFbargainChange(BigDecimal.ZERO);
			tOrderAmountChange.setFcouponChange(discount);
			tOrderAmountChange.setFcreateTime(tOrder.getFcreateTime());
			tOrderAmountChange.setForderId(tOrder.getId());
			tOrderAmountChange.setFotherChange(BigDecimal.ZERO);
			tOrderAmountChange.setFspellChange(BigDecimal.ZERO);
			orderAmountChangeDAO.save(tOrderAmountChange);
		}
		
		// 如果是自营订单
		BigDecimal freight = BigDecimal.ZERO;
		if (sellModel != null && sellModel.intValue() == 0) {
			// 收货人信息
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

			freight = this.getFreight(total, tSponsor, 1);
			
			if (StringUtils.isNotBlank(postageCouponId)) {
				TCouponDelivery tCouponDelivery = couponDeliveryDAO.getCouponbycustomer(postageCouponId, customer.getId(),
						customer.getFcreateTime());
				if (!couponService.chackCouponIsOK(customer, postageCouponId, tCouponDelivery)) {
					responseDTO.setSuccess(false);
					responseDTO.setStatusCode(106);
					responseDTO.setMsg("您选定的无效的优惠券，暂无法进行下单！");
					return responseDTO;
				}
				TCouponInformation tCoupon = couponInformationDAO.findOne(tCouponDelivery.getTCouponInformation().getId());
				BigDecimal discount = tCoupon.getFamount();
				// 如果是零元单，无需支付，所以将优惠券直接变更为20（已使用状态）
				couponService.usecoupon(customer, tCouponDelivery, tOrder);

				// 保存优惠金额
				TOrderAmountChange tOrderAmountChange = new TOrderAmountChange();
				tOrderAmountChange.setFbonusChange(0);
				tOrderAmountChange.setFbargainChange(BigDecimal.ZERO);
				tOrderAmountChange.setFcouponChange(discount);
				tOrderAmountChange.setFcreateTime(tOrder.getFcreateTime());
				tOrderAmountChange.setForderId(tOrder.getId());
				tOrderAmountChange.setFotherChange(BigDecimal.ZERO);
				tOrderAmountChange.setFspellChange(BigDecimal.ZERO);
				orderAmountChangeDAO.save(tOrderAmountChange);
				
				//减去订单运费
				freight = BigDecimal.ZERO;
			}
		}
		tOrder.setFpostage(freight);
		tOrder.setFtotal(total.add(freight));
		tOrder.setFreceivableTotal(total.add(freight));
		tOrder = orderDAO.save(tOrder);

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
			ouci.setCustomerId(tOrder.getTCustomer().getId());
			ouci.setOrderId(tOrder.getId());
			ouci.setTotal(tOrder.getFtotal());
			ouci.setTaskType(2);
			AsynchronousTasksManager.put(ouci);

			returnData.put("zero", true);
			returnData.put("orderId", tOrder.getId());
			responseDTO.setMsg("订单支付成功!");

			// 添加线程任务发送购买成功通知短信
			OrderSendSmsBean oss = new OrderSendSmsBean();
			oss.setCreateTime(now);
			oss.setCustomerId(customerDTO.getCustomerId());
			oss.setOrderId(tOrder.getId());
			oss.setOrderNum(tOrder.getForderNum());
			oss.setTaskType(3);
			AsynchronousTasksManager.put(oss);
		} else {
			// 如果是非零元单，则记录订单状态变更为未支付状态
			fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 10);

			orderDAO.updateOrderStatus(10, tOrder.getId());
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

		// 如果是自营订单，则修改对应购物车缓存
		if (StringUtils.isBlank(goodsSkuList)) {
			redisService.removeCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId());
			redisService.putCache(RedisMoudel.shoppingcar, customerDTO.getCustomerId(), mapper.toJson(remainCartMap));
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public Charge appPay(Integer clientType, String ticket, Integer payType, Integer payClientType, String orderId,
			String addressIP,String sign) {
	    
		String signStr = fxlService.httpEncrypt(addressIP, clientType);
	    if (!signStr.equals(sign)) {
	      return null;
	    }
		
		// 根据用户选择的支付类型，来进行条用不同的支付接口
		ChargeParam chargeParam = null;
		Charge charge = new Charge();
		TOrder tOrder = orderDAO.findOne(orderId);
		// TPayInfo payInfo = null;
		if (payType.intValue() == 20) {
			if (clientType.intValue() == 1) {
				String openId = tOrder.getTCustomer().getFweixinId();
				chargeParam = new ChargeParam();
				chargeParam.setChannel("wx_pub");
				chargeParam.setOrderprice(tOrder.getFtotal().multiply(new BigDecimal(100)).intValue());
				chargeParam.setOrderNo(tOrder.getForderNum());
				chargeParam.setIp(addressIP);
				chargeParam.setSubject(tOrder.getFeventTitle());
				chargeParam.setBody(tOrder.getFeventTitle());
				charge = chargeService.createChargeForWx(chargeParam, openId);
			} else {
				chargeParam = new ChargeParam();
				chargeParam.setChannel("wx");
				chargeParam.setOrderprice(tOrder.getFtotal().multiply(new BigDecimal(100)).intValue());
				chargeParam.setOrderNo(tOrder.getForderNum());
				chargeParam.setIp(addressIP);
				chargeParam.setSubject(tOrder.getFeventTitle());
				chargeParam.setBody(tOrder.getFeventTitle());
				charge = chargeService.createCharge(chargeParam);
			}

		} else if (payType.intValue() == 30) {
			chargeParam = new ChargeParam();
			chargeParam.setOrderprice(tOrder.getFtotal().multiply(new BigDecimal(100)).intValue());
			chargeParam.setOrderNo(tOrder.getForderNum());
			chargeParam.setChannel("alipay");
			chargeParam.setIp(addressIP);
			chargeParam.setSubject(tOrder.getFeventTitle());
			chargeParam.setBody(tOrder.getFeventTitle());

			charge = chargeService.createCharge(chargeParam);
		}

		// 变更订单状态为待支付和支付类型是相应支付类型
		orderDAO.updateOrderStatusAndPayType(10, payType,clientType, tOrder.getId());
		return charge;
	}

	public ResponseDTO verifyPayCallback(HttpServletRequest request, String eventStr) {
		ResponseDTO responseDTO = new ResponseDTO();
		String signatureString = PingPPUtil.getSignatureString(request);
		if (StringUtils.isBlank(signatureString)) {
			throw new RuntimeException("获取签名失败");
		}
		// todo 验证签名
		// System.out.println("------------------------------------------" +
		// eventStr);
		boolean verifyOk = PingPPUtil.verifyData(eventStr, signatureString, PingPPUtil.getPubKey());
		if (verifyOk == false) {
			throw new RuntimeException("验证失败");
		}
		Event payEvent = Webhooks.eventParse(eventStr);
		Charge charge = (Charge) payEvent.getData().getObject();
		if (charge == null) {
			throw new RuntimeException("charge为空，支付失败");
		}
		String orderNo = charge.getOrderNo();
		TOrder order = orderDAO.getByFstatusAndForderNum(10, orderNo);
		if (order == null) {
			throw new RuntimeException("订单未找到");
		}
		TPayInfo tPayInfo = new TPayInfo();
		tPayInfo.setFcustomerId(order.getTCustomer().getId());
		tPayInfo.setForderId(order.getId());
		tPayInfo.setForderNum(order.getForderNum());
		tPayInfo.setFclientType(order.getFchannel());
		tPayInfo.setFinOut(1);
		tPayInfo.setFcurrencyType(charge.getCurrency());
		tPayInfo.setFpayAmount(new BigDecimal(charge.getAmount()).divide(new BigDecimal(100)));
		tPayInfo.setFpayType(order.getFpayType());
		tPayInfo.setFpayId(charge.getId());
		tPayInfo.setFstatus(10);
		tPayInfo.setFchannel(charge.getChannel());
		tPayInfo.setFcreateTime(new Date());

		if (charge.getPaid()) {
			tPayInfo.setFconfirmPayTime(new Date());
			tPayInfo.setFstatus(20);
			orderDAO.updateOrderStatusAndPayTime(20, new Date(), order.getId());
			this.orderStatusChange(4, order.getTCustomer().getFname(), order.getId(), null, 10, 20);
			if(order.getFsource()!=null&&order.getFsource()==20){
				customerBargainingDAO.updateStatus(order.getId(), 40);
			}
			// 删除订单超时未支付取消订单任务
			timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(order.getId(), 7);
			// 更改用户附加信息表
			OrderUpdateCustomerInfoBean ouci = new OrderUpdateCustomerInfoBean();
			ouci.setCreateTime(order.getFcreateTime());
			ouci.setCustomerId(order.getTCustomer().getId());
			ouci.setOrderId(order.getId());
			ouci.setTotal(order.getFtotal());
			ouci.setTaskType(2);
			AsynchronousTasksManager.put(ouci);

			// 添加线程任务发送购买成功通知短信
			OrderSendSmsBean oss = new OrderSendSmsBean();
			oss.setCreateTime(order.getFcreateTime());
			oss.setCustomerId(order.getTCustomer().getId());
			oss.setOrderId(order.getId());
			oss.setOrderNum(order.getForderNum());
			oss.setTaskType(3);
			oss.setCustomerName(order.getFcustomerName());
			oss.setCustomerPhone(order.getFcustomerPhone());
			AsynchronousTasksManager.put(oss);
			
			//添加自动确认订单
			if(order.getFsellModel().intValue()==0){
				TTimingTask timingTask = new TTimingTask();
				timingTask.setEntityId(order.getId());
				String confirmTime = ConfigurationUtil
						.getPropertiesValue(ResponseConfigurationDict.RESPONSE_CONFIRMTIME);
				timingTask.setTaskTime(DateUtils.addDays(new Date(), Integer.parseInt(confirmTime)).getTime());
				timingTask.setTaskType(21);
				timingTaskDAO.save(timingTask);
			}
		}
		tPayInfo = payInfoDAO.save(tPayInfo);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO deleteOrder(String ticket, Integer clientType, String orderId) {
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

		TOrder tOrder = orderDAO.findOne(orderId);

		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		orderDAO.updateOrderStatus(999, orderId);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("删除订单成功");
		return responseDTO;
	}

	public ResponseDTO getVerificationCode(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null || tOrder.getFsellModel().intValue() != 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		String verificationCode = tOrder.getFverificationCode();
		returnData.put("verificationCode", verificationCode);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取订单提货码成功");
		return responseDTO;
	}

	public ResponseDTO confirmGoods(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null || tOrder.getFsellModel().intValue() != 0 || tOrder.getFstatus() != 30) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		TExpress tExpress = new TExpress();
		tExpress = expressDAO.findByOrderId(orderId);
		if (tExpress == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的订单暂未送达！");
			return responseDTO;
		}
		Express ex = new Express();
		Map<String, Object> MapBody = new HashMap<String, Object>();

		MapBody.put("com", DictionaryUtil.getCode(DictionaryUtil.ExpressCode, tExpress.getFexYpressType()));
		MapBody.put("num", tExpress.getFexpressNum());
		MapBody.put("from", tExpress.getFstarting());
		MapBody.put("to", tExpress.getFreach());
		ex.setMapBody(MapBody);
		ResponseDTO dto = new ResponseDTO();
		dto = ExpressUtil.sendExPress(ex);
		ExpressDTO expressDTO = (ExpressDTO) dto.getData().get("track");
		Integer state = null;
		if(expressDTO!=null&&expressDTO.getState()!=null){
			state = Integer.parseInt(expressDTO.getState());
		}
		if (state == null || state.intValue() != 3) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(105);
			responseDTO.setMsg("您的订单暂未送达！");
			return responseDTO;
		}
		
		fxlService.orderStatusChange(1, customerDTO.getName(), tOrder.getId(), null, 0, 70);

		orderDAO.updateOrderStatus(70, tOrder.getId());

		responseDTO.setMsg("用户确认收货成功");
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	private boolean todayOrder(String customerId, String eventId) {
		boolean limition = true;
		StringBuilder hql = new StringBuilder();
		Date now = new Date();
		hql.append("select t.id from ").append(" TOrder t left join TOrderGoods o on t.id = o.forderId ")
				.append(" where o.feventId = :eventId and t.TCustomer.id = :customerId and t.fstatus < 100 ");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("eventId", eventId);
		hqlMap.put("customerId", customerId);
		hql.append(" and t.fcreateTime >= :fcreateTimeStart and t.fcreateTime <= :fcreateTimeEnd");
		hqlMap.put("fcreateTimeStart", DateUtil.getDay(now));
		hqlMap.put("fcreateTimeEnd", now);

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (list == null || list.size() <= 0) {
			limition = false;
		}
		return limition;
	}

	// 下单成功付款之后，改变用户附加信息表中的下单金额
	public void updateCustomerInfo(OrderUpdateCustomerInfoBean bean) {
		try {
			TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(bean.getCustomerId());
			tCustomerInfo.setForderTotal(tCustomerInfo.getForderTotal().add(bean.getTotal()));
			tCustomerInfo.setFpayOrderNumber(tCustomerInfo.getFpayOrderNumber() + 1);
			tCustomerInfo.setForderNumber(tCustomerInfo.getForderNumber() + 1);
			if (bean.getTotal().compareTo(BigDecimal.ZERO) == 0) {
				tCustomerInfo.setFpayZeroOrderNumber(tCustomerInfo.getFpayZeroOrderNumber() + 1);
			}
			if (tCustomerInfo.getFfirstOrderTime() == null) {
				tCustomerInfo.setFfirstOrderTime(bean.getCreateTime());
			}

			List<TCustomerSubscribe> customerSubscribe = customerSubscribeDAO.getByOpertionId(bean.getCustomerId(), 0);
			if (customerSubscribe != null && customerSubscribe.size() == 1) {
				String deliveryId = DictionaryUtil.getString(DictionaryUtil.InvitationCoupon, 2);
				if (deliveryId != null && !deliveryId.equals("false")) {
					String desc = DictionaryUtil.getCode(DictionaryUtil.InvitationCoupon, 2);
					// 修改fans关联下单
					TCustomerSubscribe subscribe = customerSubscribe.get(0);
					subscribe.setFcouponValue(desc);
					subscribe.setFtype(1);
					subscribe.setFupdateTime(bean.getCreateTime());
					customerSubscribeDAO.save(subscribe);
					// 邀请人下单粉丝数加1
					TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(subscribe.getFcustomerId());
					Integer fans = 0;
					if (customerInfo.getForderFans() != null) {
						fans = customerInfo.getForderFans() + 1;
					}
					customerInfo.setForderFans(fans);
					// 给邀请人发送代金券
					couponsService.receiveCoupon(deliveryId, subscribe.getFcustomerId());
				}
			}

			String bonus = DictionaryUtil.getCode(DictionaryUtil.BonusType, 1);
			String growthValue = DictionaryUtil.getCode(DictionaryUtil.BonusType, 2);
			if (bonus != null && !bonus.equals("0")) {
				TCustomer customer = customerDAO.findOne(bean.getCustomerId());
				int growth = (bean.getTotal().intValue())/(Integer .parseInt(growthValue));
				if (tCustomerInfo.getFneedGrowthValue().intValue() <= growth) {
					if(tCustomerInfo.getFlevel()<5){
						tCustomerInfo.setFlevel(tCustomerInfo.getFlevel() + 1);
						TCustomerLevel customerLevel = customerLevelDAO.getByLevel(tCustomerInfo.getFlevel() + 1);
						tCustomerInfo.setFneedGrowthValue(customerLevel.getFgrowthValue() - tCustomerInfo.getFgrowthValue()
								-growth);
					}else{
						tCustomerInfo.setFneedGrowthValue(tCustomerInfo.getFneedGrowthValue().intValue() - growth);
					}
				} else {
					tCustomerInfo.setFneedGrowthValue(
							tCustomerInfo.getFneedGrowthValue().intValue() - growth);
				}
				tCustomerInfo.setFgrowthValue(tCustomerInfo.getFgrowthValue() + growth);
				int ub = (bean.getTotal().intValue())/(Integer .parseInt(bonus));
				if(ub>0){
					tCustomerInfo.setFpoint(tCustomerInfo.getFpoint() + ub);
					TCustomerBonus tCustomerBonus = new TCustomerBonus();
					tCustomerBonus.setFcreateTime(bean.getCreateTime());
					tCustomerBonus.setFbonus(ub);
					tCustomerBonus.setFcustermerId(bean.getCustomerId());
					tCustomerBonus.setFobject(bean.getOrderId());
					tCustomerBonus.setFtype(1);
					customerBonusDAO.save(tCustomerBonus);
				}

				if (tCustomerInfo != null && tCustomerInfo.getFregisterDeviceTokens() != null) {
					pushService.bonusAccount(bonus, tCustomerInfo.getFregisterDeviceTokens(), bean.getCustomerId(),
							customer.getFname());
				}

			}
			customerInfoDAO.save(tCustomerInfo);
		} catch (Exception e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", bean.getCustomerId());
			map.put("orderId", bean.getOrderId());
			OutPutLogUtil.printLoggger(e, map, logger);
			throw new ServiceException("下单时异步写入订单统计到用户附加信息表出错。");
		}
	}

	public ResponseDTO againToPayOrder(Integer clientType, String ticket, String orderId, Integer payClientType,
			Integer payType, String ip) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TOrder tOrder = orderDAO.findOne(orderId);
		if (tOrder == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不存在！");
			return responseDTO;
		}
		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() == 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("该订单已完成支付，请勿重复操作！");
			return responseDTO;
		}
		List<TOrderGoods> orderGoods = orderGoodsDAO.findByOrderId(orderId);

		Map<String, Object> returnData = Maps.newHashMap();

		// 根据用户选择的支付类型，来进行条用不同的支付接口
		WxPayResult wxPayResult = null;
		if (payType.intValue() == 20) {
			// 调用微信支付操作
			try {
				// TODO根据支付方式调用不同的支付接口，获取prePayId返回给前台
				String nonceStr = RandomStringUtils.randomAlphanumeric(32);

				// ip = "192.168.1.1";
				Date now = new Date();
				if (payClientType.intValue() == 1) {
					wxPayResult = WxPayUtil.wxPay(tOrder.getForderNum(), orderGoods.get(0).getFeventTitle(),
							tOrder.getFtotal().multiply(new BigDecimal(100)).intValue(), customerDTO.getWxId(), now,
							DateUtils.addMinutes(now, 6), ip, nonceStr);
				} else {
					wxPayResult = WxPayUtil.wxAppPay(tOrder.getForderNum(), orderGoods.get(0).getFeventTitle(),
							tOrder.getFtotal().multiply(new BigDecimal(100)).intValue(), now,
							DateUtils.addMinutes(now, 6), ip, nonceStr);
				}
				logger.info(wxPayResult.getResponse());
				orderDAO.updatePayType(orderId, payType);

				TWxPay tWxPay = wxPayDAO.getByOrderIdAndInOutAndStatus(tOrder.getId(), 1, 10);
				if (tWxPay == null) {
					tWxPay = new TWxPay();
					tWxPay.setFclientType(payClientType);
					tWxPay.setFinOut(1);
					tWxPay.setFcreateTime(now);
					tWxPay.setForderId(tOrder.getId());
					tWxPay.setForderType(1);
					tWxPay.setTCustomer(tOrder.getTCustomer());
					tWxPay.setFstatus(10);
				}
				tWxPay.setFupdateTime(now);
				tWxPay.setFppResponseInfo(wxPayResult.getResponse());
				wxPayDAO.save(tWxPay);

			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(105);
				responseDTO.setMsg("调用微信支付接口时出错，请稍后再进行支付！");
				return responseDTO;
			}
		}

		PayDTO payDTO = new PayDTO();
		payDTO.setAppId(wxPayResult.getAppId());
		payDTO.setPartnerId(wxPayResult.getPartnerId());
		payDTO.setPayType(payType);
		payDTO.setOrderId(tOrder.getId());
		payDTO.setOrderNum(tOrder.getForderNum());
		payDTO.setPrepayId(wxPayResult.getPrepayId());
		payDTO.setNonceStr(wxPayResult.getNonceStrVal());
		payDTO.setPaySign(wxPayResult.getPaySign());
		payDTO.setTimestamp(wxPayResult.getTimestamp());
		payDTO.setSignType(wxPayResult.getSignType());
		payDTO.setPayPackage(wxPayResult.getPayPackage());

		returnData.put("zero", false);
		returnData.put("payInfo", payDTO);
		responseDTO.setData(returnData);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("调用微信支付接口成功！");
		return responseDTO;
	}

	public ResponseDTO getDiscountAmount(Integer clientType, String ticket, String orderTotal, String couponDeliveryId,
			String postageCouponId,String freight, String goodsSkuList) {
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
		if (goodsSkuList == null) {
			goodsSkuList = "";
		}
		responseDTO = couponsService.getDiscountAmount(customerId, orderTotal, couponDeliveryId== null ? "": couponDeliveryId,
				postageCouponId == null ? "": postageCouponId, freight, goodsSkuList, clientType);

		return responseDTO;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendPayZeroSuccessSms(OrderSendSmsBean tOrder) {
		if (SmsUtil.isPayZeroSuccessSwitch()) {
			TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(tOrder.getCustomerId());
			if (customerInfo != null && customerInfo.getFregisterDeviceTokens() != null) {
				pushService.successPayment(tOrder.getOrderNum(), tOrder.getOrderId(),
						customerInfo.getFregisterDeviceTokens(), tOrder.getCustomerId());
			}
		}
		String msg = new StringBuilder().append("【查找优惠】")
				.append(DateFormatUtils.format(new Date(), "yyyy年MM月dd日HH时mm分")).append("有新的订单啦，")
				.append("【用户名：").append(tOrder.getCustomerName()).append("】").append("【订单号：")
				.append(tOrder.getOrderNum()).append("】").append("，快去确认订单").toString();
		DingTalkUtil.sendDingTalk(msg, DingTalkUtil.getPaySuccessSyncingDingTalk());
	}

	public ResponseDTO refundCallback(HttpServletRequest request, String eventStr) {
		ResponseDTO responseDTO = new ResponseDTO();
		String signatureString = PingPPUtil.getSignatureString(request);
		if (StringUtils.isBlank(signatureString)) {
			throw new RuntimeException("获取签名失败");
		}
		// todo 验证签名
		// System.out.println("------------------------------------------" +
		// eventStr);
		boolean verifyOk = PingPPUtil.verifyData(eventStr, signatureString, PingPPUtil.getPubKey());
		if (verifyOk == false) {
			throw new RuntimeException("验证失败");
		}
		Event payEvent = Webhooks.eventParse(eventStr);
		Refund refund = (Refund) payEvent.getData().getObject();
		if (refund == null) {
			throw new RuntimeException("refund为空，退款失败");
		}
		String orderNo = refund.getChargeOrderNo();
		TOrder order = orderDAO.getByFstatusAndForderNum(115, orderNo);
		if (order == null) {
			throw new RuntimeException("订单未找到");
		}
		TPayInfo tPayInfo = new TPayInfo();
		tPayInfo.setFcustomerId(order.getTCustomer().getId());
		tPayInfo.setForderId(order.getId());
		tPayInfo.setForderNum(order.getForderNum());
		tPayInfo.setFclientType(order.getFchannel());
		tPayInfo.setFinOut(2);
		tPayInfo.setFcurrencyType("");
		tPayInfo.setFpayAmount(new BigDecimal(refund.getAmount()).divide(new BigDecimal(100)));
		tPayInfo.setFpayType(order.getFpayType());
		tPayInfo.setFpayId(refund.getId());
		tPayInfo.setFstatus(10);
		tPayInfo.setFcreateTime(new Date());
		tPayInfo.setFcurrencyType("cny");

		if (refund.getStatus().equals("succeeded")) {
			TPayInfo pay = payInfoDAO.findByPayId(refund.getCharge());
			tPayInfo.setFconfirmPayTime(new Date());
			tPayInfo.setFstatus(120);//退款
			tPayInfo.setFchannel(pay.getFchannel());

			responseDTO = couponsService.backCoupon(order.getId(), order.getFstatus());

			fxlService.orderStatusChange(1, "ping++", order.getId(), "", 115, 120);
			orderDAO.updateOrderStatusAndRefundTimeAndRefundReason(120, new Date(), "", order.getId());
			// 将订单占用的库存数量退回
			List<TOrderGoods> list = orderGoodsDAO.findByOrderId(order.getId());
			goodsService.backStock(list);

			TCustomerInfo customerInfo = customerInfoDAO.getByCustomerId(order.getTCustomer().getId());
			if(customerInfo!=null ){
				if(customerInfo.getFregisterDeviceTokens()!=null){
					pushService.refund(order.getForderNum(), order.getId(),
							customerInfo.getFregisterDeviceTokens(), order.getTCustomer().getId());
					
				}
				String bonus = DictionaryUtil.getCode(DictionaryUtil.BonusType, 1);
				String growthValue = DictionaryUtil.getCode(DictionaryUtil.BonusType, 2);
				if (bonus != null && !bonus.equals("0")) {
					int ub = (order.getFtotal().intValue())/(Integer .parseInt(bonus));
					int growth = (order.getFtotal().intValue())/(Integer .parseInt(growthValue));
					TCustomerLevel customerLevel = customerLevelDAO.getByLevel(customerInfo.getFlevel());
					if (customerLevel!=null&&customerLevel.getFgrowthValue()>(customerInfo.getFgrowthValue()-growth)) {
						customerInfo.setFlevel(customerInfo.getFlevel() - 1);
						customerInfo.setFneedGrowthValue(customerInfo.getFneedGrowthValue()-customerInfo.getFgrowthValue()+growth);
					} else {
						customerInfo.setFneedGrowthValue(
								customerInfo.getFneedGrowthValue().intValue() + growth);
					}
					customerInfo.setFgrowthValue(customerInfo.getFgrowthValue() - growth);
					customerInfo.setFpoint(customerInfo.getFpoint() - ub);
					TCustomerBonus tCustomerBonus = new TCustomerBonus();
					tCustomerBonus.setFcreateTime(new Date());
					tCustomerBonus.setFbonus(-ub);
					tCustomerBonus.setFcustermerId(order.getTCustomer().getId());
					tCustomerBonus.setFobject(order.getId());
					tCustomerBonus.setFtype(5);
					customerBonusDAO.save(tCustomerBonus);
				}
			}

		}
		tPayInfo = payInfoDAO.save(tPayInfo);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public void orderStatusChange(int operatorType, String operatorId, String orderId, String changeReason,
			int beforeStatus, int afterStatus) {
		Date now = new Date();

		TOrderStatusChange tOrderStatusChange = new TOrderStatusChange();
		tOrderStatusChange.setFoperatorId(operatorId);
		tOrderStatusChange.setFoperatorType(operatorType);
		tOrderStatusChange.setTOrder(new TOrder(orderId));
		
		tOrderStatusChange.setFcreateTime(now);
		tOrderStatusChange.setFupdateTime(now);
		tOrderStatusChange.setFchangeReason(changeReason);
		tOrderStatusChange.setFbeforeStatus(beforeStatus);
		tOrderStatusChange.setFafterStatus(afterStatus);

		orderStatusChangeDAO.save(tOrderStatusChange);
	}
	
	public ResponseDTO commentGoodsList(Integer clientType, String ticket, Integer status,Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (status == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("status参数不能为空，请检查status的传递参数值！");
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
		
		StringBuilder hql = new StringBuilder();
		hql.append("select o.id as orderGoodsIs, e.fimage1 as goodsImage,o.fgoodsSubTitle as fgoodsSubTitle, o.fspec as spec,")
				.append(" o.fpromotionModel as fpromotionModel,o.fspec as fspec,o.fspec as fspec,o.fstatus as commentStatus ,")
				.append(" o.forderId as orderId,o.feventId as goodsId,o.feventTitle as feventTitle,o.fprice as price,o.fcount as count,o.ftotalPrice as totalPrice")
				.append(" from TOrder t left join TOrderGoods o on t.id = o.forderId inner join TEvent e on e.id = o.feventId ")
				.append(" where t.TCustomer.id = :customerId and t.fstatus = 70");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("customerId", customerDTO.getCustomerId());
		if(status!=null){
			hql.append(" and o.fstatus = :fstatus");// 0.自营
			hqlMap.put("fstatus", status);
		}

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<CommentGoodsDTO> commentGoodsDTOList = Lists.newArrayList();

		CommentGoodsDTO commentGoodsDTO = null;
		for (Map<String, Object> amap : list) {
			// 单个商品信息
			commentGoodsDTO = new CommentGoodsDTO();
			if (amap.get("orderGoodsIs") != null && StringUtils.isNotBlank(amap.get("orderGoodsIs").toString())) {
				commentGoodsDTO.setCommentGoodsId(amap.get("orderGoodsIs").toString());
			}
			if (amap.get("goodsId") != null && StringUtils.isNotBlank(amap.get("goodsId").toString())) {
				commentGoodsDTO.setGoodsId(amap.get("goodsId").toString());
			}
			if (amap.get("feventTitle") != null && StringUtils.isNotBlank(amap.get("feventTitle").toString())) {
				commentGoodsDTO.setGoodsName(amap.get("feventTitle").toString());
			}
			if (amap.get("fgoodsSubTitle") != null && StringUtils.isNotBlank(amap.get("fgoodsSubTitle").toString())) {
				commentGoodsDTO.setGoodsSubTitle(amap.get("fgoodsSubTitle").toString());
			}
			if (amap.get("price") != null && StringUtils.isNotBlank(amap.get("price").toString())) {
				commentGoodsDTO.setPrice((BigDecimal) amap.get("price"));// 单个商品价格
			}
			if (amap.get("fspec") != null && StringUtils.isNotBlank(amap.get("fspec").toString())) {
				commentGoodsDTO.setSpec(amap.get("fspec").toString());
			}
			if (amap.get("goodsImage") != null && StringUtils.isNotBlank(amap.get("goodsImage").toString())) {
				commentGoodsDTO.setGoodsImage(fxlService.getImageUrl(amap.get("goodsImage").toString(), false));
			}
			if (amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				commentGoodsDTO.setGoodsNum((Integer) amap.get("count"));
				commentGoodsDTO.setCountPrice(
						((BigDecimal) amap.get("price")).multiply(new BigDecimal(amap.get("count").toString()))
								.setScale(2, RoundingMode.HALF_UP).toString());
			}
			if (amap.get("commentStatus") != null && StringUtils.isNotBlank(amap.get("commentStatus").toString())) {
				commentGoodsDTO.setCommentStatus((Integer) amap.get("commentStatus"));
			}
			commentGoodsDTO.setCommentdes("评价商品最多可获得20U币");
			commentGoodsDTOList.add(commentGoodsDTO);
		}

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("commentGoodsList", commentGoodsDTOList);
		responseDTO.setData(returnData);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("用户商品评价列表加载成功！");
		return responseDTO;
	}
	
	public String ordercode(TOrder tOrder){
		// 商家编号作为加密的秘钥
		String fsponsorNumber = tOrder.getFsponsorNumber();
		// 生成自动核销编码
		StringBuilder allKey = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			allKey.append(fsponsorNumber);
		}
		logger.info("allKey：" + allKey);
		String orderNumAndSponsorNum = new StringBuilder().append(tOrder.getForderNum()).append("###")
				.append(fsponsorNumber).toString();
		logger.info("orderNumAndSponsorNum：" + orderNumAndSponsorNum);
		String verificationCodeAuto = null;
		try {
			verificationCodeAuto = ByteArrayUtil.toHexString(Cryptos.aesEncrypt(orderNumAndSponsorNum.getBytes(),
					allKey.substring(0, 16).toString().getBytes()));
			logger.info("加密结果：" + verificationCodeAuto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verificationCodeAuto;
	}
	
}