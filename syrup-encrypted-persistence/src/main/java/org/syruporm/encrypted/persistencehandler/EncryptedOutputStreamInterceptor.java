package org.syruporm.encrypted.persistencehandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.syruporm.core.PersistenceHandler;

public class EncryptedOutputStreamInterceptor extends OutputStream {

	private List<Byte> bytes = new ArrayList<>();
	private PersistenceHandler actualPersistenceHandler;
	private Class<? extends Object> myClass;

	@Override
	public void write(int arg0) throws IOException {
		bytes.add((byte) arg0);
	}

	@Override
	public void close() throws IOException {
		OutputStream outputStream = actualPersistenceHandler.save(myClass);

		outputStream.write(convertListToArrayAndEncrypt());
		outputStream.close();
	}

	private byte[] convertListToArrayAndEncrypt() {
		byte arrayConversion[] = new byte[bytes.size()];

		for (int i = 0; i < bytes.size(); i++)
			arrayConversion[i] = bytes.get(i);

		String key = "Bar12345Bar12345"; // 128 bit key
		String initVector = "RandomInitVector"; // 16 bytes IV
		byte[] encrypted = null;

		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			encrypted = cipher.doFinal(arrayConversion);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encrypted;
	}

	/**
	 * @param actualPersistenceHandler
	 * @param myClass
	 */
	protected EncryptedOutputStreamInterceptor(PersistenceHandler actualPersistenceHandler,
			Class<? extends Object> myClass) {
		this.actualPersistenceHandler = actualPersistenceHandler;
		this.myClass = myClass;
	}
}