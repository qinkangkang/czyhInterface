package com.innee.czyhInterface.impl.couponImpl;

import com.innee.czyhInterface.dto.m.ResponseDTO;

public interface CouponsService {
	
	public ResponseDTO getChannelCouponList(String customerId, Integer pageSize, Integer offset);
	
	public ResponseDTO receiveCouponByChannel(String customerId, String couponId);
	
	public ResponseDTO getUserCouponByStatus(String customerId, Integer status, Integer pageSize, Integer offset) ;
	
	public ResponseDTO getAvailableCoupon(Integer cityId, String customerId, String typeMaps,String sponsorMaps,
			String goodsMaps,String total,String useCouponTotal,Integer clientType,String freight) ;
	
	public ResponseDTO getAvailableNum(Integer cityId, String customerId, String typeMaps,String sponsorMaps,
			String goodsMaps,String total,String useCouponTotal,Integer clientType,String freight) ;
	
	public ResponseDTO backCoupon(String orderId,Integer status);
	
	public ResponseDTO receiveCoupon(String deliveryId, String customerId);
	
	public ResponseDTO getDiscountAmount(String customerId, String orderTotal,String couponDeliveryId,String postageCouponId,
			String freight,String goodsSkuList,Integer clientType) ;
	
	public ResponseDTO getCouponByEvent(String customerId, String eventId, String typeA, String sponsorId);

}
