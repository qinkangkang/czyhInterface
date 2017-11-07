package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TColumnBanner;

public interface ColumnBannerDAO extends JpaRepository<TColumnBanner, String>, JpaSpecificationExecutor<TColumnBanner> {

	@Query("select t from TColumnBanner t where t.ftag = ?1 and t.ftype = ?2 and t.fstatus < 999")
	TColumnBanner findColumnBanner(Integer tag,Integer type);

	@Query("select t from TColumnBanner t where t.fchannelId = ?1 and t.fstatus < 999")
	TColumnBanner findChannelId(String channelId);
	
	@Query("select t from TColumnBanner t where t.ftype = ?1 and t.fstatus < 999 order by t.ftag")
	List<TColumnBanner> findByType(Integer type);
}