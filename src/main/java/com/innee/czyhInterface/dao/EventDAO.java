package com.innee.czyhInterface.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TEvent;

public interface EventDAO extends JpaRepository<TEvent, String>, JpaSpecificationExecutor<TEvent> {

	@Modifying
	@Query("delete TEvent t where t.fstatus is null and t.fcreateTime <= ?1")
	int clearTempEvent(Date date);

	@Query("select count(t.id) from TEvent t where t.TSponsor.id = ?1 and t.fstatus between 20 and 99")
	Long getEventCountBySponsorId(String sponsorId);

	@Query("select t from TEvent t where t.fdetailHtmlUrl is null and t.fstatus between 20 and 99")
	List<TEvent> findByFdetailHtmlUrlIsnull();

	@Modifying
	@Query("update TEvent t set t.fdetailHtmlUrl = ?2 where t.id = ?1")
	void updateFdetailHtmlUrl(String id, String fdetailHtmlUrl);

	@Modifying
	@Query("update TEvent t set t.fstatus = ?1 where t.id = ?2")
	void saveStatus(Integer status, String id);

	@Modifying
	@Query("update TEvent t set t.fonSaleTime = ?1, t.foffSaleTime = ?2 where t.id = ?3")
	void saveOOTime(Date onSaleTime, Date offSaleTime, String id);

	/*
	 * @Modifying
	 * 
	 * @Query("update TEvent t set t.fsaleFlag = (select sum(s.fstock) from TEventSpec s where s.TEvent.id = ?1 and s.fstatus < 999) where t.id = ?1"
	 * ) void updateStockFlagBySpec(String eventId);
	 */

	/*
	 * @Modifying
	 * 
	 * @Query("update TEvent t set t.fsaleFlag = t.fsaleFlag + ?1 where t.id = ?2"
	 * ) void addStock(Integer count, String eventId);
	 */

	/*
	 * @Modifying
	 * 
	 * @Query("update TEvent t set t.fsaleFlag = t.fsaleFlag - ?1 where t.id = ?2"
	 * ) void subStock(Integer count, String eventId);
	 */
	@Query("select t.frecommend from TEvent t where t.id = ?1")
	Long findEventRecommend(String eventId);

	@Modifying
	@Query("update TEvent t set t.frecommend = ?2 where t.id = ?1")
	void updateEventRecommend(String eventId, long num);

	@Modifying
	@Query("update TEvent t set t.fstock = t.fstock + ?1 ,t.fsaleTotal = t.fsaleTotal - ?1 where t.id = ?2")
	void backStock(Integer count, String eventId);

	@Modifying
	@Query("update TEvent t set t.fstock = t.fstock - ?1 ,t.fsaleTotal = t.fsaleTotal + ?1 where t.id = ?2")
	void subtractStock(Integer count, String eventId);

	@Query("select t from TEvent t where t.fstatus = 20")
	List<TEvent> findAllList();

}