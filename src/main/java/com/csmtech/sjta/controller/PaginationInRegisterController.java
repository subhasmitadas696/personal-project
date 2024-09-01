package com.csmtech.sjta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csmtech.sjta.dto.PaginationInRegiserMailRespones;
import com.csmtech.sjta.dto.PaginationInRegisterDTO;
import com.csmtech.sjta.dto.PaginationInRegisterDtoResponse;
import com.csmtech.sjta.repository.PaginationInRegisterClassRepository;

@RestController
@RequestMapping("/mainpagination")

public class PaginationInRegisterController {

	@Autowired
	private PaginationInRegisterClassRepository repo;
	
	

	@PostMapping("/registerpage")
	public ResponseEntity<PaginationInRegiserMailRespones> getPaginationData(@RequestBody PaginationInRegisterDtoResponse res) {
		Integer countint=repo.getTotalUserCount();
		List<PaginationInRegisterDTO> getdtodata=repo.getUserDetailsPage(res.getPageNumber(), res.getPageSize());
		return ResponseEntity.ok(new PaginationInRegiserMailRespones(getdtodata,countint));
	}
}
