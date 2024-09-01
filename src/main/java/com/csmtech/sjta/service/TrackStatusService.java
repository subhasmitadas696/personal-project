package com.csmtech.sjta.service;

import org.json.JSONObject;

import com.csmtech.sjta.dto.TrackStatusDto;

public interface TrackStatusService {

	TrackStatusDto fetchGrievanceDetails(JSONObject formdata);

	TrackStatusDto fetchLandApplicationDetails(JSONObject formdata);

}
