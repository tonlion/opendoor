package com.zhima.opendoor.entity;

import android.os.Parcel;
import android.os.Parcelable;
public class info implements Parcelable{
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAboutMe() {
		return aboutMe;
	}
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}
	private String name;
	private String sex;
	private String aboutMe;
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(name);
		arg0.writeString(sex);
		arg0.writeString(aboutMe);

	}
	public final static Parcelable.Creator<info> CREATOR = new Creator<info>() {

		@Override
		public info createFromParcel(Parcel arg0) {
			info sc=new info();
		sc.setName(arg0.readString());
		sc.setSex(arg0.readString());
		sc.setAboutMe(arg0.readString());
		
			

			return sc;
		}

		@Override
		public info[] newArray(int arg0) {

			return new info[arg0];
		}

	};
	
}