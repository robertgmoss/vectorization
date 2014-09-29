package com.vectorization.client;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.PosixParser;

import com.google.inject.AbstractModule;

public class AppInjecter extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandLineParser.class).to(PosixParser.class);
	}

}
