package com.example.markom.http.schema;

import com.google.gson.annotations.SerializedName;

public class Pagination {

	@SerializedName("page_from")
	private int pageFrom;

	@SerializedName("page_to")
	private int pageTo;

	@SerializedName("number_of_pages")
	private int numberOfPages;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("has_more")
	private boolean hasMore;

	public int getPageFrom() {
		return pageFrom;
	}

	public void setPageFrom(int pageFrom) {
		this.pageFrom = pageFrom;
	}

	public int getPageTo() {
		return pageTo;
	}

	public void setPageTo(int pageTo) {
		this.pageTo = pageTo;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

}