package com.csmtech.sjta.util;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailUtil {
	
	@Value("${app.url}")
	private String appurl;

	@Autowired
	private JavaMailSender javaMailSender;

	public Integer sendEmail(Integer mailType, Short status, JSONObject mailData, String subject, List<String> recipientEmails,
			List<String> ccEmails, List<String> bccEmails) { 
		log.info(":: sendEmail");

		try {
			String content = mailContent(mailType, status, mailData);

			if (!content.isEmpty()) {
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);

				for (String recipient : recipientEmails) {
					helper.addTo(recipient);
				}

				helper.setSubject(subject);
				helper.setText(content, true);

				if (ccEmails != null && !ccEmails.isEmpty()) {
					for (String ccEmail : ccEmails) {
						helper.addCc(ccEmail);
					}
				}

				if (bccEmails != null && !bccEmails.isEmpty()) {
					for (String bccEmail : bccEmails) {
						helper.addBcc(bccEmail);
					}
				}

				javaMailSender.send(message);
				log.info("Email sent successfully!");
				return 1;
			} else {
				log.error("No Mail Content");
				return 0;
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			log.info("Failed to send email.");
			return 0;
		}
	}

	public String mailContent(Integer mailType, Short status, JSONObject mailData) {
		String mailHeader = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>SJLIS Template</title>\r\n"
				+ "</head>\r\n"
				+ "<body style=\"margin: 0;padding: 0;box-sizing: border-box;\">\r\n"
				+ "    <table width=\"100%\"\r\n"
				+ "        style=\"max-width: 702px; margin: 0px auto;box-shadow: 0 0 7px 0 rgb(131 131 131 / 50%);font-size: 16px;font-weight: 500;font-family: Verdana;    color: #424242; line-height: 1.5; -webkit-print-color-adjust: exact;\"\r\n"
				+ "        border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "        <tbody>\r\n"
				+ "            <tr>\r\n"
				+ "                <td\r\n"
				+ "                    style=\"background-image: "+ appurl +"/banner.jpg;background-position: center;background-repeat: no-repeat;-webkit-print-color-adjust: exact;text-align: center;height: 277px;\">\r\n"
				+ "                    <img style=\"margin-bottom: 14px;\" src=\""+ appurl +"/logo.png\" alt=\"logo\">\r\n"
				+ "                    <p\r\n"
				+ "                        style=\"margin:0;font-size: 29px;text-align: center;color: #fff;line-height: 40px;\">\r\n"
				+ "                        Shree Jagannath <br> <span style=\"font-size: 23px;text-align: center;\">Land Information & Management System</span></p>\r\n"
				+ "                </td>\r\n"
				+ "            </tr>";
		
		String mailFooter = " <tr>\r\n"
				+ "                <td style=\" padding: 25px 50px 45px 50px; display: block;border-bottom: 8px solid #153d3a;\">\r\n"
				+ "                    <p style=\"margin-top: 0;\">\r\n"
				+ "                        Thanks & Regards, <br>\r\n"
				+ "                        <span> Shree Jagannatha Temple Administration</span>\r\n"
				+ "                    </p>\r\n"
				+ "                </td>\r\n"
				+ "            </tr>\r\n"
				+ "        </tbody>\r\n"
				+ "    </table>\r\n"
				+ "</body>\r\n"
				+ "</html>";
		
		String mailBody = "";
		
		String content = "";
	
		if (mailType == 1) { //land registration
			mailBody = "<tr>\r\n"
					+ "	<td style=\"max-width: 596px; margin: 95px auto 0px auto; display: block;\">\r\n"
					+ "		<p>Dear "+ mailData.getString("fullName") +",</p>\r\n"
					+ "		<p style=\"margin-top: 25px;\"> Your registration is completed successfully. Please login to apply land.</p>\r\n"
					+ "		</p>\r\n"
					+ "	</td>\r\n"
					+ "</tr>";
		} else if (mailType == 2) { //land application
			String msg = "";
			
			if (status == 1) {
				msg = "Your Application Submitted to SJTA.";
			} else if (status == 2) {
				msg = "Your Application Forward to Land Officer.";
			} else if (status == 3) {
				msg = "Your Application Forward to Deputy Administartor.";
			} else if (status == 4) {
				msg = "Your Application was Approved.";
			} else if (status == 5) {
				msg = "Your Application was Rejected By Dealing Assistant.";
			} else if (status == 6) {
				msg = "Your Application was Rejected By Land Officer.";
			}else if (status == 7) {
				msg = "Your Application was Rejected By Deputy Administartor.";
			}else if (status == 8) {
				msg = "Your Application was Revert to Citizen By Dealing Assistant.";
			}else if (status == 9) {
				msg = "Your Application was Revert to Citizen By Land officer.";
			}else if (status == 10) {
				msg = "Your Application was Revert to Citizen By Deputy Administartor.";
			}else if (status == 11) {
				msg = "Your Application was Revert to Dealing Assistant.";
			}else if (status == 12) {
				msg = "Your Application was Revert to Land Officer.";
			}else if (status == 18) {
				msg = "Your Application Re-Submitted to SJTA.";
			}
			
			mailBody = "<tr>\r\n"
					+ "	<td style=\"max-width: 596px; margin: 95px auto 0px auto; display: block;\">\r\n"
					+ "		<p>Dear "+ mailData.getString("fullName") +",</p>\r\n"
					+ "		<p style=\"margin-top: 25px;\"> "+ msg +" Kindly login to view details.</p>\r\n"
					+ "		</p>\r\n"
					+ "	</td>\r\n"
					+ "</tr>";
			
		} else if (mailType == 3) { //Grievance

		} else if (mailType == 4) { //meeting
			mailBody = "<tr>\r\n"
					+ "	<td style=\"max-width: 596px; margin: 95px auto 0px auto; display: block;\">\r\n"
					+ "		<p>Dear All,</p>\r\n"
					+ "		<p style=\"margin-top: 25px;\"> A meeting has been scheduled as follows: </p>\r\n"
					+ "	</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "	<td\r\n"
					+ "		style=\"max-width: 562px; margin: 30px auto 0px auto; display: block;font-size: 18px;font-weight: 900;padding: 14px 18px;text-align: center;background-color: #e2ae00;border-top-left-radius: 4px;border-top-right-radius: 4px;box-shadow: 0 -2px 7px 0px rgb(218 218 218 / 50%); color: #000;\">Venue: <span style=\"font-weight: 600;color: #000;font-size: 17px;\">"+ mailData.getString("venue") +"</span>\r\n"
					+ "	</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr style=\"display: block;max-width: 598px;margin: 0px auto 35px auto;box-shadow: 0 2px 7px 0px rgb(218 218 218 / 50%);\">\r\n"
					+ "	<td style=\"padding: 25px 72px 25px 22px;margin-top: 10px;\">\r\n"
					+ "		<img style=\"margin-right:14px\" src=\"img/calander.png\" alt=\"\">\r\n"
					+ "		<p style=\"font-weight: 600;margin:0;margin-top: -50px;display: block;margin-left: 70px;\">\r\n"
					+ "			<span>"+ mailData.getString("date") +"</span><br>\r\n"
					+ "			<span>"+ mailData.getString("time") +"</span>\r\n"
					+ "		</p>\r\n"
					+ "	</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "	<td style=\"padding: 0px 50px 55px 50px; display: block;border-bottom: 1px solid #cbcbcb;\">\r\n"
					+ "		<div style=\"margin-top: 30px;\">\r\n"
					+ "			<h3 style=\" font-size: 18px;font-weight: 800;color: #153d3a;margin: 0;\">Agenda</h3>\r\n"
					+ "			<p style=\"margin:0; margin-top: 6px; width:40px;height:2px;background-color:#153d3a;\"></p>\r\n"
					+ "			<p style=\"margin-top: 10px;\">"+ mailData.getString("purpose") +"</p>\r\n"
					+ "		</div>\r\n"
					+ "	</td>\r\n"
					+ "</tr>";
		}
		
		if(!mailBody.isEmpty()) {
			content = mailHeader.concat(mailBody).concat(mailFooter);
		}

		return content;
	}

}
