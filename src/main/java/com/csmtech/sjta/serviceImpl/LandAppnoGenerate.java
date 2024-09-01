package com.csmtech.sjta.serviceImpl;

public class LandAppnoGenerate {

	private LandAppnoGenerate() {
		super();
	}

	public static String generateNumber(String baseNumber) {
		int nextNumber = Integer.parseInt(baseNumber.substring(baseNumber.length() - 5).trim()) + 1;
		return baseNumber.substring(0, baseNumber.length() - 5) + String.format("%05d", nextNumber);
	}

}
