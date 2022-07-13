package com.ace.bootstudentregistration.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ace.bootstudentregistration.dto.course.CourseRequestDto;
import com.ace.bootstudentregistration.dto.course.CourseResponseDto;

@Repository
public class CourseDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkCourseName(String name) {
        String sql = "select count(*) from course where name=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }

    public List<CourseResponseDto> selectAllCourses() {
        String sql = "select * from course";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new CourseResponseDto(
                        rs.getString("id"),
                        rs.getString("name")));
    }

    public int insertCourse(CourseRequestDto dto) {
        int result = 0;
        String sql = "insert into course values(?, ?)";
        result = jdbcTemplate.update(sql,
                dto.getId(), dto.getName());
        return result;
    }

    public List<CourseResponseDto> selectCoursesByStudentId(String id) {
        String sql = "select course.id, course.name from student_course join course on student_course.course_id = course.id where student_course.student_id = ? ";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new CourseResponseDto(
                        rs.getString("id"),
                        rs.getString("name")),
                id);
    }

}
