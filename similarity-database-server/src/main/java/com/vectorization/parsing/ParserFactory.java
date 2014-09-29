package com.vectorization.parsing;

import com.vectorization.server.Processor;

public interface ParserFactory {
	
	Parser<Command> create(Processor processor, String input);
}
