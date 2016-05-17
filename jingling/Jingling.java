package jingling;

import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Component;
import java.lang.String;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import events.events;
import map.GameMap;
import jingling.Zidanlei;


public class Jingling extends Component{
	public static final int DZ_ZOU		= 0;	//定义用到的枚举
	
	public static final int DZ_TING		= 3;
	public static final int DZ_PA		= 5;
	public static final int DZ_FX_QIAN	= 6;
	public static final int DZ_FX_HOU	= 7;
	
	public int linjiea;	//任务在地图两头的临界区里，不能在移动地图，应移动任务。
	public int linjieb;
	public int zongx;	//任务在整个关卡中的位置
	public int chaolinjie; 	//再走到地图结尾处的临界区是，超出了临界多少
	
	public int x,y;	//当前图片的坐标，右下角的 ！
	private int dongzuo_fx_current;	//当前的动作的方向
	private int dongzuo_type_current;	//当前动作的类型:跳,走,停
	private int dongzuo_tyep_yonghu;	//用户要求的动作类型	
	
	private boolean is_tiao;	//是不是在跳的状态
	private boolean tiao_state;	//跳的状态，向上为true
	private int tiao_heigh;	//当前跳的高度
	public int tiao_max;	//跳的最高高度
	
	private Dongzuo zou_qian;	//各种动作的图片
	private Dongzuo zou_hou;
	private Dongzuo ting_qian;
	private Dongzuo ting_hou;
	private Dongzuo pa_qian;
	private Dongzuo pa_hou;
	private Dongzuo tiao;
	
	//private ArrayList mapdipingxian;	//地图上的地平线，用来测试人物是不是在地平线上走，否则应往下掉。
	private float diao_num;	//向下掉的速度
	
	private Vector repository;	//定义事件监听器集
	private Vector addzidanvect;	
	events.Panel_over_linstener_jiekou dl;
	
	
	
	public Jingling(int x, int y,int zongchang,int huamianchang){
		this.x = x;
		this.y = y;
		this.zongx = x;	
		this.linjiea = x;
		//this.linjieb = zongchang - (huamianchang - x);
		this.linjieb = zongchang - (huamianchang - x) - 3;// - 23;
		
		repository = new Vector();
		addzidanvect = new Vector();
		
		is_tiao = false;
		tiao_state = true;
		tiao_heigh = 0;
		tiao_max = 60;
		diao_num = 0;
		
		dongzuo_fx_current = Jingling.DZ_FX_QIAN;
		dongzuo_type_current = Jingling.DZ_TING;
		zou_qian	= new Dongzuo("image/jingling_zou_qian.png",3);
		zou_hou		= new Dongzuo("image/jingling_zou_hou.png",3);
		ting_qian	= new Dongzuo("image/jingling_ting_qian.png",1);
		ting_hou	= new Dongzuo("image/jingling_ting_hou.png",1);
		pa_qian		= new Dongzuo("image/jingling_pa_qian.png",1);
		pa_hou		= new Dongzuo("image/jingling_pa_hou.png",1);
		tiao		= new Dongzuo("image/jingling_tiao.png",4);
	}
	private void move(){
		this.y +=this.diao_num;
		
		if(y > 400){	//测试人死了没
			System.out.println("人死了！！！！");
			shijian(new events.Panel_over_event(this));
		}
		
		if(dongzuo_type_current == DZ_ZOU){			
			switch(dongzuo_fx_current){
				case DZ_FX_QIAN :	//向前走	
					if(zongx <= linjiea){	//从前面开头临界往后走，有可能走出临界区域
						if(x > linjiea){	//已经走出临界区
							x = linjiea;
							zongx = linjiea;
						}
						else
						{
							x++;	//还没有走出临界区
						}
					}
					if(zongx > linjieb){	//在尾部的临界区（还要考虑人物会走出画面）						
						x++;
						chaolinjie++;
					}
					if(zongx >= linjiea && zongx <= linjieb){	//在非临界区的中间区段						
						zongx++;
					}
					break;
				case DZ_FX_HOU :	//向后走		
					if(zongx <= linjiea)	//在头临界区，要考虑会走出画面
						x--;
					if(zongx > linjieb){	//在尾临界区
						if(chaolinjie < 0){	//已经走出临界区	//这个判断有错误
							//x = linjieb;
							zongx = linjieb;
						}
						else
						{
							x--;
							chaolinjie--;
						}					
					}
					if(zongx >= linjiea && zongx <= linjieb)	//在非临界区的中间段
						zongx--;
					break;
			}
		}
		if(this.is_tiao == true){	//处理跳跃	
			if(this.tiao_state == true){	//方向向上
				y--;
				this.tiao_heigh++;
				if(this.tiao_heigh > this.tiao_max)	//如果跳到了顶
					this.tiao_state = false;				
			}
			else{
				y++;
				this.tiao_heigh--;
				if(this.tiao_heigh == 0){	//跳到了开始跳的位置
					this.tiao_state = true;
					this.is_tiao = false;
				}
			}
		}
		
		
		
			//处理往下掉的情况，先得到当前y所在地平线的高度，脚下是否为空
			
				boolean kong = true;
				int dpx_height = 500;	//当前地平线的高度
				
				for(int i = 0;i < GameMap.dipingxians.size();i++){
					if(this.y > ((GameMap.Dipingxian)GameMap.dipingxians.get(i)).shang && this.y <= ((GameMap.Dipingxian)GameMap.dipingxians.get(i)).xia){	//得到当前位置的地平线
						dpx_height = ((GameMap.Dipingxian)GameMap.dipingxians.get(i)).xia;	//设置当前地平线的高度		
						
						for(int j =0;j < ((GameMap.Dipingxian)GameMap.dipingxians.get(i)).duans.size();j++){	//得到当前所在位置是否脚下为空的
							if( this.zongx > ((GameMap.Map_duan)((GameMap.Dipingxian)GameMap.dipingxians.get(i)).duans.get(j)).start && this.zongx < ((GameMap.Map_duan)((GameMap.Dipingxian)GameMap.dipingxians.get(i)).duans.get(j)).end ){
								kong = ((GameMap.Map_duan)((GameMap.Dipingxian)GameMap.dipingxians.get(i)).duans.get(j)).kong ;
								break;
							}
						}
						break;
					}					
				}
				//System.out.println(y);////////////////////////////////////////				
				//System.out.println("当前的地平线：" + dpx_height);
				
				//System.out.println("当前地平线：" + dpx_height + "当前空：" + kong);
				
				/*
				for(int i = 0;i < mapdipingxian.size();i++){	//得到当前所在位置是否脚下为空的
					if(this.zongx > ((GameMap.Map_duan)mapdipingxian.get(i)).start && this.zongx < ((GameMap.Map_duan)mapdipingxian.get(i)).end){
						kong = ((GameMap.Map_duan)mapdipingxian.get(i)).kong ;
						break;
					}
				}
				*/
				
				if(this.is_tiao != true){	//检测脚下是不是空的，但要防止影响跳的效果	
					
						if(kong == false || this.y < dpx_height){	//脚下是空的话向下掉
							System.out.println("向下掉了");	
							this.diao_num += 0.07;							
						}	
						if(kong == true){	//当前y位置和地平线差距不够一次减的话直接到地平线
							if(dpx_height - this.y < this.diao_num ){
								this.diao_num = 0;	//这里有问题，当上面一句设置下掉，这里又取消了下掉
								this.y = dpx_height;
							}
						}
				}
				
				/*
				if(this.is_tiao != true){	//检测脚下是不是空的，但要防止影响跳的效果	
				
					if(kong == false || this.y < dpx_height){	//脚下是空的话向下掉
						System.out.println("向下掉了");	
						this.diao_num += 0.1;
						if(dpx_height - this.y < this.diao_num ){	//当前y位置和地平线差距不够一次减的话直接到地平线
							this.diao_num = 0;	//这里有问题，当上面一句设置下掉，这里又取消了下掉
							this.y = dpx_height;
						}
					}					
				}
				*/
				if(this.is_tiao == true && this.tiao_state == false){	//从低地平线跳到高处的地平线上的情况
					if(y == dpx_height && kong == true){	//已经到了高处地平线的高度
						this.is_tiao = false;
						this.tiao_state = true;
						this.tiao_heigh = 0;
						this.diao_num = 0;
					}
				}
				
				
				//if( (kong == false || this.diao_num != 0)){	//往下掉
				//	this.diao_num += 0.05;
				//}
			}
		//*/
	
	public void setdongzuo(int type){		
		this.dongzuo_tyep_yonghu	= type;	//当前的和用户期待的，有待改进。
		this.dongzuo_type_current	= type;	
	}
	public void tiao(){		
		if(this.dongzuo_type_current == Jingling.DZ_PA){
			System.out.println("由于当前趴着往下跳，所以掉下去了。");
			y++;	//向下移动一个像素，会导致地平线换作下面的一条地平线，达到了向下跳的效果
			this.dongzuo_type_current = Jingling.DZ_TING;
		}
		else{
			if(this.diao_num == 0)	//往下掉的时候不能再往上跳
				System.out.println("往上面跳了");				
				//System.out.println(y);				
				this.is_tiao = true;
		}
	}
	public void setfangxiang(int type){
		//this.dongzuo_tyep_yonghu	= type;	//当前的和用户期待的，有待改进。
		this.dongzuo_fx_current	= type;
	}
	public void setxy(int x,int y){
		this.x = x;
		this.y = y;
	}
	/* 由于把GameMap中的变量改为静态，故这里删去
	public void set_map_dipingxian(ArrayList dpx){
		this.mapdipingxian = dpx;		
		System.out.println("开始在精灵中测试地图");	//测试
		for(int i = 0;i < mapdipingxian.size();i++){			
			System.out.println(((GameMap.Map_duan)mapdipingxian.get(i)).start);			
		}		
	}
	*/
	public Rectangle getRectangle(){
		return new Rectangle(x,y,zou_qian.heigth,zou_qian.width);
	}
	public int getdongzuo(){
		return this.dongzuo_type_current;
	}
	public int getfangxiang(){
		return this.dongzuo_fx_current;
	}
	public void todo(Graphics2D g){	
		this.move();
		if(this.is_tiao == true){ 			//跳
			tiao.draw(g);
		}
		else
		{
			switch(dongzuo_type_current){
				case Jingling.DZ_ZOU :		//走
					if(dongzuo_fx_current == Jingling.DZ_FX_QIAN)
						zou_qian.draw(g);
					else
						zou_hou.draw(g);
					break;			
				case Jingling.DZ_TING :		//停
					if(dongzuo_fx_current == Jingling.DZ_FX_QIAN)
						ting_qian.draw(g);
					else
						ting_hou.draw(g);
					break;
				case Jingling.DZ_PA :		//趴
					if(dongzuo_fx_current == Jingling.DZ_FX_QIAN)
						pa_qian.draw(g);
					else
						pa_hou.draw(g);
					break;			
			}
		}
		//g.setColor(java.awt.Color.RED);	//调试用
		//g.drawString("zongx:" + String.valueOf(zongx) + " x:" + String.valueOf(x) + " A:" + String.valueOf(linjiea) + " B:" + String.valueOf(linjieb),20,20);
	}	
	
	public void shijian(events.Panel_over_event event) {	//触发执行事件
	    Enumeration enum = repository.elements();//这步要注意同步问题
	    while(enum.hasMoreElements())
	    {
	    	dl = (events.Panel_over_linstener_jiekou)enum.nextElement();
	    	dl.EventActivated(event);
	    }
	}
	public void add_zidan(){
		Enumeration enum = addzidanvect.elements();//这步要注意同步问题
		int fx = 0;
		int zdx = this.zongx;
		int zdy = this.y - 31;
		switch(this.dongzuo_fx_current){
			case Jingling.DZ_FX_QIAN :
				fx = Zidanlei.ZIDAN_JIAODU_90;
				zdx = this.zongx;				
				break;
			case Jingling.DZ_FX_HOU :
				fx = Zidanlei.ZIDAN_JIAODU_270;
				zdx = this.zongx - 35;
				break;
		}
		if(this.dongzuo_type_current == Jingling.DZ_PA){	//如果是趴着的话，子弹发出点应该更低一些
			zdy += 17;
		}
	    while(enum.hasMoreElements())
	    {	    	
	    	((events.Add_zidan_jiekou)enum.nextElement()).add_zidan(new events.Add_zidan_event(zdx,zdy,true,fx,this));	    	
	    }
	}
	public void addPanel_over_linstener(events.Panel_over_linstener_jiekou hdl) {	//自定义事件的事件源方处理	
		repository.addElement(hdl);//这步要注意同步问题
	}
	public void addjia_zidan_listener(events.Add_zidan_jiekou hdl) {	//自定义事件的事件源方处理	
		addzidanvect.addElement(hdl);//这步要注意同步问题
	}
	
	class Dongzuo extends Component{
		private int width,heigth;//每一幅图片的长，宽
		private int draw_count;	//当前的动作图片画到了第几画面
		private int img_conut;	//动作的图片总数
		private int count_to_draw;	//循环多少次就变换一次图像
		private int count_draw;	//循环了多少次
		private BufferedImage bfimg;
		public Dongzuo(String url,int img_conut){
			this.img_conut = img_conut;
			count_draw = 0;
			draw_count = 0;
			count_to_draw = 10;		
			
			Image img = Toolkit.getDefaultToolkit().createImage(url);		
			MediaTracker tracker = new MediaTracker(this);	//等待图片加载完成
			tracker.addImage(img, 0);	//如何完整加入图像？？		
			try     
	        {   
				tracker.waitForAll();   //等待全部加入
	        }   
	        catch   (Exception   ex)     
	        {   
	            System.err.println(ex.toString());   
	        }
	        
	        width	= img.getWidth(null)/img_conut;
	        heigth	= img.getHeight(null);
	        
	        bfimg = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			bfimg.getGraphics().drawImage(img,0,0,null);	//把图片画到缓冲区图片上		
		}
		public void draw(Graphics2D g){
			
			if(count_draw < count_to_draw){
				count_draw++;
			}
			else
			{	
				count_draw = 0;			
				if(draw_count < img_conut - 1)
					draw_count += 1;
				else
					draw_count = 0;
			}				
			g.drawImage(bfimg.getSubimage(draw_count*width,0,width,heigth),x - width,y - heigth,null);			
		}	
	}
}



















