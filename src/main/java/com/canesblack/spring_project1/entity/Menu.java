package com.canesblack.spring_project1.entity;

public class Menu {

	private int idx;
	private String memID;
	private String title;
	private String content;
	private String writer;
	private String indate;
	private int count;
	
	
	
	public Menu() {
		
	}



	public Menu(int idx, String memID, String title, String content, String writer, String indate, int count) {
		this.idx = idx;
		this.memID = memID;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.indate = indate;
		this.count = count;
	}



	public int getIdx() {
		return idx;
	}



	public void setIdx(int idx) {
		this.idx = idx;
	}



	public String getMemID() {
		return memID;
	}



	public void setMemID(String memID) {
		this.memID = memID;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getWriter() {
		return writer;
	}



	public void setWriter(String writer) {
		this.writer = writer;
	}



	public String getIndate() {
		return indate;
	}



	public void setIndate(String indate) {
		this.indate = indate;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}
	

	
	
}
