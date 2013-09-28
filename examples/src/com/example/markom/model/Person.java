package com.example.markom.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Person {

	@SerializedName("name")
	private String name;

	@SerializedName("is_male")
	private boolean isMale;

	@SerializedName("age")
	private int age;

	@SerializedName("saldo")
	private float saldo;

	@SerializedName("timestamp")
	private long timestamp;

	@SerializedName("friends")
	private List<Person> friends;

	// Empty constuctor as needed by GSON
	public Person() {
		this.name = "";
		this.isMale = false;
		this.age = -1;
		this.saldo = 1.f;
		this.timestamp = 0;
		this.friends = new ArrayList<Person>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public float getSaldo() {
		return saldo;
	}

	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public List<Person> getFriends() {
		return friends;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}

}