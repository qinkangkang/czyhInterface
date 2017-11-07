package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

public class PageDTO implements Serializable {

	public PageDTO(long totalCount, int pageSize, int offset) {
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.offset = offset;

		this.totalPage = Long.valueOf((this.totalCount + this.pageSize - 1) / this.pageSize).intValue();
		if (this.pageNum > totalPage) {
			this.pageNum = totalPage;
		}

		if (this.offset == 0 || (this.offset - this.pageSize) < 0) {
			this.pageNum = 1;
			this.prevOffset = 0;
		} else {
			this.pageNum = (this.offset / pageSize) + 1;
			this.prevOffset = this.offset - this.pageSize;
		}

		if ((this.offset + this.pageSize) >= this.totalCount) {
			this.nextOffset = this.offset;
		} else {
			this.nextOffset = this.offset + this.pageSize;
		}
	}

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_PAGE_SIZE = 10;

	private long totalCount = 0;

	private int totalPage = 1;

	private int offset = 0;

	private int prevOffset = 0;

	private int nextOffset = 0;

	private int pageNum = 1;

	private int pageSize = DEFAULT_PAGE_SIZE;

	public int getPageNum() {
		return pageNum;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public boolean isFristPage() {
		return pageNum == 1;
	}

	public boolean isLastPage() {
		return pageNum >= totalPage;
	}

	public int getPrevOffset() {
		return prevOffset;
	}

	public int getNextOffset() {
		return nextOffset;
	}

}
