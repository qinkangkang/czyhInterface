package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TCustomerLikesDislikes;

public interface CustomerLikesDislikesDAO
		extends JpaRepository<TCustomerLikesDislikes, String>, JpaSpecificationExecutor<TCustomerLikesDislikes> {

	@Query("from TCustomerLikesDislikes t where t.fcustomerId = ?1 and t.feventId = ?2 and t.ftype=?3")
	List<TCustomerLikesDislikes> findByCustomerIdAndEventId(String customerId, String eventId, Integer type);

}