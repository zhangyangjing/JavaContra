package panels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JPanel;

import events.events;
import jingling.Jingling;
import jingling.Zidanlei; 
import map.GameMap;
import option.Opotion;

public class GamePanel extends JPanel implements Runnable,KeyListener,FocusListener,events.Add_zidan_jiekou {	
	public Jingling jingling;	//精灵
	public GameMap	gamemap;	//地图的数据
	public Zidanlei zidan;		//子弹
	
	private int jinglingx;	//精灵一开始放到的x的位置
	private int lastpresskey;
	private int x,y,heigth,weidth;	//定义数据
	private boolean gonging;	//是否继续
	private Image img;	//读取图像数据
	private BufferedImage buf_image;	//绘制双缓冲
	private Sequencer midi;	//播放音乐的变量	
	private Vector repository;	//定义事件监听器集
	events.Panel_over_linstener_jiekou dl;
	
	public GamePanel(int weidth,int heigth) {	//构造化函数
		x = 0;
		y = 0;
		gonging = true;
		this.heigth = heigth;
		this.weidth = weidth;		
		gamemap = new GameMap(Opotion.getmapfile());
		zidan = new Zidanlei(this);
		
		//zidan.addzidan(0,50,true,Zidanlei.ZIDAN_JIAODU_225);	//添加一个子弹测试用
		//zidan.addzidan(10,100,true,Zidanlei.ZIDAN_JIAODU_225);	//添加一个子弹测试用
		
		repository = new Vector();
		
		this.addKeyListener(this);	//添加键盘监听事件
		this.addFocusListener(this);	//添加焦点监听事件
		
		try {
        	//播放mid实验代码
        	///*
            Sequence seq = MidiSystem.getSequence(new File("music/first.mid"));
            midi = MidiSystem.getSequencer();
            midi.open();
            midi.setSequence(seq);
            midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);	//设置循环播放
            midi.start();
            //*/
        	} 
        catch (Exception ex) {}    
			
		img = this.getToolkit().createImage("image/2004530941787.png");
		MediaTracker tracker = new MediaTracker(this);	//等待图片加载完成
		tracker.addImage(img, 0);
		try     
        {   
			tracker.waitForAll();   //等待全部加入
        }   
        catch   (Exception   ex)     
        {   
            System.err.println(ex.toString()); 
        }
        
		//System.out.println(img.getWidth(null));
        
		buf_image = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics2D buf_grafc = buf_image.createGraphics();        
		buf_grafc.drawImage(img,0,0,null);		
		
		jinglingx = 120;
		jingling = new Jingling(jinglingx,110,buf_image.getWidth(),this.weidth);	//精灵
		jingling.addPanel_over_linstener(new Ren_over());	//添加人物死掉时的结束事件
		jingling.addjia_zidan_listener(this);
		//jingling.
		
		//buf_grafc.setColor(Color.GREEN);//实验用代码
		//buf_grafc.drawRect(3,3,30,30);
		//buf_grafc.setColor(Color.RED);
		//buf_grafc.fillRect(4,4,29,29);
	}
	public void startdraw() {	//开始动画刷新，开始线程
		Thread thd = new Thread(this);
		thd.setPriority(Thread.MIN_PRIORITY);
		thd.start();
		System.out.println("THE THREAD IS START");
	}
	public void stop(){
		gonging = false;
		jingling.setdongzuo(Jingling.DZ_TING);	//让精灵暂停，否则得到焦点后即使不按走键，也会继续走
	}
	public void goon(){
		gonging = true;
	}
	
	public void run() {		//线程开始
		long time = System.currentTimeMillis(); 	//计算帧率用
		int i = 0;
		
		long times = System.currentTimeMillis();	//控制帧率
		
		while(true) {
			
			i++;	//此段判断帧率
			if(System.currentTimeMillis() - time > 1000){
				time = System.currentTimeMillis();
				System.out.println("当前帧率：" + i);
				i = 0;
			}			
			
			while(gonging == false){	//是否继续？
				try{
					Thread.sleep(10);	//休眠
				}
				catch(InterruptedException e)
				{	}
			}		
				
			this.repaint();
			
			
			
			try{	
					if(times - System.currentTimeMillis() < 9){	//控制帧率
						Thread.sleep(9 - (times - System.currentTimeMillis()));	//休眠
						times = System.currentTimeMillis();
					}									
				}			
			catch(InterruptedException e)
			{	}
			
		}
	}
	public void paint(Graphics g){ 	//重写了绘制函数		背景实现了双缓冲，但是这里没有使用双缓冲！！
		
		Graphics2D gphc2d = (Graphics2D)g;
		//System.out.println("Painting !");
		//gphc2d.drawImage(img,10,5,null);
		//gphc2d.drawImage((Image)buf_image,0,0,null);	//用这句话画出来的是一个黑框！！用上面一句却可以//已经搞定，是载入图像没有等到全载入就开始画的问题
		x = jingling.zongx - jinglingx + 1;
		//System.out.print(jingling.zongx);
		gphc2d.drawImage(buf_image.getSubimage(x,y,weidth,buf_image.getHeight()),0,0,null);	//高是图片的高宽是panel的宽
		jingling.todo(gphc2d);
		zidan.todo(gphc2d);
		
		//g.drawImage(bfimg,0,0,null);
	}
	public void addPanel_over_linstener(events.Panel_over_linstener_jiekou hdl) {	//自定义事件的事件源方处理	
		repository.addElement(hdl);//这步要注意同步问题
	}
	public void shijian(events.Panel_over_event event) {	//触发执行事件
	    Enumeration enum = repository.elements();//这步要注意同步问题
	    while(enum.hasMoreElements())
	    {
	    	dl = (events.Panel_over_linstener_jiekou)enum.nextElement();
	    	dl.EventActivated(event);
	    }
	  }
	public void keyPressed(KeyEvent e) {	//键盘监听接口的实现
		if(e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT){	//按下向右键
			lastpresskey = KeyEvent.VK_RIGHT;
			jingling.setfangxiang(Jingling.DZ_FX_QIAN);
			jingling.setdongzuo(Jingling.DZ_ZOU);
			//System.out.println("向前走");
		}
		if(e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT ){	//按下向左键
			lastpresskey = KeyEvent.VK_LEFT;
			jingling.setfangxiang(Jingling.DZ_FX_HOU);
			jingling.setdongzuo(Jingling.DZ_ZOU);		
			//System.out.println("向后走");
		}
		if(e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP ){	//按下向上键
			jingling.tiao();					
			//System.out.println("跳起！");
		}
		if(e.getKeyCode()== KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN ){	//按下向下键
			lastpresskey = KeyEvent.VK_DOWN;
			jingling.setdongzuo(Jingling.DZ_PA);
			//System.out.println("趴下");
		}
		if(e.getKeyCode()== KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_SPACE){	//按下空格键
			jingling.add_zidan();
			System.out.println("按下空格键");			
		}
	}
	public void keyTyped(KeyEvent e) { }
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN || e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode()== KeyEvent.VK_UP || e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT || e.getKeyCode()== KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT)
			if(e.getKeyCode() == lastpresskey)
				jingling.setdongzuo(Jingling.DZ_TING);
		//System.out.println("停住");
	}
	public void focusLost(FocusEvent e){
		midi.stop();
		gonging = false;
		jingling.setdongzuo(Jingling.DZ_TING);	//让精灵暂停，否则得到焦点后即使不按走键，也会继续走
	}
	public void focusGained(FocusEvent e){
		midi.start();
		gonging = true;		
	}
	public class Ren_over implements events.Panel_over_linstener_jiekou{	//MenuPanel结束事件的处理函数
		public void EventActivated(events.Panel_over_event me) 
		{		
			System.out.println("人死了，游戏结束！！！！！！！！！");
			shijian(new events.Panel_over_event(this));
		} 
	}
	public void add_zidan(events.Add_zidan_event event){	//响应子弹的事件，添加子弹
		zidan.addzidan(event.x,event.y,event.ismy,event.jiaodu);
	}
}
