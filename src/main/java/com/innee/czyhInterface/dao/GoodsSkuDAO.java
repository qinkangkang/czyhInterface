package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TAppChannelSetting;
import com.innee.czyhInterface.entity.TGoodsSku;

public interface GoodsSkuDAO extends JpaRepository<TGoodsSku, String>, JpaSpecificationExecutor<TGoodsSku> {

	@Query("select t from TGoodsSku t where t.fgoodsId = ?1 ")
	List<TGoodsSku> findGoodsListSku(String fgoodsId);
	
	@Modifying
	@Query("update TGoodsSku t set t.fstock = t.fstock - ?1 where t.id = ?2")
	void subtractStock(Integer count, String goodsSkuId);
	
	@Modifying
	@Query("update TGoodsSku t set t.fstock = t.fstock + ?1 where t.id = ?2")
	void backStock(Integer count, String goodsSkuId);
}