package com.ccthanking.framework.coreapp.orgmanage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.Role;
import com.ccthanking.framework.common.RoleVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.Cache;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.util.Pub;

public class OrgRoleManager implements Cache {
	private Hashtable roletable;
	private static OrgRoleManager instance;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("OrgRoleManager");
	private OrgRoleManager() throws Exception {
		init();
	}
	public void reBuildMemory() throws Exception{
		if(roletable != null){
			roletable.clear();
			roletable = null;
		}
		init();
	}
	public void synchronize(String data, int action) throws Exception {
		Role role = this.loadRole(data);
		Role old = this.getRole(data);
		if (action == CacheManager.ADD) {
			// update by xhb on 2013-09-02
			this.roletable.put(role.getRoleId(), role);
		} else if (action == CacheManager.UPDATE) {
			// update by xhb on 2013-09-02
			old.setName(role.getName());
			old.setDeptId(role.getDeptId());
			old.setLevelName(role.getLevelName());
			old.setMemo(role.getMemo());
		} else if (action == CacheManager.DELETE) {
			role = (Role) roletable.remove(data);
		}
		if (action == CacheManager.MAP_CHANGEED
				|| action == CacheManager.DELETE) {
			User[] users = old.getUsers();
			if (users != null)
				for (int i = 0; i < users.length; i++) {
					users[i].setRoles(null);
					users[i].setAllowedMenus(null);
				}
			old.setUsers(null);
			old.setMenus(null);
			users = role.getUsers();
			if (users != null) {
				for (int i = 0; i < users.length; i++) {
					users[i].setRoles(null);
					users[i].setAllowedMenus(null);
				}
			}
		}
	}

	private void init() throws Exception {

		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			// update by xhb on 2013-09-02
			String querySql = "select name,s_memo,level_name,DEPTID,role_id from FS_org_role ";
			String[][] list = DBUtil.query(conn, querySql);
			if (list != null) {
				roletable = new Hashtable(list.length);
				for (int i = 0; i < list.length; i++) {
					Role role = new RoleVO();
					role.setName(list[i][0]);
					role.setMemo(list[i][1]);
					role.setLevelName(list[i][2]);
					role.setDeptId(list[i][3]);
					// update by xhb on 2013-09-02
					role.setRoleId(list[i][4]);
					roletable.put(role.getRoleId(), role);
				}
			} else
				roletable = new Hashtable();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	private Role loadRole(String roleId) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			// update by xhb on 2013-09-02
			String querySql = "select name,s_memo,level_name,deptid,role_id "
					+ " from FS_org_role where role_id ='" + roleId + "'";
			String[][] list = DBUtil.query(conn, querySql);
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					Role role = new RoleVO();
					role.setName(list[i][0]);
					role.setMemo(list[i][1]);
					role.setLevelName(list[i][2]);
					role.setDeptId(list[i][3]);
					role.setRoleId(list[i][4]);
					return role;
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
		return null;
	}

	// update by xhb on 2013-09-02
	public Role getRole(String roleId) throws Exception {
		return (Role) this.roletable.get(roleId);
	}

	public Role[] getUserRoles(String account) throws Exception {
		if (Pub.empty(account))
			return null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			// update by xhb on 2013-09-02
			String sql = "select role_name,person_account,role_id " 
					+ "from FS_org_role_psn_map where person_account='" + account + "'";
			String[][] list = DBUtil.querySql(conn, sql);
			if (list != null) {
				Role[] roles = new Role[list.length];
				for (int i = 0; i < list.length; i++) {
					roles[i] = (Role) this.roletable.get(list[i][2]);
				}
				return roles;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception ee) {
			}
		}
		return null;
	}

	public User[] getRoleUsers(String role_id) throws Exception {
		if (Pub.empty(role_id))
			return null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select role_name,person_account,role_id " 
					// update by xhb on 2013-09-02
					+ "from FS_org_role_psn_map where role_id='" + role_id + "'";
			String[][] list = DBUtil.querySql(conn, sql);
			if (list != null) {
				User[] users = new User[list.length];
				for (int i = 0; i < list.length; i++) {
					users[i] = UserManager.getInstance().getUserByLoginName(list[i][1]);
				}
				return users;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception ee) {
			}
		}
	
		return null;
	}

	public MenuVo[] getRoleMenus(String role_id) throws Exception {
		if (Pub.empty(role_id))
			return null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select role_name,menu_name from FS_ORG_ROLE_MENU_MAP r, FS_EAP_MENU m"
					// update by xhb on 2013-09-02
					+ " where r.MENU_NAME=m.name and role_id='" + role_id
					+ "' and menu_name in (select NAME from fs_eap_menu where SFYX = '1')"
					+ " order by ORDERNO";
			String[][] list = DBUtil.querySql(conn, sql);
			if (list != null) {
				MenuVo[] menus = new MenuVo[list.length];
				for (int i = 0; i < list.length; i++) {
					menus[i] = MenuManager.getInstance().getMenu(list[i][1]);
				}
				return menus;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception ee) {
			}
		}
		return null;
	}

	static synchronized public OrgRoleManager getInstance() throws Exception {
		if (instance == null) {
			instance = new OrgRoleManager();
			CacheManager.register(CacheManager.CACHE_ROLE, instance);
		}
		return instance;
	}

}