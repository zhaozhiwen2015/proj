package cn.zhaozhiwen.fivepointchess;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class FivepointChessActivity extends Activity {
	private static GameView gv;
	public static SoundPool sp = new SoundPool(4,AudioManager.STREAM_MUSIC,100);
	public static int musicId;
	private static boolean isMute = false;
	private static boolean isOnline = false;
	public static Map<String,Object> res = new ConcurrentHashMap<String,Object>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        gv = new GameView(this,width,height,this.getIntent().getBooleanExtra("isOnline", false));      
        LinearLayout layout = new LinearLayout(this);
        layout.addView(gv,LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        layout.setBackgroundResource(R.drawable.bg);
        setContentView(layout);
	    musicId = sp.load(this,R.raw.play, 1);
  }
    
	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		super.onResume();
		if(res.containsKey("GVMATRIX")){
			GameView.setPersistentMap((Map<String, Object>)res.get("GVMATRIX"));  
			System.out.println("onResume"+((Map<String, Object>)res.get("GVMATRIX")).keySet());
	    }
	}
	@Override
	protected void onPause() {
		res.put("GVMATRIX", GameView.getPersistentMap());
		System.out.println("onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(isOnline)gv.send(GameView.REQUEST_TYPE_DISCONNECT);
	//	this.finish();
	}
	public static void sound(){
		if(!isMute)sp.play(musicId, 1f, 1f, 0, 0, 1);
	}
	public static void back(){
		 Map<String, Object> map = GameView.getPersistentMap();
		 map.remove("LM");
		 map.remove("type");
		 map.remove("USER");
		 map.remove("GAMEOVER");
		 map.remove("ORDER");
	}
    
}