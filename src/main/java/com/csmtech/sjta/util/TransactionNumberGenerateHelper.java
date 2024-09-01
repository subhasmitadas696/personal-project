package com.csmtech.sjta.util;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionNumberGenerateHelper {

	public static synchronized String createTransactionNumber(BigInteger landAppId) {
		String dateFormat = "ddMMyy";
		Date currentDate = new Date();
		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
		String formattedTime = currentTime.format(formatterTime);
		String formattedDate = new SimpleDateFormat(dateFormat).format(currentDate);
		log.info("createTransactionNumber method return tranction number success ..!!");
		return "AF" + formattedDate + formattedTime + landAppId;
	}
	
	
	public static synchronized String createTransactionNumberForForm(BigInteger landAppId) {
		String dateFormat = "ddMMyy";
		Date currentDate = new Date();
		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
		String formattedTime = currentTime.format(formatterTime);
		String formattedDate = new SimpleDateFormat(dateFormat).format(currentDate);
		log.info("createTransactionNumber method return tranction number success ..!!");
		return "FM" + formattedDate + formattedTime + landAppId;
	}
	
	
	public static synchronized String createTransactionNumberForPostAllotement(BigInteger landAllotementId) {
		String dateFormat = "ddMMyy";
		Date currentDate = new Date();
		LocalTime currentTime = LocalTime.now();
		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HHmmss");
		String formattedTime = currentTime.format(formatterTime);
		String formattedDate = new SimpleDateFormat(dateFormat).format(currentDate);
		log.info("createTransactionNumber method return tranction number success ..!!");
		return "PA" + formattedDate + formattedTime + landAllotementId;
	}

}
