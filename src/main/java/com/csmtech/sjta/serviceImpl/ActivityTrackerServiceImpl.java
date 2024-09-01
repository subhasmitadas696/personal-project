package com.csmtech.sjta.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.repository.ActivityTrackerRepository;
import com.csmtech.sjta.service.ActivityTrackerService;

@Service
public class ActivityTrackerServiceImpl implements ActivityTrackerService {

	@Autowired
	private ActivityTrackerRepository activityTrackerRepository;
	
	@Override
	public Integer save(String formParams) {
		JSONObject jsonData = new JSONObject(formParams);
		
		return activityTrackerRepository.save(jsonData);
	}

}
