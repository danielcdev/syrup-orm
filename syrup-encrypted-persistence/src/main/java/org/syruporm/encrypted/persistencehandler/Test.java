package org.syruporm.encrypted.persistencehandler;

import org.syruporm.core.Syrup;
import org.syruporm.core.SyrupFactory;
import org.syruporm.persistencehandler.LocalFileHandler;

public class Test {

	private void begin() {
		SyrupFactory factory = new SyrupFactory(new EncryptedPersistenceHandler(new LocalFileHandler()));
		Syrup syrup = factory.getSyrup(Model.class);

		System.out.println(syrup.getById("5"));

		// Model model = new Model();
		// model.setPassword("Password2");
		// model.setUsername("Username5");

		// syrup.save(model);
	}

	public Test() {
		try {
			begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new Test();
	}
}
