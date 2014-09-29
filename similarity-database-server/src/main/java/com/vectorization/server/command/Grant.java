package com.vectorization.server.command;

import java.sql.SQLException;
import java.util.List;

import com.vectorization.core.VectorizationException;
import com.vectorization.core.database.Database;
import com.vectorization.server.security.Security;

public class Grant extends AbstractCommand {

	private List<String> permissions;
	private String dbName;
	private String spaceName;
	private String username;
	private Security security;

	public Grant(Security security, List<String> permissions, String dbName, String spaceName,
			String username) {
		this.security = security;
		this.permissions = permissions;
		this.dbName = dbName;
		this.spaceName = spaceName;
		this.username = username;
	}

	@Override
	public String execute(Database database) {
		String roleName = username; // pick a role name for these permissions
		try {
			StringBuilder permissionBuilder = new StringBuilder();
			for(int i = 0; i < permissions.size(); i++){
				 permissionBuilder.append(permissions.get(i));
				 if(i < permissions.size() - 1) permissionBuilder.append(",");
				
			}
			String commands = permissionBuilder.toString();
			
			String permission = dbName + ":" + commands + ":" + spaceName;
			security.insertRolePermission(roleName, permission);
			//security.insertUserRole(username, roleName);
		} catch (SQLException e) {
			throw new VectorizationException(e);
		}
		return "permissions granted ";
	}

	public String getPermissionLevel() {
		return "grant";
	}

}
