package com.innee.czyhInterface.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerBargaining;

public interface CustomerBargainingDAO
		extends JpaRepository<TCustomerBargaining, String>, JpaSpecificationExecutor<TCustomerBargaining> {

	@Query("select count(t.id) from TCustomerBargaining t where t.fdefaultFloorPrice = ?1 and t.fbargainingId = ?2")
	Long getCustomerBargainByPrice(BigDecimal price,String fbargainingId);
	
	@Query("select t from TCustomerBargaining t where t.fcustomerId = ?1 and t.fbargainingId = ?2 and t.fstatus <=30")
	TCustomerBargaining getByCustomerId(String fCustomerBargainingId,String fBargainingId);
	
	@Query("select t from TCustomerBargaining t where t.forderId = ?1")
	TCustomerBargaining getByOrderId(String orderId);
	
	@Modifying
	@Query("update TCustomerBargaining t set t.fstatus = ?2 where t.forderId = ?1")
	void updateStatus(String orderId, Integer status);
	
	@Modifying
	@Query("update TCustomerBargaining t set t.fstatus = ?2 ,t.fpayTime = ?3 where t.forderId = ?1")
	void updateStatusPay(String orderId, Integer status,Date payTime);
	
	@Query("select t from TCustomerBargaining t where t.fbargainingId = ?1 and t.fstatus = 20")
	List<TCustomerBargaining> getBarList(String id);
	
	@Query("select max(t.fbargainingNum) from TCustomerBargaining t where 1=1")
	String getMaxPointCode();
	
	@Query("select t from TCustomerBargaining t where t.fbargainingId = ?1 and t.fdefaultLevel = ?2 and t.fstatus <999")
	List<TCustomerBargaining> getByBargainingId(String fBargainingId,int level);
}