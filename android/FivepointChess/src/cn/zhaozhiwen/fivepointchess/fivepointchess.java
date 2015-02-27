package cn.zhaozhiwen.fivepointchess;

public class fivepointchess {

	/**
	 * @param args
	 */
	
	private static int SIZE = 14;
	private static int type = 1;
	private static int[][] locationMartrix = {
		/*	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,-1,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}*/};
	
	private static int[][] XX = {};
	private static int[][] YY = {};
	public static void main(String[] args) {
	
		go();
	}
	public static void go(){
		init();
		
	}
	public static void init(){
		locationMartrix = new int[SIZE][SIZE];
		XX = new int[SIZE][SIZE];
		YY = new int[SIZE][SIZE];
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				locationMartrix[i][j]=0;
				XX[i][j]=0;
				YY[i][j]=0;
			}
		}
		int pading = (int)Math.floor(SIZE/1.6);
	/*	locationMartrix[pading][pading]=1;
		locationMartrix[SIZE-pading-1][SIZE-pading-1]=1;
		locationMartrix[SIZE-pading-1][pading]=-1;
		locationMartrix[pading][SIZE-pading-1]=-1;*/
		
		print(0);
		robot();
	}
	public static void robot(){
		int cns=0;
		while(!full()){
				int[] xy = beforeGet(type);
				locationMartrix[xy[0]][xy[1]] = type;
				System.out.print(type);
				print(0);
				cns++;
				if(check(type)){
					System.out.println(type+": WIN!!"+cns);
					break;
				}
				type = -type;	
		}
		if(full()){
			System.out.println("~~流局!!"+cns);
		}
	}
	public static boolean check(int type){
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==type){
					return true;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==type){
					return true;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==type){
					return true;
				}
			}
		}
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==type){
					return true;
				}
			}
		}
		return false;
	}
	public static boolean full(){
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0){
					return false;
				}
			}
		}
		return true;
	}
	public static void print(int type){//0 main 1XX -1YY
		System.out.println("+++++++++++++"+type+"+++++++++++++++");
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				if(type==0){
					if(locationMartrix[i][j]==0){
						System.out.print("--");
					}else if(locationMartrix[i][j]==1){
						System.out.print("白");
					}else if(locationMartrix[i][j]==-1){
						System.out.print("黑");
					}					
				}else if(type==1){
					System.out.print(XX[i][j]);
				}else if(type==-1){
					System.out.print(YY[i][j]);
				}
			}
			System.out.println();
		}
		
	}
	public static void empty(int type){
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
			    if(type==1)XX[i][j]=0;
			    else if(type==-1)YY[i][j]=0;
			}
		}
	}
	public static int[] beforeGet(int type){
		scan(type);
		scan(-type);
		int[] ff = new int[2];
		//0+3+0
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE-5;j++){
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
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
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
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
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
		for(int i=0;i<SIZE-5;i++){
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
		//4+1 1+4
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==-type){
			       ff[0] = i;
			       ff[1] = j;
			       return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j+1]==-type&&locationMartrix[i+2][j+2]==-type&&locationMartrix[i+3][j+3]==-type&&locationMartrix[i+4][j+4]==0){
					ff[0] = i+4;
				    ff[1] = j+4;
				    return ff;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==-type){
					   ff[0] = i;
				       ff[1] = j;
				       return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i-1][j+1]==-type&&locationMartrix[i-2][j+2]==-type&&locationMartrix[i-3][j+3]==-type&&locationMartrix[i-4][j+4]==0){
					ff[0] = i-4;
				    ff[1] = j+4;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==-type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i][j+1]==-type&&locationMartrix[i][j+2]==-type&&locationMartrix[i][j+3]==-type&&locationMartrix[i][j+4]==0){
					ff[0] = i;
				    ff[1] = j+4;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==-type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==-type&&locationMartrix[i+1][j]==-type&&locationMartrix[i+2][j]==-type&&locationMartrix[i+3][j]==-type&&locationMartrix[i+4][j]==0){
					ff[0] = i+4;
				    ff[1] = j;
				    return ff;
				}
			}
		}
		//0+3+0本方
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE-5;j++){
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
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
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
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
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
		for(int i=0;i<SIZE-5;i++){
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
		//4+1 1+4 本方
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==type){
			       ff[0] = i;
			       ff[1] = j;
			       return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j+1]==type&&locationMartrix[i+2][j+2]==type&&locationMartrix[i+3][j+3]==type&&locationMartrix[i+4][j+4]==0){
					ff[0] = i+4;
				    ff[1] = j+4;
				    return ff;
				}
			}
		}
		for(int i=4;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==type){
					   ff[0] = i;
				       ff[1] = j;
				       return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i-1][j+1]==type&&locationMartrix[i-2][j+2]==type&&locationMartrix[i-3][j+3]==type&&locationMartrix[i-4][j+4]==0){
					ff[0] = i-4;
				    ff[1] = j+4;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE-5;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i][j+1]==type&&locationMartrix[i][j+2]==type&&locationMartrix[i][j+3]==type&&locationMartrix[i][j+4]==0){
					ff[0] = i;
				    ff[1] = j+4;
				    return ff;
				}
			}
		}
		for(int i=0;i<SIZE-5;i++){
			for(int j=0;j<SIZE;j++){
				if(locationMartrix[i][j]==0&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==type){
					ff[0] = i;
				    ff[1] = j;
				    return ff;
				}else if(locationMartrix[i][j]==type&&locationMartrix[i+1][j]==type&&locationMartrix[i+2][j]==type&&locationMartrix[i+3][j]==type&&locationMartrix[i+4][j]==0){
					ff[0] = i+4;
				    ff[1] = j;
				    return ff;
				}
			}
		}
		return get(type);
	}
	public static int[] get(int type){
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
	public static void scan(int type){
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

}
