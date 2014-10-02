/*  
 *  Copyright (C) 2014 Robert Moss
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.vectorization.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vectorization.core.VectorizationException;
import com.vectorization.driver.Connection;
import com.vectorization.driver.builders.StatementBuilders;

public class TestCreateDatabase {

	private Connection connection;

	@Before
	public void setUp() {
		App app = new App();
		connection = app.getConnection();
		connection.connect();
		StatementBuilders.login("admin").with("admin").execute(connection);
		StatementBuilders.use("Test").execute(connection);
	}

	@After
	public void tearDown() {
		String result = StatementBuilders.drop("testSpace").execute(connection);
		System.out.println(result);
		connection.close();
	}

	@Test
	public void testCreate() {
		String result = StatementBuilders.create("testSpace").withDimensionality(2).execute(connection);
		System.out.println(result);
	}
	
	@Test(expected=VectorizationException.class)
	public void testCreateBlank(){
		StatementBuilders.create("").withDimensionality(2).execute(connection);
	}
	
	@Test(expected=VectorizationException.class)
	public void testCreateNull(){
		StatementBuilders.create(null).withDimensionality(2).execute(connection);
	}
}
