package com.example.markom.http.schema;

import com.google.gson.annotations.SerializedName;
import com.markom.android.http.model.ServiceResponse;

public class CustomServiceResponse<T> extends ServiceResponse<T> {

	@SerializedName("meta")
	private Meta meta;

	@SerializedName("pagination")
	private Pagination pagination;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}