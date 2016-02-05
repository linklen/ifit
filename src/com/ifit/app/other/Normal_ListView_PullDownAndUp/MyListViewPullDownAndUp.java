package com.ifit.app.other.Normal_ListView_PullDownAndUp;

//直接拿别人的demo，自己做的有小问题，找不到

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ifit.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyListViewPullDownAndUp extends ListView{

		//int firstVisibleItemIndex;//屏幕显示的第一个item的索引值
		//int lastVisibleItemIndex;//屏幕能见的最后一个item的索引值
		private View header;//头布局
		private ImageView headerArrow;// 剪头图片
		private ProgressBar headerProgressBar;//进度条
		private TextView headerTitle;//头布局中的字
		private TextView headerLastUpdated;//最后更新时间
		private View footer;//底布局
		private ImageView footerArrow;//底布局剪头
		private ProgressBar footerProgressBar;//底布局进度条
		private TextView footerTitle;//底布局文字
		private TextView footerLastUpdated;//底布局最后更新时间
		
		//private int headerWidth; //头布局宽
		private int headerHeight;//头布局高
		
		
		private Animation animation; //动画
		private Animation reverseAnimation;//动画
		private Animation otherAnimation;
		private Animation context_back;
		  
		private static final int PULL_TO_REFRESH=0;
		private static final int RELEASE_TO_REFERESH=1;
		private static final int REFERESHING=2;
		private static final int DONE=3;
		
		
		private static final float RATIO = 3;
		
		private static boolean isBack = false;
		private static boolean isUp = false;
		private boolean refereshEnable;//是否可以进行刷新
		private int state;//当前刷新状态
		
		boolean isRecorded;
		float startY;
		float firstTempY = 0;
		float secondTempY = 0; 
		private RefreshListener rListener;
		
		
		public MyListViewPullDownAndUp(Context context, AttributeSet attrs) {
		      super(context, attrs); 
		      init(context); 
		  } 
		  public MyListViewPullDownAndUp(Context context) {
		      super(context);
		      init(context);
		  } 
		  /**
		 * 初始化listview
		 * @param context
		 */
		  private void init(Context context) {
		      
			  //这只旋转动画
		      animation=new RotateAnimation(-180,0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		      animation.setDuration(150);//设置动画持续时间
		      animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
		      animation.setInterpolator(new LinearInterpolator());//定义动画变化速率
		      
		      otherAnimation = new RotateAnimation(0,180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		      otherAnimation.setFillAfter(true);
		      otherAnimation.setDuration(0);
		      
		      reverseAnimation=new RotateAnimation(0,-180, RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		      reverseAnimation.setDuration(150);
		      reverseAnimation.setFillAfter(true);
		      reverseAnimation.setInterpolator(new LinearInterpolator());
		      
		      LayoutInflater inflater=LayoutInflater.from(context);
		      header=inflater.inflate(R.layout.listfresh_header_footer, null); 
		      headerArrow=(ImageView) header.findViewById(R.id.fresh_arrow_image);
		      headerProgressBar=(ProgressBar) header.findViewById(R.id.fresh_progressbar);
		      headerTitle=(TextView) header.findViewById(R.id.fresh_tipsText);
		      headerLastUpdated=(TextView) header.findViewById(R.id.fresh_lastUpdatedText);
		      headerArrow.setMinimumWidth(70);
		      headerArrow.setMaxHeight(50);
		      
		      footer=inflater.inflate(R.layout.listfresh_header_footer, null);
		      footerArrow=(ImageView) footer.findViewById(R.id.fresh_arrow_image);
		      footerArrow.startAnimation(reverseAnimation);//把箭头方向反转过来
		      footerProgressBar=(ProgressBar) footer.findViewById(R.id.fresh_progressbar);
		      footerTitle=(TextView) footer.findViewById(R.id.fresh_tipsText);
		      footerLastUpdated=(TextView) footer.findViewById(R.id.fresh_lastUpdatedText);
		      footerTitle.setText("加载更多");
		      footerLastUpdated.setText("还未加载过...");
		      footerArrow.setMinimumWidth(70);
		      footerArrow.setMaxHeight(50);
		      
		      measureView(header);
		      
		      //headerWidth=header.getMeasuredWidth();
		      headerHeight=header.getMeasuredHeight();
		      
		      header.setPadding(0,-1*headerHeight,0,0);//设置 与界面上边距的距离
		      header.invalidate();//控件重绘
		      
		      footer.setPadding(0,-1*headerHeight,0,0);//设置与界面上边距的距离
		      footer.invalidate();//控件重绘
		      
		      addHeaderView(header);
		      addFooterView(footer);
		            
		      state=DONE;
		      refereshEnable=false;
		  }
		  
		  public void Init_contextAnimation(float fromY,float toY){
			  context_back = new TranslateAnimation(0,0,fromY, toY);
			  context_back.setDuration(300);
			  //context_back.setFillAfter(true);
		  }
		  private void measureView(View v) {
		      ViewGroup.LayoutParams lp=v.getLayoutParams();
		      if(lp==null){
		          lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        		  ViewGroup.LayoutParams.WRAP_CONTENT);
		          ////第一个参数为宽的设置，第二个参数为高的设置。
		      }
		      
		      //计算MeasureSpec然后传递到特定的子视图，此方法用来计算一个合适子视图的尺寸大小（宽度或者高度)。
		      //ViewGroup.getChildMeasureSpec(int spec, int padding, int childDimension) 
		      /*
		       *  spec 父窗口传递给子视图的大小和模式
           		  padding 父窗口的边距，也就是xml中的android:padding
           		  childDimension 子视图想要绘制的准确大小，但最终不一定绘制此值
		       */
		      int measureWidth=ViewGroup.getChildMeasureSpec(0,0,lp.width);
		      
		      int measureHeight;
		      if(lp.height>0){
		          measureHeight=MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.EXACTLY);
		      }else {
		          measureHeight=MeasureSpec.makeMeasureSpec(lp.height,MeasureSpec.UNSPECIFIED);
		      }
		      v.measure(measureWidth, measureHeight);
		  } 
		  
		  /**
		 * 中央控制台
		 * 几科所有的拉动事件皆由此驱动
		 */
		  @Override
		  public boolean onTouchEvent(MotionEvent ev) { 
		      //lastVisibleItemIndex=getLastVisiblePosition()-1;//因为加有一尾视图，所以这里要咸一
		       //getLastVisiblePosition();//指当前屏幕所显示的最后一个item是第几个item。头尾布局算上，头为0.
		      //int totalCounts=getCount()-2;//因为给listview加了一头一尾）视图所以这里要减二 
		      if(refereshEnable){
		           
		          switch (ev.getAction()) {
		          case MotionEvent.ACTION_DOWN: 
		        	  onAction_Down(ev);
		              break; 
		              
		          case MotionEvent.ACTION_MOVE:
		              onMove(ev);
		              break;
		              
		          case MotionEvent.ACTION_UP: 
		        	  onAction_Up(ev);
		              break;    
		          }
		      }
		      return super.onTouchEvent(ev);
		  }
		  
		  
		  public void onAction_Down(MotionEvent ev){
	              firstTempY=ev.getY(); 
	              isRecorded=false; 
	              isUp = false;
	              if(getFirstVisiblePosition()==0){
	                  if(!isRecorded){
	                      startY=ev.getY();
	                      isRecorded=true;
	                  }
	              }
		  }
	              
		  //移动时的操作
		  public void onMove(MotionEvent ev){
			  
              if (getFirstVisiblePosition() == 0 || getFirstVisiblePosition() == 1) { 
                  firstTempY=secondTempY;
                  secondTempY=ev.getY();
                  if (!isRecorded) {
                      startY = secondTempY;
                      isRecorded = true;
                  }
                  if (state != REFERESHING) {
                      if (state == DONE) {
                          if (secondTempY - startY > 0) {
                              state = PULL_TO_REFRESH; 
                              onHeaderStateChange();
                          }
                      }
                      if (state == PULL_TO_REFRESH) {
                          if ((secondTempY - startY) / RATIO > headerHeight&& secondTempY-firstTempY > 3) {
                              state = RELEASE_TO_REFERESH;
                              onHeaderStateChange();
                          } else if (secondTempY - startY <= -5) {
                              state = DONE;
                              onHeaderStateChange();
                          }
                      }
                      if (state == RELEASE_TO_REFERESH) {
                          if (firstTempY-secondTempY>5) {
                              state = PULL_TO_REFRESH;
                              isBack = true;// 从松开刷新 回到的下拉刷新
                              onHeaderStateChange();
                          } else if (secondTempY - startY <= -5) {
                              state = DONE; 
                              onHeaderStateChange();
                          }
                      }
                      if (state == PULL_TO_REFRESH|| state == RELEASE_TO_REFERESH) {
                          header.setPadding(0, (int) ((secondTempY - startY)/ RATIO - headerHeight), 0, 0);
                      }
                  } 
              }
              if(getLastVisiblePosition()==getCount()-2||getLastVisiblePosition()==getCount()-1){
                  firstTempY=secondTempY;
                  secondTempY=ev.getY();
                  if (!isRecorded) {
                      startY = secondTempY;
                      isRecorded = true;
                  }
                  if(state!=REFERESHING){//不是正在刷新状态
                      if (state == DONE) {
                          if (startY - secondTempY> 0) {
                              state = PULL_TO_REFRESH; 
                              onFooterStateChange();
                          }
                      }
                      if (state == PULL_TO_REFRESH) {
                          if ((startY-secondTempY) / RATIO > headerHeight    && firstTempY-secondTempY>=9) {
                              state = RELEASE_TO_REFERESH;
                              onFooterStateChange();
                          } else if (startY-secondTempY <= 0) {
                              state = DONE;
                              onFooterStateChange();
                          }
                      }
                      if (state == RELEASE_TO_REFERESH) {
                          if(firstTempY-secondTempY<-5){
                              state = PULL_TO_REFRESH;
                              isBack = true;// 从松开刷新 回到的上拉刷新
                              onFooterStateChange();
                          } else if (secondTempY-startY >= 0) {
                              state = DONE; 
                              onFooterStateChange();
                          }
                      }
                      if((state==PULL_TO_REFRESH||state==RELEASE_TO_REFERESH)&&secondTempY<startY){
                          footer.setPadding(0, 0, 0, (int) ((startY-secondTempY)/RATIO-headerHeight));
                      }
                  }
              }
		  }
		  
		  public void onAction_Up(MotionEvent ev){
			  
			  isUp = true;
			  
              if(state != REFERESHING){ 
                  if(state == PULL_TO_REFRESH){
                      state = DONE;
                      onHeaderStateChange();
                      if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2)//上拉
                          onFooterStateChange();
                  }
                  if(state == RELEASE_TO_REFERESH){
                      state = REFERESHING; 
                      if(getFirstVisiblePosition()==0){
                          //下拉
                          onHeaderStateChange(); 
                          onPullDownRefresh();//刷新得到服务器数据
                      }
                      if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2){
                          //上拉
                          onFooterStateChange();
                          onPullUpRefresh();//刷新得到服务器数据
                      }
                  }
              }
		  }
		  
		  /**
			 * 处理下拉刷新完成后事项
			 */
			  public void onPulldownRefreshComplete(){
			      state=DONE;
			      onHeaderStateChange();
			      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			      headerLastUpdated.setText("最后刷新时间："+sdf.format(new Date()));
			  }
			  /**
			 * 处理上拉刷新完成后事项
			 */
			  public void onPullupRefreshComplete(){
			      state=DONE;
			      onFooterStateChange();
			      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			      footerLastUpdated.setText("最后刷新时间："+sdf.format(new Date()));
			  }
			  
			  
		  /**
		 * 更改尾视图显示状态
		 */
		  private void onHeaderStateChange() {
		      switch (state) {
		      case PULL_TO_REFRESH: 
		          headerProgressBar.setVisibility(View.GONE);
		          headerArrow.setVisibility(View.VISIBLE);
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE);
		          
		          headerTitle.setText("下拉刷新");
		          headerArrow.clearAnimation();
		          if(isBack){
		              headerArrow.startAnimation(animation);
		              isBack=false;
		          }
		          break;
		
		      case RELEASE_TO_REFERESH:
		          headerProgressBar.setVisibility(View.GONE);
		          headerArrow.setVisibility(View.VISIBLE);
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE);
		          
		          headerTitle.setText(" 松开刷新");
		          headerArrow.clearAnimation();
		          headerArrow.startAnimation(reverseAnimation);
		          break; 
		      
		      case REFERESHING:
		          headerProgressBar.setVisibility(View.VISIBLE);
		          headerArrow.setVisibility(View.GONE); 
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE);
		          
		          headerTitle.setText("正在刷新...");
		          headerArrow.clearAnimation();
		          
		          header.setPadding(0, 0, 0,0);
		      break;
		      case DONE:
		          headerProgressBar.setVisibility(View.GONE);
		          headerArrow.setVisibility(View.VISIBLE);
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE); 
		          headerTitle.setText("下拉刷新");
		          headerArrow.clearAnimation(); 
		          header.setPadding(0, -1*headerHeight, 0,0);
		          /*if(isUp){
		        	  if(getFirstVisiblePosition()==0)//下拉
                    	  Init_contextAnimation(50f, 0);
                      this.startAnimation(context_back);
		          }
		          */
		         
		      break;
		      }
		  }
		  /**
		 * 更改尾视图显示状态
		 */
		  private void onFooterStateChange() {
		      switch (state) {
		      case PULL_TO_REFRESH: 
		          footerProgressBar.setVisibility(View.GONE);
		          footerArrow.setVisibility(View.VISIBLE);
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		          
		          footerTitle.setText("加载更多");
		          footerArrow.startAnimation(otherAnimation);
		          //footerArrow.clearAnimation();
		          if(isBack){
		              footerArrow.startAnimation(reverseAnimation);
		              isBack=false;
		          }
		          break;
		
		      case RELEASE_TO_REFERESH:
		          footerProgressBar.setVisibility(View.GONE);
		          footerArrow.setVisibility(View.VISIBLE);
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		          
		          footerTitle.setText(" 松开加载");
		          footerArrow.clearAnimation();
		          footerArrow.startAnimation(animation);
		          break; 
		      
		      case REFERESHING:
		          footerProgressBar.setVisibility(View.VISIBLE);
		          footerArrow.setVisibility(View.GONE); 
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		          
		          footerTitle.setText("加载中...");
		          footerArrow.clearAnimation();
		          
		          footer.setPadding(0, 0, 0,0);
		      break;
		      case DONE:
		          footerProgressBar.setVisibility(View.GONE);
		          footerArrow.setVisibility(View.VISIBLE);
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		         
		          footerTitle.setText("加载更多");
		          footerArrow.clearAnimation();
		          
		          footer.setPadding(0, -1*headerHeight, 0,0);
		      break;
		      }    
		  }
		  
		  
		  //接口
		  public interface RefreshListener{
		      public void pullDownRefresh();
		      public void pullUpRefresh();
		  }
		  public void setRefreshListener(RefreshListener l){
		      rListener=l;
		      refereshEnable=true;
		  }
		  
		  /**
		 * 下拉刷新的实现方法
		 */
		  private void onPullDownRefresh() {
		      if(rListener!=null){
		          rListener.pullDownRefresh();
		      }
		  }
		  
		  /**
		 * 上拉刷新的实现方法
		 */
		  private void onPullUpRefresh() {
		      if(rListener!=null)
		          rListener.pullUpRefresh();
		  }
			
	
}
