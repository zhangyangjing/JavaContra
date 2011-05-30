package jingling;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;
import panels.GamePanel;

public class Zidanlei{
	public static final int ZIDAN_JIAODU_0		= 1;	//子弹射出的八个方向
	public static final int ZIDAN_JIAODU_45		= 2;
	public static final int ZIDAN_JIAODU_90		= 3;
	public static final int ZIDAN_JIAODU_135	= 4;
	public static final int ZIDAN_JIAODU_180	= 5;
	public static final int ZIDAN_JIAODU_225	= 6;
	public static final int ZIDAN_JIAODU_270	= 7;
	public static final int ZIDAN_JIAODU_315	= 8;
	
	
	
	
	private static ArrayList list;	//当前的子弹列表
	private static ArrayList list_wait;	//空闲的子弹列表
	private GamePanel gp;	//对游戏面板的引用，用以得到地图，精灵的数据
	
	public Zidanlei(GamePanel gamepanel){
		list = new ArrayList();
		list_wait = new ArrayList();
		gp = gamepanel;
	}
	
	public void addzidan(int x,int y,boolean ismy,int jiaodu){
		System.out.println("添加了一个子弹");
		if(list_wait.size() != 0){	//如果空闲列表里有空闲的，那么就利用空闲子弹类生成
			System.out.println("从空闲列表里生成一个子弹");
			Zidan zd = (Zidan)list_wait.get(0);
			
			zd.x = x;
			zd.y = y;
			zd.ismy = ismy;
			zd.jiaodu = jiaodu;
			
			list.add(zd);	//添加到子弹列表里，从空闲列表删除
			list_wait.remove(0);
		}
		else{
			System.out.println("创建生成一个子弹");
			Zidan zd = new Zidan();			
			zd.x = x;
			zd.y = y;
			zd.ismy = ismy;
			zd.jiaodu = jiaodu;			
			list.add(zd);	//添加到子弹列表里，从空闲列表删除
		}
	}		
	public void todo(Graphics2D g){	//有外面调用
		this.move();
		this.check();
		this.draw(g);
	}
	private void move(){	//子弹的移动
		for(int i = 0;i < list.size();i++){
			Zidan zd = (Zidan)list.get(i);
			switch(zd.jiaodu ){
				case Zidanlei.ZIDAN_JIAODU_90 :
					zd.x += 2;
					break;
				case Zidanlei.ZIDAN_JIAODU_270 :
					zd.x -= 2;
					break;
				default :	//暂时调试用
					zd.x++;
					//zd.y++;
			}
		}
	}
	private void draw(Graphics2D g){	//画上子弹
		g.setColor(Color.RED);
		//System.out.println("子弹数组大小：" + list.size());
		for(int i = 0;i < list.size();i++){
			Zidan zd = (Zidan)list.get(i);			
			g.fillRect(zd.x - gp.jingling.zongx + gp.jingling.x,zd.y,5,5);
		}
	}
	private void check(){	//检测子弹
		for(int i = 0;i < list.size();i++){
			Zidan zd = (Zidan)list.get(i);
			if(zd.x - gp.jingling.zongx > 200 || gp.jingling.zongx - zd.x > 200 || zd.y < 0 || zd.y > 300){
				this.movetowait(i);
			}
		}
	}
	private void movetowait(int i){	//将出了当前界面的没用的子弹移到空闲子弹列表
		System.out.println("删除了一个子弹");
		list_wait.add(list.get(i));
		list.remove(i);
	}
	
	
	public class Zidan {	//子弹类
		private int x;	//子弹的x，y坐标；或许应该存储每次x，y移动的多少
		private int y;	//子弹的x，y坐标；
		private boolean ismy;	//是魂斗罗的子弹还是敌人的子弹。
		private int jiaodu;	//子弹打出时候的角度。
	}
}

