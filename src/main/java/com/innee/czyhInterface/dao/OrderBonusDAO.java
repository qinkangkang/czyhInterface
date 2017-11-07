package com.innee.czyhInterface.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TOrderBonus;

public interface OrderBonusDAO extends JpaRepository<TOrderBonus, String>, JpaSpecificationExecutor<TOrderBonus> {

	@Modifying
	@Query("update TOrderBonus t set t.fcustomerId = ?1 where t.fcustomerId = ?2")
	void updateOrderBounsByCustomer(String id2, String id);

	@Query("select t from TOrderBonus t where t.fcustomerId = ?1 and t.TEventBonus.id = ?2 and t.fstatus < 100")
	List<TOrderBonus> findByCunstomerAndEvent(String customerId, String eventBonusId);

	@Query("select max(t.forderNum) from TOrderBonus t where t.fcreateTime > ?1")
	String getMaxOrderNum(Date date);

	TOrderBonus getByFstatusAndForderNum(Integer status, String orderNum);

	@Modifying
	@Query("update TOrderBonus t set t.fstatus = ?1 ,t.fpayTime = now() where t.id = ?2")
	void updatePayStatus(Integer fstatus, String id);
}