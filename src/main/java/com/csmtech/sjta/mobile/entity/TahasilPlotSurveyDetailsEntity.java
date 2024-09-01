package com.csmtech.sjta.mobile.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Entity
@Table(name = "tahasil_plot_survey_details")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TahasilPlotSurveyDetailsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plot_survey_details_id")
	private Integer plotSurveyDetailsId;
	
	@Column(name ="plot_survey_id")
	private Integer plotSurveyId;
	
	@Column(name ="land_type_status_id")
	private Integer landTypeStatusId;
	
	@Column(name ="land_use_id")
	private Integer landUseId;
	
	@Column(name= "build_up_type_name")
	private String buildUpTypeName;
	
	@Column(name = "latitude")
	private String latitude;
	
	@Column(name = "longitude")
	private String longitude;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date_time")
	private Date createdDateTime;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date_time")
	private Date updatedDateTime;
	
	@Column(name = "image")
	private String image;
	
}
