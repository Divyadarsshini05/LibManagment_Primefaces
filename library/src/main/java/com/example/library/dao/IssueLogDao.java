package com.example.library.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.library.model.Book;

@Repository
public class IssueLogDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void logIssue(Long uid, String uname, int bid, String bname) {
		String sql = "INSERT INTO issue_log (uid, uname, bid, bname, issued_at)VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
		jdbcTemplate.update(sql, uid, uname, bid, bname);
	}

	public void logReturn(int bid) {
		String sql = "UPDATE issue_log SET returned_at = CURRENT_TIMESTAMP WHERE bid = ? AND returned_at IS NULL";
		jdbcTemplate.update(sql, bid);
	}

	public List<Book> getIssuedBookNames(Long uid) {
		String sql = "SELECT bname FROM issue_log WHERE uid = ? AND returned_at IS NULL";
		return jdbcTemplate.query(sql, new Object[] { uid }, (rs, rowNum) -> {
			Book b = new Book();
			b.setBname(rs.getString("bname"));
			return b;
		});
	}

	public List<Book> getReturnedBookNames(Long uid) {
		String sql = "SELECT bname FROM issue_log WHERE uid = ? AND returned_at IS NOT NULL";
		return jdbcTemplate.query(sql, new Object[] { uid }, (rs, rowNum) -> {
			Book b = new Book();
			b.setBname(rs.getString("bname"));
			return b;
		});
	}
}
