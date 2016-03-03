package jsoup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupDetail {
	public static String filepath = "page";
	public static  String ERRDETAILS ;
	public static int flag = 0;
	//数据库连接信息
    public static  Connection dbconn = null;
    public static  PreparedStatement ps = null;
    //日期格式化
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//公用字段
    public Document doc = null;
    public Elements lis = null, divs = null, uls = null;
    public Element ul = null , div = null, h1;
    public String appName = "" ,appType = "", appCategory = "", submitter = "", pkgname = "", sql ="";
    public String updateTime = "", programSize = "", systemRequire = "", screenResolution = "", userReviews="";
    public org.jsoup.Connection conn = null;
    
    
	public static void main(String[] args) {
		if(args.length != 2){
    		System.out.println("请输入参数：运行的指令(1-开始执行 : 2-重复执行)  文件路径 ");
    		return ;
    	}
    	//文件路径
    	filepath = args[1];
    	//重新  赋值
    	ERRDETAILS = filepath +"/errdetails.txt";
    	// 根据指令  设置读取的路径
    	String path = "";
    	if("1".equals(args[0]))
    		path = JsoupPage.PAGEDETAIL;
    	else if("2".equals(args[0]))
    		path = ERRDETAILS;
    	else{
    		System.out.println("指令输入错误! 1(开始执行) : 2(重复执行) ");
    		return ;
    	}
		System.out.println("JsoupDetail -- "+path);
		dbconn = getConn(); 
		new JsoupDetail().readAndExecute(path);
		closeSource();
		// 如果执行完毕没有 请求不到的路径
    	/*if(flag==0)
    		JsoupPage.deleteFile(ERRDETAILS);*/
	}
	
	/**
     * 6、读取 txt文件中的信息 依次对页面进行访问
     * 
     */
    public void readAndExecute(String path) {
        BufferedReader read = null;
        //BufferedWriter write = null;
        try {
        	
        	//读数据源
            File file = new File(path);
            //File file = new File(JsoupDetail.ERRDETAILS);
            
            read = new BufferedReader(new FileReader(file));
            
            String line = "", durl="";
            String[] lines;
            int cnt = 0;
            while ((line = read.readLine()) != null) {
            	try{
                	lines = line.split("==");
                	durl = lines[0];
                	cnt = Integer.valueOf(lines[1]);
                	if(cnt<5){
                		fiveStep(durl,cnt);
                	}else{
                		continue;
                	}
	                //break;
            	}catch(Exception e){
            		continue;
            	}
            }

        } catch (Exception e) {
           // e.printStackTrace();
            
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
     * 5、最后在这个返回的document中拿到需要的信息； 每次访问一个软件的‘地址’，又会返回一个document
     */
    public void fiveStep(String androidUrl,int cnt) throws Exception {
        
        // 得到 app 介绍的详细页面 ，从中拿到信息
       // System.out.println(androidUrl);
//        System.out.println(ip);
        try {
        	conn = Jsoup.connect(androidUrl).header("x_forword_for", JsoupPage.getRandomIp());
        	conn.timeout(8000);
            doc = conn.get();
            //<h1 class="iappname
            appName = doc.select("h1.iappname").first().text();
            appName = appName.substring(0,appName.indexOf(" "));
            // <li class="isli">
            // 获取 类别、更新时间、文件大小
            lis = doc.select("li.isli");
            appType = getResultStr(lis.get(0).text());
            appCategory = getResultStr(lis.get(1).text());
            updateTime = getResultStr(lis.get(2).text());
            programSize = getResultStr(lis.get(3).text());
            //<div class="sel_text fl">
            //获取  系统要求、分辨率
            divs = doc.select("div.sel_text");
            uls = doc.select("ul.l_list");
            // 封装 系统要求 
            systemRequire = divs.get(0).text() + "," + getSrc(uls.get(0).children());
            //<ul class="l_list"> 提取分辨率
            screenResolution = divs.get(1).text().substring(0, divs.get(1).text().length()-1) + "," + getSrc(uls.get(1).children());
            //<div class="iediter  拿到用户点评
            div = doc.select("div.iediter").first();
            if(null == div) //用户点评可能为空
            	userReviews = "";
            else
            	userReviews = getResultStr(div.text());
            // <ul class="author"> 拿到提交者和包名
            ul = doc.select("ul.author").first();
            lis = ul.children();
            submitter = getResultStr(lis.get(0).text());
            pkgname = getResultStr(lis.get(1).text());
            
            if(timeCorrect(updateTime)){
	            // 数据写入到数据库
	            //result = appType + "--" + appCategory + "--" + submitter + "--" + pkgname;
	            sql = "INSERT IGNORE INTO `mumayi_test`(app_name,pkgname,submitter,app_type,app_category,program_size,"+
	            		"system_require,screen_resolution,user_reviews,update_time,request_url,insert_time) values(?,?,?,?,?,?,?,?,?,?,?,NOW());";
	            ps = dbconn.prepareStatement(sql);
	            ps.setString(1, appName);
	            ps.setString(2, pkgname);
	            ps.setString(3, submitter);
	            ps.setString(4, appType);
	            ps.setString(5, appCategory);
	            ps.setString(6, programSize);
	            ps.setString(7, systemRequire);
	            ps.setString(8, screenResolution);
	            ps.setString(9, userReviews);
	            ps.setString(10, updateTime);
	            ps.setString(11, androidUrl);
	            ps.executeUpdate();
            }
            // System.out.println(result);
            
        } catch (Exception e) {
        	//e.printStackTrace();
        	flag++;
        	androidUrl = androidUrl + "==" + (++cnt);
            System.out.println("error detail url:" + androidUrl);
            JsoupDetail.writeErrRequest(androidUrl, ERRDETAILS);
        }
    }
    /**
     * 判断 日期 是否合适
     * @param 
     * @return
     * @throws ParseException 
     */
    private boolean timeCorrect(String dt) throws ParseException {
    	if(!dt.contains("-"))
    		return true;
    	Date date = sdf.parse(dt);
    	Date fdate = sdf.parse("2013-01-01");
    	if(date.getTime()>fdate.getTime())
    		return true;
		return false;
	}

	/*
     * 把请求失败的页面  写入到 文件 
     * 在 已有 内容之后 添加  
     * */
    public static void writeErrRequest(String errUrl,String path){
    	BufferedWriter write = null;
    	try{
	    	File file=new File(path);
			String url = file.getAbsolutePath();
			write = new BufferedWriter(new FileWriter(new File(url), true));
			
			write.write(errUrl);
        	write.newLine();
        	write.flush();
    	}catch(Exception e){
    		new Exception("wirteErrRequest(),写入文件出异常");
    	}finally{
    		try{
	    		if(write!=null)
	    			write.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
	/*
	 * 封装分辨率
	 * */
	public String getSrc(Elements uls){
		Element li = null;
		String src= "";
		for(int i=0; i<uls.size(); i++){
			li = uls.get(i);
			if(i != (uls.size()-1))
				src = src + li.text() + ",";
			else
				src = src + li.text();
		}
		return src;
	}
	
	 /*
     * 截取最终结果
     */
    public String getResultStr(String str) {
        if (null == str || "".equals(str))
            return "";
        int index = str.indexOf("：");
        str = str.substring(index + 1, str.length());
        return str;
    }

    /*
     * 获取数据库的链接
     */
    public static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String dbName = "jsoup";
        String passwrod = "12345";
        String userName = "root";
        String url = "jdbc:mysql://localhost:3306/" + dbName;

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, userName, passwrod);
            /*
             * PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery(); while (rs.next()) { System.out.println("id : " + rs.getInt(1) + " name : " +
             * rs.getString(2) + " password : " + rs.getString(3)); }
             */
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static void closeSource() {
    	try{
			if(dbconn!=null){
				dbconn.close();
			}
			if(ps!=null){
				ps.close();
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
}
