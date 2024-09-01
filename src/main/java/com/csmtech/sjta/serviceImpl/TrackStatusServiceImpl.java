package com.csmtech.sjta.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.TrackStatusDto;
import com.csmtech.sjta.repository.TrackStatusRepository;
import com.csmtech.sjta.service.TrackStatusService;

@Service
public class TrackStatusServiceImpl implements TrackStatusService {
	@Autowired
	private TrackStatusRepository trackStatusRepository;

	Object[] statusValObj = null;
	Object statusVal = null;
	String statusMsg = null;
	String statusType = null;

	TrackStatusDto trackStatusDto = new TrackStatusDto();

	@Override
	public TrackStatusDto fetchGrievanceDetails(JSONObject data) {
		statusValObj = trackStatusRepository.fetchGrievanceStatus(data);
		statusVal = null;
		if (statusValObj != null) {			
			statusVal = statusValObj[0];
		}

		trackStatusDto.setApplicationNo(null);
		trackStatusDto.setApplicationStatus(null);
		trackStatusDto.setStatusType(null);

		if (statusVal != null) {
			if (statusVal.toString().equalsIgnoreCase("0") || statusVal.toString().equalsIgnoreCase("3")) {
				statusMsg = "Pending at Grievance Officer";
				statusType = "P";
			} else if (statusVal.toString().equalsIgnoreCase("1")) {
				statusMsg = "Pending at Coordinating Officer";
				statusType = "P";
			} else if (statusVal.toString().equalsIgnoreCase("2")) {
				statusMsg = "On Hold";
				statusType = "P";
			} else if (statusVal.toString().equalsIgnoreCase("4")) {
				statusMsg = "Grievance is closed.";
				statusType = "A";
			} else if (statusVal.toString().equalsIgnoreCase("5")) {
				statusMsg = "Grievance is Rejected.";
				statusType = "R";
			}

			trackStatusDto.setApplicationNo(data.getString("applicationNo"));
			trackStatusDto.setApplicationStatus(statusMsg);
			trackStatusDto.setStatusType(statusType);
		}

		return trackStatusDto;
	}

	@Override
	public TrackStatusDto fetchLandApplicationDetails(JSONObject data) {
		statusValObj = trackStatusRepository.fetchLandApplicationStatus(data);

		trackStatusDto.setApplicationNo(null);
		trackStatusDto.setApplicationStatus(null);
		trackStatusDto.setStatusType(null);

		if (statusValObj != null) {
			statusVal = statusValObj[0];
			statusMsg = (String) statusValObj[1];

			if (statusVal.toString().equalsIgnoreCase("2") || statusVal.toString().equalsIgnoreCase("3")
					|| statusVal.toString().equalsIgnoreCase("4")) {
				statusType = "A";
			} else if (statusVal.toString().equalsIgnoreCase("5") || statusVal.toString().equalsIgnoreCase("6")
					|| statusVal.toString().equalsIgnoreCase("7") || statusVal.toString().equalsIgnoreCase("17")) {
				statusType = "R";
			} else {
				statusType = "P";
			}

			trackStatusDto.setApplicationNo(data.getString("applicationNo"));
			trackStatusDto.setApplicationStatus(statusMsg);
			trackStatusDto.setStatusType(statusType);
		}

		return trackStatusDto;
	}

}
