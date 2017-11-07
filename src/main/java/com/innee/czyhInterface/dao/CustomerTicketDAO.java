package com.innee.czyhInterface.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerTicket;

public interface CustomerTicketDAO
		extends JpaRepository<TCustomerTicket, String>, JpaSpecificationExecutor<TCustomerTicket> {

	TCustomerTicket getByFcustomerIdAndFtype(String fcustomerId, Integer ftype);

	TCustomerTicket getByFticketAndFtype(String fticket, Integer ftype);

	@Modifying
	@Query("delete TCustomerTicket t where t.fcustomerId = ?1 and t.ftype = ?2")
	void clearTicket(String customerId, Integer type);

	@Modifying
	@Query("update TCustomerTicket t set t.fticket = ?1, t.fupdateTime = now() where t.id = ?2")
	void updateTicket(String ticket, String id);

	@Modifying
	@Query("delete TCustomerTicket t where t.fcustomerId = ?1")
	void deleteAllTicket(String customerId);

	@Modifying
	@Query("delete TCustomerTicket t where t.fcustomerId = ?1 and t.ftype = ?2")
	void deleteTicket(String customerId, Integer type);

	@Modifying
	@Query("delete TCustomerTicket t where t.fupdateTime < ?1")
	int deleteCustomertTicketByOverdue(Date overdue);
}