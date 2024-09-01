package com.csmtech.sjta.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.DistrictDTO;
import com.csmtech.sjta.dto.KhatianPlotDTO;
import com.csmtech.sjta.dto.TahasilDTO;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.dto.VillageInfoDTO;
import com.csmtech.sjta.entity.PlotVerification;
import com.csmtech.sjta.repository.TahasilTeamRepository;
import com.csmtech.sjta.repository.TahasilTeamUseQueryClassRepository;
import com.csmtech.sjta.service.TahasilTeamUseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TahasilTeamUseServiceImpl implements TahasilTeamUseService {

	@Value("${sjta.bcryptpassword.secretKey}")
	private String SECRET_KEY;

	Integer parentId = 0;

	@Autowired
	private TahasilTeamUseQueryClassRepository repo;

	@Autowired
	private TahasilTeamRepository tahasilRepo;

	@Override
	public List<TahasilDTO> getTahasilData(String distId) {
		List<Object[]> results = repo.getTahasilsWithStatistics(distId);
		return results.stream().map(result -> {
			TahasilDTO dto = new TahasilDTO();
			dto.setTahasilCode((String) result[0]);
			dto.setTahasilName((String) result[1]);
			dto.setTotalMouza((BigInteger) result[2]);
			dto.setTotalKatha((BigInteger) result[3]);
			dto.setTotalPlot((BigInteger) result[4]);
			dto.setTotalArea(((BigDecimal) result[5]).toString());
			dto.setExtent((String) result[6]);
			return dto;
		}).collect(Collectors.toList());

	}

	// login
	@Override
	public List<TahasilTeamUseRequestDto> checkLogin(String tahasilCode) {

		return repo.getTahasilLoginDetails(tahasilCode);
	}

	@Override
	public List<VillageInfoDTO> getKathaRecord(String tahasilcode, String villageName) {
		if (villageName != null && !villageName.equals("")) {
			List<Object[]> resultsSearch = repo.getVillageInfoForTahasilAndSearchFunction(tahasilcode, villageName);
			log.info("Sucess..Execute and return the result sucess -003");
			return resultsSearch.stream().map(searchRes -> {
				VillageInfoDTO dtoSearch = new VillageInfoDTO();
				dtoSearch.setVillageCode((String) searchRes[0]);
				dtoSearch.setVillageName((String) searchRes[1]);
				dtoSearch.setTotalKatha((BigInteger) searchRes[2]);
				dtoSearch.setTotalPlot((BigInteger) searchRes[3]);
				if (searchRes[4] == null) {
					dtoSearch.setTotalArea(((String) searchRes[4]));
				} else {
					dtoSearch.setTotalArea(((BigDecimal) searchRes[4]).toString());
				}
				dtoSearch.setExtent((String) searchRes[5]);
				dtoSearch.setTotalVillage((BigInteger) searchRes[6]);
				dtoSearch.setPsName((String) searchRes[7]);
				return dtoSearch;
			}).collect(Collectors.toList());

		}

		List<Object[]> results = repo.getVillageInfoForTahasil(tahasilcode);
		log.info("Sucess..Execute and return the result sucess -002");
		return results.stream().map(result -> {
			VillageInfoDTO dto = new VillageInfoDTO();
			dto.setVillageCode((String) result[0]);
			dto.setVillageName((String) result[1]);
			dto.setTotalKatha((BigInteger) result[2]);
			dto.setTotalPlot((BigInteger) result[3]);
			if (result[4] == null) {
				dto.setTotalArea(((String) result[4]));
			} else {
				dto.setTotalArea(((BigDecimal) result[4]).toString());
			}
			dto.setExtent((String) result[5]);
			dto.setTotalVillage((BigInteger) result[6]);
			dto.setPsName((String) result[7]);
			return dto;
		}).collect(Collectors.toList());

	}

	@Override
	public List<KhatianPlotDTO> getKathaRecordMore(String villageCode) {

		List<Object[]> results = repo.getKhatianPlotsForVillage(villageCode);
		log.info("see db send data: " + results.toString());
		List<KhatianPlotDTO> districtDTOs = results.stream().map(result -> {
			KhatianPlotDTO dto = new KhatianPlotDTO();
			dto.setKhatianCode((String) result[0]);
			dto.setKhataNo((String) result[1]);
			dto.setPlotNo((String) result[2]);
			if (result[3] == null) {
				dto.setAreaAcre((String) result[3]);
			} else {
				dto.setAreaAcre(((BigDecimal) result[3]).toString());
			}
			dto.setExtent((String) result[13]);
			dto.setVillageCode((String) result[4]);
			dto.setVillageName((String) result[5]);
			dto.setTahasilCode((String) result[6]);
			dto.setTahasilName((String) result[7]);
			dto.setDistrictCode((String) result[8]);
			dto.setDistrictName((String) result[9]);
			dto.setPlotCode((String) result[10]);
			dto.setRemark((String) result[12]);
			dto.setStatus(result[11] != null ? (Short) result[11] : 0);

			return dto;
		}).collect(Collectors.toList());
		log.info("Sucess..Execute and return the result sucess -001");
		return districtDTOs;
	}

	@Override
	public List<DistrictDTO> getDistricts() {
		log.info("Sucess..Execute and return the result");
		List<Object[]> results = repo.getDistricts();
		List<DistrictDTO> districtDTOs = results.stream().map(result -> {
			DistrictDTO dto = new DistrictDTO();
			dto.setDistrictCode((String) result[0]);
			dto.setDistrictName((String) result[1]);
			return dto;
		}).collect(Collectors.toList());
		log.info("Sucess..Execute and return the result sucess -000");
		return districtDTOs;
	}

	@Override
	public JSONObject save(String data) {
		JSONObject json = new JSONObject();
		try {
			ObjectMapper om = new ObjectMapper();
			PlotVerification plotVerfication = om.readValue(data, PlotVerification.class);

			PlotVerification saveData = tahasilRepo.save(plotVerfication);
			log.info(" Data saved : " + saveData);
			json.put("status", 200);

		} catch (Exception e) {
			log.error(e.getMessage());
			json.put("status", 400);
		}
		return json;
	}

}
