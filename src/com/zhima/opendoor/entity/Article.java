package com.zhima.opendoor.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.os.Parcel;
import android.os.Parcelable;
@DatabaseTable(tableName="article")
public class Article implements Parcelable{
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}
	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public String getArticle_content() {
		return article_content;
	}
	public void setArticle_content(String article_content) {
		this.article_content = article_content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public long getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}
	public int getFavor() {
		return favor;
	}
	public void setFavor(int favor) {
		this.favor = favor;
	}
	public int getScan() {
		return scan;
	}
	public void setScan(int scan) {
		this.scan = scan;
	}
	public int getZan() {
		return zan;
	}
	public void setZan(int zan) {
		this.zan = zan;
	}
	public int getDiscuss() {
		return discuss;
	}
	public void setDiscuss(int discuss) {
		this.discuss = discuss;
	}
	public int getModule_id() {
		return module_id;
	}
	public void setModule_id(int module_id) {
		this.module_id = module_id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@DatabaseField(columnName="article_id")
	private int article_id;
	@DatabaseField(columnName="article_title")
	private String article_title;
	@DatabaseField(columnName="article_content")
	private String article_content;
	@DatabaseField(columnName="author")
	private String author;
	@DatabaseField(columnName="create_time")
	private long create_time;
	@DatabaseField(columnName="update_time")
	private long update_time;
	@DatabaseField(columnName="favor")
	private int favor;
	@DatabaseField(columnName="scan")
	private int scan;
	@DatabaseField(columnName="zan")
	private int zan;
	@DatabaseField(columnName="discuss")
	private int discuss;
	@DatabaseField(columnName="module_id")
	private int module_id;
	@DatabaseField(columnName="img")
	private String img;
	private String[] imageList;
	@DatabaseField(columnName="module_name")
	private String module_name;
	@DatabaseField(columnName="user_name")
	private String user_name;
	@DatabaseField(columnName="read_time")
	private long read_time;
	@DatabaseField(columnName="scan_id")
	private int scan_id;
	public long getRead_time() {
		return read_time;
	}
	public void setRead_time(long read_time) {
		this.read_time = read_time;
	}
	public int getScan_id() {
		return scan_id;
	}
	public void setScan_id(int scan_id) {
		this.scan_id = scan_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	private String head_img;
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String[] getImageList() {
		return imageList;
	}
	public void setImageList(String[] image) {
		this.imageList = image;
	}
	@Override
	public int describeContents() {
		return 0;
	}

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Article(int article_id, String article_title,
			String article_content, String author, long create_time,
			long update_time, int favor, int scan, int zan, int discuss,
			int module_id, String img, String[] image,String module_name,String user_name,String head_img) {
		super();
		this.article_id = article_id;
		this.article_title = article_title;
		this.article_content = article_content;
		this.author = author;
		this.create_time = create_time;
		this.update_time = update_time;
		this.favor = favor;
		this.scan = scan;
		this.zan = zan;
		this.discuss = discuss;
		this.module_id = module_id;
		this.img = img;
		this.imageList = image;
		this.module_name=module_name;
		this.user_name=user_name;
		this.head_img=head_img;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(article_id);
		arg0.writeString(article_title);
		arg0.writeString(article_content);
		arg0.writeString(author);
		arg0.writeLong(create_time);
		arg0.writeLong(update_time);
		arg0.writeInt(favor);
		arg0.writeInt(scan);
		arg0.writeInt(zan);
		arg0.writeInt(discuss);
		arg0.writeInt(module_id);
		arg0.writeString(img);
		arg0.writeStringArray(imageList);
		arg0.writeString(module_name);
		arg0.writeString(user_name);
		arg0.writeString(head_img);
		
	}
	public static final Parcelable.Creator<Article> CREATOR=new Creator<Article>() {

		@Override
		public Article createFromParcel(Parcel arg0) {
			Article a=new Article();
			a.setArticle_id(arg0.readInt());
			a.setArticle_title(arg0.readString());
			a.setArticle_content(arg0.readString());
			a.setAuthor(arg0.readString());
			a.setCreate_time(arg0.readLong());
			a.setUpdate_time(arg0.readLong());
			a.setFavor(arg0.readInt());
			a.setScan(arg0.readInt());
			a.setZan(arg0.readInt());
			a.setDiscuss(arg0.readInt());
			a.setModule_id(arg0.readInt());
			a.setImg(arg0.readString());
			a.setImageList(arg0.createStringArray());
			a.setModule_name(arg0.readString());
			a.setUser_name(arg0.readString());
			a.setHead_img(arg0.readString());	
			return a;
		}

		@Override
		public Article[] newArray(int arg0) {
		
			return new Article[arg0];
		}
		
	}; 

}
