package com.csmtech.sjta.service;

import java.util.List;

import com.csmtech.sjta.dto.PlotValuationDTO;
import com.csmtech.sjta.mobile.dto.LandVerificationResponseDto;
import com.csmtech.sjta.mobile.entity.LandUseEntity;

public interface PlotValuationService {

	public LandVerificationResponseDto landPlotValuation(PlotValuationDTO landDto);



}
