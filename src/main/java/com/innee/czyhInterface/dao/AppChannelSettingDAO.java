package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TAppChannelSetting;

public interface AppChannelSettingDAO
		extends JpaRepository<TAppChannelSetting, String>, JpaSpecificationExecutor<TAppChannelSetting> {

	@Query("select t from TAppChannelSetting t where t.fcity = ?1 and t.fisVisible = 1 and t.ftype = 4")
	TAppChannelSetting getIndexSilderChannel(Integer city);
	
	@Query("select t from TAppChannelSetting t where t.fcity = ?1 and t.fisVisible = 1 and t.ftype = 7")
	TAppChannelSetting getIndexDownSilderChannel(Integer city);

	@Query("select t from TAppChannelSetting t where t.fcity = ?1 and t.fisVisible = 1 order by t.forder asc ")
	List<TAppChannelSetting> findChannelListByCityId(Integer cityId);

	@Query("select t from TAppChannelSetting t where t.fcode = ?1 and t.fcity = ?2 and t.fisVisible = ?3 ")
	TAppChannelSetting getAppChannelSetting(String code, Integer city, int fIsVisible);

	@Query("select t from TAppChannelSetting t where t.fcity = ?1 and t.fisVisible = 1 and t.ffrontType in (1,2) order by t.forder asc ")
	List<TAppChannelSetting> findChannelListByCityIdAndWebClient(Integer cityId);

	@Query("select t from TAppChannelSetting t where t.fcity = ?1 and t.fisVisible = 1 and t.ffrontType in (1,3,5) order by t.forder asc ")
	List<TAppChannelSetting> findChannelListByCityIdAndIOSClient(Integer cityId);

	@Query("select t from TAppChannelSetting t where t.fcity = ?1 and t.fisVisible = 1 and t.ffrontType in (1,4,5) order by t.forder asc ")
	List<TAppChannelSetting> findChannelListByCityIdAndAndroidClient(Integer cityId);

}