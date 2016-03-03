package testpack;

public class TestString {

	public static void main(String[] args) {
		/*String str = "http://localhost:8080/mobile_server/api/op/batch/getDownFeedbackList";
		
		System.out.println(str.indexOf("api/"));
		
		String tmp = str.substring(str.indexOf("api") + 4);
		System.out.println(tmp);
		
		System.out.println(tmp.indexOf("/"));
		
		System.out.println(tmp.substring(tmp.indexOf("/") + 1));*/
		
		String str = "240*320等";
		
		str = str.substring(0, str.length()-1);
		
		System.out.println(str);
		
	}

}
