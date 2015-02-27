package cn.zhaozhiwen.game2048;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.domob.android.ads.AdView;
import cn.zhaozhiwen.game2048.menu.MenuActivity;

public class GameActivity extends Activity {
	private GameView gv = null;
	public static SoundPool sp = new SoundPool(4,AudioManager.STREAM_MUSIC,100);
	public static int musicId;
	private static boolean isMute = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display mDisplay = getWindowManager().getDefaultDisplay();
        int W = mDisplay.getWidth();
        int H = mDisplay.getHeight();
        LinearLayout layout = new LinearLayout(this);
        gv = new GameView(this,W,H);
        layout.addView(gv,LayoutParams.WRAP_CONTENT);
        
        
        AdView av = new AdView(this,"56OJzyeouN9wG7uylm","16TLmxvvApDc1NUOg3DiPvei");
        av.setKeyword("2048,kevin");
        av.setUserGender("male");
        av.setUserBirthdayStr("1986-05-23");
        av.setUserPostcode("515166");
        RelativeLayout.LayoutParams la = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        la.addRule(RelativeLayout.CENTER_HORIZONTAL);
        av.setLayoutParams(la);
        
        layout.addView(av);
        
        setContentView(layout);
        musicId = sp.load(this,R.raw.play, 1);
       
    }
    public  void toMenu(String type){
    	Intent intent = new Intent(this,MenuActivity.class);
    	Bundle bundleSimple = new Bundle();  
    	bundleSimple.putString("type", type);  
    	intent.putExtras(bundleSimple);
    	this.startActivity(intent);
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		  this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	public static void sound(){
		if(!isMute)sp.play(musicId, 1f, 1f, 0, 0, 1);
	}	
	public Integer getHightScore(){
		 SharedPreferences scoreInfo = getSharedPreferences("score_info", 0);  
		 return scoreInfo.getInt("hs", 0);
	}
	public void setHightScore(Integer i){
		 SharedPreferences scoreInfo = getSharedPreferences("score_info", 0);  
		 scoreInfo.edit().putInt("hs",i).commit();  
	}
}