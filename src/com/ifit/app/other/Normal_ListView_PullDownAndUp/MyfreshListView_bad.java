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

	
	private View headView,footView;//定义两个布局
	
	
	private int add_ViewHeight,add_ViewWidth;//加入的布局的高和宽
	
	
	private ImageView head_ArrowImage,foot_ArrowImage;//两个布局的箭头图片
	private ProgressBar head_ProgressBar,foot_ProgressBar;//两个布局的进度条
	private TextView head_tipText,foot_tipText;//提示文本（“上拉，下拉等”）
	private TextView head_last_updata_time,foot_last_updata_time;//最后更新的时间
	
	//onTouch 函数里用到的
	//private int FirstVisiblePosition = 0;// 屏幕上第一个可以看见的Item的位子
	//private int LastVisiblePosition = 0;// 这是屏幕上最后一个Item的位子
	//private int totalItem = 0;//总共Item的数量
	private boolean isRecorded;
	//private boolean isTop = false;//判断是否到顶了
	//private boolean isBottom = false;//判断是否到底了
	private float startY,firstTempY = 0,secondTempY = 0;//点下去是的Y坐标，移动后的Y坐标，手指离开是的Y坐标
	private static final float RATIO = 3;//系数
	private boolean isBack = false;//是否回到上一个状态
	
	
	private int state;// 当前头尾布局的状态
	private static final int NORMAL = 0;// 当前的操作状态为——正常状态
	private static final int PULL = 1;// 当前的操作状态为——提示下拉可以刷新的状态
	private static final int RELEASE_TO_REFERESH = 2;// 当前的操作状态为——松开可以刷新的状态
	private static final int REFRESHING = 3;// 当前的操作状态为——正在刷新状态
	private boolean refereshEnable;//是否可以进行刷新
	

	private Animation Arrow_uptodown; //箭头由上朝下动画
	private Animation Arrow_downtoup;//箭头由下朝上动画
	private Animation Arrow_up;//使箭头一直朝上
	private Animation context_back;//整个VIEW匀速返回的动画

	
	public RefreshListener refreshlistener;//设置接口函数在最底下
	
	
	//重写3个父类构造函数
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
		
		//头部初始化
		headView = inflater.inflate(R.layout.listfresh_header_footer, null);
		head_ArrowImage = (ImageView)headView.findViewById(R.id.fresh_arrow_image);
		head_ProgressBar = (ProgressBar)headView.findViewById(R.id.fresh_progressbar);
		head_tipText = (TextView)headView.findViewById(R.id.fresh_tipsText);
		head_last_updata_time = (TextView)headView.findViewById(R.id.fresh_lastUpdatedText);
		head_ArrowImage.setMinimumWidth(70);
	    head_ArrowImage.setMaxHeight(50);
	    
	    
		//尾部初始化
		footView = inflater.inflate(R.layout.listfresh_header_footer, null);
		foot_ArrowImage = (ImageView)footView.findViewById(R.id.fresh_arrow_image);
		foot_ProgressBar = (ProgressBar)footView.findViewById(R.id.fresh_progressbar);
		foot_tipText = (TextView)footView.findViewById(R.id.fresh_tipsText);
		foot_last_updata_time = (TextView)footView.findViewById(R.id.fresh_lastUpdatedText);
		foot_tipText.setText("加载更多");
		foot_last_updata_time.setText("还未加载过...");
		foot_ArrowImage.setMinimumWidth(70);
	    foot_ArrowImage.setMaxHeight(50);
	    
	    
		measureView(headView);
		
		add_ViewHeight=headView.getMeasuredWidth();
		add_ViewWidth=headView.getMeasuredHeight();
	      
		headView.setPadding(0,-1*add_ViewHeight,0,0);//设置 与界面上边距的距离
		headView.invalidate();//控件重绘
	      
	    footView.setPadding(0,-1*add_ViewHeight,0,0);//设置与界面上边距的距离
	    footView.invalidate();//控件重绘
	      
	    addHeaderView(headView);
	    addFooterView(footView);
	    
	    state = NORMAL;    
	    refereshEnable=false;
	    Init_Animation();
	}
	
	public void Init_Animation (){
		
		//这是旋转动画
		Arrow_uptodown=new RotateAnimation(-180,0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		Arrow_uptodown.setDuration(150);//设置动画持续时间
		Arrow_uptodown.setFillAfter(true);//动画执行完后是否停留在执行完的状态
		Arrow_uptodown.setInterpolator(new LinearInterpolator());//定义动画变化速率
	      
		Arrow_downtoup=new RotateAnimation(0,-180, RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		Arrow_downtoup.setDuration(150);
		Arrow_downtoup.setFillAfter(true);
		Arrow_downtoup.setInterpolator(new LinearInterpolator());
	      
		Arrow_up = new RotateAnimation(0,180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		Arrow_up.setFillAfter(true);
		Arrow_up.setDuration(0);
	      
	}
	//对子部局的位子设定
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

	//手指触摸监听,总控制台
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
          firstTempY=ev.getY(); //一按下便赋值给第一个缓存Y坐标
          isRecorded=false; //表示当前Y坐标并未记录
          if(getFirstVisiblePosition()==0){//判断是否到顶了
              if(!isRecorded){//如果没有记录则，把当前的Y坐标记录
                  startY=ev.getY();//记录的意思就是把Y坐标赋值给startY坐标，startY，整个监听中只进行一次赋值
                  isRecorded=true;//表示当前Y坐标已经被记录
              }
          }
  }
	
	public void onMove(MotionEvent ev){
		  
         if (getFirstVisiblePosition()==0 || getFirstVisiblePosition()==1) { //进行移动的时候判断是否移动到顶端了
             firstTempY=secondTempY; //把第二个缓存值赋值给第一个缓存值
             secondTempY=ev.getY();//对第二个缓存值进行赋值
             if (!isRecorded) {//判断是否当前坐标被记录，如果在按下的时候就在顶端，则已经被记录，按下的时候不在顶端就没有被记录
                 startY = secondTempY;//如果没有被记录，则把当前的Y坐标通过第二个缓存值赋值给startY
                 isRecorded = true;//标记当前Y被记录
             }
             if (state != REFRESHING) { //判断当前的状态是否正在刷新，若没有则进行
                 if (state == NORMAL) {//判断当前状态是否为常态,这是在滑到顶端时的判断若为常态，则进行
                     if (secondTempY - startY > 0) {//判断是否手指是往下拉的，大于0就是往下拉，getY越往下越大
                         state = PULL; //则改变顶部布局的状态，为往下拉
                         onHeaderStateChange();
                     }
                 }
                 if (state == PULL) {//上面判断不是常态，这里判断是否正在往下拉，若是则进行
                    if ((secondTempY - startY) / RATIO > add_ViewHeight && secondTempY-firstTempY > 3) {
                    	 //这里判断，下拉距离是否大于布局高度的三倍，至于后面大于3，因为没移动一次，getY就会变化，但是当顶端还在下拉时候
                    	 //position都是为零的，并且上面有将第二缓存Y赋值给第一缓存Y，且一直在变化，一直在判断，一直在赋值
                    	 //后一个值减去前一个值，其实是指移动的块面，当移动的快，是会跳输的，比如当前位子是1，移动的快，可以直接跳3或者4
                    	 //如果移动的慢则1到2再到3，getY也是一样的，慢慢移动可以保持小于3，快速移动便可以大于3或者其他值
                         state = RELEASE_TO_REFERESH;//改变布局状态，如果为下拉，且距离大于高度三倍，则变为松开刷新
                         onHeaderStateChange();
                     } else if (secondTempY - startY <= -5) { //当往上移动，变将布局复原为常态。
                         state = NORMAL;
                         onHeaderStateChange();
                     }
                 }
                 if (state == RELEASE_TO_REFERESH) {//进行判断状态是否为松开刷新
                     if (firstTempY-secondTempY>5) {//和上面一样，大于5其实是移动快慢，进行跳数。
                    	 //这里是反向移动，当跳数大于5，则进行
                         state = PULL;//状态还原为拉住
                         isBack = true;// 从松开刷新 回到的下拉刷新，回到上一个状态
                         onHeaderStateChange();
                     } else if (secondTempY - startY <= -5) {//这个是移动到第一个Item之下，复原布局
                         state = NORMAL; 
                         onHeaderStateChange();
                     }
                 }
                 if (state == PULL || state == RELEASE_TO_REFERESH) {//判断当状态为正在拉，或者松开刷新的状态，对头布局的内边距进行动态更改。随着拉动变大
                     headView.setPadding(0, (int) ((secondTempY - startY)/ RATIO - add_ViewHeight), 0, 0);
                 }
             } 
         }
         if(getLastVisiblePosition()==getCount()-2||getLastVisiblePosition()==getCount()-1){//进行判断是否到底，或者到倒数第二个
             firstTempY=secondTempY; //和到顶端一样，将第二个缓存值赋值给第一个缓存值
             secondTempY=ev.getY(); //对第二个缓存值进行赋值
             if (!isRecorded) { //判断是否记录过,若没被记录则进行
                 startY = secondTempY; //则将当前Y坐标通过第二缓存值进行赋值
                 isRecorded = true;//标记为被记录
             }
             
             if(state!=REFRESHING){//不是正在刷新状态
                 if (state == NORMAL) {//判断是否为常态，是则进行
                     if (startY - secondTempY> 0) {//判断是否为往上拉，getY往上变小，开始的值大于后来的值，说明往上拉
                         state = PULL; //已经在底部，继续往上拉，将状态改为正在拉
                         onFooterStateChange();
                     }
                 }
                 if (state == PULL) {//判断是否正在拉，是则进行
                     if ((startY-secondTempY) / RATIO > add_ViewHeight && firstTempY-secondTempY>=9) {
                    	 //判断上拉的距离是否是布局高度的3倍，后面的大于等于9是和前面一样，是一种往下移动快慢的判断，慢慢往下，则不会大于9
                    	 //若快速，则会跳数，就会大于9，第一缓存数是前面的值，第二缓存数是最获得的值，前面的值大于后面的，说明往上拉
                    	 //因为getY往上变小
                         state = RELEASE_TO_REFERESH;//则把正在拉改为松开刷新状态
                         onFooterStateChange();
                     } else if (startY-secondTempY <= 0) {//如果第一次赋值的Y坐标小于最新获得的现在的坐标，说明正在往下
                    	 //并且已经到了第一次赋值坐标之下，则将状态恢复到常态
                         // 上拉刷新 --》 回到 刷新完成
                         state = NORMAL;
                         onFooterStateChange();
                     }
                 }
                 if (state == RELEASE_TO_REFERESH) {//判断状态是否为松开刷新，若是则进行
                     if(firstTempY-secondTempY<-5){//也是跳数判断，但是第一次的值小于第二次，说明前面小于后面，说明是往下
                         state = PULL;//则将状态恢复为正在拉
                         isBack = true;// 从松开刷新 回到的上拉刷新
                         onFooterStateChange();
                     } else if (secondTempY-startY >= 0) {//和前面一样，到了第一次赋值之下，则将状态恢复常态
                         // 松开刷新 --》 回到 刷新完成
                         state = NORMAL; 
                         onFooterStateChange();
                     }
                 }
                 if((state==PULL||state==RELEASE_TO_REFERESH)&&secondTempY<startY){
                	 //若状态为正在拉或者放开刷新，并且最新的Y坐标一直在开始赋值坐标之上，则进行动态改变布局内边距
                     footView.setPadding(0, 0, 0, (int) ((startY-secondTempY)/RATIO-add_ViewHeight));
                 }
             }
         }
		  
	  }
	
	 public void onAction_Up(MotionEvent ev){//当手指抬起时
		  
         if(state != REFRESHING){ //判断是否为正在刷新状态，若不是则进行
             if(state == PULL){//判断是否是正在拉的状态，若是则进行
                 state = NORMAL;//正在拉的状态，放手则将状态恢复为常态
                 if(getFirstVisiblePosition()==0)//下拉
                  onHeaderStateChange();
                 if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2)//上拉
                     onFooterStateChange();
             }
             
             if(state == RELEASE_TO_REFERESH){//判断是否为松开刷新的状态，若是则进行
                 state = REFRESHING; //松开将状态改为，正在刷新
                 if(getFirstVisiblePosition()==0){
                     //下拉
                     onHeaderStateChange(); 
                     onPullDownRefresh();//刷新得到服务器数据，进行刷新
                 }
                 if(getLastVisiblePosition()==getCount()-1||getLastVisiblePosition()==getCount()-2){
                     //上拉
                     onFooterStateChange();
                     onPullUpRefresh();//刷新得到服务器数据
                 }
             }
         }
	  }
	 
	 
	 //获取最后一次刷新的时间
	 //下拉完成获取时间
		  public void onPulldownRefreshComplete(){
		      state=NORMAL;
		      onHeaderStateChange();
		      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		      head_last_updata_time.setText("最后刷新时间："+sdf.format(new Date()));
		  }
	//上拉完成获取时间
		  public void onPullupRefreshComplete(){
		      state=NORMAL;
		      onFooterStateChange();
		      SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		      foot_last_updata_time.setText("最后刷新时间："+sdf.format(new Date()));
		  }
		  
		  
	 /**
	 * 更改头部显示状态
	 */
	  private void onHeaderStateChange() {
	      switch (state) {
	      
	      case NORMAL:
	          head_ProgressBar.setVisibility(GONE);
	          head_ArrowImage.setVisibility(VISIBLE);
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE); 
	          head_tipText.setText("下拉刷新");
	          head_ArrowImage.clearAnimation(); 
	          headView.setPadding(0, -1*add_ViewHeight, 0,0);
	          break;
	      
	      case PULL: 
	          head_ProgressBar.setVisibility(GONE);
	          head_ArrowImage.setVisibility(VISIBLE);
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE);
	          head_tipText.setText("下拉刷新");
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
	          head_tipText.setText(" 松开刷新");
	          head_ArrowImage.clearAnimation();
	          head_ArrowImage.startAnimation(Arrow_downtoup);
	          break; 
	      
	      case REFRESHING:
	          head_ProgressBar.setVisibility(VISIBLE);
	          head_ArrowImage.setVisibility(GONE); 
	          head_tipText.setVisibility(VISIBLE);
	          head_last_updata_time.setVisibility(VISIBLE);
	          head_tipText.setText("正在刷新...");
	          head_ArrowImage.clearAnimation();
	          headView.setPadding(0, 0, 0,0);
	          break;
	      
	      }
	  }
	  /**
	 * 更改尾部显示状态
	 */
	  private void onFooterStateChange() {
	      switch (state) {
	      
	      case NORMAL:
	          foot_ProgressBar.setVisibility(GONE);
	          foot_ArrowImage.setVisibility(VISIBLE);
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText("加载更多");
	          foot_ArrowImage.clearAnimation();
	          footView.setPadding(0, -1*add_ViewHeight, 0,0);
	          break;
	      
	      case PULL: 
	          foot_ProgressBar.setVisibility(GONE);
	          foot_ArrowImage.setVisibility(VISIBLE);
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText("加载更多");
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
	          foot_tipText.setText(" 松开进行加载");
	          foot_ArrowImage.clearAnimation();
	          foot_ArrowImage.startAnimation(Arrow_uptodown);
	          break; 
	          
	      case REFRESHING:
	          foot_ProgressBar.setVisibility(VISIBLE);
	          foot_ArrowImage.setVisibility(GONE); 
	          foot_tipText.setVisibility(VISIBLE);
	          foot_last_updata_time.setVisibility(VISIBLE);
	          foot_tipText.setText("正在加载...");
	          foot_ArrowImage.clearAnimation();
	          footView.setPadding(0, 0, 0,0);
	          break;
	      
	      }  

	      
	  }
	//接口函数
      public interface RefreshListener{
	      public void pullDownRefresh();
	      public void pullUpRefresh();
	  }
      
	  public void setRefreshListener(RefreshListener listener){
	      refreshlistener=listener;
	      refereshEnable=true;
	  }
	  
	//数据刷新函数
	//下拉刷新的实现方法
	  private void onPullDownRefresh() {
		      if(refreshlistener!=null){
		    	  refreshlistener.pullDownRefresh();
		      }
		  }
	  
	  
	  //上拉刷新的实现方法
	  private void onPullUpRefresh() {
		      if(refreshlistener!=null)
		    	  refreshlistener.pullUpRefresh();
		  }
	  
	  
}
