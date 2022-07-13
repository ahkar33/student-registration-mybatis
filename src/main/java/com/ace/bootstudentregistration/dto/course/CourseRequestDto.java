package com.ace.bootstudentregistration.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseRequestDto {
   	private String id;
	private String name;

	public CourseRequestDto(String name) {
		super();
		this.name = name;
	}    
}
