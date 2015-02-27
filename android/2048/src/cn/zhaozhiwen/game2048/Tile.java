package cn.zhaozhiwen.game2048;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;



public class Tile {
   
	public final static String TYPE_TOP_LEFT = "TL";
	public final static String TYPE_TOP_MID_UP = "TU";
	public final static String TYPE_TOP_MID_DOWN = "TD";
	public final static String TYPE_CENTER = "TC";
	public final static String TYPE_CENTER_TILE_NUM = "TCTN";
	public final static String TYPE_CENTER_TILE = "TCT";
	
	//menu
	public final static String TYPE_MENU = "TM";
	public final static String TYPE_MENU_TAB = "TMT";
	
	private float x;
	private float y;
	private float x2;
	private float y2;
	private float width;
	private float height;
	private String txt;
    private Integer value;
    private String type;
    private int color;
    
    
    //
    private float fontSize;
    private float padding = 2;
    
	public Tile(float x, float y, float width, float height, String txt,
			String type,int color) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.txt = txt;
		this.type = type;
		this.color = color;
		this.value = 0;
	}
	public Tile(float x, float y, float width, float height, String txt,
			String type,int color,int value) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.txt = txt;
		this.type = type;
		this.color = color;
		this.value = value;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getX2() {
		return this.getX()+this.width;
	}
	public void setX2(float x2) {
		this.x2 = x2;
	}
	public float getY2() {
		return this.getY()+this.height;
	}
	public void setY2(float y2) {
		this.y2 = y2;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	private static Rect rect=new Rect();
	private static Paint paint = new Paint();
	public Object[] getConfig(){
    	
 
    	if(this.type.equals(Tile.TYPE_TOP_LEFT)){
    		this.fontSize = (float)(this.height/2.8);
    		paint.setTextSize(fontSize);
    		paint.getTextBounds(this.getTxt(),0,1, rect);
    		float h = rect.height();
    		float w = paint.measureText(this.getTxt());
    		float xx = (this.getX2()+this.getX())/2-w/2;
    		float yy = (this.getY2()+this.getY())/2+h/2;
    	     return new Object[]{this.fontSize,xx,yy};
    	}else if(this.type.equals(Tile.TYPE_TOP_MID_UP)){
    		this.fontSize =  (float)(this.height/4.2);
    		paint.setTextSize(fontSize);
    		paint.getTextBounds(this.getTxt(),0,1, rect);
    		
    		float h = rect.height();
    		float w = paint.measureText(this.getTxt());
    		float xx = (this.getX2()+this.getX())/2-w/2;
    		float yy = (this.getY()+(this.getY2()+this.getY())/2)/2+h/2;
    		
    		paint.getTextBounds(this.getValue()+"",0,1, rect);
    		float h2 = rect.height();
    		float w2 = paint.measureText(this.getValue()+"");
    		float xx2 = (this.getX2()+this.getX())/2-w2/2;
    		float yy2 = (this.getY2()+(this.getY2()+this.getY())/2)/2+h2/2;
    	     return new Object[]{this.fontSize,xx,yy,xx2,yy2};
    	}else if(this.type.equals(Tile.TYPE_TOP_MID_DOWN)){
    		this.fontSize = (float)(this.height/3);
    		paint.setTextSize(fontSize);
    		paint.getTextBounds(this.getTxt(),0,1, rect);
    		float h = rect.height();
    		float w = paint.measureText(this.getTxt());
    		float xx = (this.getX2()+this.getX())/2-w/2;
    		float yy = (this.getY2()+this.getY())/2+h/2;
    	     return new Object[]{this.fontSize,xx,yy};
    	}else if(this.type.equals(Tile.TYPE_CENTER_TILE_NUM)){
    		this.fontSize = (float)(this.height/2.5);
    		paint.setTextSize(fontSize);
    		paint.getTextBounds(this.getValue()+"",0,1, rect);
    		float h = rect.height();
    		float w = paint.measureText(this.getValue()+"");
    		float xx = (this.getX2()+this.getX())/2-w/2;
    		float yy = (this.getY2()+this.getY())/2+h/2;
    	    return new Object[]{this.fontSize,xx,yy,change(this.getValue())};
    	}else if(this.type.equals(Tile.TYPE_MENU)){
    		this.fontSize = (float)(this.height/2.5);
    		paint.setTextSize(fontSize);
    		paint.getTextBounds(this.getTxt()+"",0,1, rect);
    		float h = rect.height();
    		float w = paint.measureText(this.getTxt()+"");
    		float xx = (this.getX2()+this.getX())/2-w/2;
    		float yy = (this.getY2()+this.getY())/2+h/2;
    	    return new Object[]{this.fontSize,xx,yy};
    	}else if(this.type.equals(TYPE_MENU_TAB)){
    		this.fontSize = (float)(this.height/16);
    		float height = this.height/8;
    		paint.setTextSize(fontSize);
    		paint.getTextBounds(this.getTxt()+"",0,1, rect);
    		float h = rect.height();
    		float w = paint.measureText(this.getTxt()+"");
    		float xx = (this.getX2()+this.getX())/2-w/2;
    		float yy = (this.getY()*2+height)/2+h/2;
    		float fontSize2 = (float)(this.height/35);
    		paint.setTextSize(fontSize2);
    		paint.getTextBounds(this.getTxt()+"",0,1, rect);
    		h = rect.height()+2;
    	    return new Object[]{this.fontSize,xx,yy,(this.getX()+4),(height+this.getY()),fontSize2,h,height};
    	}
    	
    	
    	
    	return null;
    }
	private int change(int val){
		int color = Color.rgb(215, 51, 18);
		switch(val){
		case 2:color= Color.rgb(238, 228, 218);break;
		case 4:color= Color.rgb(237, 224, 200);break;
		case 8:color= Color.rgb(242, 177, 121);break;
		case 16:color= Color.rgb(236, 141, 84);break;
		case 32:color= Color.rgb(246, 124, 95);break;
		case 64:color= Color.rgb(234, 89, 55);break;
		case 128:color= Color.rgb(243, 216, 107);break;
		case 256:color= Color.rgb(105, 67, 108);break;
		case 512:color= Color.rgb(115, 58, 138);break;
		case 1024:color= Color.rgb(125, 57, 128);break;
		case 2048:color= Color.rgb(235,195,1);break;
		case 4098:color= Color.rgb(145, 53, 188);break;
		case 8196:color= Color.rgb(155, 151, 178);break;
		default:color= Color.rgb(165, 101, 108);
		
		}
		return color;
	}
   public static Map<String,Object> maps = new HashMap<String,Object>();
   public static String getAccount(){
	   String m_szDevIDShort = "35" + //we make this look like a valid IMEI 
	   Build.BOARD.length()%10 + 
	   Build.BRAND.length()%10 + 
	   Build.CPU_ABI.length()%10 + 
	   Build.DEVICE.length()%10 + 
	   Build.DISPLAY.length()%10 + 
	   Build.HOST.length()%10 + 
	   Build.ID.length()%10 + 
	   Build.MANUFACTURER.length()%10 + 
	   Build.MODEL.length()%10 + 
	   Build.PRODUCT.length()%10 + 
	   Build.TAGS.length()%10 + 
	   Build.TYPE.length()%10 + 
	   Build.USER.length()%10 ; //13 digits
	   
	   
	   String m_szLongID =  m_szDevIDShort ;      
			// compute md5     
			 MessageDigest m = null;   
			try {
			 m = MessageDigest.getInstance("MD5");
			 } catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();   
			}    
			m.update(m_szLongID.getBytes(),0,m_szLongID.length());   
			// get md5 bytes   
			byte p_md5Data[] = m.digest();   
			// create a hex string   
			String m_szUniqueID = new String();   
			for (int i=0;i<p_md5Data.length;i++) {   
			     int b =  (0xFF & p_md5Data[i]);    
			// if it is a single digit, make sure it have 0 in front (proper padding)    
			    if (b <= 0xF) 
			        m_szUniqueID+="0";    
			// add number to string    
			    m_szUniqueID+=Integer.toHexString(b); 
			   }   // hex string to uppercase   
			m_szUniqueID = m_szUniqueID.toUpperCase();
	   return m_szUniqueID.substring(8, 24);
   }
}
