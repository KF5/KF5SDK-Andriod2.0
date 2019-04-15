package com.kf5sdk.exam;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author chosen
 *
 * @version 创建时间：2016年6月15日  上午11:57:18
 */
public class Base64Utils {
	private static final Map<Integer, Character> base64CharMap = new HashMap<>();
	private static final String base64CharString = 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	static {
		// initialize base64 map
		for (int i=0;i<base64CharString.length();i++){
			char c = base64CharString.charAt(i);
			base64CharMap.put(new Integer(i), new Character(c));
		}
	}

	/**
	 * 加密
	 * @param origin
	 * @return
	 */
	public static String encode (String origin) {
		if (origin == null) {
			return null;
		}
		if (origin.length() == 0) {
			return "";
		}
		int length = origin.length();
		String binaryString = "";
		// to binary String
		for (int i=0;i<length;i++) {
			int ascii = origin.charAt(i);
			String binaryCharString = Integer.toBinaryString(ascii);
			while (binaryCharString.length() < 8) {
				binaryCharString = "0" + binaryCharString;
			}
			binaryString += binaryCharString;
		}

		// to base64 index
		int beginIndex = 0;
		int endIndex = beginIndex + 6;
		String base64BinaryString = "";
		String charString = "";
		while ((base64BinaryString = 
				binaryString.substring(beginIndex, endIndex)).length() > 0) {
			//if length is less than 6, add "0".
			while (base64BinaryString.length() < 6) {
				base64BinaryString += "0";
			}
			int index = Integer.parseInt(base64BinaryString, 2);
			char base64Char = base64CharMap.get(index);
			charString = charString + base64Char;
			beginIndex += 6;
			endIndex += 6;
			if (endIndex >= binaryString.length()) {
				endIndex = binaryString.length();
			}
			if (endIndex < beginIndex) {
				break;
			}
		}
		if (length % 3 == 2) {
			charString += "=";
		}
		if (length % 3 == 1) {
			charString += "==";
		}
		return charString;
	}

	/**
	 * 解密
	 * @param encodedString
	 * @return
	 */
	public static String decode(String encodedString) {
		if (encodedString == null) {
			return null;
		}
		if (encodedString.length() == 0) {
			return "";
		}
		//get origin base64 String
		String origin = encodedString.substring(0,encodedString.indexOf("="));
		String equals = encodedString.substring(encodedString.indexOf("="));

		String binaryString = "";
		//convert base64 string to binary string
		for (int i=0;i<origin.length();i++) {
			char c = origin.charAt(i);
			int ascii = base64CharString.indexOf(c);
			String binaryCharString = Integer.toBinaryString(ascii);
			while (binaryCharString.length() < 6) {
				binaryCharString = "0" + binaryCharString;
			}
			binaryString += binaryCharString;
		}
		// the encoded string has 1 "=", means that the binary string has append 2 "0"
		if (equals.length() == 1) {
			binaryString = binaryString.substring(0,binaryString.length()-2);
		}
		// the encoded string has 2 "=", means that the binary string has append 4 "0"
		if (equals.length() == 2) {
			binaryString = binaryString.substring(0,binaryString.length()-4);
		}

		// convert to String
		String charString = "";
		String resultString = "";
		int beginIndex = 0;
		int endIndex = beginIndex + 8;
		while ((charString = 
				binaryString.substring(beginIndex, endIndex)).length() == 8) {
			int ascii = Integer.parseInt(charString, 2);
			resultString += (char)ascii;
			beginIndex += 8;
			endIndex += 8;
			if (endIndex > binaryString.length()) {
				break;
			}
		}
		return resultString;
	}

}