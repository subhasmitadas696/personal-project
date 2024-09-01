package com.csmtech.sjta.entity;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name="t_set_workflow")
public class TSetWorkFlow implements Serializable {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)	
	@Column(name = "workflowid")
	private Integer workflowId;
	
	@Column(name = "projectid")
	private Integer projectId;
	
	@Column(name = "serviceid")
	private Integer serviceId;
	
	@Column(name = "canvasdata",length=10485760)
	private String canvasData;
	
	@Column(name = "deletedflag")
	private Integer deletedFlag;
	
	@Column(name = "createdby")
	private Integer createdBy;
	
	@Column(name = "createdon")
	private Date createdOn;
	
	@Column(name = "tintype")
	private Integer tinType;
	
	@Column(name = "vchctrlname")
	private String vchCtrlName;
	
	@Column(name = "intlabelid")
	private Integer intLabelId;
	
	@Column(name = "vchdynfilter")
	private String vchDynFilter;
	
	@Column(name = "vchdynfilterctrlid")
	private String vchDynFilterCtrlId;
	
	@Column(name = "vchmailsmsconfigids")
	private String vchMailSmsConfigIds;
}
