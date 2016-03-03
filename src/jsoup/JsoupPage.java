/**
 * 
 */
package jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @author Administrator
 *
 */
public class JsoupPage {
	public static  String filepath = "page";
    public static  String PAGEDETAIL;
    public static  String ERRPAGES ;
    public static int flag = 0;
    /**
     * @param args
     *  对页面进行访问
     */
    public static void main(String[] args) throws Exception {
    	if(args.length != 2){
    		System.out.println("请输入参数：运行的指令(1-开始执行 : 2-重复执行)  文件路径 ");
    		return ;
    	}
    	//文件路径
    	filepath = args[1];
    	//给 静态 路径 赋值
    	PAGEDETAIL = filepath + "/pagedetail.txt";
        ERRPAGES = filepath + "/errpages.txt";
        
    	// 根据指令  设置读取的路径
    	String path = "";
    	if("1".equals(args[0]))
    		path = JsoupSource.PAGES;
    	else if("2".equals(args[0]))
    		path = ERRPAGES;
    	else{
    		System.out.println("指令输入错误!  1(开始执行) : 2(重复执行) ");
    		return ;
    	}
    	System.out.println("JsoupPage -- "+path);
    	// 执行
    	new JsoupPage().readAndExecute(path);
        // 如果执行完毕没有 请求不到的路径
    	/*if(flag==0)
    		deleteFile(ERRPAGES);*/
    	
    	// 执行    第三个模块    的main方法
    	JsoupDetail.main(new String[]{args[0],filepath});
    	
    }

    /**
     * 5、读取 txt文件中的信息 依次对页面进行访问
     * 
     */
    public void readAndExecute(String path) {
        BufferedReader read = null;
       
        try {
            
        	// 读数据源 	这里需要指定两个数据源
        	// 这个数据源 是 根据页面url 生成  app详情 url
            File file = new File(path);
            //这个是数据源是  重复执行  失败的url
        	//File file = new File(ERRPAGES);
            
            //如果文件不存在就直接返回
        	if(!file.exists())
        		return;
        	read = new BufferedReader(new FileReader(file));
            
            String line = "", durl="";
            String[] lines;
            int cnt = 0;
            
            String flag = "";
            while ((line = read.readLine()) != null) {
            	lines = line.split("==");
            	durl = lines[0];
            	cnt = Integer.valueOf(lines[1]);
            	if(cnt<5){
            		flag = fourStep(durl,cnt);
            		if("stop".equals(flag))
            			break;
            	}else{
            		continue;
            	}
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (read != null)
                    read.close();
                /*if (write != null)
                    write.close();*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 4、然后在这个返回的document中获取到10个软件的‘访问地址’； for循环依次对这10个地址进行访问， 每次访问一个软件的‘地址’，又会返回一个document
     */
    public String fourStep(String pageUrl,int cnt) throws Exception {
    	BufferedWriter write = null;
    	
    	try{
    		//把app详情页面写入到文件中
    		File file = new File(PAGEDETAIL);
        	String url = file.getAbsolutePath();
        	write = new BufferedWriter(new FileWriter(new File(url), true));
        	
	    	Connection conn = Jsoup.connect(pageUrl).header("x_forword_for", JsoupPage.getRandomIp());
	    	conn.timeout(8000);
	        Document doc = conn.get();
	        // <ul class="androidList clearf block ">
	        Element ul = doc.select("ul.androidList").first();
	        // 拿到ul 下面的所有 li 子元素
	        Elements lis = ul.children();
	        // 定义一个string数组，分别存储 每个App的点击 路径
	       // String[] androidUrls = new String[lis.size()];
	        //int index = 0;
	        Element a = null;
	        String date = "";
	        // 依次 遍历 每个 li 元素
	        for (Element li : lis) {
	            // <li><a href="http://www.mumayi.com/android-1.html" title="木蚂蚁市场  Mumayi Market"
	            a = li.child(0);
	            date = li.select("dt").text();
	            //System.out.println("-- 更新信息  --"+date);
	            if(date.contains("-"))
	            	return "stop";
	            
	            write.write(a.attr("abs:href") + "==0");
	        	write.newLine();
	        	write.flush();
	            
	        }
	        // printArray(androidUrls);
    	}catch(Exception e){
    		//e.printStackTrace();
    		flag++;
    		pageUrl = pageUrl + "==" + (++cnt);
    		System.out.println("error page url:" + pageUrl);
    		JsoupDetail.writeErrRequest(pageUrl, ERRPAGES);
    	}finally{
    		if(write!=null){
    			write.close();
    		}
    	}
	    return "go";
    }
    /*
     * 删除文件
     * 
     * */
    public static void deleteFile(String path){
    	File file = new File(path);
    	if(file.exists())
    		file.delete();
    }
    
    
    /*public static String generateRangeIp() {
    	
        Random rand = new Random();
        String ip = rand.nextInt(230) + "." + rand.nextInt(230) + "." + rand.nextInt(230) + "." + rand.nextInt(230);
        return ip;
    }*/
    
    /*
     * 随机生成国内IP地址
     */
    public static String getRandomIp(){
         
        //ip范围
        int[][] range = {{607649792,608174079},//36.56.0.0-36.63.255.255
                         {1038614528,1039007743},//61.232.0.0-61.237.255.255
                         {1783627776,1784676351},//106.80.0.0-106.95.255.255
                         {2035023872,2035154943},//121.76.0.0-121.77.255.255
                         {2078801920,2079064063},//123.232.0.0-123.235.255.255
                         {-1950089216,-1948778497},//139.196.0.0-139.215.255.255
                         {-1425539072,-1425014785},//171.8.0.0-171.15.255.255
                         {-1236271104,-1235419137},//182.80.0.0-182.92.255.255
                         {-770113536,-768606209},//210.25.0.0-210.47.255.255
                         {-569376768,-564133889}, //222.16.0.0-222.95.255.255
        };
         
        Random rdint = new Random();
        int index = rdint.nextInt(10);
        String ip = num2ip(range[index][0]+new Random().nextInt(range[index][1]-range[index][0]));
        return ip;
    }
 
    /*
     * 将十进制转换成ip地址
     */
    public static String num2ip(int ip) {
        int [] b=new int[4] ;
        String x = "";
         
        b[0] = (int)((ip >> 24) & 0xff);
        b[1] = (int)((ip >> 16) & 0xff);
        b[2] = (int)((ip >> 8) & 0xff);
        b[3] = (int)(ip & 0xff);
        x=Integer.toString(b[0])+"."+Integer.toString(b[1])+"."+Integer.toString(b[2])+"."+Integer.toString(b[3]); 
         
        return x; 
     }

}
