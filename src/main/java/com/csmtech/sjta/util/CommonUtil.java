package com.csmtech.sjta.util;
 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;

import javax.persistence.EntityManager;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
 
@Component
public class CommonUtil {
 
	private CommonUtil() {
		super();
	}
 
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
 
	public static void copyAndDeleteFile(Path source, Path destination) {
		try {
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			Files.delete(source);
		} catch (IOException e) {
			logger.error("Error occurred while copying and deleting file: {}", e.getMessage());
		}
	}
 
	public static void createDirectories(Path directory) throws IOException {
		if (!Files.exists(directory)) {
			Files.createDirectories(directory);
		}
	}
 
	public static String inputStreamDecoder(String data) {
		JSONObject requestObj = new JSONObject(data);
		byte[] decoded = Base64.getDecoder().decode(requestObj.getString(CommonConstant.REQUEST_DATA));
		return new String(decoded, StandardCharsets.UTF_8);
	}
 
	public static JSONObject inputStreamEncoder(String data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("RESPONSE_DATA", Base64.getEncoder().encodeToString(data.getBytes()));
		jsonObject.put("RESPONSE_TOKEN", getHmacMessage(Base64.getEncoder().encodeToString(data.getBytes())));
		return jsonObject;
	}
 
	public static String getHmacMessage(String message) {
		return Hashing.hmacSha256("P2@G2#N541688628304395".getBytes(StandardCharsets.UTF_8))
				.hashString(message, StandardCharsets.UTF_8).toString();
	}
 
	public static boolean hashRequestMatch(String requestData, String requestToken) {
		boolean flag = false;
		String HmacMessage = getHmacMessage(requestData);
		if (HmacMessage.equals(requestToken)) {
			flag = true;
		}
		return flag;
	}
 
	@SuppressWarnings("unchecked")
	public static List<Object[]> getDynResultList(EntityManager em, String query) {
		logger.info("Inside getDynResultList method of CommonUtil");
		return em.createNativeQuery(query).getResultList();
	}
 
	public static Object getDynSingleData(EntityManager em, String query) {
		logger.info("Inside getDynSingleData method of CommonUtil");
		return em.createNativeQuery(query).getSingleResult();
	}
 
	public static long getFileSize(String uploadDirectory, String filename) throws IOException {
		Path filePath = Paths.get(uploadDirectory).resolve(filename);
		return Files.size(filePath);
	}
 
	public static String formatSizeUnits(long bytes) {
		if (bytes >= 1073741824) {
			return String.format("%.2f GB", (double) bytes / 1073741824);
		} else if (bytes >= 1048576) {
			return String.format("%.2f MB", (double) bytes / 1048576);
		} else if (bytes >= 1024) {
			return String.format("%.2f KB", (double) bytes / 1024);
		} else if (bytes > 1) {
			return bytes + " bytes";
		} else if (bytes == 1) {
			return bytes + " byte";
		} else {
			return "0 bytes";
		}
	}
}
 