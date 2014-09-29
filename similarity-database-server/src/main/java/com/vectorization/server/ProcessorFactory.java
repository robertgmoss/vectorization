package com.vectorization.server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ProcessorFactory {
	Processor create(OutputStream out, InputStream inputStream);
}
