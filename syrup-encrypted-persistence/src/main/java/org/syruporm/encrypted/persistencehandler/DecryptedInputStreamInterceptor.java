package org.syruporm.encrypted.persistencehandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.syruporm.core.PersistenceHandler;

public class DecryptedInputStreamInterceptor extends InputStream {

	private List<Byte> bytes = null;
	private PersistenceHandler actualPersistenceHandler;
	private Class<? extends Object> myClass;

	@Override
	public int read() throws IOException {
		if (bytes == null) {
			bytes = new ArrayList<>();
			readWholeStream();
			decryptStream();
		}

		byte thisByte;

		if (bytes.size() > 0) {
			thisByte = bytes.get(0);
			bytes.remove(0);
		} else {
			bytes = null;
			return -1;
		}

		return thisByte;
	}

	@Override
	public void close() {
		bytes = null;
	}

	private void readWholeStream() {
		InputStream inputStream = actualPersistenceHandler.load(myClass);
		int thisByte;

		try {
			while ((thisByte = inputStream.read()) != -1)
				bytes.add((byte) thisByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decryptStream() {
		byte[] encryptedByteArray = convertListToArray();
		bytes = new ArrayList<>();

		String key = "Bar12345Bar12345"; // 128 bit key
		String initVector = "RandomInitVector"; // 16 bytes IV

		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);

			for (byte thisByte : decryptedByteArray)
				bytes.add(thisByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] convertListToArray() {
		byte arrayConversion[] = new byte[bytes.size()];

		for (int i = 0; i < bytes.size(); i++)
			arrayConversion[i] = bytes.get(i);

		return arrayConversion;
	}

	/**
	 * @param actualPersistenceHandler
	 * @param myClass
	 */
	protected DecryptedInputStreamInterceptor(PersistenceHandler actualPersistenceHandler,
			Class<? extends Object> myClass) {
		this.actualPersistenceHandler = actualPersistenceHandler;
		this.myClass = myClass;
	}
}
