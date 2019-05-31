package com.cafe24.mysite.vo;

import java.sql.Date;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserVO {

	private Long no;
	@NotEmpty
	@Length(min=2, max=8)
	private String name;
	@Email
	@NotEmpty
	private String email;
	private String pw;
	private String gender;
	private Date regdate;
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	@Override
	public String toString() {
		return "UserVO [no=" + no + ", name=" + name + ", email=" + email + ", pw=" + pw + ", gender=" + gender
				+ ", regdate=" + regdate + "]";
	}	
	
	
}
