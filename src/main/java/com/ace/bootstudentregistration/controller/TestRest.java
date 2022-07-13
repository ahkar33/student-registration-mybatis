package com.ace.bootstudentregistration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ace.bootstudentregistration.mapper.CourseMapper;
import com.ace.bootstudentregistration.mapper.UserMapper;
import com.ace.bootstudentregistration.model.CourseBean;
import com.ace.bootstudentregistration.model.UserBean;

@RestController
@RequestMapping("/api")
public class TestRest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/findAllUsers")
    public List<UserBean> findAllUsers() {
        return userMapper.selectAllUsers();
    }

    @GetMapping("/checkLogin")
    public String checkLogin() {
        if (userMapper.checkLogin("admin@gmail.com", "qwer") == true) {
            return "true";
        } else {
            return "false";
        }
    }

    @GetMapping("/selectUserByEmail/{email}")
    public UserBean selectUserByEmail(@PathVariable("email") String email) {
        return userMapper.selectUserByEmail(email);
    }

    @GetMapping("/selectUserById/{id}")
    public UserBean selectUserById(@PathVariable("id") String id) {
        return userMapper.selectUserById(id);
    }

    @GetMapping("/checkEmail/{email}")
    public Boolean checkEmail(@PathVariable("email") String email) {
        return userMapper.checkEmailExists(email);
    }

    @GetMapping("/insertUser")
    public void insertUser() {
        UserBean user = new UserBean("paul@gmail.com", "paul", "asdf", "asdf", "User");
        user.setId("2323");
        userMapper.insertUser(user);
    }

    @GetMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userMapper.deleteUserById(id);
    }

    @GetMapping("/updateUser")
    public void updateUser() {
        UserBean user = new UserBean("paul@gmail.com", "Paul", "spice", "asdf", "User");
        user.setId("2323");
        userMapper.updateUser(user);
    }

    @GetMapping("/findUser/{id}/{name}")
    public List<UserBean> findUser(@PathVariable("id") String id, @PathVariable("name") String name) {
        return userMapper.selectUserListByIdOrName(id, name);
    }

    @GetMapping("/checkCourseName/{name}")
    public Boolean checkCoureseName(@PathVariable("name") String name) {
        return courseMapper.checkCourseName(name);
    }

    @GetMapping("/findAllCourses")
    public List<CourseBean> findAllCourses() {
        return courseMapper.selectAllCourses();
    }

    @GetMapping("/addCourse")
    public void addCourse() {
        CourseBean course = new CourseBean("2323", "HolyC");
        courseMapper.insertCourse(course);
    }

    @GetMapping("/findCoursesByStudentId/{id}")
    public List<CourseBean> findCoursesByStudentId(@PathVariable("id") String id) {
        return courseMapper.selectCoursesByStudentId(id);
    }

}
