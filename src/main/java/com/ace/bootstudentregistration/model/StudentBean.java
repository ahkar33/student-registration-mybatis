package com.ace.bootstudentregistration.model;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentBean {
    private String id;
	private String name;
	private String dob;
	private String gender;
	private String phone;
	private String education;
	private List<String> attendCourses;
	
	public void addAttendCourse(CourseBean course) {
		if(attendCourses == null) attendCourses = new ArrayList<>();
		attendCourses.add(course.getId());
	}
	
	public StudentBean(String id, String name, String dob, String gender, String phone, String education) {
		super();
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.phone = phone;
		this.education = education;
	}

	public StudentBean(String name, String dob, String gender, String phone, String education,
			List<String> attendCourses) {
		super();
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.phone = phone;
		this.education = education;
		this.attendCourses = attendCourses;
	}

	public StudentBean(String name, String dob, String gender, String phone, String education) {
		super();
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.phone = phone;
		this.education = education;
	}        
}
