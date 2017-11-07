package com.innee.czyhInterface.util.dingTalk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.innee.czyhInterface.dto.m.CartGoodsDTO;

public class DingDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String agentid;

	private Map<String, Object> text;

	private String touser;

	private String msgtype;
	
	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Map<String, Object> getText() {
		return text;
	}

	public void setText(Map<String, Object> text) {
		this.text = text;
	}

	
}