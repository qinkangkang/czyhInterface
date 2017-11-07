package com.innee.czyhInterface.service.v2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.Identities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerWithdrawalDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerWithdrawal;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.util.BufferedImageUtil;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.bufferedImage.QRCodeUtil;
import com.innee.czyhInterface.util.wx.message.WxInterfaceApi;

@Component
@Transactional
public class DistributionService {

	private static final Logger logger = LoggerFactory.getLogger(DistributionService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private final static String FontType = "微软雅黑";

	private final static String FontColorOne = "#80210d";

	private final static String FontColorTow = "#333333";

	private final static String FontColorThree = "#ee0101";

	private final static String BackColorOne = "#FFFFE3";

	private final static String BackColorTow = "#FFEFB2";

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CustomerWithdrawalDAO customerWithdrawalDAO;

	@Autowired
	private CommonService commonService;

	@Autowired
	private WxService wxService;

	/**
	 * 制作分享返利的海报页面
	 * 
	 * @param qrcodeUrl
	 * @param headUrl
	 * @param openid
	 * @return
	 */
	public ResponseDTO getDistributionImage(String openid, String eventImage, String eventId, String eventTitle,
			String eventPrice, String eventfocus, String customerId) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		StringBuilder httpUrl = null;
		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventImage)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("eventImage参数不能为空，请检查eventImage的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(206);
			responseDTO.setMsg("eventId参数不能为空，请检查eventId的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventTitle)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(207);
			responseDTO.setMsg("eventTitle参数不能为空，请检查eventTitle的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventPrice)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(208);
			responseDTO.setMsg("eventPrice参数不能为空，请检查eventPrice的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(eventfocus)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(209);
			responseDTO.setMsg("eventfocus参数不能为空，请检查eventfocus的传递参数值！");
			return responseDTO;
		}
		String backGroundUrl = PropertiesUtil.getProperty("distributionbackGroundUrl");
		TCustomer tCustomer = customerDAO.findOneByOpenId(openid);
		try {

			// 第一张为背景图片
			InputStream inputStreamA = null;
			HttpURLConnection httpURLConnectionA = null;
			URL urlA = null;
			urlA = new URL(backGroundUrl);
			httpURLConnectionA = (HttpURLConnection) urlA.openConnection();
			httpURLConnectionA.setDoInput(true);

			inputStreamA = httpURLConnectionA.getInputStream();

			// 第二张生成二维码图片
			StringBuilder str = new StringBuilder();
//			str.append(Constant.H5EventUrl).append(eventId).append("&distributionerId=").append(customerId);
			BufferedImage image2 = QRCodeUtil.createImage(str.toString(), true);

			// 第三张读取活动主图
			InputStream inputStreamC = null;
			HttpURLConnection httpURLConnectionC = null;
			URL urlC = null;
			// String eventImage =
			// "http://fileevent.fangxuele.com/eventImage/2016-07-13%2014:06/03e4b664cc8344989fe3fb2b26e64c59.jpg";
			urlC = new URL(eventImage);
			httpURLConnectionC = (HttpURLConnection) urlC.openConnection();
			httpURLConnectionC.setDoInput(true);

			inputStreamC = httpURLConnectionC.getInputStream();

			BufferedImage image = ImageIO.read(inputStreamA);
			BufferedImage image3 = ImageIO.read(inputStreamC);

			// 活动亮点
			// String eventfocus = "99元一大一小，过山车，碰碰车，选旋转木马，所有项目通玩。";
			BufferedImage image4 = null;
			BufferedImage image41 = null;

			if (eventfocus.length() > 30) {
				eventfocus = eventfocus.substring(0, 30);
				if (eventfocus.length() > 19) {
					image4 = this.createImage(eventfocus.substring(0, 19), new Font(FontType, Font.PLAIN, 26), 490, 60,
							FontColorOne, BackColorOne);
					StringBuilder strliangdian = new StringBuilder();
					strliangdian.append(eventfocus.substring(19, eventfocus.length())).append("...");
					image41 = this.createImage(strliangdian.toString(), new Font(FontType, Font.PLAIN, 26), 490, 60,
							FontColorOne, BackColorOne);
				} else {
					image4 = this.createImage(eventfocus, new Font(FontType, Font.PLAIN, 26), 490, 60, FontColorOne,
							BackColorOne);
				}

			} else {
				if (eventfocus.length() > 19) {
					image4 = this.createImage(eventfocus.substring(0, 19), new Font(FontType, Font.PLAIN, 26), 490, 60,
							FontColorOne, BackColorOne);
					image41 = this.createImage(eventfocus.substring(19, eventfocus.length()),
							new Font(FontType, Font.PLAIN, 26), 490, 60, FontColorOne, BackColorOne);
				} else {
					image4 = this.createImage(eventfocus, new Font(FontType, Font.PLAIN, 26), 490, 60, FontColorOne,
							BackColorOne);
				}
			}

			// 活动标题
			// String eventTitle = "蟹岛熊孩子乐园门票200张，28个项目能玩水，火爆人气。";
			BufferedImage image5 = null;
			BufferedImage image51 = null;
			if (eventTitle.length() > 30) {
				eventTitle = eventTitle.substring(0, 30);
				if (eventTitle.length() > 21) {
					image5 = this.createImage(eventTitle.substring(0, 21), new Font(FontType, Font.PLAIN, 32), 625, 60,
							FontColorTow, BackColorTow);
					StringBuilder strtitle = new StringBuilder();
					strtitle.append(eventTitle.substring(21, eventTitle.length())).append("...");
					image51 = this.createImage(eventTitle.toString(), new Font(FontType, Font.PLAIN, 32), 625, 45,
							FontColorTow, BackColorTow);
				} else {
					image5 = this.createImage(eventTitle, new Font(FontType, Font.PLAIN, 32), 625, 60, FontColorTow,
							BackColorTow);
				}
			} else {
				if (eventTitle.length() > 21) {
					image5 = this.createImage(eventTitle.substring(0, 21), new Font(FontType, Font.PLAIN, 32), 625, 60,
							FontColorTow, BackColorTow);
					image51 = this.createImage(eventTitle.substring(21, eventTitle.length()),
							new Font(FontType, Font.PLAIN, 32), 625, 45, FontColorTow, BackColorTow);
				} else {
					image5 = this.createImage(eventTitle, new Font(FontType, Font.PLAIN, 32), 625, 60, FontColorTow,
							BackColorTow);
				}
			}
			// 活动价格
			BufferedImage image6 = this.createImage(eventPrice, new Font(FontType, Font.PLAIN, 38), 330, 45,
					FontColorThree, BackColorTow);
			// BufferedImage image61 = this.createImage("起", new Font(FontType,
			// Font.PLAIN, 26), 35, 38, FontColorThree,
			// BackColorTow);
			BufferedImage resizeImage41 = null;
			BufferedImage resizeImage2 = BufferedImageUtil.resizeImage(image2, 134, 134);
			BufferedImage resizeImage3 = BufferedImageUtil.resizeImage(image3, 670, 407);
			BufferedImage resizeImage4 = BufferedImageUtil.resizeImage(image4, 485, 60);
			if (image41 != null) {
				resizeImage41 = BufferedImageUtil.resizeImage(image41, 485, 60);
			}
			BufferedImage resizeImage5 = BufferedImageUtil.resizeImage(image5, 625, 60);

			Graphics g = image.getGraphics();

			// 二维码位置
			g.drawImage(resizeImage2, image.getWidth() - resizeImage2.getWidth() - 36,
					// x
					image.getHeight() - resizeImage2.getHeight() - 50, // y
					resizeImage2.getWidth(), // w
					resizeImage2.getHeight(), // h
					null);

			// 货品的主图
			g.drawImage(resizeImage3, image.getWidth() - resizeImage3.getWidth() - 20,
					image.getHeight() - resizeImage3.getHeight() - 377, resizeImage3.getWidth(),
					resizeImage3.getHeight(), null);

			// 货品标题
			g.drawImage(resizeImage5, image.getWidth() - resizeImage5.getWidth() - 48,
					image.getHeight() - resizeImage5.getHeight() - 300, resizeImage5.getWidth(),
					resizeImage5.getHeight(), null);
			if (image51 != null) {
				g.drawImage(image51, image.getWidth() - image51.getWidth() - 48,
						image.getHeight() - image51.getHeight() - 275, image51.getWidth(), image51.getHeight(), null);
			}
			// 货品价格
			g.drawImage(image6, image.getWidth() - image6.getWidth() - 350,
					image.getHeight() - image6.getHeight() - 230, image6.getWidth(), image6.getHeight(), null);
			// g.drawImage(image61, image.getWidth() - image61.getWidth() - 560,
			// image.getHeight() - image61.getHeight() - 225,
			// image61.getWidth(), image61.getHeight(), null);

			// 货品亮点
			g.drawImage(resizeImage4, image.getWidth() - resizeImage4.getWidth() - 165,
					image.getHeight() - resizeImage4.getHeight() - 84, resizeImage4.getWidth(),
					resizeImage4.getHeight(), null);
			if (resizeImage41 != null) {
				g.drawImage(resizeImage41, image.getWidth() - resizeImage41.getWidth() - 165,
						image.getHeight() - resizeImage41.getHeight() - 50, resizeImage41.getWidth(),
						resizeImage41.getHeight(), null);
			}

			// 储存图片
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
				storeFileName = tCustomer.getId() + ".png";
			} else {
				storeFileName = Identities.uuid2() + ".png";
			}

			OutputStream outImage = new FileOutputStream(rootPath.append(storeFileName).toString());
			relativePath.append(storeFileName);

			httpUrl = new StringBuilder(PropertiesUtil.getProperty("fileServerUrl"))
					.append(PropertiesUtil.getProperty("imageRootPath")).append(relativePath.toString());
			ImageIO.write(image, "png", outImage);
			outImage.close();

		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("制作分享返利海报成功");
		returnData.put("sharePosterUrl", httpUrl);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public BufferedImage createImage(String str, Font font, Integer width, Integer height, String fountColor,
			String backColor) throws Exception {
		// 创建图片
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.setClip(0, 0, width, height);
		g.setColor(Color.decode(backColor));// 背景颜色
		g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
		g.setColor(Color.decode(fountColor));// 字体颜色
		g.setFont(font);// 设置画笔字体
		g.drawString(str, 0, font.getSize());// 画出字符串
		// 用于获得垂直居中 y
		// Rectangle clip = g.getClipBounds();
		// FontMetrics fm = g.getFontMetrics(font);
		// int ascent = fm.getAscent();
		// int descent = fm.getDescent();
		// int y = (clip.height - (ascent + descent)) / 2 + ascent;
		// for (int i = 0; i < 6; i++) {// 256 340 0 680
		// g.drawString(str, i * 680, y);// 画出字符串
		// }
		g.dispose();
		return image;
	}

	public ResponseDTO getSharePosterImage(String openid, String headUrl, String customerId) {

		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(openid)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("openid参数不能为空，请检查openid的传递参数值！");
			return responseDTO;
		}

		if (StringUtils.isBlank(headUrl)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("headUrl参数不能为空，请检查headUrl的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(customerId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(204);
			responseDTO.setMsg("customerId参数不能为空，请检查customerId的传递参数值！");
			return responseDTO;
		}

		Map<String, Object> returnData = Maps.newHashMap();
		TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(customerId);

		if (tCustomerInfo == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(205);
			responseDTO.setMsg("当前用户id有问题请检查！");
			return responseDTO;
		}
		if (tCustomerInfo.getFpointCode() == null) {

			try {
				String pointCodeNum = customerInfoDAO.getMaxPointCode();
				String codeNum = null;
				if (pointCodeNum != null) {
					codeNum = pointCodeNum;
				}
				int qrcodeNum;
				qrcodeNum = Integer.parseInt(codeNum) + 1;
				// 生成带参数的二维码 (2592000)
				StringBuilder qrCodeUrl = new StringBuilder();
				qrCodeUrl.append(PropertiesUtil.getProperty(WxInterfaceApi.WECHATURL));
				Map<String, Object> paramsMap = Maps.newHashMap();
				paramsMap.put("scene", qrcodeNum);
				qrCodeUrl.append(WxInterfaceApi.QRCODEWXAPI).append(WxInterfaceApi.QRTMPWXAPI);
				String resQrcode = HttpClientUtil.callUrlPost(qrCodeUrl.toString(), paramsMap);
				ResponseDTO res = mapper.fromJson(resQrcode, ResponseDTO.class);

				// String qrcodeurl =
				// "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGJ8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnczyhInterfacemNvbS9xL2xEajZZalBrSXRSYmtRZExPeFl4AAIEELrsVwMEAI0nAA%3D%3D";

				ResponseDTO resa = wxService.doPoster(res.getData().get("TmpQrCodeUrl").toString(), headUrl, openid);
				returnData.put("posterUrl", resa.getData().get("httpPosterUrl").toString());

				customerInfoDAO.updatePosterInfo(customerId, String.valueOf(qrcodeNum), new Date());
			} catch (Exception e) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				responseDTO.setSuccess(true);
				responseDTO.setStatusCode(206);
				responseDTO.setMsg("生成用户专属海错误");
				responseDTO.setData(returnData);
				return responseDTO;
			}
		} else {

			if (DateUtils.truncatedCompareTo(new Date(), tCustomerInfo.getFinvalidTime(), Calendar.SECOND) > 0) {
				try {
					// 失效重新生成
					// 生成带参数的二维码 (2592000)
					StringBuilder qrCodeUrl = new StringBuilder();
					qrCodeUrl.append(PropertiesUtil.getProperty(WxInterfaceApi.WECHATURL));
					Map<String, Object> paramsMap = Maps.newHashMap();
					paramsMap.put("scene", tCustomerInfo.getFpointCode());
					qrCodeUrl.append(WxInterfaceApi.QRCODEWXAPI).append(WxInterfaceApi.QRTMPWXAPI);
					String resQrcode = HttpClientUtil.callUrlPost(qrCodeUrl.toString(), paramsMap);
					ResponseDTO res = mapper.fromJson(resQrcode, ResponseDTO.class);

					ResponseDTO resa = wxService.doPoster(res.getData().get("TmpQrCodeUrl").toString(), headUrl,
							openid);
					returnData.put("posterUrl", resa.getData().get("httpPosterUrl").toString());
					customerInfoDAO.updatePosterInfo(customerId, tCustomerInfo.getFpointCode(), new Date());

				} catch (Exception e) {
					logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
					responseDTO.setSuccess(true);
					responseDTO.setStatusCode(206);
					responseDTO.setMsg("失效生成用户专属海错误");
					responseDTO.setData(returnData);
					return responseDTO;
				}

			} else {
				// 未失效取接口连接
				returnData.put("posterUrl", tCustomerInfo.getFposterImage());
				// try {
				// StringBuilder qrCodeUrl = new StringBuilder();
				// qrCodeUrl.append(PropertiesUtil.getProperty(WxInterfaceApi.WECHATURL));
				// Map<String, Object> paramsMap = Maps.newHashMap();
				// if (tCustomerInfo.getFpointCode() != null) {
				// paramsMap.put("scene", tCustomerInfo.getFpointCode());
				// } else {
				// String pointCodeNum = customerInfoDAO.getMaxPointCode();
				// String codeNum = null;
				// if (pointCodeNum != null) {
				// codeNum = pointCodeNum;
				// }
				// int qrcodeNum;
				// qrcodeNum = Integer.parseInt(codeNum) + 1;
				// paramsMap.put("scene", qrcodeNum);
				// }
				// qrCodeUrl.append(WxInterfaceApi.QRCODEWXAPI).append(WxInterfaceApi.QRTMPWXAPI);
				// String resQrcode =
				// HttpClientUtil.callUrlPost(qrCodeUrl.toString(), paramsMap);
				// ResponseDTO res = mapper.fromJson(resQrcode,
				// ResponseDTO.class);
				//
				// ResponseDTO resa =
				// wxService.doPoster(res.getData().get("TmpQrCodeUrl").toString(),
				// headUrl,
				// openid);
				// returnData.put("posterUrl",
				// resa.getData().get("httpPosterUrl").toString());
				// customerInfoDAO.updatePosterInfo(customerId,
				// tCustomerInfo.getFpointCode(), new Date());

				// } catch (Exception e) {
				// e.printStackTrace();
				//
				// responseDTO.setSuccess(true);
				// responseDTO.setStatusCode(205);
				// responseDTO.setMsg("失效生成用户专属海错误");
				// responseDTO.setData(returnData);
				// return responseDTO;
				// }

			}

		}
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("生成用户专属海报成功");
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getMyReward(Integer clientType, String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
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

		TCustomerInfo tCustomerInfo = customerInfoDAO.getByCustomerId(customerDTO.getCustomerId());

//		returnData.put("canCarryReward", tCustomerInfo.getFbalance().toString());
//		returnData.put("totalReward", tCustomerInfo.getFtotalBalance().toString());
		returnData.put("promoteCode", tCustomerInfo.getFposterImage());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取我的余额页面成功");
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getFansOrderList(Integer pageSize, Integer offset, Integer clientType, String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
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

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as orderId,t.forderNum as forderNum,t.fcreateTime as fcreateTime,t.fstatus as fstatus,t.TCustomer.fphoto as fphoto,t.TCustomer.fweixinName as fweixinName,t.fdistributionRebateAmount as fdistributionRebateAmount ")
				.append("from TOrder t where t.fstatus >= 20 and t.fstatus <= 100 and t.fdistributionerId = :fdistributionerId order by t.fcreateTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("fdistributionerId", customerDTO.getCustomerId());
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<Map<String, Object>> fansOrderList = Lists.newArrayList();
		for (Map<String, Object> amap : list) {
			Map<String, Object> fansOrderMap = new HashMap<String, Object>();
			if (amap.get("orderId") != null && StringUtils.isNotBlank(amap.get("orderId").toString())) {
				fansOrderMap.put("orderId", amap.get("orderId").toString());
			}
			if (amap.get("forderNum") != null && StringUtils.isNotBlank(amap.get("forderNum").toString())) {
				fansOrderMap.put("orderNum", amap.get("forderNum").toString());
			}
			if (amap.get("fcreateTime") != null && StringUtils.isNotBlank(amap.get("fcreateTime").toString())) {
				fansOrderMap.put("createTime",
						DateFormatUtils.format((Date) amap.get("fcreateTime"), "yyyy-MM-dd HH:mm:ss"));
			}
			if (amap.get("fstatus") != null && StringUtils.isNotBlank(amap.get("fstatus").toString())) {
				fansOrderMap.put("status", DictionaryUtil.getString(DictionaryUtil.OrderStatus,
						Integer.parseInt(amap.get("fstatus").toString())));
			}
			if (amap.get("fphoto") != null && StringUtils.isNotBlank(amap.get("fphoto").toString())) {
				fansOrderMap.put("fansImageUrl", amap.get("fphoto").toString());
			}
			if (amap.get("fweixinName") != null && StringUtils.isNotBlank(amap.get("fweixinName").toString())) {
				fansOrderMap.put("fansName", amap.get("fweixinName").toString());
			}
			if (amap.get("fdistributionRebateAmount") != null
					&& StringUtils.isNotBlank(amap.get("fdistributionRebateAmount").toString())) {
				fansOrderMap.put("reward", amap.get("fdistributionRebateAmount").toString());
			} else {
				fansOrderMap.put("reward", "0");
			}
			fansOrderList.add(fansOrderMap);
		}

		returnData.put("fansOrderList", fansOrderList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取粉丝订单详情成功");
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getFansOrder(Integer clientType, String ticket, String orderId) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
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

		TOrder t = orderDAO.findOne(orderId);
		Map<String, Object> fansOrderMap = new HashMap<String, Object>();
		fansOrderMap.put("orderId", t.getId());
		fansOrderMap.put("orderNum", t.getForderNum());
		fansOrderMap.put("createTime", DateFormatUtils.format(t.getFcreateTime(), "yyyy-MM-dd  HH:mm:ss"));
		fansOrderMap.put("status", DictionaryUtil.getString(DictionaryUtil.OrderStatus, t.getFstatus()));
		fansOrderMap.put("fansName", t.getTCustomer().getFweixinName());
//		fansOrderMap.put("reward", t.getFdistributionRebateAmount());
		fansOrderMap.put("orderTotal", t.getFtotal());
		returnData.put("fansOrder", fansOrderMap);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取粉丝订单详情成功");
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getWithdrawalHistory(Integer pageSize, Integer offset, Integer clientType, String ticket) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
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

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as withdrawalId,t.fstatus as fstatus,t.famount as famount,t.fapplyTime as fapplyTime,t.fauditTime as fauditTime,t.ftoAccountTime as ftoAccountTime ")
				.append("from TCustomerWithdrawal t where t.fcustomerId = :fcustomerId order by t.fcreateTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("fcustomerId", customerDTO.getCustomerId());
		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		List<Map<String, Object>> withdrawalList = Lists.newArrayList();
		for (Map<String, Object> amap : list) {
			Map<String, Object> withdrawalMap = new HashMap<String, Object>();
			if (amap.get("withdrawalId") != null && StringUtils.isNotBlank(amap.get("withdrawalId").toString())) {
				withdrawalMap.put("withdrawalId", amap.get("withdrawalId").toString());
			}
			Integer status = Integer.parseInt(amap.get("fstatus").toString());
			withdrawalMap.put("status", DictionaryUtil.getString(DictionaryUtil.WithdrawalStatus, status));
			if (status.intValue() == 10) {
				withdrawalMap.put("createTime", DateFormatUtils.format((Date) amap.get("fapplyTime"), "MM-dd HH:mm"));
			} else if (status.intValue() == 30) {
				withdrawalMap.put("createTime", DateFormatUtils.format((Date) amap.get("fauditTime"), "MM-dd HH:mm"));
			} else if (status.intValue() == 90) {
				withdrawalMap.put("createTime",
						DateFormatUtils.format((Date) amap.get("ftoAccountTime"), "MM-dd HH:mm"));
			}
			if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
				withdrawalMap.put("reawrd", amap.get("famount").toString());
			}
			withdrawalList.add(withdrawalMap);
		}
		returnData.put("withdrawalList", withdrawalList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取历史提现列表成功");
		responseDTO.setData(returnData);
		return responseDTO;
	}

	public ResponseDTO getWithdrawalStatus(Integer clientType, String ticket, String withdrawalId) {
		ResponseDTO responseDTO = new ResponseDTO();
		Map<String, Object> returnData = Maps.newHashMap();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(withdrawalId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("withdrawalId参数不能为空，请检查withdrawalId的传递参数值！");
			return responseDTO;
		}
		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TCustomerWithdrawal tCustomerWithdrawal = customerWithdrawalDAO.findOne(withdrawalId);
		Map<String, Object> withdrawalMap = new HashMap<String, Object>();
		if (tCustomerWithdrawal.getFstatus().intValue() == 10) {
			withdrawalMap.put("status", 1);
			withdrawalMap.put("applyTime", DateFormatUtils.format(tCustomerWithdrawal.getFapplyTime(), "MM-dd HH:mm"));
			withdrawalMap.put("auditTime", "");
			withdrawalMap.put("toAccountTime", "");
		} else if (tCustomerWithdrawal.getFstatus().intValue() == 30) {
			withdrawalMap.put("status", 2);
			withdrawalMap.put("applyTime", DateFormatUtils.format(tCustomerWithdrawal.getFapplyTime(), "MM-dd HH:mm"));
			withdrawalMap.put("auditTime", DateFormatUtils.format(tCustomerWithdrawal.getFauditTime(), "MM-dd HH:mm"));
			withdrawalMap.put("toAccountTime", "");
		} else if (tCustomerWithdrawal.getFstatus().intValue() == 90) {
			withdrawalMap.put("status", 3);
			withdrawalMap.put("applyTime", DateFormatUtils.format(tCustomerWithdrawal.getFapplyTime(), "MM-dd HH:mm"));
			withdrawalMap.put("auditTime", DateFormatUtils.format(tCustomerWithdrawal.getFauditTime(), "MM-dd HH:mm"));
			withdrawalMap.put("toAccountTime",
					DateFormatUtils.format(tCustomerWithdrawal.getFtoAccountTime(), "MM-dd HH:mm"));
		}
		returnData.put("withdrawalMap", withdrawalMap);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("获取历史提现列表成功");
		responseDTO.setData(returnData);
		return responseDTO;
	}

}