package com.csmtech.sjta.mobile.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csmtech.sjta.entity.Bidderregistrara;
import com.csmtech.sjta.mobile.entity.NotificationDetails;

public interface NotificationDetailsRepository extends JpaRepository<NotificationDetails, BigInteger>{

	@Query(value ="select * from application.notification_details where user_id =:userId and read_mode ='N' and deleted_flag = '0'", nativeQuery = true)
	List<NotificationDetails> findNotificationDetails(BigInteger userId);
	
	@Query(value ="select * from application.notification_details where notification_id =:notificationId and deleted_flag ='0'", nativeQuery = true)
	NotificationDetails findByNotificationId(BigInteger notificationId);

	@Query(value ="select distinct a.updated_by as userId,la.land_application_id,la.application_no,la.applicant_name,la.mobile_no,\r\n"
			+ "			d.district_name,t.tahasil_name,m.village_name,k.khata_no,k.khatian_code,\r\n"
			+ "			a.created_on, d.district_code, t.tahasil_code, m.village_code,a.pending_at_role_id FROM public.land_application la\r\n"
			+ "			LEFT join application.notification_details n on n.user_id = la.created_by \r\n"
			+ "			LEFT join user_details u on la.created_by = u.user_id \r\n"
			+ "			INNER JOIN land_bank.district_master d ON la.district_code = d.district_code \r\n"
			+ "			INNER JOIN land_bank.tahasil_master t ON la.tehsil_code = t.tahasil_code\r\n"
			+ "			INNER JOIN land_bank.village_master m ON la.village_code = m.village_code \r\n"
			+ "			INNER JOIN land_bank.khatian_information k ON la.khatian_code = k.khatian_code \r\n"
			+ "			INNER JOIN land_application_approval a ON la.land_application_id = a.land_application_id \r\n"
			+ "			WHERE a.pending_at_role_id != 0 and a.application_status_id in (1,2,3) \r\n"
			+ "			GROUP BY a.updated_by,la.land_application_id, d.district_name, t.tahasil_name, m.village_name, k.khata_no,\r\n"
			+ "			k.khatian_code, a.created_on , d.district_code, t.tahasil_code, m.village_code,a.pending_at_role_id",nativeQuery = true)
	List<Object[]> fetchLandApplicationDetailsToSendNotifications();
	
	@Query(value ="select u.user_id from user_details u inner join user_role ur \r\n"
			+ "on u.user_id = ur.user_id where ur.role_id =:roleId and u.status = '0'",nativeQuery = true)
	List<BigInteger> fetchUserDetailsOnRoleId(BigInteger roleId);

	
	@Query(value ="select cd.citizen_profile_details_id,ta.tender_auction_id from  public.citizen_profile_details cd join \r\n"
			+ "application.bidder_form_m_application bfm ON(cd.citizen_profile_details_id= bfm.user_id)\r\n"
			+ "JOIN application.tender_auction ta on bfm.tender_auction_id = ta.tender_auction_id\r\n"
			+ "where bfm.payment_status != '1'   AND NOW() <= ta.form_m_submit_end_date \r\n"
			+ "AND NOW() >= ta.form_m_submit_start_date and bfm.deleted_flag = '0'",nativeQuery = true)
	List<Object[]> findCitizens();

	@Query(value ="select * from application.bidder_form_m_application bfm where approval_status = 'N' \r\n"
			+ "and approval_datetime is null and bidder_form_m_application_number is not null and deleted_flag = '0'",nativeQuery = true)
	List<Bidderregistrara> findListForFormM();
	
//	@Query(value ="select plot_no from ")
//	String findPlotNo(String plotCode);
	
	

}
