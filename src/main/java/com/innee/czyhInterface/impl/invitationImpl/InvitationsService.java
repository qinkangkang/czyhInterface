package com.innee.czyhInterface.impl.invitationImpl;

import com.innee.czyhInterface.dto.m.ResponseDTO;

public interface InvitationsService {
	
	public ResponseDTO getInvitationList(String customerId);
	
	public ResponseDTO getInvitationTop(String customerId);
	
}
