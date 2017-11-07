package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TGoodsSpaceValue;

public interface GoodsSpaceValueDAO
		extends JpaRepository<TGoodsSpaceValue, String>, JpaSpecificationExecutor<TGoodsSpaceValue> {

	@Query("select t from TGoodsSpaceValue t where t.fgoodsId = ?1 ")
	List<TGoodsSpaceValue> findGoodsList(String goodsId);
	
}