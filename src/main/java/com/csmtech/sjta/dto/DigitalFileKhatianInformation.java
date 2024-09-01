package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalFileKhatianInformation {

	String fileDocument;
	String khatianCode;
	String villageCode;
	String tahasilCode;
	Integer fileSize;
}
