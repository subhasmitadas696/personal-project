package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface StateMasterService {
JSONObject save(String state_master);
JSONObject getById(String Id);
JSONObject getAll(String formParams);
}