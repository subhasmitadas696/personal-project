package com.csmtech.sjta.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "m_approval_action")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalActionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "approval_action_id")
	private Short approvalActionId;

	@Column(name = "approval_action")
	private String approvalAction;
}
