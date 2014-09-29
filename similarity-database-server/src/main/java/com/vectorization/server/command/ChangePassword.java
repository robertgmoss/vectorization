package com.vectorization.server.command;

import java.sql.SQLException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.vectorization.core.VectorizationException;
import com.vectorization.core.database.Database;
import com.vectorization.server.security.Security;

public class ChangePassword extends AbstractCommand{

	private String password;
	private Security security;

	public ChangePassword(Security security, String password) {
		this.security = security;
		this.password = password;
	}
	
	@Override
	public String execute(Database database) {
		Subject subject = SecurityUtils.getSubject();
		try {
			security.updatePassword(subject.getPrincipal().toString(), password);
		} catch (SQLException e) {
			throw new VectorizationException("unable to change password: "+e.getMessage());
		}
		return "password changed";
	}

	public String getPermissionLevel() {
		return "passwd";
	}

}
