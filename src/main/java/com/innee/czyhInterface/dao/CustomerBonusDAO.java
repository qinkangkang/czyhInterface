package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerBonus;


public interface CustomerBonusDAO
		extends JpaRepository<TCustomerBonus, String>, JpaSpecificationExecutor<TCustomerBonus> {

	@Query("select t from TCustomerBonus t where t.fcustermerId = ?1 and  t.ftype = 20")
	List<TCustomerBonus> findByCustomerId(String customerId);
	
	@Query("select SUM(t.fbonus) from TCustomerBonus t where t.fcustermerId = ?1")
	String findCountBouns(String customerId);

	@Modifying
	@Query("update TCustomerBonus t set t.fcustermerId = ?1 where t.fcustermerId = ?2")
	void updateCustomerBounsByCustomer(String id2, String id);
	
	@Query("select t from TCustomerBonus t where t.fcustermerId = ?1 and  t.ftype = ?2")
	List<TCustomerBonus> findByType(String customerId,Integer type);
}