package com.innee.czyhInterface.impl.welfareImpl;

import com.innee.czyhInterface.dto.m.ResponseDTO;

public interface WelfaresService {
	
	public ResponseDTO convertOrder(String customerId, String bonusGoodsId, String name, String phone, String address,
			String ip);
	
	public ResponseDTO welfareGoodsList(String customerId);
	
	public ResponseDTO welfareGoodsListhtml(String customerId);
	
	public ResponseDTO welfareOrderGoodsList(String customerId,Integer pageSize, Integer offset) ;
		
	public ResponseDTO welfareBonusDeail(String customerId, Integer pageSize, Integer offset);
	
	public ResponseDTO welfareGoodsDetail(String customerId, String goodsId);
	
}
