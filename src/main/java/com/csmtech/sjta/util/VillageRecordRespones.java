package com.csmtech.sjta.util;

import java.util.List;
import com.csmtech.sjta.dto.VillageInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VillageRecordRespones {

	private Integer status;
	private List<VillageInfoDTO> result;

}
