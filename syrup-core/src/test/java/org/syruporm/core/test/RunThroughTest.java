package org.syruporm.core.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.syruporm.core.SyrupFactory;
import org.syruporm.core.test.model.TestModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunThroughTest {

	private static TestModel generatedIdModel;
	private static SyrupFactory factory;
	private TestModel testModel = new TestModel(1337, "myFakeUsername", "myFakePassword");

	@BeforeClass
	public static void beforeTests() {
		factory = new SyrupFactory();

		factory.resetSyrup(TestModel.class);
		generatedIdModel = new TestModel(null, "generatedUsername", "generatedPassword");
	}

	@Test
	public void testApersists() {
		Assert.assertEquals(true, factory.getSyrup(TestModel.class).save(testModel));
	}

	@Test
	public void testBisFactualById() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getById(testModel.getId().toString());

		Assert.assertEquals(testModel, persistedModel);
	}

	@Test
	public void testCisFactualByField() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getByField("username",
				testModel.getUsername());

		Assert.assertEquals(testModel, persistedModel);
	}

	@Test
	public void testDrefusesDuplicateId() {
		Assert.assertEquals(false, factory.getSyrup(TestModel.class).save(testModel));
	}

	@Test
	public void testEgeneratesId() {
		Assert.assertEquals(true, factory.getSyrup(TestModel.class).save(generatedIdModel));
	}

	@Test
	public void testFgeneratedIdSaved() {
		Assert.assertEquals(new Integer(1), generatedIdModel.getId());
	}

	@Test
	public void testGgeneratedIdIsFactual() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getById("1");

		Assert.assertEquals(generatedIdModel, persistedModel);
	}

	@Test
	public void testHdoesNotExist() {
		TestModel nonexistentModel = (TestModel) factory.getSyrup(TestModel.class).getById("20");

		Assert.assertNull(nonexistentModel);
	}

	@Test
	public void testIpersistsUpdate() {
		generatedIdModel.setPassword("myNewPassword");

		Assert.assertEquals(true, factory.getSyrup(TestModel.class).update(generatedIdModel));
	}

	@Test
	public void testJupdateIsFactual() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getById("1");

		Assert.assertEquals(new String("myNewPassword"), persistedModel.getPassword());
	}

	@Test
	public void testKupdateRefusesNewObject() {
		TestModel newModel = new TestModel(1000, "newUsername", "newPassword");

		Assert.assertEquals(false, factory.getSyrup(TestModel.class).update(newModel));
	}

	@Test
	public void testLobjectDeletion() {
		Assert.assertEquals(true, factory.getSyrup(TestModel.class).delete(generatedIdModel));
	}

	@Test
	public void testMdeleteIsFactual() {
		TestModel nonexistentModel = (TestModel) factory.getSyrup(TestModel.class).getById("1");

		Assert.assertNull(nonexistentModel);
	}

	@Test
	public void testNdeletionNotExists() {
		Assert.assertEquals(false, factory.getSyrup(TestModel.class).delete(generatedIdModel));
	}
}