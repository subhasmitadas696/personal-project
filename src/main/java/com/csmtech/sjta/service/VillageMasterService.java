package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface VillageMasterService {
JSONObject save(String village_master);
JSONObject getById(String Id);
JSONObject getAll(String formParams);
JSONObject viewAll(String tahasilCode);
}