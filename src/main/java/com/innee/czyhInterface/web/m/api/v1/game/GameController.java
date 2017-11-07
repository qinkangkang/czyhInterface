package com.innee.czyhInterface.web.m.api.v1.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.innee.czyhInterface.service.v1.game.GameService;

@RestController("M_API_GameController")
@RequestMapping(value = "/m/api/game", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class GameController {

	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private GameService gameService;

	@RequestMapping(value = "/savePredictBabyHeight", method = { RequestMethod.GET, RequestMethod.POST })
	public String savePredictBabyHeight(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "babaHeight", required = false) Integer babaHeight,
			@RequestParam(value = "mamaHeight", required = false) Integer mamaHeight,
			@RequestParam(value = "babySex", required = false) Integer babySex,
			@RequestParam(value = "babyHeight", required = false) Integer babyHeight,
			@RequestParam(value = "moreThanAverage", required = false) Integer moreThanAverage) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = gameService.savePredictBabyHeight(clientType, ticket, babaHeight, mamaHeight, babySex,
					babyHeight, moreThanAverage);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("保存预测宝宝身高接口时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}
	
	@RequestMapping(value = "/saveAutoShow", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveAutoShow(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "callback", required = false) String callback,
			@RequestParam(value = "clientType", required = false, defaultValue = "1") Integer clientType,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "phone", required = false) String phone) {

		ResponseDTO responseDTO = null;
		try {
			responseDTO = gameService.saveAutoShow(clientType, name, phone);
		} catch (Exception e) {
			logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
			responseDTO = new ResponseDTO();
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(200);
			responseDTO.setMsg("保存车展用户时出错！");
		}
		if (StringUtils.isBlank(callback)) {
			return mapper.toJson(responseDTO);
		} else {
			return mapper.toJsonP(callback, responseDTO);
		}
	}

}