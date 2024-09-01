package com.csmtech.sjta.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LandAppResponseStructureDTO {

	private LandApplicantViewDTO appdto;
	private List<LandPlotViewDTO> plotto;
	private List<LandAppViewDocumentDTO> docsdto;

	private String villageCodeextend;
	private String villageMergeExtend;

	public LandAppResponseStructureDTO(LandApplicantViewDTO appdto, List<LandPlotViewDTO> plotto,
			List<LandAppViewDocumentDTO> docsdto) {
		super();
		this.appdto = appdto;
		this.plotto = plotto;
		this.docsdto = docsdto;
	}

}
