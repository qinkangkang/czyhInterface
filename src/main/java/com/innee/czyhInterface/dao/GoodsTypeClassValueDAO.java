package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TGoodsTypeClassValue;

public interface GoodsTypeClassValueDAO
		extends JpaRepository<TGoodsTypeClassValue, Long>, JpaSpecificationExecutor<TGoodsTypeClassValue> {

	@Query("select t from TGoodsTypeClassValue t where t.id = ?1 ")
	TGoodsTypeClassValue findGoodsTypeClassValue(Long id);

}