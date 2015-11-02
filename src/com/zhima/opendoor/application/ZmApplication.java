package com.zhima.opendoor.application;

import imsdk.data.IMSDK;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zhima.opendoor.entity.User;
import com.zhima.opendoor.manager.ExecutorManager;
import com.zhima.opendoor.utils.FileUitlity;

public class ZmApplication extends Application {
	private RequestQueue request;
	//private CCImageCache imagecache;
	private static ZmApplication application;
	private List<Activity> mList=new ArrayList<Activity>();
	private User user;
	@Override
	public void onCreate() {
		super.onCreate();
		IMSDK.init(getApplicationContext(), "33f015f15ff9f72717f567b7");
		application=this;
		request=Volley.newRequestQueue(this);
		 ImageLoaderConfiguration config =new ImageLoaderConfiguration.Builder(this)
			//不要缓存不同尺寸
			.denyCacheImageMultipleSizesInMemory()
			//下载图片线程的优先级
			.threadPriority(Thread.NORM_PRIORITY-2)
			//设置下载线程的执行器（线程池）
			.taskExecutor(ExecutorManager.getInstance().getExecutor())
			//设置内存缓存的大小
			.memoryCacheSize((int)Runtime.getRuntime().maxMemory()/8)
			//设置磁盘缓存大小
			.diskCacheSize(50*1024*1024)
			//设置磁盘缓存文件的命名生成器
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			//设置磁盘缓存的路径
			.discCache(new UnlimitedDiskCache(FileUitlity.getInstance(this).makeDir("imagCache")))
			//
			.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
			//设置具体的图片加载器：
			.imageDownloader(new BaseImageDownloader(this, 60*1000, 60*1000))
			//生成配置信息
			.build();
		ImageLoader.getInstance().init(config);		
	}
	public RequestQueue getRequestQueue(){
		return this.request;
	}

	public User getUser(){
		return this.user;
	}
	public void setUser(User uer){
		this.user=uer;
	}
	public void addActivity(Activity a){
		mList.add(a);
		
	}
	public void finishActivity(){
		if(mList!=null&&mList.size()>0){
			for(int i=0;i<mList.size();i++){
				mList.get(i).finish();
			}
		}
	}
	
	public static ZmApplication getInstance(){
		return application;
	}
}