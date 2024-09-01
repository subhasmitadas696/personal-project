package com.csmtech.sjta.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CommonCaptchaGenerate {

	private static Map<String, Integer> captchaMap = new ConcurrentHashMap<>();

	public static void put(String captchaId, int result) {
		captchaMap.put(captchaId, result);
	}

	public static Integer get(String captchaId) {
		return captchaMap.get(captchaId);
	}

	public static void remove(String captchaId) {
		captchaMap.remove(captchaId);
	}

}
