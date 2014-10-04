package com.vectorization.server.security;

import javax.sql.DataSource;

public interface DataSourceFactory {

	DataSource create();
}
