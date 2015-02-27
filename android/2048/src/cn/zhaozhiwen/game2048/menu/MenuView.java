package cn.zhaozhiwen.game2048.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import cn.zhaozhiwen.game2048.R;
import cn.zhaozhiwen.game2048.Tile;

public class MenuView extends View implements OnTouchListener{


	private int ScreenWidth;
	private int ScreenHeight;
	private int ScreenPadding = 20;
	private Tile restartTile = null;
	private Tile backTile = null;
	private Tile aboutusTile = null;
	private Tile versionTile = null;

	private String type = "";
	//
	private MenuActivity obj = null;
	///top left
	private float TOP_LEFT_X ;
	private float TOP_LEFT_Y ;
	private float TOP_LEFT_W ;
	private float TOP_LEFT_H ;
	
	
	public MenuView(Context context) {
		super(context);
		this.setFocusable(true);
		//this.setClickable(true);
		this.setFocusableInTouchMode(true);
	}
	public MenuView(Context context,int ScreenWidth,int ScreenHeight) {
		super(context);
		if(context instanceof MenuActivity){
			obj = (MenuActivity)context;
		}
		if(null==type|| type.equals(""))type = "menu";
		this.ScreenHeight = ScreenHeight;
		this.ScreenWidth = ScreenWidth;
			
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.rgb(255, 255, 255));
		Paint paint = new Paint();
		if(type.equals("menu")){
			float remains = ScreenWidth - 2*ScreenPadding;
			TOP_LEFT_X = ScreenPadding;
			TOP_LEFT_Y = ScreenHeight*2/15;
			TOP_LEFT_W = remains;
			TOP_LEFT_H = ScreenHeight*2/15;
			backTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "返回游戏",Tile.TYPE_MENU,Color.rgb(235,195,1));
			TOP_LEFT_Y = ScreenHeight*5/15;
			restartTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "重新开始",Tile.TYPE_MENU,Color.rgb(235,195,1));
			TOP_LEFT_Y = ScreenHeight*8/15;
			versionTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "游戏玩法",Tile.TYPE_MENU,Color.rgb(235,195,1));
			TOP_LEFT_Y = ScreenHeight*11/15;
			aboutusTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "联系我们",Tile.TYPE_MENU,Color.rgb(235,195,1));
			Tile tile = backTile;
			paint.setColor(tile.getColor());
			RectF rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			Object[] obj = tile.getConfig();
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
			
			tile = restartTile;
			paint.setColor(tile.getColor());
			rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			obj = tile.getConfig();
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);

			tile = versionTile;
			paint.setColor(tile.getColor());
			rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			obj = tile.getConfig();
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
			

			tile = aboutusTile;
			paint.setColor(tile.getColor());
			rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			obj = tile.getConfig();
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
						
		}else if(type.equals("version")){
			TOP_LEFT_X = ScreenPadding;
			TOP_LEFT_Y = ScreenPadding;
			TOP_LEFT_W = ScreenWidth - 2*ScreenPadding;
			TOP_LEFT_H = ScreenHeight - 2*ScreenPadding;
			versionTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "游戏玩法",Tile.TYPE_MENU_TAB,Color.rgb(235,195,1));
			Tile tile =  versionTile;
			Object[] obj = tile.getConfig();
			RectF rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			paint.setColor(tile.getColor());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
		 	paint.setColor(Color.rgb(255, 255, 255));
			canvas.drawRect((Float)obj[3],(Float)obj[4],(int)tile.getX2(),(int)tile.getY2(), paint);		
			float  h = (Float)obj[6];
			paint.setColor(tile.getColor());
			paint.setTextSize((Float)obj[5]);
			String[] msg = {
					"---2014玩法---",
					"1、通过上、下、左、右方向滑动方块。",
					"2、在滑动方向上出现相同数值的方块将被合",
					"  并，分数提升。",
					"3、每滑动一次，游戏会自动在空白方块处新",
					"  产生一个方块。",
					"4、当方格盘无空白方块，且四个方向无法合",
					"  并方块时，游戏结束。",
					"---高分技巧---",
					"1、最大数尽可能放在角落。",
					"2、数字按顺序紧邻排列。",
					"3、首先满足最大数和次大数在的那一列/行是",
					"  满的。",
					"4、时刻注意活动较大数（32以上）旁边要有",
					"  相近的数。",
					"5、以大数所在的一行为主要移动方向。",
					"6、不要急于“清理桌面”。"};
			float yy = 0;
			for(int i=0;i<msg.length;i++){
				String m = msg[i];
				canvas.drawText(m,(Float)obj[3]+2,(Float)obj[4]+(i+1)*h,paint);			
				yy = (Float)obj[4]+(i+1)*h;
			}
			TOP_LEFT_X =  4*ScreenPadding;
			TOP_LEFT_W = ScreenWidth - 8*ScreenPadding;
			TOP_LEFT_H = (Float)obj[7];
			TOP_LEFT_Y =  ScreenHeight-ScreenPadding-TOP_LEFT_H;
			backTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "返回菜单",Tile.TYPE_MENU,Color.rgb(235,195,1));
			tile = backTile;
			paint.setColor(tile.getColor());
			rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			obj = tile.getConfig();
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
		}else if(type.equals("aboutus")){
			TOP_LEFT_X = ScreenPadding;
			TOP_LEFT_Y = ScreenPadding;
			TOP_LEFT_W = ScreenWidth - 2*ScreenPadding;
			TOP_LEFT_H = ScreenHeight - 2*ScreenPadding;
			versionTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "联系我们",Tile.TYPE_MENU_TAB,Color.rgb(235,195,1));
			Tile tile =  versionTile;
			Object[] obj = tile.getConfig();
			RectF rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			paint.setColor(tile.getColor());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
		 	paint.setColor(Color.rgb(255, 255, 255));
			canvas.drawRect((Float)obj[3],(Float)obj[4],(int)tile.getX2(),(int)tile.getY2(), paint);
			
			Bitmap  bitmap = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.qrcode1 );
			float hh = bitmap.getHeight();
			canvas.drawBitmap(bitmap, (ScreenWidth-hh)/2, (Float)obj[4], paint);
			float  h = (Float)obj[6];
			paint.setColor(tile.getColor());
			paint.setTextSize((Float)obj[5]);
			String[] msg = {
					"",
					"---蚊子工作室出品---",
					"联  系  人：赵生",
					"联系方式：15989032382",
					"腾讯  QQ：442560143",
					"网址:http://www.zhaozhiwen.net.cn",
					"  ",
					"版权所有，违者必究！"};
			float yy = 0;
			for(int i=0;i<msg.length;i++){
				String m = msg[i];
				canvas.drawText(m,(Float)obj[3]+2,(Float)obj[4]+hh+(i+1)*h,paint);			
				yy = (Float)obj[4]+hh+(i+1)*h;
			}
			TOP_LEFT_X =  4*ScreenPadding;
			TOP_LEFT_W = ScreenWidth - 8*ScreenPadding;
			TOP_LEFT_H = (Float)obj[7];
			TOP_LEFT_Y =  ScreenHeight-ScreenPadding-TOP_LEFT_H;
			backTile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "返回菜单",Tile.TYPE_MENU,Color.rgb(235,195,1));
			tile = backTile;
			paint.setColor(tile.getColor());
			rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			obj = tile.getConfig();
			paint.setFakeBoldText(true);
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
		}

	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
        if(obj!=null&&event.getAction() == MotionEvent.ACTION_DOWN){
    		if(event.getX()>backTile.getX()&&event.getY()>backTile.getY()&&event.getX()<backTile.getX2()&&event.getY()<backTile.getY2()){
            	if(type.equals("menu"))obj.back();
            	else{
            		type = "menu";
            		this.postInvalidate();
            	}
            	return true;
            }else if(event.getX()>restartTile.getX()&&event.getY()>restartTile.getY()&&event.getX()<restartTile.getX2()&&event.getY()<restartTile.getY2()){
            	obj.restart();
            }else if(event.getX()>versionTile.getX()&&event.getY()>versionTile.getY()&&event.getX()<versionTile.getX2()&&event.getY()<versionTile.getY2()){
            	if(type.equals("menu")){
                	type  = "version";
                	this.postInvalidate();            		
            	}
            	return true;
            }   else if(event.getX()>aboutusTile.getX()&&event.getY()>aboutusTile.getY()&&event.getX()<aboutusTile.getX2()&&event.getY()<aboutusTile.getY2()){
            	if(type.equals("menu")){
                	type  = "aboutus";
                	this.postInvalidate();	
            	}
            	return true;
            }       	
        }
		return true;
	}

}
