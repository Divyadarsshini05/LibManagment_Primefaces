package com.example.library.bean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.library.dao.BookDao;
import com.example.library.dao.IssueLogDao;
import com.example.library.dao.UserDao;
import com.example.library.model.Book;
import com.example.library.model.User;

@Component("bookBean")
@ViewScoped
public class BookBean implements Serializable {

	@Autowired
	private BookDao bookDao;
	@Autowired
	private UserDao userDao;

	@Autowired
	private IssueLogDao issueLogDao;
	private boolean showIssueDropdown = false;
	private Long selectedUserId;
	private List<User> users;

	public boolean isShowIssueDropdown() {
		return showIssueDropdown;
	}

	public void setShowIssueDropdown(boolean showIssueDropdown) {
		this.showIssueDropdown = showIssueDropdown;
	}

	private Book book = new Book();
	private Book b = new Book();
	private List<Book> books;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getB() {
		return b;
	}

	public void setB(Book b) {
		this.b = b;
	}

	public Long getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(Long selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@PostConstruct
	public void init() {

		FacesContext fc = FacesContext.getCurrentInstance();

		users = userDao.findAll(); // 👈 ADD THIS

		String filter = fc.getExternalContext().getRequestParameterMap().get("filter");

		if ("issued".equals(filter)) {
			books = bookDao.getIssuedBooks();
		} else if ("available".equals(filter)) {
			books = bookDao.getAvailableBooks();
		} else {
			books = bookDao.getAllBooks();
		}

		String id = fc.getExternalContext().getRequestParameterMap().get("id");
		if (id != null) {
			b = bookDao.findById(Integer.parseInt(id));
		}
	}

	public List<Book> getBooks() {
		return books;
	}

	public String updBook(int id, String bname, String aname) {
		try {
			bookDao.updateBook(id, bname, aname);
			return "details?faces-redirect=true&id=" + id;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update book."));
			return null;
		}

	}

	public String delBook(int id) {
		bookDao.deleteBook(id);
		return "allbook?faces-redirect=true";
	}

	public void save() {
		try {
			bookDao.insertBook(book.getBname(), book.getAname());

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Book saved successfully!"));

			book = new Book();
			books = bookDao.getAllBooks();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save book."));
		}
	}

	public String issueBook(int bookId) {

		User user = userDao.findAll().stream().filter(u -> u.getId().equals(selectedUserId)).findFirst().orElse(null);

		if (user == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "User not found"));
			return null;
		}

		// 1. Mark book as issued
		bookDao.updateBookStatus(bookId, true);

		// 2. Insert into issue_log
		issueLogDao.logIssue(user.getId(), user.getUsername(), b.getId(), b.getBname());

		// 3. Refresh state
		// 3. Reset UI state
		showIssueDropdown = false;
		selectedUserId = null;
		books = bookDao.getAllBooks();

		return "details?faces-redirect=true&id=" + bookId;
	}

	public String returnBook(int id) {

		bookDao.updateBookStatus(id, false);
		issueLogDao.logReturn(id);

		b.setIssued(false);
		books = bookDao.getAllBooks();

		return "details?faces-redirect=true&id=" + id;
	}

	public void prepareIssue() {
		showIssueDropdown = true;
	}

}
