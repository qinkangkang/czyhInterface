package com.innee.czyhInterface.service.v2;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.Identities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.common.dict.ResponseConfigurationDict;
import com.innee.czyhInterface.dao.CustomerBargainingDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerSubscribeDAO;
import com.innee.czyhInterface.dao.CustomerTicketDAO;
import com.innee.czyhInterface.dao.EventBargainingDAO;
import com.innee.czyhInterface.dao.PosterDAO;
import com.innee.czyhInterface.dao.SceneUserDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.SceneStrDTO;
import com.innee.czyhInterface.dto.m.UserDTO;
import com.innee.czyhInterface.dto.m.WxMpUserDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerBargaining;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerSubscribe;
import com.innee.czyhInterface.entity.TCustomerTicket;
import com.innee.czyhInterface.entity.TEventBargaining;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TPoster;
import com.innee.czyhInterface.entity.TSceneUser;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.util.BufferedImageUtil;
import com.innee.czyhInterface.util.ConfigurationUtil;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.HeadImageUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.IpUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.BargainCountBean;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.redis.RedisMoudel;
import com.innee.czyhInterface.util.wx.AccessToken;
import com.innee.czyhInterface.util.wx.chenjar.WxChanjarMpUtil;
import com.innee.czyhInterface.util.wx.message.TemplateMessage;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import net.sf.ehcache.CacheManager;

/**
 * 微信相关接口
 * 
 * @author jinshengzhi
 *
 */
@Component
@Transactional
public class WxService {

	public static final int INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	public static final String ALGORITHM = "SHA-1";

	private static final Logger logger = LoggerFactory.getLogger(WxService.class);
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SceneUserDAO sceneUserDAO;

	@Autowired
	private PosterDAO posterDAO;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private CustomerTicketDAO customerTicketDAO;

	@Autowired
	private CustomerSubscribeDAO customerSubscribeDAO;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerBargainingDAO customerBargainingDAO;

	@Autowired
	private EventBargainingDAO eventBargainingDAO;
	
	@Autowired
	private RedisService redisService;

	public void entryptPassword(TCustomer customer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		customer.setFsalt(Encodes.encodeHex(salt));

		byte[] hashPassword = Digests.sha1(customer.getFpassword().getBytes(), salt, INTERATIONS);
		customer.setFpassword(Encodes.encodeHex(hashPassword));
	}

	public ResponseDTO getWxUserInfo(String code, HttpServletRequest request) throws WxErrorException {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		WxMpOAuth2AccessToken oauth2AccessToken = WxChanjarMpUtil.getInstance().getWxMpService()
				.oauth2getAccessToken(code);
		WxMpUser userInfo = WxChanjarMpUtil.getInstance().getWxMpService().oauth2getUserInfo(oauth2AccessToken,
				"zh_CN");
		WxMpUserDTO wxUser = new WxMpUserDTO();
		wxUser.setOpenid(userInfo.getOpenId());
		wxUser.setUnionid(userInfo.getUnionId());
		wxUser.setProvince(userInfo.getProvince());
		wxUser.setSex(userInfo.getSexId());
		wxUser.setClientType(1);
		wxUser.setCountry(userInfo.getCountry());
		wxUser.setNickname(userInfo.getNickname());
		wxUser.setHeadimgurl(userInfo.getHeadImgUrl());
		wxUser.setCity(userInfo.getCity());

		Date now = new Date();
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		String oldTicket = null;
		TCustomer tCustomer = customerDAO.getByFweixinUnionIdAndFtype(wxUser.getUnionid(), 1);
		if (tCustomer == null) {
			tCustomer = new TCustomer();
			tCustomer.setFname(wxUser.getNickname());
			tCustomer.setFpassword(PropertiesUtil.getProperty("customerInitPassword"));
			tCustomer.setFweixinId(wxUser.getOpenid());
			tCustomer.setFweixinUnionId(wxUser.getUnionid());
			tCustomer.setFweixinName(wxUser.getNickname());
			StringBuilder region = new StringBuilder();
			if (StringUtils.isNotBlank(wxUser.getCountry())) {
				region.append(wxUser.getCountry()).append(" - ");
			}
			if (StringUtils.isNotBlank(wxUser.getProvince())) {
				region.append(wxUser.getProvince()).append(" - ");
			}
			if (StringUtils.isNotBlank(wxUser.getCity())) {
				region.append(wxUser.getCity());
			}
			tCustomer.setFregion(region.toString());
			tCustomer.setFphoto(wxUser.getHeadimgurl());
			tCustomer.setFsex(wxUser.getSex());
			tCustomer.setFstatus(1);
			tCustomer.setFtype(1);
			tCustomer.setFcreateTime(now);
			tCustomer.setFupdateTime(now);
			customerService.entryptPassword(tCustomer);
			tCustomer.setFticket(ticket);
			tCustomer = customerDAO.save(tCustomer);

			// 创建用户TICKET表记录
			TCustomerTicket tCustomerTicket = new TCustomerTicket();
			tCustomerTicket.setFcreateTime(now);
			tCustomerTicket.setFupdateTime(now);
			tCustomerTicket.setFcustomerId(tCustomer.getId());
			tCustomerTicket.setFtype(1);
			tCustomerTicket.setFticket(ticket);
			customerTicketDAO.save(tCustomerTicket);

			TSceneUser tSceneUser = sceneUserDAO.findBysceneStrAndopenID(userInfo.getOpenId());
			// 新用户更新地推表
			if (tSceneUser != null) {
				sceneUserDAO.updateRegister(userInfo.getOpenId(), 1, now);
			} else {
				this.saveSceneUser("qrscene_00000000", userInfo.getOpenId(), now.getTime() / 1000L);
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
			if (StringUtils.isBlank(tCustomer.getFweixinId())) {
				customerDAO.saveWeixinId(wxUser.getOpenid(), tCustomer.getId());
			}
			// 如果客户的头像图片是微信头像，并且发送了变化，则更新客户的头像图片信息
			if (StringUtils.isNotBlank(tCustomer.getFphoto())
					&& tCustomer.getFphoto().startsWith("http://wx.qlogo.cn/mmopen")) {
				if (!tCustomer.getFphoto().equalsIgnoreCase(userInfo.getHeadImgUrl())) {
					customerDAO.savePhoto(userInfo.getHeadImgUrl(), tCustomer.getId());
				}
			}
			// 获取用户TICKET记录，如果有则更新ticket，如果没有则创建一条ticket记录
			TCustomerTicket tCustomerTicket = customerTicketDAO.getByFcustomerIdAndFtype(tCustomer.getId(), 1);
			if (tCustomerTicket == null) {
				tCustomerTicket = new TCustomerTicket();
				tCustomerTicket.setFcustomerId(tCustomer.getId());
				tCustomerTicket.setFtype(1);
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
			customerLogBean.setClientType(1);
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

		returnData.put("wxUserInfo", wxUser);

		returnData.put("userInfo", userDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO updateSceneUser(String openid, Long subscribeTime) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请输入openid！");
			return responseDTO;
		}

		TSceneUser tSceneUser = sceneUserDAO.findBysceneStrAndopenID(openid);
		if (tSceneUser == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("当前用户处于未关注状态");
			return responseDTO;
		}

		sceneUserDAO.setUnSubscribe(tSceneUser.getId(), 0, 1, new Date(subscribeTime * 1000L));
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO saveSceneUser(String sceneStr, String openid, Long subscribeTime) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(sceneStr)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请输入渠道码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请输入openid！");
			return responseDTO;
		}
		sceneStr = sceneStr.replaceAll(Constant.sceneStr, "");
		if (StringUtils.isBlank(sceneStr)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("请检查地推码参数");
			return responseDTO;
		}

		try {
			TSceneUser sceneUser = sceneUserDAO.findOneByOpenid(openid);
			if (sceneUser == null) {
				TSceneUser tSceneUser = new TSceneUser();
				tSceneUser.setFopenId(openid);
				tSceneUser.setFsceneStr(sceneStr);
				String str = sceneStr.substring(0, 1);
				if (NumberUtils.isDigits(str)) {
					tSceneUser.setFsceneId(Integer.parseInt(str));
				}
				tSceneUser.setFdelivery(0);

				// 判断TCustomer表中是否存在该用户注册记录如果存在则修改值
				TCustomer tCustomer = customerDAO.findOneByOpenId(openid);
				if (tCustomer == null) {
					tSceneUser.setFregister(0);
				} else {
					tSceneUser.setFregister(1);
					tSceneUser.setFregisterTime(tCustomer.getFcreateTime());
				}

				tSceneUser.setFsubscribeTime(new Date(subscribeTime * 1000L));
				tSceneUser.setFsubscribe(1);// 1.关注0.取消关注
				tSceneUser.setFunSubscribe(0);// 0.没取消关注 1.取消关注
				sceneUserDAO.save(tSceneUser);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO saveUnSubscribeSceneUser(String sceneStr, String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(sceneStr)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请输入渠道码！");
			return responseDTO;
		}
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请输入openid！");
			return responseDTO;
		}
		sceneStr = sceneStr.replaceAll(Constant.sceneStr, "");
		if (StringUtils.isBlank(sceneStr)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("请检查地推码参数");
			return responseDTO;
		}

		try {
			TSceneUser sceneUser = sceneUserDAO.findOneByOpenid(openid);
			if (sceneUser == null) {
				TSceneUser tSceneUser = new TSceneUser();
				tSceneUser.setFopenId(openid);
				tSceneUser.setFsceneStr(sceneStr);
				String str = sceneStr.substring(0, 2);
				if ("10".equals(str)) {
					tSceneUser.setFsceneId(1);
				} else if ("20".equals(str)) {
					tSceneUser.setFsceneId(2);
				} else {
					tSceneUser.setFsceneId(0);
				}
				tSceneUser.setFdelivery(0);

				// 判断TCustomer表中是否存在该用户注册记录如果存在则修改值
				TCustomer tCustomer = customerDAO.findOneByOpenId(openid);
				if (tCustomer == null) {
					tSceneUser.setFregister(0);
				} else {
					tSceneUser.setFregister(1);
					tSceneUser.setFregisterTime(tCustomer.getFcreateTime());
				}
				tSceneUser.setFsubscribe(0);// 1.关注0.取消关注
				tSceneUser.setFunSubscribe(0);// 0.没取消关注 1.取消关注
				sceneUserDAO.save(tSceneUser);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public ResponseDTO setSceneGPS(String openid, String gps) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(101);
			responseDTO.setMsg("请输入openID！");
			return responseDTO;
		}
		if (StringUtils.isBlank(gps)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("请输入GPS！");
			return responseDTO;
		}

		TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(openid);

		if (tSceneUser != null) {
			if (StringUtils.isBlank(tSceneUser.getFsceneGps())) {
				sceneUserDAO.saveGps(gps, tSceneUser.getId());
			}
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

	public SceneStrDTO getSceneStr(String sceneStr) {
		// SceneStrDTO sceneStrDTO = new SceneStrDTO(sceneStr, sceneID)
		int result = sceneStr.indexOf(Constant.sceneStr);
		if (result >= 0) {
			sceneStr = sceneStr.replaceAll(Constant.sceneStr, "");
			// 城市科学接的地推二维码场景是1
			return new SceneStrDTO(sceneStr, 1);
		} else {
			return null;
		}
	}

	/**
	 * 制作活动海报
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO doPoster(String qrcodeUrl, String headUrl, String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		StringBuilder httpUrl = null;
		if (StringUtils.isBlank(qrcodeUrl)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("qrcodeUrl参数不能为空，请检查qrcodeUrl的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(headUrl)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("headUrl参数不能为空，请检查headUrl的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		// 查询已经开始的海报活动
		TPoster tPoster = posterDAO.findPoster();
		String backGroundUrl = fxlService.getImageUrl(tPoster.getFimage(), false);
		// String backGroundUrl = "http://file.fangxuele.com/back.png";// 测试连接

		TCustomer tCustomer = customerDAO.findOneByOpenId(openid);

		try {

			InputStream inputStreamA = null;
			HttpURLConnection httpURLConnectionA = null;
			URL urlA = null;
			urlA = new URL(backGroundUrl);
			httpURLConnectionA = (HttpURLConnection) urlA.openConnection();
			httpURLConnectionA.setDoInput(true);

			inputStreamA = httpURLConnectionA.getInputStream();

			InputStream inputStreamB = null;
			HttpURLConnection httpURLConnectionB = null;
			URL urlB = null;
			urlB = new URL(qrcodeUrl);
			httpURLConnectionB = (HttpURLConnection) urlB.openConnection();
			httpURLConnectionB.setDoInput(true);

			inputStreamB = httpURLConnectionB.getInputStream();

			InputStream inputStreamC = null;
			HttpURLConnection httpURLConnectionC = null;
			URL urlC = null;
			urlC = new URL(headUrl);
			httpURLConnectionC = (HttpURLConnection) urlC.openConnection();
			httpURLConnectionC.setDoInput(true);

			inputStreamC = httpURLConnectionC.getInputStream();

			BufferedImage image = ImageIO.read(inputStreamA);
			BufferedImage image2 = ImageIO.read(inputStreamB);
			BufferedImage image3 = ImageIO.read(inputStreamC);

			BufferedImage resizeImage2 = BufferedImageUtil.resizeImage(image2, 186, 186);
			BufferedImage resizeImage3 = BufferedImageUtil.resizeImage(image3, 90, 90);

			Graphics g = image.getGraphics();
			// 二维码位置
			g.drawImage(resizeImage2, image.getWidth() - resizeImage2.getWidth() - 110, // x
					image.getHeight() - resizeImage2.getHeight() - 240, // y
					resizeImage2.getWidth(), // w
					resizeImage2.getHeight(), // h
					null);
			// 头像位置
			g.drawImage(resizeImage3, image.getWidth() - resizeImage3.getWidth() - 566,
					image.getHeight() - resizeImage3.getHeight() - 965, resizeImage3.getWidth(),
					resizeImage3.getHeight(), null);

			String filePathVar = PropertiesUtil.getProperty("posterCustomerPath");// personalCustomerLogoPath
			StringBuilder relativePath = new StringBuilder(filePathVar)
					.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).append("/");
			StringBuilder rootPath = new StringBuilder(Constant.RootPath)
					.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath);
			// 判断如果没有该目录则创建一个目录
			File destDir = new File(rootPath.toString());
			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			String storeFileName;
			if (tCustomer != null) {
				storeFileName = tCustomer.getId() + ".jpg";
			} else {
				storeFileName = Identities.uuid2() + ".jpg";
			}

			OutputStream outImage = new FileOutputStream(rootPath.append(storeFileName).toString());
			relativePath.append(storeFileName);

			httpUrl = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath.toString());
			// JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(outImage);
			// enc.encode(image);
			ImageIO.write(image, "jpeg", outImage);
			// imagein.close();
			// imagein2.close();
			outImage.close();

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		// 先用openid换取customerId 然后保存该用户海报跟二维码
		// TCustomer tCustomer = customerDAO.findOneByOpenId(openid);

		try {
			customerInfoDAO.updatePosterAndQr(tCustomer.getId(), httpUrl.toString(), qrcodeUrl);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("活动海报制作成功");
		returnData.put("httpPosterUrl", httpUrl);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * 判断用户渠道类型
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO getChannelType(String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}
		TSceneUser sceneUser = sceneUserDAO.findOneByOpenid(openid);

		if (StringUtils.isNotBlank(sceneUser.getFsceneStr())) {

			if (sceneUser.getFsceneStr().substring(0, 2).equals("10")) {
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				responseDTO.setMsg("当前用户为递推渠道");
				returnData.put("channelType", 1);
				responseDTO.setData(returnData);
				return responseDTO;
			} else if (sceneUser.getFsceneStr().substring(0, 2).equals("20")) {
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				responseDTO.setMsg("当前用户为物料渠道");
				returnData.put("channelType", 2);
				responseDTO.setData(returnData);
				return responseDTO;
			} else if (sceneUser.getFsceneStr().substring(0, 1).equals("9")) {
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				responseDTO.setMsg("当前用户为积分渠道");
				returnData.put("channelType", 9);
				responseDTO.setData(returnData);
				return responseDTO;
			}
		} else {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("当前用户无渠道");
			returnData.put("channelType", 999);
			responseDTO.setData(returnData);
			return responseDTO;
		}
		return responseDTO;
	}

	/**
	 * 判断是用户是否存在(积分场景专用)
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO getOpenIdToCustomerId(String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.findOneByOpenId(openid);

		TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(openid);

		if (tCustomer != null) {
			if (tSceneUser.getFsceneStr() != null) {
				// System.out.println(tCustomer.getId());
				List<TCustomerSubscribe> tCustomerSubscribeList = customerSubscribeDAO
						.getByOperationId(tCustomer.getId());
				TCustomer tCustomerOp = customerDAO.getOne(tCustomerSubscribeList.get(0).getFoperationId());// 被关注人的Id

				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				responseDTO.setMsg("用户存在且通过扫码进入");
				returnData.put("userStatus", 1);// 1.已经扫过吗的用户不能增加积分了 2.不存在
				returnData.put("subper", tCustomerOp.getFweixinId());// 已经支持过的人
				responseDTO.setData(returnData);
				return responseDTO;
			}
		} else {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("判断用户是否存在");
			returnData.put("userStatus", 2);// 1存在 2.不存在
			responseDTO.setData(returnData);
			return responseDTO;
		}
		return responseDTO;

	}

	/**
	 * 判断是用户是否存在()
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO getSceneUserExist(String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openId参数不能为空，请检查openId的传递参数值！");
			return responseDTO;
		}

		TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(openid);

		// if (tSceneUser.getFsceneStr() != null) {
		if (tSceneUser != null) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("当前用户已经存在递推表中");
			returnData.put("userStatus", 1);// 当前用户已经扫过地推码不能在领奖了
			responseDTO.setData(returnData);
			return responseDTO;

		} else {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("当前用户不存在递推表中");
			returnData.put("userStatus", 2);// 1存在 2.不存在
			responseDTO.setData(returnData);
			return responseDTO;
		}

	}

	/**
	 * 往递推表中增加一条记录并且判断用户是否注册过没有注册过 帮他注册
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO addBounsQrSubscribe(Integer clientType, String unionid, String openid, String nickname,
			String logoUrl, String country, String province, String city, Integer sex, String qrcode, String gps,
			Long subscribeTime, HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		Date now = new Date();
		String ticket = RandomStringUtils.randomAlphanumeric(16);
		// String oldTicket = null;
		TCustomer tCustomer = customerDAO.getByOpenIdAndUnId(unionid, openid, 1);
		if (tCustomer == null) {
			tCustomer = new TCustomer();
			tCustomer.setFname(nickname);
			tCustomer.setFpassword(PropertiesUtil.getProperty("customerInitPassword"));
			tCustomer.setFweixinId(openid);
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
			tCustomer.setFphoto(logoUrl);
			tCustomer.setFsex(sex);
			tCustomer.setFstatus(1);
			tCustomer.setFtype(1);
			tCustomer.setFcreateTime(now);
			tCustomer.setFupdateTime(now);
			entryptPassword(tCustomer);
			tCustomer = customerDAO.save(tCustomer);

			try {
				// 创建用户TICKET表记录
				TCustomerTicket tCustomerTicket = new TCustomerTicket();
				tCustomerTicket.setFcreateTime(now);
				tCustomerTicket.setFupdateTime(now);
				tCustomerTicket.setFcustomerId(tCustomer.getId());
				tCustomerTicket.setFtype(clientType);
				tCustomerTicket.setFticket(ticket);
				customerTicketDAO.save(tCustomerTicket);

				// 创建用户附加信息
				TCustomerInfo tCustomerInfo = new TCustomerInfo();
				tCustomerInfo.setFcustomerId(tCustomer.getId());
				tCustomerInfo.setFregisterChannel("bouns");
				// tCustomerInfo.setFregisterChannelVersion("");
				tCustomerInfo.setForderNumber(0);
				tCustomerInfo.setFpayOrderNumber(0);
				tCustomerInfo.setFpayZeroOrderNumber(0);
				tCustomerInfo.setFrefundOrderNumber(0);
				tCustomerInfo.setForderTotal(BigDecimal.ZERO);
				tCustomerInfo.setFregisterTime(now);
				tCustomerInfo.setFpoint(0);
				tCustomerInfo.setFtipNumber(0);
				tCustomerInfo.setFusedPoint(0);
				tCustomerInfo.setFcreateTime(now);
				tCustomerInfo.setFupdateTime(now);

				customerInfoDAO.save(tCustomerInfo);

				TSceneUser tSceneUser = sceneUserDAO.findOneByOpenid(openid);
				// 扫码领积分的用户增加地推表信息
				if (tSceneUser == null) {
					TSceneUser tSceneUserAdd = new TSceneUser();

					// System.out.println(qrcode + "二维码是多少");
					tSceneUserAdd.setFsceneId(9);// 9为积分领取场景
					if (StringUtils.isNotBlank(qrcode)) {
						tSceneUserAdd.setFsceneStr(qrcode);
					}
					tSceneUserAdd.setFopenId(openid);
					if (StringUtils.isNotBlank(gps)) {
						tSceneUserAdd.setFsceneGps(gps);
					}
					tSceneUserAdd.setFsubscribe(1);// 0.不关注 1.关注
					tSceneUserAdd.setFunSubscribe(0);// 0.没取消关注 1.取消关注
					tSceneUserAdd.setFdelivery(0);
					tSceneUserAdd.setFregister(1);
					tSceneUserAdd.setFregisterTime(now);
					tSceneUserAdd.setFsubscribeTime(new Date(subscribeTime * 1000L));
					sceneUserDAO.save(tSceneUserAdd);
				}
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(0);
				responseDTO.setMsg("添加失败");
			}

		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("添加成功");
		return responseDTO;

	}

	/**
	 * 修改用户附加信息
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO updateUserInfoWxPoster(String openid, String fpointCode, Date finvalidTime) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openId参数不能为空，请检查openId的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.findOneByOpenId(openid);

		if (tCustomer != null) {
			customerInfoDAO.updatePosterInfo(tCustomer.getId(), fpointCode, finvalidTime);

		}
		// String date =
		// DateFormatUtils.format(DateUtils.addDays(new Date(),
		// 30), "yyyy-MM-dd HH:mm");
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		// responseDTO.setMsg("");
		return responseDTO;

	}

	/**
	 * 获取用户附加信息
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO getUserInfoWxPoster(String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();

		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.findOneByOpenId(openid);

		if (tCustomer != null) {
			TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(tCustomer.getId());
			if (tCustomerInfo.getFinvalidTime() == null && tCustomerInfo.getFposterImage() == null) {
				returnData.put("invalidTime", 1);// 如果为空的话就回1代表
				returnData.put("posterImage", 1);
			} else {
				returnData.put("invalidTime", tCustomerInfo.getFinvalidTime());
				returnData.put("posterImage", tCustomerInfo.getFposterImage());
			}

		}
		// DateFormatUtils.format(tCustomerInfo.getFinvalidTime(),"yyyy-MM-dd
		// HH:mm")
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		// responseDTO.setMsg("");
		responseDTO.setData(returnData);
		return responseDTO;

	}

	/**
	 * 获取用户附加信息表场景码当前最大值
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO getMAXPointCode() {
		ResponseDTO responseDTO = new ResponseDTO();

		String pointCodeNum = customerInfoDAO.getMaxPointCode();
		if (pointCodeNum != null) {
			Map<String, Object> returnData = Maps.newHashMap();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("");
			returnData.put("maxPointCode", pointCodeNum);
			responseDTO.setData(returnData);
		} else {
			Map<String, Object> returnData = Maps.newHashMap();
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("");
			returnData.put("maxPointCode", 90000000);
			responseDTO.setData(returnData);
		}
		return responseDTO;
	}

	/**
	 * 获取海报关注人
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @return
	 */
	public ResponseDTO getPosterSUser(String openid) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		TCustomer tCustomer = customerDAO.findOneByOpenId(openid);

		List<TCustomerSubscribe> tCustomerSubscribeList = customerSubscribeDAO.getByOperationId(tCustomer.getId());

		TCustomer tCustomerOp = customerDAO.getOne(tCustomerSubscribeList.get(0).getFoperationId());// 被关注人的Id

		if (tCustomerOp != null) {
			responseDTO.setSuccess(true);
			responseDTO.setStatusCode(0);
			responseDTO.setMsg("返回被关注用户openid");
			returnData.put("userOpenid", tCustomerOp.getFweixinId());
			responseDTO.setData(returnData);
			return responseDTO;
		}
		return responseDTO;
	}

	/**
	 * 发送支付成功微信模板消息
	 * 
	 * @param tOrder
	 *            订单对象
	 * @param openId
	 *            用户的openId
	 */
	public void pushPayMentSuccessfulWeChatTemplateMsg(TOrder tOrder, String openId) {
		try {
			if (StringUtils.isNotBlank(openId)) {
				String orderDetailUrl = new StringBuilder().append(Constant.orderDetail).append(tOrder.getId())
						.toString();
				String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
						.append(TemplateMessage.TEMPLATEMSG_TYPE_PAYMENTSUCCESSFUL).toString();
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("openid", openId);
				paramsMap.put("url", orderDetailUrl);
				paramsMap.put("first", Constant.paymentSuccessfulFirst);
				paramsMap.put("remark", Constant.paymentSuccessfulRemark);
				paramsMap.put("orderMoneySum", tOrder.getFtotal());
				paramsMap.put("orderProductName", tOrder.getFeventTitle());
				paramsMap.put("orderName", tOrder.getForderNum());
				HttpClientUtil.callUrlPost(postUrl, paramsMap);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送支付成功微信模板消息时，支付成功微信模板消息接口出错。");
		}
	}

	/**
	 * 发送订单评价提醒模板消息
	 * 
	 * @param tOrder
	 *            订单对象
	 * @param openId
	 *            用户的openId
	 */
	public void pushOrderEvaluationMsg(TOrder tOrder, String openId) {
		try {
			if (StringUtils.isNotBlank(openId)) {
				String orderDetailUrl = new StringBuilder().append(Constant.orderStatusUrl).append(60).toString();
				String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
						.append(TemplateMessage.TEMPLATEMSG_TYPE_ORDEREVALUATION).toString();
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("openid", openId);
				paramsMap.put("url", orderDetailUrl);
				paramsMap.put("first", Constant.orderEvaluationFirst);
				paramsMap.put("remark", Constant.orderEvaluationRemark);
				paramsMap.put("keyword1", tOrder.getForderNum());
				paramsMap.put("keyword2", tOrder.getFverificationTime());
				HttpClientUtil.callUrlPost(postUrl, paramsMap);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送订单评价提醒模板消息时，订单评价提醒模板消息接口出错。");
		}
	}

	/**
	 * 发送订单未支付通知模板消息
	 * 
	 * @param tOrder
	 *            订单对象
	 * @param openId
	 *            用户的openId
	 */
	public void pushUnpaidOrderMsg(TOrder tOrder, String openId) {
		try {
			if (StringUtils.isNotBlank(openId)) {
				String orderDetailUrl = new StringBuilder().append(Constant.orderStatusUrl).append(10).toString();
				String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
						.append(TemplateMessage.TEMPLATEMSG_TYPE_UNPAIDORDER).toString();
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("openid", openId);
				paramsMap.put("msgType", TemplateMessage.TEMPLATEMSG_TYPE_UNPAIDORDER);
				paramsMap.put("url", orderDetailUrl);
				paramsMap.put("first", Constant.unpaidOrderFirst);
				paramsMap.put("remark", Constant.unpaidOrderRemark);
				paramsMap.put("ordertape", DateFormatUtils.format(tOrder.getFcreateTime(), "yyyy年MM月dd日 HH:mm"));
				paramsMap.put("ordeID", tOrder.getForderNum());
				HttpClientUtil.callUrlPost(postUrl, paramsMap);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送订单未支付通知模板消息时，订单未支付通知模板消息接口出错。");
		}
	}

	/**
	 * 发送退款申请通知模板消息
	 * 
	 * @param tOrder
	 *            订单对象
	 * @param openId
	 *            用户的openId
	 */
	public void pushRefundMsg(TOrder tOrder, CustomerDTO customerDTO) {
		try {
			String orderPushWeiXin = ConfigurationUtil
					.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_REFUNDAPPLICATION);
			if (StringUtils.isNotBlank(orderPushWeiXin) && orderPushWeiXin.equals("1")) {
				if (StringUtils.isNotBlank(customerDTO.getWxId())) {
					String orderDetailUrl = new StringBuilder().append(Constant.cancelOrderUrl).toString();
					String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
							.append(TemplateMessage.TEMPLATEMSG_TYPE_REFUNDAPPLICATION).toString();
					Map<String, Object> paramsMap = Maps.newHashMap();
					paramsMap.put("openid", customerDTO.getWxId());
					paramsMap.put("url", orderDetailUrl);
					paramsMap.put("first", Constant.refundApplicationFirst);
					paramsMap.put("remark", Constant.refundApplicationRemark);
					paramsMap.put("refundMoneySum", tOrder.getFtotal());
					paramsMap.put("refundProductName", tOrder.getFeventTitle());
					paramsMap.put("refundName", tOrder.getForderNum());
					HttpClientUtil.callUrlPost(postUrl, paramsMap);
				}
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送订单评价提醒模板消息时，订单评价提醒模板消息接口出错。");
		}
	}

	public void pushTimeOutMsg(TEventBargaining tEventBargaining) {
		try {
			String bargainingPushWeiXin = ConfigurationUtil
					.getPropertiesValue(ResponseConfigurationDict.RESPONSE_PROPERTIES_BARGAINTIMEOUT);
			if (StringUtils.isNotBlank(bargainingPushWeiXin) && bargainingPushWeiXin.equals("1")) {

				List<TCustomerBargaining> tCustomerBargainingList = customerBargainingDAO
						.getBarList(tEventBargaining.getId());

				for (TCustomerBargaining tCustomerBargaining : tCustomerBargainingList) {

					if (StringUtils.isNotBlank(tCustomerBargaining.getFcustomerId())) {
						TCustomer customer = customerDAO.findOne(tCustomerBargaining.getFcustomerId());
						String bargaininglUrl = new StringBuilder().append(Constant.bargaininglUrl)
								.append("eventBargainingId=").append(tEventBargaining.getId())
								.append("&customerBargainingId=").append(tCustomerBargaining.getId()).toString();
						String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
								.append(TemplateMessage.TEMPLATEMSG_TYPE_BARGAININGEND).toString();
						Map<String, Object> paramsMap = Maps.newHashMap();
						paramsMap.put("openid", customer.getFweixinId());
						paramsMap.put("url", bargaininglUrl);
						paramsMap.put("first", Constant.barTimeOutRemark);
						paramsMap.put("remark", Constant.barTimeOutFirst);
						paramsMap.put("keyword1", tEventBargaining.getFtitle());
						paramsMap.put("keyword2",
								DateFormatUtils.format(tEventBargaining.getFendTime(), "yyyy-MM-dd HH:mm"));
						paramsMap.put("keyword3", "活动结束后就无法再下单了，所有砍到的价格逾期作废。赶快去支付吧~");
						HttpClientUtil.callUrlPost(postUrl, paramsMap);
					}

				}
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送砍一砍活动快结束时发生错误。");
		}
	}

	public void pushremainCountMsg(BargainCountBean bargainCountBean) {
		try {
			List<TCustomerBargaining> tCustomerBargainingList = customerBargainingDAO
					.getBarList(bargainCountBean.getEventBargainId());

			for (TCustomerBargaining tCustomerBargaining : tCustomerBargainingList) {

				if (StringUtils.isNotBlank(tCustomerBargaining.getFcustomerId())) {
					TCustomer customer = customerDAO.findOne(tCustomerBargaining.getFcustomerId());
					String bargaininglUrl = new StringBuilder().append(Constant.bargaininglUrl)
							.append("eventBargainingId=").append(bargainCountBean.getEventBargainId())
							.append("&customerBargainingId=").append(tCustomerBargaining.getId()).toString();
					String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
							.append(TemplateMessage.TEMPLATEMSG_TYPE_BARGAININGREMAINCOUNT).toString();
					Map<String, Object> paramsMap = Maps.newHashMap();
					paramsMap.put("openid", customer.getFweixinId());
					paramsMap.put("url", bargaininglUrl);
					paramsMap.put("first", Constant.barRemainCountFirst);
					paramsMap.put("remark", Constant.barRemainCountRemark);
					paramsMap.put("keyword1", bargainCountBean.getEventCode());
					paramsMap.put("keyword2", bargainCountBean.getEventTitle());
					paramsMap.put("keyword3", bargainCountBean.getRemainingStock());
					HttpClientUtil.callUrlPost(postUrl, paramsMap);
				}

			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送砍一砍活动库存不足发生错误。");
		}
	}

	public void pushJoinBargainMsg(TCustomerBargaining tCustomerBargaining) {
		try {
			if (StringUtils.isNotBlank(tCustomerBargaining.getFcustomerId())) {
				TCustomer customer = customerDAO.findOne(tCustomerBargaining.getFcustomerId());
				TEventBargaining tEventBargaining = eventBargainingDAO.findOne(tCustomerBargaining.getFbargainingId());
				String bargaininglUrl = new StringBuilder().append(Constant.bargaininglUrl).append("eventBargainingId=")
						.append(tCustomerBargaining.getFbargainingId()).append("&customerBargainingId=")
						.append(tCustomerBargaining.getId()).toString();
				String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
						.append(TemplateMessage.TEMPLATEMSG_TYPE_BARGAININGJOIN).toString();
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("openid", customer.getFweixinId());
				paramsMap.put("url", bargaininglUrl);
				paramsMap.put("first", Constant.bargainJoinFirst);
				paramsMap.put("remark", Constant.bargainJoinRemark);
				paramsMap.put("keyword1", tEventBargaining.getFtitle());
				paramsMap.put("keyword2",
						DateFormatUtils.format(tCustomerBargaining.getFcreateTime(), "yyyy-MM-dd HH:mm"));
				HttpClientUtil.callUrlPost(postUrl, paramsMap);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			logger.error("发送砍一砍活动库存不足发生错误。");
		}
	}

	/**
	 * 发送订单评价返积分提醒模板消息
	 * 
	 * @param tOrder
	 *            订单对象
	 * @param openId
	 *            用户的openId
	 */
	public void pushOrderEvaluationBonusMsg(TOrder tOrder, String openId) {
		try {
			if (StringUtils.isNotBlank(openId)) {
				String orderDetailUrl = new StringBuilder().append(Constant.orderStatusUrl).append(60).toString();
				String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
						.append(TemplateMessage.TEMPLATEMSG_TYPE_COMPLIMENTARYPOINTS).toString();
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("openid", openId);
				paramsMap.put("url", orderDetailUrl);
				paramsMap.put("first", Constant.orderEvaluationBonusFirst);
				paramsMap.put("remark", Constant.orderEvaluationBonusRemark);
				paramsMap.put("keyword1", tOrder.getForderNum());
				paramsMap.put("keyword2", new StringBuilder().append(tOrder.getFverificationTime()).append("/n")
						.append(tOrder.getTEvent().getFtitle()));
				HttpClientUtil.callUrlPost(postUrl, paramsMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送订单评价返积分提醒模板消息时，订单评价返积分提醒模板消息接口出错。");
		}
	}

	/**
	 * 发送订单评价返先提醒模板消息
	 * 
	 * @param tOrder
	 *            订单对象
	 * @param openId
	 *            用户的openId
	 */
	public void pushOrderCashBackMsg(TOrder tOrder, String openId) {
		try {
			if (StringUtils.isNotBlank(openId)) {
				String orderDetailUrl = new StringBuilder().append(Constant.orderStatusUrl).append(60).toString();
				String postUrl = new StringBuilder().append(Constant.weChatPushUrl).append("/")
						.append(TemplateMessage.TEMPLATEMSG_TYPE_CASHBACK).toString();
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("openid", openId);
				paramsMap.put("url", orderDetailUrl);
				paramsMap.put("first", Constant.orderCashBackFirst);
				paramsMap.put("remark", Constant.orderCashBackRemark);
				paramsMap.put("keyword1", tOrder.getForderNum());
				paramsMap.put("keyword2", new StringBuilder().append(tOrder.getFverificationTime()).append("/n")
						.append(tOrder.getTEvent().getFtitle()));
				HttpClientUtil.callUrlPost(postUrl, paramsMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("发送订单评价返现金提醒模板消息时，订单评价返现金提醒模板消息接口出错。");
		}
	}

	public static void main(String[] args) {
		String sceneStr = "qrscene_201001";
		sceneStr = sceneStr.replaceAll(Constant.sceneStr, "");
		System.out.println(sceneStr.substring(0, 2));
	}
	
	public String getAccessToken(){
		String s = "";
		StringBuilder url = new StringBuilder().append("https://api.weixin.qq.com/cgi-bin/token");
		Map<String, Object> map = Maps.newHashMap();
		map.put("grant_type", "client_credential");
		map.put("appid", PropertiesUtil.getProperty("appId"));
		map.put("secret", PropertiesUtil.getProperty("secret"));
		try {
			s = HttpClientUtil.callUrlGet(url.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AccessToken accessToken = new AccessToken();
		accessToken = mapper.fromJson(s, AccessToken.class);
		return accessToken.getAccess_token();
	}
	
	public String getMedia(String accessToken,String media_id){
		String requestUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace(
				"MEDIA_ID", media_id);
		return requestUrl;
		
	}
	
}