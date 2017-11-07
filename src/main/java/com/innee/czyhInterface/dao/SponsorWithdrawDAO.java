package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TSponsorWithdraw;

public interface SponsorWithdrawDAO
		extends JpaRepository<TSponsorWithdraw, String>, JpaSpecificationExecutor<TSponsorWithdraw> {

}