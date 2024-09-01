package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import com.csmtech.sjta.dto.MeetingPaginationDTO;
import com.csmtech.sjta.dto.MeetingPlotsRecordDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface MeetingScheduleService {
	JSONObject save(String meeting_schedule);

	JSONObject getById(Integer Id,Integer auctionFlag);

	JSONObject getAll(String formParams);

	JSONObject deleteById(Integer id, Integer updateBy,Integer auctionFlag);

	JSONObject updateFile(String meeting_schedule);

	MeetingPaginationDTO viewAll(Integer pageNumber, Integer pageSize, String applicationNo, String khataNo, Date meetingDate,BigInteger meetingId);

	JSONObject validate(String meeting_schedule);

	JSONObject printPdf(String meeting_schedule);

	JSONObject sendMail(String data);
	
	public JSONObject getLandApplicationData(BigInteger meetingId,Integer auctionFlag); //BigInteger meetingMainId
	
	Integer saveRecordForMeetingUplodeMom(String parms) throws JsonMappingException, JsonProcessingException;
	
	public List<MeetingPlotsRecordDTO> getAllplotsRecord(String pageData);
	
	public BigInteger getRowCountOfDistinctPlotMeetings();

	public JSONObject getHistory(BigInteger meetingId);

}