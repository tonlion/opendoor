package com.zhima.opendoor.entity;
import android.os.Parcel;
import android.os.Parcelable;


public class Chanel implements Parcelable{
	public int getChanel_id() {
		return chanel_id;
	}
	public void setChanel_id(int chanel_id) {
		this.chanel_id = chanel_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	private int chanel_id;
	private String title;
	private int parent_id;
	public Chanel(int chanel_id, String title, int parent_id) {
		super();
		this.chanel_id = chanel_id;
		this.title = title;
		this.parent_id = parent_id;
	}
	public Chanel(){
		
	}
	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(chanel_id);
		arg0.writeInt(parent_id);
		arg0.writeString(title);

	}
	public static final Parcelable.Creator<Chanel> CREATOR = new Creator<Chanel>() {

		@Override
		public Chanel createFromParcel(Parcel arg0) {
			Chanel sc=new Chanel();
		sc.setChanel_id(arg0.readInt());
		sc.setParent_id(arg0.readInt());
		sc.setTitle(arg0.readString());
		
			

			return sc;
		}

		@Override
		public Chanel[] newArray(int arg0) {

			return new Chanel[arg0];
		}

	};
}
