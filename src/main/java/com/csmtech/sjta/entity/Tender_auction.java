package com.csmtech.sjta.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Table(name = "tender_auction", schema = "application")
@Entity
public class Tender_auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tender_auction_id")
	private BigInteger intId;

	@Column(name = "`auction_name`")
	private String txtAuctionName20;

	@Column(name = "created_by")
	private Integer intCreatedBy;

	@Column(name = "updated_by")
	private Integer intUpdatedBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	@Column(name = "created_on")
	@CreationTimestamp
	private Date dtmCreatedOn;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	@Column(name = "updated_on")
	private Date stmUpdatedOn;

	@Column(name = "deleted_flag", insertable = false)
	private Boolean bitDeletedFlag = false;

	@Column(name = "`auction_plot_id`")
	private BigInteger selSelectSource21;
	@Transient
	private String selSelectSource21Val;
	@Column(name = "`lease_period_years`")
	private Integer txtLeasePeriodinYears22;
	@Column(name = "`committee_member_name`")
	private String selCommitteeMemberName23;
	@Transient
	private String selCommitteeMemberName23Val;

	@Column(name = "`royality`")
	private BigDecimal txtRoyalityBasePriceofMineral26;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`form_m_submit_start_date`")
//	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private java.util.Date txtFormMSubmissionStartDate27;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`form_m_submit_end_date`")
	private java.util.Date txtFormMSubmissionEndDate28;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`secuirity_deposit_start_date`")
	private java.util.Date txtSecurityDepositStartDate29;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`secuirity_deposit_end_date`")
	private java.util.Date txtSecurityDepositEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`bid_document_downlode_start_date`")
	private java.util.Date txtBidDocumentDownloadStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`bid_document_downlode_end_date`")
	private java.util.Date txtBidDocumentDownloadEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`date_of_techinical_evaluation`")
	private java.util.Date txtDateOfTechnicalEvaluation;

	@Column(name = "`application_fee_not_refund`")
	private BigDecimal txtApplicationFeeNonRefundable;
	@Column(name = "`secutity_amount_deposite`")
	private BigDecimal txtSecurityAmount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`slot_for_auction_from_date`")
	private java.util.Date txtSlotsForAuctionFromDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "`slot_for_auction_to_date`")
	private java.util.Date txtSlotsForAuctionToDate;
	
	@Transient
	private Integer hidebidDocsAuctionFlag;
	
	@Column(name = "plot_code")
	private String plotCode;


	

}