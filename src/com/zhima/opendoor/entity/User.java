package com.zhima.opendoor.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName="user")
public class User {
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public long getCur_time() {
		return cur_time;
	}
	public void setCur_time(long cur_time) {
		this.cur_time = cur_time;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@DatabaseField(columnName="score")
	private int score;
	@DatabaseField(columnName="user_name")
	private String user_name;
	@DatabaseField(columnName="user_id")
	private String user_id;
	@DatabaseField(columnName="head_img")
	private String head_img;
	@DatabaseField(columnName="role")
	private int role;
	@DatabaseField(columnName="cur_time")
	private long cur_time;
	@DatabaseField(columnName="create_time")
	private long create_time;
	@DatabaseField(columnName="password")
	private String password;
	@DatabaseField(generatedId=true)
	private int id;
	public int getFavor() {
		return favor;
	}
	public void setFavor(int favor) {
		this.favor = favor;
	}
	public int getCollection() {
		return collection;
	}
	public void setCollection(int collection) {
		this.collection = collection;
	}
	public int getFocus() {
		return focus;
	}
	public void setFocus(int focus) {
		this.focus = focus;
	}
	public int getBefocus() {
		return befocus;
	}
	public void setBefocus(int befocus) {
		this.befocus = befocus;
	}
	@DatabaseField(columnName="favor")
	private int favor;
	@DatabaseField(columnName="collection")
	private int collection;
	@DatabaseField(columnName="focus")
	private int focus;
	@DatabaseField(columnName="befocus")
	private int befocus;
	@DatabaseField(columnName="isFocus")
	private int isFocus;
	public int getIsFocus() {
		return isFocus;
	}
	public void setIsFocus(int isFocus) {
		this.isFocus = isFocus;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUser_desc() {
		return user_desc;
	}
	public void setUser_desc(String jieshao) {
		this.user_desc = jieshao;
	}
	@DatabaseField(columnName="sex")
	private String sex;
	@DatabaseField(columnName="user_desc")
	private String user_desc;

}
