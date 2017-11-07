package com.innee.czyhInterface.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TAppChannelSetting;
import com.innee.czyhInterface.entity.TComment;

public interface CommentDAO extends JpaRepository<TComment, String>, JpaSpecificationExecutor<TComment> {

	@Query("select count(t.id) from TComment t where t.fobjectId = ?1 and t.ftype = ?2")
	Long getCommentCount(String objectId, Integer type);

	@Query("select count(t.id) from TComment t where t.fobjectId in (select e.id from TEvent e where e.TSponsor.id = ?1) and t.ftype = ?2")
	Long getMerchantCommentCount(String sponsorId, Integer type);

	@Query("select t.frecommend from TComment t where t.id = ?1")
	Long findCommentRecommend(String commentId);

	@Modifying
	@Query("update TComment t set t.frecommend = ?2 where t.id = ?1")
	void updateCommentRecommend(String commentId, long num);
	
	@Modifying
	@Query("update TComment t set t.fcustomerId = ?1 where t.fcustomerId = ?2")
	void updateCommentByCustomer(String id2, String id);
	
	@Query("select count(t.id) from TComment t where t.fobjectId = ?1 and t.ftype = 2 and t.fstatus >= 20 and t.fstatus < 999 ")
	Long findByEvent(String eventId);
	
	@Query("select t from TComment t where t.fobjectId = ?1 and t.fstatus = 20 and t.ftype = 2")
	List<TComment> findComment(String goodsId);
	
}