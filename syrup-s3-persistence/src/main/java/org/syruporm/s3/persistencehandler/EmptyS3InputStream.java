package org.syruporm.s3.persistencehandler;

import java.io.IOException;
import java.io.InputStream;

public class EmptyS3InputStream extends InputStream {

	@Override
	public int read() throws IOException {
		return -1;
	}

	protected EmptyS3InputStream() {

	}
}