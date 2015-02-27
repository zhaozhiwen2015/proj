package cn.zhaozhiwen.fivepointchess;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

	private  int SCREEN_WIDTH = 480;
	private  int SCREEN_HEIGHT = 320;
	private  int radius = 34;//锟斤拷锟教凤拷锟斤拷锟斤�?
	private  int offset = 17+17;//偏锟斤拷锟斤�?
	public  Bitmap[] bms = new Bitmap[6];
	private  int USER = 1;//1锟阶凤拷   -1锟节凤拷
	private  boolean GAMEOVER = false;
	private  boolean isOnline = false;
	private  boolean isTipShow = false;
	private int TIP_HEIGHT = 80;
	private String TIP_TEXT = "";
	private List<String> msgLst = new ArrayList<String>();
	
	public  Bitmap[]  load() {
		if(persistentMap.containsKey("BMS")){
			bms = (Bitmap[])persistentMap.get("BMS");
		}else{
		     bms[0] = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.white)).getBitmap();
		     bms[1] = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.black)).getBitmap();
		     bms[2] = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.bg)).getBitmap();	
		     bms[3] = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.bg2)).getBitmap();	
		     bms[4] = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.white1)).getBitmap();
		     bms[5] = ((BitmapDrawable)this.getResources().getDrawable(R.drawable.black1)).getBitmap();
		     persistentMap.put("BMS", bms);
		}
	    return bms;
	}

	public GameView(Context context) {
		super(context);
	}
	public GameView(Context context,int w,int h,boolean isOnline) {
		super(context);
		this.setFocusable(true);
		if(w<h){
			int tmp = w;
			w = h;
			h = tmp;
		}
		this.SCREEN_WIDTH = w;
		this.SCREEN_HEIGHT = h;   
		radius = SCREEN_HEIGHT/(SIZE+1);
		offset = radius ;
		this.isOnline = isOnline;
		load();
		
		persistentMap.remove("LM");
		persistentMap.remove("type");
		persistentMap.remove("USER");
		persistentMap.remove("GAMEOVER");
		persistentMap.remove("isOnline");
		persistentMap.remove("ORDER");
		init();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
		USER = (Integer)persistentMap.get("USER");
		isOnline = (Boolean)persistentMap.get("isOnline");

		//draw white black tiles
		canvas.drawText(isOnline?"网络版V0.0.1":"单机版V0.0.1", SCREEN_WIDTH-84, 14, paint);
		int fontSize = 14;
		paint.setTextScaleX(2);
		paint.setTextSize(fontSize);
		canvas.drawBitmap(bms[4], SIZE*radius+offset, radius+(radius-bms[4].getWidth())/2, paint);
		if(!isOnline){
			canvas.drawText((USER==1)?"玩家":"电脑", SIZE*radius+offset+radius, radius+radius-2-(radius-fontSize)/2, paint);			
		}else{
			canvas.drawText((USER==1)?"我方":"网友", SIZE*radius+offset+radius, radius+radius-2-(radius-fontSize)/2, paint);	
		}
		canvas.drawBitmap(bms[5], SIZE*radius+offset, 2*radius+(radius-bms[4].getWidth())/2, paint);
		if(!isOnline){
			canvas.drawText((USER==1)?"电脑":"玩家", SIZE*radius+offset+radius, 2*radius+radius-2-(radius-fontSize)/2, paint);			
		}else{
			canvas.drawText((USER==1)?"网友":"我方", SIZE*radius+offset+radius, 2*radius+radius-2-(radius-fontSize)/2, paint);	
		}
		
		int fontSizeMsg = radius*6/15;//12;
		paint.setTextSize(fontSizeMsg);
		paint.setTextScaleX(1);
		if(msgLst.size()>0){
			int idx = -1;
			int start  = (msgLst.size()>15)?14:(msgLst.size()-1);
			for(int i=msgLst.size()-1;i>=0;i--){
				if(idx>13)break;
				canvas.drawText(msgLst.get(i), SIZE*radius+offset, (++idx)*fontSizeMsg+3*radius+fontSizeMsg, paint);
			}	
		}
		
//		canvas.drawText(getDir()+"--remains:"+data.getTileRemains()+(GAMEOVER?"!!GAMEOVER":""), 0, 10, paint);
		paint.setColor(Color.BLACK);
		for(int i=0;i<SIZE;i++){
			paint.setTextSize(12);
			canvas.drawText(""+change(i+1), radius*i+offset-5, radius/2, paint);
			canvas.drawLine(offset, radius*i+offset, radius*(SIZE-1)+offset, radius*i+offset, paint);			
		}
		for(int i=0;i<SIZE;i++){
			paint.setTextSize(12);
			canvas.drawText(" "+(i+1), 0,radius*i+offset+5, paint);
			canvas.drawLine(radius*i+offset , offset, radius*i+offset , radius*(SIZE-1)+offset, paint);			
		}
		locationMartrix = (int[][])persistentMap.get("LM");
		XXC = (int[][])persistentMap.get("XXC");
		YYC = (int[][])persistentMap.get("YYC");
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
					if(locationMartrix[i][j]==0){
					}else if(locationMartrix[i][j]==1){
						canvas.drawBitmap(bms[0], i*radius-radius/2+offset, j*radius-radius/2+offset, paint);
						paint.setColor(Color.BLACK);
						String str = String.valueOf(XXC[i][j]);
						int length = str.length();
						int os = 5;
						if(length==2){
							os =10;
						}
						canvas.drawText(str, i*radius+offset-os, j*radius+offset+5, paint);
					}else if(locationMartrix[i][j]==-1){
						canvas.drawBitmap(bms[1], i*radius-radius/2+offset, j*radius-radius/2+offset, paint);
						paint.setColor(Color.WHITE);
						String str = String.valueOf(YYC[i][j]);
						int length = str.length();
						int os = 5;
						if(length==2){
							os =10;
						}
						canvas.drawText(str, i*radius+offset-os, j*radius+offset+5, paint);
					}					
			}
		}
		GAMEOVER = (Boolean)persistentMap.get("GAMEOVER");
		if(GAMEOVER){	
			Rect rect = new Rect(0,(SCREEN_HEIGHT-TIP_HEIGHT)/2,SCREEN_WIDTH,(SCREEN_HEIGHT+TIP_HEIGHT)/2);
			paint.setColor(Color.rgb(255, 0, 0));
			canvas.drawRect(rect, paint);
			paint.setColor(Color.rgb(255, 255, 255));
			paint.setTextSize(20);
			canvas.drawText(TIP_TEXT,SCREEN_WIDTH/2-45, (SCREEN_HEIGHT-20)/2+10 , paint);
			isTipShow = true;;
		}
		paint.setColor(Color.BLACK);
		paint.setTextSize(12);
		canvas.drawText("建议分辨率320X480,网络版正在内侧中,敬请关注!",0, (SCREEN_HEIGHT)-2 , paint);
		
		
  	}
	private String change(int idx){
		return ""+(char)(idx+64);
	}
	private void back(){
		FivepointChessActivity.back();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();
		super.onTouchEvent(event);
		if(Boolean.TRUE==(Boolean)persistentMap.get("GAMEOVER")&&!isTipShow){
			return true;
	}else if(Boolean.TRUE==(Boolean)persistentMap.get("GAMEOVER")&&isTipShow){
				if(y>((SCREEN_HEIGHT-TIP_HEIGHT)/2)&&y<((SCREEN_HEIGHT+TIP_HEIGHT)/2)&&x>0&&x<SCREEN_WIDTH){
					persistentMap.remove("LM");
					persistentMap.remove("type");
					persistentMap.remove("USER");
					persistentMap.remove("GAMEOVER");
					persistentMap.remove("ORDER");
					init();					
				}
		}else if(event.getAction()==MotionEvent.ACTION_DOWN && !isTipShow){
			click(x,y);	
		}
		return true;
	}
	public  void  click(final float x ,final float y ){
		USER = (Integer)persistentMap.get("USER");
		type = (Integer)persistentMap.get("type");
		GAMEOVER = (Boolean)persistentMap.get("GAMEOVER");
		if(USER!=type||GAMEOVER)return;
		new Thread(new Runnable(){
			public void run(){
				locationMartrix = (int[][])persistentMap.get("LM");
				XXC = (int[][])persistentMap.get("XXC");
				YYC = (int[][])persistentMap.get("YYC");
				ORDER = (Integer)persistentMap.get("ORDER");
				for(int i=0;i<SIZE;i++){
					for(int j=0;j<SIZE;j++){
						if(locationMartrix[i][j]==0&&(x>(i*radius-radius/2+offset)&&x<(i*radius+radius/2+offset)&&y>(j*radius-radius/2+offset)&&y<(j*radius+radius/2+offset))){
							locationMartrix[i][j]=type;
							if(type==1){
								XXC[i][j] = ORDER+1;
								persistentMap.put("XXC", XXC);
								msgLst.add("玩家:"+(ORDER+1)+"[ "+(j+1)+","+change(i+1)+" ]");
							}else{
								YYC[i][j] = ORDER+1;
								persistentMap.put("YYC", YYC);
								msgLst.add("电脑:"+(ORDER+1)+"[ "+(j+1)+","+change(i+1)+" ]");
							}
							persistentMap.put("ORDER", ORDER+1);
							persistentMap.put("LM", locationMartrix);
							FivepointChessActivity.sound();
							postInvalidate();
							if(check(type)){
								persistentMap.put("GAMEOVER", true);
								isOnline = (Boolean)persistentMap.get("isOnline");
								if(isOnline){
									TIP_TEXT = "我方胜!";									
								}else{
									TIP_TEXT = "玩家胜!";	
								}
								postInvalidate();
							}else{
								persistentMap.put("type", -USER);
								if(!isOnline){
									robot();											
								}else{
								   send(REQUEST_TYPE_LOCATION,i,j,ORDER+1);
								}
							}
							break;
						}			
					}
				}						
			}					
	  }).start();
	}
	private  Runnable runnable= new  Runnable(){
		public void run(){
				   int cns=0;
			       USER = (Integer)persistentMap.get("USER");
			       type = (Integer)persistentMap.get("type");
			       GAMEOVER = (Boolean)persistentMap.get("GAMEOVER");
				   if(USER!=type&&!GAMEOVER){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					
					}
					if(!full()){
						XXC = (int[][])persistentMap.get("XXC");
						YYC = (int[][])persistentMap.get("YYC");
						ORDER = (Integer)persistentMap.get("ORDER");
						int[] xy = beforeGet(type);
						locationMartrix[xy[0]][xy[1]] = type;
						if(type==1){
							XXC[xy[0]][xy[1]] = ORDER+1;
							persistentMap.put("XXC", XXC);
						}else{
							YYC[xy[0]][xy[1]] = ORDER+1;
							persistentMap.put("YYC", YYC);
						}
						msgLst.add("电脑:"+(ORDER+1)+"[ "+(xy[1]+1)+","+change(xy[0]+1)+" ]");
						persistentMap.put("ORDER", ORDER+1);
						persistentMap.put("LM", locationMartrix);
						FivepointChessActivity.sound();
						 postInvalidate();
						cns++;
						if(check(type)){
							System.out.println(type+": WIN!!"+cns);
							persistentMap.put("GAMEOVER",true);
						    TIP_TEXT = "电脑胜!";					
						    postInvalidate();
						}
						persistentMap.put("type",USER);
				 }
			  }
			if(full()){
				System.out.println("~~full!!"+cns);
				persistentMap.put("GAMEOVER",true);
				TIP_TEXT = "此局双方和棋!";	
				postInvalidate();
			}		
	
		}	
	};
   //Core
	public  int SIZE = 9;
	public  int type = 1;
	public  int[][] locationMartrix = {};
	private  int[][] XX = {};
	private  int[][] YY = {};
	private  int[][] XXC = {};
	private  int[][] YYC = {};
	private int ORDER = 0;
	public  void init(){
		msgLst.clear();
		isTipShow = false;
		if(persistentMap.containsKey("type")){
			type = (Integer)persistentMap.get("type");
		}else{
			type = 1;
			persistentMap.put("type", type);
		}
		if(persistentMap.containsKey("USER")){
			USER = (Integer)persistentMap.get("USER");
		}else{
			persistentMap.put("USER", USER);
		}
		if(persistentMap.containsKey("GAMEOVER")){
			GAMEOVER = (Boolean)persistentMap.get("GAMEOVER");
		}else{
			GAMEOVER = false;
			persistentMap.put("GAMEOVER", GAMEOVER);
		}
		if(persistentMap.containsKey("isOnline")){
			isOnline = (Boolean)persistentMap.get("isOnline");
		}else{
			persistentMap.put("isOnline", this.isOnline);
		}
		if(persistentMap.containsKey("ORDER")){
			ORDER = (Integer)persistentMap.get("ORDER");
		}else{
			ORDER = 0;
			persistentMap.put("ORDER", this.ORDER);
		}
		
		if(persistentMap.containsKey("LM")){
			locationMartrix = (int[][])persistentMap.get("LM");
			XX = locationMartrix;
			YY = locationMartrix;
		}else{
			locationMartrix = new int[SIZE][SIZE];
			XX = new int[SIZE][SIZE];
			YY = new int[SIZE][SIZE];
			XXC = new int[SIZE][SIZE];
			YYC = new int[SIZE][SIZE];
			for(int i=0;i<SIZE;i++){
				for(int j=0;j<SIZE;j++){
					locationMartrix[i][j]=0;
					XX[i][j]=0;
					YY[i][j]=0;
					XXC[i][j]=0;
					YYC[i][j]=0;
				}
			}			
			persistentMap.put("XXC", XXC);
			persistentMap.put("YYC", YYC);
			persistentMap.put("LM", locationMartrix);
		}
		postInvalidate();
		if(!isOnline){
			robot();
		}else{
			initNetVersion();
		}

	}
	public  void robot(){
		new Thread(runnable).start();
	}
	public  boolean check(int type){
		locationMartrix = (int[][])persistentMap.get("LM");
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==type){
					return true;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==type){
					return true;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==type){
					return true;
				}
			}
		}
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==type){
					return true;
				}
			}
		}
		return false;
	}
	public  boolean full(){
		locationMartrix = (int[][])persistentMap.get("LM");
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0){
					return false;
				}
			}
		}
		return true;
	}
	/*public  void print(int type){//0 main 1XX -1YY
		System.out.println("+++++++++++++"+type+"+++++++++++++++");
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(type==0){
					if(locationMartrix[i][j]==0){
						System.out.print("--");
					}else if(locationMartrix[i][j]==1){
						System.out.print("锟斤�?);
					}else if(locationMartrix[i][j]==-1){
						System.out.print("锟斤�?);
					}					
				}else if(type==1){
					System.out.print(XX[i][j]);
				}else if(type==-1){
					System.out.print(YY[i][j]);
				}
			}
			System.out.println();
		}
	}*/
	public  void empty(int type){
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
			    if(type==1)XX[i][j]=0;
			    else if(type==-1)YY[i][j]=0;
			}
		}
	}
	public  int[] beforeGet(int type){
		scan(type);
		scan(-type);
		int[] ff = new int[2];
		//103  202 301锟斤拷锟斤拷
		//4+0 0+4 锟斤拷锟斤拷
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==type){
			       ff[0] = i;
			       ff[1] = j;
			       return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==0){
					ff[0] = i+4;
				    ff[1] = j+4;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==0&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==type){
					ff[0] = i+2;
				    ff[1] = j+2;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==0&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==type){
					ff[0] = i+1;
				    ff[1] = j+1;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==0&&locationMartrix[i+4][j+4]==type){
					ff[0] = i+3;
				    ff[1] = j+3;
				    return ff;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==type){
					   ff[0] = i;
				       ff[1] = j;
				       return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==0){
					ff[0] = i-4;
				    ff[1] = j+4;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==0&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==type){
					ff[0] = i-2;
				    ff[1] = j+2;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==0&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==type){
					ff[0] = i-1;
				    ff[1] = j+1;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==0&&locationMartrix[i-4][j+4]==type){
					ff[0] = i-3;
				    ff[1] = j+3;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==0){
					ff[0] = i;
				    ff[1] = j+4;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==0&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==type){
					ff[0] = i;
				    ff[1] = j+2;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==0&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==type){
					ff[0] = i;
				    ff[1] = j+1;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==0&&locationMartrix[i][j+4]==type){
					ff[0] = i;
				    ff[1] = j+3;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==0){
					ff[0] = i+4;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==0&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==type){
					ff[0] = i+2;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==0&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==type){
					ff[0] = i+1;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==0&&locationMartrix[i+4][j]==type){
					ff[0] = i+3;
				    ff[1] = j;
				    return ff;
				}
			}
		}
		//103  202 301锟皆凤拷
		//4+0 0+4 锟皆凤拷
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==-type){
			       ff[0] = i;
			       ff[1] = j;
			       return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==0){
					ff[0] = i+4;
				    ff[1] = j+4;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==0&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==-type){
					ff[0] = i+2;
				    ff[1] = j+2;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==0&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==-type){
					ff[0] = i+1;
				    ff[1] = j+1;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==0&&locationMartrix[i+4][j+4]==-type){
					ff[0] = i+3;
				    ff[1] = j+3;
				    return ff;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==-type){
					   ff[0] = i;
				       ff[1] = j;
				       return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==0){
					ff[0] = i-4;
				    ff[1] = j+4;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==0&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==-type){
					ff[0] = i-2;
				    ff[1] = j+2;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==0&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==-type){
					ff[0] = i-1;
				    ff[1] = j+1;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==0&&locationMartrix[i-4][j+4]==-type){
					ff[0] = i-3;
				    ff[1] = j+3;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==-type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==0){
					ff[0] = i;
				    ff[1] = j+4;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==0&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==-type){
					ff[0] = i;
				    ff[1] = j+2;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==0&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==-type){
					ff[0] = i;
				    ff[1] = j+1;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==0&&locationMartrix[i][j+4]==-type){
					ff[0] = i;
				    ff[1] = j+3;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==-type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==0){
					ff[0] = i+4;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==0&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==-type){
					ff[0] = i+2;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==0&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==-type){
					ff[0] = i+1;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==0&&locationMartrix[i+4][j]==-type){
					ff[0] = i+3;
				    ff[1] = j;
				    return ff;
				}
			}
		}
		//0+3+0锟皆凤拷  201 102
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==0){
			        if(type==1){
			        	if(YY[i][j]>YY[i+4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j+4;
			        	}
			        }else if(type==-1){
			        	if(XX[i][j]>XX[i+4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j+4;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE-3;i++){
			for(int j=0;j<SIZE-3;j++){
				if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==0&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type){
	        		ff[0] = i+1;
	        		ff[1] = j+1;      
			        return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==0&&locationMartrix[i+3][j+3]==-type){
	        		ff[0] = i+2;
	        		ff[1] = j+2;      
			        return ff;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==0){
			        if(type==1){
			        	if(YY[i][j]>YY[i-4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i-4;
			        		ff[1] = j+4;
			        	}
			        }else if(type==-1){
			        	if(XX[i][j]>XX[i-4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i-4;
			        		ff[1] = j+4;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=3;i<SIZE;i++){
			for(int j=0;j<SIZE-3;j++){
				if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==0&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type){
	        		ff[0] = i-1;
	        		ff[1] = j+1;
			        return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==0&&locationMartrix[i-3][j+3]==-type){
	        		ff[0] = i-2;
	        		ff[1] = j+2;
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==0){
			        if(type==1){
			        	if(YY[i][j]>YY[i][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i;
			        		ff[1] = j+4;
			        	}
			        }else if(type==-1){
			        	if(XX[i][j]>XX[i][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i;
			        		ff[1] = j+4;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-3;j++){
				if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==0&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type){
	        		ff[0] = i;
	        		ff[1] = j+1;	
			        return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==0&&locationMartrix[i][j+3]==-type){
	        		ff[0] = i;
	        		ff[1] = j+2;	
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==0){
			        if(type==1){
			        	if(YY[i][j]>YY[i+4][j]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j;
			        	}
			        }else if(type==-1){
			        	if(XX[i][j]>XX[i+4][j]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE-3;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==0&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type){
	        		ff[0] = i+1;
	        		ff[1] = j;
			        return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==0&&locationMartrix[i+3][j]==-type){
	        		ff[0] = i+2;
	        		ff[1] = j;
			        return ff;
				}
			}
		}
		//0+3+0锟斤拷锟斤拷
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==0){
			        if(type==1){
			        	if(XX[i][j]>XX[i+4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j+4;
			        	}
			        }else if(type==-1){
			        	if(YY[i][j]>YY[i+4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j+4;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE-3;i++){
			for(int j=0;j<SIZE-3;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==0&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type){
	        		ff[0] = i+1;
	        		ff[1] = j+1;
			        return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==0&&locationMartrix[i+3][j+3]==type){
	        		ff[0] = i+2;
	        		ff[1] = j+2;
			        return ff;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==0){
			        if(type==1){
			        	if(XX[i][j]>XX[i-4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i-4;
			        		ff[1] = j+4;
			        	}
			        }else if(type==-1){
			        	if(YY[i][j]>YY[i-4][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i-4;
			        		ff[1] = j+4;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=3;i<SIZE;i++){
			for(int j=0;j<SIZE-3;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==0&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type){
	        		ff[0] = i-1;
	        		ff[1] = j+1;
			        return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==0&&locationMartrix[i-3][j+3]==type){
	        		ff[0] = i-2;
	        		ff[1] = j+2;
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-4;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==0){
			        if(type==1){
			        	if(XX[i][j]>XX[i][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i;
			        		ff[1] = j+4;
			        	}
			        }else if(type==-1){
			        	if(YY[i][j]>YY[i][j+4]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i;
			        		ff[1] = j+4;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-3;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==0&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type){
	        		ff[0] = i;
	        		ff[1] = j+1;
			        return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==0&&locationMartrix[i][j+3]==type){
	        		ff[0] = i;
	        		ff[1] = j+2;
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE-4;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==0){
			        if(type==1){
			        	if(XX[i][j]>XX[i+4][j]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j;
			        	}
			        }else if(type==-1){
			        	if(YY[i][j]>YY[i+4][j]){
			        		ff[0] = i;
			        		ff[1] = j;
			        	}else{
			        		ff[0] = i+4;
			        		ff[1] = j;
			        	}
			        }
			        return ff;
				}
			}
		}
		for(int i=0;i<SIZE-3;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==0&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type){
	        		ff[0] = i+1;
	        		ff[1] = j;
			        return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==0&&locationMartrix[i+3][j]==type){
	        		ff[0] = i+2;
	        		ff[1] = j;
			        return ff;
				} 
			}
		}
		
		return get(type);
	}
	public  int[] get(int type){
		int[] ff = new int[2];
		int flag = 0;
		int tmp = 0;
		int x = 0,y = 0;
		for(int i=SIZE/2;i>=0;i--){
			for(int j=SIZE/2;j>=0;j--){
				if(1==type){
					if(0==flag){
						flag = 1;
						tmp = YY[i][j];
						x = i;
						y = j;
					}else{
						if(YY[i][j]>tmp){
							tmp = YY[i][j];
							x = i;
							y = j;
						}
					}
				}else if(-1==type){
					if(0==flag){
						flag = 1;
						tmp = XX[i][j];
						x = i;
						y = j;
					}else{
						if(XX[i][j]>tmp){
							tmp = XX[i][j];
							x = i;
							y = j;
						}
					}
				}
			}
		}
		for(int i=SIZE/2;i<SIZE;i++){
			for(int j=SIZE/2;j<SIZE;j++){
				if(1==type){
					if(0==flag){
						flag = 1;
						tmp = YY[i][j];
						x = i;
						y = j;
					}else{
						if(YY[i][j]>tmp){
							tmp = YY[i][j];
							x = i;
							y = j;
						}
					}
				}else if(-1==type){
					if(0==flag){
						flag = 1;
						tmp = XX[i][j];
						x = i;
						y = j;
					}else{
						if(XX[i][j]>tmp){
							tmp = XX[i][j];
							x = i;
							y = j;
						}
					}
				}
			}
		}
		for(int i=SIZE/2;i>=0;i--){
			for(int j=SIZE/2;j<SIZE;j++){
				if(1==type){
					if(0==flag){
						flag = 1;
						tmp = YY[i][j];
						x = i;
						y = j;
					}else{
						if(YY[i][j]>tmp){
							tmp = YY[i][j];
							x = i;
							y = j;
						}
					}
				}else if(-1==type){
					if(0==flag){
						flag = 1;
						tmp = XX[i][j];
						x = i;
						y = j;
					}else{
						if(XX[i][j]>tmp){
							tmp = XX[i][j];
							x = i;
							y = j;
						}
					}
				}
			}
		}
		for(int i=SIZE/2;i<SIZE;i++){
			for(int j=SIZE/2;j>=0;j--){
				if(1==type){
					if(0==flag){
						flag = 1;
						tmp = YY[i][j];
						x = i;
						y = j;
					}else{
						if(YY[i][j]>tmp){
							tmp = YY[i][j];
							x = i;
							y = j;
						}
					}
				}else if(-1==type){
					if(0==flag){
						flag = 1;
						tmp = XX[i][j];
						x = i;
						y = j;
					}else{
						if(XX[i][j]>tmp){
							tmp = XX[i][j];
							x = i;
							y = j;
						}
					}
				}
			}
		}
		ff[0]=x;
		ff[1]=y;
		return ff;
	}
	public  void scan(int type){
		empty(type);
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0){
					int x = i,y=j;
					while((--x)>=0){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
						if(1==type)XX[i][j]++;
						else if(-1==type)YY[i][j]++;
						break;
						}
					}
					x = i;y=j;
					while((++x)<SIZE){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
							break;
						}
					}
					x = i;y=j;
					while((--y)>=0){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
						    break;
						}
					}
					x = i;y=j;
					while((++y)<SIZE){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
							break;
						}
					}
					x = i;y=j;
					while(((++y)<SIZE)&&((++x)<SIZE)){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
							break;
						}
					}
					x = i;y=j;
					while(((--y)>=0)&&((--x)>=0)){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
							break;
						}
					}
					x = i;y=j;
					while(((++y)<SIZE)&&((--x)>=0)){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
							break;
						}
					}
					x = i;y=j;
					while(((--y)>=0)&&((++x)<SIZE)){
						if(locationMartrix[x][y]==-type){
							break;
						}
						if(locationMartrix[x][y]==type){
							if(1==type)XX[i][j]++;
							else if(-1==type)YY[i][j]++;
							break;
						}
					}
				}
			}
		}
		
	}

	public int[][] getLocationMartrix() {
		return locationMartrix;
	}

	public void setLocationMartrix(int[][] locationMartrix) {
		this.locationMartrix = locationMartrix;
	}
//鍦ㄧ嚎鐗堟湰
	private String SERVER_IP = "192.168.0.102";
	//private String SERVER_IP = "14.23.153.66";
	private Integer SERVER_TCP_PORT = 8888;
	private Integer SERVER_UDP_PORT = 8889;
	private static Integer CLIENT_UDP_PORT = 5554;
	private static Integer REQUEST_TYPE_CONNECT = -10;
	private static Integer REQUEST_TYPE_LOCATION = -11;
	public static Integer REQUEST_TYPE_DISCONNECT = -12;
	private static Integer REQUEST_TYPE_DISCONNECTED = -13;
	private Integer id = 0;
	private Integer pid = 0;
	private static Map<String, Object> persistentMap = new 	ConcurrentHashMap<String, Object>();
	private DatagramSocket ds ;
	
	public static Map<String, Object> getPersistentMap() {
		return persistentMap;
	}


	public static void setPersistentMap(Map<String, Object> persistentMap) {
		GameView.persistentMap = persistentMap;
	}

	public void initNetVersion(){
		Socket socket = null;
		try {
			if(persistentMap.containsKey("SOCKET")){
				socket = (Socket)persistentMap.get("SOCKET");
				id =  (Integer)persistentMap.get("Id");
				USER =  (Integer)persistentMap.get("USER");
				pid =  (Integer)persistentMap.get("pid");
			}else{
				socket = new Socket(SERVER_IP,SERVER_TCP_PORT);
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(CLIENT_UDP_PORT);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				id = dis.readInt();
				USER = dis.readInt();
				pid =  dis.readInt();
				System.out.println("pid="+pid);
				persistentMap.put("SOCKET", socket);
				persistentMap.put("ID", id);
				persistentMap.put("USER", USER);
				persistentMap.put("pid", pid);
				dis.close();
				dos.close();
				socket.close();
			}
			if(persistentMap.containsKey("DS")){
				ds = (DatagramSocket)persistentMap.get("DS");
			}else{
				ds = new DatagramSocket(CLIENT_UDP_PORT);
				persistentMap.put("DS", ds);
			}
			new Thread(new UDPRECVThread()).start();
			
		} catch (UnknownHostException e) {
			Log.i("Exception", e.getMessage(),e);
		} catch (IOException e) {
			Log.i("Exception", e.getMessage(),e);
			e.printStackTrace();
		}
		
	}
	public void send(Integer type0,Integer...params) {
	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			ds = (DatagramSocket)persistentMap.get("DS");
			id =  (Integer)persistentMap.get("ID");
			pid =  (Integer)persistentMap.get("pid");
			if(type0==REQUEST_TYPE_LOCATION){
				if(null==ds){
					ds = new DatagramSocket(CLIENT_UDP_PORT);
				}
				dos.writeInt(REQUEST_TYPE_LOCATION);
				dos.writeInt(pid);
				dos.writeInt(id);
				dos.writeInt(params[0]);
				dos.writeInt(params[1]);
				dos.writeInt(params[2]);
			}else if(type0==REQUEST_TYPE_DISCONNECT){
				dos.writeInt(REQUEST_TYPE_DISCONNECT);
				dos.writeInt(pid);
				dos.writeInt(id);				
			}
		    byte[] buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(SERVER_IP, SERVER_UDP_PORT));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private class UDPRECVThread implements Runnable {
		byte[] buf = new byte[4*1024];
		public void run() {
			ds = (DatagramSocket)persistentMap.get("DS");
			USER = (Integer)persistentMap.get("USER");
			while (ds != null) {
				try {
					locationMartrix = (int[][])persistentMap.get("LM");
					DatagramPacket dp = new DatagramPacket(buf, buf.length);
					ds.receive(dp);
					ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
							.getLength());
					DataInputStream dis = new DataInputStream(bais);
					int type0  = dis.readInt();
					if(type0==REQUEST_TYPE_LOCATION){
						int pid = dis.readInt();
						int id = dis.readInt();
						Integer x = dis.readInt();
						Integer y = dis.readInt();
						Integer order = dis.readInt();
  						locationMartrix[x][y] = -USER;
  						XXC = (int[][])persistentMap.get("XXC");
  						YYC = (int[][])persistentMap.get("YYC");
  						if(USER==1){
  							YYC[x][y] = order;
  						}else{
  							XXC[x][y] = order;
  						}
  						persistentMap.put("ORDER", order);
  						persistentMap.put("XXC", XXC);
  						persistentMap.put("YYC", YYC);
					    persistentMap.put("type", USER);
					    persistentMap.put("LM", locationMartrix);
						postInvalidate();
					}else if(type0==REQUEST_TYPE_DISCONNECTED){
						//对方已经断线了disconnected  自己也清�?
						send(GameView.REQUEST_TYPE_DISCONNECT);
						Log.e("REQUEST_TYPE", "parnet is disconneted");
						//init();
					}else if(type0==REQUEST_TYPE_CONNECT){
						Log.e("REQUEST_TYPE","ALL CONNECTED");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
