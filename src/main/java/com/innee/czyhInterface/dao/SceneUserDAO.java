package com.innee.czyhInterface.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TSceneUser;

public interface SceneUserDAO extends JpaRepository<TSceneUser, String>, JpaSpecificationExecutor<TSceneUser> {
	@Query("select t from TSceneUser t where t.fopenId = ?1 and t.fsubscribe = 1 ")
	TSceneUser findBysceneStrAndopenID(String openid);
	
	@Modifying
	@Query("update TSceneUser t set t.fsubscribe = ?2, t.funSubscribe = ?3,t.funSubscribeTime = ?4 where t.id = ?1")
	void setUnSubscribe(String id, int i, int j, Date date);
	
	@Query("select t from TSceneUser t where t.fopenId = ?1 ")
	TSceneUser findOneByOpenid(String openid);
	
	@Modifying
	@Query("update TSceneUser t set t.fsceneGps = ?1 where t.id = ?2")
	void saveGps(String gps, String string);

	@Modifying
	@Query("update TSceneUser t set t.fdelivery = ?2,t.fdeliveryTime = ?3 where t.fopenId = ?1")
	void updateDelivery(String openid, int i, Date now);
	
	@Modifying
	@Query("update TSceneUser t set t.fregister = ?2,t.fregisterTime = ?3 where t.fopenId = ?1")
	void updateRegister(String openid, int i, Date now);

}