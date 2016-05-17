package panels;

import events.events;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.MediaTracker;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import java.util.Enumeration;
import javax.swing.*;
///*
import javax.sound.midi.*;
import java.io.*;

//*/

import java.util.Timer;
import java.util.TimerTask;

public class MenuPanel extends JPanel implements KeyListener,FocusListener {
	private java.util.Timer timer; //循环定时检测是否继续
	private Vector repository;	//定义事件监听器集
	events.Panel_over_linstener_jiekou dl;
	//private events.MenuPanel_over_linstener_jiekou dl;
	int heigth,weidth;	//定义数据,宽高
	int selectid;	//当前的选项
	public boolean gonging;	//是否继续
	private int x,y;	//第一个选项指标的位置
	Image selectimg;
	BufferedImage bufimg;
	Sequencer midi;	//播放音乐的变量
	Sequencer midis;	
	
	public MenuPanel(){
		gonging = true;
		repository = new Vector();
		selectid = 0;
		x = 34;
		y = 142;
		Image img = this.getToolkit().createImage("image/menu.jpg");
		selectimg = this.getToolkit().createImage("image/xueze.jpg");
		MediaTracker tracker = new MediaTracker(this);	//等待图片加载完成
		tracker.addImage(img, 0);
		tracker.addImage(selectimg, 1);
		try     
        {   
			tracker.waitForAll();   //等待全部加入   
        }   
        catch   (Exception   ex)     
        {   
            System.err.println(ex.toString());   
        }
        
        bufimg = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);	//建立缓冲图片
		Graphics2D buf_grafc = bufimg.createGraphics();
		this.addKeyListener(this);	//添加键盘监听事件
		this.addFocusListener(this);	//添加焦点监听事件
        buf_grafc.drawImage(img,0,0,null);	//将背景画到缓冲图片        
        
        try {
        	//播放mid实验代码
        	///*
            Sequence seq = MidiSystem.getSequence(new File("music/bgmusic.mid"));
            midi = MidiSystem.getSequencer();
            midi.open();
            midi.setSequence(seq);
            midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);	//设置循环播放
            midi.start();    
            
            
            //*/
        	} 
        catch (Exception ex) {}    
        
        
        timer = new Timer(true); 
        timer.schedule(
        new TimerTask() { public void run(){check_gonging();} }, 0, 50); 
	}
	public void paint(Graphics g){ 	//重写了绘制函数
		Graphics2D buf_grafc = bufimg.createGraphics();
		buf_grafc.setColor(Color.BLACK);
		buf_grafc.fillRect(x,y,21,35);	//覆盖掉原来的选项指标
		buf_grafc.drawImage(selectimg,x,y+selectid*15,null);	//向背景图上绘画选项指标
		
		Graphics2D gphc2d = (Graphics2D)g;
		gphc2d.drawImage(bufimg,0,0,null);	//画到面板上		
	}
	public void addPanel_over_linstener(events.Panel_over_linstener_jiekou hdl) {	//自定义事件的事件源方处理	
		repository.addElement(hdl);//这步要注意同步问题
	}
	public void removeMenuPanel_over_linstener(events.Panel_over_linstener_jiekou hdl) {
		repository.removeElement(hdl);//这步要注意同步问题
	}
	public void shijian(events.Panel_over_event event) {	//触发执行事件
	    Enumeration enum = repository.elements();//这步要注意同步问题
	    while(enum.hasMoreElements())
	    {
	    	dl = (events.Panel_over_linstener_jiekou)enum.nextElement();
	    	dl.EventActivated(event);
	    }
	  }
	private void check_gonging() {
		///*
		if(gonging == false){	//如果暂停了
			if(midi.isRunning())
				midi.stop();
			if(midis != null &&midis.isRunning())
				midis.stop();
		}
		else{
			if(! midi.isRunning())
				midi.start();
			if(midis != null && ! midis.isRunning())
				midis.start();
		}
		//*/
    }
	private void playselectmusic(){
		
		 try {
        	//播放mid实验代码
		 	///*		 		
		 		Sequence seqs = MidiSystem.getSequence(new File("music/pass.mid"));		 		
	            midis = MidiSystem.getSequencer();
	            midis.setSequence(seqs);
	            midis.open();            
	            //midis.start();
            //*/		 	
		 	
            }       			
        catch (Exception ex) {}        
	}
	public void keyPressed(KeyEvent e) {	//键盘监听接口的实现
		if(e.getKeyCode()== KeyEvent.VK_KP_DOWN || e.getKeyCode()== KeyEvent.VK_DOWN ){	//按下向下键
			if (selectid == 0)
				selectid = 1;
			else
				selectid--;
			playselectmusic();
			this.repaint();			
			System.out.println(selectid);
		}
		if(e.getKeyCode()== KeyEvent.VK_KP_UP || e.getKeyCode()== KeyEvent.VK_UP ){	//按下向上键
			if (selectid == 1)
				selectid = 0;				
			else
				selectid++;
			playselectmusic();
			this.repaint();
			System.out.println(selectid);
		}
		if(e.getKeyCode()== KeyEvent.VK_ENTER){		//按下回车键，进入游戏
			timer.cancel();
			///*
			if(midi.isRunning())
				midi.stop();
			if(midis != null && midis.isRunning())
				midis.stop();
			midi.close();
			//*/
			shijian(new events.Panel_over_event(this));
			
		}
	}
	public void keyTyped(KeyEvent e) { }
	public void keyReleased(KeyEvent e) { }	
	public void focusLost(FocusEvent e){		
		gonging = false;
	}
	public void focusGained(FocusEvent e){		
		gonging = true;
	}
}

