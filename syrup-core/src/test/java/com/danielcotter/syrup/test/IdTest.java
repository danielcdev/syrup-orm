package com.danielcotter.syrup.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.danielcotter.syrup.SyrupFactory;
import com.danielcotter.syrup.test.model.BooleanModel;
import com.danielcotter.syrup.test.model.ByteModel;
import com.danielcotter.syrup.test.model.DoubleModel;
import com.danielcotter.syrup.test.model.FloatModel;
import com.danielcotter.syrup.test.model.LongModel;
import com.danielcotter.syrup.test.model.StringModel;

public class IdTest {

	private static SyrupFactory factory;
	private LongModel longModel = new LongModel(1l);
	private DoubleModel doubleModel = new DoubleModel(3.14d);
	private FloatModel floatModel = new FloatModel(14.3f);
	private BooleanModel booleanModel = new BooleanModel(true);
	private ByteModel byteModel = new ByteModel((byte) 0x64);
	private StringModel stringModel = new StringModel("abc");

	@BeforeClass
	public static void beforeTests() {
		factory = new SyrupFactory();

		factory.resetSyrup(LongModel.class);
		factory.resetSyrup(DoubleModel.class);
		factory.resetSyrup(FloatModel.class);
		factory.resetSyrup(BooleanModel.class);
		factory.resetSyrup(ByteModel.class);
		factory.resetSyrup(StringModel.class);
	}

	@Test
	public void longModel() {
		Assert.assertEquals(true, factory.getSyrup(LongModel.class).save(longModel));

		LongModel persistedLongModel = (LongModel) factory.getSyrup(LongModel.class).getById("1");
		Assert.assertEquals(longModel, persistedLongModel);
	}

	@Test
	public void doubleModel() {
		Assert.assertEquals(true, factory.getSyrup(DoubleModel.class).save(doubleModel));

		DoubleModel persistedDoubleModel = (DoubleModel) factory.getSyrup(DoubleModel.class).getById("3.14");
		Assert.assertEquals(doubleModel, persistedDoubleModel);
	}

	@Test
	public void floatModel() {
		Assert.assertEquals(true, factory.getSyrup(FloatModel.class).save(floatModel));

		FloatModel persistedFloatModel = (FloatModel) factory.getSyrup(FloatModel.class).getById("14.3");
		Assert.assertEquals(floatModel, persistedFloatModel);
	}

	@Test
	public void booleanModel() {
		Assert.assertEquals(true, factory.getSyrup(BooleanModel.class).save(booleanModel));

		BooleanModel persistedBooleanModel = (BooleanModel) factory.getSyrup(BooleanModel.class).getById("true");
		Assert.assertEquals(booleanModel, persistedBooleanModel);
	}

	@Test
	public void byteModel() {
		Assert.assertEquals(true, factory.getSyrup(ByteModel.class).save(byteModel));

		ByteModel persistedByteModel = (ByteModel) factory.getSyrup(ByteModel.class)
				.getById(new Byte((byte) 0x64).toString());
		Assert.assertEquals(byteModel, persistedByteModel);
	}

	@Test
	public void stringModel() {
		Assert.assertEquals(true, factory.getSyrup(StringModel.class).save(stringModel));

		StringModel persistedStringModel = (StringModel) factory.getSyrup(StringModel.class).getById("abc");
		Assert.assertEquals(stringModel, persistedStringModel);
	}
}