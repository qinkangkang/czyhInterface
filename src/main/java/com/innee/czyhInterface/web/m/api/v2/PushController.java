package com.innee.czyhInterface.web.m.api.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.service.v2.PushService;

@RestController("M_API_V2_PushController")
@RequestMapping(value = "/m/api/v2/systemPush", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

public class PushController {

	private static final Logger logger = LoggerFactory.getLogger(PushController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private PushService pushService;

	/**
	 * 推送广播(Android广播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushAndroidAll", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAndroidAll(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendAndroidBroadcast(Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android广播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 推送广播(Android单播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushAndroidUnicast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAndroidUnicast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "DeviceToken", required = false) String DeviceToken,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendAndroidUnicast(DeviceToken, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android单播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 组播广播(Android组播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushAndroidgroupcast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAndroidgroupcast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendAndroidGroupcast(tag, condition, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android组播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * alias自定义推送(Android自定义推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushAndroidCustomizedcast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAndroidCustomizedcast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "aliastype", required = false) String aliastype,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.UmengPushAndroidCustomizedcast(alias, aliastype, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android组播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 组播文件推送(Android)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushAndroidCustomizedcastFile", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAndroidCustomizedcastFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "aliastype", required = false) String aliastype,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.UmengPushAndroidCustomizedcastFile(alias, aliastype, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android文件推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * Filecast广播(Android)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushAndroidFilecast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAndroidFilecast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "aliastype", required = false) String aliastype,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendAndroidFilecast(alias, aliastype, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android文件推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 广播推送(IOS广播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushIOSAll", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushIOSAll(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "text", required = false) String des) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendIOSroadcast(text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("IOS广播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 推送广播(IOS单播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushIOSUnicast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushIOSUnicast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "DeviceToken", required = false) String DeviceToken,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendIOSUnicast(DeviceToken, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("Android单播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 组播推送(IOS组播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushIOSGroupcast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushIOSGroupcast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendIOSGroupcast(tag, condition, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("IOS组播推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 个性定制广播(IOS组播推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushIOSCustomizedcast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushIOSCustomizedcast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "aliastype", required = false) String aliastype,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendIOSCustomizedcast(alias, aliastype, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("IOS个性推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 文件广播(IOS文件推送)
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushIOSFilecast", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushIOSFilecast(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "alias", required = false) String alias,
			@RequestParam(value = "aliastype", required = false) String aliastype,
			@RequestParam(value = "Ticker", required = false) String Ticker,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "des", required = false) String des) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.sendIOSFilecast(alias, aliastype, Ticker, title, text, des);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("IOS个性推送时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}

	/**
	 * 添加用户绑定device_token
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param callback
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/pushaddToken", method = { RequestMethod.GET, RequestMethod.POST })
	public String PushAddToken(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "deviceTokens", required = false) String deviceTokens,
			@RequestParam(value = "DEVICE_ID", required = false) String DEVICE_ID,
			@RequestParam(value = "clientType", required = false) Integer clientType) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.saveToken(ticket, deviceTokens, DEVICE_ID, clientType);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("添加device_tokens时发生错误！");
		}

		return mapper.toJson(responseDTO);
	}

	/**
	 * 用于给用户打标签 并跟友盟同步上传标签
	 * 
	 * @author jinshengzhi
	 * @param request
	 * @param response
	 * @param tag
	 * @param device_tokens
	 * @return
	 */
	@RequestMapping(value = "/addCustomerTags", method = { RequestMethod.GET, RequestMethod.POST })
	public String addCustomerTags(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "ftag", required = false) Integer ftag,
			@RequestParam(value = "fdevice", required = false) String fdevice,
			@RequestParam(value = "clientType", required = false) Integer clientType,
			@RequestParam(value = "device_tokens", required = false) String device_tokens) {
		ResponseDTO responseDTO = null;
		try {
			responseDTO = pushService.addCustomerTags(ticket, ftag, fdevice, clientType, device_tokens);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("用户打标时发生错误！");
		}
		return mapper.toJson(responseDTO);

	}
	/*
	 * //测试钉钉发送消息
	 * 
	 * @RequestMapping(value = "/test", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String test(HttpServletRequest request,
	 * HttpServletResponse response,
	 * 
	 * @RequestParam(value = "msg", required = false) String msg,
	 * 
	 * @RequestParam(value = "toUsers", required = false) String toUsers){
	 * ResponseDTO responseDTO = null; try { pushService.sendDingTalk(msg,
	 * toUsers, request); } catch (Exception e) {
	 * logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e))
	 * ); responseDTO = new ResponseDTO(); responseDTO.setSuccess(false);
	 * responseDTO.setStatusCode(200);
	 * responseDTO.setMsg("添加device_tokens时发生错误！"); }
	 * 
	 * return mapper.toJson(responseDTO); }
	 */
}