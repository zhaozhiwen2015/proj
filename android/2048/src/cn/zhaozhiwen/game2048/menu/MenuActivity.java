package cn.zhaozhiwen.game2048.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import cn.zhaozhiwen.game2048.GameActivity;
import cn.zhaozhiwen.game2048.Tile;

public class MenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display mDisplay = getWindowManager().getDefaultDisplay();
        int W = mDisplay.getWidth();
        int H = mDisplay.getHeight();
        LinearLayout layout = new LinearLayout(this);
        layout.addView(new MenuView(this,W,H),LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        setContentView(layout);
    }
    public void back(){
    	this.finish();
    	Intent intent = new Intent(this,GameActivity.class);
    	this.startActivity(intent);
    }
    public void restart(){
    	this.finish();
    	Tile.maps.clear();
    	Intent intent = new Intent(this,GameActivity.class);
    	this.startActivity(intent);
    }

    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		  this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}