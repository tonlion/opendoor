package com.zhima.opendoor.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zhima.opendoor.entity.Article;
import com.zhima.opendoor.entity.User;

public class DataBaseOpenHelper extends OrmLiteSqliteOpenHelper {
	private static DataBaseOpenHelper helper;
	private static String DB_NAME = "openDoor.db";
	private static int DB_VERSION = 1;

	private DataBaseOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	public static DataBaseOpenHelper getInstance(Context context) {
		synchronized (DataBaseOpenHelper.class) {
			if (helper == null) {
				synchronized (DataBaseOpenHelper.class) {
					if(helper==null){
						helper=new DataBaseOpenHelper(context);
					}
				}
			}
		}

	
		return helper;

	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(arg1, User.class);
			TableUtils.createTable(arg1, Article.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {

	}
	private Dao<User,String> userDao=null;
	private Dao<Article,String> articleDao=null;

	public Dao<User, String> getStudentDao() {
		if(userDao==null){
			try {
				userDao=getDao(User.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userDao;

	}
	public Dao<Article, String> getArticleDao() {
		if(articleDao==null){
			try {
				articleDao=getDao(Article.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return articleDao;

	}
	/**
	 * ÊÍ·Å×ÊÔ´
	 */
	@Override
	public void close() {
		super.close();
		userDao=null;
		articleDao=null;
	}

}
