package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TSponsorBalance;

public interface SponsorBalanceDAO
		extends JpaRepository<TSponsorBalance, String>, JpaSpecificationExecutor<TSponsorBalance> {

}