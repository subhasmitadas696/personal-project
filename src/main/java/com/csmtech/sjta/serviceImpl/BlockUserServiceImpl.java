/**
 * 
 */
package com.csmtech.sjta.serviceImpl;

import org.springframework.stereotype.Service;

import com.csmtech.sjta.service.BlockUserService;

/**
 * @author prasanta.sethi
 */
@Service
public class BlockUserServiceImpl implements BlockUserService {

	@Override
	public Boolean isUserBlocked(Long userId) {

		return isUserBlocked(userId);
	}

}
