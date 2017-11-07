package com.innee.czyhInterface.service.v1.game;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dao.AutoShowDAO;
import com.innee.czyhInterface.dao.PredictHeightDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TAutoShow;
import com.innee.czyhInterface.entity.TPredictHeight;
import com.innee.czyhInterface.service.v2.FxlService;

/**
 * 游戏service
 * 
 * @author jinshenzhi
 *
 */
@Component
@Transactional
public class GameService {

	private static final Logger logger = LoggerFactory.getLogger(GameService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private FxlService fxlService;

	@Autowired
	private PredictHeightDAO predictHeightDAO;
	
	@Autowired
	private AutoShowDAO autoShowDAO;

	public ResponseDTO savePredictBabyHeight(Integer clientType, String ticket, Integer babaHeight, Integer mamaHeight,
			Integer babySex, Integer babyHeight, Integer moreThanAverage) {
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

		Date now = new Date();
		TPredictHeight tPredictHeight = predictHeightDAO.getByFcustomerId(customerDTO.getCustomerId());
		if (tPredictHeight == null) {
			tPredictHeight = new TPredictHeight();
			tPredictHeight.setFcustomerId(customerDTO.getCustomerId());
			tPredictHeight.setFcreateTime(now);
		}
		tPredictHeight.setFupdateTime(now);
		tPredictHeight.setFbabaHeight(babaHeight);
		tPredictHeight.setFmamaHeight(mamaHeight);
		tPredictHeight.setFbabySex(babySex);
		tPredictHeight.setFbabyHeight(babyHeight);
		tPredictHeight.setFmoreThanAverage(moreThanAverage);

		predictHeightDAO.save(tPredictHeight);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("预测宝宝身高信息保存成功！");
		return responseDTO;
	}
	
	public ResponseDTO saveAutoShow(Integer clientType, String name, String phone) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(name)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("name参数不能为空，请检查name的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(phone)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("phone参数不能为空，请检查phone的传递参数值！");
			return responseDTO;
		}

		Date now = new Date();
		TAutoShow autoShow = new TAutoShow();
		autoShow.setFcreateTime(now);
		autoShow.setFname(name);
		autoShow.setFphone(phone);
		autoShow = autoShowDAO.save(autoShow);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		responseDTO.setMsg("保存车展用户信息保存成功！");
		return responseDTO;
	}

}