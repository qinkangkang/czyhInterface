package com.innee.czyhInterface.service.v2;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.CouponActivitycouponsDAO;
import com.innee.czyhInterface.dao.CouponDAO;
import com.innee.czyhInterface.dao.CouponDeliveryDAO;
import com.innee.czyhInterface.dao.CouponDeliveryHistoryDAO;
import com.innee.czyhInterface.dao.CouponInformationDAO;
import com.innee.czyhInterface.dao.CouponShareOrderDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.TimingTaskDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.AppShareDTO;
import com.innee.czyhInterface.dto.m.CouponChannelDTO;
import com.innee.czyhInterface.dto.m.CouponDTO;
import com.innee.czyhInterface.dto.m.CouponInofDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCoupon;
import com.innee.czyhInterface.entity.TCouponActivitycoupons;
import com.innee.czyhInterface.entity.TCouponDelivery;
import com.innee.czyhInterface.entity.TCouponDeliveryHistory;
import com.innee.czyhInterface.entity.TCouponInformation;
import com.innee.czyhInterface.entity.TCouponShareOrder;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TDelivery;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.log.OutPutLogUtil;

/**
 * 优惠券service
 * 
 * @author Duke
 *
 */
@Component
@Transactional
public class CouponService {

	private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

	@Autowired
	private CouponDAO couponDAO;

	@Autowired
	private CommonService commonService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CouponInformationDAO couponInformationDAO;

	@Autowired
	private CouponDeliveryDAO couponDeliveryDAO;

	@Autowired
	private CouponActivitycouponsDAO couponActivitycouponsDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CouponDeliveryHistoryDAO couponDeliveryHistoryDAO;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private CouponShareOrderDAO couponShareOrderDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;
	
	@Autowired
	private TimingTaskDAO timingTaskDAO;

	public ResponseDTO couponExplain() {
		ResponseDTO responseDTO = new ResponseDTO();
		List<String> datalist = Lists.newArrayList();
		Map<String, Object> returnData = Maps.newHashMap();
		datalist.add("1.下载零到壹App，用手机号注册或将手机绑定至已注册的零到壹账户后即可使用；");
		datalist.add("2.使用优惠券下单手机号需要与领取优惠时的手机号一致，领取后在有效期内使用；");
		datalist.add("3.每个订单只能用一张优惠券，不能与其它的券叠加使用，且不与找零，订单申请退款时优惠券不退；");
		datalist.add("4.在法律法规许可的范围内，零到壹对本次活动拥有解释权，如有问题，请联系零到壹客服电话：010-53689210;");
		returnData.put("couponExplainInfo", datalist);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO findCouponInfo(String couponId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(couponId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("couponId参数不能为空，请检查couponId的传递参数值！");
			return responseDTO;
		}
		TCoupon tCoupon = couponDAO.findOne(couponId);
		if (tCoupon == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("couponId参数有误，这是个无效的couponId！");
			return responseDTO;
		}
		CouponDTO couponDTO = new CouponDTO();
		if (StringUtils.isNotBlank(tCoupon.getId())) {
			couponDTO.setCouponId(tCoupon.getId());
			;
		}

		if (StringUtils.isNotBlank(tCoupon.getFtitle())) {
			couponDTO.setTitle(tCoupon.getFtitle());
		}
		if (tCoupon.getFuseStartTime() != null && StringUtils.isNotBlank(tCoupon.getFuseStartTime().toString())) {
			couponDTO.setUseStartTime(DateFormatUtils.format(tCoupon.getFuseStartTime(), "yyyy-MM-dd"));
		}
		if (tCoupon.getFuseEndTime() != null && StringUtils.isNotBlank(tCoupon.getFuseEndTime().toString())) {
			couponDTO.setUseEndTime(DateFormatUtils.format(tCoupon.getFuseEndTime(), "yyyy-MM-dd"));
		}
		if (tCoupon.getFlimitation() != null && StringUtils.isNotBlank(tCoupon.getFlimitation().toString())) {
			couponDTO.setLimitation(tCoupon.getFlimitation());
			couponDTO.setLimitationInfo(
					new StringBuffer().append("满").append(couponDTO.getLimitation()).append("元可使用").toString());
		} else if (tCoupon.getFamount() != null && StringUtils.isNotBlank(tCoupon.getFamount().toString())) {
			couponDTO.setLimitationInfo(
					new StringBuffer().append("直减").append(tCoupon.getFamount()).append("元").toString());
		}
		if (tCoupon.getFdiscount() != null && StringUtils.isNotBlank(tCoupon.getFdiscount().toString())) {
			BigDecimal discount = ((BigDecimal) tCoupon.getFdiscount()).multiply(new BigDecimal(10));
			couponDTO.setDiscount(discount);
		}
		if (tCoupon.getFamount() != null && StringUtils.isNotBlank(tCoupon.getFamount().toString())) {
			couponDTO.setAmount(tCoupon.getFamount());
		}
		if (tCoupon.getFuseRange() != null && StringUtils.isNotBlank(tCoupon.getFuseRange())) {
			couponDTO.setUseRange(tCoupon.getFuseRange());
			couponDTO.setLimitationClient(tCoupon.getFuseRange());
		}
		/*
		 * if (tCoupon.getstatus != null &&
		 * StringUtils.isNotBlank(tCoupon.get("status").toString())) {
		 * couponDTO.setStatus((Integer) tCoupon.get("status")); }
		 */
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("couponInfo", couponDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO appShareCoupon(String couponId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(couponId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("couponId参数不能为空，请检查couponId的传递参数值！");
			return responseDTO;
		}
		TCoupon tCoupon = couponDAO.findOne(couponId);
		if (tCoupon == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("couponId参数有误，这是个无效的couponId！");
			return responseDTO;
		}

		AppShareDTO appShareDTO = new AppShareDTO();

		appShareDTO.setBrief("更宽广的视野，更高效的陪伴。零到壹集合3-12岁小朋友最喜欢、最优惠的活动，赶快带上孩子来体验。");
		appShareDTO.setImageUrl("http://www.fangxuele.com/img/logo_full.png");
		appShareDTO.setTitle("零到壹&五彩城送您一份亲子大礼包");
		appShareDTO.setUrl("http://www.fangxuele.com/p");
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("appSubject", appShareDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	// 获取某活动可领取的优惠券
	public List<CouponDTO> getCouponByEvent(CustomerDTO customerDTO, TEvent tEvent, boolean isHadTicket) {

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		// 获取所有的可内部领券活动
		hql.append(
				"select c.id as couponId,c.ftitle as title,c.fuseStartTime as useStartTime ,c.fuseEndTime as useEndTime,c.fuseRange as useRange,c.famount as amount,c.flimitation as limitation,c.fvalidDays as validDays,")
				.append(" c.fdiscount as discount,a.fdeliveryCount as count,a.fsendCount as sendCount,o.fobjectId as objectid,o.fuseType as usetype,d.freciveLimit as reciveLimit")
				.append(" from TCouponInformation c inner join c.TCouponObjects o inner join TCouponActivitycoupons a on a.fcouponId = c.id inner join TDelivery d  on d.id = a.fdeliveryId")
				.append(" where d.freciveChannel = 0 and d.factivityType = 20 and d.fstatus = 40 and d.fdeliveryStartTime <= :now and d.fdeliveryEndTime >= :now order by c.fcreateTime asc");

		Date now = new Date();
		hqlMap.put("now", now);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		// 过滤出只适合该活动可领的优惠券
		List<Map<String, Object>> couponList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> cmap : list) {
			if (Integer.parseInt(cmap.get("usetype").toString()) == 10) {
				couponList.add(cmap);
			} else if (Integer.parseInt(cmap.get("usetype").toString()) == 40
					&& cmap.get("objectid").toString().equals(tEvent.getId().toString())) {
				couponList.add(cmap);
			} else if (Integer.parseInt(cmap.get("usetype").toString()) == 20
					&& cmap.get("objectid").toString().equals(tEvent.getTSponsor().getId())) {
				couponList.add(cmap);
			} else if (Integer.parseInt(cmap.get("usetype").toString()) == 30
					&& cmap.get("objectid").toString().equals(tEvent.getFtypeA().toString())) {
				couponList.add(cmap);
			}
		}
		List<CouponDTO> dataList = Lists.newArrayList();
		CouponDTO couponDTO = null;
		for (Map<String, Object> amap : couponList) {
			couponDTO = new CouponDTO();
			if (amap.get("couponId") != null && StringUtils.isNotBlank(amap.get("couponId").toString())) {
				couponDTO.setCouponId(amap.get("couponId").toString());
				if (isHadTicket) {
					couponDTO.setReceive(isReceiveCoupon(amap.get("couponId").toString(),
							(Integer) amap.get("reciveLimit"), customerDTO.getCustomerId()));
				}
			}
			if (amap.get("title") != null && StringUtils.isNotBlank(amap.get("title").toString())) {
				couponDTO.setTitle(amap.get("title").toString());
			}
			// 计算券的有效期
			Date useStartTime = null;
			Date useEndTime = null;
			if (amap.get("validDays") != null && StringUtils.isNotBlank(amap.get("validDays").toString())) {
				SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
				try {
					useStartTime = DateUtils.parseDate(sdfstart.format(now).toString(), "yyyy-MM-dd HH:mm:ss");
					Calendar c = Calendar.getInstance();
					c.setTime(useStartTime);
					c.set(Calendar.DATE, c.get(Calendar.DATE) + (Integer) amap.get("validDays") - 1);
					useEndTime = DateUtils.parseDate(sdfend.format(c.getTime()).toString(), "yyyy-MM-dd HH:mm:ss");
				} catch (ParseException e) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("eventId", tEvent.getId());
					map.put("customerId", customerDTO.getCustomerId());
					OutPutLogUtil.printLoggger(e, map, logger);
				}
			} else {
				if (amap.get("useStartTime") != null && StringUtils.isNotBlank(amap.get("useStartTime").toString())) {
					useStartTime = (Date) amap.get("useStartTime");
				}
				if (amap.get("useEndTime") != null && StringUtils.isNotBlank(amap.get("useEndTime").toString())) {
					useEndTime = (Date) amap.get("useEndTime");
				}
			}

			if (useStartTime != null) {
				couponDTO.setUseStartTime(DateFormatUtils.format(useStartTime, "yyyy-MM-dd"));
			}
			if (useEndTime != null) {
				couponDTO.setUseEndTime(DateFormatUtils.format(useEndTime, "yyyy-MM-dd"));
			}

			if (amap.get("limitation") != null && StringUtils.isNotBlank(amap.get("limitation").toString())) {
				couponDTO.setLimitation((BigDecimal) amap.get("limitation"));
				couponDTO.setLimitationInfo("满" + amap.get("limitation") + "元可使用");
			} else {
				couponDTO.setLimitationInfo("直减" + amap.get("amount") + "元");
			}
			if (amap.get("amount") != null && StringUtils.isNotBlank(amap.get("amount").toString())) {
				couponDTO.setAmount((BigDecimal) amap.get("amount"));
			}
			if (amap.get("useRange") != null && StringUtils.isNotBlank(amap.get("useRange").toString())) {
				couponDTO.setUseRange(amap.get("useRange").toString());
				couponDTO.setLimitationClient(amap.get("useRange").toString());
			}
			if (amap.get("sendCount") != null && StringUtils.isNotBlank(amap.get("sendCount").toString())
					&& amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				if ((Integer) amap.get("count") > 0 && (Integer) amap.get("count") <= (Integer) amap.get("sendCount")) {
					couponDTO.setReceiveFinish(true);
				}
			}
			dataList.add(couponDTO);
		}

		return dataList;
	}

	/**
	 * 用户领取优惠券service
	 * 
	 * @param couponId
	 *            优惠券id
	 * @param ticket
	 *            用户票
	 * @param type
	 * @return
	 */
	public ResponseDTO receiveCoupon(Integer clientType, String couponId, String ticket, Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(couponId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("couponId参数不能为空，请检查couponId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
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

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select c.id as couponId,d.id as deliveryId,c.ftitle as title,c.fuseStartTime as useStartTime ,c.fuseEndTime as useEndTime,c.fuseRange as useRange,c.famount as amount,c.flimitation as limitation,")
				.append(" c.fdiscount as discount,a.fdeliveryCount as count,a.fsendCount as sendCount,d.freciveLimit as reciveLimit,c.fvalidDays as validDays")
				.append(" from TCouponInformation c inner join TCouponActivitycoupons a on a.fcouponId = c.id inner join TDelivery d  on d.id = a.fdeliveryId")
				.append(" where d.factivityType = 20 and d.fstatus = 40 and d.fdeliveryStartTime <= :now and d.fdeliveryEndTime >= :now");

		Date now = new Date();
		hqlMap.put("now", now);
		hql.append(" and c.id = :couponId");
		hqlMap.put("couponId", couponId);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (list == null || list.size() != 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setMsg("该优惠券已过期！");
			return responseDTO;
		}
		Map<String, Object> cMap = list.get(0);

		boolean isReceiveCoupon = isReceiveCoupon(couponId, (Integer) cMap.get("reciveLimit"),
				customerDTO.getCustomerId());
		if (isReceiveCoupon) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您已经拥有该优惠券，不能重复领取！");
			return responseDTO;
		}
		if ((Integer) cMap.get("count") > 0 && (Integer) cMap.get("count") <= (Integer) cMap.get("sendCount")) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(109);
			responseDTO.setMsg("该优惠券领取完了！");
			return responseDTO;
		}

		// 计算券的有效期
		Date useStartTime = null;
		Date useEndTime = null;
		if (cMap.get("validDays") != null && StringUtils.isNotBlank(cMap.get("validDays").toString())) {
			SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			try {
				useStartTime = DateUtils.parseDate(sdfstart.format(now).toString(), "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();
				c.setTime(useStartTime);
				c.set(Calendar.DATE, c.get(Calendar.DATE) + (Integer) cMap.get("validDays") - 1);
				useEndTime = DateUtils.parseDate(sdfend.format(c.getTime()).toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponId", couponId);
				map.put("customerId", customerDTO.getCustomerId());
				OutPutLogUtil.printLoggger(e, map, logger);
			}
		} else {
			useStartTime = (Date) cMap.get("useStartTime");
			useEndTime = (Date) cMap.get("useEndTime");
		}

		TCouponDelivery couponDelivery = new TCouponDelivery();
		couponDelivery.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		couponDelivery.setFdeliverTime(new Date());
		couponDelivery.setTCouponInformation(new TCouponInformation(couponId));
		couponDelivery.setTDelivery(new TDelivery(cMap.get("deliveryId").toString()));
		couponDelivery.setFuseEndTime(useEndTime);
		couponDelivery.setFuseStartTime(useStartTime);
		couponDelivery = couponDeliveryDAO.save(couponDelivery);
		Map<String, Object> returnData = Maps.newHashMap();
		if (type == 1) {
			CouponDTO couponDTO = new CouponDTO();
			if (StringUtils.isNotBlank(couponDelivery.getId())) {
				couponDTO.setCouponDeliveryId(couponDelivery.getId());
			}
			couponDTO.setCouponId(couponId);
			if (StringUtils.isNotBlank(cMap.get("title").toString())) {
				couponDTO.setTitle(cMap.get("title").toString());
			}

			couponDTO.setUseStartTime(DateFormatUtils.format(useStartTime, "yyyy-MM-dd"));

			couponDTO.setUseEndTime(DateFormatUtils.format(useEndTime, "yyyy-MM-dd"));

			if (cMap.get("limitation") != null && StringUtils.isNotBlank(cMap.get("limitation").toString())) {
				couponDTO.setLimitation(new BigDecimal(cMap.get("limitation").toString()));
				couponDTO.setLimitationInfo(
						new StringBuffer().append("满").append(cMap.get("limitation")).append("元可使用").toString());
			} else {
				couponDTO.setLimitationInfo(
						new StringBuffer().append("直减").append(cMap.get("amount")).append("元").toString());
			}
			if (cMap.get("discount") != null && StringUtils.isNotBlank(cMap.get("discount").toString())) {
				BigDecimal discount = new BigDecimal(cMap.get("discount").toString()).multiply(new BigDecimal(10));
				couponDTO.setDiscount(discount);
			}
			if (cMap.get("amount") != null && StringUtils.isNotBlank(cMap.get("amount").toString())) {
				couponDTO.setAmount(new BigDecimal(cMap.get("amount").toString()));
			}
			couponDTO.setStatus(10);
			returnData.put("couponInfo", couponDTO);
		}
		TCouponActivitycoupons activitycoupons = couponActivitycouponsDAO.getTCouponActivitycoupons(couponId,
				cMap.get("deliveryId").toString());
		// 领取完之后在记录表中添加一条领取记录
		activitycoupons.setFsendCount(activitycoupons.getFsendCount() + 1);
		couponActivitycouponsDAO.save(activitycoupons);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("领取优惠券成功！");
		return responseDTO;
	}

	boolean isReceiveCoupon(String couponId, Integer reciveLimit, String customerId) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select t.id as Id from TCouponDelivery t where 1=1 and t.TCustomer.id = :customerId  and t.TCouponInformation.id = :couponId ");

		hqlMap.put("customerId", customerId);
		hqlMap.put("couponId", couponId);
		SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		Date date = new Date();
		if (reciveLimit.intValue() == 20) {
			try {
				hql.append(" and t.fdeliverTime >= :deliverstart ");
				hqlMap.put("deliverstart",
						DateUtils.parseDate(sdfstart.format(date).toString(), "yyyy-MM-dd HH:mm:ss"));
				hql.append(" and t.fdeliverTime <= :deliverend ");
				hqlMap.put("deliverend", DateUtils.parseDate(sdfend.format(date).toString(), "yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponId", couponId);
				map.put("customerId", customerId);
				OutPutLogUtil.printLoggger(e, map, logger);
			}

		}
		// 查询可用优惠券表
		List<Map<String, Object>> couponlist = commonService.find(hql.toString(), hqlMap);
		// 查询历史优惠券表
		hql.delete(0, hql.length());
		hql.append(
				"select t.id as Id from TCouponDeliveryHistory t where 1=1 and t.TCustomer.id = :customerId  and t.TCouponInformation.id = :couponId ");
		if (reciveLimit.intValue() == 20) {
			hql.append(" and t.fdeliverTime >= :deliverstart ");
			hql.append(" and t.fdeliverTime <= :deliverend ");
		}
		// 查询可用优惠券表
		List<Map<String, Object>> couponHistorylist = commonService.find(hql.toString(), hqlMap);
		couponlist.addAll(couponHistorylist);
		if (couponlist == null || couponlist.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取用户的优惠券
	 * 
	 * @param ticket
	 *            票
	 * @param status
	 *            优惠券的状态
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getUserCouponByStatus(Integer clientType, String ticket, Integer status, Integer pageSize,
			Integer offset) {
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
			responseDTO.setMsg("优惠券状态不能为空！");
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
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		// f.fID,f.fTitle,f.fUseStartTime,f.fUseEndTime,f.fAmount,f.fLimitation,f.fDiscount,f.fUseType
		if (status.intValue() == 10) {
			hql.append(
					"select t.id as couponDeliveryId,c.ftitle as ftitle,t.fuseStartTime as fuseStartTime ,t.fuseEndTime as fuseEndTime,c.fuserPoint as userpoint,")
					.append(" c.famount as famount,c.flimitation as flimitation,c.fdiscount as fdiscount,c.fuseRange AS fuseRange,d.fdeliverType as fdeliverType")
					.append(" from TCouponInformation c inner join c.TCouponObjects o inner join c.TCouponDeliveries t inner join t.TDelivery d")
					.append(" where d.fstatus in (90,40,100,120) and (t.TCustomer.id = :customerId or t.TCustomer.id is null) and t.fdeliverTime > :time and t.fuseEndTime >:now and t.fuseStartTime <:now")
					.append(" and c.id not in(select h.TCouponInformation.id from TCouponDeliveryHistory h inner join h.TDelivery hd where hd.fdeliverType = 10 and h.TCustomer.id = :customerId)");
		} else {
			hql.append(
					"select t.id as couponDeliveryId,c.ftitle as ftitle,t.fuseStartTime as fuseStartTime ,t.fuseEndTime as fuseEndTime,c.fuserPoint as userpoint,")
					.append(" c.famount as famount,c.flimitation as flimitation,c.fdiscount as fdiscount,c.fuseRange AS fuseRange,t.fstatus as status,d.fdeliverType as fdeliverType")
					.append(" from TCouponInformation c inner join c.TCouponObjects o inner join c.TCouponDeliveryHistorys t inner join t.TDelivery d")
					.append(" where d.fstatus in (90,40,100,120) and (t.TCustomer.id = :customerId or t.TCustomer.id is null) and t.fdeliverTime > :time");
		}
		hqlMap.put("customerId", customer.getId());
		hqlMap.put("time", customer.getFcreateTime());
		if (status.intValue() == 10) {
			hqlMap.put("now", new Date());
		}
		hqlMap.put("customerId", customerDTO.getCustomerId());
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		CouponDTO couponDTO = null;
		Date date = null;
		List<CouponDTO> dataList = Lists.newArrayList();
		boolean isAdd = true;
		for (Map<String, Object> amap : list) {
			/*
			 * if (((Integer) amap.get("fdeliverType")).intValue() == 10) {
			 * TCouponDeliveryHistory couponDeliveryHistory =
			 * couponDeliveryHistoryDAO
			 * .getCouponDelivery(amap.get("couponDeliveryId").toString(),
			 * customerDTO.getCustomerId()); if (couponDeliveryHistory != null)
			 * { isAdd = false; } }
			 */
			if (isAdd) {
				couponDTO = new CouponDTO();
				if (amap.get("couponDeliveryId") != null
						&& StringUtils.isNotBlank(amap.get("couponDeliveryId").toString())) {
					couponDTO.setCouponDeliveryId(amap.get("couponDeliveryId").toString());
				}

				if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
					couponDTO.setTitle(amap.get("ftitle").toString());
				}
				if (amap.get("fuseStartTime") != null && StringUtils.isNotBlank(amap.get("fuseStartTime").toString())) {
					date = (Date) amap.get("fuseStartTime");
					couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
				}
				if (amap.get("fuseEndTime") != null && StringUtils.isNotBlank(amap.get("fuseEndTime").toString())) {
					date = (Date) amap.get("fuseEndTime");
					couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
				}
				if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
					couponDTO.setLimitation((BigDecimal) amap.get("flimitation"));
					couponDTO.setLimitationInfo(
							new StringBuffer().append("满").append(couponDTO.getLimitation()).append("元可使用").toString());
				} else {
					couponDTO.setLimitationInfo(
							new StringBuffer().append("直减").append(amap.get("famount")).append("元").toString());
				}
				if (amap.get("fdiscount") != null && StringUtils.isNotBlank(amap.get("fdiscount").toString())) {
					BigDecimal discount = ((BigDecimal) amap.get("fdiscount")).multiply(new BigDecimal(10));
					couponDTO.setDiscount(discount);
				}
				if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
					couponDTO.setAmount((BigDecimal) amap.get("famount"));
				}
				if (amap.get("status") != null && StringUtils.isNotBlank(amap.get("status").toString())) {
					couponDTO.setStatus((Integer) amap.get("status"));
				} else {
					couponDTO.setStatus(10);
				}
				if (amap.get("fuseRange") != null && StringUtils.isNotBlank(amap.get("fuseRange").toString())) {
					couponDTO.setUseRange(amap.get("fuseRange").toString());
					couponDTO.setLimitationClient(amap.get("fuseRange").toString());
				}
				dataList.add(couponDTO);
			}

		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userCouponList", dataList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 获得用户的可用优惠券总数
	 * 
	 * 
	 * @return
	 */
	public Integer couponNumByStatus(TCustomer customer) {

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select d.id,t.fdeliverType as fdeliverType,d.TCouponInformation.id as couponId from TCouponDelivery d inner join d.TDelivery t where t.fstatus in (90,40,100,120) and (d.TCustomer.id = :customerId")
				.append(" or d.TCustomer.id is null ) and d.fdeliverTime > :time and d.fuseEndTime >:now and d.fuseStartTime <:now");
		hqlMap.put("customerId", customer.getId());
		hqlMap.put("time", customer.getFcreateTime());
		hqlMap.put("now", new Date());
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		int size = 0;
		for (Map<String, Object> amap : list) {
			size++;
			if (((Integer) amap.get("fdeliverType")).intValue() == 10) {
				TCouponDeliveryHistory couponDeliveryHistory = couponDeliveryHistoryDAO
						.getCouponDelivery(amap.get("couponId").toString(), customer.getId());
				if (couponDeliveryHistory != null) {
					size--;
				}
			}
		}

		return size;
	}

	/**
	 * 获取用户可用的优惠券Service
	 * 
	 * @param ticket
	 *            票
	 * @param orderId
	 *            订单ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getCouponListByOrder(Integer clientType, String ticket, String orderId) {

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

		if (tOrder.getFlockFlag().intValue() == 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("您的订单已锁定状态，暂无法进行操作！");
			return responseDTO;
		}
		if (tOrder.getFstatus().intValue() != 10) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(104);
			responseDTO.setMsg("您的订单不是未支付状态！");
			return responseDTO;
		}

		if (!customerDTO.getCustomerId().equals(tOrder.getTCustomer().getId())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("您输入的orderId参数有误，orderId=“" + orderId + "”的订单不属于该客户！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();

		hql.append(
				"select t.id as couponDeliveryId,c.ftitle as ftitle,t.fuseStartTime as fuseStartTime ,t.fuseEndTime as fuseEndTime,c.fuserPoint as userpoint,c.fuseRange as fuseRange,")
				.append(" c.famount as famount,c.flimitation as flimitation,c.fdiscount as fdiscount,o.fobjectId as objectId,o.fuseType as useType,c.fcity as cityid")
				.append(" from TCouponInformation c inner join c.TCouponObjects o inner join c.TCouponDeliveries t inner join t.TDelivery d")
				.append(" where d.fstatus in (90,40,100,120) and (t.TCustomer.id = :customerId or t.TCustomer.id is null) and t.fdeliverTime > :time and t.fuseEndTime >:now and c.flimitation <= :total and t.fuseStartTime <:now");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		hqlMap.put("total", tOrder.getFreceivableTotal());
		hqlMap.put("customerId", customer.getId());
		hqlMap.put("time", customer.getFcreateTime());
		hqlMap.put("now", new Date());

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		List<CouponDTO> dataList = Lists.newArrayList();
		CouponDTO couponDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			boolean isAdd = true;
			// 校验优惠券适用范围
			if (amap.get("useType") != null && StringUtils.isNotBlank(amap.get("useType").toString())) {
				int useType = (Integer) amap.get("useType");
				couponDTO = new CouponDTO();
				String objectId = "";
				if (amap.get("objectId") != null && StringUtils.isNotBlank(amap.get("objectId").toString())) {
					objectId = amap.get("objectId").toString();
				} else if (useType != 10) {
					break;
				}
				if (useType == 10) {
				} else if (useType == 20) {
					if (!tOrder.getTSponsor().getId().equals(objectId)) {
						isAdd = false;
					}
				} else if (useType == 30) {
					TEvent event = tOrder.getTEvent();
					if (!event.getFtypeA().toString().equals(objectId)) {
						isAdd = false;
					}
				} else {
					if (!tOrder.getTEvent().getId().equals(objectId)) {
						isAdd = false;
					}
				}
			}
			// 校验优惠券适用城市
			if (amap.get("cityid") != null && StringUtils.isNotBlank(amap.get("cityid").toString())) {
				Integer cityId = (Integer) amap.get("cityid");
				if (cityId == 0) {
				} else {
					if (tOrder.getFcityId().intValue() != cityId.intValue()) {
						isAdd = false;
					}
				}
			}
			// 校验优惠券适用终端
			if (amap.get("userpoint") != null && StringUtils.isNotBlank(amap.get("userpoint").toString())) {
				int userpoint = (Integer) amap.get("userpoint");
				if (userpoint == 10) {
				} else {
					if (clientType == 1 && userpoint == 20) {
						isAdd = false;
					} else if ((clientType == 2 || clientType == 3) && userpoint == 30) {
						isAdd = false;
					}
				}
			}
			// 校验过是否使用过优惠券
			TCouponDeliveryHistory couponDeliveryHistory = couponDeliveryHistoryDAO
					.getCouponDelivery(amap.get("couponDeliveryId").toString(), customerDTO.getCustomerId());
			if (couponDeliveryHistory != null) {
				isAdd = false;
			}
			if (isAdd) {
				if (amap.get("couponDeliveryId") != null
						&& StringUtils.isNotBlank(amap.get("couponDeliveryId").toString())) {
					couponDTO.setCouponDeliveryId(amap.get("couponDeliveryId").toString());
				}

				if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
					couponDTO.setTitle(amap.get("ftitle").toString());
				}
				if (amap.get("fuseStartTime") != null && StringUtils.isNotBlank(amap.get("fuseStartTime").toString())) {
					date = (Date) amap.get("fuseStartTime");
					couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
				}
				if (amap.get("fuseEndTime") != null && StringUtils.isNotBlank(amap.get("fuseEndTime").toString())) {
					date = (Date) amap.get("fuseEndTime");
					couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
				}
				if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
					couponDTO.setLimitation((BigDecimal) amap.get("flimitation"));
					couponDTO.setLimitationInfo("满" + amap.get("flimitation") + "元可使用");
				} else {
					couponDTO.setLimitationInfo("直减" + amap.get("famount") + "元");
				}
				if (amap.get("fdiscount") != null && StringUtils.isNotBlank(amap.get("fdiscount").toString())) {
					BigDecimal discount = ((BigDecimal) amap.get("fdiscount")).multiply(new BigDecimal(10));
					couponDTO.setDiscount(discount);
				}
				if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
					couponDTO.setAmount((BigDecimal) amap.get("famount"));
				}
				couponDTO.setStatus(10);
				if (amap.get("fuseRange") != null && StringUtils.isNotBlank(amap.get("fuseRange").toString())) {
					couponDTO.setLimitationClient(amap.get("fuseRange").toString());
				}
				/*
				 * if (amap.get("fuseType") != null &&
				 * StringUtils.isNotBlank(amap.get("fuseType").toString())) {
				 * 
				 * }
				 */
				dataList.add(couponDTO);
			}
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("userCouponList", dataList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 获取用户单笔订单可用的最优优惠券Service
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> getCouponByOrder(Integer clientType, TEvent event, CustomerDTO customerDTO,
			BigDecimal total) {

		Map<String, Object> rMap = new HashMap<String, Object>();
		String couponDeliveryId = null;
		BigDecimal maxAmount = BigDecimal.ZERO;
		// 不可使用优惠券活动
		if (event.getFusePreferential() != null && event.getFusePreferential().intValue() == 0) {
			rMap.put("couponDeliveryId", couponDeliveryId);
			rMap.put("maxAmount", maxAmount);
			return rMap;
		}
		StringBuilder hql = new StringBuilder();

		hql.append(
				"select t.id as couponDeliveryId,c.id as couponId,c.ftitle as ftitle,t.fuseStartTime as fuseStartTime ,t.fuseEndTime as fuseEndTime,c.fuserPoint as userpoint,d.fdeliverType as fdeliverType,")
				.append(" c.famount as famount,c.flimitation as flimitation,c.fdiscount as fdiscount,o.fobjectId as objectId,o.fuseType as useType,c.fcity as cityid,t.fdeliverTime as deliverTime")
				.append(" from TCouponInformation c inner join c.TCouponObjects o inner join c.TCouponDeliveries t inner join t.TDelivery d")
				.append(" where d.fstatus in (90,40,100,120) and (t.TCustomer.id = :customerId or t.TCustomer.id is null) and t.fdeliverTime > :time and t.fuseEndTime >:now and c.flimitation <= :total");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		hqlMap.put("total", total);
		hqlMap.put("customerId", customer.getId());
		hqlMap.put("time", customer.getFcreateTime());
		hqlMap.put("now", new Date());

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);

		Date date = null;

		for (Map<String, Object> amap : list) {
			boolean isAdd = true;
			// 校验优惠券适用范围
			if (amap.get("useType") != null && StringUtils.isNotBlank(amap.get("useType").toString())) {
				int useType = (Integer) amap.get("useType");
				String objectId = "";
				if (amap.get("objectId") != null && StringUtils.isNotBlank(amap.get("objectId").toString())) {
					objectId = amap.get("objectId").toString();
				} else if (useType != 10) {
					break;
				}
				if (useType == 10) {
				} else if (useType == 20) {
					if (!event.getTSponsor().getId().equals(objectId)) {
						isAdd = false;
					}
				} else if (useType == 30) {
					if (!event.getFtypeA().toString().equals(objectId)) {
						isAdd = false;
					}
				} else {
					if (!event.getId().equals(objectId)) {
						isAdd = false;
					}
				}
			}
			// 校验优惠券适用城市
			if (amap.get("cityid") != null && StringUtils.isNotBlank(amap.get("cityid").toString())) {
				Integer cityId = (Integer) amap.get("cityid");
				if (cityId.intValue() == 0) {
				} else {
					if (event.getFcity() != cityId) {
						isAdd = false;
					}
				}
			}
			// 校验优惠券适用终端
			if (amap.get("userpoint") != null && StringUtils.isNotBlank(amap.get("userpoint").toString())) {
				int userpoint = (Integer) amap.get("userpoint");
				if (userpoint == 10) {
				} else {
					if (clientType == 1 && userpoint == 20) {
						isAdd = false;
					} else if ((clientType == 2 || clientType == 3) && userpoint == 30) {
						isAdd = false;
					}
				}
			}
			// 校验过是否使用过优惠券
			if (((Integer) amap.get("fdeliverType")).intValue() == 10) {
				TCouponDeliveryHistory couponDeliveryHistory = couponDeliveryHistoryDAO
						.getCouponDelivery(amap.get("couponId").toString(), customerDTO.getCustomerId());
				if (couponDeliveryHistory != null) {
					isAdd = false;
				}
			}
			if (isAdd) {
				BigDecimal amount = (BigDecimal) amap.get("famount");
				if (amount.compareTo(maxAmount) == 1) {
					couponDeliveryId = amap.get("couponDeliveryId").toString();
					maxAmount = (BigDecimal) amap.get("famount");
					date = (Date) amap.get("deliverTime");
				} else if (amount.compareTo(maxAmount) == 0) {
					int old = DateUtils.truncatedCompareTo((Date) amap.get("deliverTime"), date, Calendar.SECOND);
					if (old == -1) {
						couponDeliveryId = amap.get("couponDeliveryId").toString();
						maxAmount = (BigDecimal) amap.get("famount");
						date = (Date) amap.get("deliverTime");
					}
				}
			}
		}
		rMap.put("couponDeliveryId", couponDeliveryId);
		rMap.put("maxAmount", maxAmount);
		return rMap;
	}

	/**
	 * 下单使用优惠券
	 * 
	 * 
	 * @return
	 */
	public void usecoupon(TCustomer customer, TCouponDelivery tCouponDelivery, TOrder tOrder) {
		TCouponDeliveryHistory couponDeliveryHistory = new TCouponDeliveryHistory();
		couponDeliveryHistory.setFdeliverTime(tCouponDelivery.getFdeliverTime());
		couponDeliveryHistory.setFstatus(20);
		couponDeliveryHistory.setFuseEndTime(tCouponDelivery.getFuseEndTime());
		couponDeliveryHistory.setFuseStartTime(tCouponDelivery.getFuseStartTime());
		couponDeliveryHistory.setFuseTime(new Date());
		couponDeliveryHistory.setTCouponInformation(tCouponDelivery.getTCouponInformation());
		couponDeliveryHistory.setTCustomer(customer);
		couponDeliveryHistory.setTDelivery(tCouponDelivery.getTDelivery());
		couponDeliveryHistory.setTOrder(tOrder);
		if (tCouponDelivery.getFfromOrderId() != null) {
			couponDeliveryHistory.setFfromOrderId(tCouponDelivery.getFfromOrderId());
		}
		couponDeliveryHistoryDAO.save(couponDeliveryHistory);
		if (tCouponDelivery.getTCustomer() != null) {
			couponDeliveryDAO.delete(tCouponDelivery);
		}
		// 使用后记录表中使用优惠券数量加 1
		TCouponActivitycoupons activitycoupons = couponActivitycouponsDAO
				.getTActivitycoupons(tCouponDelivery.getTCouponInformation().getId());
		activitycoupons.setFuseCount(activitycoupons.getFuseCount() + 1);
		couponActivitycouponsDAO.save(activitycoupons);
		// 删除订单超时未支付取消订单任务
		timingTaskDAO.clearTimeTaskByEntityIdAndTaskType(tCouponDelivery.getId(), 7);
	}

	public boolean chackCouponIsOK(TCustomer customer, String couponDeliveryId, TCouponDelivery tCouponDelivery) {
		Map<String, Object> rMap = new HashMap<String, Object>();
		// TODO 条件规则不全，需要补充
		if (tCouponDelivery == null) {
			return false;
			// TODO
		}
		if (tCouponDelivery.getTCustomer() != null
				&& !tCouponDelivery.getTCustomer().getId().equals(customer.getId())) {
			return false;
		}
		if (tCouponDelivery.getTCustomer() == null) {
			TCouponDeliveryHistory couponDeliveryHistory = couponDeliveryHistoryDAO
					.getCouponDelivery(tCouponDelivery.getTCouponInformation().getId(), customer.getId());
			if (couponDeliveryHistory != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取用户可用的优惠券Service
	 * 
	 * @param ticket
	 *            票
	 * @param orderId
	 *            订单ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getCouponListByEvent(Integer clientType, String ticket, String eventId, BigDecimal total) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (total == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("total参数不能为空，请检查total的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TEvent tEvent = eventDAO.findOne(eventId);
		if (tEvent == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的eventId参数有误，eventId=“" + eventId + "”的订单不存在！");
			return responseDTO;
		}

		if (tEvent.getFstatus().intValue() != 20) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("该活动已下架！");
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		// 不可使用优惠券活动
		Map<String, Object> returnData = Maps.newHashMap();
		List<CouponDTO> dataList = Lists.newArrayList();
		if (tEvent.getFusePreferential() != null && tEvent.getFusePreferential().intValue() == 0) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			returnData.put("userCouponList", dataList);
			responseDTO.setData(returnData);
			return responseDTO;
		}
		hql.append(
				"select t.id as couponDeliveryId,c.id as couponId,c.ftitle as ftitle,t.fuseStartTime as fuseStartTime ,t.fuseEndTime as fuseEndTime,c.fuserPoint as userpoint,c.fuseRange as fuseRange,")
				.append(" c.famount as famount,c.flimitation as flimitation,c.fdiscount as fdiscount,o.fobjectId as objectId,o.fuseType as useType,c.fcity as cityid,d.fdeliverType as fdeliverType")
				.append(" from TCouponInformation c inner join c.TCouponObjects o inner join c.TCouponDeliveries t inner join t.TDelivery d")
				.append(" where d.fstatus in (90,40,100,120) and (t.TCustomer.id = :customerId or t.TCustomer.id is null) and t.fdeliverTime > :time and t.fuseEndTime >:now and c.flimitation <= :total and t.fuseStartTime <:now");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		TCustomer customer = customerDAO.findOne(customerDTO.getCustomerId());
		hqlMap.put("total", total);
		hqlMap.put("customerId", customer.getId());
		hqlMap.put("time", customer.getFcreateTime());
		hqlMap.put("now", new Date());

		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		CouponDTO couponDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			boolean isAdd = true;
			// 校验优惠券适用范围
			if (amap.get("useType") != null && StringUtils.isNotBlank(amap.get("useType").toString())) {
				int useType = (Integer) amap.get("useType");
				couponDTO = new CouponDTO();
				String objectId = "";
				if (amap.get("objectId") != null && StringUtils.isNotBlank(amap.get("objectId").toString())) {
					objectId = amap.get("objectId").toString();
				} else if (useType != 10) {
					break;
				}
				if (useType == 10) {
				} else if (useType == 20) {
					if (!tEvent.getTSponsor().getId().equals(objectId)) {
						isAdd = false;
					}
				} else if (useType == 30) {
					if (!tEvent.getFtypeA().toString().equals(objectId)) {
						isAdd = false;
					}
				} else {
					if (!tEvent.getId().equals(objectId)) {
						isAdd = false;
					}
				}
			}
			// 校验优惠券适用城市
			if (amap.get("cityid") != null && StringUtils.isNotBlank(amap.get("cityid").toString())) {
				int cityId = (Integer) amap.get("cityid");
				if (cityId == 0) {
				} else {
					if (tEvent.getFcity() != cityId) {
						isAdd = false;
					}
				}
			}
			// 校验优惠券适用终端
			if (amap.get("userpoint") != null && StringUtils.isNotBlank(amap.get("userpoint").toString())) {
				int userpoint = (Integer) amap.get("userpoint");
				if (userpoint == 10) {
				} else {
					if (clientType == 1 && userpoint == 20) {
						isAdd = false;
					} else if (((clientType == 2 || clientType == 3) || clientType == 3) && userpoint == 30) {
						isAdd = false;
					}
				}
			}
			// 校验过是否使用过优惠券
			if (((Integer) amap.get("fdeliverType")).intValue() == 10) {
				TCouponDeliveryHistory couponDeliveryHistory = couponDeliveryHistoryDAO
						.getCouponDelivery(amap.get("couponId").toString(), customerDTO.getCustomerId());
				if (couponDeliveryHistory != null) {
					isAdd = false;
				}
			}
			if (isAdd) {
				if (amap.get("couponDeliveryId") != null
						&& StringUtils.isNotBlank(amap.get("couponDeliveryId").toString())) {
					couponDTO.setCouponDeliveryId(amap.get("couponDeliveryId").toString());
				}

				if (amap.get("ftitle") != null && StringUtils.isNotBlank(amap.get("ftitle").toString())) {
					couponDTO.setTitle(amap.get("ftitle").toString());
				}
				if (amap.get("fuseStartTime") != null && StringUtils.isNotBlank(amap.get("fuseStartTime").toString())) {
					date = (Date) amap.get("fuseStartTime");
					couponDTO.setUseStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
				}
				if (amap.get("fuseEndTime") != null && StringUtils.isNotBlank(amap.get("fuseEndTime").toString())) {
					date = (Date) amap.get("fuseEndTime");
					couponDTO.setUseEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
				}
				if (amap.get("flimitation") != null && StringUtils.isNotBlank(amap.get("flimitation").toString())) {
					couponDTO.setLimitation((BigDecimal) amap.get("flimitation"));
					couponDTO.setLimitationInfo("满" + amap.get("flimitation") + "元可使用");
				} else {
					couponDTO.setLimitationInfo("直减" + amap.get("famount") + "元");
				}
				if (amap.get("fdiscount") != null && StringUtils.isNotBlank(amap.get("fdiscount").toString())) {
					BigDecimal discount = ((BigDecimal) amap.get("fdiscount")).multiply(new BigDecimal(10));
					couponDTO.setDiscount(discount);
				}
				if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
					couponDTO.setAmount((BigDecimal) amap.get("famount"));
				}
				couponDTO.setStatus(10);

				if (amap.get("fuseRange") != null && StringUtils.isNotBlank(amap.get("fuseRange").toString())) {
					couponDTO.setLimitationClient(amap.get("fuseRange").toString());
				}
				/*
				 * if (((Integer) amap.get("userpoint")).intValue() == 20) {
				 * couponDTO.setLimitationClient("仅限移动端下单使用"); } else if
				 * (((Integer) amap.get("userpoint")).intValue() == 30) {
				 * couponDTO.setLimitationClient("仅限微信端下单使用"); } else if
				 * (((Integer) amap.get("userpoint")).intValue() == 10) {
				 * couponDTO.setLimitationClient("所有终端下单使用"); }
				 */

				/*
				 * if (amap.get("fuseType") != null &&
				 * StringUtils.isNotBlank(amap.get("fuseType").toString())) {
				 * 
				 * }
				 */
				dataList.add(couponDTO);
			}
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		returnData.put("userCouponList", dataList);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO receiveCouponByPhone(String deliveryId, String ticket, Integer type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(deliveryId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("deliveryId参数不能为空，请检查deliveryId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, 1);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select d.id as deliveryId,a.fdeliveryCount as count,a.fsendCount as sendCount,d.freciveLimit as reciveLimit,c.ftitle as title,c.fuseRange as useRange,c.famount as amount,c.flimitation as limitation,c.fvalidDays as validDays,")
				.append(" d.fdeliveryStartTime as deliveryStartTime,d.fdeliveryEndTime as deliveryEndTime,c.id as couponId,c.fuseStartTime as couponStartTime,c.fuseEndTime as couponEndTime,c.fuserPoint as userpoint,c.fuseRange as useRange,")
				.append(" d.fdeliverType as deliveryType from TDelivery d inner join TCouponActivitycoupons a on a.fdeliveryId = d.id inner join TCouponInformation c on c.id = a.fcouponId")
				.append(" where d.factivityType = 20 and d.fstatus = 40 and d.freciveChannel = 20");

		hql.append(" and d.id = :deliveryId");
		hqlMap.put("deliveryId", deliveryId);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (list == null || list.size() == 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setMsg("该领券活动已过期，请期待下次领券活动！");
			return responseDTO;
		}
		Map<String, Object> dMap = list.get(0);
		// 校验是否是新用户
		if (((Integer) dMap.get("deliveryType")).intValue() == 130) {
			// 校验用户信息是否属于新用户
			TCustomerInfo t = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
			if (t == null || (t.getForderTotal() != null && t.getForderTotal().compareTo(BigDecimal.ZERO) > 0)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(139);
				responseDTO.setMsg("该活动仅限新客户参加！");
				return responseDTO;
			}
		}

		Date now = new Date();

		boolean isReceivedelivery = isReceivedelivery(deliveryId, (Integer) dMap.get("reciveLimit"),
				customerDTO.getCustomerId());

		List<TCouponDelivery> coupondeliverylist = new ArrayList<TCouponDelivery>();
		List<CouponInofDTO> CouponInfoDTOlist = new ArrayList<CouponInofDTO>();
		List<TCouponActivitycoupons> activitycouponList = new ArrayList<TCouponActivitycoupons>();

		for (Map<String, Object> cMap : list) {
			// 校验优惠券是否已领完
			if ((Integer) cMap.get("count") > 0 && (Integer) cMap.get("count") <= (Integer) cMap.get("sendCount")) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(109);
				responseDTO.setMsg("该优惠券领取完了！");
				return responseDTO;
			}
			// 计算券的有效期
			Date useStartTime = null;
			Date useEndTime = null;
			if (cMap.get("validDays") != null && StringUtils.isNotBlank(cMap.get("validDays").toString())) {
				SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
				try {
					useStartTime = DateUtils.parseDate(sdfstart.format(now).toString(), "yyyy-MM-dd HH:mm:ss");
					Calendar c = Calendar.getInstance();
					c.setTime(useStartTime);
					c.set(Calendar.DATE, c.get(Calendar.DATE) + (Integer) cMap.get("validDays") - 1);
					useEndTime = DateUtils.parseDate(sdfend.format(c.getTime()).toString(), "yyyy-MM-dd HH:mm:ss");
				} catch (ParseException e) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("deliveryId", deliveryId);
					map.put("customerId", customerDTO.getCustomerId());
					OutPutLogUtil.printLoggger(e, map, logger);
				}
			} else {
				useStartTime = (Date) cMap.get("couponStartTime");
				useEndTime = (Date) cMap.get("couponEndTime");
			}
			// 将本次活动领取的券保存到数据表里
			TCouponDelivery couponDelivery = new TCouponDelivery();
			couponDelivery.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
			couponDelivery.setFdeliverTime(new Date());
			couponDelivery.setTCouponInformation(new TCouponInformation(cMap.get("couponId").toString()));
			couponDelivery.setTDelivery(new TDelivery(cMap.get("deliveryId").toString()));
			couponDelivery.setFuseEndTime(useEndTime);
			couponDelivery.setFuseStartTime(useStartTime);
			coupondeliverylist.add(couponDelivery);
			// 返回给接口的数据
			if (type.intValue() == 1) {
				CouponInofDTO couponInofDTO = new CouponInofDTO();
				couponInofDTO.setCouponDeliveryId(cMap.get("couponId").toString());
				couponInofDTO.setCouponId(cMap.get("couponId").toString());
				if (StringUtils.isNotBlank(cMap.get("title").toString())) {
					couponInofDTO.setTitle(cMap.get("title").toString());
				}
				couponInofDTO.setUseStartTime(DateFormatUtils.format(useStartTime, "yyyy-MM-dd"));
				couponInofDTO.setUseEndTime(DateFormatUtils.format(useEndTime, "yyyy-MM-dd"));
				if (cMap.get("limitation") != null && StringUtils.isNotBlank(cMap.get("limitation").toString())) {
					couponInofDTO.setLimitation(new BigDecimal(cMap.get("limitation").toString()));
					couponInofDTO.setLimitationInfo(
							new StringBuffer().append("满").append(cMap.get("limitation")).append("元可使用").toString());
				} else {
					couponInofDTO.setLimitationInfo(
							new StringBuffer().append("直减").append(cMap.get("amount")).append("元").toString());
				}
				if (cMap.get("discount") != null && StringUtils.isNotBlank(cMap.get("discount").toString())) {
					BigDecimal discount = new BigDecimal(cMap.get("discount").toString()).multiply(new BigDecimal(10));
					couponInofDTO.setDiscount(discount);
				}
				if (cMap.get("amount") != null && StringUtils.isNotBlank(cMap.get("amount").toString())) {
					couponInofDTO.setAmount(new BigDecimal(cMap.get("amount").toString()));
				}
				if (cMap.get("useRange") != null && StringUtils.isNotBlank(cMap.get("useRange").toString())) {
					couponInofDTO.setUseRange(cMap.get("useRange").toString());
				}
				if (cMap.get("useRange") != null && StringUtils.isNotBlank(cMap.get("useRange").toString())) {
					couponInofDTO.setLimitationClient(cMap.get("useRange").toString());
				}
				/*
				 * if ((Integer) cMap.get("userpoint") == 20) {
				 * couponInofDTO.setLimitationClient("仅限移动端下单使用"); } else if
				 * ((Integer) cMap.get("userpoint") == 30) {
				 * couponInofDTO.setLimitationClient("仅限微信端下单使用"); } else if
				 * ((Integer) cMap.get("userpoint") == 10) {
				 * couponInofDTO.setLimitationClient("所有终端下单使用"); }
				 */
				couponInofDTO.setStatus(10);
				CouponInfoDTOlist.add(couponInofDTO);
			}
			// 领取完之后在记录表中添加一条领取记录
			if (!isReceivedelivery) {
				TCouponActivitycoupons activitycoupons = couponActivitycouponsDAO
						.getTCouponActivitycoupons(cMap.get("couponId").toString(), cMap.get("deliveryId").toString());
				activitycoupons.setFsendCount(activitycoupons.getFsendCount() + 1);
				activitycouponList.add(activitycoupons);
			}
		}

		Map<String, Object> returnData = Maps.newHashMap();
		if (type.intValue() == 1) {
			returnData.put("couponInfoList", CouponInfoDTOlist);
		}
		// 校验是否领取过
		if (isReceivedelivery) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setData(returnData);
			responseDTO.setMsg("您已经领取过该优惠券了，赶快去零到壹找好玩的活动吧。");
			return responseDTO;
		}
		// 校验活动有效期
		Date deliveryStartTime = (Date) dMap.get("deliveryStartTime");
		Date deliveryEndTime = (Date) dMap.get("deliveryEndTime");
		if (deliveryStartTime.compareTo(now) > 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setData(returnData);
			responseDTO.setMsg("该领券活动尚未开始，敬请期待！");
			return responseDTO;
		}
		if (deliveryEndTime.compareTo(now) < 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setData(returnData);
			responseDTO.setMsg("该领券活动已过期，请期待下次领券活动！");
			return responseDTO;
		}
		couponActivitycouponsDAO.save(activitycouponList);
		coupondeliverylist = couponDeliveryDAO.save(coupondeliverylist);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("领取优惠券成功！");
		return responseDTO;
	}

	boolean isReceivedelivery(String deliveryId, Integer reciveLimit, String customerId) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select t.id as Id from TCouponDelivery t where 1=1 and t.TCustomer.id = :customerId  and t.TDelivery.id = :deliveryId ");

		hqlMap.put("customerId", customerId);
		hqlMap.put("deliveryId", deliveryId);
		SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		Date date = new Date();
		if (reciveLimit.intValue() == 20) {
			try {
				hql.append(" and t.fdeliverTime >= :deliverstart ");
				hqlMap.put("deliverstart",
						DateUtils.parseDate(sdfstart.format(date).toString(), "yyyy-MM-dd HH:mm:ss"));
				hql.append(" and t.fdeliverTime <= :deliverend ");
				hqlMap.put("deliverend", DateUtils.parseDate(sdfend.format(date).toString(), "yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("deliveryId", deliveryId);
				map.put("customerId", customerId);
				OutPutLogUtil.printLoggger(e, map, logger);
			}

		}
		// 查询可用优惠券表
		List<Map<String, Object>> couponlist = commonService.find(hql.toString(), hqlMap);
		// 查询历史优惠券表
		hql.delete(0, hql.length());
		hql.append(
				"select t.id as Id from TCouponDeliveryHistory t where 1=1 and t.TCustomer.id = :customerId  and t.TDelivery.id = :deliveryId ");
		if (reciveLimit.intValue() == 20) {
			hql.append(" and t.fdeliverTime >= :deliverstart ");
			hql.append(" and t.fdeliverTime <= :deliverend ");
		}
		// 查询可用优惠券表
		List<Map<String, Object>> couponHistorylist = commonService.find(hql.toString(), hqlMap);
		couponlist.addAll(couponHistorylist);
		if (couponlist == null || couponlist.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public ResponseDTO receiveCouponByOrder(String ticket, String orderId, String deliveryId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(deliveryId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("deliveryId参数不能为空，请检查deliveryId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, 1);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select d.id as deliveryId,a.fdeliveryCount as count,a.fsendCount as sendCount,d.freciveLimit as reciveLimit,c.ftitle as title,c.fuseRange as useRange,c.famount as amount,c.flimitation as limitation,c.fvalidDays as validDays,")
				.append(" d.fdeliveryStartTime as deliveryStartTime,d.fdeliveryEndTime as deliveryEndTime,c.id as couponId,c.fuseStartTime as couponStartTime,c.fuseEndTime as couponEndTime,c.fuserPoint as userpoint,c.fuseRange as useRange")
				.append(" from TDelivery d inner join TCouponActivitycoupons a on a.fdeliveryId = d.id inner join TCouponInformation c on c.id = a.fcouponId")
				.append(" where d.factivityType = 20 and d.fstatus = 40 and d.freciveChannel = 20");

		hql.append(" and d.id = :deliveryId");
		hqlMap.put("deliveryId", deliveryId);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (list == null || list.size() != 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setMsg("该领券活动已结束，请期待下次领券活动！");
			return responseDTO;
		}
		Map<String, Object> dMap = list.get(0);

		Date now = new Date();
		// 计算券的有效期
		Date useStartTime = null;
		Date useEndTime = null;
		if (dMap.get("validDays") != null && StringUtils.isNotBlank(dMap.get("validDays").toString())) {
			SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			try {
				useStartTime = DateUtils.parseDate(sdfstart.format(now).toString(), "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();
				c.setTime(useStartTime);
				c.set(Calendar.DATE, c.get(Calendar.DATE) + (Integer) dMap.get("validDays") - 1);
				useEndTime = DateUtils.parseDate(sdfend.format(c.getTime()).toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("orderId", orderId);
				map.put("deliveryId", deliveryId);
				map.put("customerId", customerDTO.getCustomerId());
				OutPutLogUtil.printLoggger(e, map, logger);
			}
		} else {
			useStartTime = (Date) dMap.get("couponStartTime");
			useEndTime = (Date) dMap.get("couponEndTime");
		}

		// 将本次活动领取的券保存到数据表里
		TCouponDelivery couponDelivery = new TCouponDelivery();
		couponDelivery.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		couponDelivery.setFdeliverTime(new Date());
		couponDelivery.setTCouponInformation(new TCouponInformation(dMap.get("couponId").toString()));
		couponDelivery.setTDelivery(new TDelivery(dMap.get("deliveryId").toString()));
		couponDelivery.setFuseEndTime(useEndTime);
		couponDelivery.setFuseStartTime(useStartTime);
		couponDelivery.setFfromOrderId(orderId);
		// 返回给接口的数据
		CouponInofDTO couponInofDTO = new CouponInofDTO();
		couponInofDTO.setCouponDeliveryId(dMap.get("couponId").toString());
		couponInofDTO.setCouponId(dMap.get("couponId").toString());
		if (StringUtils.isNotBlank(dMap.get("title").toString())) {
			couponInofDTO.setTitle(dMap.get("title").toString());
		}

		couponInofDTO.setUseStartTime(DateFormatUtils.format(useStartTime, "yyyy-MM-dd"));
		couponInofDTO.setUseEndTime(DateFormatUtils.format(useEndTime, "yyyy-MM-dd"));

		if (dMap.get("limitation") != null && StringUtils.isNotBlank(dMap.get("limitation").toString())) {
			couponInofDTO.setLimitation(new BigDecimal(dMap.get("limitation").toString()));
			couponInofDTO.setLimitationInfo(
					new StringBuffer().append("满").append(dMap.get("limitation")).append("元可使用").toString());
		} else {
			couponInofDTO.setLimitationInfo(
					new StringBuffer().append("直减").append(dMap.get("amount")).append("元").toString());
		}
		if (dMap.get("discount") != null && StringUtils.isNotBlank(dMap.get("discount").toString())) {
			BigDecimal discount = new BigDecimal(dMap.get("discount").toString()).multiply(new BigDecimal(10));
			couponInofDTO.setDiscount(discount);
		}
		if (dMap.get("amount") != null && StringUtils.isNotBlank(dMap.get("amount").toString())) {
			couponInofDTO.setAmount(new BigDecimal(dMap.get("amount").toString()));
		}
		if (dMap.get("useRange") != null && StringUtils.isNotBlank(dMap.get("useRange").toString())) {
			couponInofDTO.setUseRange(dMap.get("useRange").toString());
		}
		if ((Integer) dMap.get("userpoint") == 20) {
			couponInofDTO.setLimitationClient("仅限移动端下单使用");
		} else if ((Integer) dMap.get("userpoint") == 30) {
			couponInofDTO.setLimitationClient("仅限微信端下单使用");
		} else if ((Integer) dMap.get("userpoint") == 10) {
			couponInofDTO.setLimitationClient("所有终端下单使用");
		}
		couponInofDTO.setStatus(10);

		Map<String, Object> returnData = Maps.newHashMap();
		List<CouponInofDTO> CouponInfoDTOlist = new ArrayList<CouponInofDTO>();
		CouponInfoDTOlist.add(couponInofDTO);
		returnData.put("couponInfoList", CouponInfoDTOlist);

		List<Map<String, Object>> cList = isReceiveByOrder(deliveryId, orderId);
		// 校验是否领取过该优惠券
		for (Map<String, Object> lMap : cList) {
			if (lMap.get("customerId").toString().equals(customerDTO.getCustomerId())) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(101);
				responseDTO.setData(returnData);
				responseDTO.setMsg("您已经领取过该优惠券了，赶快去零到壹找好玩的活动吧。");
				return responseDTO;
			}
		}

		// 校验优惠券是否已领完
		if (cList != null && ((Integer) dMap.get("count") == cList.size())) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(109);
			responseDTO.setData(returnData);
			responseDTO.setMsg("该优惠券领取完了！");
			return responseDTO;
		}

		// 校验活动有效期
		Date deliveryStartTime = (Date) dMap.get("deliveryStartTime");
		Date deliveryEndTime = (Date) dMap.get("deliveryEndTime");

		if (deliveryStartTime.compareTo(now) > 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(129);
			responseDTO.setData(returnData);
			responseDTO.setMsg("该领券活动尚未开始，敬请期待！");
			return responseDTO;
		}
		if (deliveryEndTime.compareTo(now) < 0) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setData(returnData);
			responseDTO.setMsg("该领券活动已结束，请期待下次领券活动！");
			return responseDTO;
		}

		// 领取完之后在记录表中添加一条领取记录
		TCouponActivitycoupons activitycoupons = couponActivitycouponsDAO
				.getTCouponActivitycoupons(dMap.get("couponId").toString(), dMap.get("deliveryId").toString());
		activitycoupons.setFsendCount(activitycoupons.getFsendCount() + 1);
		couponActivitycouponsDAO.save(activitycoupons);
		couponDelivery = couponDeliveryDAO.save(couponDelivery);

		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("领取优惠券成功！");
		return responseDTO;
	}

	public List<Map<String, Object>> isReceiveByOrder(String deliveryId, String fromOrderId) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		hql.append(
				"select t.id as Id,t.TCustomer.id as customerId from TCouponDelivery t where t.TDelivery.id = :deliveryId and t.ffromOrderId =:fromOrderId ");
		hqlMap.put("deliveryId", deliveryId);
		hqlMap.put("fromOrderId", fromOrderId);

		// 查询可用优惠券表
		List<Map<String, Object>> couponlist = commonService.find(hql.toString(), hqlMap);
		// 查询历史优惠券表
		hql.delete(0, hql.length());
		hql.append(
				"select t.id as Id,t.TCustomer.id as customerId from TCouponDeliveryHistory t where t.TDelivery.id = :deliveryId and t.ffromOrderId =:fromOrderId ");

		// 查询可用优惠券表
		List<Map<String, Object>> couponHistorylist = commonService.find(hql.toString(), hqlMap);
		couponlist.addAll(couponHistorylist);

		return couponlist;

	}

	public ResponseDTO shareCouponByOrder(String ticket, String orderId, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, 1);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}
		if (StringUtils.isBlank(orderId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("orderId参数不能为空，请检查orderId的传递参数值！");
			return responseDTO;
		}
		TOrder order = orderDAO.findOne(orderId);
		if (order == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("orderId参数有误，这是个无效或者过期的orderId信息！");
			return responseDTO;
		}
		BigDecimal orderprice = order.getFtotal();

		TCouponShareOrder tCouponShareOrder = couponShareOrderDAO.getByOrderPrice(orderprice);
		Map<String, Object> returnData = Maps.newHashMap();
		Map<String, Object> shareData = Maps.newHashMap();
		if (tCouponShareOrder == null) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			returnData.put("ifCanShare", false);
			responseDTO.setData(returnData);
			responseDTO.setMsg("该订单不能分享红包！");
			return responseDTO;
		}
		StringBuilder shareUrl = new StringBuilder();
		/*
		 * String url = PropertiesUtil.getProperty("shareCouponUrl");
		 * shareUrl.append(url).append("/type=2/fromOrderId=").append(orderId).
		 * append("/deliveryId=") .append(tCouponShareOrder.getFdeliveryId());
		 */

		shareUrl.append(request.getScheme()).append("://").append(request.getServerName()).append(":")
				.append(request.getServerPort()).append(request.getContextPath())
				.append("/api/system/share/couponByOrder/2/").append(orderId).append("/")
				.append(tCouponShareOrder.getFdeliveryId());

		shareData.put("frompt", tCouponShareOrder.getFprompt());
		shareData.put("shareUrl", shareUrl);
		shareData.put("title", tCouponShareOrder.getFtitle());
		shareData.put("description", tCouponShareOrder.getFdescription());
		shareData.put("photoUrl", tCouponShareOrder.getFphoto());
		returnData.put("ifCanShare", true);
		returnData.put("shareData", shareData);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获得订单分享红包！");
		return responseDTO;
	}

	// 将订单已使用的优惠券返回给用户
	public void backCoupon(String orderId) {
		TCouponDeliveryHistory couponDeliveryHistory = couponDeliveryHistoryDAO.getCouponbyOrder(orderId);
		if (couponDeliveryHistory != null) {
			TCouponDelivery couponDelivery = couponDeliveryDAO.getCouponbyCustomer(
					couponDeliveryHistory.getTCustomer().getId(),
					couponDeliveryHistory.getTCouponInformation().getId());
			if (couponDeliveryHistory.getFuseEndTime().compareTo(new Date()) == 1) {
				TCouponDelivery tCouponDelivery = new TCouponDelivery();
				tCouponDelivery.setFdeliverTime(couponDeliveryHistory.getFdeliverTime());
				tCouponDelivery.setFuseEndTime(couponDeliveryHistory.getFuseEndTime());
				tCouponDelivery.setFuseStartTime(couponDeliveryHistory.getFuseStartTime());
				tCouponDelivery.setTCouponInformation(couponDeliveryHistory.getTCouponInformation());
				tCouponDelivery.setTCustomer(couponDeliveryHistory.getTCustomer());
				tCouponDelivery.setTDelivery(couponDeliveryHistory.getTDelivery());
				if (couponDeliveryHistory.getFfromOrderId() != null) {
					tCouponDelivery.setFfromOrderId(couponDeliveryHistory.getFfromOrderId());
				}
				if (couponDelivery == null) {
					couponDeliveryDAO.save(tCouponDelivery);
				}
				couponDeliveryHistoryDAO.delete(couponDeliveryHistory);
				// 使用后记录表中使用优惠券数量- 1
				TCouponActivitycoupons activitycoupons = couponActivitycouponsDAO
						.getTActivitycoupons(tCouponDelivery.getTCouponInformation().getId());
				activitycoupons.setFuseCount(activitycoupons.getFuseCount() - 1);
				couponActivitycouponsDAO.save(activitycoupons);
			}
		}
	}

	@Transactional(readOnly = true)
	public ResponseDTO getChannelCouponList(Integer clientType, String ticket, Integer pageSize, Integer offset) {

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
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append("select cc.fcouponName as title,cc.fsubtitle as subTitle, ci.id as couponId,ci.fvalidDays as validDays,o.fuseType as useType,o.fobjectId as objectId ")
			.append(" ,ci.fuseStartTime as couponStartTime,ci.fuseEndTime as couponEndTime,ci.fuserPoint as userpoint,ci.fuseRange as useRange ")
			.append(" ,ci.famount as amount,ci.flimitation as limitation, ca.fdeliveryCount as count,ca.fsendCount as sendCount,d.freciveLimit as reciveLimit,d.fdeliverType as deliveryType ")
			.append(" from TCouponInformation ci inner join ci.TCouponObjects o ")
			.append(" inner join TCouponActivitycoupons ca on ci.id = ca.fcouponId ")
			.append(" inner join TDelivery d on ca.fdeliveryId = d.id")
			.append(" inner join TCouponChannel cc on d.id = cc.fdeliveryId")
			.append(" where cc.fstatus = 10 and d.fstatus = 40 and d.factivityType = 20 and cc.fbeginTime < :now and cc.fendTime > :now ")
			.append(" order by cc.forder");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("now", new Date());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();
		List<CouponChannelDTO> dataList = Lists.newArrayList();
		
		CouponChannelDTO couponChannelDTO = null;
		TCustomerInfo t = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
		Date now = new Date();
		for (Map<String, Object> amap : list) {
			if (((Integer) amap.get("deliveryType")).intValue() == 130) {
				// 校验用户信息是否属于新用户
				if (t == null || (t.getForderTotal() != null && t.getForderTotal().compareTo(BigDecimal.ZERO) > 0)) {
					continue;
				}
			}
			couponChannelDTO = new CouponChannelDTO();
			if (amap.get("couponId") != null && StringUtils.isNotBlank(amap.get("couponId").toString())) {
				couponChannelDTO.setCouponId(amap.get("couponId").toString());
				if (customerDTO!=null) {
					couponChannelDTO.setReceive(isReceiveCoupon(amap.get("couponId").toString(),
							(Integer) amap.get("reciveLimit"), customerDTO.getCustomerId()));
				}
			}
			if (amap.get("title") != null && StringUtils.isNotBlank(amap.get("title").toString())) {
				couponChannelDTO.setTitle(amap.get("title").toString());
			}
			if (amap.get("subTitle") != null && StringUtils.isNotBlank(amap.get("subTitle").toString())) {
				couponChannelDTO.setSubTitle(amap.get("subTitle").toString());
			}
			// 计算券的有效期
			Date useStartTime = null;
			Date useEndTime = null;
			if (amap.get("validDays") != null && StringUtils.isNotBlank(amap.get("validDays").toString())) {
				SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
				try {
					useStartTime = DateUtils.parseDate(sdfstart.format(now).toString(), "yyyy-MM-dd HH:mm:ss");
					Calendar c = Calendar.getInstance();
					c.setTime(useStartTime);
					c.set(Calendar.DATE, c.get(Calendar.DATE) + (Integer) amap.get("validDays") - 1);
					useEndTime = DateUtils.parseDate(sdfend.format(c.getTime()).toString(), "yyyy-MM-dd HH:mm:ss");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				if (amap.get("couponStartTime") != null && StringUtils.isNotBlank(amap.get("couponStartTime").toString())) {
					useStartTime = (Date) amap.get("couponStartTime");
				}
				if (amap.get("couponEndTime") != null && StringUtils.isNotBlank(amap.get("couponEndTime").toString())) {
					useEndTime = (Date) amap.get("couponEndTime");
				}
			}

			if (useStartTime != null) {
				couponChannelDTO.setUseStartTime(DateFormatUtils.format(useStartTime, "yyyy-MM-dd"));
			}
			if (useEndTime != null) {
				couponChannelDTO.setUseEndTime(DateFormatUtils.format(useEndTime, "yyyy-MM-dd"));
			}

			if (amap.get("limitation") != null && StringUtils.isNotBlank(amap.get("limitation").toString())) {
				couponChannelDTO.setLimitation((BigDecimal) amap.get("limitation"));
				couponChannelDTO.setLimitationInfo("满" + amap.get("limitation") + "可用");
			} else {
				couponChannelDTO.setLimitationInfo("直减" + amap.get("amount") + "元");
			}
			if (amap.get("amount") != null && StringUtils.isNotBlank(amap.get("amount").toString())) {
				couponChannelDTO.setAmount((BigDecimal) amap.get("amount"));
			}
			if (amap.get("useRange") != null && StringUtils.isNotBlank(amap.get("useRange").toString())) {
				couponChannelDTO.setUseRange(amap.get("useRange").toString());
				couponChannelDTO.setLimitationClient(amap.get("useRange").toString());
			}
			if (amap.get("sendCount") != null && StringUtils.isNotBlank(amap.get("sendCount").toString())
					&& amap.get("count") != null && StringUtils.isNotBlank(amap.get("count").toString())) {
				if ((Integer) amap.get("count") > 0 && (Integer) amap.get("count") <= (Integer) amap.get("sendCount")) {
					couponChannelDTO.setReceiveFinish(true);
				}
			}
			if (amap.get("useType") != null && StringUtils.isNotBlank(amap.get("useType").toString())) {
				if (Integer.parseInt(amap.get("useType").toString()) == 40) {
					String eventDetail = new StringBuilder().append(Constant.eventDetail)
							.append(amap.get("objectId").toString()).toString();
					couponChannelDTO.setUseUrl(eventDetail);
				}else{
					couponChannelDTO.setUseUrl(Constant.H5Url);
				}
			}
			dataList.add(couponChannelDTO);
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		returnData.put("userCouponList", dataList);
		responseDTO.setData(returnData);
		return responseDTO;
	}
	
	/**
	 * 用户领取优惠券service
	 * 
	 * @param couponId
	 *            优惠券id
	 * @param ticket
	 *            用户票
	 * @param type
	 * @return
	 */
	public ResponseDTO receiveCouponByChannel(Integer clientType, String ticket, String couponId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(couponId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("couponId参数不能为空，请检查couponId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
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

		StringBuilder hql = new StringBuilder();
		Map<String, Object> hqlMap = Maps.newHashMap();
		
		hql.append("select cc.fcouponName as title,cc.fsubtitle as subTitle, ci.id as couponId,ci.fvalidDays as validDays,o.fuseType as useType,o.fobjectId as objectId ")
		.append(" ,ci.fuseStartTime as couponStartTime,ci.fuseEndTime as couponEndTime,ci.fuserPoint as userpoint,ci.fuseRange as useRange,d.id as deliveryId ")
		.append(" ,ci.famount as amount,ci.flimitation as limitation, ca.fdeliveryCount as count,ca.fsendCount as sendCount,d.freciveLimit as reciveLimit,d.fdeliverType as deliveryType ")
		.append(" from TCouponInformation ci inner join ci.TCouponObjects o ")
		.append(" inner join TCouponActivitycoupons ca on ci.id = ca.fcouponId ")
		.append(" inner join TDelivery d on ca.fdeliveryId = d.id")
		.append(" inner join TCouponChannel cc on d.id = cc.fdeliveryId")
		.append(" where cc.fstatus = 10 and d.fstatus = 40 and d.factivityType = 20 and cc.fbeginTime < :now and cc.fendTime > :now ")
		.append(" and d.fdeliveryStartTime <= :now and d.fdeliveryEndTime >= :now");

		Date now = new Date();
		hqlMap.put("now", now);
		hql.append(" and ci.id = :couponId");
		hqlMap.put("couponId", couponId);
		List<Map<String, Object>> list = commonService.find(hql.toString(), hqlMap);
		if (list == null || list.size() != 1) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(119);
			responseDTO.setMsg("该优惠券已过期！");
			return responseDTO;
		}
		Map<String, Object> cMap = list.get(0);

		boolean isReceiveCoupon = isReceiveCoupon(couponId, (Integer) cMap.get("reciveLimit"),
				customerDTO.getCustomerId());
		if (isReceiveCoupon) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您已经拥有该优惠券，不能重复领取！");
			return responseDTO;
		}
		if ((Integer) cMap.get("count") > 0 && (Integer) cMap.get("count") <= (Integer) cMap.get("sendCount")) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(109);
			responseDTO.setMsg("该优惠券领取完了！");
			return responseDTO;
		}
		if (((Integer) cMap.get("deliveryType")).intValue() == 130) {
			// 校验用户信息是否属于新用户
			TCustomerInfo t = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());
			if (t == null || (t.getForderTotal() != null && t.getForderTotal().compareTo(BigDecimal.ZERO) > 0)) {
				responseDTO.setSuccess(false);
				responseDTO.setStatusCode(139);
				responseDTO.setMsg("该活动仅限新客户参加！");
				return responseDTO;
			}
		}

		// 计算券的有效期
		Date useStartTime = null;
		Date useEndTime = null;
		if (cMap.get("validDays") != null && StringUtils.isNotBlank(cMap.get("validDays").toString())) {
			SimpleDateFormat sdfstart = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			SimpleDateFormat sdfend = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			try {
				useStartTime = DateUtils.parseDate(sdfstart.format(now).toString(), "yyyy-MM-dd HH:mm:ss");
				Calendar c = Calendar.getInstance();
				c.setTime(useStartTime);
				c.set(Calendar.DATE, c.get(Calendar.DATE) + (Integer) cMap.get("validDays") - 1);
				useEndTime = DateUtils.parseDate(sdfend.format(c.getTime()).toString(), "yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("couponId", couponId);
				map.put("customerId", customerDTO.getCustomerId());
				OutPutLogUtil.printLoggger(e, map, logger);
			}
		} else {
			useStartTime = (Date) cMap.get("couponStartTime");
			useEndTime = (Date) cMap.get("couponEndTime");
		}

		TCouponDelivery couponDelivery = new TCouponDelivery();
		couponDelivery.setTCustomer(new TCustomer(customerDTO.getCustomerId()));
		couponDelivery.setFdeliverTime(new Date());
		couponDelivery.setTCouponInformation(new TCouponInformation(couponId));
		couponDelivery.setTDelivery(new TDelivery(cMap.get("deliveryId").toString()));
		couponDelivery.setFuseEndTime(useEndTime);
		couponDelivery.setFuseStartTime(useStartTime);
		couponDelivery = couponDeliveryDAO.save(couponDelivery);
		Map<String, Object> returnData = Maps.newHashMap();
		
		CouponChannelDTO couponChannelDTO = new CouponChannelDTO();
		if (StringUtils.isNotBlank(couponDelivery.getId())) {
			couponChannelDTO.setCouponDeliveryId(couponDelivery.getId());
		}
		couponChannelDTO.setCouponId(couponId);
		if (cMap.get("title")!=null && StringUtils.isNotBlank(cMap.get("title").toString())) {
			couponChannelDTO.setTitle(cMap.get("title").toString());
		}
		
		if (cMap.get("subTitle") != null && StringUtils.isNotBlank(cMap.get("subTitle").toString())) {
			couponChannelDTO.setSubTitle(cMap.get("subTitle").toString());
		}

		couponChannelDTO.setUseStartTime(DateFormatUtils.format(useStartTime, "yyyy-MM-dd"));

		couponChannelDTO.setUseEndTime(DateFormatUtils.format(useEndTime, "yyyy-MM-dd"));

		if (cMap.get("limitation") != null && StringUtils.isNotBlank(cMap.get("limitation").toString())) {
			couponChannelDTO.setLimitation(new BigDecimal(cMap.get("limitation").toString()));
			couponChannelDTO.setLimitationInfo(
					new StringBuffer().append("满").append(cMap.get("limitation")).append("元可使用").toString());
		} else {
			couponChannelDTO.setLimitationInfo(
					new StringBuffer().append("直减").append(cMap.get("amount")).append("元").toString());
		}
		if (cMap.get("discount") != null && StringUtils.isNotBlank(cMap.get("discount").toString())) {
			BigDecimal discount = new BigDecimal(cMap.get("discount").toString()).multiply(new BigDecimal(10));
			couponChannelDTO.setDiscount(discount);
		}
		if (cMap.get("amount") != null && StringUtils.isNotBlank(cMap.get("amount").toString())) {
			couponChannelDTO.setAmount(new BigDecimal(cMap.get("amount").toString()));
		}
		if (cMap.get("useType") != null && StringUtils.isNotBlank(cMap.get("useType").toString())) {
			if (Integer.parseInt(cMap.get("useType").toString()) == 40) {
				String eventDetail = new StringBuilder().append(Constant.eventDetail)
						.append(cMap.get("objectId").toString()).toString();
				couponChannelDTO.setUseUrl(eventDetail);
			}else{
				couponChannelDTO.setUseUrl(Constant.H5Url);
			}
		}
		couponChannelDTO.setStatus(10);
		returnData.put("couponInfo", couponChannelDTO);
		TCouponActivitycoupons activitycoupons = couponActivitycouponsDAO.getTCouponActivitycoupons(couponId,
				cMap.get("deliveryId").toString());
		// 领取完之后在记录表中添加一条领取记录
		activitycoupons.setFsendCount(activitycoupons.getFsendCount() + 1);
		couponActivitycouponsDAO.save(activitycoupons);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("领取优惠券成功！");
		return responseDTO;
	}
}
