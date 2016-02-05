package com.ifit.app.other.Normal_ListView_PullDownAndUp;

//ֱ���ñ��˵�demo���Լ�������С���⣬�Ҳ���

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

		//int firstVisibleItemIndex;//��Ļ��ʾ�ĵ�һ��item������ֵ
		//int lastVisibleItemIndex;//��Ļ�ܼ������һ��item������ֵ
		private View header;//ͷ����
		private ImageView headerArrow;// ��ͷͼƬ
		private ProgressBar headerProgressBar;//������
		private TextView headerTitle;//ͷ�����е���
		private TextView headerLastUpdated;//������ʱ��
		private View footer;//�ײ���
		private ImageView footerArrow;//�ײ��ּ�ͷ
		private ProgressBar footerProgressBar;//�ײ��ֽ�����
		private TextView footerTitle;//�ײ�������
		private TextView footerLastUpdated;//�ײ���������ʱ��
		
		//private int headerWidth; //ͷ���ֿ�
		private int headerHeight;//ͷ���ָ�
		
		
		private Animation animation; //����
		private Animation reverseAnimation;//����
		private Animation otherAnimation;
		private Animation context_back;
		  
		private static final int PULL_TO_REFRESH=0;
		private static final int RELEASE_TO_REFERESH=1;
		private static final int REFERESHING=2;
		private static final int DONE=3;
		
		
		private static final float RATIO = 3;
		
		private static boolean isBack = false;
		private static boolean isUp = false;
		private boolean refereshEnable;//�Ƿ���Խ���ˢ��
		private int state;//��ǰˢ��״̬
		
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
		 * ��ʼ��listview
		 * @param context
		 */
		  private void init(Context context) {
		      
			  //��ֻ��ת����
		      animation=new RotateAnimation(-180,0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		      animation.setDuration(150);//���ö�������ʱ��
		      animation.setFillAfter(true);//����ִ������Ƿ�ͣ����ִ�����״̬
		      animation.setInterpolator(new LinearInterpolator());//���嶯���仯����
		      
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
		      footerArrow.startAnimation(reverseAnimation);//�Ѽ�ͷ����ת����
		      footerProgressBar=(ProgressBar) footer.findViewById(R.id.fresh_progressbar);
		      footerTitle=(TextView) footer.findViewById(R.id.fresh_tipsText);
		      footerLastUpdated=(TextView) footer.findViewById(R.id.fresh_lastUpdatedText);
		      footerTitle.setText("���ظ���");
		      footerLastUpdated.setText("��δ���ع�...");
		      footerArrow.setMinimumWidth(70);
		      footerArrow.setMaxHeight(50);
		      
		      measureView(header);
		      
		      //headerWidth=header.getMeasuredWidth();
		      headerHeight=header.getMeasuredHeight();
		      
		      header.setPadding(0,-1*headerHeight,0,0);//���� ������ϱ߾�ľ���
		      header.invalidate();//�ؼ��ػ�
		      
		      footer.setPadding(0,-1*headerHeight,0,0);//����������ϱ߾�ľ���
		      footer.invalidate();//�ؼ��ػ�
		      
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
		          ////��һ������Ϊ������ã��ڶ�������Ϊ�ߵ����á�
		      }
		      
		      //����MeasureSpecȻ�󴫵ݵ��ض�������ͼ���˷�����������һ����������ͼ�ĳߴ��С����Ȼ��߸߶�)��
		      //ViewGroup.getChildMeasureSpec(int spec, int padding, int childDimension) 
		      /*
		       *  spec �����ڴ��ݸ�����ͼ�Ĵ�С��ģʽ
           		  padding �����ڵı߾࣬Ҳ����xml�е�android:padding
           		  childDimension ����ͼ��Ҫ���Ƶ�׼ȷ��С�������ղ�һ�����ƴ�ֵ
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
		 * �������̨
		 * �������е������¼����ɴ�����
		 */
		  @Override
		  public boolean onTouchEvent(MotionEvent ev) { 
		      //lastVisibleItemIndex=getLastVisiblePosition()-1;//��Ϊ����һβ��ͼ����������Ҫ��һ
		       //getLastVisiblePosition();//ָ��ǰ��Ļ����ʾ�����һ��item�ǵڼ���item��ͷβ�������ϣ�ͷΪ0.
		      //int totalCounts=getCount()-2;//��Ϊ��listview����һͷһβ����ͼ��������Ҫ���� 
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
	              
		  //�ƶ�ʱ�Ĳ���
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
                              isBack = true;// ���ɿ�ˢ�� �ص�������ˢ��
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
                  if(state!=REFERESHING){//��������ˢ��״̬
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
                              isBack = true;// ���ɿ�ˢ�� �ص�������ˢ��
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
                      if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2)//����
                          onFooterStateChange();
                  }
                  if(state == RELEASE_TO_REFERESH){
                      state = REFERESHING; 
                      if(getFirstVisiblePosition()==0){
                          //����
                          onHeaderStateChange(); 
                          onPullDownRefresh();//ˢ�µõ�����������
                      }
                      if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2){
                          //����
                          onFooterStateChange();
                          onPullUpRefresh();//ˢ�µõ�����������
                      }
                  }
              }
		  }
		  
		  /**
			 * ��������ˢ����ɺ�����
			 */
			  public void onPulldownRefreshComplete(){
			      state=DONE;
			      onHeaderStateChange();
			      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			      headerLastUpdated.setText("���ˢ��ʱ�䣺"+sdf.format(new Date()));
			  }
			  /**
			 * ��������ˢ����ɺ�����
			 */
			  public void onPullupRefreshComplete(){
			      state=DONE;
			      onFooterStateChange();
			      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			      footerLastUpdated.setText("���ˢ��ʱ�䣺"+sdf.format(new Date()));
			  }
			  
			  
		  /**
		 * ����β��ͼ��ʾ״̬
		 */
		  private void onHeaderStateChange() {
		      switch (state) {
		      case PULL_TO_REFRESH: 
		          headerProgressBar.setVisibility(View.GONE);
		          headerArrow.setVisibility(View.VISIBLE);
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE);
		          
		          headerTitle.setText("����ˢ��");
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
		          
		          headerTitle.setText(" �ɿ�ˢ��");
		          headerArrow.clearAnimation();
		          headerArrow.startAnimation(reverseAnimation);
		          break; 
		      
		      case REFERESHING:
		          headerProgressBar.setVisibility(View.VISIBLE);
		          headerArrow.setVisibility(View.GONE); 
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE);
		          
		          headerTitle.setText("����ˢ��...");
		          headerArrow.clearAnimation();
		          
		          header.setPadding(0, 0, 0,0);
		      break;
		      case DONE:
		          headerProgressBar.setVisibility(View.GONE);
		          headerArrow.setVisibility(View.VISIBLE);
		          headerTitle.setVisibility(View.VISIBLE);
		          headerLastUpdated.setVisibility(View.VISIBLE); 
		          headerTitle.setText("����ˢ��");
		          headerArrow.clearAnimation(); 
		          header.setPadding(0, -1*headerHeight, 0,0);
		          /*if(isUp){
		        	  if(getFirstVisiblePosition()==0)//����
                    	  Init_contextAnimation(50f, 0);
                      this.startAnimation(context_back);
		          }
		          */
		         
		      break;
		      }
		  }
		  /**
		 * ����β��ͼ��ʾ״̬
		 */
		  private void onFooterStateChange() {
		      switch (state) {
		      case PULL_TO_REFRESH: 
		          footerProgressBar.setVisibility(View.GONE);
		          footerArrow.setVisibility(View.VISIBLE);
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		          
		          footerTitle.setText("���ظ���");
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
		          
		          footerTitle.setText(" �ɿ�����");
		          footerArrow.clearAnimation();
		          footerArrow.startAnimation(animation);
		          break; 
		      
		      case REFERESHING:
		          footerProgressBar.setVisibility(View.VISIBLE);
		          footerArrow.setVisibility(View.GONE); 
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		          
		          footerTitle.setText("������...");
		          footerArrow.clearAnimation();
		          
		          footer.setPadding(0, 0, 0,0);
		      break;
		      case DONE:
		          footerProgressBar.setVisibility(View.GONE);
		          footerArrow.setVisibility(View.VISIBLE);
		          footerTitle.setVisibility(View.VISIBLE);
		          footerLastUpdated.setVisibility(View.VISIBLE);
		         
		          footerTitle.setText("���ظ���");
		          footerArrow.clearAnimation();
		          
		          footer.setPadding(0, -1*headerHeight, 0,0);
		      break;
		      }    
		  }
		  
		  
		  //�ӿ�
		  public interface RefreshListener{
		      public void pullDownRefresh();
		      public void pullUpRefresh();
		  }
		  public void setRefreshListener(RefreshListener l){
		      rListener=l;
		      refereshEnable=true;
		  }
		  
		  /**
		 * ����ˢ�µ�ʵ�ַ���
		 */
		  private void onPullDownRefresh() {
		      if(rListener!=null){
		          rListener.pullDownRefresh();
		      }
		  }
		  
		  /**
		 * ����ˢ�µ�ʵ�ַ���
		 */
		  private void onPullUpRefresh() {
		      if(rListener!=null)
		          rListener.pullUpRefresh();
		  }
			
	
}
