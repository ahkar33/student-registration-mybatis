package com.ace.bootstudentregistration.mapper;

import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ace.bootstudentregistration.model.CourseBean;

@Mapper
public interface CourseMapper {
   
    @Select("select count(*) from course where name=#{name}")    
    Boolean checkCourseName(@PathParam("name") String name);

    @Select("select * from course")
    List<CourseBean> selectAllCourses();

    @Insert("insert into course values(#{id}, #{name})") 
    void insertCourse(CourseBean course);

    @Select("select course.id, course.name from student_course join course on student_course.course_id = course.id where student_course.student_id = #{id}")
    List<CourseBean> selectCoursesByStudentId(@PathParam("id") String id);

}
