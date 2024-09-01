package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface Quarry_land_scheduleService {
JSONObject save(String quarry_land_schedule);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}