package com.csmtech.sjta.repository;

import org.json.JSONObject;

public interface TrackStatusRepository {

	Object[] fetchGrievanceStatus(JSONObject data);

	Object[] fetchLandApplicationStatus(JSONObject data);

}
