package com.innee.czyhInterface.service.v1.invitation;

import java.util.Scanner;

public class test {
	
	/**
	 * amount为非0最大为99 的整数
	 * 在函数中需要将amount转化为中文的描述，类似四十五（按照日常的管理，例如14需要钻花为“十四”，而不是“一十四”）
	 * 数字大写说明：一二三四五六七八九
	 */
	
	public static void main(String[] args) {
	
		Scanner in = new Scanner(System.in);
		String z = in.nextLine();
		int length = z.length();
		if(length < 2){
			String replaceStr = replaceStr(z, 1);
			System.out.println(replaceStr);
		}else{
			char[] charArray = z.toCharArray();
			String replaceStr = replaceStr(String.valueOf(charArray[0]) , 2);
			replaceStr += replaceStr(String.valueOf(charArray[1]) , 1);
			System.out.println(replaceStr);
		}
	}
	
	 
	public static String  replaceStr(String str,int a){
		String result = "";
		if(str.equals("1")){
			if(a == 1){
				result = "一";
			}
		}else if(str.equals("2")){
			result = "二";
		}else if(str.equals("3")){
			result = "三";
		}else if(str.equals("4")){
			result = "四";
		}else if(str.equals("5")){
			result = "五";
		}else if(str.equals("6")){
			result = "六";
		}else if(str.equals("7")){
			result = "七";
		}else if(str.equals("8")){
			result = "八";
		}else if(str.equals("9")){
			result = "九";
		}
		if(a == 2){
			result += "十";
		}
		
		return result;
	}

}
