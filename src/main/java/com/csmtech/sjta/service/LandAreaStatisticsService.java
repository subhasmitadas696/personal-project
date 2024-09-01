package com.csmtech.sjta.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.csmtech.sjta.dto.StatisticsDTO;

public interface LandAreaStatisticsService{

	public StatisticsDTO fetchStatisticsInfo(String data);

	public List<Map<String, Object>> plotsDoc(String formParams);

	public List<Map<String, Object>> plotInfoForTableView(String formParams);

	public List<Map<String, Object>> landApplicationDetails(String formParams);

	public List<Map<String, Object>> landMeetingDetails(String formParams);

	public Integer landMerge(JSONObject data);

	public Integer landSplit(JSONObject data);

	public List<Map<String, Object>> getMergeSplitDetails(String type);

	public Integer updateRollBack(JSONObject formParams);
}
