package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TColumnBanner;
import com.innee.czyhInterface.entity.TSeckillModule;

public interface SeckillModuleDAO
		extends JpaRepository<TSeckillModule, String>, JpaSpecificationExecutor<TSeckillModule> {
	
	@Query("select t from TSeckillModule t where t.fgoodstatus = ?1 order by t.fgoodsUpdateTime")
	List<TSeckillModule> findByStatus(Integer status);
	
	@Query("select t from TSeckillModule t where t.fgoodsId = ?1 order by t.fgoodsUpdateTime")
	TSeckillModule findByGoodsId(String goodsId);

}