package com.vectorization.server.command;

import com.vectorization.core.Vector;
import com.vectorization.parsing.Command;
import com.vectorization.server.security.Security;

public abstract class AbstractCommandFactory implements CommandFactory{

	public Command newCreateCommand(String spaceName, int dimensionality) {
		return new Create(spaceName, dimensionality);
	}

	public Command newDropCommand(String spaceName) {
		return new Drop(spaceName);
	}

	public Command newFindCommand(String spaceName, int k, Vector v) {
		return new Find(k, v, spaceName);
	}

	public Command newFindVectorCommand(String space, int k, String querySpace,
			String queryVectorName) {
		return new FindVector(k, querySpace, queryVectorName, space);
	}

	public Command newInsertCommand(String spaceName, Vector v) {
		return new Insert(v, spaceName);
	}

	public Command newListCommand() {
		return new ListCommand();
	}

	public Command newNullCommand(String msg) {
		return new Null(msg);
	}

	public Command newRemoveCommand(String spaceName, String vectorName) {
		return new Remove(spaceName, vectorName);
	}

	public Command newShowCommand(String spaceName, String vectorName) {
		return new Show(spaceName, vectorName);
	}

	public Command newUseCommand(String databaseName) {
		return new Use(databaseName);
	}

	public Command newLoginCommand(String username, String password) {
		return new Login(username, password);
	}

	public Command newAddUserCommand(Security security, String username, String password) {
		return new AddUser(security, username, password);
	}

	public Command newGrantCommand(Security security,java.util.List<String> permissions,
			String dbName, String spaceName, String username) {
		return new Grant(security, permissions, dbName, spaceName, username);
	}

	public Command newChangePasswordCommand(Security security,String password) {
		return new ChangePassword(security, password);
	}
}
