package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.csmtech.sjta.dto.GrievanceGoSeenRecordDTO;
import com.csmtech.sjta.dto.GrievanceMainDTO;

public interface GrievanceService {

	JSONObject save(String grievance);

	JSONObject getById(Integer Id);

	JSONObject getAll(String formParams);

	JSONObject deleteById(Integer id);

	public List<GrievanceGoSeenRecordDTO> getgrivanceUserRecord(Integer statusId, Integer pageNumber, Integer pageSize,
			String selDistrictCode, String selTahasilCode, String selVillageCode, String grievanceNo);

	public List<GrievanceMainDTO> getRecordGrivanceUserMore(Integer grivanceId);

	public JSONObject updateGrievanceStatus(Integer grievanceId, Integer newStatus, String remark, java.sql.Date date);

	JSONObject updateGrievanceStatusFinal(Integer grievanceId, Integer newStatus, String remark);

	BigInteger countRecord(Integer grievanceId, String selDistrictCode, String selTahasilCode, String selVillageCode, String grievanceNo);

}