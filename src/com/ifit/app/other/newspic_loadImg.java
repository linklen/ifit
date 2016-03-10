package com.ifit.app.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.SystemClock;

//图片加载器，用线程池管理线程，避免出现oom和资源浪费

public class newspic_loadImg {
	public Map<String,SoftReference<Bitmap>> imgCache 
	= new HashMap<String,SoftReference<Bitmap>>();
	
	
	private ExecutorService excutorService= 
			Executors.newFixedThreadPool(20);
	
	private final Handler handler = new Handler();
	
	
	public Bitmap loadBitmap(final String imgpath, final int position,final int number,
			final ImgCallback callback){
		//如果缓存过就从缓存中取出数据
		if(imgCache.containsKey(imgpath)){
			SoftReference<Bitmap> softReference = imgCache.get(imgpath);
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		//缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
		
		excutorService.submit(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					final Bitmap img = getImg(imgpath);
					imgCache.put(imgpath, new SoftReference<Bitmap>(img));
					
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							callback.imageLoaded(img,position,number);
						}
					});
					
				}catch(Exception e){
					throw new RuntimeException(e);
				}
			}
		});
		return null;
		
		
	}
	
	
	//获取数据
	protected Bitmap getImg(String imgPath){
		try{
			InputStream Is = null;
			Bitmap bm;
			BitmapFactory.Options opts;
			opts = new BitmapFactory.Options();
			opts.inTempStorage = new byte[100*1024];
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			//opts.inSampleSize = 2;
			opts.inInputShareable = true;
			Is = new FileInputStream(imgPath);
			bm = BitmapFactory.decodeStream(Is,null,opts);
			return  bm;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public interface ImgCallback{
		public void imageLoaded(Bitmap bm,int position,int number);
	}
	
	public void ClosePoll(){
		excutorService.shutdown();
	}
	
}
