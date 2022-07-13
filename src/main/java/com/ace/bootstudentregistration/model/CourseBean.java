package com.ace.bootstudentregistration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseBean {
    private String id;
	private String name;

	public CourseBean(String name) {
		super();
		this.name = name;
	}    
}
