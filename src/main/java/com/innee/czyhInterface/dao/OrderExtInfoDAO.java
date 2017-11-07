package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TOrderExtInfo;

public interface OrderExtInfoDAO extends JpaRepository<TOrderExtInfo, String>, JpaSpecificationExecutor<TOrderExtInfo> {

	@Query("select t from TOrderExtInfo t where t.forderId = ?1 order by t.forder")
	List<TOrderExtInfo> findByOrderId(String orderId);
}