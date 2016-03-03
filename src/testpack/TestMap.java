package testpack;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestMap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Map<String,String> map = new HashMap<String, String>();
		map.put("img1", "http://www.milaisz.com:10010/mobile_server/img/goods/good_20150530210933227/imgFile1.png");
		
		System.out.println(map.toString());*/
		
		String str = "[{\"img1\":\"http://www.milaisz.com:10010/mobile_server/img/goods/detail_1/1.jpg\"},{\"img2\":\"http://www.milaisz.com:10010/mobile_server/img/goods/detail_1/2.jpg\"}]";
		
		JSONArray json = JSONArray.fromObject(str);
		
		System.out.println(json);//{"c":"d","a":"b"}
		
		//System.out.println(json.get("c"));//d
	}	
}
