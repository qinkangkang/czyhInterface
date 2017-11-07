package com.innee.czyhInterface.service.v2;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
import org.springside.modules.utils.Exceptions;

import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerTicketDAO;
import com.innee.czyhInterface.dao.SceneUserDAO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.SceneUserInfoDTO;
import com.innee.czyhInterface.dto.m.UserDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerTicket;
import com.innee.czyhInterface.entity.TSceneUser;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.util.HeadImageUtil;
import com.innee.czyhInterface.util.IpUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.redis.RedisMoudel;

/**
 * interface业务管理类.
 * 
 * @author jinshenzhi
 */
@Component
@Transactional
public class SceneUserService {

	private static final Logger logger = LoggerFactory.getLogger(SceneUserService.class);

	// private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	public static final int INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final String ALGORITHM = "SHA-1";
	// private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private RedisService redisService;

	@Autowired
	private SceneUserDAO sceneUserDAO;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CustomerTicketDAO customerTicketDAO;

	public void entryptPassword(TCustomer customer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		customer.setFsalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(customer.getFpassword().getBytes(), salt, INTERATIONS);
		customer.setFpassword(Encodes.encodeHex(hashPassword));
	}

	public ResponseDTO getSceneUserInfo(Integer clientType, String unionid, String openid, String nickname,
			String headimgurl, String country, String province, String city, Integer sex, HttpServletRequest request,
			Boolean subscribe) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(unionid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("unionid参数不能为空，请检查unionid的传递参数值！");
			return responseDTO;
		}
		SceneUserInfoDTO sceneUserInfoDTO = new SceneUserInfoDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		TSceneUser tSceneUser = sceneUserDAO.findBysceneStrAndopenID(openid);
		TCustomer tCustomer = customerDAO.getByFweixinUnionIdAndFtype(unionid, 1);
		Date now = new Date();
		// 0.新用户可以领奖 1.老用户不可领奖 2.未关注用户关注后领奖
		if (!subscribe) {
			sceneUserInfoDTO.setDelivery(false);
			sceneUserInfoDTO.setStatus(2);
			sceneUserInfoDTO.setMsg1("请扫描工作人员二维码");
			sceneUserInfoDTO.setMsg2("并且关注零到壹微信公众号才可领奖");
			returnData.put("info", sceneUserInfoDTO);
		} else if (tSceneUser.getFdelivery() == 1) {
			sceneUserInfoDTO.setDelivery(false);
			sceneUserInfoDTO.setStatus(1);
			sceneUserInfoDTO.setMsg1("该礼品只有第一次注册零到壹的用户才能领取");
			sceneUserInfoDTO.setMsg2("您可以访问零到壹发现更多福利");
			returnData.put("info", sceneUserInfoDTO);
		} else if (tSceneUser.getFdelivery() == 0) {
			sceneUserInfoDTO.setDelivery(false);
			sceneUserInfoDTO.setStatus(0);
			sceneUserInfoDTO.setMsg1("您是零到壹新用户可以进行领取我们精美小礼品");
			sceneUserInfoDTO.setMsg2("您可以访问零到壹发现更多福利");
			returnData.put("info", sceneUserInfoDTO);
			sceneUserDAO.updateDelivery(openid, 1, now);
		}
		
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;
		// TCustomer tCustomer =
		// customerDAO.getByFweixinUnionIdAndFtype(unionid, 1);
		if (tCustomer == null) {
			tCustomer = new TCustomer();
			tCustomer.setFname(nickname);
			tCustomer.setFpassword(PropertiesUtil.getProperty("customerInitPassword"));
			// 当web端调用时，才保存openid
			if (clientType.intValue() == 1) {
				tCustomer.setFweixinId(openid);
			}
			tCustomer.setFweixinUnionId(unionid);
			tCustomer.setFweixinName(nickname);
			StringBuilder region = new StringBuilder();
			if (StringUtils.isNotBlank(country)) {
				region.append(country).append(" - ");
			}
			if (StringUtils.isNotBlank(province)) {
				region.append(province).append(" - ");
			}
			if (StringUtils.isNotBlank(city)) {
				region.append(city);
			}
			tCustomer.setFregion(region.toString());
			tCustomer.setFphoto(headimgurl);
			tCustomer.setFsex(sex);
			tCustomer.setFstatus(1);
			tCustomer.setFtype(1);
			tCustomer.setFcreateTime(now);
			tCustomer.setFupdateTime(now);
			entryptPassword(tCustomer);
			tCustomer = customerDAO.save(tCustomer);
			// 创建用户TICKET表记录
			TCustomerTicket tCustomerTicket = new TCustomerTicket();
			tCustomerTicket.setFcreateTime(now);
			tCustomerTicket.setFupdateTime(now);
			tCustomerTicket.setFcustomerId(tCustomer.getId());
			tCustomerTicket.setFtype(clientType);
			tCustomerTicket.setFticket(ticket);
			customerTicketDAO.save(tCustomerTicket);
			// 为用户添加扩展信息 并为用户打标
			customerService.saveCustomerInfo(tCustomer.getId(), 1, "ditui", null, null, null, null, clientType);
			// 新用户更新地推表
			if (tSceneUser != null) {
				sceneUserDAO.updateRegister(openid, 1, now);
			}

		} else if (tCustomer.getFstatus().equals(10)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("您的账号处于冻结状态，请联系零到壹客服人员！");
			return responseDTO;
		} else if (tCustomer.getFstatus().equals(999)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("您的账号处于作废状态，请联系零到壹客服人员！");
			return responseDTO;
		} else {
			// 当web端调用时并且weixinid为空的时候，设置openid到weixinid
			if (clientType.intValue() == 1 && StringUtils.isBlank(tCustomer.getFweixinId())) {
				customerDAO.saveWeixinId(openid, tCustomer.getId());
			}

			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			TCustomerTicket tCustomerTicket = customerTicketDAO.getByFcustomerIdAndFtype(tCustomer.getId(), clientType);
			if (tCustomerTicket == null) {
				tCustomerTicket = new TCustomerTicket();
				tCustomerTicket.setFcustomerId(tCustomer.getId());
				tCustomerTicket.setFtype(clientType);
				tCustomerTicket.setFcreateTime(now);
			} else {
				oldTicket = tCustomerTicket.getFticket();
			}
			tCustomerTicket.setFticket(ticket);
			tCustomerTicket.setFupdateTime(now);
			customerTicketDAO.save(tCustomerTicket);
		}

		try {
			try {
				redisService.putCache(RedisMoudel.TicketToId, ticket, tCustomer.getId());
				if (StringUtils.isNotBlank(oldTicket)) {
					redisService.removeCache(RedisMoudel.TicketToId, oldTicket);
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			}

			CustomerLogBean customerLogBean = new CustomerLogBean();
			customerLogBean.setCustomerId(tCustomer.getId());
			customerLogBean.setUrl(request.getRequestURI());
			customerLogBean.setCustomerType(1);
			customerLogBean.setCreateTime(now);
			customerLogBean.setClientType(clientType);
			customerLogBean.setClientIp(IpUtil.getIpAddr(request));
			customerLogBean.setTaskType(1);
			AsynchronousTasksManager.put(customerLogBean);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(tCustomer.getId());
		userDTO.setType(tCustomer.getFtype());
		userDTO.setName(tCustomer.getFname());
		userDTO.setWxName(tCustomer.getFweixinName());
		userDTO.setHeadimgUrl(HeadImageUtil.getHeadImage(tCustomer.getFphoto(), 64));
		userDTO.setTicket(ticket);
		userDTO.setSessionId(request.getSession().getId());
//		userDTO.setBaby(tCustomer.getFbaby());
		userDTO.setWxId(tCustomer.getFweixinId());
		userDTO.setPhone(tCustomer.getFphone());
		userDTO.setSex(tCustomer.getFsex() != null ? tCustomer.getFsex().toString() : StringUtils.EMPTY);
		userDTO.setAddress(tCustomer.getFregion());
		/*
		 * // 修改用户扩展信息并修改同步用户打标 updateCustomerInfo(tCustomer.getId(), 1,
		 * channel, version, deviceInfo, deviceId, deviceTokens, clientType);
		 */

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("微信用户登录应用成功！");
		returnData.put("userInfo", userDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

}