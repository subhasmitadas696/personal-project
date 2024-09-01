package com.csmtech.sjta.service;
import org.json.JSONArray;
import org.json.JSONObject;
 

public interface Noc_plotService {
JSONObject save(String noc_plot);
JSONObject getById(Integer Id);
JSONArray getAll(String formParams);
JSONObject deleteById(Integer id);
}