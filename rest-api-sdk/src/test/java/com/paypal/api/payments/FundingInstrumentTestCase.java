package com.paypal.api.payments;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FundingInstrumentTestCase {

	public static final CreditCard CREDITCARD = CreditCardTestCase
			.createCreditCard();

	public static FundingInstrument createFundingInstrument() {
		FundingInstrument fundingInstrument = new FundingInstrument();
		CreditCard creditCard = CreditCardTestCase.createCreditCard();
		fundingInstrument.setCreditCard(creditCard);
		CreditCardToken creditCardToken = CreditCardTokenTestCase
				.createCreditCardToken();
		fundingInstrument.setCreditCardToken(creditCardToken);
		return fundingInstrument;
	}

	@Test
	public void testConstruction() {
		FundingInstrument fundingInsturment = createFundingInstrument();
		Assert.assertEquals(fundingInsturment.getCreditCardToken()
				.getCreditCardId(), CreditCardTokenTestCase.CREDITCARDID);
		Assert.assertEquals(fundingInsturment.getCreditCard().getCvv2(),
				CreditCardTestCase.CVV2);
	}

	@Test
	public void testTOJSON() {
		FundingInstrument fundingInsturment = createFundingInstrument();
		Assert.assertEquals(fundingInsturment.toJSON().length() == 0, false);
	}

	@Test
	public void testTOString() {
		FundingInstrument fundingInsturment = createFundingInstrument();
		Assert.assertEquals(fundingInsturment.toString().length() == 0, false);
	}

}
