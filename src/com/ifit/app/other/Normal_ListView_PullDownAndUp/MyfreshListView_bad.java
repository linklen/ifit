package com.ifit.app.other.Normal_ListView_PullDownAndUp;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyfreshListView_bad extends ListView {

	
	private View headView,footView;//������������
	
	
	private int add_ViewHeight,add_ViewWidth;//����Ĳ��ֵĸߺͿ�
	
	
	private ImageView head_ArrowImage,foot_ArrowImage;//�������ֵļ�ͷͼƬ
	private ProgressBar head_ProgressBar,foot_ProgressBar;//�������ֵĽ�����
	private TextView head_tipText,foot_tipText;//��ʾ�ı����������������ȡ���
	private TextView head_last_updata_time,foot_last_updata_time;//�����µ�ʱ��
	
	//onTouch �������õ���
	//private int FirstVisiblePosition = 0;// ��Ļ�ϵ�һ�����Կ�����Item��λ��
	//private int LastVisiblePosition = 0;// ������Ļ�����һ��Item��λ��
	//private int totalItem = 0;//�ܹ�Item������
	private boolean isRecorded;
	//private boolean isTop = false;//�ж��Ƿ񵽶���
	//private boolean isBottom = false;//�ж��Ƿ񵽵���
	private float startY,firstTempY = 0,secondTempY = 0;//����ȥ�ǵ�Y���꣬�ƶ����Y���꣬��ָ�뿪�ǵ�Y����
	private static final float RATIO = 3;//ϵ��
	private boolean isBack = false;//�Ƿ�ص���һ��״̬
	
	
	private int state;// ��ǰͷβ���ֵ�״̬
	private static final int NORMAL = 0;// ��ǰ�Ĳ���״̬Ϊ��������״̬
	private static final int PULL = 1;// ��ǰ�Ĳ���״̬Ϊ������ʾ��������ˢ�µ�״̬
	private static final int RELEASE_TO_REFERESH = 2;// ��ǰ�Ĳ���״̬Ϊ�����ɿ�����ˢ�µ�״̬
	private static final int REFRESHING = 3;// ��ǰ�Ĳ���״̬Ϊ��������ˢ��״̬
	private boolean refereshEnable;//�Ƿ���Խ���ˢ��
	

	private Animation Arrow_uptodown; //��ͷ���ϳ��¶���
	private Animation Arrow_downtoup;//��ͷ���³��϶���
	private Animation Arrow_up;//ʹ��ͷһֱ����
	private Animation context_back;//����VIEW���ٷ��صĶ���

	
	public RefreshListener refreshlistener;//���ýӿں����������
	
	
	//��д3�����๹�캯��
	public MyfreshListView_bad(Context context) {
		super(context);
		InitView(context);
	}

	
	public MyfreshListView_bad(Context context,AttributeSet attrs){
		super(context,attrs);
		InitView(context);
	}
	
	
	public MyfreshListView_bad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView(context);
    }
	
	
	private void InitView (Context context){
		
		LayoutInflater inflater = LayoutInflater.from(context);
		
		//ͷ����ʼ��
		headView = inflater.inflate(R.layout.listfresh_header_footer, null);
		head_ArrowImage = (ImageView)headView.findViewById(R.id.fresh_arrow_image);
		head_ProgressBar = (ProgressBar)headView.findViewById(R.id.fresh_progressbar);
		head_tipText = (TextView)headView.findViewById(R.id.fresh_tipsText);
		head_last_updata_time = (TextView)headView.findViewById(R.id.fresh_lastUpdatedText);
		head_ArrowImage.setMinimumWidth(70);
	    head_ArrowImage.setMaxHeight(50);
	    
	    
		//β����ʼ��
		footView = inflater.inflate(R.layout.listfresh_header_footer, null);
		foot_ArrowImage = (ImageView)footView.findViewById(R.id.fresh_arrow_image);
		foot_ProgressBar = (ProgressBar)footView.findViewById(R.id.fresh_progressbar);
		foot_tipText = (TextView)footView.findViewById(R.id.fresh_tipsText);
		foot_last_updata_time = (TextView)footView.findViewById(R.id.fresh_lastUpdatedText);
		foot_tipText.setText("���ظ���");
		foot_last_updata_time.setText("��δ���ع�...");
		foot_ArrowImage.setMinimumWidth(70);
	    foot_ArrowImage.setMaxHeight(50);
	    
	    
		measureView(headView);
		
		add_ViewHeight=headView.getMeasuredWidth();
		add_ViewWidth=headView.getMeasuredHeight();
	      
		headView.setPadding(0,-1*add_ViewHeight,0,0);//���� ������ϱ߾�ľ���
		headView.invalidate();//�ؼ��ػ�
	      
	    footView.setPadding(0,-1*add_ViewHeight,0,0);//����������ϱ߾�ľ���
	    footView.invalidate();//�ؼ��ػ�
	      
	    addHeaderView(headView);
	    addFooterView(footView);
	    
	    state = NORMAL;    
	    refereshEnable=false;
	    Init_Animation();
	}
	
	public void Init_Animation (){
		
		//������ת����
		Arrow_uptodown=new RotateAnimation(-180,0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		Arrow_uptodown.setDuration(150);//���ö�������ʱ��
		Arrow_uptodown.setFillAfter(true);//����ִ������Ƿ�ͣ����ִ�����״̬
		Arrow_uptodown.setInterpolator(new LinearInterpolator());//���嶯���仯����
	      
		Arrow_downtoup=new RotateAnimation(0,-180, RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		Arrow_downtoup.setDuration(150);
		Arrow_downtoup.setFillAfter(true);
		Arrow_downtoup.setInterpolator(new LinearInterpolator());
	      
		Arrow_up = new RotateAnimation(0,180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		Arrow_up.setFillAfter(true);
		Arrow_up.setDuration(0);
	      
	}
	//���Ӳ��ֵ�λ���趨
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

	//��ָ��������,�ܿ���̨
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if(refereshEnable){
		switch(ev.getAction()){
		
		case MotionEvent.ACTION_DOWN :
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
          firstTempY=ev.getY(); //һ���±㸳ֵ����һ������Y����
          isRecorded=false; //��ʾ��ǰY���겢δ��¼
          if(getFirstVisiblePosition()==0){//�ж��Ƿ񵽶���
              if(!isRecorded){//���û�м�¼�򣬰ѵ�ǰ��Y�����¼
                  startY=ev.getY();//��¼����˼���ǰ�Y���긳ֵ��startY���꣬startY������������ֻ����һ�θ�ֵ
                  isRecorded=true;//��ʾ��ǰY�����Ѿ�����¼
              }
          }
  }
	
	public void onMove(MotionEvent ev){
		  
         if (getFirstVisiblePosition()==0 || getFirstVisiblePosition()==1) { //�����ƶ���ʱ���ж��Ƿ��ƶ���������
             firstTempY=secondTempY; //�ѵڶ�������ֵ��ֵ����һ������ֵ
             secondTempY=ev.getY();//�Եڶ�������ֵ���и�ֵ
             if (!isRecorded) {//�ж��Ƿ�ǰ���걻��¼������ڰ��µ�ʱ����ڶ��ˣ����Ѿ�����¼�����µ�ʱ���ڶ��˾�û�б���¼
                 startY = secondTempY;//���û�б���¼����ѵ�ǰ��Y����ͨ���ڶ�������ֵ��ֵ��startY
                 isRecorded = true;//��ǵ�ǰY����¼
             }
             if (state != REFRESHING) { //�жϵ�ǰ��״̬�Ƿ�����ˢ�£���û�������
                 if (state == NORMAL) {//�жϵ�ǰ״̬�Ƿ�Ϊ��̬,�����ڻ�������ʱ���ж���Ϊ��̬�������
                     if (secondTempY - startY > 0) {//�ж��Ƿ���ָ���������ģ�����0������������getYԽ����Խ��
                         state = PULL; //��ı䶥�����ֵ�״̬��Ϊ������
                         onHeaderStateChange();
                     }
                 }
                 if (state == PULL) {//�����жϲ��ǳ�̬�������ж��Ƿ����������������������
                    if ((secondTempY - startY) / RATIO > add_ViewHeight && secondTempY-firstTempY > 3) {
                    	 //�����жϣ����������Ƿ���ڲ��ָ߶ȵ����������ں������3����Ϊû�ƶ�һ�Σ�getY�ͻ�仯�����ǵ����˻�������ʱ��
                    	 //position����Ϊ��ģ����������н��ڶ�����Y��ֵ����һ����Y����һֱ�ڱ仯��һֱ���жϣ�һֱ�ڸ�ֵ
                    	 //��һ��ֵ��ȥǰһ��ֵ����ʵ��ָ�ƶ��Ŀ��棬���ƶ��Ŀ죬�ǻ�����ģ����統ǰλ����1���ƶ��Ŀ죬����ֱ����3����4
                    	 //����ƶ�������1��2�ٵ�3��getYҲ��һ���ģ������ƶ����Ա���С��3�������ƶ�����Դ���3��������ֵ
                         state = RELEASE_TO_REFERESH;//�ı䲼��״̬�����Ϊ�������Ҿ�����ڸ߶����������Ϊ�ɿ�ˢ��
                         onHeaderStateChange();
                     } else if (secondTempY - startY <= -5) { //�������ƶ����佫���ָ�ԭΪ��̬��
                         state = NORMAL;
                         onHeaderStateChange();
                     }
                 }
                 if (state == RELEASE_TO_REFERESH) {//�����ж�״̬�Ƿ�Ϊ�ɿ�ˢ��
                     if (firstTempY-secondTempY>5) {//������һ��������5��ʵ���ƶ�����������������
                    	 //�����Ƿ����ƶ�������������5�������
                         state = PULL;//״̬��ԭΪ��ס
                         isBack = true;// ���ɿ�ˢ�� �ص�������ˢ�£��ص���һ��״̬
                         onHeaderStateChange();
                     } else if (secondTempY - startY <= -5) {//������ƶ�����һ��Item֮�£���ԭ����
                         state = NORMAL; 
                         onHeaderStateChange();
                     }
                 }
                 if (state == PULL || state == RELEASE_TO_REFERESH) {//�жϵ�״̬Ϊ�������������ɿ�ˢ�µ�״̬����ͷ���ֵ��ڱ߾���ж�̬���ġ������������
                     headView.setPadding(0, (int) ((secondTempY - startY)/ RATIO - add_ViewHeight), 0, 0);
                 }
             } 
         }
         if(getLastVisiblePosition()==getCount()-2||getLastVisiblePosition()==getCount()-1){//�����ж��Ƿ񵽵ף����ߵ������ڶ���
             firstTempY=secondTempY; //�͵�����һ�������ڶ�������ֵ��ֵ����һ������ֵ
             secondTempY=ev.getY(); //�Եڶ�������ֵ���и�ֵ
             if (!isRecorded) { //�ж��Ƿ��¼��,��û����¼�����
                 startY = secondTempY; //�򽫵�ǰY����ͨ���ڶ�����ֵ���и�ֵ
                 isRecorded = true;//���Ϊ����¼
             }
             
             if(state!=REFRESHING){//��������ˢ��״̬
                 if (state == NORMAL) {//�ж��Ƿ�Ϊ��̬���������
                     if (startY - secondTempY> 0) {//�ж��Ƿ�Ϊ��������getY���ϱ�С����ʼ��ֵ���ں�����ֵ��˵��������
                         state = PULL; //�Ѿ��ڵײ�����������������״̬��Ϊ������
                         onFooterStateChange();
                     }
                 }
                 if (state == PULL) {//�ж��Ƿ����������������
                     if ((startY-secondTempY) / RATIO > add_ViewHeight && firstTempY-secondTempY>=9) {
                    	 //�ж������ľ����Ƿ��ǲ��ָ߶ȵ�3��������Ĵ��ڵ���9�Ǻ�ǰ��һ������һ�������ƶ��������жϣ��������£��򲻻����9
                    	 //�����٣�����������ͻ����9����һ��������ǰ���ֵ���ڶ������������õ�ֵ��ǰ���ֵ���ں���ģ�˵��������
                    	 //��ΪgetY���ϱ�С
                         state = RELEASE_TO_REFERESH;//�����������Ϊ�ɿ�ˢ��״̬
                         onFooterStateChange();
                     } else if (startY-secondTempY <= 0) {//�����һ�θ�ֵ��Y����С�����»�õ����ڵ����꣬˵����������
                    	 //�����Ѿ����˵�һ�θ�ֵ����֮�£���״̬�ָ�����̬
                         // ����ˢ�� --�� �ص� ˢ�����
                         state = NORMAL;
                         onFooterStateChange();
                     }
                 }
                 if (state == RELEASE_TO_REFERESH) {//�ж�״̬�Ƿ�Ϊ�ɿ�ˢ�£����������
                     if(firstTempY-secondTempY<-5){//Ҳ�������жϣ����ǵ�һ�ε�ֵС�ڵڶ��Σ�˵��ǰ��С�ں��棬˵��������
                         state = PULL;//��״̬�ָ�Ϊ������
                         isBack = true;// ���ɿ�ˢ�� �ص�������ˢ��
                         onFooterStateChange();
                     } else if (secondTempY-startY >= 0) {//��ǰ��һ�������˵�һ�θ�ֵ֮�£���״̬�ָ���̬
                         // �ɿ�ˢ�� --�� �ص� ˢ�����
                         state = NORMAL; 
                         onFooterStateChange();
                     }
                 }
                 if((state==PULL||state==RELEASE_TO_REFERESH)&&secondTempY<startY){
                	 //��״̬Ϊ���������߷ſ�ˢ�£��������µ�Y����һֱ�ڿ�ʼ��ֵ����֮�ϣ�����ж�̬�ı䲼���ڱ߾�
                     footView.setPadding(0, 0, 0, (int) ((startY-secondTempY)/RATIO-add_ViewHeight));
                 }
             }
         }
		  
	  }
	
	 public void onAction_Up(MotionEvent ev){//����ָ̧��ʱ
		  
         if(state != REFRESHING){ //�ж��Ƿ�Ϊ����ˢ��״̬�������������
             if(state == PULL){//�ж��Ƿ�����������״̬�����������
                 state = NORMAL;//��������״̬��������״̬�ָ�Ϊ��̬
                 if(getFirstVisiblePosition()==0)//����
                  onHeaderStateChange();
                 if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2)//����
                     onFooterStateChange();
             }
             
             if(state == RELEASE_TO_REFERESH){//�ж��Ƿ�Ϊ�ɿ�ˢ�µ�״̬�����������
                 state = REFRESHING; //�ɿ���״̬��Ϊ������ˢ��
                 if(getFirstVisiblePosition()==0){
                     //����
                     onHeaderStateChange(); 
                     onPullDownRefresh();//ˢ�µõ����������ݣ�����ˢ��
                 }
                 if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2){
                     //����
                     onFooterStateChange();
                     onPullUpRefresh();//ˢ�µõ�����������
                 }
             }
         }
	  }
	 
	 
	 //��ȡ���һ��ˢ�µ�ʱ��
	 //������ɻ�ȡʱ��
		  public void onPulldownRefreshComplete(){
		      state=NORMAL;
		      onHeaderStateChange();
		      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		      head_last_updata_time.setText("���ˢ��ʱ�䣺"+sdf.format(new Date()));
		  }
	//������ɻ�ȡʱ��
		  public void onPullupRefreshComplete(){
		      state=NORMAL;
		      onFooterStateChange();
		      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		      foot_last_updata_time.setText("���ˢ��ʱ�䣺"+sdf.format(new Date()));
		  }
		  
		  
	 /**
	 * ����ͷ����ʾ״̬
	 */
	  private void onHeaderStateChange() {
	      switch (state) {
	      
	      case NORMAL:
	          head_ProgressBar.setVisibility(GONE);
	          head_ArrowImage.setVisibility(VISIBLE);
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE); 
	          head_tipText.setText("����ˢ��");
	          head_ArrowImage.clearAnimation(); 
	          headView.setPadding(0, -1*add_ViewHeight, 0,0);
	          break;
	      
	      case PULL: 
	          head_ProgressBar.setVisibility(GONE);
	          head_ArrowImage.setVisibility(VISIBLE);
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE);
	          head_tipText.setText("����ˢ��");
	          head_ArrowImage.clearAnimation();
	          if(isBack){
	            head_ArrowImage.startAnimation(Arrow_uptodown);
	            isBack=false;
	          	}
	          break;
	
	      case RELEASE_TO_REFERESH:
	          head_ProgressBar.setVisibility(GONE);
	          head_ArrowImage.setVisibility(VISIBLE);
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE);
	          head_tipText.setText(" �ɿ�ˢ��");
	          head_ArrowImage.clearAnimation();
	          head_ArrowImage.startAnimation(Arrow_downtoup);
	          break; 
	      
	      case REFRESHING:
	          head_ProgressBar.setVisibility(VISIBLE);
	          head_ArrowImage.setVisibility(GONE); 
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE);
	          head_tipText.setText("����ˢ��...");
	          head_ArrowImage.clearAnimation();
	          headView.setPadding(0, 0, 0,0);
	          break;
	      
	      }
	  }
	  /**
	 * ����β����ʾ״̬
	 */
	  private void onFooterStateChange() {
	      switch (state) {
	      
	      case NORMAL:
	          foot_ProgressBar.setVisibility(GONE);
	          foot_ArrowImage.setVisibility(VISIBLE);
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText("���ظ���");
	          foot_ArrowImage.clearAnimation();
	          footView.setPadding(0, -1*add_ViewHeight, 0,0);
	          break;
	      
	      case PULL: 
	          foot_ProgressBar.setVisibility(GONE);
	          foot_ArrowImage.setVisibility(VISIBLE);
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText("���ظ���");
	          foot_ArrowImage.startAnimation(Arrow_up);
	          //foot_ArrowImage.clearAnimation();
	          if(isBack){
	              foot_ArrowImage.startAnimation(Arrow_downtoup);
	              isBack=false;
	          }
	          break;
	          
	      case RELEASE_TO_REFERESH:
	          foot_ProgressBar.setVisibility(GONE);
	          foot_ArrowImage.setVisibility(VISIBLE);
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText(" �ɿ����м���");
	          foot_ArrowImage.clearAnimation();
	          foot_ArrowImage.startAnimation(Arrow_uptodown);
	          break; 
	          
	      case REFRESHING:
	          foot_ProgressBar.setVisibility(VISIBLE);
	          foot_ArrowImage.setVisibility(GONE); 
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText("���ڼ���...");
	          foot_ArrowImage.clearAnimation();
	          footView.setPadding(0, 0, 0,0);
	          break;
	      
	      }  

	      
	  }
	//�ӿں���
      public interface RefreshListener{
	      public void pullDownRefresh();
	      public void pullUpRefresh();
	  }
      
	  public void setRefreshListener(RefreshListener listener){
	      refreshlistener=listener;
	      refereshEnable=true;
	  }
	  
	//����ˢ�º���
	//����ˢ�µ�ʵ�ַ���
	  private void onPullDownRefresh() {
		      if(refreshlistener!=null){
		    	  refreshlistener.pullDownRefresh();
		      }
		  }
	  
	  
	  //����ˢ�µ�ʵ�ַ���
	  private void onPullUpRefresh() {
		      if(refreshlistener!=null)
		    	  refreshlistener.pullUpRefresh();
		  }
	  
	  
}
