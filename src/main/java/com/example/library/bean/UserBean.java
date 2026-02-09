package com.example.library.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.library.dao.IssueLogDao;
import com.example.library.dao.UserDao;
import com.example.library.model.Book;
import com.example.library.model.User;

@Component("userBean")
@SessionScoped
public class UserBean implements Serializable {

	private User user = new User();
	private String username;
	private String password;

	// Issued / Returned books
	private List<Book> books;

	@Autowired
	private UserDao userDao;

	@Autowired
	private IssueLogDao issueLogDao;

	public void register() {
		try {
			userDao.save(user);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success", "User registered!"));
			user = new User();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error", "Username exists!"));
		}
	}

	public String login() {
		User u = userDao.findByUsername(username);

		if (u != null && u.getPass().equals(password)) {
			user = u;
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u);

			if ("ADMIN".equals(u.getRole())) {
				return "/admin/index.xhtml?faces-redirect=true";
			} else {
				return "/member/index.xhtml?faces-redirect=true";
			}
		}
		if (u == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "User does not exists"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password"));
		return null;
	}

	public String showIssuedBooks() {
		User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

		if (currentUser == null || !"MEMBER".equals(currentUser.getRole())) {
			return "/unauthorized?faces-redirect=true";
		}
		books = issueLogDao.getIssuedBookNames(currentUser.getId());
		return "issuedBooks?faces-redirect=true";
	}

	public String showReturnedBooks() {
		User currentUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");

		if (currentUser == null || !"MEMBER".equals(currentUser.getRole())) {
			return "/unauthorized?faces-redirect=true";
		}
		books = issueLogDao.getReturnedBookNames(currentUser.getId());
		return "returnedBooks?faces-redirect=true";
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces-redirect=true";
	}

	public User getUser() {
		return user;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
