package com.csmtech.sjta.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommonApplicationNumberGenerator {

	private static final String DATA_FILE_PATH = "LAcounter_data.txt";
	private static Integer coutTabRecord1;
	private static Date lastGeneratedDate;

	static {
		readCounterDataFromFile();
	}

	public static synchronized String generateApplicantUniqueNumber(String appName) {
		String dateFormat = "ddMMyy";

		Date currentDate = new Date();
		if (!isSameDate(currentDate, lastGeneratedDate)) {
			lastGeneratedDate = currentDate;
			coutTabRecord1 = 1; 
		}

		String formattedDate = new SimpleDateFormat(dateFormat).format(currentDate);
		String formattedCounter = String.format("%03d", getNextCounterValue());
		saveCounterDataToFile();
		return appName + formattedDate + formattedCounter;
	}

	private static boolean isSameDate(Date date1, Date date2) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(date1).equals(dateFormat.format(date2));
	}

	private static int getNextCounterValue() {
		return coutTabRecord1++;
	}

	private static void readCounterDataFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_PATH))) {
			lastGeneratedDate = new SimpleDateFormat("yyyyMMdd").parse(reader.readLine());
			coutTabRecord1 = Integer.parseInt(reader.readLine());
		} catch (Exception e) {
			lastGeneratedDate = new Date();
			coutTabRecord1 = 1;
		}
	}

	private static void saveCounterDataToFile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) {
			writer.write(new SimpleDateFormat("yyyyMMdd").format(lastGeneratedDate));
			writer.newLine();
			writer.write(coutTabRecord1.toString());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	
}
