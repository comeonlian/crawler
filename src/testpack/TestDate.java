package testpack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt1 = "2013-01-01";
		String dt2 = "2015-10-11";
		
		Date date1 = sdf.parse(dt1);
		Date date2 = sdf.parse(dt2);
		
		if(date2.getTime() > date1.getTime())
			System.out.println(" --- date2 > date1 --- ");
		
	}

}
