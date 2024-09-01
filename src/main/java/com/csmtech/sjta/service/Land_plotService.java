package com.csmtech.sjta.service;

import java.math.BigInteger;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Land_plotService {
	JSONObject save(String land_plot);

	JSONObject getById(Integer Id);

	JSONObject getByIntLandApplicantId(Integer IntLandApplicantId);

	JSONArray getAll(String formParams);

	JSONObject deleteById(Integer id);

	JSONObject deleteByIntLandApplicantId(Integer IntLandApplicantId);
}