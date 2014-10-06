package com.vectorization.server.command;

import com.vectorization.core.Vector;
import com.vectorization.parsing.ServerCommand;
import com.vectorization.server.security.Security;

public abstract class AbstractCommandFactory implements CommandFactory{

	public ServerCommand newCreateCommand(String spaceName, int dimensionality) {
		return new Create(spaceName, dimensionality);
	}

	public ServerCommand newDropCommand(String spaceName) {
		return new Drop(spaceName);
	}

	public ServerCommand newFindCommand(String spaceName, int k, Vector v) {
		return new Find(k, v, spaceName);
	}

	public ServerCommand newFindVectorCommand(String space, int k, String querySpace,
			String queryVectorName) {
		return new FindVector(k, querySpace, queryVectorName, space);
	}

	public ServerCommand newInsertCommand(String spaceName, Vector v) {
		return new Insert(v, spaceName);
	}

	public ServerCommand newListCommand() {
		return new ListCommand();
	}

	public ServerCommand newNullCommand(String msg) {
		return new Null(msg);
	}

	public ServerCommand newRemoveCommand(String spaceName, String vectorName) {
		return new Remove(spaceName, vectorName);
	}

	public ServerCommand newShowCommand(String spaceName, String vectorName) {
		return new Show(spaceName, vectorName);
	}

	public ServerCommand newUseCommand(String databaseName) {
		return new Use(databaseName);
	}

	public ServerCommand newLoginCommand(String username, String password) {
		return new Login(username, password);
	}

	public ServerCommand newAddUserCommand(Security security, String username, String password) {
		return new AddUser(security, username, password);
	}

	public ServerCommand newGrantCommand(Security security,java.util.List<String> permissions,
			String dbName, String spaceName, String username) {
		return new Grant(security, permissions, dbName, spaceName, username);
	}

	public ServerCommand newChangePasswordCommand(Security security,String password) {
		return new ChangePassword(security, password);
	}
}
