

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*;
import java.awt.event.WindowListener;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import events.events;

import panels.GamePanel;
import panels.MenuPanel;
import option.Opotion;


class game extends JFrame implements WindowListener,WindowFocusListener{
	public static final int PANEL_TYPE_GMAEPANEL = 0;
	public static final int PANEL_TYPE_MENUPANEL = 1;
	
	JButton button1;
	GamePanel gamepanl;
	MenuPanel menupanl;
	private Opotion opts;
	private int PanelType;
	
	public static void main(String[] args)
	{
		new game();
	}
	
	public game() {
		opts = new Opotion();	//全局的数据
		
		this.setLayout(null);	//手工排版
		this.setTitle("小游戏");
		this.setIconImage(this.getToolkit().createImage("image\\icon.png"));
		//this.setResizable(false);	//禁止调整大小
		//this.setUndecorated(true);	//设置无标题栏
		this.setBounds(opts.x,opts.y,264,255);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);
		this.addWindowFocusListener(this);
		
					
		/**
		button1 = new JButton("开始动画");	//添加一个按钮
		button1.setBounds(2,3,80,40);
		button1.addMouseListener(new button1_click());
		this.add(button1);
		*/
		///**
		menupanl = new MenuPanel();	//添加菜单面板MenuPanel
		menupanl.setBounds(0,0,opts.kuan,opts.gao);
		menupanl.addPanel_over_linstener(new MenuPanel_over_linsteners());
		this.add(menupanl);
		PanelType = game.PANEL_TYPE_MENUPANEL;
		//*/
		/**
		gamepanl = new GamePanel(opts.kuan,opts.gao);	//添加游戏面板GamePanel		
		gamepanl.setBounds(0,45,opts.kuan,opts.gao);
		this.add(gamepanl);
		*/
		
		this.setVisible(true);	
		menupanl.requestFocus();		
	}
	/*
	public void paint(Graphics g){
		BufferedImage bfimg = new BufferedImage(g.getClipBounds().width,g.getClipBounds().height,BufferedImage.TYPE_INT_ARGB);	//建立另一个缓冲图片
		Graphics2D newg = (Graphics2D)bfimg.getGraphics();
		//Graphics2D gphc2d = (Graphics2D)g;
		//System.out.println("Painting !");
		//gphc2d.drawImage(img,10,5,null);
		//gphc2d.drawImage((Image)buf_image,0,0,null);	//用这句话画出来的是一个黑框！！用上面一句却可以//已经搞定，是载入图像没有等到全载入就开始画的问题
		super.paint(newg);		
		
		g.drawImage(bfimg.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_FAST),0,0,null);
		System.out.println(this.getWidth());
	}
	*/
	public void windowLostFocus(WindowEvent e){
		setTitle("小游戏游戏-暂停");	
	}
	public void windowGainedFocus(WindowEvent e){
		setTitle("小游戏");				
		if(menupanl != null)
		{
			menupanl.gonging = true;
			//menupanl.requestFocus();	//让menupanl得到焦点，可是当menupanl已经卸下了，别的panl在运行怎么办？
			//System.out.println("dddd");
			switch(PanelType)
			{
				case game.PANEL_TYPE_GMAEPANEL:
					gamepanl.requestFocus();
					System.out.println("GMAEPANEL得到了焦点");
					break;
				case game.PANEL_TYPE_MENUPANEL:
					menupanl.requestFocus();
					System.out.println("MENUPANEL得到了焦点");
					break;
			}
		}
	}
	public void windowClosing(WindowEvent e) {	//关闭程序退出时保存配置
		opts.x = this.getX();		
		opts.y = this.getY();
		opts.saveopotion();
	}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}	
	public void windowActivated(WindowEvent e) {}
	
	public class MenuPanel_over_linsteners implements events.Panel_over_linstener_jiekou{	//MenuPanel结束事件的处理函数
		public void EventActivated(events.Panel_over_event me) 
		{		
		System.out.println("执行menu面板的over时间函数。");
		remove(menupanl);	//显示的调用remove函数？？
		//repaint();
		//menupanl.	//这里如何删除这个jpanel控件？？
		gamepanl = null;
		gamepanl = new GamePanel(opts.kuan,opts.gao);	//添加游戏面板GamePanel		
		gamepanl.setBounds(0,0,opts.kuan,opts.gao);
		add(gamepanl);
		gamepanl.requestFocus();
		PanelType = game.PANEL_TYPE_GMAEPANEL;
		System.out.println("开始动画");
		gamepanl.addPanel_over_linstener(new GamePanel_over_linsteners());
		gamepanl.startdraw();			
		} 
	}
	
	public class GamePanel_over_linsteners implements events.Panel_over_linstener_jiekou{	//MenuPanel结束事件的处理函数
		public void EventActivated(events.Panel_over_event me) 
		{		
			remove(gamepanl);
			gamepanl = null;
			repaint();
		} 
	}
	
	private class button1_click extends MouseAdapter	//一开始调试用，现在已经无用
	{
		public void mouseClicked(MouseEvent e)
		{
			gamepanl.startdraw();
			//System.out.println("sdfsdafasd");
		}
	}	
}


