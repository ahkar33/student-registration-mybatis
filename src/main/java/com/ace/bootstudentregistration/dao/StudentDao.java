package com.ace.bootstudentregistration.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ace.bootstudentregistration.dto.student.StudentRequestDto;
import com.ace.bootstudentregistration.dto.student.StudentResponseDto;

@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<StudentResponseDto> selectAllStudents() {
        String sql = "select * from student";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new StudentResponseDto(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("education")));
    }

    public int insertStudent(StudentRequestDto dto) {
        int result = 0;
        String sql = "insert into student values(?, ?, ?, ?, ?, ?)";
        result = jdbcTemplate.update(sql,
                dto.getId(), dto.getName(), dto.getDob(), dto.getGender(), dto.getPhone(), dto.getEducation());
        return result;
    }

    public int deleteStudentById(String id) {
        int result = 0;
        String sql = "delete from student where id=?";
        result = jdbcTemplate.update(sql, id);
        return result;
    }

    public int insertStudentCourse(String course_id, String student_id) {
        int result = 0;
        String sql = "insert into student_course (course_id, student_id) values(?, ?)";
        result = jdbcTemplate.update(sql, course_id, student_id);
        return result;
    }

    public StudentResponseDto selectStudentById(String id) {
        String sql = "select * from student where id=?";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new StudentResponseDto(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("education")),
                id);
    }

    public int updateStudent(StudentRequestDto dto) {
        int result = 0;
        String sql = "update student set id=?, name=?, dob=?, gender=?, phone=?, education=? where id=?";
        result = jdbcTemplate.update(sql,
                dto.getId(), dto.getName(), dto.getDob(), dto.getGender(), dto.getPhone(), dto.getEducation(),
                dto.getId());
        return result;
    }

    public int updateStudentCourse(String course_id, String student_id) {
        int result = 0;
        String sql = "update student_course set course_id=? where student_id=?";
        result = jdbcTemplate.update(sql, course_id, student_id);
        return result;
    }

    public int deleteAttendCoursesByStudentId(String student_id) {
        int result = 0;
        String sql = "delete from student_course where student_id=?";
        result = jdbcTemplate.update(sql, student_id);
        return result;
    }

    public List<StudentResponseDto> selectStudentListByIdOrNameOrCourse(String id, String name, String course) {
        String sql = "select distinct student.id, student.name from student_course join student on student_course.student_id = student.id join course on student_course.course_id = course.id where student.id like ? or student.name like ? or course.name like ?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new StudentResponseDto(
                        rs.getString("id"),
                        rs.getString("name")),
                "%" + id + "%", "%" + name + "%", "%" + course + "%");
    }

}
