package com.example.markom.http.handler;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.markom.http.schema.CustomServiceResponse;
import com.example.markom.http.schema.Meta;
import com.example.markom.http.schema.Pagination;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.handler.BaseGenericHandler;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Custom generic handler that parses JSON schema assuming that schema is structured like following example:
 * <p>
 * { "meta":{ "success":true, "error_code":477, "error_type":"Service error", "error_message":"Neispravan API key",
 * "user_error_message":"Doslo je do promjena podataka, molimo vas za relog!" },
 * <p>
 * "pagination":{ "page_from":1, "page_to":1, "number_of_pages":10, "next_url":"www.getnextpage.com", "has_more":true },
 * <p>
 * "data":[ { "name":"Tina", "is_male":false, "age":21, "saldo":234.44, "timestamp":123456, "friends":[] }, {
 * "name":"Nikola", "is_male":true, "age":18, "saldo":100.44, "timestamp":4848488, "friends":[] }, { "name":"Marko",
 * "is_male":true, "age":23, "saldo":36.44, "timestamp":12343453, "friends":[ { "name":"Tina", "is_male":false,
 * "age":21, "saldo":234.44, "timestamp":123456, "friends":[] }, { "name":"Nikola", "is_male":true, "age":18,
 * "saldo":100.44, "timestamp":4848488, "friends":[] } ] } ] }
 * 
 * @author Marko Milos
 * 
 * @param <T> generic type of object to parse.
 */
public class CustomGenericHandler<T> extends BaseGenericHandler<T> {

	public CustomGenericHandler(Type type) {
		super(type);
	}

	@Override
	protected ServiceResponse<T> parseServiceResponse(String response) throws GsonParsingException {
		try {
			// Retrive json objects from JSON schema
			JSONObject jsonObject = new JSONObject(response);
			JSONObject metaObject = jsonObject.getJSONObject("meta");
			JSONObject paginationObject = jsonObject.getJSONObject("pagination");
			JSONArray dataArray = jsonObject.getJSONArray("data");

			// Use GSONParser to create object from json string
			Meta meta = GSONParser.createObjectFromResponse(Meta.class, metaObject.toString());
			Pagination pagination = GSONParser.createObjectFromResponse(Pagination.class, paginationObject.toString());
			T data = GSONParser.createObjectListFromResponse(type, dataArray.toString());

			// Setup CustomServiceResponse that extends ServiceResponse class and return the result
			CustomServiceResponse<T> customServiceResponse = new CustomServiceResponse<T>();
			customServiceResponse.setMeta(meta);
			customServiceResponse.setPagination(pagination);
			customServiceResponse.setData(data);
			return customServiceResponse;

		} catch (JSONException e) {
			throw new GsonParsingException(e.getMessage());
		}
	}

}