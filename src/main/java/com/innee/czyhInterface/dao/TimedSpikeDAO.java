package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.innee.czyhInterface.entity.TTimedSpike;

public interface TimedSpikeDAO extends JpaRepository<TTimedSpike, String>, JpaSpecificationExecutor<TTimedSpike> {

}