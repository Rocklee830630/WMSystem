package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.dic.TreeNode;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class DictServiceImpl {
	
	public String getDictByParentId(String parentId) {
		Connection conn = DBUtil.getConnection();
		String queryDictSql = "SELECT ID,PARENT_ID,DIC_CODE," 
				+ "DIC_VALUE,DIC_VALUE_SPELL FROM FS_DIC_TREE " 
				+ "ORDER BY TO_NUMBER(ID)";
		JSONArray jsonArr = new JSONArray();
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json=new JSONObject();
				json.put("id",rsMap.get("ID"));
			    json.put("parentId", rsMap.get("PARENT_ID"));
			    json.put("name", rsMap.get("DIC_VALUE"));
			    jsonArr.add(json);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}
	
	public String getCustomDictByParentId(String parentId, String type) {
		Connection conn = DBUtil.getConnection();
		parentId = StringUtils.isBlank(parentId)?"0":parentId;
		String manageTypeSql = "1".equals(type) ? "" : (" AND MANAGETYPE = '1' ");
		String queryDictSql = "SELECT ID,PARENT_ID,DIC_CODE," 
				+ "DIC_VALUE,DIC_VALUE_SPELL,PXH FROM FS_DIC_TREE WHERE PARENT_ID='" + parentId+"' " 
				+ manageTypeSql + "ORDER BY TO_NUMBER(ID)";
		JSONArray jsonArr = new JSONArray();
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json=new JSONObject();
				json.put("id",rsMap.get("ID"));
				json.put("parentId", rsMap.get("PARENT_ID"));
				json.put("name", rsMap.get("DIC_VALUE"));
				json.put("dict_code", rsMap.get("DIC_CODE"));
				json.put("pxh",rsMap.get("PXH"));
				jsonArr.add(json);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}
	
	public String getDicsByParent(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select  ID,PARENT_ID,DIC_CODE,DIC_VALUE,DIC_VALUE_SPELL,DIC_VALUE_ASPELL,DIC_LAYER,DIC_VALUE2,PXH from FS_DIC_TREE ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				domresult = bs.getJson();
				LogManager.writeUserLog(null, YwlxManager.SYSTEM_DICT,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询字典", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	public JSONObject insertOrUpdate(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		JSONObject jsonObj = null;
		try {
				conn.setAutoCommit(false);
				JSONArray list = new BaseVO().doInitJson(json);
				jsonObj = (JSONObject)list.get(0);
				String sql = "";
				String id = jsonObj.getString("ID");
				String parentId = jsonObj.getString("PARENT_ID");
				String dicValue = jsonObj.getString("DIC_VALUE");
				String pxh = jsonObj.getString("PXH");
				String dic_value2 = jsonObj.getString("DIC_VALUE2");
				if(Pub.empty(pxh)){
					pxh = "";
				}
				if(Pub.empty(id)){
					
					//最大主键值+1
					sql = "select max(to_number(id)) + 1 from fs_dic_tree";
					String ids[][] = DBUtil.query(conn, sql);
					
					//最大dic_code+1
					sql = "select max(to_number(dic_code)) + 1 from fs_dic_tree where parent_id = '" + parentId + "'";
					String dics[][] = DBUtil.query(conn, sql);
					String dicCode = Pub.empty(dics[0][0])?("10001"):dics[0][0];
					id = Pub.empty(ids[0][0])?("1"):ids[0][0];
					jsonObj.remove("ID");
					jsonObj.put("ID", id);
					
					//查询dic_name_code和dic_name_value
					sql = "select dic_name_code,dic_name_value from fs_dic_tree where id = '" + parentId + "'";
					dics = DBUtil.query(conn, sql);
					
					//插入
					sql = "insert into fs_dic_tree(id,parent_id,dic_code,dic_value,dic_layer,is_leaf,dic_name_code,dic_name_value,dic_value2,pxh) values(?,?,?,?,?,?,?,?,?,?)";
					Object paras[] = new Object[10];
					paras[0] = id;
					paras[1] = parentId;
					paras[2] = dicCode;
					paras[3] = dicValue;
					paras[4] = parentId;
					paras[5] = "1";
					paras[6] = dics[0][0];
					paras[7] = dics[0][1];
					paras[8] = dic_value2;
					paras[9] = pxh;

					DBUtil.executeUpdate(conn, sql, paras);
					conn.commit();
					TreeNode tn = new TreeNode();
					tn.setId(id);
					tn.setParentId(parentId);
					tn.setDicLayer(parentId);
					tn.setDicCode(dicCode);
					tn.setDicValue(dicValue);
					Dics.addDicValue(parentId,parentId, tn);
				}else{
//					if("".equals(pxh)){
//						sql = "update fs_dic_tree set dic_value = '" + dicValue + "'  where id = '" + id + "'";
//					}else{
//						sql = "update fs_dic_tree set dic_value = '" + dicValue + "' ,pxh='"+pxh+"' where id = '" + id + "'";
//					}
					sql = "update fs_dic_tree set dic_value='" + dicValue + "',pxh='"+pxh+"',DIC_VALUE2='"+dic_value2+"' where id = '" + id + "'";
					DBUtil.execUpdateSql(conn, sql);
					conn.commit();
					sql = "select ID, PARENT_ID, DIC_CODE, DIC_VALUE, DIC_VALUE_SPELL, DIC_VALUE_ASPELL, DIC_LAYER, IS_LEAF,DIC_VALUE2,PXH from fs_dic_tree where id = '" + id + "'";
					String result[][] = DBUtil.query(sql);
					TreeNode tn = new TreeNode();
					tn.setId(result[0][0]);
					tn.setParentId(result[0][1]);
					tn.setDicLayer(result[0][6]);
					tn.setDicCode(result[0][2]);
					tn.setDicValue(result[0][3]);
					Dics.updateDicValue(result[0][1], result[0][0], tn);
				}

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonObj;
	}
	

	public String checkUnique(String parent_id, String dict_code, User user) {
		String sql = "SELECT COUNT(1) CNT FROM fs_dic_tree WHERE parent_id = '" + parent_id + "' and DIC_CODE='" + dict_code + "'";
		String[][] cnt = DBUtil.query(sql);
		JSONObject jsonObj = new JSONObject();
		if ("0".equals(cnt[0][0])) {
			jsonObj.put("isUnique", "可以使用");
			jsonObj.put("sign", "0");
		} else {
			jsonObj.put("isUnique", "登录用户名重复，请重新填写");
			jsonObj.put("sign", "1");
		}
		return jsonObj.toString();
	}
	
	public JSONObject insertOrUpdateSystemDict(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		JSONObject jsonObj = null;
		try {
				conn.setAutoCommit(false);
				JSONArray list = new BaseVO().doInitJson(json);
				jsonObj = (JSONObject)list.get(0);
				String sql = "";
				String id = jsonObj.getString("ID");
				String parentId = jsonObj.getString("PARENT_ID");
				String dicValue = jsonObj.getString("DIC_VALUE");
				String dictCode = jsonObj.getString("DIC_CODE");
				String pxh = jsonObj.getString("PXH");
				if(Pub.empty(pxh)){
					pxh = "";
				}
				if(Pub.empty(id)){
					
					//最大主键值+1
					sql = "select max(to_number(id)) + 1 from fs_dic_tree";
					String ids[][] = DBUtil.query(conn, sql);
					id = Pub.empty(ids[0][0])?("1"):ids[0][0];
					jsonObj.remove("ID");
					jsonObj.put("ID", id);
					
					//查询dic_name_code和dic_name_value
					sql = "select dic_name_code,dic_name_value from fs_dic_tree where id = '" + parentId + "'";
					String dics[][] = DBUtil.query(conn, sql);
					
					//插入
					sql = "insert into fs_dic_tree(id,parent_id,dic_code,dic_value,dic_layer,is_leaf,dic_name_code,dic_name_value,pxh) values" +
							"('" + id + "','" + parentId + "','" + dictCode + "','" + dicValue + "','" + parentId + "','1','" + dics[0][0] + "','" + dics[0][1] + "','"+pxh+"')";
					
					DBUtil.execSql(conn, sql);
					conn.commit();
					TreeNode tn = new TreeNode();
					tn.setId(id);
					tn.setParentId(parentId);
					tn.setDicLayer(parentId);
					tn.setDicCode(dictCode);
					tn.setDicValue(dicValue);
					Dics.addDicValue(parentId,parentId, tn);
				} else {
//					if("".equals(pxh)){
//						sql = "update fs_dic_tree set dic_value = '" + dicValue + "'  where id = '" + id + "'";
//					}else{
//						sql = "update fs_dic_tree set dic_value = '" + dicValue + "' ,pxh='"+pxh+"' where id = '" + id + "'";
//					}
					sql = "update fs_dic_tree set dic_value = '" + dicValue + "',pxh='"+pxh+"' where id = '" + id + "'";
					DBUtil.execUpdateSql(conn, sql);
					conn.commit();
					sql = "select ID, PARENT_ID, DIC_CODE, DIC_VALUE, DIC_VALUE_SPELL, DIC_VALUE_ASPELL, DIC_LAYER, IS_LEAF,pxh from fs_dic_tree where id = '" + id + "'";
					String result[][] = DBUtil.query(sql);
					TreeNode tn = new TreeNode();
					tn.setId(result[0][0]);
					tn.setParentId(result[0][1]);
					tn.setDicLayer(result[0][6]);
					tn.setDicCode(result[0][2]);
					tn.setDicValue(result[0][3]);
					Dics.updateDicValue(result[0][1], result[0][0], tn);
				}

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonObj;
	}
	
	public JSONObject deleteSystemDict(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		JSONObject jsonObj = null;
		try {
				conn.setAutoCommit(false);
				JSONArray list = new BaseVO().doInitJson(json);
				jsonObj = (JSONObject)list.get(0);
				String id = jsonObj.getString("ID");
					
				String sql = "update fs_dic_tree set sfyx='0' where id='" + id + "'";
				DBUtil.execUpdateSql(conn, sql);
				conn.commit();
		//		Dics.delDic(id);
				Dics.getInstance().reBuildMemory();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonObj;
	}
}
