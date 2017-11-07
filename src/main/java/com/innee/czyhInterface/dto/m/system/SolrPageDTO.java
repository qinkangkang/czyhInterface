package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;

import org.apache.solr.common.SolrDocumentList;

public class SolrPageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int pageSize = 10;
	
	private int offset = 0;
	
	private long totalCount = 0;
	
	private SolrDocumentList SolrDocumentList;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public SolrDocumentList getSolrDocumentList() {
		return SolrDocumentList;
	}

	public void setSolrDocumentList(SolrDocumentList solrDocumentList) {
		SolrDocumentList = solrDocumentList;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
}