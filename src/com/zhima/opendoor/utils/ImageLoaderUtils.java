package com.zhima.opendoor.utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zhima.opendoor.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoaderUtils {
	private static DisplayImageOptions options=new DisplayImageOptions.Builder().
			showImageOnLoading(R.drawable.image_group_qzl_1)
			.showImageOnFail(R.drawable.image_group_load_f)
			.showImageForEmptyUri(R.drawable.image_group_load_f)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.resetViewBeforeLoading(true)
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.displayer(new FadeInBitmapDisplayer(200))
			.build();
	public static void display(String url,ImageView view){
		//ImageLoader.getInstance().displayImage(url, view);	
		ImageLoader.getInstance().displayImage(url,view,options);
		
	}

}
