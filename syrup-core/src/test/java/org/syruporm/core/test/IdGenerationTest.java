package org.syruporm.core.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.syruporm.core.SyrupFactory;
import org.syruporm.core.test.model.ByteModel;
import org.syruporm.core.test.model.DoubleModel;
import org.syruporm.core.test.model.FloatModel;
import org.syruporm.core.test.model.LongModel;
import org.syruporm.core.test.model.StringModel;

public class IdGenerationTest {

	private static SyrupFactory factory;
	private LongModel longModel = new LongModel(null);
	private DoubleModel doubleModel = new DoubleModel(null);
	private FloatModel floatModel = new FloatModel(null);
	private StringModel stringModel = new StringModel(null);
	private ByteModel byteModel = new ByteModel(null);

	@BeforeClass
	public static void beforeTests() {
		factory = new SyrupFactory();

		factory.resetSyrup(LongModel.class);
		factory.resetSyrup(DoubleModel.class);
		factory.resetSyrup(FloatModel.class);
		factory.resetSyrup(StringModel.class);
		factory.resetSyrup(ByteModel.class);
	}

	@Test
	public void longModel() {
		Assert.assertEquals(true, factory.getSyrup(LongModel.class).save(longModel));

		LongModel persistedLongModel = (LongModel) factory.getSyrup(LongModel.class).getById("0");
		Assert.assertEquals(longModel, persistedLongModel);
	}

	@Test
	public void doubleModel() {
		Assert.assertEquals(true, factory.getSyrup(DoubleModel.class).save(doubleModel));

		DoubleModel persistedDoubleModel = (DoubleModel) factory.getSyrup(DoubleModel.class).getById("0");
		Assert.assertEquals(doubleModel, persistedDoubleModel);
	}

	@Test
	public void floatModel() {
		Assert.assertEquals(true, factory.getSyrup(FloatModel.class).save(floatModel));

		FloatModel persistedFloatModel = (FloatModel) factory.getSyrup(FloatModel.class).getById("0");
		Assert.assertEquals(floatModel, persistedFloatModel);
	}

	@Test
	public void stringModel() {
		Assert.assertEquals(true, factory.getSyrup(StringModel.class).save(stringModel));

		StringModel persistedStringModel = (StringModel) factory.getSyrup(StringModel.class).getById("0");
		Assert.assertEquals(stringModel, persistedStringModel);
	}

	@Test
	public void byteModel() {
		Assert.assertEquals(true, factory.getSyrup(ByteModel.class).save(byteModel));

		ByteModel persistedByteModel = (ByteModel) factory.getSyrup(ByteModel.class).getById("0");
		Assert.assertEquals(byteModel, persistedByteModel);
	}
}