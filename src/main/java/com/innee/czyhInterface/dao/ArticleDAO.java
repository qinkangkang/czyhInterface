package com.innee.czyhInterface.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.innee.czyhInterface.entity.TArticle;

public interface ArticleDAO extends JpaRepository<TArticle, String>, JpaSpecificationExecutor<TArticle> {

	@Modifying
	@Query("update TArticle t set t.frecommend = ?2 where t.id = ?1")
	void updateArticleRecommend(String articleId, long num);

	@Query("select t.frecommend from TArticle t where t.id = ?1")
	Long findArticleRecommend(String articleId);

	@Modifying
	@Query("update TArticle t set t.fcomment = t.fcomment + 1 where t.id = ?1")
	void addOneComment(String articleId);

}