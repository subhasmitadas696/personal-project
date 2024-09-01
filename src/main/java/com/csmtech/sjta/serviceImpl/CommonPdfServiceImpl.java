package com.csmtech.sjta.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.csmtech.sjta.dto.AuctionPriviewDTO;
import com.csmtech.sjta.dto.LandAppResponseStructureDTO;
import com.csmtech.sjta.dto.TransactionGenerateDTO;
import com.csmtech.sjta.repository.LandApplicantNativeRepository;
import com.csmtech.sjta.repository.LeaseCaseRepository;
import com.csmtech.sjta.repository.ReceiptRepository;
import com.csmtech.sjta.repository.TenderAuctionRepository;
import com.csmtech.sjta.service.CommonPdfService;
import com.csmtech.sjta.util.CommonConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@Slf4j
public class CommonPdfServiceImpl implements CommonPdfService {

	@Value("${file.jasperPath}")
	private String jasperValPath;

	@Value("${file.path.imageUrl}")
	private String jasperValPathImage;

	@Autowired
	private LandApplicantNativeRepository nrepo;

	@Autowired
	private TenderAuctionRepository nativeRepo;

	@Autowired
	private LeaseCaseRepository repo;

	@Autowired
	private ReceiptRepository receiptRepo;

	public byte[] exportReport(String reportFormat, BigInteger id) throws FileNotFoundException, JRException {

//		LandAppResponseStructureDTO respones = nrepo.getCombinedDataForApplicant(id);
		LandAppResponseStructureDTO respones = new LandAppResponseStructureDTO();
		List<LandAppResponseStructureDTO> dataList = new ArrayList<>();
		dataList.add(respones);
		List<String> getrecordId = respones.getPlotto().stream().map(nn -> nn.getPlotNoId())
				.collect(Collectors.toList());
		String getrecordIdRes = String.join("\n", getrecordId);
		List<String> getrecordTotal = respones.getPlotto().stream().map(nn -> nn.getTotalArea().toString())
				.collect(Collectors.toList());
		String getrecordTotalRes = String.join("\n", getrecordTotal);
		List<String> getrecordPurchase = respones.getPlotto().stream().map(nn -> nn.getPurchaseArea().toString())
				.collect(Collectors.toList());
		String getrecordPurchaseRes = String.join("\n", getrecordPurchase);
		List<String> getrecordVerities = respones.getPlotto().stream().map(nn -> nn.getVarietiesId())
				.collect(Collectors.toList());
		String getrecordVeritiesRes = String.join("\n", getrecordVerities);
		List<String> documentName = respones.getDocsdto().stream().map(nn -> nn.getDocumentName())
				.collect(Collectors.toList());
		String documentNameRes = String.join("\n", documentName);

		File file = ResourceUtils.getFile(jasperValPath + "TenderAuctionPdf.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dataList);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("getrecordId", getrecordIdRes);
		parameters.put("getrecordTotal", getrecordTotalRes);
		parameters.put("getrecordPurchase", getrecordPurchaseRes);
		parameters.put("getrecordVerities", getrecordVeritiesRes);
		parameters.put("documentNameRes", documentNameRes);
		parameters.put("path", jasperValPathImage + "sjtaLogo2.PNG");

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);

		byte[] reportBytes;
		if (reportFormat.equalsIgnoreCase("pdf")) {
			reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		} else {
			throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
		}

		return reportBytes;
	}

	@Override
	public JSONObject exportReportForTenderAuction(String data) {
		JSONObject json = new JSONObject();

		try {
			ObjectMapper om = new ObjectMapper();

			AuctionPriviewDTO dto = om.readValue(data, AuctionPriviewDTO.class);
			List<Object[]> entity = nativeRepo.getAuctionsGetId(dto.getIntid());
			json.put(CommonConstant.STATUS_KEY, 200);
			json.put("result", new JSONArray(entity));

		} catch (Exception e) {
			log.error("Inside retrieve method of commonpdf, an error occurred: {}", e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;
	}

	/*
	 * @Override public byte[] exportReportForTenderAuction(String reportFormat,
	 * BigInteger id) throws FileNotFoundException, JRException {
	 * 
	 * List<Object[]> getRecord = nativeRepo.getAuctionsGetId(id);
	 * List<AuctionPriviewDTO> auctionPreviewDTOs = new ArrayList<>(); for (Object[]
	 * row : getRecord) { AuctionPriviewDTO dto = new AuctionPriviewDTO();
	 * dto.setIntid((BigInteger) row[0]); dto.setAuctionId((BigInteger) row[1]);
	 * dto.setCombinedValues((String) row[2]); dto.setLeasePeriodYears((Integer)
	 * row[3]); dto.setCommitteeMemberName((String) row[4]);
	 * dto.setRoyalty((BigDecimal) row[5]); dto.setFormMSubmitStartDate((Date)
	 * row[6]); dto.setFormMSubmitEndDate((Date) row[7]);
	 * dto.setSecurityDepositStartDate((Date) row[8]);
	 * dto.setSecurityDepositEndDate((Date) row[9]);
	 * dto.setBidDocumentDownloadStartDate((Date) row[10]);
	 * dto.setBidDocumentDownloadEndDate((Date) row[11]);
	 * dto.setDateOfTechnicalEvaluation((Date) row[12]);
	 * dto.setApplicationFeeNotRefund((BigDecimal) row[13]);
	 * dto.setSecurityAmountDeposit((BigDecimal) row[14]);
	 * dto.setSlotForAuctionFromDate((Date) row[15]);
	 * dto.setSlotForAuctionToDate((Date) row[16]); dto.setAuctionName2((String)
	 * row[17]); dto.setCommitteeMemberNameVal((String) row[18]);
	 * auctionPreviewDTOs.add(dto); }
	 * 
	 * File file = ResourceUtils.getFile(jasperValPath + "TenderAuctionPdf.jrxml");
	 * JasperReport jasperReport =
	 * JasperCompileManager.compileReport(file.getAbsolutePath());
	 * JRBeanCollectionDataSource datasource = new
	 * JRBeanCollectionDataSource(auctionPreviewDTOs); Map<String, Object>
	 * parameters = new HashMap<>(); JasperPrint jasperPrint =
	 * JasperFillManager.fillReport(jasperReport, parameters, datasource);
	 * 
	 * byte[] reportBytes; if (reportFormat.equalsIgnoreCase("pdf")) { reportBytes =
	 * JasperExportManager.exportReportToPdf(jasperPrint); } else { throw new
	 * IllegalArgumentException("Unsupported report format: " + reportFormat); }
	 * return reportBytes; }
	 */
	@Override
	public LandAppResponseStructureDTO gtePdfRecord(BigInteger id) {
		return nrepo.getCombinedDataForApplicant(id);
	}

	@Override
	public ByteArrayInputStream getReportForTransaction(String reportFormat, BigInteger id)
			throws FileNotFoundException, JRException {
		List<Object[]> result = receiptRepo.fetchFeePayment(id);
	    List<TransactionGenerateDTO> getRecoreForPayment=new ArrayList<>();
		for (Object[] row : result) {
			TransactionGenerateDTO dto = new TransactionGenerateDTO();
            dto.setApplicantName((String) row[1]);
            dto.setDistrictName((String) row[2]);
            dto.setTahasilName((String) row[3]);
            dto.setMouzaName((String) row[4]);
            dto.setKhataNo((String) row[5]);
            dto.setPlotNo((String) row[6]);
            dto.setTransactionNo((String) row[7]);
            dto.setPaymantDate((String) row[8]);
            dto.setAmountPaid((BigDecimal) row[9]);
            dto.setPaymantMethod((Short) row[10]);
            dto.setReceiptNo((String) row[11]);
            getRecoreForPayment.add(dto);
		}
		List<TransactionGenerateDTO> dataList = new ArrayList<>();
        if(getRecoreForPayment.size()==1) {
    		TransactionGenerateDTO dto = new TransactionGenerateDTO();
    		dto.setReceiptNo(getRecoreForPayment.get(0).getReceiptNo());
    		dto.setPaymantDate(getRecoreForPayment.get(0).getPaymantDate());
    		dto.setAmountPaid(getRecoreForPayment.get(0).getAmountPaid());
    		dto.setPaymantMethod(getRecoreForPayment.get(0).getPaymantMethod());
    		dto.setTransactionNo(getRecoreForPayment.get(0).getTransactionNo());
    		dto.setDistrictName(getRecoreForPayment.get(0).getDistrictName());
    		dto.setTahasilName(getRecoreForPayment.get(0).getTahasilName());
    		dto.setMouzaName(getRecoreForPayment.get(0).getMouzaName());
    		dto.setKhataNo(getRecoreForPayment.get(0).getKhataNo());
    		dto.setPlotNo(getRecoreForPayment.get(0).getPlotNo());
    		dto.setApplicantName(getRecoreForPayment.get(0).getApplicantName());
    		
    		StringBuilder add2 = new StringBuilder();
    		add2.append(getRecoreForPayment.get(0).getMouzaName());
    		add2.append(",");
    		add2.append(getRecoreForPayment.get(0).getTahasilName());
    		add2.append(",");
    		add2.append(getRecoreForPayment.get(0).getDistrictName());
    		dto.setAddressLine2(add2.toString());
    		dataList.add(dto);
        }else {
        	TransactionGenerateDTO dto = new TransactionGenerateDTO();
        	dto.setNoRecord("No Record Found");
        }
		File file = ResourceUtils.getFile(jasperValPath + "ApplicationFeeReceipt.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dataList);
	
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
		byte[] reportBytes;
		ByteArrayInputStream byteArrayInputStream;
		if (reportFormat.equalsIgnoreCase("pdf")) {
		    reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		    byteArrayInputStream = new ByteArrayInputStream(reportBytes);
		} else {
		    throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
		}
		return byteArrayInputStream;
	}

	@Override
	public ByteArrayInputStream getReportForFormMTransaction(String reportFormat, BigInteger id)
			throws FileNotFoundException, JRException {
		List<Object[]> result = receiptRepo.fetchFormMFeePayment(id);
	    List<TransactionGenerateDTO> getRecoreForPayment=new ArrayList<>();
		for (Object[] row : result) {
			TransactionGenerateDTO dto = new TransactionGenerateDTO();
            dto.setApplicantName((String) row[4]);
            dto.setDistrictName((String) row[5]);
            dto.setTahasilName((String) row[6]);
            dto.setMouzaName((String) row[7]);
            dto.setKhataNo((String) row[8]);
            dto.setPlotNo((String) row[9]);
            dto.setTransactionNo((String) row[3]);
            dto.setPaymantDate((String) row[1]);
            dto.setAmountPaid((BigDecimal) row[2]);
            dto.setReceiptNo((String) row[0]);
            getRecoreForPayment.add(dto);
		}
		List<TransactionGenerateDTO> dataList = new ArrayList<>();
        if(getRecoreForPayment.size()==1) {
    		TransactionGenerateDTO dto = new TransactionGenerateDTO();
    		dto.setReceiptNo(getRecoreForPayment.get(0).getReceiptNo());
    		dto.setPaymantDate(getRecoreForPayment.get(0).getPaymantDate());
    		dto.setAmountPaid(getRecoreForPayment.get(0).getAmountPaid());
    		dto.setPaymantMethod(getRecoreForPayment.get(0).getPaymantMethod());
    		dto.setTransactionNo(getRecoreForPayment.get(0).getTransactionNo());
    		dto.setDistrictName(getRecoreForPayment.get(0).getDistrictName());
    		dto.setTahasilName(getRecoreForPayment.get(0).getTahasilName());
    		dto.setMouzaName(getRecoreForPayment.get(0).getMouzaName());
    		dto.setKhataNo(getRecoreForPayment.get(0).getKhataNo());
    		dto.setPlotNo(getRecoreForPayment.get(0).getPlotNo());
    		dto.setApplicantName(getRecoreForPayment.get(0).getApplicantName());
    		
    		StringBuilder add2 = new StringBuilder();
    		add2.append(getRecoreForPayment.get(0).getMouzaName());
    		add2.append(",");
    		add2.append(getRecoreForPayment.get(0).getTahasilName());
    		add2.append(",");
    		add2.append(getRecoreForPayment.get(0).getDistrictName());
    		dto.setAddressLine2(add2.toString());
    		dataList.add(dto);
        }else {
        	TransactionGenerateDTO dto = new TransactionGenerateDTO();
        	dto.setNoRecord("No Record Found");
        }
		File file = ResourceUtils.getFile(jasperValPath + "FormMFeeReceipt.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dataList);
	
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
		byte[] reportBytes;
		ByteArrayInputStream byteArrayInputStream;
		if (reportFormat.equalsIgnoreCase("pdf")) {
		    reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		    byteArrayInputStream = new ByteArrayInputStream(reportBytes);
		} else {
		    throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
		}
		return byteArrayInputStream;
	}

	@Override
	public ByteArrayInputStream getReportForFinalTransaction(String reportFormat, BigInteger id)
			throws FileNotFoundException, JRException {
		List<Object[]> result = receiptRepo.fetchFinalFeePayment(id);
	    List<TransactionGenerateDTO> getRecoreForPayment=new ArrayList<>();
		for (Object[] row : result) {
			TransactionGenerateDTO dto = new TransactionGenerateDTO();
            dto.setApplicantName((String) row[4]);
            dto.setDistrictName((String) row[5]);
            dto.setTahasilName((String) row[6]);
            dto.setMouzaName((String) row[7]);
            dto.setKhataNo((String) row[8]);
            dto.setPlotNo((String) row[9]);
            dto.setTransactionNo((String) row[3]);
            dto.setPaymantDate((String) row[1]);
            dto.setAmountPaid((BigDecimal) row[2]);
            dto.setReceiptNo((String) row[0]);
            getRecoreForPayment.add(dto);
		}
		List<TransactionGenerateDTO> dataList = new ArrayList<>();
        if(getRecoreForPayment.size()==1) {
    		TransactionGenerateDTO dto = new TransactionGenerateDTO();
    		dto.setReceiptNo(getRecoreForPayment.get(0).getReceiptNo());
    		dto.setPaymantDate(getRecoreForPayment.get(0).getPaymantDate());
    		dto.setAmountPaid(getRecoreForPayment.get(0).getAmountPaid());
    		dto.setPaymantMethod(getRecoreForPayment.get(0).getPaymantMethod());
    		dto.setTransactionNo(getRecoreForPayment.get(0).getTransactionNo());
    		dto.setDistrictName(getRecoreForPayment.get(0).getDistrictName());
    		dto.setTahasilName(getRecoreForPayment.get(0).getTahasilName());
    		dto.setMouzaName(getRecoreForPayment.get(0).getMouzaName());
    		dto.setKhataNo(getRecoreForPayment.get(0).getKhataNo());
    		dto.setPlotNo(getRecoreForPayment.get(0).getPlotNo());
    		dto.setApplicantName(getRecoreForPayment.get(0).getApplicantName());
    		
    		StringBuilder add2 = new StringBuilder();
    		add2.append(getRecoreForPayment.get(0).getMouzaName());
    		add2.append(",");
    		add2.append(getRecoreForPayment.get(0).getTahasilName());
    		add2.append(",");
    		add2.append(getRecoreForPayment.get(0).getDistrictName());
    		dto.setAddressLine2(add2.toString());
    		dataList.add(dto);
        }else {
        	TransactionGenerateDTO dto = new TransactionGenerateDTO();
        	dto.setNoRecord("No Record Found");
        }
		File file = ResourceUtils.getFile(jasperValPath + "PostAllotmentFeeReceipt.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(dataList);
	
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
		byte[] reportBytes;
		ByteArrayInputStream byteArrayInputStream;
		if (reportFormat.equalsIgnoreCase("pdf")) {
		    reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);
		    byteArrayInputStream = new ByteArrayInputStream(reportBytes);
		} else {
		    throw new IllegalArgumentException("Unsupported report format: " + reportFormat);
		}
		return byteArrayInputStream;
	}
}
