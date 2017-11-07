package com.innee.czyhInterface.util.express;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.express.ExpressDTO;
import com.innee.czyhInterface.util.HttpClientUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.sms.SmsUtil2;

public class ExpressUtil {

	private static Logger logger = LoggerFactory.getLogger(SmsUtil2.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	private static final String ExCustomerId = PropertiesUtil.getProperty("exCustomerId");

	private static final String ExKey = PropertiesUtil.getProperty("exKey");

	private static final String ExApiUrl = PropertiesUtil.getProperty("exApiUrl");

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public static ResponseDTO sendExPress(Express ex) {
		ResponseDTO responseDTO = new ResponseDTO();

		ExpressDTO dto = new ExpressDTO();
		try {
			String postUrl = new StringBuilder().append(ExApiUrl).toString();

			Map<String, Object> paramsMap = Maps.newHashMap();

			JSONObject jsonParam = new JSONObject();
			for (Map.Entry<String, Object> e : ex.getMapBody().entrySet()) {
				jsonParam.put(e.getKey(), e.getValue().toString());
			}
			StringBuilder sb = new StringBuilder();
			sb.append(jsonParam).append(ExKey).append(ExCustomerId);
			paramsMap.put("customer", ExCustomerId);
			paramsMap.put("sign", MD5Util.md5Encode(sb.toString(), "UTF-8").toUpperCase());
			paramsMap.put("param", jsonParam);

			String resex = HttpClientUtil.callUrlPost(postUrl, paramsMap);
			dto = mapper.fromJson(resex, ExpressDTO.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("track", dto);
		responseDTO.setData(data);
		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;

	}
}
