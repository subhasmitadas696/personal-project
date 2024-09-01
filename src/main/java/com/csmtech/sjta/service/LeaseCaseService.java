package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import com.csmtech.sjta.dto.LeaseCaseDTO;
import com.csmtech.sjta.entity.LeaseCasePaymantEntity;

public interface LeaseCaseService {
	
	public List<LeaseCaseDTO> getleaseCaseAllRecord(String data);
	
	public BigInteger getcountFromgetleaseCaseAllRecord(String data);
	
	public List<LeaseCaseDTO> getleaseCaseAllRecordWithId(String data);
	
	public BigInteger getcountFromgetleaseCaseAllRecordWithId(String data);
	
	public List<LeaseCaseDTO> getleaseCaseAllRecordWitjLeaseCaseStatusDetails(String data);
	
	public JSONObject saveRecordForStatus(String data);
	
	public JSONObject insertPaymantRecord(String data);
	
	public List<LeaseCasePaymantEntity> getPaymantDataWithId(String data);
	
	public BigInteger getCountLeaseCasePayment(String data);
	
	public List<LeaseCaseDTO> getLeaseCaseStatus(String data);

}
