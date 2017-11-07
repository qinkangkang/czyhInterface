package com.innee.czyhInterface.service.v1.welfare;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.ShoppingAddressDAO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.entity.TShoppingAddress;
import com.innee.czyhInterface.impl.welfareImpl.WelfaresService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.IpUtil;

@Component
@Transactional
public class WelfareService {

	private static final Logger logger = LoggerFactory.getLogger(WelfareService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private WelfaresService welfaresService;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private ShoppingAddressDAO shoppingAddressDAO;

	/**
	 * 兑换商城商品
	 * 
	 * @return
	 */
	public ResponseDTO convertOrder(String ticket, String bonusGoodsId, Integer clientType, String addressId
			,HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		if (StringUtils.isBlank(bonusGoodsId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("bonusGoodsId参数不能为空，请检查bonusGoodsId的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		TShoppingAddress shoppingAddress = shoppingAddressDAO.findOne(addressId);
		if (shoppingAddress == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(102);
			responseDTO.setMsg("收货地址为空");
			return responseDTO;
		}
		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}

		responseDTO = welfaresService.convertOrder(customerId, bonusGoodsId, shoppingAddress.getFname(),
			shoppingAddress.getFphone(), shoppingAddress.getFaddress(),IpUtil.getIpAddr(request));

		return responseDTO;
	}

	public ResponseDTO welfareGoodsList(String ticket, Integer clientType) {
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

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}

		responseDTO = welfaresService.welfareGoodsList(customerId);

		return responseDTO;
	}

	public ResponseDTO welfareGoodsListhtml(String ticket, Integer clientType) {
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

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}

		responseDTO = welfaresService.welfareGoodsListhtml(customerId);

		return responseDTO;
	}

	public ResponseDTO welfareOrderGoodsList(String ticket, Integer clientType, Integer pageSize, Integer offset) {
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

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		responseDTO = welfaresService.welfareOrderGoodsList(customerId, page.getPageSize(), page.getOffset());

		return responseDTO;
	}

	public ResponseDTO welfareBonusDeail(String ticket, Integer clientType, Integer pageSize, Integer offset) {
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

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}
		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		responseDTO = welfaresService.welfareBonusDeail(customerId, page.getPageSize(), page.getOffset());

		return responseDTO;
	}

	public ResponseDTO welfareGoodsDetail(String goodsId, String ticket, Integer clientType) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}

		if (StringUtils.isBlank(goodsId)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("goodsId参数不能为空，请检查goodsId的传递参数值！");
			return responseDTO;
		}

		CustomerDTO customerDTO = fxlService.getCustomerByTicket(ticket, clientType);
		if (!customerDTO.isEnable()) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(customerDTO.getStatusCode());
			responseDTO.setMsg(customerDTO.getMsg());
			return responseDTO;
		}

		String customerId = "";
		if (customerDTO != null) {
			customerId = customerDTO.getCustomerId();
		}

		responseDTO = welfaresService.welfareGoodsDetail(customerId, goodsId);

		return responseDTO;
	}

}