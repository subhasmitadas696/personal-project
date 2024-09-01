package com.csmtech.sjta.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.LeaseCaseDTO;
import com.csmtech.sjta.entity.LeaseCasePaymantEntity;
import com.csmtech.sjta.entity.LeaseCaseStatusEntity;
import com.csmtech.sjta.repository.LeaseCasePaymentRepository;
import com.csmtech.sjta.repository.LeaseCaseRepository;
import com.csmtech.sjta.repository.LeaseCaseStatusRepository;
import com.csmtech.sjta.service.LeaseCaseService;
import com.csmtech.sjta.util.CommonConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaseCaseServiceImpl implements LeaseCaseService {

	@Autowired
	private LeaseCaseRepository leaseRepo;

	@Autowired
	private LeaseCaseStatusRepository repoStatus;

	@Autowired
	private LeaseCasePaymentRepository paymantRepo;

	@SuppressWarnings("unused")
	@Override
	public List<LeaseCaseDTO> getleaseCaseAllRecord(String data) {
		List<LeaseCaseDTO> getallRecord = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		Integer pageSize = jsonData.getInt("size");
		Integer pageNo = jsonData.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		String caseNo = jsonData.getString("caseNo");
		String caseYear = jsonData.getString("caseYear");
		List<Object[]> getRecord;
		if (!"".equals(caseNo) || !"".equals(caseYear)) {
			getRecord = leaseRepo.getleaseCaseAllRecordUseLikeOperator(pageSize, offset, caseNo, caseYear);
			log.info("Search work....!!");
		} else {
			getRecord = leaseRepo.getleaseCaseAllRecord(pageSize, offset);
			log.info("Normal Work...!!");
		}

		for (Object[] result : getRecord) {
			LeaseCaseDTO dto = new LeaseCaseDTO();
			dto.setLeaseCaseId((BigInteger) result[0]);
			dto.setCaseYear((String) result[1]);
			dto.setApplicantName((String) result[2]);
			dto.setAddress((String) result[3]);
			dto.setContactNo((String) result[4]);
			dto.setDocumentSubmitted((String) result[5]);
			dto.setCaseNo((String) result[6]);
			getallRecord.add(dto);
		}
		log.info("return service of getleaseCaseAllRecord method success..!!");
		return getallRecord;
	}

	@Override
	public BigInteger getcountFromgetleaseCaseAllRecord(String data) {
		log.info("return service of getcountFromgetleaseCaseAllRecord method success..!!");
		BigInteger count = null;
		JSONObject jsonData = new JSONObject(data);
		String caseNo = jsonData.getString("caseNo");
		String caseYear = jsonData.getString("caseYear");
		if (!"".equals(caseNo) || !"".equals(caseYear)) {
			log.info("Search count work....!!");
			count = leaseRepo.getcountFromgetleaseCaseAllRecorduseLikeOperator(caseNo, caseYear);
		} else {
			log.info("Normal count  Work...!!");
			count = leaseRepo.getcountFromgetleaseCaseAllRecord();
		}
		return count;
	}

	@Override
	public List<LeaseCaseDTO> getleaseCaseAllRecordWithId(String data) {
		List<LeaseCaseDTO> getallRecord = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		List<Object[]> getRecord = leaseRepo.getleaseCaseAllRecordWitjId(jsonData.getBigInteger("caseId"));
		for (Object[] result : getRecord) {
			LeaseCaseDTO dto = new LeaseCaseDTO();
			dto.setLeaseCasePlotId((BigInteger) result[7]);
			dto.setDistrictName((String) result[8]);
			dto.setTahasilName((String) result[9]);
			dto.setVillageName((String) result[10]);
			dto.setPoliceName((String) result[11]);
			dto.setKhataNo((String) result[12]);
			dto.setPlotNo((String) result[13]);
			dto.setTotalArea((BigDecimal) result[14]);
			dto.setPurchaseArea((BigDecimal) result[15]);
			dto.setKissam((String) result[16]);
			dto.setRsdNo((String) result[17]);
			dto.setIsCaseMatter((Short) result[18]);
			dto.setCaseYear((String) result[1]);
			dto.setApplicantName((String) result[2]);
			dto.setAddress((String) result[3]);
			dto.setContactNo((String) result[4]);
			dto.setDocumentSubmitted((String) result[5]);
			dto.setCaseNo((String) result[6]);
			dto.setExtent((String) result[19]);
//			dto.setLeaseCaseStatusId((BigInteger) result[19]);
//			dto.setFieldInquery((Short) result[20]);
//			dto.setDlscMeeting((Date) result[21]);
//			dto.setTlscMeeting((Date) result[22]);
//			dto.setMcMeeting((Date) result[23]);
//			dto. setNoticeIssued((Short) result[24]);
//			dto.setConsiderationMonyDeposite((Short) result[25]);
//			dto.setStatus((Short) result[26]);
//			dto.setRemerk((String) result[27]);
			getallRecord.add(dto);
		}
		log.info("return service of getleaseCaseAllRecordWithId method success..!!");
		return getallRecord;
	}

	@Override
	public BigInteger getcountFromgetleaseCaseAllRecordWithId(String data) {
		JSONObject jsonData = new JSONObject(data);
		return leaseRepo.getcountFromgetleaseCaseAllRecordWithId(jsonData.getBigInteger("caseId"));
	}

	@Override
	public List<LeaseCaseDTO> getleaseCaseAllRecordWitjLeaseCaseStatusDetails(String data) {
		List<LeaseCaseDTO> getallRecord = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		List<Object[]> getRecord = leaseRepo
				.getleaseCaseAllRecordWitjLeaseCaseStatusDetails(jsonData.getBigInteger("leaseCasePlotId"));
		for (Object[] result : getRecord) {
			LeaseCaseDTO dto = new LeaseCaseDTO();
			dto.setLeaseCasePlotId((BigInteger) result[7]);
			dto.setDistrictName((String) result[8]);
			dto.setTahasilName((String) result[9]);
			dto.setVillageName((String) result[10]);
			dto.setPoliceName((String) result[11]);
			dto.setKhataNo((String) result[12]);
			dto.setPlotNo((String) result[13]);
			dto.setTotalArea((BigDecimal) result[14]);
			dto.setPurchaseArea((BigDecimal) result[15]);
			dto.setKissam((String) result[16]);
			dto.setRsdNo((String) result[17]);
			dto.setIsCaseMatter((Short) result[18]);
			dto.setLeaseCaseStatusId((BigInteger) result[19]);
			dto.setFieldInquery((Short) result[20]);
			dto.setDlscMeeting((Date) result[21]);
			dto.setTlscMeeting((Date) result[22]);
			dto.setMcMeeting((Date) result[23]);
			dto.setNoticeIssued((Short) result[24]);
			dto.setConsiderationMonyDeposite((Short) result[25]);
			dto.setStatus((Short) result[26]);
			dto.setRemerk((String) result[27]);
			dto.setSlcMeeting((Date) result[28]);
			getallRecord.add(dto);
		}
		log.info("return service of getleaseCaseAllRecordWitjLeaseCaseStatusDetails method success..!!");
		return getallRecord;
	}

	@Override
	public JSONObject saveRecordForStatus(String data) {
		JSONObject js = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			LeaseCaseStatusEntity leaseList = om.readValue(data, LeaseCaseStatusEntity.class);
			repoStatus.save(leaseList);
			if (leaseList.getFlagInsertUpdate() == 1) {
				js.put(CommonConstant.STATUS_KEY, 202);
				log.info("return service of saveRecordForStatus insert success..!!");
			} else {
				js.put(CommonConstant.STATUS_KEY, 200);
				log.info("return service of saveRecordForStatus update success..!!");
			}
		} catch (Exception e) {
			log.info("getting exception" + e);
			js.put("status", 500);
		}
		return js;
	}

	@Override
	public JSONObject insertPaymantRecord(String data) {
		JSONObject js = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			LeaseCasePaymantEntity leaseList = om.readValue(data, LeaseCasePaymantEntity.class);
			LeaseCasePaymantEntity paymentRecord = paymantRepo.save(leaseList);
			if (paymentRecord.getLeasecasepaymantId() != null) {
				js.put("status", 200);
				log.info("return service of saveRecordForStatus insert success..!!");
			} else {
				js.put("status", 404);
				log.info("return service of saveRecordForStatus update success..!!");
			}
		} catch (Exception e) {
			log.info("getting exception" + e);
			js.put("status", 500);
		}
		return js;
	}

	@Override
	public List<LeaseCasePaymantEntity> getPaymantDataWithId(String data) {
		JSONObject jsdata = new JSONObject(data);
		Integer pageSize = jsdata.getInt("size");
		Integer pageNo = jsdata.getInt("pageNo");
		Integer offset = (pageNo - 1) * pageSize;
		return paymantRepo.getPaymantWithPlotId(jsdata.getBigInteger("leaseCasePlotId"), pageSize, offset);

	}

	@Override
	public BigInteger getCountLeaseCasePayment(String data) {
		JSONObject jsonData = new JSONObject(data);
		return paymantRepo.countPaymantRecord(jsonData.getBigInteger("leaseCasePlotId"));
	}

	@Override
	public List<LeaseCaseDTO> getLeaseCaseStatus(String data) {
		List<LeaseCaseDTO> getallRecord = new ArrayList<>();
		JSONObject jsonData = new JSONObject(data);
		List<Object[]> getRecord = leaseRepo.getLeaseCaseStatus(jsonData.getBigInteger("leaseCasePlotId"));
		for (Object[] result : getRecord) {
			LeaseCaseDTO dto = new LeaseCaseDTO();
			dto.setFieldInquery((Short) result[0]);
			dto.setDlscMeeting((Date) result[1]);
			dto.setTlscMeeting((Date) result[2]);
			dto.setMcMeeting((Date) result[3]);
			dto.setSlcMeeting((Date) result[4]);
			dto.setNoticeIssued((Short) result[5]);
			dto.setConsiderationMonyDeposite((Short) result[6]);
			dto.setStatus((Short) result[7]);
			dto.setRemerk((String) result[8]);
			getallRecord.add(dto);
		}
		log.info("return service of getLeaseCaseStatus method success..!!");
		return getallRecord;
	}
}
