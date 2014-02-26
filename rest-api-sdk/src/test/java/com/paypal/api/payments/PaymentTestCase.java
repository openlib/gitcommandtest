package com.paypal.api.payments;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paypal.core.ConfigManager;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.QueryParameters;

public class PaymentTestCase {

	private static final Logger logger = Logger
			.getLogger(PaymentTestCase.class);

	private String createdPaymentID = null;

	private String saleID = null;

	public static final String CREATEDTIME = "2013-01-17T18:12:02.347Z";

	public static final String CANCELURL = "http://somedomain.com";

	public static final String RETURNURL = "http://somedomain.com";

	public static final String INTENT = "sale";

	public static final String ID = "12345";

	public static Payment payment;

	@BeforeClass
	public void beforeClass() throws PayPalRESTException {
		File testFile = new File(".",
				"src/test/resources/sdk_config.properties");
		Payment.initConfig(testFile);
	}

	@BeforeTest
	public void beforeTest() {

	}

	public static Payment createCallPayment() {
		Address billingAddress = AddressTestCase.createAddress();

		CreditCard creditCard = new CreditCard();
		creditCard.setBillingAddress(billingAddress);
		creditCard.setCvv2("874");
		creditCard.setExpireMonth(11);
		creditCard.setExpireYear(2018);
		creditCard.setFirstName("Joe");
		creditCard.setLastName("Shopper");
		creditCard.setNumber("4417119669820331");
		creditCard.setType("visa");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("7");

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction
				.setDescription("This is the payment transaction description.");
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		FundingInstrument fundingInstrument = new FundingInstrument();
		fundingInstrument.setCreditCard(creditCard);
		List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
		fundingInstrumentList.add(fundingInstrument);

		Payer payer = new Payer();
		payer.setFundingInstruments(fundingInstrumentList);
		payer.setPaymentMethod("credit_card");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		return payment;
	}

	public static Payment createPayment() {
		Address billingAddress = AddressTestCase.createAddress();

		CreditCard creditCard = new CreditCard();
		creditCard.setBillingAddress(billingAddress);
		creditCard.setCvv2("874");
		creditCard.setExpireMonth(11);
		creditCard.setExpireYear(2018);
		creditCard.setFirstName("Joe");
		creditCard.setLastName("Shopper");
		creditCard.setNumber("4111111111111111");
		creditCard.setType("visa");

		Details details = new Details();
		details.setShipping("10");
		details.setSubtotal("75");
		details.setTax("15");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("100");
		amount.setDetails(details);

		Payee payee = new Payee();
		payee.setMerchantId("NMXBYHSEL4FEY");

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setPayee(payee);
		transaction
				.setDescription("This is the payment transaction description.");
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		FundingInstrument fundingInstrument = new FundingInstrument();
		fundingInstrument.setCreditCard(creditCard);
		List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
		fundingInstrumentList.add(fundingInstrument);

		Payer payer = new Payer();
		payer.setFundingInstruments(fundingInstrumentList);
		payer.setPaymentMethod("credit_card");

		List<Links> links = new ArrayList<Links>();
		links.add(LinksTestCase.createLinks());

		RedirectUrls redirectUrls = RedirectUrlsTestCase.createRedirectUrls();

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setId(ID);
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		payment.setCreateTime(CREATEDTIME);
		payment.setLinks(links);
		payment.setRedirectUrls(redirectUrls);
		return payment;
	}

	public static Payment createPaymentForExecution() {
		Details details = new Details();
		details.setShipping("10");
		details.setSubtotal("75");
		details.setTax("15");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("100");
		amount.setDetails(details);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://www.hawaii.com");
		redirectUrls.setReturnUrl("http://www.hawaii.com");

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction
				.setDescription("This is the payment transaction description.");
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setRedirectUrls(redirectUrls);
		payment.setTransactions(transactions);
		return payment;
	}

	@Test()
	public void testConstruction() {
		Payment payment = createPayment();
		Assert.assertEquals(payment.getPayer().getPaymentMethod(),
				"credit_card");
		Assert.assertEquals(payment.getTransactions().get(0).getAmount()
				.getTotal(), "100");
		Assert.assertEquals(payment.getIntent(), INTENT);
		Assert.assertEquals(payment.getRedirectUrls().getCancelUrl(),
				RedirectUrlsTestCase.CANCELURL);
		Assert.assertEquals(payment.getRedirectUrls().getReturnUrl(),
				RedirectUrlsTestCase.RETURNURL);
		Assert.assertEquals(payment.getId(), ID);
		Assert.assertEquals(payment.getCreateTime(), CREATEDTIME);
		Assert.assertEquals(payment.getLinks().size(), 1);
	}

	@Test
	public void testCreatePaymentAPI() throws PayPalRESTException {
		logger.info("**** Create Payment ****");
		String clientID = ConfigManager.getInstance().getValue("clientID");
		String clientSecret = ConfigManager.getInstance().getValue(
				"clientSecret");
		TokenHolder.accessToken = new OAuthTokenCredential(clientID,
				clientSecret).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
		Payment payment = createCallPayment();
		Payment createdPayment = payment.create(TokenHolder.accessToken);
		logger.info("Request = " + Payment.getLastRequest());
		logger.info("Response = " + Payment.getLastResponse());
		logger.info("Payment created with ID = " + createdPayment.getId());
		createdPaymentID = createdPayment.getId();
		String json = Payment.getLastResponse();
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(json);
		JsonObject obj = jsonElement.getAsJsonObject();

		// State of a created payment object is approved
		Assert.assertEquals(true, obj.get("state").getAsString()
				.equalsIgnoreCase("approved"));
		saleID = obj.get("transactions").getAsJsonArray().get(0)
				.getAsJsonObject().get("related_resources").getAsJsonArray()
				.get(0).getAsJsonObject().get("sale").getAsJsonObject()
				.get("id").getAsString();
	}

	@Test(dependsOnMethods = { "testCreatePaymentAPI" })
	public void testGetPaymentAPI() throws PayPalRESTException {
		logger.info("**** Get Payment ****");
		logger.info("Setting Access Token = " + TokenHolder.accessToken);
		payment = Payment.get(TokenHolder.accessToken, createdPaymentID);
		logger.info("Request = " + Payment.getLastRequest());
		logger.info("Response = " + Payment.getLastResponse());
		logger.info("Retrieved Payment status = " + payment.getState());
	}

	@Test(enabled = false, dependsOnMethods = { "testGetPaymentAPI" })
	public void testExecutePayment() throws PayPalRESTException, IOException {
		logger.info("**** Execute Payment ****");
		Payment exPayment = createPaymentForExecution();
		exPayment = exPayment.create(TokenHolder.accessToken);
		logger.info("Create Payment Response: " + exPayment);
		logger.info("Payer ID:");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String payerId = in.readLine();
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		exPayment = exPayment
				.execute(TokenHolder.accessToken, paymentExecution);
		logger.info("Request = " + Payment.getLastRequest());
		logger.info("Response = " + Payment.getLastResponse());
		logger.info("Retrieved Payment status = " + exPayment.getState());
	}

	@Test(dependsOnMethods = { "testGetPaymentAPI" })
	public void testGetPaymentHistoryAPI() throws PayPalRESTException {
		logger.info("**** Get Payment History ****");
		logger.info("Setting Access Token = " + TokenHolder.accessToken);
		Map<String, String> containerMap = new HashMap<String, String>();
		containerMap.put("count", "10");
		PaymentHistory paymentHistory = Payment.get(TokenHolder.accessToken,
				containerMap);
		logger.info("Request = " + Payment.getLastRequest());
		logger.info("Response = " + Payment.getLastResponse());
		logger.info("Retrieved Payments count = " + paymentHistory.getCount());
	}
	
	@Test(dependsOnMethods = { "testGetPaymentHistoryAPI" })
	public void testGetPaymentHistoryQueryParamsAPI() throws PayPalRESTException {
		logger.info("**** Get Payment History ****");
		logger.info("Setting Access Token = " + TokenHolder.accessToken);
		QueryParameters params = new QueryParameters();
		params.setCount("10");
		PaymentHistory paymentHistory = Payment.get(TokenHolder.accessToken,
				params);
		logger.info("Request = " + Payment.getLastRequest());
		logger.info("Response = " + Payment.getLastResponse());
		logger.info("Retrieved Payments count = " + paymentHistory.getCount());
	}

	@Test(dependsOnMethods = { "testGetPaymentHistoryQueryParamsAPI" })
	public void testFailCreatePaymentAPI() {
		logger.info("**** Failing Create Payment ****");
		logger.info("Setting Access Token = " + TokenHolder.accessToken);
		Payment payment = new Payment();
		try {
			Payment createdPayment = payment.create(TokenHolder.accessToken);
		} catch (PayPalRESTException e) {
			Assert.assertEquals(e.getCause().getClass().getSimpleName(),
					"HttpErrorException");
		}
	}

	@Test(dependsOnMethods = { "testFailCreatePaymentAPI" })
	public void testFailGetPaymentAPI() {
		logger.info("**** Failing Get Payment ****");
		logger.info("Setting Access Token = " + TokenHolder.accessToken);
		try {
			Payment createdPayment = Payment.get(TokenHolder.accessToken,
					(String) null);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e != null,
					"Illegal Argument Exception not thrown for null arguments");
		} catch (PayPalRESTException e) {
			Assert.fail();
		}
	}

	@Test
	public void testTOJSON() {
		Payment payment = createPayment();
		Assert.assertEquals(payment.toJSON().length() == 0, false);
	}

	@Test
	public void testTOString() {
		Payment payment = createPayment();
		Assert.assertEquals(payment.toString().length() == 0, false);
	}

}
