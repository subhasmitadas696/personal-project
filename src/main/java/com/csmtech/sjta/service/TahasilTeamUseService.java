package com.csmtech.sjta.service;

import java.util.List;

import org.json.JSONObject;

import com.csmtech.sjta.dto.DistrictDTO;
import com.csmtech.sjta.dto.KhatianPlotDTO;
import com.csmtech.sjta.dto.TahasilTeamUseRequestDto;
import com.csmtech.sjta.dto.TahasilDTO;
import com.csmtech.sjta.dto.VillageInfoDTO;

public interface TahasilTeamUseService {
	
	public List<DistrictDTO> getDistricts();
	
	public List<TahasilDTO> getTahasilData(String distId);
	
	public List<TahasilTeamUseRequestDto> checkLogin(String tahasilCode);

	public  List<VillageInfoDTO> getKathaRecord(String tahasilcode,String villaegName);
	
	public List<KhatianPlotDTO> getKathaRecordMore(String villageCode);

	public JSONObject save(String plotVerfication);
	


}
