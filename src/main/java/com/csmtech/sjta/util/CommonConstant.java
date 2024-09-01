package com.csmtech.sjta.util;

import org.springframework.stereotype.Component;

@Component
public class CommonConstant {

	private CommonConstant() {
		super();
	}

	public static final String STATUS_KEY = "status";
	public static final String MESSAGE_KEY = "message";
	public static final String ERROR_MESSAGE = "errMsg";
	public static final String DATA_FOUND = "DATA FOUND";
	public static final String NO_DATA_FOUND = "NO DATA FOUND";
	public static final String ERROR = "error";
	public static final String METHOD = "method";
	public static final String SERVICE_CLASS_NAME = "com.csmtech.sjta.serviceImpl.";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_MS_EXCEL= "application/vnd.ms-excel";
	public static final String RESULT = "result";
	public static final String DATA = "data";
	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	public static final String COUNT = "count";
	public static final String INTID = "intId";
	public static final String REQUEST_DATA = "REQUEST_DATA";
	public static final String REQUEST_TOKEN = "REQUEST_TOKEN";
	public static final String RESPONSE_DATA = "RESPONSE_DATA";
	public static final String RESPONSE_TOKEN = "RESPONSE_TOKEN";
	public static final String NO_MAIL_MESSAGE = "Mail are not send to the user so mail service won't work";
	public static final String MAIL_SENT_MESSAGE = "Mail sent successfully ";
	public static final String LAND_APPLICATION = "Land Application";
	public static final String HTML_HEADER = "<!DOCTYPE html>  <html>  <head>  ";
	public static final String HTML_TITLE = "    <title> Welcome to Shree Jagnath Land Management Information</title>   </head> ";
	public static final String HTML_BODY_START = " <body>  <p>Dear User,</p>";
	public static final String HTML_BODY_END = "<p>Thanks & Regards,<br>SJTA</p>";
	public static final String HTML_FOOT = "    </body> </html>  ";
	public static final String TIME_EXPIRED = "timeExpired";
	public static final String MULTI_RESULT = "multiResult";
	public static final String ERROR_MSG = "Fetching record failed.Some error occured";
	public static final String INVALID_USERNAME_PASSWORD ="Invalid username/password";
	public static final String NO_RECORD_FOUND ="No Record Found";
	public static final String ATTACHMENT_FILENAME ="attachment; filename=";
	public static final String REVERSE_RECORD = "reverseRecord";
	public static final String NO_RECORD_VILLAGE_FOUND = "No Record Found Village";
	public static final Integer SUCCESS_CODE = 200;
	public static final Integer NO_RECORD = 404;
	public static final String INVALID_TIME_LOGIN ="Invalid Time";
	public static final String TIME_LEFT ="TimeLeft";

	

}
