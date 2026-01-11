package com.example.library.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.library.model.Book;

@Repository
public class BookDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Book> getAllBooks() {
		String sql = "SELECT id, bname, aname,issued FROM books";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Book b = new Book();
			b.setId(rs.getInt("id"));
			b.setBname(rs.getString("bname"));
			b.setAname(rs.getString("aname"));
			b.setIssued(rs.getBoolean("issued"));
			return b;
		});
	}

	public Book findById(int id) {
		String sql = "SELECT id, bname, aname,issued FROM books WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, i) -> {
			Book b = new Book();
			b.setId(rs.getInt("id"));
			b.setBname(rs.getString("bname"));
			b.setAname(rs.getString("aname"));
			b.setIssued(rs.getBoolean("issued"));
			return b;
		});
	}

	public void insertBook(String bname, String aname) {
		String sql = "INSERT INTO books (bname,aname) VALUES (?,?)";
		jdbcTemplate.update(sql, bname, aname);
	}

	public void updateBook(int id, String bname, String aname) {
		String sql = "UPDATE books SET bname = ?, aname = ? WHERE id = ?";
		jdbcTemplate.update(sql, bname, aname, id);
	}

	public void deleteBook(int id) {
		String sql = "DELETE FROM books WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	public void updateBookStatus(int id, boolean issued) {
		String sql = "UPDATE books SET issued = ? WHERE id = ?";
		jdbcTemplate.update(sql, issued, id);
	}

	public List<Book> getIssuedBooks() {
		String sql = "SELECT id, bname, aname, issued FROM books WHERE issued = TRUE";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Book b = new Book();
			b.setId(rs.getInt("id"));
			b.setBname(rs.getString("bname"));
			b.setAname(rs.getString("aname"));
			b.setIssued(rs.getBoolean("issued"));
			return b;
		});
	}

	public List<Book> getAvailableBooks() {
		String sql = "SELECT id, bname, aname, issued FROM books WHERE issued = FALSE";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Book b = new Book();
			b.setId(rs.getInt("id"));
			b.setBname(rs.getString("bname"));
			b.setAname(rs.getString("aname"));
			b.setIssued(rs.getBoolean("issued"));
			return b;
		});
	}

}
