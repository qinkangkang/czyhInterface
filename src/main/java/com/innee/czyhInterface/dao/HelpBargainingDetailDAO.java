package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.THelpBargainingDetail;

public interface HelpBargainingDetailDAO
		extends JpaRepository<THelpBargainingDetail, String>, JpaSpecificationExecutor<THelpBargainingDetail> {

	@Query("select count(t.id) from THelpBargainingDetail t where t.fhelperId = ?1 and t.fbargainingId = ?2")
	Long getDetailByHelper(String fHelperId, String eventBargainId);

	@Query("select count(t.id) from THelpBargainingDetail t where t.fcustomerBargainingId = ?1")
	Long getDetailByBargainId(String fCustomerBargainingId);

	@Query("select count(t.id) from THelpBargainingDetail t where t.fhelperId = ?1 and t.fcustomerBargainingId = ?2")
	Long getByBargainIdAndBargainId(String fHelperId, String fbargainingId);
}