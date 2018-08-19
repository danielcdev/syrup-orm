package org.syruporm.s3.persistencehandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class S3OutputStream extends OutputStream {

	private List<Byte> bytes = new ArrayList<>();
	private AmazonS3 s3Client;
	private String bucketName;
	private String objectPath;

	@Override
	public void write(int arg0) throws IOException {
		bytes.add((byte) arg0);
	}

	@Override
	public void close() {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(bytes.size());

		s3Client.putObject(bucketName, objectPath, new ByteArrayInputStream(convertListToArray()), metadata);
	}

	private byte[] convertListToArray() {
		byte arrayConversion[] = new byte[bytes.size()];

		for (int i = 0; i < bytes.size(); i++)
			arrayConversion[i] = bytes.get(i);

		return arrayConversion;
	}

	/**
	 * @param s3Client
	 * @param bucketName
	 * @param objectPath
	 */
	protected S3OutputStream(AmazonS3 s3Client, String bucketName, String objectPath) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
		this.objectPath = objectPath;
	}
}