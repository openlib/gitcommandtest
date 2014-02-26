package com.paypal.api.payments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.core.rest.PayPalRESTException;

public class CreditCardTestCase {

	private static final Logger logger = Logger
			.getLogger(CreditCardTestCase.class);

	public CreditCard creditCard;

	public static String createdCreditCardId = null;

	public static final String TYPE = "visa";

	public static final String NUMBER = "4111111111111111";

	public static final String FIRSTNAME = "Joe";

	public static final String LASTNAME = "Shopper";

	public static final int EXPMONTH = 11;

	public static final int EXPYEAR = 2018;

	public static final String CVV2 = "874";

	public static final String ID = "12345";

	public static final String PAYERID = "12345";

	public static final String STATE = "Approved";

	public static final String VALIDUNTIL = "2020";

	public static final Address BILLINGADDRESS = AddressTestCase
			.createAddress();

	@BeforeClass
	public void beforeClass() throws PayPalRESTException {
		File testFile = new File(".",
				"src/test/resources/sdk_config.properties");
		CreditCard.initConfig(testFile);
	}

	public static CreditCard createCreditCard() {
		CreditCard creditCard = new CreditCard();
		creditCard.setBillingAddress(BILLINGADDRESS).setExpireMonth(EXPMONTH)
				.setExpireYear(EXPYEAR).setFirstName(FIRSTNAME)
				.setLastName(LASTNAME).setNumber(NUMBER).setType(TYPE)
				.setCvv2(CVV2).setBillingAddress(BILLINGADDRESS).setId(ID)
				.setPayerId(PAYERID).setState(STATE).setValidUntil(VALIDUNTIL);
		return creditCard;
	}

	public static CreditCard createDummyCreditCard() {
		CreditCard creditCard = new CreditCard();
		creditCard.setBillingAddress(BILLINGADDRESS);
		creditCard.setExpireMonth(EXPMONTH);
		creditCard.setExpireYear(EXPYEAR);
		creditCard.setFirstName(FIRSTNAME);
		creditCard.setLastName(LASTNAME);
		creditCard.setNumber(NUMBER);
		creditCard.setType(TYPE);
		creditCard.setCvv2(CVV2);
		creditCard.setBillingAddress(BILLINGADDRESS);
		creditCard.setId(ID);
		creditCard.setPayerId(PAYERID);
		creditCard.setState(STATE);
		creditCard.setValidUntil(VALIDUNTIL);
		List<Links> links = new ArrayList<Links>();
		links.add(LinksTestCase.createLinks());
		creditCard.setLinks(links);
		return creditCard;
	}

	@Test
	public void testConstruction() {
		CreditCard creditCard = createDummyCreditCard();
		Assert.assertEquals(creditCard.getCvv2(), CVV2);
		Assert.assertEquals(creditCard.getExpireMonth(), EXPMONTH);
		Assert.assertEquals(creditCard.getExpireYear(), EXPYEAR);
		Assert.assertEquals(creditCard.getFirstName(), FIRSTNAME);
		Assert.assertEquals(creditCard.getLastName(), LASTNAME);
		Assert.assertEquals(creditCard.getNumber(), NUMBER);
		Assert.assertEquals(creditCard.getType(), TYPE);
		Assert.assertEquals(creditCard.getBillingAddress().getCity(),
				AddressTestCase.CITY);
		Assert.assertEquals(creditCard.getId(), ID);
		Assert.assertEquals(creditCard.getPayerId(), PAYERID);
		Assert.assertEquals(creditCard.getState(), STATE);
		Assert.assertEquals(creditCard.getValidUntil(), VALIDUNTIL);
		Assert.assertEquals(creditCard.getLinks().size(), 1);
	}

	@Test(dependsOnMethods = { "testSaleRefundAPIForNullRefund" })
	public void createCreditCardTest() throws PayPalRESTException {
		logger.info("**** Create CreditCard ****");
		logger.info("Generated Access Token = " + TokenHolder.accessToken);

		CreditCard creditCard = new CreditCard();
		creditCard.setExpireMonth(EXPMONTH);
		creditCard.setExpireYear(EXPYEAR);
		creditCard.setNumber(NUMBER);
		creditCard.setType(TYPE);
		this.creditCard = creditCard.create(TokenHolder.accessToken);
		logger.info("Request = " + CreditCard.getLastRequest());
		logger.info("Response = " + CreditCard.getLastResponse());
		logger.info("Credit Card created with ID = " + this.creditCard.getId());
		Assert.assertEquals(true,
				"ok".equalsIgnoreCase(this.creditCard.getState()));
		logger.info("Created Credit Card status = "
				+ this.creditCard.getState());
		createdCreditCardId = this.creditCard.getId();
	}

	@Test(dependsOnMethods = { "createCreditCardTest" })
	public void getCreditCard() throws PayPalRESTException {
		logger.info("**** Get CreditCard ****");
		logger.info("Generated Access Token = " + TokenHolder.accessToken);

		CreditCard retrievedCreditCard = CreditCard.get(
				TokenHolder.accessToken, createdCreditCardId);
		logger.info("Request = " + CreditCard.getLastRequest());
		logger.info("Response = " + CreditCard.getLastResponse());
		Assert.assertEquals(
				true,
				this.creditCard.getId().equalsIgnoreCase(
						retrievedCreditCard.getId()));
		logger.info("Retrieved Credit Card status = "
				+ retrievedCreditCard.getState());

	}
	
	@Test(dependsOnMethods = { "getCreditCard" })
	public void deleteCreditCard() throws PayPalRESTException {
		logger.info("**** Delete CreditCard ****");
		logger.info("Generated Access Token = " + TokenHolder.accessToken);

		CreditCard retrievedCreditCard = CreditCard.get(
				TokenHolder.accessToken, createdCreditCardId);
		retrievedCreditCard.delete(TokenHolder.accessToken);
		logger.info("Request = " + CreditCard.getLastRequest());
		logger.info("Response = " + CreditCard.getLastResponse());
	}

	@Test(dependsOnMethods = { "getCreditCard" })
	public void getCreditCardForNull() {
		logger.info("**** Get CreditCard Null ****");
		logger.info("Generated Access Token = " + TokenHolder.accessToken);

		CreditCard retrievedCreditCard;
		try {
			retrievedCreditCard = CreditCard.get(TokenHolder.accessToken, null);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e != null,
					"Illegal Argument Exception not thrown for null arguments");
		} catch (PayPalRESTException e) {
			Assert.fail();
		}
	}

	@Test
	public void testCreditCardUnknownFileConfiguration() {
		try {
			CreditCard.initConfig(new File("unknown.properties"));
		} catch (PayPalRESTException e) {
			Assert.assertEquals(e.getCause().getClass().getSimpleName(),
					"FileNotFoundException");
		}
	}

	@Test
	public void testCreditCardInputStreamConfiguration() {
		try {
			File testFile = new File(".",
					"src/test/resources/sdk_config.properties");
			FileInputStream fis = new FileInputStream(testFile);
			CreditCard.initConfig(fis);
		} catch (PayPalRESTException e) {
			Assert.fail("[sdk_config.properties] stream loading failed");
		} catch (FileNotFoundException e) {
			Assert.fail("[sdk_config.properties] file is not available");
		}
	}

	@Test
	public void testCreditCardPropertiesConfiguration() {
		try {
			File testFile = new File(".",
					"src/test/resources/sdk_config.properties");
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream(testFile);
			props.load(fis);
			CreditCard.initConfig(props);
		} catch (FileNotFoundException e) {
			Assert.fail("[sdk_config.properties] file is not available");
		} catch (IOException e) {
			Assert.fail("[sdk_config.properties] file is not loaded into properties");
		}
	}

	@Test
	public void testTOJSON() {
		CreditCard creditCard = createCreditCard();
		Assert.assertEquals(creditCard.toJSON().length() == 0, false);
	}

	@Test
	public void testTOString() {
		CreditCard creditCard = createCreditCard();
		Assert.assertEquals(creditCard.toString().length() == 0, false);
	}

}
