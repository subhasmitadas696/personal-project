package com.csmtech.sjta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMoreUploadDocumentsDTO {

	Short amtxtDocumentName;
	String amfileDocument;
	String documentNumber;

}
