package com.innee.czyhInterface.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TGoodsTypeClass;

public interface GoodsTypeClassDAO
		extends JpaRepository<TGoodsTypeClass, Long>, JpaSpecificationExecutor<TGoodsTypeClass> {

	@Query("select t from TGoodsTypeClass t where t.id = ?1 ")
	TGoodsTypeClass findGoodsTypeClassId(Long id);

}