package com.ifit.app.other;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;

public class RoundedCornersDrawable extends Drawable {

	
	private Paint mPaint;
	private Bitmap mBitmap;
	//private int getWidth,getHeight;
	//private int setWidth=250,setHeight=450;
	//private float scaleWidth,scaleHeight;
	private RectF rectF;
	
	public RoundedCornersDrawable(Bitmap bitmap,int W,int H){
		
		/*Matrix matrix = new Matrix();
		getWidth = bitmap.getWidth();
		getHeight = bitmap.getHeight();
		scaleWidth = ((float)setWidth)/getWidth;
		scaleHeight = ((float)setHeight)/getHeight;
		matrix.postScale(scaleWidth, scaleHeight);
		mBitmap = Bitmap.createBitmap(bitmap,0,0,getWidth,getHeight,matrix,true);*/
		//Log.d("xxx", W+""+11);
		//Log.d("xxx",H+"");
		mBitmap = ThumbnailUtils.extractThumbnail(bitmap,W,H);
		BitmapShader bitmapShader = new BitmapShader(mBitmap,TileMode.CLAMP,
				TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
	}
	
	
	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.setBounds(left, top, right, bottom);
		rectF = new RectF(left,top,right,bottom);
	}


	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawRoundRect(rectF, 40, 40, mPaint);
	}

	
	@Override
	public int getIntrinsicWidth() {
		// TODO Auto-generated method stub
		return mBitmap.getWidth();
	}


	@Override
	public int getIntrinsicHeight() {
		// TODO Auto-generated method stub
		return mBitmap.getHeight();
	}


	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return PixelFormat.TRANSLUCENT;
	}

}
