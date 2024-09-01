package com.csmtech.sjta.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KhatianInformationDTO {

	private String khatianCode;

	private String khataNo;

	private String ownerName;

	private String marfatdarName;

	private String sotwa;

	private Date publicationDate;

	private String villageCode;
}
