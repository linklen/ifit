package com.ifit.app.other;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MyPageTransformer implements ViewPager.PageTransformer{

	private static final float MIN_SCALE = 0.75f;
	private static final float MIN_ALPHA = 0.75F;
	@Override
	public void transformPage(View view, float position) {
		// TODO Auto-generated method stub
		
		int pageWidth = view.getWidth();
		int pageHeight =view.getHeight();
		float scaleFactor = Math.max(MIN_SCALE, 1-Math.abs(position));
		float vertMargin = pageHeight*(1-scaleFactor)/2;
		float hoezMargin = pageWidth*(1-scaleFactor)/2;
		//Log.d("pqq",""+position);
		
		if(position < -1){//[-Infinity,-1)
			view.setAlpha(MIN_ALPHA);
			view.setScaleX(MIN_SCALE);
			view.setScaleY(MIN_SCALE);
			
			
		}else if(position <= 1)//(0,-1]
		{
			if(position < 0)//(-1,0)
			{
				view.setTranslationX(hoezMargin-vertMargin/2); //Ë®Æ½ÒÆ¶¯
			}else//[0,1]
			{
				view.setTranslationX(-hoezMargin+vertMargin/2);
			}
			
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);
			view.setAlpha(MIN_ALPHA+(scaleFactor-MIN_SCALE)
					/(1-MIN_SCALE)*(1-MIN_ALPHA));
		}else//(1,+Infinity]
		{
			
			view.setAlpha(MIN_ALPHA);
			view.setScaleX(MIN_SCALE);
			view.setScaleY(MIN_SCALE);
			

		}
		
	}

}
