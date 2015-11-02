package com.zhima.opendoor.entity;

import java.util.List;

public class DiscussList {
	private Discuss main;
	private List<Discuss> other;
	public Discuss getMain() {
		return main;
	}
	public void setMain(Discuss main) {
		this.main = main;
	}
	public List<Discuss> getOther() {
		return other;
	}
	public void setOther(List<Discuss> other) {
		this.other = other;
	}

}
