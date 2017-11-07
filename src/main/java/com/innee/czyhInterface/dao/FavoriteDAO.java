package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TFavorite;

public interface FavoriteDAO extends JpaRepository<TFavorite, String>, JpaSpecificationExecutor<TFavorite> {

	@Query("select t from TFavorite t where t.TCustomer.id = ?1 and t.fobjectId = ?2 and t.ftype = 1")
	TFavorite getFavoriteEventByCustomerIdAndObejctId(String customerId, String goodsId);

	@Query("select t from TFavorite t where t.TCustomer.id = ?1 and t.fobjectId = ?2 and t.ftype = 2")
	TFavorite getFavoriteMerchantByCustomerIdAndObejctId(String customerId, String merchantId);

	@Query("select t from TFavorite t where t.TCustomer.id = ?1 and t.fobjectId = ?2 and t.ftype = 3")
	TFavorite getFavoriteArticleIdByCustomerIdAndObejctId(String customerId, String articleId);

	@Query("select count(t.id) from TFavorite t where t.fobjectId = ?1 and t.ftype = ?2")
	Long getFavoriteCount(String objectId, Integer type);
	
	@Query("select t from TFavorite t where t.TCustomer.id = ?1 and t.fobjectId in (?2) and  t.ftype = ?3")
	List<TFavorite> findByIds(String customerId, List ids, Integer type);
	
	@Query("select count(t.id) from TFavorite t where t.fobjectId = ?1 ")
	Long getFavoriteCount(String objectId);

}