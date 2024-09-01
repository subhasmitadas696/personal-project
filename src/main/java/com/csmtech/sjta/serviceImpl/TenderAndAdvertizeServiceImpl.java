package com.csmtech.sjta.serviceImpl;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.TenderAdvertisementDTO;
import com.csmtech.sjta.entity.DistrictMaster;
import com.csmtech.sjta.entity.KhatianInformation;
import com.csmtech.sjta.entity.PlotInformation;
import com.csmtech.sjta.entity.TahasilMaster;
import com.csmtech.sjta.entity.TenderAndAdvertizeEntity;
import com.csmtech.sjta.entity.TenderType;
import com.csmtech.sjta.entity.VillageMaster;
import com.csmtech.sjta.repository.DistrictMasterRepository;
import com.csmtech.sjta.repository.KhatianInformationRepository;
import com.csmtech.sjta.repository.PlotInformationRepository;
import com.csmtech.sjta.repository.TahasilMasterRepository;
import com.csmtech.sjta.repository.TenderAndAdvertizeClassRepository;
import com.csmtech.sjta.repository.TenderAndAdvertizeRepository;
import com.csmtech.sjta.repository.TenderTypeRepository;
import com.csmtech.sjta.repository.VillageMasterRepository;
import com.csmtech.sjta.service.TenderAndAdvertizeService;
import com.csmtech.sjta.util.CommonConstant;
import com.csmtech.sjta.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenderAndAdvertizeServiceImpl implements TenderAndAdvertizeService {

	@Autowired
	private TenderAndAdvertizeClassRepository repo;

	@Autowired
	private TenderAndAdvertizeRepository tenderRepo;

	@Autowired
	private TenderTypeRepository tenderTypeRepo;

	@Autowired
	private DistrictMasterRepository districtRepo;

	@Autowired
	private TahasilMasterRepository tehsilRepo;

	@Autowired
	private VillageMasterRepository villageRepo;

	@Autowired
	private KhatianInformationRepository khatianRepo;

	@Autowired
	private PlotInformationRepository plotRepo;

	@Value("${tempUpload.path}")
	private String tempUploadPath;
	@Value("${file.path}")
	private String finalUploadPath;

	BigInteger parentId;

	@Override
	public List<TenderAdvertisementDTO> getAllTender(Integer pageNumber, Integer pageSize, String title) {
		return repo.findAllByTitle(pageNumber, pageSize, title);
	}

	@Override
	public TenderAndAdvertizeEntity findByTenderAdvertisementId(BigInteger tenderAdvertisementId) {
		return repo.findByTenderAdvertisementId(tenderAdvertisementId);
	}

	@Override
	public TenderAndAdvertizeEntity updateTender(TenderAndAdvertizeEntity tender) {
		return tenderRepo.save(tender);
	}

	@Override
	public JSONObject saveRecord(String data) {

		Date currentDateTime = new Date();
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			TenderAndAdvertizeEntity tenderData = om.readValue(data, TenderAndAdvertizeEntity.class);
			List<String> fileUploadList = new ArrayList<>();
			fileUploadList.add(tenderData.getFileUploadTenderDocument());
			if (!Objects.isNull(tenderData.getTenderAdvertisementId())) {
				TenderAndAdvertizeEntity getEntity = tenderRepo
						.findByTenderAdvertisementIdAndStatus(tenderData.getTenderAdvertisementId(), false);
				getEntity.setSelTenderType(tenderData.getSelTenderType());
				getEntity.setTxtTitle(tenderData.getTxtTitle());
				getEntity.setLetterNo(tenderData.getLetterNo());
				getEntity.setTxtStartDate(tenderData.getTxtStartDate());
				getEntity.setTxtCloseDate(tenderData.getTxtCloseDate());
				getEntity.setFileUploadTenderDocument(tenderData.getFileUploadTenderDocument());
				getEntity.setSelDistrict(tenderData.getSelDistrict());
				getEntity.setSelTehsil(tenderData.getSelTehsil());
				getEntity.setSelMouza(tenderData.getSelMouza());
				getEntity.setSelKhataNo(tenderData.getSelKhataNo());
				getEntity.setSelPlotNo(tenderData.getSelPlotNo());

				getEntity.setUpdatedBy(tenderData.getCreatedBy());
				getEntity.setUpdatedOn(currentDateTime);
				TenderAndAdvertizeEntity updateData = tenderRepo.save(getEntity);
				parentId = updateData.getTenderAdvertisementId();
				json.put(CommonConstant.STATUS_KEY, 202);
			} else {
				tenderData.setCreatedBy(tenderData.getCreatedBy());
				TenderAndAdvertizeEntity saveData = tenderRepo.save(tenderData);
				parentId = saveData.getTenderAdvertisementId();
				json.put(CommonConstant.STATUS_KEY, 200);
			}
			json.put("app_id", parentId);
			for (String fileUpload : fileUploadList) {
				if (fileUpload != null && (!fileUpload.equals(""))) {
					File f = new File(tempUploadPath + fileUpload);
					if (f.exists()) {

						Path srcPath = Paths.get(tempUploadPath + fileUpload);
						Path destPath = Paths.get(finalUploadPath + "/" + fileUpload);

						CommonUtil.copyAndDeleteFile(srcPath, destPath);
					}

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			json.put(CommonConstant.STATUS_KEY, 400);
		}
		return json;

	}

	@Override
	public List<TenderType> getAllTenderType() {
		return tenderTypeRepo.findAll();
	}

	@Override
	public Integer deleteTender(Integer createdBy, BigInteger tenderAdvertisementId) {
		return repo.deleteRecord(createdBy, tenderAdvertisementId);
	}

	@Override
	public List<DistrictMaster> getAllDistrict() {
		return districtRepo.findAllOrderByTxtDistrictName();
	}

	@Override
	public List<TahasilMaster> getAllTehsil(String txtDistrictCode) {
		return tehsilRepo.findAllByTxtDistrictCodeOrderByTahasilName(txtDistrictCode);
	}

	@Override
	public List<VillageMaster> getAllVillage(String tahasilCode) {
		return villageRepo.findAllByTahasilCodeOrderByVillageName(tahasilCode);
	}

	@Override
	public List<KhatianInformation> getAllKhatian(String villageCode) {
		return khatianRepo.findAllByVillageCode(villageCode);
	}

	@Override
	public List<PlotInformation> getAllPlot(String khatianCode) {
		return plotRepo.findAllByKhatianCode(khatianCode);
	}

	@Override
	public List<TenderAdvertisementDTO> viewAllTender(String title, Date startDate) {
		return repo.findAllByTitleStatusFalse(title, startDate);
	}

	@Override
	public Integer getCount(String title) {
		return repo.getCount(title);
	}

}
