package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TPoster;


public interface PosterDAO extends JpaRepository<TPoster, String>, JpaSpecificationExecutor<TPoster> {
	
	@Query("select t from TPoster t where t.fstatus = 20")
	TPoster findPoster();
}