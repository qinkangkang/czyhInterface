package com.innee.czyhInterface.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TOrder;

public interface OrderDAO extends JpaRepository<TOrder, String>, JpaSpecificationExecutor<TOrder> {

	@Query("select max(t.forderNum) from TOrder t where t.fcreateTime > ?1")
	String getMaxOrderNum(Date date);

	@Modifying
	@Query("update TOrder t set t.fstatus = ?1 where t.id = ?2")
	int updateOrderStatus(Integer status, String id);

	@Modifying
	@Query("update TOrder t set t.fstatus = ?1, t.fpayType = ?2, t.fchannel = ?3 where t.id = ?4")
	void updateOrderStatusAndPayType(Integer status, Integer payType,Integer channel, String id);

	@Modifying
	@Query("update TOrder t set t.fstatus = ?1, t.frefundTime = ?2, t.frefundReason = ?3 where t.id = ?4")
	void updateOrderStatusAndRefundTimeAndRefundReason(Integer status, Date refundTime, String refundReason, String id);

	@Modifying
	@Query("update TOrder t set t.fpayType = ?2 where t.id = ?1")
	void updatePayType(String id, Integer payType);

	@Modifying
	@Query("update TOrder t set t.fpayType = ?1, t.frecipient = ?2, t.fremark = ?3 where t.id = ?4")
	void updateOrderPayTypeAndRecipientAndRemark(Integer payType, String recipient, String remark, String id);

	@Modifying
	@Query("update TOrder t set t.fstatus = ?1, t.fpayType = ?2, t.frecipient = ?3, t.fremark = ?4 where t.id = ?5")
	void updateOrderStatusAndPayTypeAndRecipientAndRemark(Integer status, Integer payType, String recipient,
			String remark, String id);

	@Modifying
	@Query("update TOrder t set t.fstatus = ?1, t.fpayTime = ?2 where t.id = ?3")
	void updateOrderStatusAndPayTime(Integer status, Date payTime, String id);

	@Modifying
	@Query("update TOrder t set t.fstatus = ?2, t.fverificationTime = ?3 where t.id = ?1")
	int updateOrderStatusAndFverificationTime(String id, Integer status, Date verificationTime);

	@Query("select count(t.id) from TOrder t where t.TEvent.id = ?1 and t.fstatus < 100")
	Long getOrderCountByEventId(String eventId);

	TOrder getByFstatusAndForderNum(Integer status, String orderNum);

	@Query("select count(t.id) from TOrder t where t.TCustomer.id = ?1 and t.fstatus = ?2 and t.fsource != 20")
	Integer countOrderNumByStatus(String customerId, Integer i);

	@Query("select COALESCE(sum(t.fcount),0) from TOrder t where t.fstatus < ?3 and t.TCustomer.id = ?1 and t.TEvent.id = ?2")
	Integer sumBuyCount(String customerId, String eventId, Integer status);

	@Modifying
	@Query("update TOrder t set t.fgps = ?1 where t.id = ?2")
	void updateOrderGps(String gps, String id);

	@Modifying
	@Query("update TOrder t set t.TCustomer.id = ?1 where t.TCustomer.id = ?2")
	void updateOrderByCustomer(String id2, String id);

	@Query("select t from TOrder t where t.TEvent.id = ?1 and t.TEventSession.id = ?2 and t.fstatus = 20")
	List<TOrder> findByEventAndSession(String eventId, String sessionId);

//	@Modifying
//	@Query("update TOrder t set t.fstatus = ?2, t.fverificationTime = ?3 ,t.fautoVerificationTime = ?4 where t.id = ?1")
//	int updateOrderStatusAndFverificationTimeAuto(String id, Integer status, Date verificationTime,
//			Date autoVerificationTime);
	
	@Query("select t from TOrder t where t.TEvent.id = ?1 and t.TCustomer.id = ?2 ")
	List<TOrder> findByEventAndCustomer(String eventId, String customerId);
	
	@Query("select count(t.id) from TOrder t where t.TCustomer.id = ?1 and t.fstatus >= ?2 and t.fstatus <= ?3 and t.fsource != 20")
	Integer orderNumByStatus(String customerId, Integer i,Integer l);

	@Query("select t from TOrder t where t.TSponsor.id = ?1 and t.fverificationCode = ?2 and t.fstatus = 20 ")
	TOrder findByCode(String TSponsor, String verificationCode);
	
}