package com.innee.czyhInterface.util;

import java.util.List;
import java.util.Map;

public class CommonPage {

	public static final int DEFAULT_PAGE_SIZE = 10;

	private long totalCount = 0;

	private int totalPage = 1;

	private int offset = 0;

	private int pageSize = DEFAULT_PAGE_SIZE;

	private List<Map<String, Object>> result = null;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
		this.totalPage = Long.valueOf((this.totalCount + this.pageSize - 1) / this.pageSize).intValue();
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<Map<String, Object>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}

}
