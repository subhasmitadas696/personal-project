package com.csmtech.sjta.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "land_allotement", schema = "application")
@Entity
public class LandAllotementEntity {
	
	
	@Id	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "land_allotement_id", nullable = false, unique = true)
    private BigInteger landAllotmentId;

    @Column(name = "created_by")
    private BigInteger createdBy;


//    @Column(name = "updated_by")
//    @UpdateTimestamp
//    private BigInteger updatedBy;
//
//    @Column(name = "updated_on")
//    private Date updatedOn;

    @Column(name = "meeting_schedule_id")
    private BigInteger meetingScheduleId;

    @Column(name = "deleted_flag", insertable = false)
    private Boolean deletedFlag = false;

    @Column(name = "land_application_id")
    private BigInteger applicantName;

    @Column(name = "plot_code")
    private String selPlotNo;

    @Column(name = "total_area")
    private BigDecimal totalArea;

    @Column(name = "purchase_area")
    private BigDecimal purchaseArea;

    @Column(name = "price_per_acer")
    private BigDecimal piceInPerAcer;

    @Column(name = "total_price_in_purchase_area")
    private BigDecimal totalPriceInPurchaseArea;
    
    @Column(name = "land_allotement_flag")
    private Short landAlloteFlag;
    
    @Column(name = "form_16_docs")
    private String fileDocument;
    
    @Column(name = "land_order_number")
    private String orderNumber;
    
    @Column(name = "land_order_date")
    private Date  orderDate;

    @Transient
    private Integer flagStatus;
    
    @Column(name = "owner_details")
    private String ownerdetails;
    

}
