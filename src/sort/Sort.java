package sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sort {

	public static void main(String[] args) {
		/*String s1 = "全柴发机配件";
		String s2 = "液压件";
		System.out.println(s1.length()+"---"+s2.length());
		Integer len1 = 1;
		Integer len2 = 5;
		System.out.println(len1.compareTo(len2));*/
		
		List<String> list = new ArrayList<String>();
		list.add("液压件");
		list.add("超级跑车");
		list.add("大型柴油发动机");
		list.add("gh");
		
		//Collections.sort(list);
		Collections.sort(list, new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				Integer len1 = s1.length();
				Integer len2 = s2.length();
				return len1.compareTo(len2);
			}
		});
		System.out.println(list);
		
	}

}
