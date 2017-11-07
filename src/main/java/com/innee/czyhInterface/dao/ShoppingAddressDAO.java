package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TShoppingAddress;

import java.util.Date;
import java.util.List;

public interface ShoppingAddressDAO
		extends JpaRepository<TShoppingAddress, String>, JpaSpecificationExecutor<TShoppingAddress> {

	@Modifying
	@Query("update TShoppingAddress t set t.fstauts = ?3 where t.id = ?1 and t.fcustomerId = ?2")
	void delAddress(String id, String customerId, int fstauts);

	@Query("select t from TShoppingAddress t where t.fcustomerId = ?1 and t.fdefault = 0 and t.fstauts < 999")
	TShoppingAddress findByDefault(String customerId);

	@Query("select t from TShoppingAddress t where t.fcustomerId = ?1  and t.fstauts < 999")
	List<TShoppingAddress> addressList(String customerId);

	@Modifying
	@Query("update TShoppingAddress t set t.fdefault = ?3 where t.id = ?1 and t.fcustomerId = ?2")
	void updateAddress(String id, String customerId, int fdefault);
	
	@Modifying
	@Query("update TShoppingAddress t set t.fcustomerId = ?1 where t.fcustomerId = ?2")
	void updateByCustomerId(String id2, String id);
}