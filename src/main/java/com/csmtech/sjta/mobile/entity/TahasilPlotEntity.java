package com.csmtech.sjta.mobile.entity;

import java.time.OffsetDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tahasil_plot_survey")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TahasilPlotEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plot_survey_id")
	private Integer plotSurveyId;

	@Column(name = "tahasil_code")
	private String tahasilCode;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "plot_no")
	private String plotNo;

	@Column(name = "survey_date")
	private Date surveyDate;
	
	@Column(name= "village_code")
	private String villageCode;
	
	@Column(name ="khatian_code")
	private String khatianCode;
	
	@Column(name = "plot_code")
	private String plotCode;
	

	public TahasilPlotEntity(String tahasilCode, String remarks, String plotNo, Date surveyDate) {
		this.tahasilCode = tahasilCode;
		this.remarks = remarks;
		this.plotNo = plotNo;
		this.surveyDate = surveyDate;
	}

	public TahasilPlotEntity(String tahasilCode, String remarks, String plotNo, Date surveyDate,
			String villageCode, String khatianCode) {
		super();
		this.tahasilCode = tahasilCode;
		this.remarks = remarks;
		this.plotNo = plotNo;
		this.surveyDate = surveyDate;
		this.villageCode = villageCode;
		this.khatianCode = khatianCode;
	}
	
	
	

}
