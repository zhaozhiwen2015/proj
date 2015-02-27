package cn.zhaozhiwen.game2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameView extends View implements OnTouchListener{

	private float x0;
	private float y0;
	private float x1;
	private float y1;
	private int ScreenWidth;
	private int ScreenHeight;
	private int ScreenPadding = 10;
	private List<Tile> staticTile = new ArrayList<Tile>();
	private Tile hightScoreTile = null;
	private Tile hightHisScoreTile = null;
	private Tile menuTile = null;
	private Tile rankTile = null;
	private Tile baseBackgroundTile = null;
	
	private String msg = "";
	
	
	private int TILE_SIZE = 4;
	
	private float TILE_NUM_PADDING=5;
	private float TILE_WIDTH;
	private float TILE_NUM_LOC_X;
	private float TILE_NUM_LOC_Y;

	//
	private GameActivity obj = null;	
	///top left
	private float TOP_LEFT_X ;
	private float TOP_LEFT_Y ;
	private float TOP_LEFT_W ;
	private float TOP_LEFT_H ;
	
	private float TOP_MID_X ;
	private float TOP_MID_Y ;
	private float TOP_MID_W ;
	private float TOP_MID_H ;
	
	private float TOP_MID2_X ;
	private float TOP_MID2_Y ;
	private float TOP_MID2_W ;
	private float TOP_MID2_H ;
	
	private float TOP_RIGHT_X ;
	private float TOP_RIGHT_Y ;
	private float TOP_RIGHT_W ;
	private float TOP_RIGHT_H ;
	
	private float TOP_RIGHT2_X ;
	private float TOP_RIGHT2_Y ;
	private float TOP_RIGHT2_W ;
	private float TOP_RIGHT2_H ;
	
	public GameView(Context context) {
		super(context);
		this.setFocusable(true);
		//this.setClickable(true);
		this.setFocusableInTouchMode(true);
	}
	public GameView(Context context,int ScreenWidth,int ScreenHeight) {
		super(context);
		
		Tile tile = null;
		this.ScreenHeight = ScreenHeight;
		this.ScreenWidth = ScreenWidth;
		float remains = ScreenWidth - 4*ScreenPadding;
		TOP_LEFT_X = ScreenPadding;
		TOP_LEFT_Y = ScreenPadding;
		TOP_LEFT_W = remains*6/16;
		TOP_LEFT_H = remains*5/16+ScreenPadding;
		tile = new Tile(TOP_LEFT_X, TOP_LEFT_Y, TOP_LEFT_W, TOP_LEFT_H, "2048",Tile.TYPE_TOP_LEFT,Color.rgb(235,195,1));
		staticTile.add(tile);
		TOP_MID_X = TOP_LEFT_X +TOP_LEFT_W+ ScreenPadding;
		TOP_MID_Y = TOP_LEFT_Y;
		TOP_MID_W = remains*5/16;
		TOP_MID_H = (float)(remains*3/16);
		hightScoreTile = new Tile(TOP_MID_X, TOP_MID_Y, TOP_MID_W, TOP_MID_H, "分数",Tile.TYPE_TOP_MID_UP,Color.rgb(187,173,160));
		
		TOP_MID2_X = TOP_LEFT_X +TOP_LEFT_W+ ScreenPadding;
		TOP_MID2_Y = TOP_MID_Y + TOP_MID_H+ScreenPadding;
		TOP_MID2_W = remains*5/16;
		TOP_MID2_H = remains*2/16;
		menuTile = new Tile(TOP_MID2_X, TOP_MID2_Y, TOP_MID2_W, TOP_MID2_H, "菜单",Tile.TYPE_TOP_MID_DOWN,Color.rgb(237,153,91));
		staticTile.add(menuTile);
		TOP_RIGHT_X = TOP_MID_X +TOP_MID_W+ ScreenPadding;
		TOP_RIGHT_Y = TOP_MID_Y;
		TOP_RIGHT_W = remains*5/16;
		TOP_RIGHT_H = (float)(remains*3/16);
		hightHisScoreTile = new Tile(TOP_RIGHT_X, TOP_RIGHT_Y, TOP_RIGHT_W, TOP_RIGHT_H, "历史最高成绩",Tile.TYPE_TOP_MID_UP,Color.rgb(187,173,160));

		TOP_RIGHT2_X = TOP_MID_X +TOP_MID_W+ ScreenPadding;
		TOP_RIGHT2_Y = TOP_RIGHT_Y+TOP_RIGHT_H+ScreenPadding;
		TOP_RIGHT2_W = remains*5/16;
		TOP_RIGHT2_H = remains*2/16;
		rankTile = new Tile(TOP_RIGHT2_X, TOP_RIGHT2_Y, TOP_RIGHT2_W, TOP_RIGHT2_H, "排行榜",Tile.TYPE_TOP_MID_DOWN,Color.rgb(237,153,91));
		staticTile.add(rankTile);
		
		baseBackgroundTile = new Tile(ScreenPadding, TOP_LEFT_X+TOP_LEFT_H+ScreenPadding, TOP_LEFT_W+TOP_MID_W+TOP_RIGHT_W+ScreenPadding*2, TOP_LEFT_W+TOP_MID_W+TOP_RIGHT_W+ScreenPadding*2, "",Tile.TYPE_CENTER,Color.rgb(187,173,160));
		staticTile.add(baseBackgroundTile);
		
		TILE_WIDTH = (TOP_LEFT_W+TOP_MID_W+TOP_RIGHT_W+ScreenPadding*2 - (TILE_SIZE+1)*TILE_NUM_PADDING)/TILE_SIZE;
		TILE_NUM_LOC_X = ScreenPadding+TILE_NUM_PADDING;
		TILE_NUM_LOC_Y = TOP_LEFT_X+TOP_LEFT_H+ScreenPadding+TILE_NUM_PADDING;
		for(int i=0;i<TILE_SIZE*TILE_SIZE;i++){
			int x = i%TILE_SIZE;
			int y = i/TILE_SIZE;
			tile = new Tile(TILE_NUM_LOC_X+x*(TILE_WIDTH+TILE_NUM_PADDING), TILE_NUM_LOC_Y+y*(TILE_WIDTH+TILE_NUM_PADDING), TILE_WIDTH, TILE_WIDTH, "",Tile.TYPE_CENTER_TILE,Color.rgb(204,192,180));
			staticTile.add(tile);
		}
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnTouchListener(this);

		//random to generate two tiles of 2
		if(Tile.maps.containsKey("MM")){
			MATRIX = (int[][])Tile.maps.get("MM");
			hightScoreTile.setValue((Integer)Tile.maps.get("HS"));
			hightHisScoreTile.setValue((Integer)Tile.maps.get("HHS"));
		}else{
			int ALL = TILE_SIZE*TILE_SIZE;
			MATRIX = new int[TILE_SIZE][TILE_SIZE];
			for(int i=0;i<ALL;i++){
				MATRIX[i/TILE_SIZE][i%TILE_SIZE] = 0;
			}
			Integer idx = random.nextInt(ALL);
			while(0!=MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]){
				 idx = random.nextInt(ALL);
			}
			MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]  = 2;
			
			idx = random.nextInt(ALL);
			while(0!=MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]){
				 idx = random.nextInt(ALL);
			}
			MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]  = 2;
		}	
		
		if(context instanceof GameActivity){
			obj = (GameActivity)context;
			hightHisScoreTile.setValue(obj.getHightScore());
		}else{
			hightHisScoreTile.setValue(0);			
		}

	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 if((event.getAction() ==MotionEvent.ACTION_DOWN)&&(event.getX()>menuTile.getX()&&event.getY()>menuTile.getY()&&event.getX()<menuTile.getX2()&&event.getY()<menuTile.getY2())){
			 if(obj!=null){
				 Tile.maps.put("MM",MATRIX);
				 Tile.maps.put("HS",hightScoreTile.getValue());
				 Tile.maps.put("HHS",hightHisScoreTile.getValue());
				 obj.toMenu("menu");
			 }
			 return true;
		 }else if((event.getAction() ==MotionEvent.ACTION_DOWN)&&(event.getX()>rankTile.getX()&&event.getY()>rankTile.getY()&&event.getX()<rankTile.getX2()&&event.getY()<rankTile.getY2())){
			 if(obj!=null){
				 obj.toMenu("rank"); 
			 }
			 return true;
		 }else if(event.getX()>baseBackgroundTile.getX()&&event.getY()>baseBackgroundTile.getY()&&event.getX()<baseBackgroundTile.getX2()&&event.getY()<baseBackgroundTile.getY2()){
			 if(event.getAction() == MotionEvent.ACTION_DOWN){
					x0 = event.getX();
					y0 = event.getY();
					Log.i("ACTION_DOWN", event.getX()+"");
			}else if(event.getAction() == MotionEvent.ACTION_UP){
					x1 = event.getX();
					y1 = event.getY();
					float x = x1-x0;
		            float y = y1 - y0;
		            float x_limit = ScreenWidth / 4;
		            float y_limit = ScreenHeight / 6;
		            float x_abs = Math.abs(x);
		            float y_abs = Math.abs(y);
		            if(x_abs >= y_abs){
		                if(x > x_limit || x < -x_limit){
		                    if(x>0){
		                    	msg = "right";
		                    }else if(x<=0){
		                    	msg = "left";
		                    }
		                }
		            }else{
		                if(y > y_limit || y < -y_limit){
		                    if(y>0){
		                    	msg = "down";
		                    }else if(y<=0){
		                    	msg = "up";
		                    }
		                }
		            }
		            algorithm(msg);
		            msg = "";
					Log.i("onTouch",msg);
				}
		 }
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.rgb(255, 255, 255));
		Paint paint = new Paint();
		//getAccount
		canvas.drawText(Tile.getAccount(),10,10,paint);
		for(Tile tile : staticTile){
			paint.setColor(tile.getColor());
			RectF rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			if(tile.getType().equals(Tile.TYPE_TOP_LEFT)){
				Object[] obj = tile.getConfig();
				paint.setFakeBoldText(true);
				paint.setColor(Color.rgb(255, 255,255));
				paint.setTextSize((Float)obj[0]);
				canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
			}else if(tile.getType().equals(Tile.TYPE_TOP_MID_DOWN)){
				Object[] obj = tile.getConfig();
				paint.setColor(Color.rgb(255, 255,255));
				paint.setTextSize((Float)obj[0]);
				canvas.drawText(tile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
			}
		}
		
	   if(hightScoreTile!=null){
		   paint.setColor(hightScoreTile.getColor());
			RectF rect = new RectF(hightScoreTile.getX(),hightScoreTile.getY(),hightScoreTile.getX2(),hightScoreTile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			Object[] obj = hightScoreTile.getConfig();
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(hightScoreTile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
			canvas.drawText(hightScoreTile.getValue()+"",(Float)obj[3], (Float)obj[4], paint);
	   }
	   if(hightHisScoreTile!=null){
		   paint.setColor(hightHisScoreTile.getColor());
			RectF rect = new RectF(hightHisScoreTile.getX(),hightHisScoreTile.getY(),hightHisScoreTile.getX2(),hightHisScoreTile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			Object[] obj = hightHisScoreTile.getConfig();
			paint.setColor(Color.rgb(255, 255,255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(hightHisScoreTile.getTxt(),(Float)obj[1], (Float)obj[2], paint);
			canvas.drawText(hightHisScoreTile.getValue()+"",(Float)obj[3], (Float)obj[4], paint);
	   }
	
		for(Tile tile:getTiles()){
			Object[] obj = tile.getConfig();
			paint.setColor((Integer)obj[3]);
			RectF rect = new RectF(tile.getX(),tile.getY(),tile.getX2(),tile.getY2());
			canvas.drawRoundRect(rect, 10f, 10f, paint);
			paint.setColor(Color.rgb(255, 255, 255));
			paint.setTextSize((Float)obj[0]);
			canvas.drawText(tile.getValue()+"",(Float)obj[1], (Float)obj[2], paint);

		}
	}

	private static Random random = new Random();
	private int[][] MATRIX = {}; 
	private void algorithm(String direction){
		boolean emptySound = false;
		boolean mergeSound = false;
		if(null!=direction&& !direction.equals("")){
			if(check()){
				if(direction.equals("up")){
					//empty
					for(int j=0;j<TILE_SIZE;j++){
						int idx = 0;
						for(int i=0;i<TILE_SIZE;i++){
							if(MATRIX[i][j]!=0){
								MATRIX[idx][j]  = MATRIX[i][j];
								if(idx!=i){
									MATRIX[i][j] = 0;
									emptySound = true;
								}
								idx++;
							}
						}
					}
					//merge
					for(int j=0;j<TILE_SIZE;j++){
						int flag = -1;
						for(int i=0;i<TILE_SIZE-1;i++){
							if(flag!=-1)break;
							if(MATRIX[i][j]!=0&&MATRIX[i][j]==MATRIX[i+1][j]){
								MATRIX[i][j] =2*MATRIX[i][j];
								hightScoreTile.setValue(hightScoreTile.getValue()+MATRIX[i][j]);
								if(hightScoreTile.getValue()>hightHisScoreTile.getValue()){
									hightHisScoreTile.setValue(hightScoreTile.getValue());
									obj.setHightScore(hightHisScoreTile.getValue());
								}
								flag = i;
							}
						}
						if(flag!=-1){
							    int i=flag+1;
								for(;i<TILE_SIZE-1;i++){
										MATRIX[i][j]=MATRIX[i+1][j];
								}					
								MATRIX[i][j]=0;
								mergeSound = true;
						}
					}
				}else if(direction.equals("down")){
					for(int j=0;j<TILE_SIZE;j++){
						int idx = TILE_SIZE-1;
						for(int i=TILE_SIZE-1;i>=0;i--){
							if(MATRIX[i][j]!=0){
								MATRIX[idx][j]  = MATRIX[i][j];
								if(idx!=i){
									MATRIX[i][j] = 0;
									emptySound = true;
								}
								idx--;
							}
						}
					}
					//merge
					for(int j=0;j<TILE_SIZE;j++){
						int flag = -1;
						for(int i=TILE_SIZE-1;i>0;i--){
							if(flag!=-1)break;
							if(MATRIX[i][j]!=0&&MATRIX[i][j]==MATRIX[i-1][j]){
								MATRIX[i][j] = 2*MATRIX[i][j];
								hightScoreTile.setValue(hightScoreTile.getValue()+MATRIX[i][j]);
								if(hightScoreTile.getValue()>hightHisScoreTile.getValue()){
									hightHisScoreTile.setValue(hightScoreTile.getValue());
									obj.setHightScore(hightHisScoreTile.getValue());
								}
								flag = i;
							}
						}
						if(flag!=-1){
							    int i=flag-1;
								for(;i>0;i--){
										MATRIX[i][j]=MATRIX[i-1][j];
								}		
								MATRIX[i][j]=0;
								mergeSound = true;
						}
					}
				}else if(direction.equals("left")){
					for(int i=0;i<TILE_SIZE;i++){
						int idx = 0;
						for(int j=0;j<TILE_SIZE;j++){
							if(MATRIX[i][j]!=0){
								MATRIX[i][idx]  = MATRIX[i][j];
								if(idx!=j){
									MATRIX[i][j] = 0;
									emptySound = true;
								}
								idx++;
							}
						}
					}
					//merge
					for(int i=0;i<TILE_SIZE;i++){
						int flag = -1;
						for(int j=0;j<TILE_SIZE-1;j++){
							if(flag!=-1)break;
							if(MATRIX[i][j]!=0&&MATRIX[i][j]==MATRIX[i][j+1]){
								MATRIX[i][j] = 2*MATRIX[i][j];
								hightScoreTile.setValue(hightScoreTile.getValue()+MATRIX[i][j]);
								if(hightScoreTile.getValue()>hightHisScoreTile.getValue()){
									hightHisScoreTile.setValue(hightScoreTile.getValue());
									obj.setHightScore(hightHisScoreTile.getValue());
								}
								flag = j;
							}
						}
						if(flag!=-1){
							    int j=flag+1;
								for(;j<TILE_SIZE-1;j++){
										MATRIX[i][j]=MATRIX[i][j+1];
								}		
								MATRIX[i][j]=0;
								mergeSound = true;
						}
					}
				}else if(direction.equals("right")){
					for(int i=0;i<TILE_SIZE;i++){
						int idx = TILE_SIZE-1;
						for(int j=TILE_SIZE-1;j>=0;j--){
							if(MATRIX[i][j]!=0){
								MATRIX[i][idx]  = MATRIX[i][j];
								if(idx!=j){
									MATRIX[i][j] = 0;
									emptySound = true;
								}
								idx--;
							}
						}
					}
					//merge
					for(int i=0;i<TILE_SIZE;i++){
						int flag = -1;
						for(int j=TILE_SIZE-1;j>0;j--){
							if(flag!=-1)break;
							if(MATRIX[i][j]!=0&&MATRIX[i][j]==MATRIX[i][j-1]){
								MATRIX[i][j] = 2*MATRIX[i][j];
								hightScoreTile.setValue(hightScoreTile.getValue()+MATRIX[i][j]);
								if(hightScoreTile.getValue()>hightHisScoreTile.getValue()){
									hightHisScoreTile.setValue(hightScoreTile.getValue());
									obj.setHightScore(hightHisScoreTile.getValue());
								}
								flag = j;
							}
						}
						if(flag!=-1){
							int j=flag-1;
							for(;j>0;j--){
									MATRIX[i][j]=MATRIX[i][j-1];
							}
							MATRIX[i][j]=0;		
							mergeSound = true;
						}
					}
				}
				//control the sound
				if(mergeSound){
					GameActivity.sound();
				}else if(emptySound){
					GameActivity.sound();
				}
				if(mergeSound||emptySound){
					if(has()){//check is there has the empty tile positon
						int ALL = TILE_SIZE*TILE_SIZE;
						Integer idx = random.nextInt(ALL);
						while(0!=MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]){
							 idx = random.nextInt(ALL);
						}
						int num = random.nextInt(10);//random to generate the 2 or 4
						if(num==4||num==5){
							MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]  = 4;			
						}else{
							MATRIX[idx/TILE_SIZE][idx%TILE_SIZE]  = 2;			
						}						
					}
					this.postInvalidate();					
				}
			}else{
				Log.i("status", "GAMEOVER");	
			}
		}
	}
	private boolean check(){
		int ALL = TILE_SIZE*TILE_SIZE;
		for(int i=0;i<ALL;i++){
			if(MATRIX[i/TILE_SIZE][i%TILE_SIZE] == 0)return true;
		}
		//0 1 2 3
		//4 5 6 7
		for(int i=0;i<ALL;i+=4){
			if(MATRIX[i/TILE_SIZE][i%TILE_SIZE] == MATRIX[(i+1)/TILE_SIZE][(i+1)%TILE_SIZE]||
			  MATRIX[(i+1)/TILE_SIZE][(i+1)%TILE_SIZE] == MATRIX[(i+2)/TILE_SIZE][(i+2)%TILE_SIZE]||
			  MATRIX[(i+2)/TILE_SIZE][(i+2)%TILE_SIZE] == MATRIX[(i+3)/TILE_SIZE][(i+3)%TILE_SIZE])return true;
		}
		for(int i=0;i<TILE_SIZE;i++){
			if(MATRIX[0][i%TILE_SIZE] == MATRIX[1][i%TILE_SIZE]||
			  MATRIX[1][i%TILE_SIZE] == MATRIX[2][i%TILE_SIZE]||
			  MATRIX[2][i%TILE_SIZE] == MATRIX[3][i%TILE_SIZE])return true;
		}
		return false;
	}
	private boolean has(){
		int ALL = TILE_SIZE*TILE_SIZE;
		for(int i=0;i<ALL;i++){
			if(MATRIX[i/TILE_SIZE][i%TILE_SIZE] == 0)return true;
		}
		return false;
	}
	private ArrayList<Tile> getTiles(){
		ArrayList<Tile> lst = new ArrayList<Tile>();
		int ALL = TILE_SIZE*TILE_SIZE;
		for(int i=0;i<ALL;i++){
			if(MATRIX[i%TILE_SIZE][i/TILE_SIZE]!= 0){
				int x = i/TILE_SIZE;
				int y = i%TILE_SIZE;
				Tile tile = new Tile(TILE_NUM_LOC_X+x*(TILE_WIDTH+TILE_NUM_PADDING), TILE_NUM_LOC_Y+y*(TILE_WIDTH+TILE_NUM_PADDING), TILE_WIDTH, TILE_WIDTH, "",Tile.TYPE_CENTER_TILE_NUM,Color.rgb(204,192,180),MATRIX[i%TILE_SIZE][i/TILE_SIZE]);
				lst.add(tile);
			}
		}
		return lst;
	}

}
