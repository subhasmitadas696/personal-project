/**
 * 
 */
package com.csmtech.sjta.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.CitizenInfoDTO;
import com.csmtech.sjta.dto.UserDetailsDTO;
import com.csmtech.sjta.dto.ViewCitizenPaginationDTO;
import com.csmtech.sjta.service.ShowCitizenInfoService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auth Prasanta Kumar Sethi
 */

@Data
class search {
	String searchKeyword;
	Integer pageNumber;
	Integer pageSize;

}

@RestController
@RequestMapping("/showcitizenInfo")

@Slf4j
public class ShowCitizenInfoController {

	@Autowired
	private ShowCitizenInfoService showCitizenInfoService;

	@PostMapping("/getcitizeninfo")
	public ResponseEntity<?> getDetails(CitizenInfoDTO request) {
		JSONObject responsejson = new JSONObject();
		log.info("getcitizen info started running.....!!");

		List<CitizenInfoDTO> response = showCitizenInfoService.getCitizenInfo();
		if (response.isEmpty()) {
			responsejson.put("status", 401);
			responsejson.put("result", "No Record Found");
			return ResponseEntity.ok(response.toString());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/search")
	public List<UserDetailsDTO> searchUsers(@RequestBody search ser) {
		log.info("search started ..... !!");
		return showCitizenInfoService.searchUsers(ser.getSearchKeyword());
	}

	@PostMapping("/pagination")
	public ResponseEntity<ViewCitizenPaginationDTO> getPaginationSearchData(@RequestBody search res) {
		if (res.getSearchKeyword() != null && !res.getSearchKeyword().isEmpty()) {
			log.info("land search pagination method started..");
			Integer countint = showCitizenInfoService.getSearchedApplicantCount(res.getSearchKeyword());
			List<UserDetailsDTO> getdtodata = showCitizenInfoService.getSearchUserDetailsPage(res.getSearchKeyword(),
					res.getPageNumber(), res.getPageSize());
			return ResponseEntity.ok(new ViewCitizenPaginationDTO(getdtodata, countint));
		} else {
			log.info("view citizen pagination method started..");
			Integer countint = showCitizenInfoService.getTotalApplicantCount();
			List<UserDetailsDTO> getdtodata = showCitizenInfoService.getUserDetailsPage(res.getPageNumber(),
					res.getPageSize());
			return ResponseEntity.ok(new ViewCitizenPaginationDTO(getdtodata, countint));
		}
	}

}
