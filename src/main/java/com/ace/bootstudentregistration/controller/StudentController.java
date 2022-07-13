package com.ace.bootstudentregistration.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ace.bootstudentregistration.mapper.CourseMapper;
import com.ace.bootstudentregistration.mapper.StudentMapper;
import com.ace.bootstudentregistration.model.CourseBean;
import com.ace.bootstudentregistration.model.StudentBean;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentMapper studentDao;

	@Autowired
	private CourseMapper courseDao;

	@GetMapping("/studentManagement")
	public String studentManagement(ModelMap model) {
		List<StudentBean> studentList = studentDao.selectAllStudents();
		List<List<CourseBean>> coursesLists = new ArrayList<>();
		for (StudentBean student : studentList) {
			List<CourseBean> courseList = courseDao.selectCoursesByStudentId(student.getId());
			coursesLists.add(courseList);
		}
		model.addAttribute("studentList", studentList);
		model.addAttribute("coursesLists", coursesLists);
		return "STU003";
	}

	@GetMapping("/addStudent")
	public String setupAddStudent(ModelMap model) {
		List<CourseBean> courseList = courseDao.selectAllCourses();
		model.addAttribute("courseList", courseList);
		model.addAttribute("data", new StudentBean());
		return "STU001";
	}

	@PostMapping("/addStudent")
	public String addStudent(@ModelAttribute("data") StudentBean studentBean, ModelMap model) {
		List<CourseBean> courseList = courseDao.selectAllCourses();
		model.addAttribute("courseList", courseList);
		if (studentBean.getAttendCourses().size() == 0) {
			model.addAttribute("error", "Fill the blank !!");
			model.addAttribute("data", studentBean);
			return "STU001";
		}
		if (studentBean.getName().isBlank() || studentBean.getDob().isBlank() || studentBean.getGender().isBlank()
				|| studentBean.getPhone().isBlank() || studentBean.getEducation().isBlank()) {
			model.addAttribute("error", "Fill the blank !!");
			model.addAttribute("data", studentBean);
			return "STU001";
		}
		List<StudentBean> studentList = studentDao.selectAllStudents();
		if (studentList == null) {
			studentList = new ArrayList<>();
		}
		if (studentList.size() == 0) {
			studentBean.setId("STU001");
		} else {
			int tempId = Integer.parseInt(studentList.get(studentList.size() - 1).getId().substring(3)) + 1;
			String userId = String.format("STU%03d", tempId);
			studentBean.setId(userId);
		}
		studentDao.insertStudent(studentBean);
		String[] attendCourses = new String[studentBean.getAttendCourses().size()];
		attendCourses = studentBean.getAttendCourses().toArray(attendCourses);
		for (int i = 0; i < attendCourses.length; i++) {
			studentDao.insertStudentCourse(attendCourses[i], studentBean.getId());
		}
		model.addAttribute("message", "Registered Succesfully !!");
		// clear the bean
		model.addAttribute("data", new StudentBean());
		return "STU001";
	}

	@GetMapping("/seeMore/{id}")
	public String seeMore(@PathVariable("id") String id, ModelMap model) {
		StudentBean student = studentDao.selectStudentById(id);
		List<CourseBean> attendCourses = courseDao.selectCoursesByStudentId(id);
		for (CourseBean course : attendCourses) {
			student.addAttendCourse(course);
		}
		List<CourseBean> courses = courseDao.selectAllCourses();
		model.addAttribute("courseList", courses);
		model.addAttribute("data", student);
		return "STU002";
	}

	@PostMapping("/updateStudent")
	public String updateStudent(@ModelAttribute("data") StudentBean studentBean, ModelMap model) {
		List<CourseBean> courseList = courseDao.selectAllCourses();
		model.addAttribute("courseList", courseList);
		if (studentBean.getAttendCourses().size() == 0) {
			model.addAttribute("error", "Fill the blank !!");
			model.addAttribute("data", studentBean);
			return "STU002";
		}
		if (studentBean.getName().isBlank() || studentBean.getDob().isBlank() || studentBean.getGender().isBlank()
				|| studentBean.getPhone().length() < 4 || studentBean.getEducation().isBlank()) {
			model.addAttribute("error", "Fill the blank !!");
			model.addAttribute("data", studentBean);
			return "STU002";
		}
		studentDao.updateStudent(studentBean);
		studentDao.deleteAttendCoursesByStudentId(studentBean.getId());
		String[] attendCourses = new String[studentBean.getAttendCourses().size()];
		attendCourses = studentBean.getAttendCourses().toArray(attendCourses);
		for (int i = 0; i < attendCourses.length; i++) {
			studentDao.insertStudentCourse(attendCourses[i], studentBean.getId());
		}
		return "redirect:/student/studentManagement";
	}

	@GetMapping("/deleteStudent/{id}")
	public String deleteStudent(@PathVariable("id") String id) {
		// you have to delete from transition table first and then from student table
		studentDao.deleteAttendCoursesByStudentId(id);
		studentDao.deleteStudentById(id);
		return "redirect:/student/studentManagement";
	}

	@GetMapping("/searchStudent")
	public String searchStudent(@RequestParam("id") String searchId, @RequestParam("name") String searchName,
			@RequestParam("course") String searchCourse, ModelMap model) {
		// ")#<>(}" <- this is just random bullshit to avoid sql wildcard, not REGEX
		String id = searchId.isBlank() ? ")#<>(}" : "%" + searchId + "%";
		String name = searchName.isBlank() ? ")#<>(}" : "%" + searchName + "%";
		String course = searchCourse.isBlank() ? ")#<>(}" : "%" + searchCourse + "%";

		List<StudentBean> studentList = studentDao.selectStudentListByIdOrNameOrCourse(id, name, course);
		List<List<CourseBean>> coursesLists = new ArrayList<>();
		for (StudentBean student : studentList) {
			List<CourseBean> courseList = courseDao.selectCoursesByStudentId(student.getId());
			coursesLists.add(courseList);
		}
		if (studentList.size() == 0) {
			studentList = studentDao.selectAllStudents();
			List<List<CourseBean>> coursesList = new ArrayList<>();
			for (StudentBean student : studentList) {
				List<CourseBean> courseList = courseDao.selectCoursesByStudentId(student.getId());
				coursesList.add(courseList);
			}
			model.addAttribute("studentList", studentList);
			model.addAttribute("coursesLists", coursesList);
			return "STU003";
		}
		model.addAttribute("studentList", studentList);
		model.addAttribute("coursesLists", coursesLists);
		return "STU003";
	}

}
