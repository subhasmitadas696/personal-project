package com.csmtech.sjta.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;

import org.json.JSONObject;

import com.csmtech.sjta.dto.LandAppResponseStructureDTO;

import net.sf.jasperreports.engine.JRException;

public interface CommonPdfService {

	public byte[] exportReport(String reportFormat, BigInteger id) throws FileNotFoundException, JRException;
	
	public  JSONObject exportReportForTenderAuction(String data) ;
	
	public LandAppResponseStructureDTO gtePdfRecord(BigInteger id);
	
	public ByteArrayInputStream getReportForTransaction(String reportFormat, BigInteger id) throws FileNotFoundException, JRException;

	public ByteArrayInputStream getReportForFormMTransaction(String reportFormat, BigInteger id) throws FileNotFoundException, JRException;

	public ByteArrayInputStream getReportForFinalTransaction(String reportFormat, BigInteger id) throws FileNotFoundException, JRException;
	
}
