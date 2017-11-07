package com.innee.czyhInterface.service.v2;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.SponsorBalanceDAO;
import com.innee.czyhInterface.dao.SponsorStatementDAO;
import com.innee.czyhInterface.dao.SponsorWithdrawDAO;
import com.innee.czyhInterface.dto.m.OrderVerificationDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.StatementDetailDTO;
import com.innee.czyhInterface.dto.m.StatementSimpleDTO;
import com.innee.czyhInterface.dto.m.WithdrawalDTO;
import com.innee.czyhInterface.dto.m.WithdrawalSimpleDTO;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TSponsor;
import com.innee.czyhInterface.entity.TSponsorBalance;
import com.innee.czyhInterface.entity.TSponsorStatement;
import com.innee.czyhInterface.entity.TSponsorWithdraw;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.DictionaryUtil;

import net.sf.ehcache.CacheManager;

/**
 * interface业务管理类.
 * 
 * @author jinshenzhi
 */
@Component
@Transactional
public class SettlementService {

	private static final Logger logger = LoggerFactory.getLogger(SettlementService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CommonService commonService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private SponsorBalanceDAO sponsorBalanceDAO;

	@Autowired
	private SponsorStatementDAO sponsorStatementDAO;

	@Autowired
	private SponsorWithdrawDAO sponsorWithdrawDAO;

	@Transactional(readOnly = true)
	public ResponseDTO getUnStatementList(String ticket, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
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
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<OrderVerificationDTO> unstatementList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, o.id as orderId, o.feventTitle as eventTitle, o.forderNum as orderNum, t.forderOriginalAmount as amount, t.fcreateTime as createTime from TOrderVerification t inner join t.TOrder o where t.TSponsor.id = :sponsorId and t.fstatus = 10 order by t.fcreateTime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("sponsorId", tCustomer.getTSponsor().getId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		OrderVerificationDTO verificationDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			verificationDTO = new OrderVerificationDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				verificationDTO.setVerificationId(amap.get("id").toString());
			}
			if (amap.get("createTime") != null && StringUtils.isNotBlank(amap.get("createTime").toString())) {
				date = (Date) amap.get("createTime");
				verificationDTO.setVrificationTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("orderId") != null && StringUtils.isNotBlank(amap.get("orderId").toString())) {
				verificationDTO.setOrderId(amap.get("orderId").toString());
			}
			if (amap.get("eventTitle") != null && StringUtils.isNotBlank(amap.get("eventTitle").toString())) {
				verificationDTO.setEventTitle(amap.get("eventTitle").toString());
			}
			if (amap.get("amount") != null && StringUtils.isNotBlank(amap.get("amount").toString())) {
				BigDecimal amount = (BigDecimal) amap.get("amount");
				verificationDTO.setAmount(amount);
				if (amount.compareTo(BigDecimal.ZERO) < 0) {
					verificationDTO.setInOut(-1);
				} else {
					verificationDTO.setInOut(1);
				}
			} else {
				verificationDTO.setAmount(BigDecimal.ZERO);
			}
			unstatementList.add(verificationDTO);
		}

		hql.delete(0, hql.length());
		hql.append(
				"select sum(t.forderOriginalAmount) as total from TOrderVerification t inner join t.TOrder o where t.TSponsor.id = :sponsorId and t.fstatus = 10");
		BigDecimal total = commonService.findUnique(hql.toString(), hqlMap);
		if (total == null) {
			total = BigDecimal.ZERO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("unstatementList", unstatementList);
		returnData.put("total", total.toString());
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getStatementList(String ticket, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
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
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<StatementSimpleDTO> statementList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.fstatementNum as fstatementNum, t.fbeginTime as fbeginTime, t.fendTime as fendTime, t.famount as famount, t.ftime as ftime from TSponsorStatement t where t.TSponsor.id = :sponsorId order by t.ftime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("sponsorId", tCustomer.getTSponsor().getId());

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		StatementSimpleDTO statementSimpleDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			statementSimpleDTO = new StatementSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				statementSimpleDTO.setStatementId(amap.get("id").toString());
			}
			if (amap.get("fstatementNum") != null && StringUtils.isNotBlank(amap.get("fstatementNum").toString())) {
				statementSimpleDTO.setStatementNum(amap.get("fstatementNum").toString());
			}
			if (amap.get("fbeginTime") != null && StringUtils.isNotBlank(amap.get("fbeginTime").toString())) {
				date = (Date) amap.get("fbeginTime");
				statementSimpleDTO.setStartTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("fendTime") != null && StringUtils.isNotBlank(amap.get("fendTime").toString())) {
				date = (Date) amap.get("fendTime");
				statementSimpleDTO.setEndTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("ftime") != null && StringUtils.isNotBlank(amap.get("ftime").toString())) {
				date = (Date) amap.get("ftime");
				statementSimpleDTO.setCreateTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
				statementSimpleDTO.setTotal((BigDecimal) amap.get("famount"));
			} else {
				statementSimpleDTO.setTotal(BigDecimal.ZERO);
			}
			statementList.add(statementSimpleDTO);
		}

		hql.delete(0, hql.length());
		hql.append("select sum(t.famount) as total from TSponsorStatement t where t.TSponsor.id = :sponsorId");
		BigDecimal total = commonService.findUnique(hql.toString(), hqlMap);
		if (total == null) {
			total = BigDecimal.ZERO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("statementList", statementList);
		returnData.put("total", total.toString());
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getStatementDetailList(String ticket, String statementId, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
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
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<StatementDetailDTO> statementDetailList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.ftype as ftype, t.fobjectId as fobjectId, t.fobjectNum as fobjectNum, t.famount as famount, t.ftime as ftime from TSponsorBalance t where t.TSponsorStatement.id = :statementId order by t.ftime desc");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("statementId", statementId);

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		StatementDetailDTO statementDetailDTO = null;
		Date date = null;
		int type = 0;
		for (Map<String, Object> amap : list) {
			statementDetailDTO = new StatementDetailDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				statementDetailDTO.setDetailId(amap.get("id").toString());
			}
			if (amap.get("fobjectNum") != null && StringUtils.isNotBlank(amap.get("fobjectNum").toString())) {
				statementDetailDTO.setTitle(amap.get("fobjectNum").toString());
			}
			if (amap.get("ftype") != null && StringUtils.isNotBlank(amap.get("ftype").toString())) {
				type = ((Integer) amap.get("ftype")).intValue();
				statementDetailDTO.setType(type);
				statementDetailDTO.setTypeString(DictionaryUtil.getString(DictionaryUtil.BalanceType, type));
				if (type == 10) {
					statementDetailDTO.setInOut(1);
				} else if (type == 20 || type == 50) {
					statementDetailDTO.setInOut(-1);
				}
			}
			if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
				statementDetailDTO.setAmount(amap.get("famount").toString());
			} else {
				statementDetailDTO.setAmount("0");
			}
			if (amap.get("ftime") != null && StringUtils.isNotBlank(amap.get("ftime").toString())) {
				date = (Date) amap.get("ftime");
				statementDetailDTO.setCreateTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			statementDetailList.add(statementDetailDTO);
		}

		StatementSimpleDTO statementSimpleDTO = new StatementSimpleDTO();
		TSponsorStatement tSponsorStatement = sponsorStatementDAO.getOne(statementId);

		statementSimpleDTO.setStatementId(statementId);
		statementSimpleDTO.setStartTime(tSponsorStatement.getFbeginTime() != null
				? DateFormatUtils.format(tSponsorStatement.getFbeginTime(), "yyyy-MM-dd") : StringUtils.EMPTY);
		statementSimpleDTO.setEndTime(tSponsorStatement.getFendTime() != null
				? DateFormatUtils.format(tSponsorStatement.getFendTime(), "yyyy-MM-dd") : StringUtils.EMPTY);
		statementSimpleDTO.setTotal(tSponsorStatement.getFamount());

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("statementInfo", statementSimpleDTO);
		returnData.put("statementDetailList", statementDetailList);
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO viewStatementDetail(String ticket, String statementDetailId) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("您提供的Ticket信息有误，请重新登录零到壹！");
			return responseDTO;
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
		}

		TSponsorBalance tSponsorBalance = sponsorBalanceDAO.findOne(statementDetailId);
		if (tSponsorBalance == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的statementDetailId参数有误，statementDetailId=“" + statementDetailId + "”的订单不存在！");
			return responseDTO;
		}
		TSponsorStatement sponsorStatement = tSponsorBalance.getTSponsorStatement();

		StatementDetailDTO statementDetailDTO = new StatementDetailDTO();
		statementDetailDTO.setStatementId(sponsorStatement.getId());
		statementDetailDTO.setDetailId(statementDetailId);
		statementDetailDTO.setCreateTime(DateFormatUtils.format(tSponsorBalance.getFtime(), "yyyy-MM-dd HH:mm"));
		if (tSponsorBalance.getFtype().intValue() == 10) {
			statementDetailDTO.setInOut(1);
		} else if (tSponsorBalance.getFtype().intValue() == 20 || tSponsorBalance.getFtype().intValue() == 50) {
			statementDetailDTO.setInOut(-1);
		}
		statementDetailDTO.setAmount(tSponsorBalance.getFamount().toString());
		statementDetailDTO.setTitle(tSponsorBalance.getFobjectNum());
		statementDetailDTO.setType(tSponsorBalance.getFtype());
		statementDetailDTO
				.setTypeString(DictionaryUtil.getString(DictionaryUtil.BalanceType, tSponsorBalance.getFtype()));
		statementDetailDTO.setStatementNum(sponsorStatement.getFstatementNum());
		statementDetailDTO.setStartTime(DateFormatUtils.format(sponsorStatement.getFbeginTime(), "yyyy-MM-dd"));
		statementDetailDTO.setEndTime(DateFormatUtils.format(sponsorStatement.getFendTime(), "yyyy-MM-dd"));

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("statementDetailInfo", statementDetailDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO getWithdrawalList(String ticket, Integer status, Integer pageSize, Integer offset) {
		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("ticket参数有误，这是个无效或者过期的ticket信息！");
			return responseDTO;
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
		}

		CommonPage page = new CommonPage();
		if (pageSize != null) {
			page.setPageSize(pageSize);
		}
		if (offset != null) {
			page.setOffset(offset);
		}

		List<WithdrawalSimpleDTO> withdrawalList = Lists.newArrayList();

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select t.id as id, t.famount as famount, t.fapplyTime as fapplyTime ")
		.append("from TSponsorWithdraw t where t.TSponsor.id = :sponsorId and t.fstatus < 999");
		Map<String, Object> hqlMap = new HashMap<String, Object>();
		hqlMap.put("sponsorId", tCustomer.getTSponsor().getId());
		if(status != 0){
			hql.append("  and t.fstatus = :status");
			hqlMap.put("status", status);
		}
		hql.append(" order by t.ftime desc");

		commonService.findPage(hql.toString(), page, hqlMap);
		List<Map<String, Object>> list = page.getResult();

		WithdrawalSimpleDTO withdrawalSimpleDTO = null;
		Date date = null;
		for (Map<String, Object> amap : list) {
			withdrawalSimpleDTO = new WithdrawalSimpleDTO();
			if (amap.get("id") != null && StringUtils.isNotBlank(amap.get("id").toString())) {
				withdrawalSimpleDTO.setWithdrawalId(amap.get("id").toString());
			}
			if (amap.get("fapplyTime") != null && StringUtils.isNotBlank(amap.get("fapplyTime").toString())) {
				date = (Date) amap.get("fapplyTime");
				withdrawalSimpleDTO.setApplyTime(DateFormatUtils.format(date, "yyyy-MM-dd"));
			}
			if (status.intValue() == 10) {
				//withdrawalSimpleDTO.setTitle("提现申请");
			} else if (status.intValue() == 90) {
				//withdrawalSimpleDTO.setTitle("提现完成");
			}
			if (amap.get("famount") != null && StringUtils.isNotBlank(amap.get("famount").toString())) {
				//withdrawalSimpleDTO.setAmount((BigDecimal) amap.get("famount"));
			} else {
				//withdrawalSimpleDTO.setAmount(BigDecimal.ZERO);
			}
			withdrawalList.add(withdrawalSimpleDTO);
		}

		hql.delete(0, hql.length());
		hql.append(
				"select sum(t.famount) as total from TSponsorWithdraw t where t.TSponsor.id = :sponsorId and t.fstatus = :status");
		BigDecimal total = commonService.findUnique(hql.toString(), hqlMap);
		if (total == null) {
			total = BigDecimal.ZERO;
		}

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("withdrawalList", withdrawalList);
		returnData.put("total", total.toString());
		PageDTO pageDTO = new PageDTO(page.getTotalCount(), page.getPageSize(), page.getOffset());
		returnData.put("page", pageDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}

	@Transactional(readOnly = true)
	public ResponseDTO viewWithdrawal(String ticket, String withdrawalId) {

		ResponseDTO responseDTO = new ResponseDTO();
		if (StringUtils.isBlank(ticket)) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(201);
			responseDTO.setMsg("ticket参数不能为空，请检查ticket的传递参数值！");
			return responseDTO;
		}
		TCustomer tCustomer = customerDAO.getByFticketAndFtype(ticket, 10);
		if (tCustomer == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(100);
			responseDTO.setMsg("您提供的Ticket信息有误，请重新登录零到壹！");
			return responseDTO;
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
		}
		TSponsor tSponsor = tCustomer.getTSponsor();

		TSponsorWithdraw tSponsorWithdraw = sponsorWithdrawDAO.findOne(withdrawalId);
		if (tSponsorWithdraw == null) {
			responseDTO.setSuccess(false);
			responseDTO.setStatusCode(202);
			responseDTO.setMsg("您输入的withdrawalId参数有误，withdrawalId=“" + withdrawalId + "”的提现单不存在！");
			return responseDTO;
		}

		WithdrawalDTO withdrawalDTO = new WithdrawalDTO();

		withdrawalDTO.setWithdrawalId(withdrawalId);
		StringBuilder bankInfo = new StringBuilder();
		String ba = tSponsor.getFbankAccount();
		if (ba.length() > 4) {
			ba = ba.substring(ba.length() - 4, ba.length());
		}
		bankInfo.append(DictionaryUtil.getString(DictionaryUtil.Bank, tSponsor.getFbankId())).append("(").append(ba)
				.append(")");
		withdrawalDTO.setBankInfo(bankInfo.toString());
		withdrawalDTO.setStatusString(
				DictionaryUtil.getString(DictionaryUtil.WithdrawalStatus, tSponsorWithdraw.getFstatus()));
		withdrawalDTO.setAmount(tSponsorWithdraw.getFamount());
		withdrawalDTO.setApplyTime(DateFormatUtils.format(tSponsorWithdraw.getFapplyTime(), "yyyy-MM-dd HH:mm"));
		withdrawalDTO.setAccountTime(tSponsorWithdraw.getFtoAccountTime() != null
				? DateFormatUtils.format(tSponsorWithdraw.getFtoAccountTime(), "yyyy-MM-dd HH:mm") : StringUtils.EMPTY);

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		Map<String, Object> returnData = Maps.newHashMap();
		returnData.put("withdrawalInfo", withdrawalDTO);
		responseDTO.setData(returnData);
		return responseDTO;
	}
}