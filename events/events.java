package events;
import  java.util.EventObject;
import  java.util.EventListener;
public class events {
	static public class Panel_over_event extends EventObject{	//一个面板结束的事件对象
		public Panel_over_event(Object source){
			super(source);
		}
	}
	static public class Add_zidan_event extends EventObject{	//发出一个子弹的事件对象
		public int x;	//子弹的x，y坐标；或许应该存储每次x，y移动的多少
		public int y;	//子弹的x，y坐标；
		public boolean ismy;	//是魂斗罗的子弹还是敌人的子弹。
		public int jiaodu;	//子弹打出时候的角度。
		
		public Add_zidan_event(int x,int y,boolean ismy,int jiaodu,Object source){
			super(source);
			this.x		= x;
			this.y		= y;
			this.ismy	= ismy;
			this.jiaodu	= jiaodu;
		}
	}
	/*
	static public class MenuPanel_over_linstener implements MenuPanel_over_linstener_jiekou{
		public void EventActivated(events.MenuPanel_over_event me) 
		{ 
		System.out.println("事件已经被触发"); 
		} 
	}
	*/
	public interface Panel_over_linstener_jiekou extends EventListener{	//EventListener是一个接口，可是为什么要用extends继承而不是用implements实现接口？
		public void EventActivated(events.Panel_over_event me);
	}
	public interface Add_zidan_jiekou extends EventListener{	//EventListener是一个接口，可是为什么要用extends继承而不是用implements实现接口？
		public void add_zidan(events.Add_zidan_event event);
	}
}
/*
 * 		事件，监听器
 * 事件类包含了要传递了一些消息
 * 监听器类是一个含有处理事件函数的类
 * 
 * 注册监听器就是把一个监听器的引用（指针）传递给要注册的事件源类。当事件发生时，事件源类根
 * 据这个指针调用监听器类的 * 处理方法函数
 * 
 * 要想实现自定义事件，要在事件源类中添加实现（如addDemoListener）来把这个处理类的指针添
 * 加到自己的监听器数组集中（这里要注意同步问题）
 */
