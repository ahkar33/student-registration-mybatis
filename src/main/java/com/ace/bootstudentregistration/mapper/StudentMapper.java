package com.ace.bootstudentregistration.mapper;

import java.util.List;

import javax.websocket.server.PathParam;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ace.bootstudentregistration.model.StudentBean;

@Mapper
public interface StudentMapper {

    @Select("select * from student")
    List<StudentBean> selectAllStudents();

    @Insert("insert into student values(#{id}, #{name}, #{dob}, #{gender}, #{phone}, #{education})")
    void insertStudent(StudentBean student);

    @Delete("delete from student where id=#{id}")
    void deleteStudentById(@PathParam("id") String id);

    @Insert("insert into student_course (course_id, student_id) values(#{course_id}, #{student_id})")
    void insertStudentCourse(@PathParam("course_id") String course_id, @PathParam("student_id") String student_id);

    @Select("select * from student where id=#{id}")
    StudentBean selectStudentById(@PathParam("id") String id);

    @Update("update student set id=#{id}, name=#{name}, dob=#{dob}, gender=#{gender}, phone=#{phone}, education=#{education} where id=#{id}")
    void updateStudent(StudentBean student);

    @Update("update student_course set course_id=#{course_id} where student_id=#{student_id}")
    void updateStudentCourse(@PathParam("course_id") String course_id, @PathParam("student_id") String student_id);

    @Delete("delete from student_course where student_id=#{id}")
    void deleteAttendCoursesByStudentId(@PathParam("id") String id);

    @Select("select distinct student.id, student.name from student_course join student on student_course.student_id = student.id join course on student_course.course_id = course.id where student.id like #{id} or student.name like #{name} or course.name like #{course}")
    List<StudentBean> selectStudentListByIdOrNameOrCourse(
        @PathParam("id") String id,
        @PathParam("name") String name,
        @PathParam("course") String course
    );

}
