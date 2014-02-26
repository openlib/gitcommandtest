package com.paypal.api.payments;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PayeeTestCase {

	public static final String MERCHANTID = "12345";

	public static final String EMAIL = "somename@somedomain.com";

	public static final String PHONE = "716-298-1822";

	public static Payee createPayee() {
		Payee payee = new Payee();
		payee.setMerchantId(MERCHANTID);
		payee.setEmail(EMAIL);
		payee.setPhone(PHONE);
		return payee;
	}

	@Test
	public void testConstruction() {
		Payee payee = createPayee();
		Assert.assertEquals(payee.getMerchantId(), MERCHANTID);
		Assert.assertEquals(payee.getEmail(), EMAIL);
		Assert.assertEquals(payee.getPhone(), PHONE);
	}

	@Test
	public void testTOJSON() {
		Payee payee = createPayee();
		Assert.assertEquals(payee.toJSON().length()==0, false);
	}
	
	@Test
	public void testTOString() {
		Payee payee = createPayee();
		Assert.assertEquals(payee.toString().length()==0, false);
	}

}
