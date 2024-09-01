package com.csmtech.sjta.service;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.csmtech.sjta.entity.DistrictBoundary;
 

public interface DistrictMasterService {
JSONObject save(String district_master);
JSONObject getById(String Id);
JSONObject getAll(String formParams);
JSONObject viewAll();
}