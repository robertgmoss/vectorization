package com.vectorization.example;

import com.vectorization.core.vectors.Vectors;
import com.vectorization.driver.Connection;
import com.vectorization.driver.Vectorization;
import com.vectorization.driver.builders.StatementBuilders;

public class App extends Vectorization{
	
	public static void main(String[] args) {
		App application = new App();
		Connection connection = application.getConnection();
		connection.connect();
//		Statement statement = connection.createStatement();
//		String result = statement.execute("login admin with admin");
		String result = StatementBuilders.login("admin").with("admin").execute(connection);
		System.out.println(result);
		
		String s = StatementBuilders
				.use("Test")
				.execute(connection);
		System.out.println(s);
		
		s = StatementBuilders
				.create("test")
				.withDimensionality(2)
				.execute(connection);
		System.out.println(s);
		
		s = StatementBuilders
				.list()
				.execute(connection);
		System.out.println(s);
		
		s = StatementBuilders
				.show("test")
				.execute(connection).toString();
		System.out.println(s);
		
		s = StatementBuilders
				.insert(Vectors.createVector("myvector", 0.5, 0.5))
				.into("test")
				.execute(connection);
		
		System.out.println(s);
		
		s = StatementBuilders
				.remove("myvector")
				.from("test")
				.execute(connection);
		
		System.out.println(s);
		
		s = StatementBuilders
				.find(3)
				.nearestTo("test.myvector")
				.in("test")
				.execute(connection).toString();
		System.out.println(s);
		
		s = StatementBuilders.drop("test").execute(connection);
		System.out.println(s);
		

		
		connection.close();
	}

}
