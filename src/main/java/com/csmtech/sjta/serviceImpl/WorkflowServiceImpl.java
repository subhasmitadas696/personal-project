package com.csmtech.sjta.serviceImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.csmtech.sjta.entity.TSetAuthority;
import com.csmtech.sjta.entity.TSetWorkFlow;
import com.csmtech.sjta.repository.ApprovalConfigRepository;
//import com.csmtech.sjta.repository.TSetAuthorityRepository;
import com.csmtech.sjta.repository.TSetWorkFlowRepository;
import com.csmtech.sjta.service.WorkflowService;
import com.csmtech.sjta.util.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TSetWorkFlowRepository tSetWorkFlowRepository;

	@Autowired
	private ApprovalConfigRepository approvalConfig;

	String updateTSetWorkFlow;
	String updateTSetAuthority;

	// getAuthority

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getallApprovalAction() {
		try {
			List<Map<String, Object>> myList = new ArrayList<>();
			jdbcTemplate.query(
					" select approval_action_id \"tinApprovalActionId\",approval_action \"vchActionName\" from m_approval_action where status = '0' ",
					new RowCallbackHandler() {
						public void processRow(ResultSet resultSet) throws SQLException {
							do {

								try {
									Map<String, Object> map = new HashMap<>();

									map.put("tinApprovalActionId", resultSet.getInt("tinApprovalActionId"));
									map.put("vchActionName", resultSet.getString("vchActionName"));
									myList.add(map);

								} catch (Exception e) {
									log.error(e.getMessage());
								}

							} while (resultSet.next());

						}
					});
			return myList;
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject response = new JSONObject();
			response.put(CommonConstant.STATUS_KEY, "400");
			response.put("msg", "error");
			return (List<Map<String, Object>>) response;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getallOfficersApi() {
		try {
			List<Map<String, Object>> myList = new ArrayList<>();

			jdbcTemplate.query(
					"SELECT role_id AS intRoleId, role_name AS vchRoleName FROM m_role"
							+ " WHERE status = '0' and role_id != 2 and department_id != 0 ORDER BY role_id ASC",

					new RowCallbackHandler() {
						public void processRow(ResultSet resultSet) throws SQLException {
							do {

								try {
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("intRoleId", resultSet.getInt("intRoleId"));
									map.put("vchRoleName", resultSet.getString("vchRoleName"));
									myList.add(map);

								} catch (Exception e) {
									log.error(e.getMessage());
								}

							} while (resultSet.next());

						}
					});
			return myList;
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject response = new JSONObject();
			response.put("status", "400");
			response.put("msg", "error");
			return (List<Map<String, Object>>) response;
		}

	}

	@Transactional
	@Override
	public String setWorkflow(String setWorkflow) {

		try {

			long millis = System.currentTimeMillis();
			JSONObject workFlowReq = new JSONObject(setWorkflow);
			byte[] requestData = Base64.getDecoder().decode(workFlowReq.getString(CommonConstant.REQUEST_DATA));
			JSONObject workFlowRequest = new JSONObject(new String(requestData));
			JSONArray stageData = null;
			String response = null;

			String arrays = workFlowRequest.get("stageData").toString();
			if (workFlowRequest.has("stageData")) {
				stageData = new JSONArray(arrays);
			}

			try {
				String escapedHTML = workFlowRequest.getString("drawData");
				String ctrlId = workFlowRequest.getString("dynFilterCtrlId");
				String dynFilterDetails = workFlowRequest.getString("dynFilterDetails");
				Integer workFlowCount = 0;
				if (dynFilterDetails.length() > 0 && ctrlId.length() > 0) {
					workFlowCount = tSetWorkFlowRepository.countFilterData(workFlowRequest.getInt("serviceId"),
							dynFilterDetails, ctrlId);
				}

				updateTSetWorkFlow = " UPDATE t_set_workflow SET deletedFlag = 1 WHERE serviceId= "
						+ workFlowRequest.getInt("serviceId");

				if (workFlowCount > 0) {
					if (workFlowRequest.getString("dynFilterDetails").length() > 0) {
						updateTSetWorkFlow = updateTSetWorkFlow + " and vchDynFilter= '"
								+ workFlowRequest.getString("dynFilterDetails") + "'";
					}
				} else if (ctrlId.length() > 0) {
					updateTSetWorkFlow = updateTSetWorkFlow + " and vchDynFilterCtrlId != '" + ctrlId + "'";
				}

				KeyHolder keyHolder = new GeneratedKeyHolder();
				jdbcTemplate.update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = null;
						try {
							ps = connection.prepareStatement(updateTSetWorkFlow, new String[] { "workflowid" });
						} catch (SQLException | JSONException e) {
							log.error(e.getMessage());
							JSONObject response = new JSONObject();
							response.put("status", "400");
							response.put("msg", "error");
							return (PreparedStatement) response;
						}
						return ps;
					}
				}, keyHolder);

				TSetWorkFlow tSetWorkflow = new TSetWorkFlow();
				tSetWorkflow.setProjectId(workFlowRequest.getInt("projectId"));
				tSetWorkflow.setServiceId(workFlowRequest.getInt("serviceId"));
				tSetWorkflow.setCanvasData(escapedHTML);
				tSetWorkflow.setDeletedFlag(0);
				tSetWorkflow.setCreatedBy(20);
				tSetWorkflow.setCreatedOn(new Date(millis));
				// developed by Abhijit--------------------------------------
				for (int i = 0; i <= stageData.length() - 1; i++) {
					JSONObject object = stageData.getJSONObject(i);
					if (!object.getString("authActions").equals("")) {
						tSetWorkflow.setVchMailSmsConfigIds(workFlowRequest.get("applicantMailSmsDetails").toString());
					}

				}
				// -----------------------------------------------
				if (workFlowRequest.getString("dynFilterDetails").length() > 0) {
					tSetWorkflow.setVchDynFilter(workFlowRequest.getString("dynFilterDetails"));
				}

				if (workFlowRequest.getString("dynFilterCtrlId").length() > 0) {
					tSetWorkflow.setVchDynFilterCtrlId(workFlowRequest.getString("dynFilterCtrlId"));
				} else {
					tSetWorkflow.setVchDynFilterCtrlId("0");
				}
				tSetWorkFlowRepository.save(tSetWorkflow);

				String serviceName = "";
				if (workFlowRequest.getInt("serviceId") == 1) {
					serviceName = "Land Application";
				} else if (workFlowRequest.getInt("serviceId") == 2) {
					serviceName = "NOC Application";
				}
				approvalConfig.saveApprovalConfig(stageData, serviceName);

			}

			catch (Exception e) {
				log.error(e.getMessage());
			}
			response = "success";

			return response;
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject response = new JSONObject();
			response.put("status", "400");
			response.put("msg", "error");
			return response.toString();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getFormName() {
		try {
			List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();

			jdbcTemplate.query("SELECT menu_id as intId, menu_name as vchProcessName FROM m_menu"
					+ " WHERE status = '0' ORDER BY menu_id ASC", new RowCallbackHandler() {
						public void processRow(ResultSet resultSet) throws SQLException {
							do {

								try {
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("intId", resultSet.getInt("intId"));
									map.put("vchProcessName", resultSet.getString("vchProcessName"));
									myList.add(map);

								} catch (Exception e) {
									log.error(e.getMessage());
								}

							} while (resultSet.next());

						}
					});
			return myList;
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject response = new JSONObject();
			response.put("status", "400");
			response.put("msg", "error");
			return (List<Map<String, Object>>) response;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDocumentList() {
		try {
			List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();

			jdbcTemplate.query(
					"SELECT doc_type_id as docId, doc_name as docName FROM m_document_type"
							+ " WHERE deleted_flag = '0' AND doc_type = '2' ORDER BY doc_name ASC",
					new RowCallbackHandler() {
						public void processRow(ResultSet resultSet) throws SQLException {
							do {

								try {
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("docId", resultSet.getInt("docId"));
									map.put("docName", resultSet.getString("docName"));
									myList.add(map);

								} catch (Exception e) {
									log.error(e.getMessage());
								}

							} while (resultSet.next());

						}
					});
			return myList;
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject response = new JSONObject();
			response.put("status", "400");
			response.put("msg", "error");
			return (List<Map<String, Object>>) response;
		}

	}

	public JSONObject fillWorkflow(String serviceId, String dynFilterDetails) {
		try {
			JSONObject object = new JSONObject();

			String queryFillWorkFlow = " select canvasData from t_set_workflow where deletedFlag = 0 and serviceId= "
					+ serviceId;
			if (dynFilterDetails.length() > 0) {
				queryFillWorkFlow += " and vchDynFilter= '" + dynFilterDetails + "'";
			}

			jdbcTemplate.query(queryFillWorkFlow, new RowCallbackHandler() {
				public void processRow(ResultSet resultSet) throws SQLException {

					do {

						try {
							String unEscapedHTML = StringEscapeUtils.unescapeHtml4(resultSet.getString("canvasData"));
							object.put("result", unEscapedHTML);

						} catch (Exception e) {
							log.error(e.getMessage());
						}

					} while (resultSet.next());

				}

			});

			return object;
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject response = new JSONObject();
			response.put("status", "400");
			response.put("msg", "error");
			return response;
		}
	}

}
