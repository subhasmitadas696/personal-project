package com.csmtech.sjta.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "approval_configration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalConfigurationEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "approval_configration_id")
	private Short approvalConfigurationId;

	@Column(name = "approval_type")
	private String approvalType;

	@Transient
	private String roleName;

	@Column(name = "role_id")
	private Short roleId;

	@Column(name = "approval_action_ids")
	private String approvalActionIds;
}
