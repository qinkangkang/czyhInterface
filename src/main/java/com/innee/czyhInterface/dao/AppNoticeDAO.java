package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TAppIndexItem;
import com.innee.czyhInterface.entity.TAppNotice;
import com.innee.czyhInterface.entity.TOrder;

public interface AppNoticeDAO extends JpaRepository<TAppNotice, String>, JpaSpecificationExecutor<TAppNotice> {

	@Query("select t from TAppNotice t order by forder")
	List<TAppNotice> findOrderBy();
}