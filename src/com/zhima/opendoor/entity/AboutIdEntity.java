package com.zhima.opendoor.entity;

public class AboutIdEntity {
	public User getU() {
		return u;
	}
	public void setU(User u) {
		this.u = u;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private User u;
	private int type;
	private boolean isSelect;
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
