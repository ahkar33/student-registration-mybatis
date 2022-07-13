package com.ace.bootstudentregistration.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ace.bootstudentregistration.dto.user.UserRequestDto;
import com.ace.bootstudentregistration.dto.user.UserResponseDto;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkLogin(String email, String password) {
        String sql = "select count(*) from user where binary `email`=? && binary `password`=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        return count != null && count > 0;
    }

    public UserResponseDto selectUserByEmail(String email) {
        String sql = "select * from user where email=?";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new UserResponseDto(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("role")),
                email);
    }

    public UserResponseDto selectUserById(String id) {
        String sql = "select * from user where id=?";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new UserResponseDto(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("role")),
                id);
    }

    public List<UserResponseDto> selectAllUsers() {
        String sql = "select * from user";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new UserResponseDto(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("role")));
    }

    public boolean checkEmailExists(String email) {
        String sql = "select count(*) from user where binary `email`=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public int insertUser(UserRequestDto dto) {
        int result = 0;
        String sql = "insert into user values(?, ?, ?, ?, ?)";
        result = jdbcTemplate.update(sql,
                dto.getId(), dto.getEmail(), dto.getName(), dto.getPassword(), dto.getUserRole());
        return result;
    }

    public int deleteUserById(String id) {
        int result = 0;
        String sql = "delete from user where id=?";
        result = jdbcTemplate.update(sql, id);
        return result;
    }

    public int updateUser(UserRequestDto dto) {
        int result = 0;
        String sql = "update user set email=?, name=?, password=?, role=? where id=?";
        result = jdbcTemplate.update(sql,
                dto.getEmail(), dto.getName(), dto.getPassword(), dto.getUserRole(), dto.getId());
        return result;
    }

    public List<UserResponseDto> selectUserListByIdOrName(String id, String name) {
        String sql = "select * from user where id like ? or name like ?";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new UserResponseDto(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("role")),
                "%" + id + "%", "%" + name + "%");
    }

}
