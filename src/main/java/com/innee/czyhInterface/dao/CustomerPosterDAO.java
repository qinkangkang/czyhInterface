package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TCustomerPoster;



public interface CustomerPosterDAO extends JpaRepository<TCustomerPoster, String>, JpaSpecificationExecutor<TCustomerPoster> {
	
}