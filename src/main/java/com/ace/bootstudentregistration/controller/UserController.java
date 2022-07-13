package com.ace.bootstudentregistration.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ace.bootstudentregistration.mapper.UserMapper;
import com.ace.bootstudentregistration.model.UserBean;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userDao;

    @GetMapping("/userManagement")
    public String showUserManagement(ModelMap model) {
        List<UserBean> userList = userDao.selectAllUsers();
        model.addAttribute("userList", userList);
        return "USR003";
    }

    @GetMapping("/addUser")
    public ModelAndView setupAdduser() {
        return new ModelAndView("USR001", "data", new UserBean());
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("data") UserBean userBean, ModelMap model) {
        if (userBean.getEmail().isBlank() || userBean.getName().isBlank() || userBean.getPassword().isBlank()
                || userBean.getConfirmPassword().isBlank() || userBean.getUserRole().isBlank()) {
            model.addAttribute("error", "Fill the blank !!");
            model.addAttribute("data", userBean);
            return "USR001";
        } else if (!userBean.getPassword().equals(userBean.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match !!");
            model.addAttribute("data", userBean);
            return "USR001";
        }
        List<UserBean> userList = userDao.selectAllUsers();
        if (userDao.checkEmailExists(userBean.getEmail())) {
            model.addAttribute("error", "Email already exists !!");
            model.addAttribute("data", userBean);
            return "USR001";
        }
        if (userList.size() == 0) {
            userBean.setId("USR001");
        } else {
            int tempId = Integer.parseInt(userList.get(userList.size() - 1).getId().substring(3)) + 1;
            String userId = String.format("USR%03d", tempId);
            userBean.setId(userId);
        }
        userDao.insertUser(userBean);
        model.addAttribute("message", "Registered Succesfully !!");
        // clear the bean
        model.addAttribute("data", new UserBean());
        return "USR001";
    }

    @GetMapping("/updateUser/{id}")
    public ModelAndView setupUpdateUser(@PathVariable("id") String id) {
        UserBean userBean = userDao.selectUserById(id);
        userBean.setConfirmPassword(userBean.getPassword());
        return new ModelAndView("USR002", "data", userBean);
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("data") UserBean userBean, ModelMap model, HttpSession session,
            HttpServletRequest req) {
        UserBean sessionBean = (UserBean) session.getAttribute("userInfo");
        if (userBean.getEmail().isBlank() || userBean.getName().isBlank() || userBean.getPassword().isBlank()
                || userBean.getConfirmPassword().isBlank() || userBean.getUserRole().isBlank()) {
            model.addAttribute("error", "Fill the blank !!");
            return "USR002";
        }
        if (!userBean.getPassword().equals(userBean.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match !!");
            return "USR002";
        }
        UserBean tempUser = userDao.selectUserById(userBean.getId());
        // UserRequestDto reqDto = new UserRequestDto(userBean.getId(),
        // userBean.getEmail(), userBean.getName(),
        // userBean.getPassword(), userBean.getUserRole());
        if (!tempUser.getEmail().equals(userBean.getEmail())) {
            if (userDao.checkEmailExists(userBean.getEmail())) {
                model.addAttribute("error", "Email already exists !!");
                return "USR002";
            }
            if (!userBean.getPassword().equals(userBean.getConfirmPassword())) {
                model.addAttribute("error", "Passwords do not match !!");
                return "USR002";
            }
            userDao.updateUser(userBean);
            if (userBean.getEmail().equals(sessionBean.getEmail())) {
                session.setAttribute("userInfo", userBean);
            }
            List<UserBean> userList = userDao.selectAllUsers();
            req.getServletContext().setAttribute("userList", userList);
            return "redirect:/user/userManagement";
        }
        userDao.updateUser(userBean);
        if (userBean.getEmail().equals(sessionBean.getEmail())) {
            session.setAttribute("userInfo", userBean);
        }
        List<UserBean> userList = userDao.selectAllUsers();
        req.getServletContext().setAttribute("userList", userList);
        return "redirect:/user/userManagement";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") String id) {
        userDao.deleteUserById(id);
        return "redirect:/user/userManagement";
    }

    @GetMapping("/searchUser")
    public String searchUser(@RequestParam("id") String searchId, @RequestParam("name") String searchName,
            ModelMap model) {
        // ")#<>(}" <- this is just random bullshit to avoid sql wildcard, not REGEX
        String id = searchId.isBlank() ? ")#<>(}" : "%" + searchId + "%";
        String name = searchName.isBlank() ? ")#<>(}" : searchName;
        List<UserBean> searchUserList = null;
        searchUserList = userDao.selectUserListByIdOrName(id, name);
        if (searchUserList.size() == 0) {
            searchUserList = userDao.selectAllUsers();
        }
        model.addAttribute("userList", searchUserList);
        return "USR003";
    }

}
