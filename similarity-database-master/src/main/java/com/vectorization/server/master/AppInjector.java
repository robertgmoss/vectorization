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
package com.vectorization.server.master;

import javax.sql.DataSource;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;
import org.apache.derby.jdbc.EmbeddedDataSource;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.vectorization.parsing.Lexer;
import com.vectorization.parsing.LexerFactory;
import com.vectorization.parsing.ParserFactory;
import com.vectorization.parsing.ServerLexer;
import com.vectorization.parsing.ServerParserFactory;
import com.vectorization.server.Processor;
import com.vectorization.server.ProcessorFactory;
import com.vectorization.server.Server;
import com.vectorization.server.command.CommandFactory;
import com.vectorization.server.security.Security;
import com.vectorization.server.security.SecurityImpl;

public class AppInjector extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandLineParser.class).to(PosixParser.class);
		install(new FactoryModuleBuilder().implement(Processor.class,
				MasterProcessor.class).build(ProcessorFactory.class));
		bind(ParserFactory.class).to(ServerParserFactory.class);
		install(new FactoryModuleBuilder().implement(Lexer.class,
				ServerLexer.class).build(LexerFactory.class));
		bind(Network.class).to(NetworkImpl.class).in(Singleton.class);
		bind(Security.class).to(SecurityImpl.class).in(Singleton.class);
	}
	
	@Provides
	CommandFactory provideCommandFactory(Network network){
		return new MasterCommandFactory(network);
	}

	@Singleton
	@Provides
	Server provideServer(ProcessorFactory processorFactory) {
		return new Master(processorFactory);
	}

	@Singleton
	@Provides
	DataSource provideDataSource() {
		EmbeddedDataSource ds = new EmbeddedDataSource();
		ds.setDatabaseName("Network");
		ds.setCreateDatabase("create");
		return ds;
	}
}
