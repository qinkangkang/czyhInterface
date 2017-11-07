package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TOrderExtInfo;
import com.innee.czyhInterface.entity.TOrderGoods;

public interface OrderGoodsDAO extends JpaRepository<TOrderGoods, String>, JpaSpecificationExecutor<TOrderGoods> {

	@Query("select t from TOrderGoods t where t.forderId = ?1")
	List<TOrderGoods> findByOrderId(String orderId);
	
	@Modifying
	@Query("update TOrderGoods t set t.fstatus = ?1 where t.id = ?2")
	int updateCommntStatus(Integer status, String id);
}