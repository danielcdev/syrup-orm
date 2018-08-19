package org.syruporm.s3.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.syruporm.core.SyrupFactory;
import org.syruporm.s3.persistencehandler.AmazonS3Handler;
import org.syruporm.s3.test.model.TestModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S3RunThroughTest {

	private static SyrupFactory factory;
	private TestModel testModel = new TestModel(1337, "myS3Username", "myS3Password");

	@BeforeClass
	public static void beforeTests() {
		AmazonS3Handler persistenceHandler = new AmazonS3Handler("syruptest");

		factory = new SyrupFactory(persistenceHandler);
		factory.resetSyrup(TestModel.class);
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
	public void testCobjectDeletion() {
		Assert.assertEquals(true, factory.resetSyrup(TestModel.class));
	}

	@Test
	public void testDdeleteIsFactual() {
		TestModel nonexistentModel = (TestModel) factory.getSyrup(TestModel.class).getById("1337");

		Assert.assertNull(nonexistentModel);
	}
}