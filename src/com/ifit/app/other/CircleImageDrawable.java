package com.ifit.app.other;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class CircleImageDrawable extends Drawable {

	private Paint mPaint;
	private int mWidth;
	private Bitmap mBitmap;
	
	public CircleImageDrawable(Bitmap bitmap){
		mBitmap = bitmap;
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP,TileMode.CLAMP);
		mPaint = new Paint(); 
		mPaint.setAntiAlias(true); 
		mPaint.setShader(bitmapShader);  
		mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());  
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);  
	}

	
	@Override
	public int getIntrinsicWidth() {
		// TODO Auto-generated method stub
		return mWidth;  
	}
	@Override
	public int getIntrinsicHeight() {
		// TODO Auto-generated method stub
		return mWidth;  
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
