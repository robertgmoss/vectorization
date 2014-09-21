package com.vectorization.example;

import com.vectorization.driver.Connection;
import com.vectorization.driver.Statement;
import com.vectorization.driver.Vectorization;
import com.vectorization.driver.builders.StatementBuilders;

public class App extends Vectorization{
	
	public static void main(String[] args) {
		App application = new App();
		Connection connection = application.getConnection();
		connection.connect();
		Statement statement = connection.createStatement();
		String result = statement.execute("login root with secret");
		System.out.println(result);
		String s = StatementBuilders.use("Test").build();
		System.out.println(statement.execute(s));
		s = StatementBuilders.list().build();
		System.out.println(statement.execute(s));
		s = StatementBuilders.show("test").build();
		System.out.println(statement.execute(s));
		System.out.println();
		s = StatementBuilders.find(1).nearestTo("test.a").in("test").build();
		System.out.println(statement.execute(s));
		connection.close();
	}

}
