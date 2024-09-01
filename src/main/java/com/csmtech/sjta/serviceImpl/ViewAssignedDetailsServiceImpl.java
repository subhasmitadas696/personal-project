/**
 * @author prasanta.sethi
 */
package com.csmtech.sjta.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.mobile.dto.PlotLandInspectionDto;
import com.csmtech.sjta.repository.ViewAssignedDetailsRepository;
import com.csmtech.sjta.service.ViewAssignedDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author prasanta.sethi
 */
@Slf4j
@Service
public class ViewAssignedDetailsServiceImpl implements ViewAssignedDetailsService {
	@Autowired
	ViewAssignedDetailsRepository repository;

	@Override
	public List<PlotLandInspectionDto> getAssignedDetails() {

		List<Object[]> result = repository.getAssignedDetails();
		List<PlotLandInspectionDto> response = new ArrayList<>();
		log.info("inside getAssignedDetailsServiceImpl !!!");

		for (Object[] row : result) {
			PlotLandInspectionDto dto = new PlotLandInspectionDto();
			dto.setPlotLandInspectionId((Integer) row[0]);
			dto.setDistrictCode((String) row[1]);
			dto.setTahasilCode((String) row[2]);
			dto.setVillageCode((String) row[3]);
			dto.setKhatianCode((String) row[4]);
			dto.setPlotCode((String) row[5]);
			dto.setCoRemarks(row[6] != null ? (String) row[6].toString() : "");
			dto.setCoUploadedPhoto((String) row[7]);
			dto.setInspectionDate(row[8] != null ? (String) row[8].toString() : "");
			dto.setCreatedDate(row[9] != null ? (String) row[9].toString() : "");
			dto.setScheduledInspectionDate(row[10] != null ? (String) row[10].toString() : "");
			dto.setPlotNumber((String) row[11]);
			dto.setKhataNo((String) row[12]);
			dto.setDistrictName((String) row[13]);
			dto.setTahasilName((String) row[14]);
			dto.setVillageName((String) row[15]);
			dto.setAreaAcre(row[16] != null ? (String) row[16].toString() : "");
			dto.setTahasilRemarks(row[17] != null ? (String) row[17].toString() : "");
			dto.setTahasilUploadedPhoto((String) row[18]);
			response.add(dto);
		}

		return response;
	}
}
