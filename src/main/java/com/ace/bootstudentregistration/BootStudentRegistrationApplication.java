package com.ace.bootstudentregistration;

import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import com.ace.bootstudentregistration.model.CourseBean;
import com.ace.bootstudentregistration.model.StudentBean;
import com.ace.bootstudentregistration.model.UserBean;

@MappedTypes({UserBean.class, CourseBean.class, StudentBean.class})
@MapperScan("com.ace.bootstudentregistration.mapper")
@SpringBootApplication
@ServletComponentScan
public class BootStudentRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootStudentRegistrationApplication.class, args);
	}

}
