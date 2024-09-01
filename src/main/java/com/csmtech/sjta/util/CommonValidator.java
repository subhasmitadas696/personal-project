package com.csmtech.sjta.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonValidator {

	private CommonValidator() {
		super();
	}

	public static boolean isEmpty(String data) {
		return (data == null || data.isEmpty() || data.trim().isEmpty());
	}

	public static boolean minLengthCheck(String data, int minLength) {
		return (data.length() > 0 && data.length() < minLength);
	}

	public static boolean maxLengthCheck(String data, int maxLength) {
		return (data.length() > 0 && data.length() > maxLength);
	}

	public static boolean isCharecterKey(String charKey) {
		String charKeyRegex = "^[A-Za-z ]+$";
		Pattern pattern = Pattern.compile(charKeyRegex);
		Matcher matcher = pattern.matcher(charKey);
		return matcher.matches();
	}

	public static boolean isNumericKey(String numberKey) {
		String numberKeyRegex = "^\\d+$";
		Pattern pattern = Pattern.compile(numberKeyRegex);
		Matcher matcher = pattern.matcher(numberKey);
		return matcher.matches();
	}

	public static boolean isAlphaNumericKey(String alphaNumKey) {
		String alphaNumKeyRegex = "^[0-9a-zA-Z @.-/,]*$";
		Pattern pattern = Pattern.compile(alphaNumKeyRegex);
		Matcher matcher = pattern.matcher(alphaNumKey);
		return matcher.matches();

	}

	public static boolean isDecimal(String decimalKey) {
		String decimalKeyRegex = "^\\d+\\.\\d+$";
		Pattern pattern = Pattern.compile(decimalKeyRegex);
		Matcher matcher = pattern.matcher(decimalKey);
		return matcher.matches();
	}

	public static boolean emailCheck(String emailId) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(emailId);
		return matcher.matches();
	}

	public static boolean isSpecialCharKey(String text) {
		String textRegx = "[^&%$#@!~]*";
		Pattern pattern = Pattern.compile(textRegx);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

	public static boolean validNumberCheck(String numberKey) {
		String numberKeyRegex = "^\\d{1,3}$";
		Pattern pattern = Pattern.compile(numberKeyRegex);
		Matcher matcher = pattern.matcher(numberKey);
		return matcher.matches();
	}

	public static boolean validNumberWithSlashCheck(String numbeKey) {
		String numbeKeyRegex = "^[0-9/]+$";
		Pattern pattern = Pattern.compile(numbeKeyRegex);
		Matcher matcher = pattern.matcher(numbeKey);
		return matcher.matches();
	}

	public static boolean validTelCheck(String mobile) {
		String mobileRegex = "(0/91)?[7-9]\\d{9}";
		Pattern pattern = Pattern.compile(mobileRegex);
		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}

	public static boolean validPwdCheck(String password) {
		String pwRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&!()*?]).{8,15}$";

		Pattern pattern = Pattern.compile(pwRegex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static boolean isUrl(String url) {
		String urlRegex = "^(?:(?:https?|ftp)://)?[\\w\\d.-]+\\.[a-zA-Z]{2,}(?:/\\S*)?$";
		Pattern pattern = Pattern.compile(urlRegex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	public static boolean isPassword(String password) {
		String pwRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,15}$";
		Pattern pattern = Pattern.compile(pwRegex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static boolean blankCheckRdoDropChk(String data) {
		return (data == null || data.isEmpty() || data.trim().isEmpty()) || data.equals("0");
	}

	public static boolean validateFile(String str) {
		String regex = ".*\\.(?i)(jpe?g|png|gif|bmp|pdf|docx?|xls[xbmst]|csv|dbf|htm|html|mht|mhtml|ods|prn|txt|xla[mbs]|xltx|xlw|xps|odt|rtf|wps|xml|msword|mp[34]|og[agv]|webm|mpeg|zip)";
		return !str.matches(regex);
	}
}
