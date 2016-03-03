package jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @author Administrator
 *  jsoup网页爬虫的测试
 */

public class JsoupUnit {
	
	public static void main(String[] args) throws Exception{
		String url = "http://www.open-open.com/jsoup/example-list-links.htm";
		System.out.println("Fetching  ————> " + url);
		//根据url的get请求拿到一个document
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		
		
		System.out.println("Links: " + links.size());
		for(Element link : links){
			System.out.println(" *a:<" + link.attr("abs:href") + ">   " + "(" + link.text().trim() +")");
		}
		
	}
	
}
