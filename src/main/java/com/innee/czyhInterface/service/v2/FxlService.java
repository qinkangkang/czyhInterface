package com.innee.czyhInterface.service.v2;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.Identities;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.CommentDAO;
import com.innee.czyhInterface.dao.ConsultDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerTagDAO;
import com.innee.czyhInterface.dao.CustomerTicketDAO;
import com.innee.czyhInterface.dao.DictionaryDao;
import com.innee.czyhInterface.dao.EncryptRetDAO;
import com.innee.czyhInterface.dao.EventCategoryDAO;
import com.innee.czyhInterface.dao.ImageDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.OrderStatusChangeDAO;
import com.innee.czyhInterface.dao.SmsDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerTicket;
import com.innee.czyhInterface.entity.TDictionary;
import com.innee.czyhInterface.entity.TEncryptRet;
import com.innee.czyhInterface.entity.TEventCategory;
import com.innee.czyhInterface.entity.TImage;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TOrderStatusChange;
import com.innee.czyhInterface.entity.TSms;
import com.innee.czyhInterface.exception.ServiceException;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.ImageUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.quota.CustomerQuotaBean;
import com.innee.czyhInterface.util.quota.EventQuotaBean;
import com.innee.czyhInterface.util.redis.RedisMoudel;
import com.innee.czyhInterface.util.sms.SmsResult;
import com.innee.czyhInterface.util.sms.SmsUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * interface业务管理类.
 * 
 * @author jinshenzhi
 */
@Component
@Transactional
public class FxlService {

	private static final Logger logger = LoggerFactory.getLogger(FxlService.class);
	
	private static String USER_AGENT = "Mozilla/2.0";

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ImageDAO imageDao;

	@Autowired
	private OrderStatusChangeDAO orderStatusChangeDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private SmsDAO smsDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private ConsultDAO consultDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private CustomerTagDAO customerTagDAO;

	@Autowired
	private CustomerTicketDAO customerTicketDAO;

	@Autowired
	private DictionaryDao dictionaryDao;

	@Autowired
	private EventCategoryDAO eventCategoryDAO;

	@Autowired
	private PublicService publicService;

	@Autowired
	private CommentDAO commentDAO;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private EncryptRetDAO encryptRetDAO;

	@Transactional(readOnly = true)
	public void initDictionary() {
		Map<Long, Map<String, Map<Integer, String>>> dictionaryCalssMap = Maps.newHashMap();
		Map<Long, Map<String, String>> nameCodeMap = Maps.newHashMap();
		List<TDictionary> list = dictionaryDao.findAllOrderByClassId();
		Map<String, Map<Integer, String>> dictionaryMap = null;
		Map<Integer, String> dictionaryCnMap = null;
		Map<Integer, String> dictionaryEnMap = null;
		Map<String, String> dictionaryNameMap = null;
		long tempId = 0;

		for (TDictionary tDictionary : list) {
			if (!tDictionary.getTDictionaryClass().getId().equals(tempId)) {
				dictionaryCnMap = Maps.newTreeMap();
				dictionaryEnMap = Maps.newTreeMap();
				dictionaryMap = Maps.newHashMap();
				dictionaryNameMap = Maps.newTreeMap();

				dictionaryMap.put("zh_CN", dictionaryCnMap);
				dictionaryMap.put("en_US", dictionaryEnMap);
				dictionaryCalssMap.put(tDictionary.getTDictionaryClass().getId(), dictionaryMap);
				nameCodeMap.put(tDictionary.getTDictionaryClass().getId(), dictionaryNameMap);
			} else {

			}
			if (tDictionary.getCode() != null) {
				dictionaryNameMap.put(tDictionary.getCode(), tDictionary.getName());
			}
			dictionaryCnMap.put(tDictionary.getValue(), tDictionary.getName());
			dictionaryEnMap.put(tDictionary.getValue(), tDictionary.getCode());
			tempId = tDictionary.getTDictionaryClass().getId();
		}

		if (!CollectionUtils.isEmpty(list)) {
			DictionaryUtil.setCodeCalssMap(dictionaryCalssMap, nameCodeMap);
		}
	}

	@Transactional(readOnly = true)
	public void initEventCategory() {
		Cache eventCategoryCache = cacheManager.getCache(Constant.EventCategory);
		eventCategoryCache.removeAll();
		// 获取所有活动一级类目
		List<TEventCategory> eventCategoryList = eventCategoryDAO.findByLevel(1);

		Element element = null;
		for (TEventCategory tEventCategory : eventCategoryList) {
			element = new Element(tEventCategory.getValue(), tEventCategory.getName());
			eventCategoryCache.put(element);
		}
	}

	/**
	 * 保存移动端上传的头像图片保存起来，并且返回图片的相对路径
	 * 
	 * @param pathVar
	 * @param file
	 *            Base64格式的图片文件字符串
	 * @return
	 */
	public String saveBase64Image(String pathVar, String file) {
		String filePathVar = PropertiesUtil.getProperty(pathVar);// personalCustomerLogoPath
		// 定义相对路径，并且将日期作为分隔子目录
		StringBuilder relativePath = new StringBuilder(filePathVar)
				.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).append("/");
		// 定义全路径，为了以后将相对路径和文件名添加进去
		StringBuilder rootPath = new StringBuilder(Constant.RootPath)
				.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath);

		File tempFile = null;
		try {
			String[] headAndBody = StringUtils.split(file, ",");
			// String head = headAndBody[0];
			String body = headAndBody[1];

			// 先创建保存文件的目录
			File destDir = new File(rootPath.toString());
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			// 定义最终目标文件对象
			File destFile = null;
			// 定义存储文件名
			String storeFileName = Identities.uuid2() + ".jpg";
			tempFile = new File(System.getProperty("java.io.tmpdir"), storeFileName);
			// 将Base64字符串图片内容转换成byte数组
			byte[] fileByte = Base64Utils.decodeFromString(body);

			// 将byte数组写入临时文件中
			org.apache.commons.io.FileUtils.writeByteArrayToFile(tempFile, fileByte, false);
			// 目标文件
			destFile = new File(rootPath.append(storeFileName).toString());
			// 将临时文件剪裁成为200高宽的正方图保存到目标文件中
			ImageUtil.square(tempFile, destFile, 200);
			// 相对路径加上保存文件名得到图片文件的相对全路径
			relativePath.append(storeFileName);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("保存上传的附件文件时出错！");
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
		return relativePath.toString();
	}

	/**
	 * 保存移动端上传的头像图片保存起来，并且返回图片的相对路径
	 * 
	 * @param pathVar
	 * @param file
	 *            Base64格式的图片文件字符串
	 * @return
	 */
	public String saveFileImage(String pathVar, MultipartFile file) {
		String filePathVar = PropertiesUtil.getProperty(pathVar);// personalCustomerLogoPath
		// 定义相对路径，并且将日期作为分隔子目录
		StringBuilder relativePath = new StringBuilder(filePathVar)
				.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).append("/");
		// 定义全路径，为了以后将相对路径和文件名添加进去
		StringBuilder rootPath = new StringBuilder(Constant.RootPath)
				.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath);

		File tempFile = null;
		try {

			// 先创建保存文件的目录
			File destDir = new File(rootPath.toString());
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			// 定义最终目标文件对象
			File destFile = null;
			// 定义存储文件名
			String storeFileName = Identities.uuid2() + ".jpg";
			tempFile = new File(System.getProperty("java.io.tmpdir"), storeFileName);
			file.transferTo(tempFile);
			// 目标文件
			destFile = new File(rootPath.append(storeFileName).toString());
			// 将临时文件剪裁成为200高宽的正方图保存到目标文件中
			ImageUtil.square(tempFile, destFile, 200);
			// 相对路径加上保存文件名得到图片文件的相对全路径
			relativePath.append(storeFileName);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			throw new ServiceException("保存上传的附件文件时出错！");
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
		return relativePath.toString();
	}

	@Transactional(readOnly = true)
	public String getImageUrl(String id, boolean isThumbnail) {
		String res = null;

		if (StringUtils.isBlank(id)) {
			return StringUtils.EMPTY;
		}
		TImage tImage = imageDao.getOne(Long.valueOf(id));
		try {
			if (tImage.getFlag() == 1) {// 返回老版本图片服务器图片路径
				StringBuilder imageUrl = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append(tImage.getRelativePath());
				if (isThumbnail && tImage.getIsThumbnail().equals(1)) {
					imageUrl.append(tImage.getThumbnailFileName());
				} else {
					imageUrl.append(tImage.getStoreFileName());
				}
				imageUrl.append(".").append(tImage.getStorefileExt());
				res = imageUrl.toString();
			} else if (tImage.getFlag() == 2) {// 返回新版本七牛图片服务器路径
				StringBuilder imageUrl = new StringBuilder(PropertiesUtil.getProperty("publicQnEventService"))
						.append(tImage.getRelativePath()).append(tImage.getStoreFileName()).append(".")
						.append(tImage.getStorefileExt());
				// 返回 图片尺寸可调整 现在为75p
				res = publicService.getThumbnailview(imageUrl.toString(), 75);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			if (isThumbnail) {
				res = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString();
			} else {
				res = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString();
			}

		}
		return res;
	}

	@Transactional(readOnly = true)
	public String[] getImageUrls(String ids, boolean isThumbnail) {
		if (StringUtils.isBlank(ids)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		String[] urls = ids.split(";");
		List<Long> idList = Lists.newArrayList();
		for (String string : urls) {
			if (StringUtils.isNotBlank(string)) {
				idList.add(Long.valueOf(string));
			}
		}
		List<TImage> imageList = imageDao.findByIdIn(idList);

		String rootUrl = PropertiesUtil.getProperty("fileServerUrl") + PropertiesUtil.getProperty("imageRootPath");
		String[] imageUrls = ArrayUtils.EMPTY_STRING_ARRAY;

		StringBuilder imageUrl = new StringBuilder();
		try {
			for (TImage tImage : imageList) {

				if (tImage.getFlag() == 1) {
					imageUrl.delete(0, imageUrl.length());
					imageUrl.append(rootUrl).append(tImage.getRelativePath());

					if (isThumbnail && tImage.getIsThumbnail().equals(1)) {
						imageUrl.append(tImage.getThumbnailFileName());
					} else {
						imageUrl.append(tImage.getStoreFileName());
					}
					imageUrl.append(".").append(tImage.getStorefileExt());

					imageUrls = ArrayUtils.add(imageUrls, imageUrl.toString());
				} else if (tImage.getFlag() == 2) {
					imageUrl.delete(0, imageUrl.length());
					imageUrl.append(PropertiesUtil.getProperty("publicQnEventService")).append(tImage.getRelativePath())
							.append(tImage.getStoreFileName()).append(".").append(tImage.getStorefileExt());

					// 返回 图片尺寸可调整 现在为75p
					imageUrls = ArrayUtils.add(imageUrls, publicService.getThumbnailview(imageUrl.toString(), 75));
				}

			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			if (isThumbnail) {
				imageUrls = ArrayUtils.add(imageUrls,
						new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
								.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPicThumbnail.jpg")
								.toString());
			} else {
				imageUrls = ArrayUtils.add(imageUrls, new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
						.append(PropertiesUtil.getProperty("imageRootPath")).append("/foms/noPic.jpg").toString());
			}

		}
		return imageUrls;
	}

	@Transactional(readOnly = true)
	public ResponseDTO updateSmsSwitch(Map<String, Object> valueMap) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (valueMap.containsKey("CheckCodeSwitch")
				&& StringUtils.isNotBlank(valueMap.get("CheckCodeSwitch").toString())) {
			if (valueMap.get("CheckCodeSwitch").toString().equals("1")) {
				SmsUtil.setCheckCodeSwitch(true);
			} else {
				SmsUtil.setCheckCodeSwitch(false);
			}
		}
		if (valueMap.containsKey("PaySuccessSwitch")
				&& StringUtils.isNotBlank(valueMap.get("PaySuccessSwitch").toString())) {
			if (valueMap.get("PaySuccessSwitch").toString().equals("1")) {
				SmsUtil.setPaySuccessSwitch(true);
			} else {
				SmsUtil.setPaySuccessSwitch(false);
			}
		}
		if (valueMap.containsKey("PayZeroSuccessSwitch")
				&& StringUtils.isNotBlank(valueMap.get("PayZeroSuccessSwitch").toString())) {
			if (valueMap.get("PayZeroSuccessSwitch").toString().equals("1")) {
				SmsUtil.setPayZeroSuccessSwitch(true);
			} else {
				SmsUtil.setPayZeroSuccessSwitch(false);
			}
		}
		if (valueMap.containsKey("VerificationSuccessSwitch")
				&& StringUtils.isNotBlank(valueMap.get("VerificationSuccessSwitch").toString())) {
			if (valueMap.get("VerificationSuccessSwitch").toString().equals("1")) {
				SmsUtil.setVerificationSuccessSwitch(true);
			} else {
				SmsUtil.setVerificationSuccessSwitch(false);
			}
		}
		if (valueMap.containsKey("TimeOutNoPaySwitch")
				&& StringUtils.isNotBlank(valueMap.get("TimeOutNoPaySwitch").toString())) {
			if (valueMap.get("TimeOutNoPaySwitch").toString().equals("1")) {
				SmsUtil.setTimeOutNoPaySwitch(true);
			} else {
				SmsUtil.setTimeOutNoPaySwitch(false);
			}
		}
		if (valueMap.containsKey("RefundSuccessSwitch")
				&& StringUtils.isNotBlank(valueMap.get("RefundSuccessSwitch").toString())) {
			if (valueMap.get("RefundSuccessSwitch").toString().equals("1")) {
				SmsUtil.setRefundSuccessSwitch(true);
			} else {
				SmsUtil.setRefundSuccessSwitch(false);
			}
		}
		if (valueMap.containsKey("LoginPwdSwitch")
				&& StringUtils.isNotBlank(valueMap.get("LoginPwdSwitch").toString())) {
			if (valueMap.get("LoginPwdSwitch").toString().equals("1")) {
				SmsUtil.setLoginPwdSwitch(true);
			} else {
				SmsUtil.setLoginPwdSwitch(false);
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("CheckCodeSwitch", SmsUtil.isCheckCodeSwitch());
		returnData.put("PayZeroSuccessSwitch", SmsUtil.isPayZeroSuccessSwitch());
		returnData.put("PaySuccessSwitch", SmsUtil.isPaySuccessSwitch());
		returnData.put("VerificationSuccessSwitch", SmsUtil.isVerificationSuccessSwitch());
		returnData.put("TimeOutNoPaySwitch", SmsUtil.isTimeOutNoPaySwitch());
		returnData.put("RefundSuccessSwitch", SmsUtil.isRefundSuccessSwitch());
		returnData.put("LoginPwdSwitch", SmsUtil.isLoginPwdSwitch());
		responseDTO.setData(returnData);
		responseDTO.setMsg("更新零到壹服务接口系统短信开关，如果需要启停开关，请在请求参数中添加开关值。0表示关闭；1表示开启！");
		return responseDTO;
	}

	/**
	 * 用户是否领取改优惠券
	 * 
	 * @param couponId
	 *            优惠券ID
	 * @param customerId
	 *            用户ID
	 * @return false表示没有领取， true表示已经领取
	 *//*
		 * @Transactional(readOnly = true) public boolean isReceiveCoupon(String
		 * couponId, String customerId) { int useCouponNum =
		 * couponDeliveryDAO.isReceiveCoupon(couponId, customerId); return
		 * useCouponNum == 0 ? false : true; }
		 */

	@Transactional(propagation = Propagation.REQUIRES_NEW)
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

	@Transactional(readOnly = true)
	public ResponseDTO setupEventQuota(Integer onOff, String eventId, Integer quotaCount, Integer quotaType) {
		ResponseDTO responseDTO = new ResponseDTO();
		// 获取活动限购缓存对象
		Cache eventQuotaCache = cacheManager.getCache(Constant.EventQuota);
		Element ele = null;
		if (onOff != null) {
			if (onOff.intValue() == 1) {
				ele = eventQuotaCache.get(eventId);
				if (ele == null) {
					EventQuotaBean eventQuotaBean = new EventQuotaBean();
					eventQuotaBean.setEventId(eventId);
					eventQuotaBean.setQuotaCount(quotaCount);
					eventQuotaBean.setQuotaType(quotaType);
					ele = new Element(eventId, eventQuotaBean);
					eventQuotaCache.put(ele);
				} else {
					EventQuotaBean eventQuotaBean = (EventQuotaBean) ele.getObjectValue();
					eventQuotaBean.setQuotaCount(quotaCount);
					eventQuotaBean.setQuotaType(quotaType);
				}
			} else {
				eventQuotaCache.remove(eventId);
			}

			// 获取客户已购买限制数缓存对象
			Cache customerQuotaCache = cacheManager.getCache(Constant.CustomerQuota);
			Map<Object, Element> map = customerQuotaCache.getAll(customerQuotaCache.getKeys());
			for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
				Map.Entry<Object, Element> e = (Map.Entry<Object, Element>) it.next();
				ele = customerQuotaCache.get(e.getKey());
				CustomerQuotaBean customerQuotaBean = (CustomerQuotaBean) ele.getObjectValue();
				customerQuotaBean.getEventBuyMap().remove(eventId);
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		Map<Object, Element> map = eventQuotaCache.getAll(eventQuotaCache.getKeys());
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<Object, Element> e = (Map.Entry<Object, Element>) it.next();
			ele = eventQuotaCache.get(e.getKey());
			EventQuotaBean eventQuotaBean = (EventQuotaBean) ele.getObjectValue();
			returnData.put(eventQuotaBean.getEventId(), eventQuotaBean);
		}
		responseDTO.setData(returnData);
		responseDTO.setMsg("更新零到壹设置限购规则器接口！");
		return responseDTO;
	}

	public void addCustomerBuy(String eventId, String customerId, int buyCount) {
		// 获取活动限购缓存对象
		Cache eventQuotaCache = cacheManager.getCache(Constant.EventQuota);
		// 先获取活动的限购对象
		Element ele = eventQuotaCache.get(eventId);
		// 如果限购对象为空，证明此活动不限购，返回false
		if (ele == null) {
			return;
		}

		// 获取客户已购买限制数缓存对象
		Cache customerQuotaCache = cacheManager.getCache(Constant.CustomerQuota);
		ele = customerQuotaCache.get(customerId);
		if (ele != null) {
			CustomerQuotaBean customerQuotaBean = (CustomerQuotaBean) ele.getObjectValue();
			if (customerQuotaBean.getEventBuyMap().containsKey(eventId)) {
				int boughtCount = customerQuotaBean.getEventBuyMap().get(eventId);
				customerQuotaBean.getEventBuyMap().put(eventId, boughtCount + buyCount);
			}
		}
	}

	public void subCustomerBuy(String eventId, String customerId, int buyCount) {
		// 获取活动限购缓存对象
		Cache eventQuotaCache = cacheManager.getCache(Constant.EventQuota);
		// 先获取活动的限购对象
		Element ele = eventQuotaCache.get(eventId);
		// 如果限购对象为空，证明此活动不限购，返回false
		if (ele == null) {
			return;
		}
		EventQuotaBean eventQuotaBean = (EventQuotaBean) ele.getObjectValue();
		if (eventQuotaBean.getQuotaType() == 0) {
			// 获取客户已购买限制数缓存对象
			Cache customerQuotaCache = cacheManager.getCache(Constant.CustomerQuota);
			ele = customerQuotaCache.get(customerId);
			if (ele != null) {
				CustomerQuotaBean customerQuotaBean = (CustomerQuotaBean) ele.getObjectValue();
				if (customerQuotaBean.getEventBuyMap().containsKey(eventId)) {
					int boughtCount = customerQuotaBean.getEventBuyMap().get(eventId);
					customerQuotaBean.getEventBuyMap().put(eventId,
							(boughtCount - buyCount) < 0 ? 0 : (boughtCount - buyCount));
				}
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean isQuota(String eventId, String customerId, int buyCount) {
		// 获取活动限购缓存对象
		Cache eventQuotaCache = cacheManager.getCache(Constant.EventQuota);
		// 先获取活动的限购对象
		Element ele = eventQuotaCache.get(eventId);
		// 如果限购对象为空，证明此活动不限购，返回false
		if (ele == null) {
			return false;
		}
		EventQuotaBean eventQuotaBean = (EventQuotaBean) ele.getObjectValue();
		// 如果购买的数量大于活动的限购数量，直接返回true
		if (eventQuotaBean.getQuotaCount() < buyCount) {
			return true;
		}
		int boughtCount = 0;
		// 获取客户已购买限制数缓存对象
		Cache customerQuotaCache = cacheManager.getCache(Constant.CustomerQuota);
		ele = customerQuotaCache.get(customerId);
		if (ele == null) {
			if (eventQuotaBean.getQuotaType() == 0) {
				boughtCount = orderDAO.sumBuyCount(customerId, eventId, 100);
			} else {
				boughtCount = orderDAO.sumBuyCount(customerId, eventId, 999);
			}

			Map<String, Integer> eventBuyMap = Maps.newHashMap();
			eventBuyMap.put(eventId, boughtCount);

			CustomerQuotaBean customerQuotaBean = new CustomerQuotaBean();
			customerQuotaBean.setCustomerId(customerId);
			customerQuotaBean.setEventBuyMap(eventBuyMap);

			ele = new Element(customerId, customerQuotaBean);
			customerQuotaCache.put(ele);
		} else {
			CustomerQuotaBean customerQuotaBean = (CustomerQuotaBean) ele.getObjectValue();

			if (customerQuotaBean.getEventBuyMap().containsKey(eventId)) {
				boughtCount = customerQuotaBean.getEventBuyMap().get(eventId);
			} else {
				if (eventQuotaBean.getQuotaType() == 0) {
					boughtCount = orderDAO.sumBuyCount(customerId, eventId, 100);
				} else {
					boughtCount = orderDAO.sumBuyCount(customerId, eventId, 999);
				}
				customerQuotaBean.getEventBuyMap().put(eventId, boughtCount);
			}
		}
		// 如果下单数量大于活动限购数减去客户已购买数，返回限购，否则返回不限购
		if (buyCount > eventQuotaBean.getQuotaCount() - boughtCount) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int getQuota(String eventId) {
		// 获取活动限购缓存对象
		Cache eventQuotaCache = cacheManager.getCache(Constant.EventQuota);
		// 先获取活动的限购对象
		Element ele = eventQuotaCache.get(eventId);
		// 如果限购对象为空，证明此活动不限购，返回false
		if (ele == null) {
			return 0;
		}
		EventQuotaBean eventQuotaBean = (EventQuotaBean) ele.getObjectValue();
		return eventQuotaBean.getQuotaCount();
	}

	public int sendSms() {
		// StringBuilder orderInfo = new StringBuilder();
		// orderInfo.append("[").append(tOrder.getFeventTitle()).append("]").append("【订单号：")
		// .append(tOrder.getForderNum()).append("】");
		//
		Map<String, String> smsParamMap = Maps.newHashMap();
		// smsParamMap.put("orderName", orderInfo.toString());
		// smsParamMap.put("orderList", "【我的-我的报名】");

		// 发送订单支付成功通知短信
		// SmsResult smsResult = SmsUtil.sendSms(SmsUtil.PaySuccessSms,
		// tOrder.getFcustomerPhone(), smsParamMap);
		// smsParamMap.put("why", "因为曾在零到壹购买过亲子活动产品");
		// smsParamMap.put("evaluate", "需要亲点击链接填写问卷");
		// smsParamMap.put("reward", "会奉上20元零到壹红包哟");

		smsParamMap.put("czyhInterface", "零到壹微信公众号");
		smsParamMap.put("couponList", "[我的—我的优惠券]");
		List<String> phones = null;
		try {
			phones = FileUtils.readLines(new File("D:\\ZZ\\phone.txt"), "UTF-8");
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		int i = 0;
		for (String phone : phones) {
			SmsResult smsResult = SmsUtil.sendSms(SmsUtil.ThankSms, phone, smsParamMap);

			TSms tSms = new TSms();
			tSms.setSendPhone(phone);
			tSms.setSendTime(new Date());
			tSms.setSmsContent(smsResult.getContent());
			tSms.setSmsType(SmsUtil.ThankSms);
			tSms.setSendResponse(smsResult.getResponse());
			if (smsResult.isSuccess()) {
				tSms.setSendSuccess(1);
			} else {
				tSms.setSendSuccess(0);
			}
			smsDAO.save(tSms);
			i++;
		}
		return i;
	}

	public void ok() {
		try {
			List<String> infoCustomerIdList = customerInfoDAO.findByCustomerIdList();

			StringBuilder hql = new StringBuilder();
			Map<String, Object> hqlMap = Maps.newHashMap();
			Date now = new Date();

			TCustomerInfo customerInfo = null;
			List<String> list = customerDAO.findByEnableCustomer(1, 1);
			for (String customerId : list) {
				if (!infoCustomerIdList.contains(customerId)) {
					customerInfo = new TCustomerInfo();
					customerInfo.setFcustomerId(customerId);
					customerInfo.setFpoint(0);
					customerInfo.setFusedPoint(0);
					customerInfo.setFregisterTime(now);
					customerInfo.setFtipNumber(0);
					customerInfo.setFcreateTime(now);
					customerInfo.setFupdateTime(now);
				} else {
					customerInfo = customerInfoDAO.getByCustomerId(customerId);
				}

				// 计算用户的订单总数
				hql.delete(0, hql.length());
				hql.append(
						"select COALESCE(count(t.id),0) as a from TOrder t where t.TCustomer.id = :customerId and t.fstatus < 999");
				hqlMap.clear();
				hqlMap.put("customerId", customerId);
				Query q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				List<Map<String, Object>> list2 = q.getResultList();
				Map<String, Object> bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("a") != null && StringUtils.isNotBlank(bmap.get("a").toString())) {
						customerInfo.setForderNumber(Integer.valueOf(bmap.get("a").toString()));
					}
				}
				// 计算用户的订单总金额
				hql.delete(0, hql.length());
				hql.append(
						"select COALESCE(sum(t.ftotal),0) as a from TOrder t where t.TCustomer.id = :customerId and t.fstatus <= 120");
				hqlMap.clear();
				hqlMap.put("customerId", customerId);
				q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				list2 = q.getResultList();
				bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("a") != null && StringUtils.isNotBlank(bmap.get("a").toString())) {
						customerInfo.setForderTotal(new BigDecimal(bmap.get("a").toString()));
					}
				}

				// 计算用户的支付非零元订单数量
				hql.delete(0, hql.length());
				hql.append(
						"select COALESCE(count(t.id),0) as a from TOrder t where t.TCustomer.id = :customerId and t.ftotal > 0 and t.fstatus <= 120");
				hqlMap.clear();
				hqlMap.put("customerId", customerId);
				q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				list2 = q.getResultList();
				bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("a") != null && StringUtils.isNotBlank(bmap.get("a").toString())) {
						customerInfo.setFpayOrderNumber(Integer.valueOf(bmap.get("a").toString()));
					}
				}

				// 计算用户的零元订单数量
				hql.delete(0, hql.length());
				hql.append(
						"select COALESCE(count(t.id),0) as a from TOrder t where t.TCustomer.id = :customerId and t.ftotal = 0 and t.fstatus <= 120");
				hqlMap.clear();
				hqlMap.put("customerId", customerId);
				q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				list2 = q.getResultList();
				bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("a") != null && StringUtils.isNotBlank(bmap.get("a").toString())) {
						customerInfo.setFpayZeroOrderNumber(Integer.valueOf(bmap.get("a").toString()));
					}
				}

				// 计算用户的退款订单数量
				hql.delete(0, hql.length());
				hql.append(
						"select COALESCE(count(t.id),0) as a from TOrder t where t.TCustomer.id = :customerId and t.fstatus = 120");
				hqlMap.clear();
				hqlMap.put("customerId", customerId);
				q = commonService.createQuery(hql.toString(), hqlMap);
				q.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				q.setFirstResult(0).setMaxResults(1);
				list2 = q.getResultList();
				bmap = CollectionUtils.isNotEmpty(list2) ? list2.get(0) : null;
				if (MapUtils.isNotEmpty(bmap)) {
					if (bmap.get("a") != null && StringUtils.isNotBlank(bmap.get("a").toString())) {
						customerInfo.setFrefundOrderNumber(Integer.valueOf(bmap.get("a").toString()));
					}
				}
				customerInfoDAO.save(customerInfo);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

	}

	/**
	 * 根据ticket获取用户ID
	 * 
	 * @param ticket
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public String getCustomerIdByTicket(String ticket, Integer clientType) {
		String customerId = null;

		String ticketElement = null;
		try {
			ticketElement = redisService.getValue(ticket, RedisMoudel.TicketToId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ticketElement == null) {
			// 获取用户TICKET记录
			TCustomerTicket tCustomerTicket = customerTicketDAO.getByFticketAndFtype(ticket, clientType);
			if (tCustomerTicket != null) {
				redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomerTicket.getFcustomerId());
				customerId = tCustomerTicket.getFcustomerId();
			}
		} else {
			customerId = ticketElement.toString();
		}
		return customerId;
	}

	/**
	 * 根据ticket获取用户缓存中的用户DTO对象
	 * 
	 * @param ticket
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public CustomerDTO getCustomerByTicket(String ticket, Integer clientType) {
		CustomerDTO customerDTO = new CustomerDTO();

		String ticketElement = null;
		try {
			ticketElement = redisService.getValue(ticket, RedisMoudel.TicketToId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String customerId = null;
		if (ticketElement == null) {
			// 获取用户TICKET记录
			TCustomerTicket tCustomerTicket = customerTicketDAO.getByFticketAndFtype(ticket, clientType);
			if (tCustomerTicket == null) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(100);
				customerDTO.setMsg("您的帐号信息已失效,请重新登录！");
				return customerDTO;
			}
			redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomerTicket.getFcustomerId());
			customerId =  tCustomerTicket.getFcustomerId();
		}else{
			customerId =  ticketElement.toString();
		}
		// 获取用户DTO缓存对象
		String CustomerEentity = null;
		try {
			CustomerEentity = redisService.getValue(customerId, RedisMoudel.CustomerEentity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (CustomerEentity == null) {
			TCustomer customer = customerDAO.findOne(customerId);
			if (customer == null) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(101);
				customerDTO.setMsg("您的帐号异常，请联系客服处理！");
				return customerDTO;
			} else if (customer.getFstatus().equals(999)) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(101);
				customerDTO.setMsg("您的账号已作废，请联系客服处理！");
				return customerDTO;
			} else if (customer.getFtype().equals(10)) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(102);
				customerDTO.setMsg("您的帐号异常，请联系客服处理！");
				return customerDTO;
			} else if (customer.getFstatus().equals(10)) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(102);
				customerDTO.setMsg("您的帐号已冻结，请联系客服处理！");
				return customerDTO;
			} else {
				customerDTO.setCustomerId(customerId);
				customerDTO.setPhone(customer.getFphone());
				customerDTO.setSex(customer.getFsex() != null ? customer.getFsex() : 0);
				customerDTO.setPhoto(customer.getFphoto());
				customerDTO.setName(customer.getFname());
				customerDTO.setWxId(customer.getFweixinId());
				customerDTO.setWxUnionId(customer.getFweixinUnionId());
				customerDTO.setWxName(customer.getFweixinName());
				customerDTO.setBaby(customer.getFbaby());
				customerDTO.setEnable(true);
				redisService.putCache(RedisMoudel.CustomerEentity, customerId, mapper.toJson(customerDTO));
			}
		} else {
			customerDTO = mapper.fromJson(CustomerEentity, CustomerDTO.class);
		}
		return customerDTO;
	}

	/**
	 * 根据ticket获取用户缓存中的用户DTO对象
	 * 
	 * @param ticket
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	public CustomerDTO getCustomerByCustomerId(String customerId) {
		CustomerDTO customerDTO = new CustomerDTO();
		// 获取用户DTO缓存对象
		String CustomerEentity = null;
		try {
			CustomerEentity = redisService.getValue(customerId, RedisMoudel.CustomerEentity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (CustomerEentity == null) {
			TCustomer customer = customerDAO.findOne(customerId);
			if (customer == null) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(101);
				customerDTO.setMsg("您的帐号异常，请联系客服处理！");
				return customerDTO;
			} else if (customer.getFstatus().equals(10)) {
				customerDTO.setEnable(false);
				customerDTO.setStatusCode(102);
				customerDTO.setMsg("您的帐号已冻结，请联系客服处理！");
				return customerDTO;
			} else {
				customerDTO.setCustomerId(customerId);
				customerDTO.setPhone(customer.getFphone());
				customerDTO.setSex(customer.getFsex() != null ? customer.getFsex() : 0);
				customerDTO.setPhoto(customer.getFphoto());
				customerDTO.setName(customer.getFname());
				customerDTO.setWxId(customer.getFweixinId());
				customerDTO.setWxUnionId(customer.getFweixinUnionId());
				customerDTO.setWxName(customer.getFweixinName());
				customerDTO.setBaby(customer.getFbaby());
				customerDTO.setEnable(true);
				redisService.putCache(RedisMoudel.CustomerEentity, customerId, mapper.toJson(customerDTO));
			}
		} else {
			customerDTO = mapper.fromJson(CustomerEentity, CustomerDTO.class);
		}
		return customerDTO;
	}

	/**
	 * 查询数据库是否连接
	 * 
	 * @param ticket
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO callInterface() {
		ResponseDTO responseDTO = new ResponseDTO();
		StringBuilder hql = new StringBuilder();
		hql.append("select 1 from TCouponDelivery");
		// 用优惠券表
		List<Map<String, Object>> list = commonService.find(hql.toString());
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("数据库连接成功");
		return responseDTO;
	}
	
	
	public String httpEncrypt(String addressIP, Integer clientType) {

		TEncryptRet tEncryptRet = encryptRetDAO.findToken(new Long(clientType));
		
		StringBuilder uploadJson = new StringBuilder();
		uploadJson.append(addressIP);
		uploadJson.append(tEncryptRet.getFretToken());
		uploadJson.append(clientType);
		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000 / 60 / 60 / 24 ));
		uploadJson.append(timestamp);
		uploadJson.append(USER_AGENT);

		String sign = DigestUtils.md5Hex(uploadJson.toString());
		return sign;
	}
	
	public static void main(String[] args) {
		
		StringBuilder uploadJson = new StringBuilder();
		uploadJson.append("61.148.60.234");
		uploadJson.append("sVACqyTyUNALZinn");
		uploadJson.append("1");
		String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000 / 60 / 60 / 24 ));
		uploadJson.append( timestamp);
		uploadJson.append( USER_AGENT);
		String sign = DigestUtils.md5Hex("61.148.60.234sVACqyTyUNALZinn117410Mozilla/2.0");
		System.out.println(sign);
	}

}