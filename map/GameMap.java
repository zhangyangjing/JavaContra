package map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/*
 * Created on 2009-3-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GameMap {
	
	public static ArrayList dipingxians;	//地平线的集合
	
	public GameMap(String mapfile){
		dipingxians = new ArrayList();
		System.out.println("生成了Map！");
		readxml(mapfile);
	}
	public ArrayList getdipingxian(){
		return dipingxians;
	}
	private void readxml(String mapfile){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		System.out.println("开始读地图文件");
		try {				
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(mapfile);		
			
			NodeList xians = doc.getElementsByTagName("xian");
			int shang = 0,xia = 0;	//一个地平线的上下作用域
			for(int i = 0;i < xians.getLength();i++){	//循环地平线				
				xia = Integer.parseInt(((Element)xians.item(i)).getAttribute("height"));			
				NodeList duans = ((Element)xians.item(i)).getElementsByTagName("duan");
				ArrayList arraylist = new ArrayList();	//地平线,此处暂时的				
				for(int j = 0;j < duans.getLength();j++){	//循环地平线中的每一个段						
					Element duan = (Element)duans.item(j);
					Map_duan map_duan = new Map_duan();					
					Element start = (Element)duan.getElementsByTagName("start").item(0);					
					map_duan.start = Integer.parseInt(start.getFirstChild().getNodeValue());					
					Element end = (Element)duan.getElementsByTagName("end").item(0);
					map_duan.end = Integer.parseInt(end.getFirstChild().getNodeValue());					
					Element kong = (Element)duan.getElementsByTagName("kong").item(0);
					map_duan.kong = Boolean.parseBoolean(kong.getFirstChild().getNodeValue());					
					arraylist.add(map_duan);
				}
				Dipingxian dipingxian = new Dipingxian();
				dipingxian.shang = shang;
				dipingxian.xia = xia;
				shang = xia;
				dipingxian.duans = arraylist;
				dipingxians.add(dipingxian);
			}
			/*
			Element xian = (Element)doc.getElementsByTagName("xian").item(0);	//得到一些默认的不可动态更改的属性，宽，高
			NodeList duans = xian.getElementsByTagName("duan");		
			
			for(int i = 0;i < duans.getLength();i++){
				Element duan = (Element)duans.item(i);
				Map_duan map_duan = new Map_duan();
				
				Element start = (Element)duan.getElementsByTagName("start").item(0);					
				map_duan.start = Integer.parseInt(start.getFirstChild().getNodeValue());					
				Element end = (Element)duan.getElementsByTagName("end").item(0);
				map_duan.end = Integer.parseInt(end.getFirstChild().getNodeValue());					
				Element kong = (Element)duan.getElementsByTagName("kong").item(0);
				map_duan.kong = Boolean.parseBoolean(kong.getFirstChild().getNodeValue());
				
				
				
				arraylist.add(map_duan);
			}
			*/
			System.out.println("读地图文件OVER");
			System.out.println("打印读入地图信息 begin");	
			for(int i = 0;i < dipingxians.size();i++){							
				System.out.println("---一条地平线---");
				System.out.println(	((Dipingxian)dipingxians.get(i)).shang);
				System.out.println(	((Dipingxian)dipingxians.get(i)).xia);
				System.out.println();
				
				for(int j = 0;j < ((Dipingxian)dipingxians.get(i)).duans.size();j++){
					System.out.println(((Map_duan)((Dipingxian)dipingxians.get(i)).duans.get(j)).start);
					System.out.println(((Map_duan)((Dipingxian)dipingxians.get(i)).duans.get(j)).end);					
					System.out.println(((Map_duan)((Dipingxian)dipingxians.get(i)).duans.get(j)).kong);
				}				
			}
			System.out.println("打印读入地图信息 over");
			/*
			for(int i = 0;i < arraylist.size();i++){	//遍历显示列表中的元素	调试用
				System.out.println(((Map_duan)arraylist.get(i)).end);
			}
			//*/
		}
		catch (Exception e) {
		e.printStackTrace();
		}
	}
	
	public static class Map_duan{	//描述地图中一个段的类
		public int start;
		public int end;
		public boolean kong;
	}
	public static class Dipingxian{
		public int shang;
		public int xia;
		public ArrayList duans;
	}
}

