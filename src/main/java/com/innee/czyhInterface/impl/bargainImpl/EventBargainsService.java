package com.innee.czyhInterface.impl.bargainImpl;

import javax.servlet.http.HttpServletRequest;

import com.innee.czyhInterface.dto.m.ResponseDTO;

public interface EventBargainsService {
	
	public ResponseDTO eventBargainingList(Integer pageSize, Integer offset);
	
	public ResponseDTO getGamesType(String eventId);
	
	public ResponseDTO getMyBargain(String eventBargainingId, String customerId, String customerBargainingId);
	
	public ResponseDTO helpBargain(String customerId, String customerBargainingId);
	
	public ResponseDTO getbargainList(String customerId, String eventBargainingId,
			Integer pageSize, Integer offset) ;
	
	public ResponseDTO getbargainHelpList(String customerId, String customerBargainingId, Integer pageSize,
			Integer offset);
	
	public ResponseDTO getbargainShare(String customerBargainingId, HttpServletRequest request);
	
	public ResponseDTO getbargaining(String customerId,Integer pageSize, Integer offset);
	
	public ResponseDTO getbarrage(String customerId, String customerBargainingId, Integer pageSize, Integer offset);
	

}
