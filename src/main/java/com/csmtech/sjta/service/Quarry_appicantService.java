package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface Quarry_appicantService {
JSONObject save(String quarry_appicant);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}