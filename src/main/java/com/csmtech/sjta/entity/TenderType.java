package com.csmtech.sjta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_tender_type", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenderType {
	@Id
	@Column(name = "tender_type_id")
	private Long tenderTypeId;

	@Column(name = "tender_type")
	private String typeOfTender;

}
