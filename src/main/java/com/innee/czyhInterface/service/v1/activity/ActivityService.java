package com.innee.czyhInterface.service.v1.activity;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
import org.springside.modules.utils.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.innee.czyhInterface.dao.ArticleDAO;
import com.innee.czyhInterface.dao.CommentDAO;
import com.innee.czyhInterface.dao.CustomerDAO;
import com.innee.czyhInterface.dao.CustomerInfoDAO;
import com.innee.czyhInterface.dao.CustomerTicketDAO;
import com.innee.czyhInterface.dao.EventDAO;
import com.innee.czyhInterface.dao.OrderDAO;
import com.innee.czyhInterface.dao.ShoppingAddressDAO;
import com.innee.czyhInterface.dao.SmsDAO;
import com.innee.czyhInterface.dto.CommentRecommendDTO;
import com.innee.czyhInterface.dto.CustomerDTO;
import com.innee.czyhInterface.dto.m.CommentDTO;
import com.innee.czyhInterface.dto.m.PageDTO;
import com.innee.czyhInterface.dto.m.PublicImageDTO;
import com.innee.czyhInterface.dto.m.ResponseDTO;
import com.innee.czyhInterface.dto.m.UserDTO;
import com.innee.czyhInterface.dto.m.user.ShoppingAddressDTO;
import com.innee.czyhInterface.entity.TComment;
import com.innee.czyhInterface.entity.TCustomer;
import com.innee.czyhInterface.entity.TCustomerInfo;
import com.innee.czyhInterface.entity.TCustomerTicket;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.entity.TOrder;
import com.innee.czyhInterface.entity.TShoppingAddress;
import com.innee.czyhInterface.entity.TSms;
import com.innee.czyhInterface.service.v2.CommonService;
import com.innee.czyhInterface.service.v2.FxlService;
import com.innee.czyhInterface.service.v2.PublicService;
import com.innee.czyhInterface.util.ArrayStringUtils;
import com.innee.czyhInterface.util.BadWordUtil;
import com.innee.czyhInterface.util.CommonPage;
import com.innee.czyhInterface.util.Constant;
import com.innee.czyhInterface.util.DictionaryUtil;
import com.innee.czyhInterface.util.HeadImageUtil;
import com.innee.czyhInterface.util.PropertiesUtil;
import com.innee.czyhInterface.util.asynchronoustasks.AsynchronousTasksManager;
import com.innee.czyhInterface.util.asynchronoustasks.taskBeanImpl.CustomerLogBean;
import com.innee.czyhInterface.util.dingTalk.DingTalkUtil;
import com.innee.czyhInterface.util.log.OutPutLogUtil;
import com.innee.czyhInterface.util.sms.SmsResult;
import com.innee.czyhInterface.util.sms.SmsUtil;
import com.innee.czyhInterface.util.wx.WxmpUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 营销类接口
 * 
 * @author jinshengzhi
 *
 */
@Component
@Transactional
public class ActivityService {

	private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private SmsDAO smsDAO;

	@Autowired
	PublicService publicService;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private FxlService fxlService;

	@Autowired
	private CustomerInfoDAO customerInfoDAO;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ResponseDTO invitationCourtesy(String ticket, Integer clientType) {
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

		List<TCustomerInfo> followCuIdList = customerInfoDAO.findfollowCustomerIdList(customerDTO.getCustomerId());
		
		
		
		
		

		

		responseDTO.setSuccess(true);
		responseDTO.setStatusCode(0);
		return responseDTO;
	}

}