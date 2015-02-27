package cn.zhaozhiwen.fivepointchess;

import cn.zhaozhiwen.fivepointchess.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HomepageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}
	
	public void click(View v){

		Intent intent = new Intent(this,FivepointChessActivity.class);
		if(v.getId()==R.id.dj){
			intent.putExtra("isOnline", false);			
			startActivity(intent);
		}else if(v.getId()==R.id.wl){
	//		intent.putExtra("isOnline", true);
			Toast.makeText(this, "ÍøÂç°æÄÚ²âÖÐ~~~¾´ÇëÆÚ´ý!!", Toast.LENGTH_SHORT).show();
		}

	}

}
