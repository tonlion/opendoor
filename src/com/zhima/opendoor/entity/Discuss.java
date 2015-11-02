package com.zhima.opendoor.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Discuss implements Parcelable{
	public int getDiscussed_id() {
		return discussed_id;
	}
	public void setDiscussed_id(int discussed_id) {
		this.discussed_id = discussed_id;
	}
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getDiscussed_type() {
		return discussed_type;
	}
	public void setDiscussed_type(int discussed_type) {
		this.discussed_type = discussed_type;
	}
	public int getDiscussed_item_id() {
		return discussed_item_id;
	}
	public void setDiscussed_item_id(int discussed_item_id) {
		this.discussed_item_id = discussed_item_id;
	}
	private int discussed_id;
	private int article_id;
	private String user_name;
	private String content;
	private long time;
	private int discussed_type;
	private int discussed_item_id;
	private String user_id;
	private String head_img;
	private String article_title;
	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(user_name);
		arg0.writeString(content);
		arg0.writeInt(article_id);
		arg0.writeInt(discussed_id);
		arg0.writeInt(discussed_item_id);
		arg0.writeInt(discussed_type);
		arg0.writeLong(time);
		arg0.writeString(user_id);
		arg0.writeString(head_img);
		
	}
	public static final Parcelable.Creator<Discuss> CREATOR=new Creator<Discuss>() {

		@Override
		public Discuss createFromParcel(Parcel arg0) {
			Discuss discuss=new Discuss();
			discuss.setUser_name(arg0.readString());
			discuss.setContent(arg0.readString());
			discuss.setArticle_id(arg0.readInt());
			discuss.setDiscussed_id(arg0.readInt());
			discuss.setDiscussed_item_id(arg0.readInt());
			discuss.setDiscussed_type(arg0.readInt());
			discuss.setTime(arg0.readLong());
			discuss.setUser_id(arg0.readString());
			discuss.setHead_img(arg0.readString());
			
			return discuss;
		}

		@Override
		public Discuss[] newArray(int arg0) {
		
			return new Discuss[arg0];
		}
	};

}
