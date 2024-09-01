package com.csmtech.sjta.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.csmtech.sjta.dto.ModuleMenuDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MenuBindingRepository {

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	// Chnages By Prasanta
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ModuleMenuDataDTO> getModuleAndMenuByUserId(short roleId)
			throws JsonMappingException, JsonProcessingException {
		String query = "SELECT DISTINCT mm.module_id, mm.module_name, mm.icon_class, mm.sl_no,\r\n"
				+ "(SELECT CAST(JSON_AGG(menuquery.menu_object) AS VARCHAR) \r\n"
				+ "FROM (SELECT json_build_object('menu_id', mme.menu_id,'menuName', mme.menu_name, 'menuurl', mme.url_mapping_name,'sl_no', mme.sl_no) AS menu_object              \r\n"
				+ "FROM m_menu AS mme\r\n" + "JOIN set_permission_role_wise AS msprw ON mme.menu_id = msprw.menu_id\r\n"
				+ "WHERE mme.module_id = mm.module_id AND msprw.role_id =:roleId AND mme.status = '0'  AND msprw.status = '0'  \r\n"
				+ "ORDER BY mme.sl_no) AS menuquery) AS menus\r\n" + "FROM m_module AS mm \r\n"
				+ "JOIN set_permission_role_wise AS sprw ON mm.module_id = sprw.module_id \r\n"
				+ "WHERE mm.status = '0' AND sprw.role_id =:roleId AND sprw.status = '0'\r\n" + "ORDER BY mm.sl_no";

		try {
			Query queryManager = entityManager.createNativeQuery(query).setParameter("roleId", roleId);

			List<Object[]> rows = queryManager.getResultList();

			Map<Short, ModuleMenuDataDTO> moduleMap = new HashMap<>();

			List<ModuleMenuDataDTO> result = new ArrayList<>();

			log.info(" :: getModuleAndMenuByUserId Start putting data !!");
			for (Object[] row : rows) {
				Short moduleId = (Short) row[0];
				String moduleName = (String) row[1];
				String menuIcon = (String) row[2];
				Short slNo = (Short) row[3];
				String menus = (String) row[4];

				ModuleMenuDataDTO moduleData = moduleMap.get(moduleId);
				if (moduleData == null) {
					ObjectMapper objectMapper = new ObjectMapper();
					List<Map<String, String>> menuData = null;
					try {
						if (menus != null) {
							menuData = objectMapper.readValue(menus, new TypeReference<List<Map<String, String>>>() {
							});
						}

					} catch (IOException e) {
					}
					moduleData = new ModuleMenuDataDTO(moduleId, moduleName, menuIcon, slNo, menuData);
					moduleMap.put(moduleId, moduleData);
					result.add(moduleData);
				}

			}
			log.info("::  getModuleAndMenuByUserId  Retturn the result !! ");
			return result;
		} catch (NoResultException e) {
			return new ArrayList<>();
		} finally {
			entityManager.close();
		}
	}
}
