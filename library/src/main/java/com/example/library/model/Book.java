package com.example.library.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.dao.BookDao;

public class Book {
	private int id;
	private String bname;
	private String aname;
	private boolean issued;

	public boolean isIssued() {
		return issued;
	}

	public void setIssued(boolean issued) {
		this.issued = issued;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

}
