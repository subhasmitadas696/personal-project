package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface TahasilMasterService {
JSONObject save(String tahasil_master);
JSONObject getById(String Id);
JSONObject getAll(String formParams);
JSONObject viewAll(String districtCode);
JSONObject getLastTahasilCode(String selDistrictCode);
}