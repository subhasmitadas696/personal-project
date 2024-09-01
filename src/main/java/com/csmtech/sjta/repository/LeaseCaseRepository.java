package com.csmtech.sjta.repository;

import java.math.BigInteger;
import java.util.List;

public interface LeaseCaseRepository {
	
	public List<Object[]> getleaseCaseAllRecord(Integer limit,Integer offset);
	
	public BigInteger getcountFromgetleaseCaseAllRecord();
	
	public List<Object[]> getleaseCaseAllRecordWitjId(BigInteger caseId);
	
	public BigInteger getcountFromgetleaseCaseAllRecordWithId(BigInteger caseId);
	
	public List<Object[]> getleaseCaseAllRecordWitjLeaseCaseStatusDetails(BigInteger leaseCasePlotId);
	
	public List<Object[]> getLeaseCaseStatus(BigInteger leaseCasePlotId);
	
	public List<Object[]> getleaseCaseAllRecordUseLikeOperator(Integer limit, Integer offset,String caseNo,String caseYear);

	public BigInteger getcountFromgetleaseCaseAllRecorduseLikeOperator(String caseNo,String caseYear);

}
