package com.innee.czyhInterface.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerSubscribe;

public interface CustomerSubscribeDAO
		extends JpaRepository<TCustomerSubscribe, String>, JpaSpecificationExecutor<TCustomerSubscribe> {

	@Query("from TCustomerSubscribe t where t.fcustomerId=?1 and t.ftype = 1 order by t.foperationTime desc")
	List<TCustomerSubscribe> getByCustomer(String fcustomerId);

	@Query("from TCustomerSubscribe t where t.foperationId=?1 and t.ftype = ?2")
	List<TCustomerSubscribe> getByOpertionId(String foperationId, Integer type);

	@Modifying
	@Query("update TCustomerSubscribe t set t.fcustomerId = ?1 where t.fcustomerId = ?2")
	void updateCustomerSubscribeByCustomer(String id2, String id);

	@Modifying
	@Query("update TCustomerSubscribe t set t.foperationId = ?1 where t.foperationId = ?2")
	void updateCustomerSubscribeByOpertion(String id2, String id);

	@Query("from TCustomerSubscribe t where t.fcustomerId=?1 and t.ftype = 1 order by t.foperationTime asc")
	List<TCustomerSubscribe> getByOperationId(String fcustomerId);
	
	@Query("select DISTINCT t.fcustomerId from TCustomerSubscribe t")
	List<String> findtFcustomerId();
	
	@Query("from TCustomerSubscribe t where t.fcustomerId=?1 order by t.foperationTime desc")
	List<TCustomerSubscribe> findByCustomer(String fcustomerId);
	
	@Modifying
	@Query("update TCustomerSubscribe t set t.fupdateTime = ?1,t.ftype = 1,t.fcouponValue = ?2 where t.foperationId = ?3")
	void updateTypeOpertion(Date date, String couponValue, String id);

}