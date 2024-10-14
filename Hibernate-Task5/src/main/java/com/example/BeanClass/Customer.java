package com.example.BeanClass;

public class Customer {
	private int id;
	private String name;
	private int age;
	private long mobileNumber;
	private String emailId;
	private String location;
	private String Gender;

	public Customer(){

	}

	public Customer(String name, int age, long mobileNumber, String emailId, String location, String gender) {
		this.name = name;
		this.age = age;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.location = location;
		this.Gender = gender;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

}
