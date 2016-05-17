package option;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;


public class Opotion {
		private String optfile;		//配置文件的位置
		public int kuan,gao,x,y;	//游戏的界面的大小,位置
		public int guanlength;		//游戏有多少关
		public static int guannow;			//当前是第几关		
		private static ArrayList guan;//关卡数组
		
		public Opotion(){	//构造函数
			guannow = 1;
			guan = new ArrayList();
			optfile = "option/option.xml";
			readopotion();
		}
		public static String getmapfile(){
			return ((Guan)guan.get(guannow)).mapfile;
		}
		public static String getguanname(){
			return ((Guan)guan.get(guannow)).name;
		}
		protected void finalize()throws Throwable{	//析构函数
			super.finalize();			
		}
		private void readopotion(){	//读取配置信息				
			File fl = new File(this.optfile);
			if(fl.exists()){	//判断配置文件是否存在
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				try {				
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse(this.optfile);
					
					Element dft	= (Element)doc.getElementsByTagName("default").item(0);	//得到一些默认的不可动态更改的属性，x,y,宽，高
					Element wd	= (Element)dft.getElementsByTagName("width").item(0);
					this.kuan	= Integer.parseInt(wd.getFirstChild().getNodeValue());
					Element ht	= (Element)dft.getElementsByTagName("height").item(0);
					this.gao	= Integer.parseInt(ht.getFirstChild().getNodeValue());
					Element xs	= (Element)dft.getElementsByTagName("x").item(0);
					this.x		= Integer.parseInt(xs.getFirstChild().getNodeValue());
					Element ys	= (Element)dft.getElementsByTagName("y").item(0);
					this.y		= Integer.parseInt(ys.getFirstChild().getNodeValue());
					
					NodeList guans = doc.getElementsByTagName("guan");
					this.guanlength = guans.getLength();
										
					
					for(int i = 0;i < this.guanlength;i++){
						Guan g = new Guan();
						Element guank = (Element)guans.item(i);	
						Element name = (Element)guank.getElementsByTagName("name").item(0);
						Element mapfile = (Element)guank.getElementsByTagName("map").item(0);						
						
						g.name = name.getFirstChild().getNodeValue();
						g.mapfile = mapfile.getFirstChild().getNodeValue();
						
						guan.add(g);
						
					}
					
					//System.out.println(kuan + "	" + gao + "	" + x + "	" + y);
				}
				catch (Exception e) {
				e.printStackTrace();
				}
			}
			else
			{
				System.out.println("文件不存在，将创建文件。");
			}			
		}
		
		public void saveopotion(){
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {				
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(this.optfile);
				
				Element dft = (Element)doc.getElementsByTagName("default").item(0);	//得到一些默认的不可动态更改的属性，宽，高
				Element el;
				el = (Element)dft.getElementsByTagName("x").item(0);
				el.getFirstChild().setNodeValue(String.valueOf(this.x));
				el = (Element)dft.getElementsByTagName("y").item(0);
				el.getFirstChild().setNodeValue(String.valueOf(this.y));
				
				TransformerFactory tFactory =TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new java.io.File(this.optfile));
				transformer.transform(source, result);				
			}
			catch (Exception e) {
			e.printStackTrace();
			}	
			System.out.println("配置已经保存。");
		}
}

class Guan{
	public String name;
	public String mapfile;
}

/*
 * 要用这个类实现以下功能：
 * 1.	从XML读取游戏数据：窗口数据，高手排行榜，上次是否保存进度，进度文件的索引
 * 2.	保存数据
 * 2.	程序运行时随时提供查询
 */
