package com.danielcotter.syrup.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.danielcotter.syrup.SyrupFactory;
import com.danielcotter.syrup.test.model.TestModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunThroughTest {

	private static TestModel generatedIdModel;
	private SyrupFactory factory = new SyrupFactory();
	private TestModel testModel = new TestModel(1337, "myFakeUsername", "myFakePassword");

	@BeforeClass
	public static void beforeTests() {
		new SyrupFactory().resetSyrup(TestModel.class);
	}

	@Test
	public void testApersists() {
		Assert.assertEquals(true, factory.getSyrup(TestModel.class).save(testModel));
	}

	@Test
	public void testBisFactual() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getById(testModel.getId().toString());

		Assert.assertEquals(testModel, persistedModel);
	}

	@Test
	public void testCrefusesDuplicateId() {
		Assert.assertEquals(false, factory.getSyrup(TestModel.class).save(testModel));
	}

	@Test
	public void testDgeneratesId() {
		generatedIdModel = new TestModel(null, "generatedUsername", "generatedPassword");

		Assert.assertEquals(true, factory.getSyrup(TestModel.class).save(generatedIdModel));
	}

	@Test
	public void testEgeneratedIdSaved() {
		Assert.assertEquals(new Integer(1), generatedIdModel.getId());
	}

	@Test
	public void testFgeneratedIdIsFactual() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getById("1");

		Assert.assertEquals(generatedIdModel, persistedModel);
	}

	@Test
	public void testGdoesNotExist() {
		TestModel nonexistentModel = (TestModel) factory.getSyrup(TestModel.class).getById("20");

		Assert.assertNull(nonexistentModel);
	}

	@Test
	public void testHpersistsUpdate() {
		generatedIdModel.setPassword("myNewPassword");

		Assert.assertEquals(true, factory.getSyrup(TestModel.class).update(generatedIdModel));
	}

	@Test
	public void testIupdateIsFactual() {
		TestModel persistedModel = (TestModel) factory.getSyrup(TestModel.class).getById("1");

		Assert.assertEquals(new String("myNewPassword"), persistedModel.getPassword());
	}

	@Test
	public void testJupdateRefusesNewObject() {
		TestModel newModel = new TestModel(1000, "newUsername", "newPassword");

		Assert.assertEquals(false, factory.getSyrup(TestModel.class).update(newModel));
	}
}