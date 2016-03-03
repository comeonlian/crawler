package testpack;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class ArrayExam {
	/*
	 * 编写java程序，有5个同学，每个同学有3门课程的成绩，通过控制台输入学生成绩，利用二位数组保存。 
	 * 编写方法aveStudent计算每个同学的平均分，aveCourse计算每门课程的平均分。 显示并输出相关信息。
	 */
	private static Scanner sc = new Scanner(System.in);
	private static DecimalFormat format = new DecimalFormat("#.00");
	
	public static void main(String[] args) {
		int stus = 2;
		int cous = 3;
		double[][] grades = new double[stus][cous];
		//初始化 数组
		init(stus,cous,grades);
		TreeMap<String,Object> map = aveStudent(stus,cous,grades);
		print(map);
		map = aveCourse(stus, cous, grades);
		print(map);
	}
	private static void init(int row,int col,double[][] grades){
		for(int i = 0; i<row; i++){
			for(int j=0; j<col; j++){
				double grade = sc.nextDouble();
				grades[i][j] = grade;
			}
		}
	}
	/*
	 * 求每个学生的平均成绩
	 * */
	private static TreeMap<String,Object> aveStudent(int row,int col,double[][] grades){
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		double  sum,avg;
		String str;
		for(int i=0; i<row; i++){
			sum = 0d;
			for(int j=0; j<col ;j++){
				sum = sum + grades[i][j];
			}
			avg = sum / col;
			str = "第" + (i+1) + "位同学平均成绩是：";
			map.put(str, format.format(avg));
		}
		return map;
	}
	/*
	 * 求每门课程的平均成绩
	 * */
	private static TreeMap<String,Object> aveCourse(int row,int col,double[][] grades){
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		double  sum,avg;
		String str;
		for(int i=0; i<col; i++){
			sum = 0d;
			for(int j=0; j<row; j++){
				sum = sum + grades[j][i];
			}
			avg = sum / row;
			str = "第" + (i+1) + "门课平均成绩是：";
			map.put(str, format.format(avg));
		}
		return map;
	}
	/*
	 * 打印map集合
	 * */
	private static void print(Map<String,Object> map){
		System.out.println("-----------------    平均成绩         -----------------");
		Set<Map.Entry<String, Object>> set = map.entrySet();
		for(Map.Entry<String, Object> entry : set){
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
	}
}
