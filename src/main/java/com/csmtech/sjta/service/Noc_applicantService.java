package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface Noc_applicantService {
JSONObject save(String noc_applicant);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}