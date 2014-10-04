package com.vectorization.server.security;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class EmbeddedDataSourceFactory implements DataSourceFactory{
	
	private String dbname; 
	private boolean create;
	
	@Inject
	public EmbeddedDataSourceFactory(@Assisted String dbname, @Assisted boolean create) {
		this.dbname = dbname;
		this.create = create;
	}

	public DataSource create() {
		EmbeddedDataSource ds = new EmbeddedDataSource();
		ds.setDatabaseName(dbname);
		if (create)
			ds.setCreateDatabase("create");
		return ds;
	}

}
