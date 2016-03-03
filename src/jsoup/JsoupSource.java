/**
 * 
 */
package jsoup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Administrator
 *
 */
public class JsoupSource {
	//public static final String URL = "http://www.mumayi.com/android/xitonggongju/downs/index.html";
	
	public static  String filepath = "page";
	public static  String PAGES ;
	
	private static JsoupSource jsoup = new JsoupSource();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//执行接受两个参数；一个是  某个子类的url，一个是 文件的路径
		if(args.length != 2){
    		System.out.println("请输入 两个参数 : url filepath");
    		return ;
    	}
		// 拿到文件路径
		filepath = args[1];
		
		mkdir(filepath);
		// 赋值
		PAGES = filepath +"/pages.txt";
		//1、拿到  ‘类别分类’ 的 url9
		//String[] urls = jsoup.oneStep(args[0]);
		
		String[] urls = new String[]{args[0]};
		//打印信息
		jsoup.printArray(urls);
		System.out.println("JsoupSource --- "+PAGES);
		// 2、获取页面信息
		jsoup.twoStep(urls);
		
		//调用       第二个模块   的  main方法   -- 1、处理 正常页面
		JsoupPage.main(new String[]{"1",filepath});
		// 2、处理错误页面
		JsoupPage.main(new String[]{"2",filepath});
	}
	
	
	private static void mkdir(String filename) {
		File file = new File(filename);
		if(!file.exists())
			file.mkdirs();
	}


	/**
	 * 3、再次for循环遍历 ‘页数’
		   在for循环的内部，依次对‘页数’的URL进行‘封装拼接’
		   最后在for循环的内部，对已经‘拼接’好的URL进行访问；
		   访问之后又会返回一个document
	 */
	public void threeStep(String commUrl,Integer pageNumber,BufferedWriter write) throws Exception{
		
		String pageUrl = "";
		for(int i=0; i<pageNumber; i++){
			pageUrl = commUrl + (i + 1) + ".html" + "==0";
			
//			System.out.println(pageUrl);
			
			write.write(pageUrl);
			write.newLine();
//			fourStep(pageUrl);
//			break;
		}
		write.flush();
		//write.close();
	}
	
	/**
	 * 2、for循环遍历 分类检索 的 所有地址links
	 * http://www.mumayi.com/android/xitonggongju/
	http://www.mumayi.com/android/zhutibizhi/
		   在for循环的内部，依次对 分类检索 的地址 进行访问；
		   每次访问 分类检索 的地址之后，又会返回一个document
		   从这个document中获取到 ‘分页地址信息’
	 */
	public void twoStep(String[] urls) throws Exception{
		BufferedWriter write = null;
		try{
			//将拼接之后的 页面的 url 写入到  文件中 
			File file=new File(PAGES);
			String url = file.getAbsolutePath();
			write = new BufferedWriter(new FileWriter(new File(url), false));
			
			Document doc = null;
			// http://www.mumayi.com/android/xitonggongju/
			Element div;
			Elements pageLinks;
			String pageUrl,commUrl;
			Integer pageNumber;
			for (String uri : urls) {
				doc = Jsoup.connect(uri).header("x_forword_for", JsoupPage.getRandomIp()).get();
				// <div class="pagination"> 锁定 分页信息
				div = doc.select("div.pagination").first();
				pageLinks = div.select("a[href]");
				//http://www.mumayi.com/android/xitonggongju/list_47_683.html
				pageUrl = pageLinks.last().attr("abs:href");
				commUrl = getPageUrl(pageUrl);
				pageNumber = getPageNumber(pageUrl);
				
				threeStep(commUrl, pageNumber,write);
				
				//break;
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(write != null)
				write.close();
		}
	}
	
	/**
	 * 1、连接主地址，获取返回的document
		   把‘分类检索’下面的多个分类的 URL地址拿到，拿绝对地址
		   例如：/android/xitonggongju/  ---  系统工具
	 */
	public String[] oneStep(String url) throws Exception {
		Document doc = Jsoup.connect(url).header("x_forword_for", JsoupPage.getRandomIp()).get();
		// <ul class="listleft clearf"> 
		// 拿到 UL : 分类检索 这个元素
		Element ul = doc.select("ul.listleft").first();
		// 拿到ul 下面的所有 li 子元素
		Elements  lis = ul.children();
		//定义一个string数组，分别存储 url 路径
		String[] urls = new String[lis.size()];
		int index = 0;
		Element a = null;
		// 依次 遍历 每个 li 元素
		for (Element li : lis) {
			//<li><a href="/android/xitonggongju/" title="系统工具" class="current" ><em>系统工具<i>(6821)</i></em></a></li>
			a = li.child(0);
			urls[index++] = a.attr("abs:href") + "downs/index.html";
		}
		return urls;
	}
	
	/*
	 * 切割字符串
	 * */
	public String getPageUrl(String pageUrl){
		//http://www.mumayi.com/android/xitonggongju/list_47_683.html
		int index = pageUrl.lastIndexOf("_");
		return pageUrl.substring(0, (index + 1));
	}
	/*
	 * 获取页数信息
	 * */
	public Integer getPageNumber(String pageUrl){
		//http://www.mumayi.com/android/xitonggongju/list_47_683.html
		int index1 = pageUrl.lastIndexOf("_");
		int index2 = pageUrl.lastIndexOf(".");
		Integer pageNumber = Integer.valueOf(pageUrl.substring( (index1 + 1), index2));
		return pageNumber;
	}
	
	/*
	 * 打印数组方法
	 * */
	public void printArray(String[] arr){
		System.out.println("size : " + arr.length);
		for (String s : arr) {
			System.out.println(s);
		}
	}
	
}
