package com.csmtech.sjta.entity;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "land_allotement_for_auction",schema = "application")
@Entity
public class LandAllotementForAuctionEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "land_allotement_for_auction_id")
    private BigInteger landAllotementForAuctionId;

    @Column(name = "plot_no", length = 48)
    private String selPlotNo;

    @Column(name = "created_by")
    private BigInteger createdBy;

    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "update_by")
    private BigInteger updateBy;

    @Column(name = "update_on")
    @UpdateTimestamp
    private Date updateOn;

    @Column(name = "deleted_flag", insertable = false)
    private Boolean deletedFlag;
}
