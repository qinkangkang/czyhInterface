package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TGoodsTypeClassCategory;

public interface GoodsTypeClassCategoryDAO
		extends JpaRepository<TGoodsTypeClassCategory, Long>, JpaSpecificationExecutor<TGoodsTypeClassCategory> {

}