package com.ssafy.yamyam.dto;

import java.time.LocalDateTime;

public class User {
	private String userId;
	private String email;
	private String password;
	private String name;
	private String gender;
	private double height;
	private double weight;
	private int age;
	private String specialNote; // 체중감량, 유지, 근육량 증가 등
	private String activityLevel; // 활동 수준 추가 (예: low, moderate, high)
	private LocalDateTime createdAt;

	public User() {
	}

	public User(String userId, String email, String password, String name, String gender,
				double height, double weight, int age, String specialNote, String activityLevel, LocalDateTime createdAt) {
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.height = height;
		this.weight = weight;
		this.age = age;
		this.specialNote = specialNote;
		this.activityLevel = activityLevel;
		this.createdAt = createdAt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSpecialNote() {
		return specialNote;
	}

	public void setSpecialNote(String specialNote) {
		this.specialNote = specialNote;
	}

	public String getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(String activityLevel) {
		this.activityLevel = activityLevel;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId='" + userId + '\'' +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", gender='" + gender + '\'' +
				", height=" + height +
				", weight=" + weight +
				", age=" + age +
				", specialNote='" + specialNote + '\'' +
				", activityLevel='" + activityLevel + '\'' +
				'}';
	}
}
