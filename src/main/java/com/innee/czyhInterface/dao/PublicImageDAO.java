package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TPublicImages;

public interface PublicImageDAO extends JpaRepository<TPublicImages, String>, JpaSpecificationExecutor<TPublicImages> {

}