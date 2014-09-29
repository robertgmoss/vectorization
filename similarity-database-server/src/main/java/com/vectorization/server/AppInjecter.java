package com.vectorization.server;

import javax.sql.DataSource;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.vectorization.parsing.Lexer;
import com.vectorization.parsing.LexerFactory;
import com.vectorization.parsing.ParserFactory;
import com.vectorization.parsing.ServerLexer;
import com.vectorization.parsing.ServerParserFactory;
import com.vectorization.server.command.CommandFactory;
import com.vectorization.server.command.DefaultCommandFactory;
import com.vectorization.server.command.SecureCommandFactory;
import com.vectorization.server.security.Security;
import com.vectorization.server.security.SecurityImpl;

public class AppInjecter extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandLineParser.class).to(PosixParser.class);
		install(new FactoryModuleBuilder().implement(Processor.class,
				ProcessorImpl.class).build(ProcessorFactory.class));
		bind(ParserFactory.class).to(ServerParserFactory.class);
		install(new FactoryModuleBuilder().implement(Lexer.class,
				ServerLexer.class).build(LexerFactory.class));
		bind(Security.class).to(SecurityImpl.class).in(Singleton.class);
	}
	
	@Provides
	CommandFactory provideCommandFactory(){
		return new SecureCommandFactory(
				new DefaultCommandFactory());
	}

	@Singleton
	@Provides
	Server provideServer(ProcessorFactory processorFactory) {
		return new ServerImpl(processorFactory);
	}

	@Singleton
	@Provides
	DataSource provideDataSource() {
		EmbeddedDataSource ds = new EmbeddedDataSource();
		ds.setDatabaseName("Auth");
		ds.setCreateDatabase("create");
		return ds;
	}

	@Singleton
	@Provides
	Realm provideRealm(DataSource dataSource) {
		JdbcRealm realm = new JdbcRealm();
		realm.setPermissionsLookupEnabled(true);
		realm.setDataSource(dataSource);
		realm.setCacheManager(new MemoryConstrainedCacheManager());
		return realm;
	}

	@Singleton
	@Provides
	SecurityManager provideSecurityManager(Realm realm) {
		return new DefaultSecurityManager(realm);
	}

}
