package com.csmtech.sjta.repository;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.LandApplicantMeetingDTO;
import com.csmtech.sjta.dto.MeetingRemoveApplicantDto;
import com.csmtech.sjta.dto.MeetingScheduleDTO;
import com.csmtech.sjta.entity.MeetingSchedule;
import com.csmtech.sjta.util.MailUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MeetingScheduleNativeRepository {

	/**
	 * @author guru.prasad and @author rashmi.jena
	 */

	private static final Logger logger = LoggerFactory.getLogger(MeetingScheduleNativeRepository.class);

	@PersistenceContext
	@Autowired
	EntityManager entityManager;

	@Autowired
	private MailUtil mailUtil;

	String subject = " Meeting Created Successfully ";

	@Transactional
	public Integer updateRecord(MeetingSchedule meeting_schedule) {
		try {
			Date currentDate = new Date();

			String updateAuctionLandApplicant = "";
			if (meeting_schedule.getAuctionEditFlag() == 3) {
				updateAuctionLandApplicant = "bidder_form_m_application_id";
			} else {
				updateAuctionLandApplicant = " land_application_id ";
			}

			String updatemeetingscheduleQuery = "UPDATE application.meeting_schedule SET "
					+ "meeting_date = :meetingDate, meeting_purpose =:meetingPurpose, "
					+ "meeting_level_id =:meetingLevelId, venue =:venue, updated_by =:updatedBy, "
					+ "updated_on =:updatedOn WHERE meeting_schedule_id = :meetingScheduleId";

//			String updatemeetingapplicantQuery = "UPDATE application.meeting_schedule_applicant SET "
//					+ "status = CAST(:setstatus AS BIT), updated_by =:applicantupdatedBy, "
//					+ "updated_on =:appupdatedOn WHERE meeting_schedule_id = :meetingScheduleId";

			String updatemeetingccQuery = "UPDATE application.meeting_schedule_mail_cc SET "
					+ "status = CAST(:setstatus AS BIT), updated_by =:ccupdatedBy, updated_on =:ccupdatedOn "
					+ "WHERE meeting_schedule_id = :meetingScheduleId";

			String updatemeetingbccQuery = "UPDATE application.meeting_schedule_mail_bcc SET "
					+ "status = CAST(:setstatus AS BIT), updated_by =:bccupdatedBy, "
					+ "updated_on =:bccupdatedOn WHERE meeting_schedule_id = :meetingScheduleId";

//			String insertMeetingScheduleApplicantQuery = "INSERT INTO application.meeting_schedule_applicant (meeting_schedule_id, "
//					+ updateAuctionLandApplicant + " , created_by, updated_by, updated_on,plot_no)"
//					+ " VALUES (:meetingScheduleId, :landApplicationId, :applicantCreatedBy, :applicantupdatedBy, :appupdatedOn,:plotNo)";

			String insertMeetingScheduleMailCCQuery = "INSERT INTO application.meeting_schedule_mail_cc (meeting_schedule_id, user_id, created_by, updated_by, updated_on)"
					+ " VALUES (:meetingScheduleId, :ccUserId, :ccCreatedBy, :ccupdatedby, :ccupdatedon)";

			String insertMeetingScheduleMailBCCQuery = "INSERT INTO application.meeting_schedule_mail_bcc (meeting_schedule_id, user_id, created_by, updated_by, updated_on)"
					+ " VALUES (:meetingScheduleId, :bccUserId, :bccCreatedBy, :bccupdatedby, :bccupdatedon)";

			String updateLandApplication = " UPDATE public.land_schedule " + " SET meeting_schedule_flag = '0' "
					+ " WHERE land_application_id = :landAppId AND deleted_flag = '0' AND plot_code = :plotCode ";

			String updateNewApplicantPlots = " UPDATE public.land_schedule " + " SET meeting_schedule_flag = '1' "
					+ " WHERE land_application_id = :landAppId AND deleted_flag = '0' AND plot_code = :plotCode ";

			List<BigInteger> selEdit = Arrays.asList(meeting_schedule.getApplicantRemovedIds());
			if (!selEdit.isEmpty()) {
				log.info("update execution Start ....!!");
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 0; i < selEdit.size(); i++) {
					stringBuilder.append(selEdit.get(i));
					if (i < selEdit.size() - 1) {
						stringBuilder.append(",");
					}
				}
				String updateApplicantFlagZero = " UPDATE public.land_application " + " SET meeting_shedule_flag = '0' "
						+ " WHERE land_application_id IN (" + stringBuilder + ") AND deleted_flag = '0' ";
				Query query = entityManager.createNativeQuery(updateApplicantFlagZero);
				query.executeUpdate();
				log.info("update success....!!");
			} else {
				log.info("Record Are Zero....!!");
			}

			Integer meetingScheduleIdInteger = meeting_schedule.getIntId();

			List<Integer> selectedLandApplicationIds = Arrays.asList(meeting_schedule.getSelectedLandApplicationId());
			List<Integer> selectedCCOfficers = Arrays.asList(meeting_schedule.getSelectedCCOfficers());
			List<Integer> selectedBCCOfficers = Arrays.asList(meeting_schedule.getSelectedBCCOfficers());

			entityManager.createNativeQuery(updatemeetingscheduleQuery)
					.setParameter("meetingDate", meeting_schedule.getTxtMeetingDate53())
					.setParameter("meetingPurpose", meeting_schedule.getTxtrMeetingPurpose54())
					.setParameter("meetingLevelId", meeting_schedule.getSelMeetingLevel55())
					.setParameter("venue", meeting_schedule.getVenue())
					.setParameter("updatedBy", meeting_schedule.getIntUpdatedBy().longValue())
					.setParameter("updatedOn", currentDate)
					.setParameter("meetingScheduleId", meeting_schedule.getIntId()).executeUpdate();

			String findApplicantEmailQuery = "SELECT email_address from land_application where land_application_id IN (:selectedLandApplicationIds) ";

			String landIdQuery = "select land_application_id from application.meeting_schedule_applicant where meeting_schedule_id = :meetingScheduleIdInteger ";

			@SuppressWarnings("unchecked")
			List<Integer> dblandIds = entityManager.createNativeQuery(landIdQuery)
					.setParameter("meetingScheduleIdInteger", meetingScheduleIdInteger).getResultList();
			logger.info("landIds : " + dblandIds);
			@SuppressWarnings("unchecked")
			List<String> applicantEmail = entityManager.createNativeQuery(findApplicantEmailQuery)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds).getResultList();

			logger.info("appEmail" + applicantEmail);

//			if(meeting_schedule.getAuctionEditFlag()!=3) {
//				entityManager.createNativeQuery(updatemeetingapplicantQuery).setParameter("setstatus", 1)
//				.setParameter("applicantupdatedBy", meeting_schedule.getIntUpdatedBy().longValue())
//				.setParameter("appupdatedOn", currentDate)
//				.setParameter("meetingScheduleId", meetingScheduleIdInteger).executeUpdate();
//			}

//			List<String> selectedPlotsIds = Arrays.asList(meeting_schedule.getPlotsNos());
//			Integer size = Math.min(selectedLandApplicationIds.size(), selectedPlotsIds.size());
//			for (int i = 0; i < size; i++) {
//				entityManager.createNativeQuery(insertMeetingScheduleApplicantQuery)
//						.setParameter("meetingScheduleId", meetingScheduleIdInteger)
//						.setParameter("landApplicationId", selectedLandApplicationIds.get(i))
//						.setParameter("applicantCreatedBy", meeting_schedule.getIntCreatedBy().longValue())
//						.setParameter("applicantupdatedBy", meeting_schedule.getIntUpdatedBy().longValue())
//						.setParameter("plotNo", selectedPlotsIds.get(i)).setParameter("appupdatedOn", currentDate)
//						.executeUpdate();
//				entityManager.createNativeQuery(updateNewApplicantPlots)
//						.setParameter("landAppId", selectedLandApplicationIds.get(i))
//						.setParameter("plotCode", selectedPlotsIds.get(i)).executeUpdate();
//
//			}

			// update the plot_code in update case
			List<MeetingRemoveApplicantDto> getRemoveApplicant = meeting_schedule.getRemovedApplicants();
			for (MeetingRemoveApplicantDto dto : getRemoveApplicant) {
//				 Query query = entityManager.createNativeQuery(" SELECT plot_code FROM land_bank.plot_information WHERE plot_no = :plotNo ");
//				 query.setParameter("plotNo",dto.getPlotNo());
//				 Object result = query.getSingleResult();
//				 String plotCode = (result != null) ? result.toString() : null;
//				 if(plotCode!=null) {
				entityManager.createNativeQuery(updateLandApplication).setParameter("landAppId", dto.getId())
						.setParameter("plotCode", dto.getPlotNo()).executeUpdate();

			}

			if (!selectedCCOfficers.isEmpty()) {

				entityManager.createNativeQuery(updatemeetingccQuery).setParameter("setstatus", 1)
						.setParameter("ccupdatedBy", meeting_schedule.getIntUpdatedBy().longValue())
						.setParameter("ccupdatedOn", currentDate)
						.setParameter("meetingScheduleId", meetingScheduleIdInteger).executeUpdate();

				String ccofficerIdQuery = "select user_id from application.meeting_schedule_mail_cc where meeting_schedule_id = :meetingScheduleIdInteger ";

				@SuppressWarnings("unchecked")
				List<Integer> dbccofficerIds = entityManager.createNativeQuery(ccofficerIdQuery)
						.setParameter("meetingScheduleIdInteger", meetingScheduleIdInteger).getResultList();
				logger.info("ccIds : " + dbccofficerIds);

				String findOfficerCCEmailQuery = "SELECT email_id from user_details where user_id IN (:selectedCCOfficers) ";
				@SuppressWarnings("unchecked")
				List<String> officerCCEmail = entityManager.createNativeQuery(findOfficerCCEmailQuery)
						.setParameter("selectedCCOfficers", selectedCCOfficers).getResultList();
				logger.info("ccEmail" + officerCCEmail);

				for (int i = 0; i < selectedCCOfficers.size(); i++) {
					entityManager.createNativeQuery(insertMeetingScheduleMailCCQuery)
							.setParameter("meetingScheduleId", meetingScheduleIdInteger)
							.setParameter("ccUserId", selectedCCOfficers.get(i))
							.setParameter("ccCreatedBy", meeting_schedule.getCcCreatedBy().longValue())
							.setParameter("ccupdatedby", meeting_schedule.getCcCreatedBy().longValue())
							.setParameter("ccupdatedon", currentDate).executeUpdate();

				}
			}

			if (!selectedBCCOfficers.isEmpty()) {

				entityManager.createNativeQuery(updatemeetingbccQuery).setParameter("setstatus", 1)
						.setParameter("bccupdatedBy", meeting_schedule.getIntUpdatedBy().longValue())
						.setParameter("bccupdatedOn", currentDate)
						.setParameter("meetingScheduleId", meetingScheduleIdInteger).executeUpdate();

				String bccofficerIdQuery = "select user_id from application.meeting_schedule_mail_bcc where meeting_schedule_id = :meetingScheduleIdInteger ";

				@SuppressWarnings("unchecked")
				List<Integer> dbbccofficerIds = entityManager.createNativeQuery(bccofficerIdQuery)
						.setParameter("meetingScheduleIdInteger", meetingScheduleIdInteger).getResultList();
				logger.info("bccIds : " + dbbccofficerIds);

				String findOfficerBCCEmailQuery = "SELECT email_id from user_details where user_id IN (:selectedBCCOfficers) ";
				@SuppressWarnings("unchecked")
				List<String> officerBCCEmail = entityManager.createNativeQuery(findOfficerBCCEmailQuery)
						.setParameter("selectedBCCOfficers", selectedBCCOfficers).getResultList();
				logger.info("bccEmail" + officerBCCEmail);

				for (int i = 0; i < selectedBCCOfficers.size(); i++) {

					entityManager.createNativeQuery(insertMeetingScheduleMailBCCQuery)
							.setParameter("meetingScheduleId", meetingScheduleIdInteger)
							.setParameter("bccUserId", selectedBCCOfficers.get(i))
							.setParameter("bccCreatedBy", meeting_schedule.getBccCreatedBy().longValue())
							.setParameter("bccupdatedby", meeting_schedule.getBccCreatedBy().longValue())
							.setParameter("bccupdatedon", currentDate).executeUpdate();

				}
			}

			return meetingScheduleIdInteger;

		} catch (Exception e) {
			logger.error("Error in updateRecord: " + e.getMessage(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	public Integer saveRecord(MeetingSchedule meeting_schedule) {

		try {

			String getMeetingLevelOfLandApplicant = "SELECT CAST(meeting_level_id AS INTEGER) as meeting_level_id from application.meeting_schedule "
					+ " JOIN application.meeting_schedule_applicant using(meeting_schedule_id) "
					+ " WHERE land_application_id in (:selectedLandApplicationIds) ";

			String matchMeetingLevelWithLandApplicant = "SELECT COUNT(*) from application.meeting_schedule "
					+ " JOIN application.meeting_schedule_applicant using(meeting_schedule_id) "
					+ " WHERE land_application_id in (:selectedLandApplicationIds) and meeting_level_id = :meetingLevelId ";

			String checkAuctionLandApplicant = "SELECT COUNT(*) from application.meeting_schedule "
					+ " JOIN application.meeting_schedule_applicant using(meeting_schedule_id) "
					+ " WHERE land_application_id in (:selectedLandApplicationIds) and go_for_auction = 1 ";

			String insertMeetingSqlReturnId = "INSERT INTO application.meetings (created_by, created_on) VALUES (:createdBy, CURRENT_TIMESTAMP)\r\n"
					+ "RETURNING meeting_id ";

			String insertMeetingScheduleQuery = "INSERT INTO application.meeting_schedule (meeting_date, meeting_purpose, meeting_level_id, venue, created_by,meeting_id)"
					+ " VALUES (:meetingDate, :meetingPurpose, :meetingLevelId, :venue, :createdBy,:meetingId)"
					+ " RETURNING meeting_schedule_id";

			String insertMeetingScheduleApplicantQuery = "";
			String insertMeetingScheduleApplicantAuctionQuery = "";
			String createMeetingForAuctionFlagQuery = "";
			if (meeting_schedule.getChangeTabFlag() == 2) {
				insertMeetingScheduleApplicantAuctionQuery = "INSERT INTO application.meeting_schedule_applicant (meeting_schedule_id, bidder_form_m_application_id, "
						+ " created_by,plot_no,metting_auction_status)"
						+ " VALUES (:meetingScheduleId, :landApplicationId, :applicantCreatedBy , :plotsno,1)";
				createMeetingForAuctionFlagQuery = "update application.auction_plot_details  set create_meeting_for_auction_flag=1  where  plot_code=:plotCode ";

			} else {
				insertMeetingScheduleApplicantQuery = "INSERT INTO application.meeting_schedule_applicant (meeting_schedule_id, land_application_id, created_by,plot_no)"
						+ " VALUES (:meetingScheduleId, :landApplicationId, :applicantCreatedBy , :plotsno)";
			}

			String insertMeetingScheduleMailCCQuery = "INSERT INTO application.meeting_schedule_mail_cc (meeting_schedule_id, user_id, created_by)"
					+ " VALUES (:meetingScheduleId, :ccUserId, :ccCreatedBy)";

			String insertMeetingScheduleMailBCCQuery = "INSERT INTO application.meeting_schedule_mail_bcc (meeting_schedule_id, user_id, created_by)"
					+ " VALUES (:meetingScheduleId, :bccUserId, :bccCreatedBy)";

			String updateLandApplication = " UPDATE public.land_schedule " + " SET meeting_schedule_flag = '1' "
					+ " WHERE land_application_id = :landAppId AND deleted_flag = '0' AND plot_code = :plotCode ";

			int batchSize = 50;
			int rowsAffected = 0;

			List<Integer> selectedLandApplicationIds = Arrays.asList(meeting_schedule.getSelectedLandApplicationId());
			List<String> selectedPlotsIds = Arrays.asList(meeting_schedule.getPlotsNos());
			List<Integer> selectedCCOfficers = Arrays.asList(meeting_schedule.getSelectedCCOfficers());
			List<Integer> selectedBCCOfficers = Arrays.asList(meeting_schedule.getSelectedBCCOfficers());
			Integer meetingScheduleIdInteger = null;
			BigInteger countOfMeetingLevel = (BigInteger) entityManager
					.createNativeQuery(matchMeetingLevelWithLandApplicant)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds)
					.setParameter("meetingLevelId", meeting_schedule.getSelMeetingLevel55()).getSingleResult();
			log.info("countOfMeetingLevel " + countOfMeetingLevel);

			BigInteger countOfAuction = (BigInteger) entityManager.createNativeQuery(checkAuctionLandApplicant)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds).getSingleResult();

			log.info("countOfAuction " + countOfAuction);

			@SuppressWarnings("unchecked")
			List<Integer> getMeetingLevel = entityManager.createNativeQuery(getMeetingLevelOfLandApplicant)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds).getResultList();

			log.info("getMeetingLevel" + getMeetingLevel);

			BigInteger meetingId = (BigInteger) entityManager.createNativeQuery(insertMeetingSqlReturnId)
					.setParameter("createdBy", meeting_schedule.getIntCreatedBy().longValue()).getSingleResult();
			BigInteger meetingScheduleId = BigInteger.valueOf(0);
			if (meetingId != null) {
				meetingScheduleId = (BigInteger) entityManager.createNativeQuery(insertMeetingScheduleQuery)
						.setParameter("meetingDate", meeting_schedule.getTxtMeetingDate53())
						.setParameter("meetingPurpose", meeting_schedule.getTxtrMeetingPurpose54())
						.setParameter("meetingLevelId", meeting_schedule.getSelMeetingLevel55())
						.setParameter("venue", meeting_schedule.getVenue()).setParameter("meetingId", meetingId)
						.setParameter("createdBy", meeting_schedule.getIntCreatedBy().longValue()).getSingleResult();
			}
			meetingScheduleIdInteger = meetingScheduleId.intValue();

			String findApplicantEmailQuery = "SELECT email_address from land_application where land_application_id IN (:selectedLandApplicationIds) ";

			@SuppressWarnings("unchecked")
			List<String> applicantEmail = entityManager.createNativeQuery(findApplicantEmailQuery)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds).getResultList();

			logger.info("appEmail" + applicantEmail);

			List<String> officerUserCCEmail = null;
			List<String> officerUserBCCEmail = null;
			Set<String> processedCombinations = new HashSet<>();
			String plotsloopNo = null;
			for (String plotsNo : selectedPlotsIds) {
				plotsloopNo = plotsNo;
			}

			int size = Math.min(selectedLandApplicationIds.size(), selectedPlotsIds.size());
			if (meeting_schedule.getChangeTabFlag() == 2) {
				for (int i = 0; i < size; i++) {
					Integer coutn = entityManager.createNativeQuery(insertMeetingScheduleApplicantAuctionQuery)
							.setParameter("meetingScheduleId", meetingScheduleIdInteger)
							.setParameter("landApplicationId", selectedLandApplicationIds.get(i))
							.setParameter("applicantCreatedBy", meeting_schedule.getIntCreatedBy().longValue())
							.setParameter("plotsno", selectedPlotsIds.get(i)).executeUpdate();
					if (coutn > 0) {
						Integer plotCount = entityManager.createNativeQuery(createMeetingForAuctionFlagQuery)
								.setParameter("plotCode", selectedPlotsIds.get(i)).executeUpdate();
						log.info("update plots are " + plotCount);
					}

				}
			} else {
				for (int i = 0; i < size; i++) {
					entityManager.createNativeQuery(insertMeetingScheduleApplicantQuery)
							.setParameter("meetingScheduleId", meetingScheduleIdInteger)
							.setParameter("landApplicationId", selectedLandApplicationIds.get(i))
							.setParameter("applicantCreatedBy", meeting_schedule.getIntCreatedBy().longValue())
							.setParameter("plotsno", selectedPlotsIds.get(i)).executeUpdate();
//					 Query query = entityManager.createNativeQuery(" SELECT plot_code FROM land_bank.plot_information WHERE plot_no = :plotNo ");
//					 query.setParameter("plotNo", selectedPlotsIds.get(i));
//					 Object result = query.getSingleResult();
//					 String plotCode = (result != null) ? result.toString() : null;
					entityManager.createNativeQuery(updateLandApplication)
							.setParameter("landAppId", selectedLandApplicationIds.get(i))
							.setParameter("plotCode", selectedPlotsIds.get(i)).executeUpdate();

				}
			}
			if (!selectedCCOfficers.isEmpty()) {
				String findOfficerCCEmailQuery = "SELECT email_id from user_details where user_id IN (:selectedCCOfficers) ";
				@SuppressWarnings("unchecked")
				List<String> officerCCEmail = entityManager.createNativeQuery(findOfficerCCEmailQuery)
						.setParameter("selectedCCOfficers", selectedCCOfficers).getResultList();
				logger.info("ccEmail" + officerCCEmail);
				officerUserCCEmail = officerCCEmail;
				for (int i = 0; i < selectedCCOfficers.size(); i++) {

					entityManager.createNativeQuery(insertMeetingScheduleMailCCQuery)
							.setParameter("meetingScheduleId", meetingScheduleIdInteger)
							.setParameter("ccUserId", selectedCCOfficers.get(i))
							.setParameter("ccCreatedBy", meeting_schedule.getCcCreatedBy().longValue()).executeUpdate();

				}
			}

			if (!selectedBCCOfficers.isEmpty()) {
				String findOfficerBCCEmailQuery = "SELECT email_id from user_details where user_id IN (:selectedBCCOfficers) ";
				@SuppressWarnings("unchecked")
				List<String> officerBCCEmail = entityManager.createNativeQuery(findOfficerBCCEmailQuery)
						.setParameter("selectedBCCOfficers", selectedBCCOfficers).getResultList();
				logger.info("bccEmail" + officerBCCEmail);
				officerUserBCCEmail = officerBCCEmail;
				for (int i = 0; i < selectedBCCOfficers.size(); i++) {
					entityManager.createNativeQuery(insertMeetingScheduleMailBCCQuery)
							.setParameter("meetingScheduleId", meetingScheduleIdInteger)
							.setParameter("bccUserId", selectedBCCOfficers.get(i))
							.setParameter("bccCreatedBy", meeting_schedule.getBccCreatedBy().longValue())
							.executeUpdate();

					rowsAffected++;

					if (i % batchSize == 0) {
						entityManager.flush();
						entityManager.clear();
					}
				}
			}
			log.info("data for applicant" + processedCombinations + officerUserBCCEmail + officerUserCCEmail);
			return meetingScheduleIdInteger;

		} catch (Exception e) {
			logger.error("Error in saveRecord: " + e.getMessage(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	public MeetingSchedule updateFile(MeetingSchedule meeting_schedule) {
		try {
			Date currentDate = new Date();
			if (meeting_schedule.getAuctionStatus() == 1) {
				String insertQuery = "INSERT INTO application.land_allotement_for_auction (plot_no, created_by,created_on) "
						+ "SELECT DISTINCT (SELECT plot_no FROM land_bank.plot_information WHERE plot_code = ls.plot_code) AS plot_no, "
						+ ":createdBy AS created_by,CURRENT_TIMESTAMP as created_on FROM application.meeting_schedule ms "
						+ "JOIN application.meeting_schedule_applicant msa ON msa.meeting_schedule_id = ms.meeting_schedule_id "
						+ "JOIN public.land_schedule ls ON ls.land_application_id = msa.land_application_id "
						+ "WHERE ms.meeting_schedule_id = :meetingId AND ms.status = '0' AND msa.status = '0' AND ls.deleted_flag = '0'; ";
				BigInteger valueMeeting = new BigInteger(String.valueOf(meeting_schedule.getIntId()));
				BigInteger valueCreatedBy = new BigInteger(String.valueOf(meeting_schedule.getIntCreatedBy()));
				entityManager.createNativeQuery(insertQuery).setParameter("createdBy", valueCreatedBy)
						.setParameter("meetingId", valueMeeting).executeUpdate();
			}
			if (meeting_schedule.getAuctionStatus() > 0) {
				String nativeQuery = "UPDATE application.meeting_schedule SET upload_mom =:upload_mom, "
						+ "updated_by =:updatedBy, updated_on =:updatedOn, go_for_auction =:auctionStatus "
						+ "WHERE meeting_schedule_id = :meetingScheduleId";

				entityManager.createNativeQuery(nativeQuery)
						.setParameter("upload_mom", meeting_schedule.getFileUploadDocument())
						.setParameter("updatedBy", meeting_schedule.getIntUpdatedBy().longValue())
						.setParameter("updatedOn", currentDate)
						.setParameter("auctionStatus", meeting_schedule.getAuctionStatus())
						.setParameter("meetingScheduleId", meeting_schedule.getIntId().longValue()).executeUpdate();

			} else {
				String nativeQuery = "UPDATE application.meeting_schedule SET upload_mom =:upload_mom, "
						+ "updated_by =:updatedBy, updated_on =:updatedOn "
						+ "WHERE meeting_schedule_id = :meetingScheduleId";

				entityManager.createNativeQuery(nativeQuery)
						.setParameter("upload_mom", meeting_schedule.getFileUploadDocument())
						.setParameter("updatedBy", meeting_schedule.getIntUpdatedBy().longValue())
						.setParameter("updatedOn", currentDate)
						.setParameter("meetingScheduleId", meeting_schedule.getIntId().longValue()).executeUpdate();
			}

			return meeting_schedule;
		} catch (Exception e) {
			logger.error("Error in updateFile: " + e.getMessage(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	public Integer deleteRecord(Integer id, Integer updatedby) {
		entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
		Date currentDate = new Date();
		String nativeQuery = "UPDATE application.meeting_schedule SET status = CAST(:setstatus AS BIT), updated_on =:updatedOn, updated_by =:updatedBy WHERE meeting_schedule_id = :meetingScheduleId ";
		Integer status = entityManager.createNativeQuery(nativeQuery).setParameter("setstatus", 1)
				.setParameter("updatedOn", currentDate).setParameter("updatedBy", updatedby)
				.setParameter("meetingScheduleId", id).executeUpdate();
		entityManager.close();
		return status;
	}

	@Transactional
	public String updateMeetingNo(Integer meetingScheduleId, String meetingNo) {
		try {
			entityManager
					.createNativeQuery("UPDATE application.meeting_schedule SET " + " meeting_no=:meetingNo "
							+ " WHERE meeting_schedule_id = :meetingScheduleId")
					.setParameter("meetingScheduleId", meetingScheduleId.longValue())
					.setParameter("meetingNo", meetingNo).executeUpdate();

			entityManager.close();
		} catch (Exception e) {
			e.getMessage();
		}
		entityManager.close();
		return meetingNo;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Object[]> getMeetingNoById(Integer meetingScheduleId) {
		try {
			return entityManager.createNativeQuery(
					"SELECT meeting_no,meeting_date,venue FROM application.meeting_schedule WHERE meeting_schedule_id = :meetingScheduleId")
					.setParameter("meetingScheduleId", meetingScheduleId).getResultList();
		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	public List<MeetingScheduleDTO> viewMeetingScheduleDetails(Integer pageNumber, Integer pageSize,
			String applicationNo, String khataNo, Date meetingDate, BigInteger meetingId) {
		StringBuilder addConditionSqlAuction = new StringBuilder();
		StringBuilder addConditionSqlLand = new StringBuilder();
		if (applicationNo != null && !applicationNo.isEmpty()) {
			addConditionSqlAuction.append("AND bfma.bidder_form_m_application_number='" + applicationNo + "'");
			addConditionSqlLand.append("AND la1.application_no='" + applicationNo + "'");
		}
		if (meetingDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(meetingDate);
			String dateCondition = "AND DATE(ms.meeting_date) ='" + formattedDate + "'";
			addConditionSqlAuction.append(dateCondition);
			addConditionSqlLand.append(dateCondition);
		}
		String joinblesForAuction = "";
		String joinblesForLand = "";
		if (khataNo != null && !khataNo.isEmpty()) {
			String joinAuction = " JOIN application.tender_auction ta ON ta.tender_auction_id = bfma.tender_auction_id "
					+ " JOIN application.auction_plot ap ON ap.auction_plot_id = ta.auction_plot_id "
					+ " JOIN application.auction_plot_details apd ON ap.auction_plot_id = apd.auction_plot_id "
					+ " JOIN land_bank.plot_information pii ON(pii.plot_code=apd.plot_code) "
					+ " JOIN land_bank.khatian_information ki ON(ki.khatian_code=pii.khatian_code)";
			String joinLand = " JOIN public.land_schedule ls1 ON(ls1.land_application_id=la1.land_application_id) "
					+ " JOIN land_bank.plot_information pii ON(pii.plot_code=ls1.plot_code) "
					+ " JOIN land_bank.khatian_information ki ON(ki.khatian_code=pii.khatian_code)";
			joinblesForAuction = joinAuction;
			joinblesForLand = joinLand;
			String auctionCondition = "AND ki.khata_no='" + khataNo + "'";
			String landCondition = "AND ki.khata_no='" + khataNo + "'";
			addConditionSqlAuction.append(auctionCondition);
			addConditionSqlLand.append(landCondition);
		}

		String sql = " SELECT DISTINCT ms.meeting_schedule_id, ms.meeting_no, "
				+ " ms.venue, ms.meeting_date,  ms.meeting_purpose, ml.meeting_level, ms.upload_mom, "
				+ " (SELECT CAST (JSON_AGG(json_build_object( 'application_no', la.application_no, 'applicant_name', "
				+ " la.applicant_name, 'mobile_no', la.mobile_no, 'village_name', vm.village_name, "
				+ " 'tahasil_name', tm.tahasil_name, 'district_name', dm.district_name, 'khata_no', "
				+ " ki.khata_no, 'plot_no',  (select plot_no from land_bank.plot_information where plot_code=msa.plot_no) )) AS VARCHAR)  "
				+ " FROM application.meeting_schedule_applicant AS msa    "
				+ " JOIN land_application AS la ON msa.land_application_id = la.land_application_id  "
				+ " AND la.deleted_flag = '0'    "
				+ " JOIN land_bank.district_master AS dm ON la.district_code = dm.district_code  "
				+ " JOIN land_bank.village_master AS vm ON la.village_code = vm.village_code    "
				+ " JOIN land_bank.tahasil_master AS tm ON la.tehsil_code = tm.tahasil_code    "
				+ " JOIN land_bank.khatian_information AS ki ON la.khatian_code = ki.khatian_code    "
				+ " WHERE msa.meeting_schedule_id = ms.meeting_schedule_id  AND msa.status = '0' ) AS applicant_data,  "
				+ " ms.pdf_status,ms.meeting_level_id, ms.meeting_id ,CAST(ms.after_meeting_flag as varchar),msa.bidder_form_m_application_id,ms.no_plots_for_next_meeting_flag,"
				+ " msa.direct_meeting_auction_flag  "
				+ " FROM application.meeting_schedule AS ms   "
				+ " JOIN public.m_meeting_level AS ml ON ms.meeting_level_id = ml.meeting_level_id "
				+ " JOIN application.meeting_schedule_applicant msa ON (ms.meeting_schedule_id = msa.meeting_schedule_id) "
				+ " JOIN public.land_application la1 ON(la1.land_application_id=msa.land_application_id)  "
				+ joinblesForLand
				+ " WHERE ms.status = '0' AND ms.meeting_no IS NOT NULL AND msa.land_application_id IS NOT NULL AND ms.meeting_id=:meetingId  "
				+ addConditionSqlLand 
				+ " union "
				+ " select  DISTINCT ON(ms.meeting_schedule_id) ms.meeting_schedule_id , "
				+ " ms.meeting_no,ms.venue,ms.meeting_date,ms.meeting_purpose, ml.meeting_level,ms.upload_mom, "
				+ " ( SELECT CAST (JSON_AGG(json_build_object( 'application_no', bfma.bidder_form_m_application_number, "
				+ " 'applicant_name',(select full_name from public.citizen_profile_details where user_id=citizen_profile_details_id limit 1) , "
				+ " 'mobile_no',(select mobile_no from public.citizen_profile_details where user_id=citizen_profile_details_id limit 1), "
				+ " 'village_name',(select (select (select  vm.village_name "
				+ " from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ " from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1) , "
				+ " 'tahasil_name',(select (select (select (select  tm.tahasil_name "
				+ " from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
				+ " from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ " from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1), "
				+ " 'district_name',(select (select (select (select (select dm.district_name  "
				+ " from land_bank.district_master dm where dm.district_code=tm.district_code LIMIT 1)  "
				+ " from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
				+ " from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ " from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1), "
				+ " 'khata_no',(select (select ki.khata_no "
				+ " from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
				+ " from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1), "
				+ " 'plot_no',(select   pii.plot_no "
				+ " from land_bank.plot_information pii  where pii.plot_code=apd.plot_code limit 1))) AS VARCHAR)  "
				+ " FROM application.meeting_schedule_applicant AS msa "
				+ " JOIN application.bidder_form_m_application bfma ON(bfma.bidder_form_m_application_id=msa.bidder_form_m_application_id) "
				+ " JOIN application.tender_auction ta ON ta.tender_auction_id = bfma.tender_auction_id "
//				+ " JOIN application.auction_plot ap ON ap.auction_plot_id = ta.auction_plot_id "
				+ " JOIN application.auction_plot_details apd ON ta.auction_plot_id = apd.auction_plot_details_id AND apd.plot_code=msa.plot_no "
				+ " JOIN application.auction_plot ap ON ap.auction_plot_id = apd.auction_plot_id "
				+ " WHERE  msa.meeting_schedule_id = ms.meeting_schedule_id   AND msa.status = '0' AND bfma.deleted_flag = '0') AS applicant_data,  "
				+ " ms.pdf_status,ms.meeting_level_id, ms.meeting_id ,CAST(ms.after_meeting_flag as varchar),msa.bidder_form_m_application_id,ms.no_plots_for_next_meeting_flag,  "
				+ " msa.direct_meeting_auction_flag  "
				+ " FROM application.meeting_schedule AS ms   "
				+ " JOIN public.m_meeting_level AS ml ON ms.meeting_level_id = ml.meeting_level_id "
				+ " JOIN application.meeting_schedule_applicant msa ON (ms.meeting_schedule_id = msa.meeting_schedule_id) "
				+ " JOIN application.bidder_form_m_application bfma ON(bfma.bidder_form_m_application_id=msa.bidder_form_m_application_id) "
				+ joinblesForAuction
				+ " WHERE ms.status = '0' AND ms.meeting_no IS NOT NULL AND ms.meeting_id=:meetingId    "
				+ addConditionSqlAuction 
				+ " UNION select "
				+ "DISTINCT ms.meeting_schedule_id, "
				+ "ms.meeting_no, ms.venue,  ms.meeting_date,ms.meeting_purpose, "
				+ "ml.meeting_level, ms.upload_mom, "
				+ "( SELECT CAST (JSON_AGG(json_build_object( 'application_no', "
				+ "'N/A', 'applicant_name', null ,  'mobile_no', null , "
				+ "'village_name', (select "
				+ "(select  (select  vm.village_name from land_bank.village_master vm   "
				+ "where  vm.village_code=ki.village_code LIMIT 1) from land_bank.khatian_information ki   "
				+ "where ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii   "
				+ "where pii.plot_code=msa.plot_no limit 1) , "
				+ "'tahasil_name',  (select (select (select (select "
				+ "tm.tahasil_name  from land_bank.tahasil_master tm   "
				+ "where  tm.tahasil_code=vm.tahasil_code LIMIT 1)    "
				+ "from land_bank.village_master vm where "
				+ "vm.village_code=ki.village_code LIMIT 1)  from land_bank.khatian_information ki   "
				+ "where  ki.khatian_code=pii.khatian_code LIMIT 1) from land_bank.plot_information pii   "
				+ "where pii.plot_code=msa.plot_no limit 1),   "
				+ "'district_name',(select (select (select (select (select dm.district_name    "
				+ "from  land_bank.district_master dm  where dm.district_code=tm.district_code LIMIT 1)    "
				+ "from land_bank.tahasil_master tm   where tm.tahasil_code=vm.tahasil_code LIMIT 1)    "
				+ "from land_bank.village_master vm where vm.village_code=ki.village_code LIMIT 1)    "
				+ "from land_bank.khatian_information ki where  ki.khatian_code=pii.khatian_code LIMIT 1)     "
				+ "from  land_bank.plot_information pii where  pii.plot_code=msa.plot_no limit 1), "
				+ "'khata_no', (select (select  ki.khata_no from land_bank.khatian_information ki   "
				+ "where  ki.khatian_code=pii.khatian_code LIMIT 1) from  land_bank.plot_information pii   "
				+ "where pii.plot_code=msa.plot_no limit 1),  'plot_no',(select "
				+ "pii.plot_no  from land_bank.plot_information pii   "
				+ "where  pii.plot_code=msa.plot_no limit 1))) AS VARCHAR) "
				+ "FROM  application.meeting_schedule_applicant AS msa    "
				+ "WHERE msa.meeting_schedule_id = ms.meeting_schedule_id  AND msa.status = '0') AS applicant_data,   "
				+ "ms.pdf_status,ms.meeting_level_id, ms.meeting_id , "
				+ "CAST(ms.after_meeting_flag as varchar),msa.bidder_form_m_application_id,ms.no_plots_for_next_meeting_flag ,msa.direct_meeting_auction_flag    "
				+ "FROM application.meeting_schedule AS ms     "
				+ "JOIN public.m_meeting_level AS ml  ON ms.meeting_level_id = ml.meeting_level_id   "
				+ "JOIN application.meeting_schedule_applicant msa  ON (ms.meeting_schedule_id = msa.meeting_schedule_id)    "
				+ "WHERE  ms.status = '0'   AND ms.meeting_no IS NOT NULL  AND ms.meeting_id=:meetingId AND msa.direct_meeting_auction_flag=1 "
				+ " ORDER BY meeting_schedule_id DESC "
				+ " LIMIT :pageSize  OFFSET :offset ";
		Integer offset = (pageNumber - 1) * pageSize;
		List<MeetingScheduleDTO> resultList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> rows = entityManager.createNativeQuery(sql).setParameter("offset", offset)
				.setParameter("pageSize", pageSize).setParameter("meetingId", meetingId).getResultList();
		for (Object[] row : rows) {
			MeetingScheduleDTO dto = new MeetingScheduleDTO();
			dto.setMeetingScheduleId((BigInteger) row[0]);
			dto.setMeetingNo((String) row[1]);
			dto.setVenue((String) row[2]);
			dto.setMeetingDate((Date) row[3]);
			dto.setMeetingPurpose((String) row[4]);
			dto.setMeetingLevel((String) row[5]);
			dto.setUploadMom((String) row[6]);
			dto.setPdfStatus((Short) row[8]);
			dto.setMeetingLevleId((Short) row[9]);
			dto.setMeetingId((BigInteger) row[10]);
			dto.setAfterMeetingFlag((String) row[11]);
			dto.setBidderFromApplicationId((BigInteger) row[12]);
			dto.setFlagForNoMeeting((Short) row[13]);
			dto.setDirectMeetingFlag((Short) row[14]);

			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, String>> applicantDataList = null;
			try {
				if (row[7] != null) {
					applicantDataList = objectMapper.readValue(row[7].toString(),
							new TypeReference<List<Map<String, String>>>() {
							});
				}

				dto.setApplicantData(applicantDataList);
			} catch (IOException e) {
			}
			resultList.add(dto);
		}
		entityManager.close();
		log.info(":: viewMeetingScheduleDetails() --   Result Return Scucess..!!" + resultList);
		return resultList;

	}

	@Transactional
	public BigInteger countMeetingScheduleDetails(String applicationNo, String khataNo, Date meetingDate) {
		String query = "SELECT COUNT(*) FROM application.meeting_schedule ms where ms.status = '0' AND ms.meeting_no IS NOT NULL";
		BigInteger count = (BigInteger) entityManager.createNativeQuery(query).getSingleResult();
		entityManager.close();
		return count;
	}

	public MeetingScheduleDTO findDataByIntId(Integer intId, Integer auctionFlag) {
		logger.info("intId : " + intId);
		String query = "SELECT ms.meeting_schedule_id, ms.meeting_no, ms.venue, ms.meeting_date, ms.meeting_purpose, ml.meeting_level_id, ARRAY_TO_STRING ( ARRAY( SELECT msa.land_application_id FROM application.meeting_schedule_applicant msa \r\n"
				+ "JOIN land_application AS la ON msa.land_application_id = la.land_application_id AND la.deleted_flag = '0' \r\n"
				+ "WHERE msa.meeting_schedule_id = ms.meeting_schedule_id AND msa.status = '0' ), ',') AS land_application_ids, ARRAY_TO_STRING ( ARRAY( SELECT mscc.user_id FROM application.meeting_schedule_mail_cc mscc \r\n"
				+ "JOIN user_details AS ud ON mscc.user_id = ud.user_id AND ud.status = '0' \r\n"
				+ "WHERE mscc.meeting_schedule_id = ms.meeting_schedule_id AND mscc.status = '0' ), ',' ) AS officer_cc_ids, ARRAY_TO_STRING ( ARRAY ( SELECT msbcc.user_id \r\n"
				+ "FROM application.meeting_schedule_mail_bcc msbcc JOIN user_details AS ud ON msbcc.user_id = ud.user_id AND ud.status = '0' \r\n"
				+ "WHERE msbcc.meeting_schedule_id = ms.meeting_schedule_id AND msbcc.status = '0' ), ',' ) AS officer_bcc_ids FROM application.meeting_schedule ms \r\n"
				+ "JOIN m_meeting_level ml USING(meeting_level_id) \r\n"
				+ "WHERE ms.status = '0' AND ms.meeting_schedule_id = :intId";

		String applicantSql = "";
		if (auctionFlag == 3) {
			applicantSql = "SELECT\r\n"
					+ "DISTINCT ON (a.bidder_form_m_application_number) a.bidder_form_m_application_number,\r\n"
					+ "(select full_name from public.citizen_profile_details where a.user_id=citizen_profile_details_id limit 1) as landAppName,\r\n"
					+ "(select mobile_no from public.citizen_profile_details where a.user_id=citizen_profile_details_id limit 1),\r\n"
					+ "(select (select (select (select (select dm.district_name \r\n"
					+ "from land_bank.district_master dm where dm.district_code=tm.district_code LIMIT 1) \r\n"
					+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1) \r\n"
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1) \r\n"
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)  \r\n"
					+ "from land_bank.plot_information pii  where pii.plot_code=pii.plot_code limit 1),\r\n"
					+ "(select (select (select (select  tm.tahasil_name\r\n"
					+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1) \r\n"
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1) \r\n"
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)  \r\n"
					+ "from land_bank.plot_information pii  where pii.plot_code=pii.plot_code limit 1),\r\n"
					+ "(select (select (select  vm.village_name\r\n"
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1) \r\n"
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)  \r\n"
					+ "from land_bank.plot_information pii  where pii.plot_code=pii.plot_code limit 1),\r\n"
					+ "(select (select ki.khata_no\r\n"
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)  \r\n"
					+ "from land_bank.plot_information pii  where pii.plot_code=pii.plot_code limit 1),\r\n "
					+ "(select plot_no from land_bank.plot_information where plot_code=msa.plot_no) as plot_no,  "
//					+ "STRING_AGG(DISTINCT msa.plot_no,', ') AS plot_numbers,\r\n"
					+ "a.bidder_form_m_application_id,\r\n"
					+ "(select email_id from public.citizen_profile_details where a.user_id=citizen_profile_details_id limit 1),\r\n"
					+ "a.created_on ,msa.plot_no as plotCode  " 
					+ "FROM application.bidder_form_m_application a   \r\n"
					+ "JOIN application.meeting_schedule_applicant msa ON(msa.bidder_form_m_application_id=a.bidder_form_m_application_id)    \r\n"
					+ "JOIN application.meeting_schedule ms ON(msa.meeting_schedule_id=ms.meeting_schedule_id)\r\n"
					+ "JOIN land_bank.plot_information pii ON(pii.plot_code=msa.plot_no)\r\n"
					+ "WHERE  a.deleted_flag='0' AND msa.status='0' AND ms.status='0' AND ms.meeting_schedule_id=:intId   \r\n"
					+ "GROUP BY  a.bidder_form_m_application_number, a.user_id, a.bidder_form_m_application_id,msa.plot_no  ";
		} else {
			applicantSql = " SELECT "
//					+ "DISTINCT ON (a.application_no) a.application_no, "
					+ " a.application_no ,(SELECT applicant_name FROM public.land_application   WHERE land_application_id = msa.land_application_id LIMIT 1) as landAppName,"
					+ " a.mobile_no, d.district_name, t.tahasil_name, v.village_name, k.khata_no, p.plot_no AS plot_numbers, "
					+ " a.land_application_id, a.email_address, b.created_on ,p.plot_code as plotCode    "
					+ " FROM land_application a  JOIN land_application_approval b USING(land_application_id)    "
					+ " JOIN land_schedule ls USING(land_application_id)   "
					+ " JOIN land_bank.district_master d USING(district_code)   "
					+ " JOIN land_bank.tahasil_master t  ON a.tehsil_code = t.tahasil_code   "
					+ " JOIN land_bank.village_master v USING(village_code)   "
					+ " JOIN land_bank.khatian_information k USING(khatian_code)   "
//						+ " JOIN land_bank.plot_information p  ON p.plot_code = ls.plot_code   "
					+ " JOIN application.meeting_schedule_applicant msa  ON(msa.land_application_id=a.land_application_id)   "
					+ " JOIN application.meeting_schedule ms  ON(msa.meeting_schedule_id=ms.meeting_schedule_id)   "
					+ " JOIN land_bank.plot_information p  ON p.plot_code = msa.plot_no   "
					+ " WHERE  a.deleted_flag='0'  AND ls.deleted_flag='0' AND msa.status='0' AND ms.status='0' "
					+ " AND ms.meeting_schedule_id=:intId  "
					+ " GROUP BY a.application_no, a.applicant_name, a.mobile_no, d.district_name, "
					+ " t.tahasil_name, v.village_name, k.khata_no, a.land_application_id, a.email_address, "
					+ " b.created_on,  " + " msa.land_application_id,p.plot_no, p.plot_code    ";
		}
		try {
			@SuppressWarnings("unchecked")
			List<Object[]> respones = entityManager.createNativeQuery(applicantSql).setParameter("intId", intId)
					.getResultList();
			List<LandApplicantMeetingDTO> result = new ArrayList<>();
			for (Object[] row : respones) {
				LandApplicantMeetingDTO dto = new LandApplicantMeetingDTO();
				dto.setApplicantNo((String) row[0]);
				dto.setApplicantName((String) row[1]);
				dto.setMobileNo((String) row[2]);
				dto.setDistrictName((String) row[3]);
				dto.setTehsilName((String) row[4]);
				dto.setMouzaName((String) row[5]);
				dto.setKhataNo((String) row[6]);
				dto.setPlotNo((String) row[7]);
				dto.setLandApplicantId((BigInteger) row[8]);
				dto.setEmailId((String) row[9]);
				dto.setCreatedOn((Date) row[10]);
				dto.setPlotCode((String) row[11]);
				result.add(dto);
			}
			Object[] row = (Object[]) entityManager.createNativeQuery(query).setParameter("intId", intId)
					.getSingleResult();
			logger.info("inside query...");
			MeetingScheduleDTO dto = new MeetingScheduleDTO();
			dto.setMeetingScheduleId((BigInteger) row[0]);
			dto.setMeetingNo((String) row[1]);
			dto.setVenue((String) row[2]);
			dto.setMeetingDate((Date) row[3]);
			dto.setMeetingPurpose((String) row[4]);
			dto.setMeetingLevelId((Short) row[5]);
			dto.setLandApplicantIds((String) row[6]);
			dto.setOfficerCCIds((String) row[7]);
			dto.setOfficerBCCIds((String) row[8]);
			dto.setLandApplicantResult(result);
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("inside catch");
			return null;
		} finally {
			entityManager.close();
		}
	}

	public Integer validate(MeetingSchedule meeting_schedule) {
		try {
			String getMeetingLevelOfLandApplicant = "SELECT CAST(meeting_level_id AS INTEGER) as meeting_level_id from application.meeting_schedule "
					+ " JOIN application.meeting_schedule_applicant using(meeting_schedule_id) "
					+ " WHERE land_application_id in (:selectedLandApplicationIds) ";
			String matchMeetingLevelWithLandApplicant = "SELECT COUNT(*) from application.meeting_schedule "
					+ " JOIN application.meeting_schedule_applicant using(meeting_schedule_id) "
					+ " WHERE land_application_id in (:selectedLandApplicationIds) and meeting_level_id = :meetingLevelId ";
			String checkAuctionLandApplicant = "SELECT COUNT(*) from application.meeting_schedule "
					+ " JOIN application.meeting_schedule_applicant using(meeting_schedule_id) "
					+ " WHERE land_application_id in (:selectedLandApplicationIds) and go_for_auction = 1 ";
			List<Integer> selectedLandApplicationIds = Arrays.asList(meeting_schedule.getSelectedLandApplicationId());
			Integer meetingScheduleIdInteger = null;
			BigInteger countOfMeetingLevel = (BigInteger) entityManager
					.createNativeQuery(matchMeetingLevelWithLandApplicant)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds)
					.setParameter("meetingLevelId", meeting_schedule.getSelMeetingLevel55()).getSingleResult();

			BigInteger countOfAuction = (BigInteger) entityManager.createNativeQuery(checkAuctionLandApplicant)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds).getSingleResult();
			@SuppressWarnings("unchecked")
			List<Integer> getMeetingLevel = entityManager.createNativeQuery(getMeetingLevelOfLandApplicant)
					.setParameter("selectedLandApplicationIds", selectedLandApplicationIds).getResultList();
			if (!getMeetingLevel.isEmpty()) {
				if (!getMeetingLevel.contains(1)) {
					logger.info("Meeting level 1 has not been held.");
					meetingScheduleIdInteger = -1;
					return meetingScheduleIdInteger;
				}
				if (!getMeetingLevel.contains(2) && meeting_schedule.getSelMeetingLevel55() != 2) {
					logger.info("Meeting level 2 has not been held.");
					meetingScheduleIdInteger = -1;
					return meetingScheduleIdInteger;
				}
				if (!getMeetingLevel.contains(3) && meeting_schedule.getSelMeetingLevel55() != 3
						&& meeting_schedule.getSelMeetingLevel55() != 2) {
					logger.info("Meeting level 3 has not been held.");
					meetingScheduleIdInteger = -1;
					return meetingScheduleIdInteger;
				}
				if (!getMeetingLevel.contains(4) && meeting_schedule.getSelMeetingLevel55() != 4
						&& meeting_schedule.getSelMeetingLevel55() != 3
						&& meeting_schedule.getSelMeetingLevel55() != 2) {
					logger.info("Meeting level 4 has not been held.");
					meetingScheduleIdInteger = -1;
					return meetingScheduleIdInteger;
				}
			}

			if (getMeetingLevel.isEmpty() && (meeting_schedule.getSelMeetingLevel55().intValue() == 2
					|| meeting_schedule.getSelMeetingLevel55().intValue() == 3
					|| meeting_schedule.getSelMeetingLevel55().intValue() == 4)) {

				logger.info("Meeting level 1 is missing.");
				meetingScheduleIdInteger = -1;
				return meetingScheduleIdInteger;
			}

			if (countOfMeetingLevel.intValue() > 0) {
				logger.info("This level of meeting has already been held.");
				meetingScheduleIdInteger = 0;
				return meetingScheduleIdInteger;
			} else if (countOfAuction.intValue() > 0) {
				logger.info("Applicant is in auction.");
				meetingScheduleIdInteger = -2;
				return meetingScheduleIdInteger;
			} else if ((getMeetingLevel.isEmpty() && meeting_schedule.getSelMeetingLevel55().intValue() == 1)
					|| (!getMeetingLevel.isEmpty() && meeting_schedule.getSelMeetingLevel55().intValue() == 2)
					|| (getMeetingLevel.isEmpty() && meeting_schedule.getSelMeetingLevel55().intValue() == 3)
					|| (getMeetingLevel.isEmpty() && meeting_schedule.getSelMeetingLevel55().intValue() == 4)
					|| countOfMeetingLevel.intValue() == 0) {

				meetingScheduleIdInteger = 1;
				return meetingScheduleIdInteger;
			} else {
				meetingScheduleIdInteger = -1;
				return meetingScheduleIdInteger;
			}
		} catch (Exception e) {
			logger.error("Error in saveRecord: " + e.getMessage(), e);
			return null;
		} finally {
			entityManager.close();
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Object> printPdf(Integer meetingId, Integer auctionFlag) {
		String sql = "";
		if (auctionFlag == 3) {
			sql = " SELECT " + "ms.meeting_no, " + "ms.meeting_date, " + "ms.venue, " + "ms.meeting_purpose, "
					+ "ms.meeting_level_id, " + "CAST((SELECT json_agg( jsonb_build_object( 'land_app_id', "
					+ "bfma.bidder_form_m_application_id, " + "'applicant_name', "
					+ "(select full_name from public.citizen_profile_details where user_id=citizen_profile_details_id limit 1), "
					+ "'application_no', " + "bfma.bidder_form_m_application_number, " + "'district_name', "
					+ "(select (select (select (select (select dm.district_name  "
					+ "from land_bank.district_master dm where dm.district_code=tm.district_code LIMIT 1)  "
					+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
					+ "from land_bank.plot_information pii  where pii.plot_code=pii.plot_code limit 1), "
					+ "'tahasil_name', (select (select (select (select  tm.tahasil_name "
					+ "from land_bank.tahasil_master tm  where tm.tahasil_code=vm.tahasil_code LIMIT 1)  "
					+ "from land_bank.village_master vm  where vm.village_code=ki.village_code LIMIT 1)  "
					+ "from land_bank.khatian_information ki  where ki.khatian_code=pii.khatian_code LIMIT 1)   "
					+ "from land_bank.plot_information pii  where pii.plot_code=pii.plot_code limit 1) )) AS applicantData  "
					+ "FROM application.meeting_schedule_applicant AS msa   "
					+ "JOIN application.bidder_form_m_application bfma ON(bfma.bidder_form_m_application_id=msa.bidder_form_m_application_id) "
					+ "JOIN land_bank.plot_information pii ON(pii.plot_code=msa.plot_no) "
					+ "WHERE ms.meeting_schedule_id = msa.meeting_schedule_id AND msa.status = '0')as varchar), "
					+ "CAST(( SELECT json_agg( jsonb_build_object( 'user_id', " + "mcc.user_id, " + "'full_name', "
					+ "ud.full_name, " + "'role_name', " + "mr.role_name)) AS ccData  "
					+ "FROM application.meeting_schedule_mail_cc AS mcc  "
					+ "JOIN user_details AS ud ON mcc.user_id = ud.user_id  "
					+ "JOIN user_role AS ur ON ud.user_id = ur.user_id  "
					+ "JOIN m_role AS mr ON ur.role_id = mr.role_id  "
					+ "WHERE ms.meeting_schedule_id = mcc.meeting_schedule_id  "
					+ "AND mcc.status = '0')as varchar), CAST((SELECT " + "json_agg( jsonb_build_object( 'user_id', "
					+ "mbcc.user_id, " + "'full_name', " + "ud.full_name, " + "'role_name', "
					+ "mr.role_name)) AS bccData  " + "FROM  application.meeting_schedule_mail_bcc AS mbcc  "
					+ "JOIN user_details AS ud  ON mbcc.user_id = ud.user_id  "
					+ "JOIN  user_role AS ur ON ud.user_id = ur.user_id  "
					+ "JOIN m_role AS mr ON ur.role_id = mr.role_id  "
					+ "WHERE  ms.meeting_schedule_id = mbcc.meeting_schedule_id  "
					+ "AND mbcc.status = '0') as varchar)  " + "FROM application.meeting_schedule AS ms  "
					+ "WHERE ms.status = '0' AND meeting_schedule_id = :meetingId  ";
		} else {
			sql = "SELECT ms.meeting_no, ms.meeting_date, ms.venue, ms.meeting_purpose, ms.meeting_level_id, "
					+ "CAST((SELECT json_agg( " + "jsonb_build_object( " + "'land_app_id', msa.land_application_id, "
					+ "'applicant_name', la.applicant_name, " + "'application_no', la.application_no, "
					+ "'district_name', dm.district_name, " + "'tahasil_name', tm.tahasil_name )) AS applicantData "
					+ "FROM application.meeting_schedule_applicant AS msa  "
					+ "JOIN land_application AS la ON msa.land_application_id = la.land_application_id "
					+ "JOIN land_bank.district_master AS dm ON la.district_code = dm.district_code "
					+ "JOIN land_bank.tahasil_master AS tm ON la.tehsil_code = tm.tahasil_code "
					+ "WHERE ms.meeting_schedule_id = msa.meeting_schedule_id AND msa.status = '0')as varchar), "
					+ "CAST(( " + "SELECT json_agg( " + "jsonb_build_object( " + "'user_id', mcc.user_id, "
					+ "'full_name', ud.full_name, " + "'role_name', mr.role_name)) AS ccData "
					+ "FROM application.meeting_schedule_mail_cc AS mcc "
					+ "JOIN user_details AS ud ON mcc.user_id = ud.user_id "
					+ "JOIN user_role AS ur ON ud.user_id = ur.user_id "
					+ "JOIN m_role AS mr ON ur.role_id = mr.role_id "
					+ "WHERE ms.meeting_schedule_id = mcc.meeting_schedule_id AND mcc.status = '0')as varchar), "
					+ "CAST((SELECT json_agg( " + "jsonb_build_object( " + "'user_id', mbcc.user_id, "
					+ "'full_name', ud.full_name, " + "'role_name', mr.role_name)) AS bccData "
					+ "FROM application.meeting_schedule_mail_bcc AS mbcc "
					+ "JOIN user_details AS ud ON mbcc.user_id = ud.user_id "
					+ "JOIN user_role AS ur ON ud.user_id = ur.user_id "
					+ "JOIN m_role AS mr ON ur.role_id = mr.role_id "
					+ "WHERE ms.meeting_schedule_id = mbcc.meeting_schedule_id AND mbcc.status = '0') as varchar) "
					+ "FROM application.meeting_schedule AS ms "
					+ "WHERE ms.status = '0' AND meeting_schedule_id = :meetingId";
		}

		entityManager
				.createNativeQuery(
						"UPDATE application.meeting_schedule SET pdf_status = 1 WHERE meeting_schedule_id = :meetingId")
				.setParameter("meetingId", meetingId).executeUpdate();
		return entityManager.createNativeQuery(sql).setParameter("meetingId", meetingId).getResultList();
	}

	@Transactional
	public Integer updateStatus(Integer intId) {
		String nativeQuery = "UPDATE application.meeting_schedule SET pdf_status = 2 WHERE meeting_schedule_id = :meetingScheduleId ";
		Integer status = entityManager.createNativeQuery(nativeQuery).setParameter("meetingScheduleId", intId)
				.executeUpdate();
		entityManager.close();
		return status;
	}

	@Transactional
	public List<Object[]> fetchMails(Integer intId,Integer auctionFlag) {
		String addQuery="";
		if(auctionFlag==1) {
			addQuery="(SELECT (select email_id from public.citizen_profile_details where citizen_profile_details_id=la.user_id) "
					+ "AS receipientMail FROM application.meeting_schedule_applicant AS msa  "
					+ "JOIN  application.bidder_form_m_application AS la ON msa.bidder_form_m_application_id = la.bidder_form_m_application_id    "
					+ "WHERE ms.meeting_schedule_id = msa.meeting_schedule_id  AND msa.status = '0' LIMIT 1),  ";
		}else {
			addQuery="(SELECT STRING_AGG(la.email_address, ',') AS receipientMail  "
					+ "FROM application.meeting_schedule_applicant AS msa    "
					+ "JOIN land_application AS la ON msa.land_application_id = la.land_application_id   "
					+ "WHERE ms.meeting_schedule_id = msa.meeting_schedule_id AND msa.status = '0'),  ";
		}
	 String	nativeQuery = "SELECT TO_CHAR(ms.meeting_date, 'Month DD, YYYY') AS meeting_date, TO_CHAR(ms.meeting_date, 'HH12:MI AM') AS meeting_time,  "
	 		    + " ms.venue, ms.meeting_purpose, " +addQuery
				+ "(SELECT STRING_AGG(ud.email_id, ',') AS ccMail   \r\n"
				+ "FROM application.meeting_schedule_mail_cc AS mcc   \r\n"
				+ "JOIN user_details AS ud ON mcc.user_id = ud.user_id     \r\n"
				+ "WHERE ms.meeting_schedule_id = mcc.meeting_schedule_id AND mcc.status = '0'),\r\n"
				+ "(SELECT STRING_AGG(ud.email_id, ',') AS bccMail   \r\n"
				+ "FROM application.meeting_schedule_mail_bcc AS mbcc   \r\n"
				+ "JOIN user_details AS ud ON mbcc.user_id = ud.user_id    \r\n"
				+ "WHERE ms.meeting_schedule_id = mbcc.meeting_schedule_id AND mbcc.status = '0')  \r\n"
				+ "FROM application.meeting_schedule AS ms  \r\n"
				+ "WHERE ms.status = '0' AND meeting_schedule_id = :meetingScheduleId";
		
		return entityManager.createNativeQuery(nativeQuery).setParameter("meetingScheduleId", intId).getResultList();
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Object[]> getLandApplicationData(BigInteger meetingId) {
		String nativeQuery = "SELECT DISTINCT ms.meeting_schedule_id as meetingId, (select plot_no from land_bank.plot_information where plot_code=msa.plot_no)  as plotNo, msa.land_application_id as landAppId, "
				+ " CASE WHEN msa.land_application_id IS NOT NULL THEN (SELECT applicant_name  FROM "
				+ " public.land_application WHERE " 
				+ " land_application_id = msa.land_application_id LIMIT 1)    "
				+ " ELSE (select (SELECT full_name FROM public.citizen_profile_details WHERE citizen_profile_details_id=bfma.user_id) "
				+ " from application.bidder_form_m_application bfma where msa.bidder_form_m_application_id=bfma.user_id limit 1)    "
				+ " END as landAppName ,"
				+ " (SELECT application_no FROM public.land_application WHERE land_application_id = msa.land_application_id LIMIT 1) as landAppNo, "
				+ " ms.meeting_level_id as mettingLevleId, " + " ms.plots_ids as plotIds ,"
				+ " msa.approval_status as approvalStatus, msa.plot_no as plot_code,msa.metting_auction_status, msa.direct_meeting_auction_flag  "
				+ " FROM application.meeting_schedule ms "
				+ " JOIN application.meeting_schedule_applicant msa ON (ms.meeting_schedule_id = msa.meeting_schedule_id) "
				+ " WHERE ms.meeting_schedule_id = :meetingId AND ms.status = '0' AND msa.status = '0' "
				+ " GROUP BY ms.meeting_schedule_id, msa.plot_no, msa.land_application_id,msa.approval_status,msa.metting_auction_status,msa.direct_meeting_auction_flag,msa.bidder_form_m_application_id ";
		Query query = entityManager.createNativeQuery(nativeQuery).setParameter("meetingId", meetingId);
		return query.getResultList();
	}

	@Transactional
	public Integer updateMeetingScheduleUploadMom(BigInteger meetingScheduleId, String newUploadMom, String plotsNo,
			Short noAuctionFlag) {
		String updateQuery = "UPDATE application.meeting_schedule SET upload_mom = :newUploadMom,plots_ids=:plotsIds,no_plots_for_next_meeting_flag=:noAuctionFlag "
				+ " WHERE meeting_schedule_id = :meetingScheduleId";
		return entityManager.createNativeQuery(updateQuery).setParameter("newUploadMom", newUploadMom)
				.setParameter("meetingScheduleId", meetingScheduleId).setParameter("plotsIds", plotsNo)
				.setParameter("noAuctionFlag", noAuctionFlag).executeUpdate();
	}

	@Transactional
	public Integer updateMeetingScheduleApplicantApprovalStatus(BigInteger landApplicationId,
			BigInteger meetingScheduleId, Integer approvalStatus, Short mettingLevle, Integer auctionFlag,
			String plotCode) {
		String changeFlagCondition = "";
		if (auctionFlag == 3) {
			changeFlagCondition = "WHERE bidder_form_m_application_id";
		} else {
			changeFlagCondition = "WHERE land_application_id";

		}
		String updateQuery = "UPDATE application.meeting_schedule_applicant "
				+ "SET approval_status = :approvalStatus  " + changeFlagCondition + "= :landApplicationId "
				+ "AND meeting_schedule_id = :meetingScheduleId AND status='0' AND plot_no=:plotCode ";
		return entityManager.createNativeQuery(updateQuery).setParameter("landApplicationId", landApplicationId)
				.setParameter("meetingScheduleId", meetingScheduleId).setParameter("approvalStatus", approvalStatus)
				.setParameter("plotCode", plotCode).executeUpdate();
	}

	@Transactional
	public Integer insertLandAllotmentForAuction(String plotNo, BigInteger createdBy, BigInteger meetingId,
			String plotCode,Short auctionProcessFlag) {
		String insertQuery = "INSERT INTO application.land_allotement_for_auction (plot_no, created_by, created_on,meeting_schedule_id,plot_code) "
				+ "VALUES (:plotNo, :createdBy, CURRENT_TIMESTAMP,:meetingId,:plotCode)";

		return entityManager.createNativeQuery(insertQuery).setParameter("plotNo", plotNo)
				.setParameter("createdBy", createdBy).setParameter("meetingId", meetingId)
				.setParameter("plotCode", plotCode).executeUpdate();
	}

	@Transactional
	public BigInteger saveRecordsForAfetrMeetings(MeetingSchedule meeting_schedule) {
		String insertMeetingScheduleQuery = "INSERT INTO application.meeting_schedule (meeting_date, meeting_purpose, meeting_level_id, venue,meeting_id,created_by)"
				+ " VALUES (:meetingDate, :meetingPurpose, :meetingLevelId, :venue,:meetingId,:createdBy)"
				+ " RETURNING meeting_schedule_id";

		String changeFlagCondition = "";
		String changeFlagPlotsColumn = "";
		String changeFlagPlotsValue = "";
		if (meeting_schedule.getAuctionFlag() == 3) {
			changeFlagCondition = " bidder_form_m_application_id";
			changeFlagPlotsColumn = " , metting_auction_status ";
			changeFlagPlotsValue = ", 1";
		} else {
			changeFlagCondition = "land_application_id";

		}
		String insertMeetingScheduleApplicantQuery = "INSERT INTO application.meeting_schedule_applicant (meeting_schedule_id, "
				+ changeFlagCondition + ", created_by,plot_no  " + changeFlagPlotsColumn + " )"
				+ " VALUES (:meetingScheduleId, :landApplicationId,:createdBy,:plots " + changeFlagPlotsValue + "  )";

		String insertMeetingScheduleMailCCQuery = "INSERT INTO application.meeting_schedule_mail_cc (meeting_schedule_id, user_id, created_by)"
				+ " VALUES (:meetingScheduleId, :ccUserId, :ccCreatedBy)";

		String insertMeetingScheduleMailBCCQuery = "INSERT INTO application.meeting_schedule_mail_bcc (meeting_schedule_id, user_id, created_by)"
				+ " VALUES (:meetingScheduleId, :bccUserId, :bccCreatedBy)";
		String addLandAuctionId = "";
		if (meeting_schedule.getAuctionFlag() == 3) {
			addLandAuctionId = " bidder_form_m_application_id ";
		} else {
			addLandAuctionId = " land_application_id ";
		}
		String plotQuery = "select plot_no from application.meeting_schedule_applicant "
				+ " where meeting_schedule_id=:meetingSheduleId AND " + addLandAuctionId
				+ " =:landApplicationId AND status='0' ";
		BigInteger bigIntegerValue = BigInteger.valueOf(meeting_schedule.getIntCreatedBy());
		BigInteger meetingScheduleId = (BigInteger) entityManager.createNativeQuery(insertMeetingScheduleQuery)
				.setParameter("meetingDate", meeting_schedule.getTxtMeetingDate53())
				.setParameter("meetingPurpose", meeting_schedule.getTxtrMeetingPurpose54())
				.setParameter("meetingLevelId", meeting_schedule.getSelMeetingLevel55())
				.setParameter("venue", meeting_schedule.getVenue())
				.setParameter("meetingId", meeting_schedule.getMeetingIdRe()).setParameter("createdBy", bigIntegerValue)
				.getSingleResult(); // .setParameter("createdBy", meeting_schedule.getIntCreatedBy().longValue())
		
		
		List<MeetingRemoveApplicantDto> selectlandApplicationAndPlotsIds =meeting_schedule.getLandApplicantAndPlots();

		
		List<Integer> selectedCCOfficers = Arrays.asList(meeting_schedule.getSelectedCCOfficers());
		List<Integer> selectedBCCOfficers = Arrays.asList(meeting_schedule.getSelectedBCCOfficers());
		List<String> officerUserCCEmail = null;
		List<String> officerUserBCCEmail = null;
		int batchSize = 50;
		int rowsAffected = 0;
		Integer applicantInsert = 0;
		if (meetingScheduleId != null) {

			for (int i = 0; i < selectlandApplicationAndPlotsIds.size(); i++) {
				
//				String plots = null;
//				@SuppressWarnings("unchecked")
//				List<String> plotList = entityManager.createNativeQuery(plotQuery)
//						.setParameter("meetingSheduleId", meeting_schedule.getMeetingSheduleIdRe())
//						.setParameter("landApplicationId", selectlandApplicationAndPlotsIds.get(i).getId()).getResultList();
//				if (!plotList.isEmpty()) {
//					plots = plotList.get(0);
//				} else {
//				}
				applicantInsert = entityManager.createNativeQuery(insertMeetingScheduleApplicantQuery)
						.setParameter("meetingScheduleId", meetingScheduleId)
						.setParameter("landApplicationId", selectlandApplicationAndPlotsIds.get(i).getId())
						.setParameter("createdBy", bigIntegerValue).setParameter("plots",selectlandApplicationAndPlotsIds.get(i).getPlotNo()).executeUpdate();
			}
		}
		if (applicantInsert > 0) {
			if (!selectedCCOfficers.isEmpty()) {
				String findOfficerCCEmailQuery = "SELECT email_id from user_details where user_id IN (:selectedCCOfficers) ";
				@SuppressWarnings("unchecked")
				List<String> officerCCEmail = entityManager.createNativeQuery(findOfficerCCEmailQuery)
						.setParameter("selectedCCOfficers", selectedCCOfficers).getResultList();
				logger.info("ccEmail" + officerCCEmail);
				officerUserCCEmail = officerCCEmail;
				for (int i = 0; i < selectedCCOfficers.size(); i++) {
					entityManager.createNativeQuery(insertMeetingScheduleMailCCQuery)
							.setParameter("meetingScheduleId", meetingScheduleId)
							.setParameter("ccUserId", selectedCCOfficers.get(i))
							.setParameter("ccCreatedBy", meeting_schedule.getCcCreatedBy().longValue()).executeUpdate();

				}
			}

			if (!selectedBCCOfficers.isEmpty()) {
				String findOfficerBCCEmailQuery = "SELECT email_id from user_details where user_id IN (:selectedBCCOfficers) ";
				@SuppressWarnings("unchecked")
				List<String> officerBCCEmail = entityManager.createNativeQuery(findOfficerBCCEmailQuery)
						.setParameter("selectedBCCOfficers", selectedBCCOfficers).getResultList();
				logger.info("bccEmail" + officerBCCEmail);
				officerUserBCCEmail = officerBCCEmail;
				for (int i = 0; i < selectedBCCOfficers.size(); i++) {
					entityManager.createNativeQuery(insertMeetingScheduleMailBCCQuery)
							.setParameter("meetingScheduleId", meetingScheduleId)
							.setParameter("bccUserId", selectedBCCOfficers.get(i))
							.setParameter("bccCreatedBy", meeting_schedule.getBccCreatedBy().longValue())
							.executeUpdate();
					rowsAffected++;
					if (i % batchSize == 0) {
						entityManager.flush();
						entityManager.clear();
					}
				}
			}
		}
		return meetingScheduleId;

	}

	@Transactional
	public Integer updateMeetingFlag(BigInteger meetingId) {
		String sql = "update application.meeting_schedule set after_meeting_flag='1' where meeting_schedule_id=:meetingSheduleId";
		return entityManager.createNativeQuery(sql).setParameter("meetingSheduleId", meetingId).executeUpdate();
	}

	@Transactional
	public BigInteger getTotalMeetingScheduleCount(String applicationNo, String khataNo, Date meetingDate,
			BigInteger meetingId) {
		StringBuilder addConditionSqlAuction = new StringBuilder();
		StringBuilder addConditionSqlLand = new StringBuilder();
		if (applicationNo != null && !applicationNo.isEmpty()) {
			addConditionSqlAuction.append("AND bfma.bidder_form_m_application_number='" + applicationNo + "'");
			addConditionSqlLand.append("AND la1.application_no='" + applicationNo + "'");
		}
		if (meetingDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(meetingDate);
			String dateCondition = "AND DATE(ms.meeting_date) ='" + formattedDate + "'";
			addConditionSqlAuction.append(dateCondition);
			addConditionSqlLand.append(dateCondition);
		}
		String joinblesForAuction = "";
		String joinblesForLand = "";
		if (khataNo != null && !khataNo.isEmpty()) {
			String joinAuction = " JOIN application.tender_auction ta ON ta.tender_auction_id = bfma.tender_auction_id "
					+ " JOIN application.auction_plot ap ON ap.auction_plot_id = ta.auction_plot_id "
					+ " JOIN application.auction_plot_details apd ON ap.auction_plot_id = apd.auction_plot_id "
					+ " JOIN land_bank.plot_information pii ON(pii.plot_code=apd.plot_code) "
					+ " JOIN land_bank.khatian_information ki ON(ki.khatian_code=pii.khatian_code)";
			String joinLand = " JOIN public.land_schedule ls1 ON(ls1.land_application_id=la1.land_application_id) "
					+ " JOIN land_bank.plot_information pii ON(pii.plot_code=ls1.plot_code) "
					+ " JOIN land_bank.khatian_information ki ON(ki.khatian_code=pii.khatian_code)";
			joinblesForAuction = joinAuction;
			joinblesForLand = joinLand;
			String auctionCondition = "AND ki.khata_no='" + khataNo + "'";
			String landCondition = "AND ki.khata_no='" + khataNo + "'";
			addConditionSqlAuction.append(auctionCondition);
			addConditionSqlLand.append(landCondition);
		}

		String nativeQuery = "WITH MeetingScheduleResults AS ( " + " SELECT DISTINCT ms.meeting_schedule_id "
				+ " FROM application.meeting_schedule AS ms  "
				+ " JOIN public.m_meeting_level AS ml ON ms.meeting_level_id = ml.meeting_level_id "
				+ " JOIN application.meeting_schedule_applicant msa ON (ms.meeting_schedule_id = msa.meeting_schedule_id)  "
				+ " JOIN public.land_application la1 ON(la1.land_application_id=msa.land_application_id)"
				+ joinblesForLand
				+ " WHERE ms.status = '0' AND ms.meeting_id =:meetingId AND ms.meeting_no IS NOT NULL AND msa.land_application_id IS NOT NULL "
				+ addConditionSqlLand 
				+ " UNION " 
				+ " SELECT DISTINCT ms.meeting_schedule_id "
				+ " FROM application.meeting_schedule AS ms  "
				+ " JOIN public.m_meeting_level AS ml ON ms.meeting_level_id = ml.meeting_level_id "
				+ " JOIN application.meeting_schedule_applicant msa ON (ms.meeting_schedule_id = msa.meeting_schedule_id) "
				+ " JOIN application.bidder_form_m_application bfma ON (bfma.bidder_form_m_application_id = msa.bidder_form_m_application_id) "
				+ joinblesForAuction
				+ " WHERE ms.status = '0' AND ms.meeting_id =:meetingId AND ms.meeting_no IS NOT NULL AND msa.bidder_form_m_application_id IS NOT  NULL "
				+ addConditionSqlAuction 
				+ " UNION select DISTINCT ms.meeting_schedule_id   "
				+ "FROM application.meeting_schedule AS ms     "
				+ "JOIN public.m_meeting_level AS ml  ON ms.meeting_level_id = ml.meeting_level_id   "
				+ "JOIN application.meeting_schedule_applicant msa  ON (ms.meeting_schedule_id = msa.meeting_schedule_id)    "
				+ "WHERE  ms.status = '0'   AND ms.meeting_no IS NOT NULL  AND ms.meeting_id=:meetingId AND msa.direct_meeting_auction_flag=1  "
				+ " )SELECT COUNT(DISTINCT meeting_schedule_id) AS total_count "
				+ " FROM MeetingScheduleResults";

		Query query = entityManager.createNativeQuery(nativeQuery).setParameter("meetingId", meetingId);
		@SuppressWarnings("unchecked")
		List<Object> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			return ((BigInteger) resultList.get(0));
		} else {
			return null;
		}
	}

	@Transactional
	public List<Object[]> getAuctionApplicantRecord(BigInteger meetingSheduleId) {
		Query nativeQuery = entityManager
				.createNativeQuery("SELECT (SELECT (SELECT full_name FROM public.citizen_profile_details "
						+ "WHERE citizen_profile_details_id = bfma.user_id) "
						+ "FROM application.bidder_form_m_application bfma "
						+ "WHERE bfma.bidder_form_m_application_id = msa.bidder_form_m_application_id) AS applicantName, "
						+ "(SELECT bfma.bidder_form_m_application_number "
						+ "FROM application.bidder_form_m_application bfma "
						+ "WHERE bfma.bidder_form_m_application_id = msa.bidder_form_m_application_id) AS applicantNo, "
						+ "msa.plot_no ,msa.bidder_form_m_application_id , ms.meeting_schedule_id ,msa.approval_status,ms.meeting_level_id,ms.plots_ids, "
						+ "(select plot_no from land_bank.plot_information where plot_code=msa.plot_no) as plotCode "
						+ "FROM application.meeting_schedule ms "
						+ "JOIN application.meeting_schedule_applicant msa ON (ms.meeting_schedule_id = msa.meeting_schedule_id) "
						+ "WHERE ms.meeting_schedule_id = :meetingScheduleId AND ms.status = '0' AND msa.status = '0'")
				.setParameter("meetingScheduleId", meetingSheduleId);
		@SuppressWarnings("unchecked")
		List<Object[]> results = nativeQuery.getResultList();
		return results;

	}

	@Transactional
	public Integer updateLandApplicationMeetingFlag(BigInteger meetingId) {
		String nativeQuery = "  WITH SelectedLandApplications AS (    " + "SELECT msa.land_application_id,    "
				+ "msa.plot_no " + "FROM application.meeting_schedule_applicant msa      "
				+ "JOIN application.meeting_schedule ms ON (ms.meeting_schedule_id = msa.meeting_schedule_id)     "
				+ "WHERE ms.meeting_schedule_id =:meetingId AND ms.status = '0' AND msa.status = '0')      "
				+ "UPDATE public.land_schedule ls     " + "SET meeting_schedule_flag = '0'     "
				+ "FROM SelectedLandApplications sla     "
				+ "WHERE ls.land_application_id = sla.land_application_id    " + "AND ls.plot_code=sla.plot_no ";

		Query query = entityManager.createNativeQuery(nativeQuery).setParameter("meetingId", meetingId);
		return query.executeUpdate();
	}

	@Transactional
	public BigInteger getCountForMeetingSchedule(BigInteger meetingScheduleId) {
		String sqlQuery = "SELECT COUNT(*) FROM application.meeting_schedule "
				+ "WHERE meeting_schedule_id = :meetingScheduleId AND meeting_level_id = 1";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("meetingScheduleId", meetingScheduleId);
		return ((BigInteger) query.getSingleResult());
	}

	@Transactional
	public Short getMeetingLevleId(BigInteger meetingId) {
		String sqlQuery = "SELECT meeting_level_id FROM application.meeting_schedule WHERE meeting_schedule_id = :id";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("id", meetingId);
		Object result = query.getSingleResult();
		Short plotCode = (result != null) ? (Short) result : null;
		return plotCode;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getLevleIdThroughData(BigInteger meetingId, Integer levleId) {
		String sql = " SELECT\r\n" + "        DISTINCT ms.meeting_schedule_id as meetingId,\r\n"
				+ "        msa.plot_no as plotNo,\r\n" + "        msa.land_application_id as landAppId,\r\n"
				+ "        CASE \r\n" + "            WHEN msa.land_application_id IS NOT NULL THEN (SELECT\r\n"
				+ "                applicant_name  \r\n" + "            FROM\r\n"
				+ "                public.land_application \r\n" + "            WHERE\r\n"
				+ "                land_application_id = msa.land_application_id LIMIT 1)     \r\n"
				+ "            ELSE (select\r\n" + "                (SELECT\r\n" + "                    full_name \r\n"
				+ "                FROM\r\n" + "                    public.citizen_profile_details \r\n"
				+ "                WHERE\r\n" + "                    citizen_profile_details_id=bfma.user_id)  \r\n"
				+ "            from\r\n" + "                application.bidder_form_m_application bfma \r\n"
				+ "            where\r\n" + "                bfma.bidder_form_m_application_id=bfma.user_id\r\n"
				+ "            )     \r\n" + "        END as landAppName , (\r\n" + "            SELECT\r\n"
				+ "                application_no \r\n" + "            FROM\r\n"
				+ "                public.land_application \r\n" + "            WHERE\r\n"
				+ "                land_application_id = msa.land_application_id LIMIT 1\r\n"
				+ "        ) as landAppNo,  ms.meeting_level_id as mettingLevleId,  ms.plots_ids as plotIds , msa.approval_status as approvalStatus  \r\n"
				+ "    FROM\r\n" + "        application.meeting_schedule ms  \r\n" + "    JOIN\r\n"
				+ "        application.meeting_schedule_applicant msa \r\n" + "            ON (\r\n"
				+ "                ms.meeting_schedule_id = msa.meeting_schedule_id\r\n" + "            )  \r\n"
				+ "    WHERE\r\n" + "         ms.status = '0' \r\n" + "        AND msa.status = '0'\r\n"
				+ "		AND meeting_id=:meetingId  \r\n" + "        AND meeting_level_id=:levleId  \r\n"
				+ "    GROUP BY\r\n" + "        ms.meeting_schedule_id,\r\n" + "        msa.plot_no,\r\n"
				+ "        msa.land_application_id,\r\n" + "        msa.approval_status \r\n" + "		\r\n"
				+ "		\r\n" + "		";

		Query query = entityManager.createNativeQuery(sql).setParameter("meetingId", meetingId).setParameter("levleId",
				levleId);
		return query.getResultList();
	}

	@Transactional
	public List<Object[]> getPlotsRecord(Integer pageSize, Integer offset) {
		String sql = "  select   " + "ms.meeting_id,ms.meeting_no,ms.meeting_schedule_id, "
				+ "CAST(JSON_AGG(json_build_object(  " + "'districtName',dm1.district_name,  "
				+ "'tahasilName',tm1.tahasil_name,  " + "'villageName',vm1.village_name,  "
				+ "'khata_no',ki1.khata_no,  " + "'plot_no',pi1.plot_no  " + ")) AS VARCHAR) as plotDetails " + "from  "
				+ "(select A.meeting_id, "
				+ "(select aa.meeting_schedule_id from application.meeting_schedule_applicant aa "
				+ "JOIN application.meeting_schedule Z ON(Z.meeting_schedule_id=aa.meeting_schedule_id) "
				+ "where land_application_id >0 and Z.meeting_id=A.meeting_id AND Z.status='0'  "
				+ "order by Z.meeting_level_id desc limit 1) from application.meetings A " 
				+ " UNION "
				+ "select A.meeting_id, "
				+ "(select aa.meeting_schedule_id from application.meeting_schedule_applicant aa "
				+ "JOIN application.meeting_schedule Z ON(Z.meeting_schedule_id=aa.meeting_schedule_id) "
				+ "where aa.bidder_form_m_application_id >0 and Z.meeting_id=A.meeting_id AND Z.status='0'  "
				+ "order by Z.meeting_level_id desc limit 1) from application.meetings A   "
				+ " UNION  "
				+ "SELECT  DISTINCT ON (A.meeting_id) A.meeting_id,  A.meeting_schedule_id  "
				+ "FROM application.meeting_schedule A\r\n"
				+ "JOIN application.meeting_schedule_applicant msa ON(msa.meeting_schedule_id=A.meeting_schedule_id) "
				+ "WHERE msa.direct_meeting_auction_flag=1 AND msa.status='0' AND A.status='0' ) Z1  "
				+ "JOIN application.meeting_schedule ms ON Z1.meeting_schedule_id=ms.meeting_schedule_id "
				+ "JOIN application.meeting_schedule_applicant msa ON(ms.meeting_schedule_id=msa.meeting_schedule_id) "
				+ "JOIN land_bank.plot_information pi1 ON pi1.plot_code = msa.plot_no  "
				+ "JOIN land_bank.khatian_information ki1 ON ki1.khatian_code=pi1.khatian_code  "
				+ "JOIN land_bank.village_master vm1 ON vm1.village_code=ki1.village_code  "
				+ "JOIN land_bank.tahasil_master tm1 ON tm1.tahasil_code=vm1.tahasil_code  "
				+ "JOIN land_bank.district_master dm1 ON dm1.district_code=tm1.district_code  "
				+ "where Z1.meeting_schedule_id is not null AND ms.status='0' AND msa.status='0' "
				+ "GROUP BY ms.meeting_id,ms.meeting_no,ms.meeting_schedule_id  ORDER BY ms.meeting_id DESC  " + "  LIMIT :pageSize  OFFSET :offset";
		Query query = entityManager.createNativeQuery(sql).setParameter("pageSize", pageSize).setParameter("offset",
				offset);
		@SuppressWarnings("unchecked")
		List<Object[]> result = query.getResultList();
		return result;

	}

	@Transactional
	public BigInteger getRowCountOfDistinctPlotMeetings() {
		String sqlQuery = "SELECT COUNT(*) FROM (   " + "SELECT    "
				+ "ms.meeting_id, ms.meeting_no, ms.meeting_schedule_id,   " + "CAST(JSON_AGG(json_build_object(    "
				+ "'districtName',dm1.district_name,    " + "'tahasilName',tm1.tahasil_name,    "
				+ "'villageName',vm1.village_name,    " + "'khata_no',ki1.khata_no,    " + "'plot_no',pi1.plot_no    "
				+ ")) AS VARCHAR) as plotDetails   " + "FROM    " + "(SELECT A.meeting_id,   "
				+ "(SELECT aa.meeting_schedule_id FROM application.meeting_schedule_applicant aa   "
				+ "JOIN application.meeting_schedule Z ON(Z.meeting_schedule_id=aa.meeting_schedule_id)   "
				+ "WHERE land_application_id >0 AND Z.meeting_id=A.meeting_id AND Z.status='0'    "
				+ "ORDER BY Z.meeting_level_id DESC LIMIT 1) FROM application.meetings A   " 
				+ "UNION   "
				+ "SELECT A.meeting_id,   "
				+ "(SELECT aa.meeting_schedule_id FROM application.meeting_schedule_applicant aa   "
				+ "JOIN application.meeting_schedule Z ON(Z.meeting_schedule_id=aa.meeting_schedule_id)   "
				+ "WHERE aa.bidder_form_m_application_id >0 AND Z.meeting_id=A.meeting_id AND Z.status='0'    "
				+ "ORDER BY Z.meeting_level_id DESC LIMIT 1) FROM application.meetings A "
				+ " UNION  "
				+ "SELECT  DISTINCT ON (A.meeting_id) A.meeting_id,  A.meeting_schedule_id  "
				+ "FROM application.meeting_schedule A\r\n"
				+ "JOIN application.meeting_schedule_applicant msa ON(msa.meeting_schedule_id=A.meeting_schedule_id) "
				+ "WHERE msa.direct_meeting_auction_flag=1 AND msa.status='0' AND A.status='0' ) Z1  "
				+ "JOIN application.meeting_schedule ms ON Z1.meeting_schedule_id=ms.meeting_schedule_id   "
				+ "JOIN application.meeting_schedule_applicant msa ON(ms.meeting_schedule_id=msa.meeting_schedule_id)   "
				+ "JOIN land_bank.plot_information pi1 ON pi1.plot_code = msa.plot_no    "
				+ "JOIN land_bank.khatian_information ki1 ON ki1.khatian_code=pi1.khatian_code    "
				+ "JOIN land_bank.village_master vm1 ON vm1.village_code=ki1.village_code    "
				+ "JOIN land_bank.tahasil_master tm1 ON tm1.tahasil_code=vm1.tahasil_code    "
				+ "JOIN land_bank.district_master dm1 ON dm1.district_code=tm1.district_code    "
				+ "WHERE Z1.meeting_schedule_id IS NOT NULL AND ms.status='0' AND msa.status='0'   "
				+ "GROUP BY ms.meeting_id, ms.meeting_no, ms.meeting_schedule_id   " + ") AS subquery  ";
		Query query = entityManager.createNativeQuery(sqlQuery);
		BigInteger rowCount = (BigInteger) query.getSingleResult();
		return rowCount;
	}

	@Transactional
	public Integer getUpdatePlotForAuction(String plotCode,BigInteger meetingSheduleId) {
		Query query = entityManager.createNativeQuery(
				  " UPDATE application.meeting_schedule_applicant SET metting_auction_status = 1 WHERE plot_no = :plotCode  "
				+ " AND status='0' AND  meeting_schedule_id=:meetingSheduleId  ")
				.setParameter("plotCode", plotCode).setParameter("meetingSheduleId", meetingSheduleId);
		return query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getHistory(BigInteger meetingId) {
		List<Object[]> resultList = new ArrayList<>();
		try {
			String nativeQuery = " select " + "ms.meeting_id, " + "ms.meeting_schedule_id, " + "ms.venue, "
					+ "ms.meeting_date, " + "ms.meeting_purpose, " + "ms.meeting_level_id, "
					+ "msa.meeting_schedule_applicant_id, " + "msa.land_application_id, " + "msa.plot_no, "
					+ "msa.approval_status, " + "CASE WHEN msa.land_application_id IS NOT NULL THEN "
					+ "(SELECT applicant_name FROM public.land_application WHERE land_application_id = msa.land_application_id LIMIT 1) "
					+ "WHEN msa.bidder_form_m_application_id IS NOT NULL THEN "
					+ "(SELECT (select full_name  from public.citizen_profile_details where user_id=citizen_profile_details_id)  "
					+ "FROM application.bidder_form_m_application WHERE bidder_form_m_application_id = msa.bidder_form_m_application_id LIMIT 1) "
					+ "ELSE null  " + "END AS fullname, " + "CASE WHEN land_application_id IS NOT NULL THEN "
					+ "(select application_no from public.land_application where "
					+ "land_application_id=msa.land_application_id limit 1)  "
					+ "WHEN bidder_form_m_application_id IS NOT NULL THEN "
					+ "(select bidder_form_m_application_number FROM application.bidder_form_m_application  "
					+ "WHERE bidder_form_m_application_id = msa.bidder_form_m_application_id ) "
					+ "END as applicationno, "
					+ "(select plot_no from land_bank.plot_information where plot_code=msa.plot_no) as plotNo , metting_auction_status,msa.direct_meeting_auction_flag   "
					+ "from application.meeting_schedule ms    "
					+ "join application.meeting_schedule_applicant msa  on ms.meeting_schedule_id = msa.meeting_schedule_id    "
					+ "where  ms.meeting_id =:meetingId  AND ms.status='0'  AND msa.status='0'     "
					+ "ORDER BY ms.meeting_id  ";
			Query query = entityManager.createNativeQuery(nativeQuery).setParameter("meetingId", meetingId);
			log.info("query starts executing for fetching details of meeting");
			resultList = query.getResultList();
		} finally {
			entityManager.close();
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getplotCode(BigInteger meetingId) {
		String plotDetailsinMs = "select ms.plots_ids,ms.meeting_schedule_id from application.meeting_schedule ms where meeting_id=:meetingId";
		Query query = entityManager.createNativeQuery(plotDetailsinMs).setParameter("meetingId", meetingId);
		return query.getResultList();

	}

	@Transactional
	public Integer updateplotForAuction(BigInteger meetingId) {
		String nativeQuery = " UPDATE application.land_allotement_for_auction\r\n"
				+ "SET create_meeting_for_auction_flag = 0\r\n"
				+ "WHERE plot_code IN (SELECT plot_no FROM application.meeting_schedule_applicant \r\n"
				+ "WHERE meeting_schedule_id =:meetingId  and status='0' ) ";

		Query query = entityManager.createNativeQuery(nativeQuery).setParameter("meetingId", meetingId);
		return query.executeUpdate();
	}
	
	
	@Transactional
	public Object getPlot(BigInteger meetingId) {
		String plotIdQuery = "SELECT STRING_AGG(CONCAT('''', plot_no, ''''), ',') AS plotCode FROM application.meeting_schedule_applicant WHERE status = '0' AND meeting_schedule_id = :meetingId";
		return entityManager.createNativeQuery(plotIdQuery).setParameter("meetingId", meetingId).getSingleResult();
	}
	
	@Transactional
	public Object getPlotLevle(BigInteger meetingId) {
		String plotIdQuery = "SELECT STRING_AGG(CONCAT('''', plot_no, ''''), ',') AS plotCode FROM application.meeting_schedule_applicant msa   "
				+ "JOIN application.meeting_schedule ms ON ms.meeting_schedule_id=msa.meeting_schedule_id "
				+ "WHERE msa.status = '0' AND ms.status='0' AND msa.meeting_schedule_id = :meetingId AND ms.meeting_level_id=4 ";
		return entityManager.createNativeQuery(plotIdQuery).setParameter("meetingId", meetingId).getSingleResult();
	}
	
	@Transactional
	public Integer updateGoForAuctionFlag(BigInteger meetingSheId) {
		String updateQuery = "update application.auction_plot_details set go_to_auction_flag=0  "
				             + "where meeting_schedule_id=:meetingSheId and deleted_flag='0'";
		return entityManager.createNativeQuery(updateQuery)
				.setParameter("meetingSheId", meetingSheId).executeUpdate();
	}
	
	@Transactional
	public BigInteger getCountToTrueOrFalseMeetings(BigInteger meetingId, Short levleId) {
		String countQuery="select count(*) from application.meeting_schedule where meeting_id=:meetingId AND meeting_level_id=:levleId";
		return (BigInteger) entityManager.createNativeQuery(countQuery)
				.setParameter("meetingId", meetingId).setParameter("levleId", levleId).getSingleResult();
	}
}
