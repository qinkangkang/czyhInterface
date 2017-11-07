package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCalendar;

public interface CalendarDAO extends JpaRepository<TCalendar, Long>, JpaSpecificationExecutor<TCalendar> {

	TCalendar getByEventDate(int eventDate);

	@Query("from TCalendar t where t.cityId = ?1 and (t.eventDate between ?2 and ?3)")
	List<TCalendar> findByCalendarStartAndEnd(Integer cityId, Integer startDay, Integer endDay);

	@Query("from TCalendar t where t.eventNum > 0 and t.cityId = ?1 and (t.eventDate between ?2 and ?3)")
	List<TCalendar> findByCalendarStartAndEndAndHaveNum(Integer cityId, Integer startDay, Integer endDay);

	@Modifying
	@Query("update TCalendar t set t.eventDate = ?2, t.eventNum = 0, t.updateTime = current_timestamp() where t.eventDate = ?1")
	int updateCalendarDay(int yesterday, int future);

	@Modifying
	@Query("update TCalendar t set t.eventNum = ?2, t.updateTime = current_timestamp() where t.id = ?1")
	int eventOnOffSaleToCalendar(Long id, int newCount);

}