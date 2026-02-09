package com.example.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.library.model.User;

import java.util.List;

@Repository
public class UserDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(User user) {
		String sql = "INSERT INTO users (username, pass, fullname, role) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getUsername(), user.getPass(), user.getFullname(), user.getRole());
	}

	public User findByUsername(String username) {

		String sql = "SELECT * FROM users WHERE username = ?";

		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { username }, (rs, rowNum) -> {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setUsername(rs.getString("username"));
				user.setPass(rs.getString("pass"));
				user.setFullname(rs.getString("fullname"));
				user.setRole(rs.getString("role"));
				return user;
			});
		} catch (EmptyResultDataAccessException e) {
			return null; // IMPORTANT: user not found
		}
	}

	public List<User> findAll() {
		String sql = "SELECT * FROM users";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
		    User user = new User();
		    user.setId(rs.getLong("id"));
		    user.setUsername(rs.getString("username"));
		    user.setPass(rs.getString("pass"));
		    user.setFullname(rs.getString("fullname"));
		    user.setRole(rs.getString("role"));
		    return user;
		});
	}
	public User findById(Long id) {
	    String sql = "SELECT * FROM users WHERE id = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
			User user = new User();
			user.setId(rs.getLong("id"));
			user.setUsername(rs.getString("username"));
			user.setPass(rs.getString("pass"));
			user.setFullname(rs.getString("fullname"));
			user.setRole(rs.getString("role"));
			return user;
	});
	}
}


