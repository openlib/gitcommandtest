package com.paypal.api.payments;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ItemTestCase {

	public static final String NAME = "Sample Item";

	public static final String CURRENCY = "USD";

	public static final String PRICE = "10.50";

	public static final String QUANTITY = "5";

	public static final String SKU = "123";

	public static Item createItem() {
		Item item = new Item();
		item.setName(NAME);
		item.setCurrency(CURRENCY);
		item.setPrice(PRICE);
		item.setQuantity(QUANTITY);
		item.setSku(SKU);
		return item;
	}

	@Test
	public void testConstruction() {
		Item item = createItem();
		Assert.assertEquals(item.getName(), NAME);
		Assert.assertEquals(item.getCurrency(), CURRENCY);
		Assert.assertEquals(item.getPrice(), PRICE);
		Assert.assertEquals(item.getQuantity(), QUANTITY);
		Assert.assertEquals(item.getSku(), SKU);
	}
	
	@Test
	public void testTOJSON() {
		Item item = createItem();
		Assert.assertEquals(item.toJSON().length() == 0, false);
	}
	
	@Test
	public void testTOString() {
		Item item = createItem();
		Assert.assertEquals(item.toString().length() == 0, false);
	}

}
