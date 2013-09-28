package com.markom.android.http.parser;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.markom.android.http.exceptions.GsonParsingException;

/**
 * Factory class for creating model object instances based on JSON string and Class parameter.
 * 
 * @author Marko Milos
 */
public class GSONParser {

	/**
	 * Creates an object from the JSON response and the class which the object would be mapped to.
	 * 
	 * @param clazz a class instance.
	 * @param json a JSON response.
	 * @return a object of type <T>.
	 * @throws GsonParsingException error while parsing JSON string with {@link Gson}.
	 */
	public static <T> T createObjectFromResponse(Class<T> clazz, final String json) throws GsonParsingException {
		Gson gson = new Gson();

		T object = null;
		try {
			object = gson.fromJson(json, clazz);
			return object;
		} catch (JsonSyntaxException e) {
			throw new GsonParsingException(e.getMessage());
		}
	}

	/**
	 * Creates an object list from the JSON response and the type of collection which the object would be mapped to.
	 * 
	 * @param type a type of collection to create.
	 * @param json a JSON response.
	 * @return a object of type <T>.
	 * @throws GsonParsingException error while parsing JSON string with {@link Gson}.
	 */
	public static <T> T createObjectListFromResponse(Type type, final String json) throws GsonParsingException {
		Gson gson = new Gson();

		T object = null;
		try {
			object = gson.fromJson(json, type);
			return object;
		} catch (JsonSyntaxException e) {
			throw new GsonParsingException(e.getMessage());
		}
	}

}