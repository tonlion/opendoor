package com.zhima.opendoor.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Module implements Parcelable{
	public int getModule_id() {
		return module_id;
	}
	public void setModule_id(int module_id) {
		this.module_id = module_id;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getModule_admin() {
		return module_admin;
	}
	public void setModule_admin(String module_admin) {
		this.module_admin = module_admin;
	}
	public String getModule_content() {
		return module_content;
	}
	public void setModule_content(String module_content) {
		this.module_content = module_content;
	}
	public int getAttentions() {
		return attentions;
	}
	public void setAttentions(int attentions) {
		this.attentions = attentions;
	}
	public int getArticle_num() {
		return article_num;
	}
	public void setArticle_num(int article_num) {
		this.article_num = article_num;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	private int module_id;
	private String module_name;
	private String module_admin;
	private String module_content;
	private int attentions;
	private int article_num;
	private long time;
	private int isFollowed;
	private String img;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getIsFollowed() {
		return isFollowed;
	}
	public void setIsFollowed(int isFollowed) {
		this.isFollowed = isFollowed;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	public Module(int module_id, String module_name, String module_admin,
			String module_content, int attentions, int article_num, long time,String img) {
		super();
		this.module_id = module_id;
		this.module_name = module_name;
		this.module_admin = module_admin;
		this.module_content = module_content;
		this.attentions = attentions;
		this.article_num = article_num;
		this.time = time;
		this.img=img;
	}
	public Module() {
		super();
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(module_id);
		arg0.writeString(module_name);
		arg0.writeString(module_admin);
		arg0.writeString(module_content);
		arg0.writeInt(attentions);
		arg0.writeInt(article_num);
		arg0.writeLong(time);
		arg0.writeInt(isFollowed);
		arg0.writeString(img);
	}
	public static final Parcelable.Creator<Module> CREATOR = new Creator<Module>() {

		@Override
		public Module createFromParcel(Parcel arg0) {
			Module sc=new Module();
		sc.setModule_id(arg0.readInt());
		sc.setModule_name(arg0.readString());
		sc.setModule_admin(arg0.readString());
		sc.setModule_content(arg0.readString());
		sc.setAttentions(arg0.readInt());
		sc.setArticle_num(arg0.readInt());
		sc.setTime(arg0.readLong());
		sc.setIsFollowed(arg0.readInt());
		sc.setImg(arg0.readString());
			return sc;
		}

		@Override
		public Module[] newArray(int arg0) {

			return new Module[arg0];
		}

	};

}
