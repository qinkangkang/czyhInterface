package com.innee.czyhInterface.service.v2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;
import org.springside.modules.utils.Identities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.AppErrorDAO;
import com.innee.czyhInterface.dao.PublicImageDAO;
import com.innee.czyhInterface.dto.m.PublicDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TAppError;
import com.innee.czyhInterface.entity.TPublicImages;
import com.innee.czyhInterface.service.v1.system.RedisService;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.publicImage.common.QiniuException;
import com.innee.czyhInterface.util.publicImage.common.Zone;
import com.innee.czyhInterface.util.publicImage.http.Response;
import com.innee.czyhInterface.util.publicImage.storage.BucketManager;
import com.innee.czyhInterface.util.publicImage.storage.Configuration;
import com.innee.czyhInterface.util.publicImage.storage.UploadManager;
import com.innee.czyhInterface.util.publicImage.storage.model.DefaultPutRet;
import com.innee.czyhInterface.util.publicImage.util.Auth;
import com.innee.czyhInterface.util.redis.ExpireTime;
import com.innee.czyhInterface.util.redis.RedisMoudel;

import net.sf.ehcache.CacheManager;

/**
 * 公共方法(1.公共上传图片方法)
 * 
 * @author jinshengzhi
 *
 */
@Component
@Transactional
public class PublicService {

	private static final Logger logger = LoggerFactory.getLogger(PublicService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static final String imageMogr2 = "imageMogr2";// 图片高级处理

	@Autowired
	private PublicImageDAO publicImageDAO;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private AppErrorDAO appErrorDAO;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private WxService wxService;

	/**
	 * 为客户端生成图片上传Token
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResponseDTO getToken() {

		ResponseDTO responseDTO = new ResponseDTO();

		PublicDTO publicDTO = new PublicDTO();
		Auth auth = Auth.create(PropertiesUtil.getProperty("qiniuAk"), PropertiesUtil.getProperty("qiniuSK"));
		// 获取token的时候为固定上传空间未来多空间时 可增加空间参数进行上传
		String token = auth.uploadToken(PropertiesUtil.getProperty("bucket"), null, 1000 * 3600 * 24 * 12 * 10, null);

		publicDTO.setToken(token);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("getToken", publicDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * app端图片上传接口
	 * 
	 * @param file
	 * @return
	 */
	public ResponseDTO appUpload(File file) {
		ResponseDTO responseDTO = new ResponseDTO();
		String res = null;

		String path = "comment/" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "/";
		String name = Identities.uuid2();
		String type = ".jpg";

		StringBuilder urlPath = new StringBuilder(path).append(name).append(type);
		try {
			String token = getQnToken();
			res = appuploadQn(file, urlPath.toString(), token);
		} catch (IOException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
		TPublicImages tpublicImage = new TPublicImages();
		tpublicImage.setFurl(urlPath.toString());
		tpublicImage.setFtype(1);
		tpublicImage.setIsThumbnail(1);
		tpublicImage.setFcreateTime(new Date());
		publicImageDAO.save(tpublicImage);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("上传图片成功！");

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("urlPath", PropertiesUtil.getProperty("publicQnService") + urlPath.toString());
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * app图片上传接口七牛方法
	 * 
	 * @param FilePath
	 * @param name
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public String appuploadQn(File FilePath, String name, String token) throws IOException {
		Response res = null;
		try {
			// 创建上传对象
			UploadManager uploadManager = new UploadManager(null);
			// 调用put方法上传
			res = uploadManager.put(FilePath, name, token);
		} catch (QiniuException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			Response r = e.response;
			// 请求失败时打印的异常的信息
			// System.out.println(r.toString());
			logger.info(r.bodyString());
			try {
				// System.out.println(r.bodyString());
				logger.info(r.bodyString());
			} catch (QiniuException e1) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				// System.out.println(r.bodyString());
			}
		}
		return res.bodyString();
	}

	/**
	 * web图片上传接口
	 * 
	 * @param file
	 * @return
	 */
	public ResponseDTO webUpload(File file) {

		ResponseDTO responseDTO = new ResponseDTO();

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(now);

		String path = "comment/" + date + "/";
		String name = Identities.uuid2();
		String type = ".jpg";

		StringBuilder urlPath = new StringBuilder(path).append(name).append(type);
		try {
			String token = getQnToken();
			webuploadQn(file, urlPath.toString(), token);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(103);
			responseDTO.setMsg("图片上传到服务器时出错！");
			return responseDTO;
		}

		TPublicImages tpublicImage = new TPublicImages();
		tpublicImage.setFurl(urlPath.toString());
		tpublicImage.setFtype(1);
		tpublicImage.setIsThumbnail(1);
		tpublicImage.setFcreateTime(now);
		publicImageDAO.save(tpublicImage);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("图片上传到服务器成功！");

		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("urlPath", PropertiesUtil.getProperty("publicQnService") + urlPath.toString());
		responseDTO.setData(returnData);
		return responseDTO;
	}

	/**
	 * web端上传七牛调用接口
	 * 
	 * @param FilePath
	 * @return
	 * @throws IOException
	 */
	public String webuploadQn(File FilePath, String name, String token) throws IOException {
		Response res = null;
		try {
			Zone z = Zone.zone1();
	    	Configuration c = new Configuration(z);
			UploadManager uploadManager = new UploadManager(c);
			// 调用put方法上传
			res = uploadManager.put(FilePath, name, token);
		} catch (QiniuException e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			Response r = e.response;
			// 请求失败时打印的异常的信息
			// System.out.println(r.bodyString());
			logger.info(r.bodyString());
			try {

				// System.out.println(r.bodyString());
				logger.info(r.bodyString());
			} catch (QiniuException e1) {
				logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
				// System.out.println(r.bodyString());
				logger.info(r.bodyString());
			}
		}
		return res.bodyString();
	}

	
	/**
	 * 上传头像通用版接口
	 * 
	 * @param file
	 * @return
	 */
	public String logoUpload(String filebase64, File file, Integer flag) {
		String urlLogo = null;
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(now);

		String path = "logo/" + date + "/";
		String name = Identities.uuid2();
		String type = ".jpg";

		StringBuilder urlPath = new StringBuilder(path).append(name).append(type);
		try {
			if (flag == 1) {
				String token = getQnTokenLogo();
//				System.out.println(token + "是什么");
				logoUploadQn(filebase64, urlPath.toString(), token);
			} else {
				String token = getQnTokenLogo();
				webuploadQn(file, urlPath.toString(), token);
			}
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		}
//		System.out.println(PropertiesUtil.getProperty("publicQnServiceLogo") + urlPath.toString());
		urlLogo = PropertiesUtil.getProperty("publicQnServiceLogo") + urlPath.toString();
		
		return urlLogo;
	}
	
	/**
	 * 通用版接口上传调用七牛方法
	 * 
	 * @param FilePath
	 * @return
	 * @throws IOException
	 */
	public String logoUploadQn(String FilePath, String name, String token) throws IOException {
		Response res = null;
		try {
			Zone z = Zone.zone1();
	    	Configuration c = new Configuration(z);
			UploadManager uploadManager = new UploadManager(c);
			String[] headAndBody = StringUtils.split(FilePath, ",");
			String body = headAndBody[1];
			byte[] fileByte = Base64.decodeBase64(body);
//			byte[] fileByte = Base64Utils.decodeFromString(body);
			
			// 调用put方法上传
			res = uploadManager.put(fileByte, name, token);
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			logger.warn(r.bodyString() + "失败原因");
		}
		return res.bodyString();
	}
	
	/**
	 * 申请comment空间token
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getQnToken() {

		Auth auth = Auth.create(PropertiesUtil.getProperty("qiniuAk"), PropertiesUtil.getProperty("qiniuSK"));
		String token = auth.uploadToken(PropertiesUtil.getProperty("bucket"));
		return token;
	}
	
	/**
	 * 申请上传头像空间token
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getQnTokenLogo() {

		// token默认失效时间为86400s 为24小时
		Auth auth = Auth.create(PropertiesUtil.getProperty("qiniuAk"), PropertiesUtil.getProperty("qiniuSK"));
		String token = auth.uploadToken(PropertiesUtil.getProperty("bucketlogo"));
		return token;
	}

	/**
	 * 按照传入的比例对图片进行缩略
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getThumbnailview(String urlPath, Integer proportion) {

		StringBuffer sb = new StringBuffer();
		sb.append(urlPath);
		sb.append("?");
		sb.append(imageMogr2).append("/");
		sb.append("thumbnail").append("/").append("!");
		sb.append(proportion).append("p");

		return sb.toString();
	}

	/**
	 * 终端错误信息上报
	 * 
	 * @param clientType
	 * @param clientInfo
	 * @param errorMessage
	 * @return
	 */
	public ResponseDTO addAppError(Integer clientType, String clientInfo, String errorMessage, String ferrorText,
			String fsystem, String fuser, String fview, String fdata) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (clientType == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("您输入的clientType参数有误，请检查clientType的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(clientInfo)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("clientInfo参数不能为空，请检查clientInfo的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(errorMessage)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(203);
			responseDTO.setMsg("errorMessage参数不能为空，请检查errorMessage的传递参数值！");
			return responseDTO;
		}

		TAppError tAppError = new TAppError();
		tAppError.setFreportTime(new Date());
		tAppError.setFclientType(clientType);

		if (StringUtils.isNotBlank(clientInfo)) {
			if (clientInfo.length() >= 2048) {
				tAppError.setFclientInfo(StringUtils.substring(clientInfo, 0, 2048));
			} else {
				tAppError.setFclientInfo(clientInfo);
			}
		}

		if (StringUtils.isNotBlank(errorMessage)) {
			if (errorMessage.length() >= 2048) {
				tAppError.setFerrorMessage(StringUtils.substring(errorMessage, 0, 2048));
			} else {
				tAppError.setFerrorMessage(errorMessage);
			}
		}

		if (StringUtils.isNotBlank(ferrorText)) {
			if (ferrorText.length() >= 2048) {
				tAppError.setFerrorText(StringUtils.substring(ferrorText, 0, 2048));
			} else {
				tAppError.setFerrorText(ferrorText);
			}
		}
		tAppError.setFsystem(fsystem);
		tAppError.setFuser(fuser);
		tAppError.setFview(fview);

		if (StringUtils.isNotBlank(fdata)) {
			if (fdata.length() >= 2048) {
				tAppError.setFdata(StringUtils.substring(fdata, 0, 2048));
			} else {
				tAppError.setFdata(fdata);
			}
		}
		appErrorDAO.save(tAppError);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("上报错误信息成功,感谢您的上报！");
		return responseDTO;
	}
	
	public ResponseDTO uploadBatch(String base64) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(base64)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("您输入的base64参数有误，请检查base64的传递参数值！");
			return responseDTO;
		}
		JavaType JT = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> baseString = Lists.newArrayList();
		baseString = mapper.fromJson(base64, JT);
		List<String> url = new ArrayList<>();
	    for (int a = 1; a <= baseString.size(); a++) {
	    	url.add(this.logoUpload(baseString.get(a-1), null, 1));
        }

	    Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("url", url);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("批量上传图片成功！");
		return responseDTO;
	}
	
	public ResponseDTO webUploadUrl(String imageUrl) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(imageUrl)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("您输入的imageUrl参数有误，请检查imageUrl的传递参数值！");
			return responseDTO;
		}
		JavaType JT = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> imageUrltring = Lists.newArrayList();
		imageUrltring = mapper.fromJson(imageUrl, JT);
		List<String> url = new ArrayList<>();
	    for (int a = 1; a <= imageUrltring.size(); a++) {
	    	Auth auth = Auth.create(PropertiesUtil.getProperty("qiniuAk"), PropertiesUtil.getProperty("qiniuSK"));
	    	//获取空间管理器
	    	Zone z = Zone.zone1();
	    	Configuration c = new Configuration(z);
	    	BucketManager bucketManager = new BucketManager(auth,c);
	    	String path = "comment/" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "/";
	    	String name = Identities.uuid2();
	    	String type = ".jpg";
	    	
	    	StringBuilder imageKey = new StringBuilder(path).append(name).append(type);
	        try {
				DefaultPutRet putret = bucketManager.fetch(imageUrltring.get(a-1), 
						PropertiesUtil.getProperty("bucket"), imageKey.toString());
			} catch (QiniuException e) {
				 Response r = e.response;
				 System.out.println(r.toString());
				 e.printStackTrace();
			}
	        url.add(PropertiesUtil.getProperty("fexternalUrl") + imageKey);
        }

	    Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("url", url);
		responseDTO.setData(returnData);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("批量上传图片成功！");
		return responseDTO;
	}
	
	
	public ResponseDTO webDownLoadUrl(String wxServeId) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(wxServeId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("您输入的wxServeId参数有误，请检查wxServeId的传递参数值！");
			return responseDTO;
		}
		JavaType JT = mapper.contructCollectionType(ArrayList.class, String.class);
		List<String> imageUrltring = Lists.newArrayList();
		imageUrltring = mapper.fromJson(wxServeId, JT);
		List<String> urls = new ArrayList<>();
		String accessToken = wxService.getAccessToken();
	    for (int a = 1; a <= imageUrltring.size(); a++) {
	    	urls.add(wxService.getMedia(accessToken, imageUrltring.get(a-1)));
        }
	    responseDTO =  this.webUploadUrl(mapper.toJson(urls));
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("批量上传图片成功！");
		return responseDTO;
	}
}