package com.csmtech.sjta.serviceImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmtech.sjta.dto.DistrictCode;
import com.csmtech.sjta.repository.BannerDataRepository;
import com.csmtech.sjta.service.BannerDataService;

@Service
public class BannerDataServiceImpl implements BannerDataService {

	@Autowired
	private BannerDataRepository bannerDataRepository;

	@Override
	public List<DistrictCode> getDistrictCodes() {
		List<Object[]> resultList = bannerDataRepository.getDistrictCodes();
		List<DistrictCode> bannerData = new ArrayList<>();
		for (Object[] result : resultList) {
			String districtCode = (String) result[0];
			BigInteger tahasil =(BigInteger) result[1];
			BigInteger mouza = (BigInteger) result[2];
			BigInteger plotNumber = (BigInteger) result[3];
			String area = ((BigDecimal) result[4]).toString();
			
			DistrictCode bannerdataInfo = new DistrictCode();
			bannerdataInfo.setDistrictCode(districtCode);
			bannerdataInfo.setPlot(plotNumber);
			bannerdataInfo.setMouza(mouza);
			bannerdataInfo.setTahasil(tahasil);
			bannerdataInfo.setArea(area);
			bannerData.add(bannerdataInfo);
		}

		return bannerData;

	}

}
