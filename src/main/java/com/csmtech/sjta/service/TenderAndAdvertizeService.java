package com.csmtech.sjta.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.csmtech.sjta.dto.TenderAdvertisementDTO;
import com.csmtech.sjta.entity.DistrictMaster;
import com.csmtech.sjta.entity.KhatianInformation;
import com.csmtech.sjta.entity.PlotInformation;
import com.csmtech.sjta.entity.TahasilMaster;
import com.csmtech.sjta.entity.TenderAndAdvertizeEntity;
import com.csmtech.sjta.entity.TenderType;
import com.csmtech.sjta.entity.VillageMaster;

public interface TenderAndAdvertizeService {

	public List<TenderAdvertisementDTO> getAllTender(Integer pageNumber, Integer pageSize, String title);

	public TenderAndAdvertizeEntity findByTenderAdvertisementId(BigInteger tenderAdvertisementId);

	public TenderAndAdvertizeEntity updateTender(TenderAndAdvertizeEntity tender);

	public JSONObject saveRecord(String tenderData);

	public List<TenderType> getAllTenderType();

	public Integer deleteTender(Integer createdBy, BigInteger tenderAdvertisementId);

	public List<DistrictMaster> getAllDistrict();

	public List<TahasilMaster> getAllTehsil(String txtDistrictCode);

	public List<VillageMaster> getAllVillage(String tahasilCode);

	public List<KhatianInformation> getAllKhatian(String villageCode);

	public List<PlotInformation> getAllPlot(String khatianCode);

	public List<TenderAdvertisementDTO> viewAllTender(String title, Date startDate);

	public Integer getCount(String title);

}
