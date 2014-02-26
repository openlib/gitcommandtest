// #CreatePayment Using Saved Card Sample
// This sample code demonstrates how you can process a 
// Payment using a previously saved credit card.
// API used: /v1/payments/payment
package com.paypal.api.payments.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import com.paypal.api.payments.*;
import com.paypal.api.payments.util.*;
import com.paypal.core.rest.*;

/**
 * @author lvairamani
 * 
 */
public class PaymentWithSavedCardServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger
			.getLogger(PaymentWithSavedCardServlet.class);

	public void init(ServletConfig servletConfig) throws ServletException {
		// ##Load Configuration
		// Load SDK configuration for
		// the resource. This intialization code can be
		// done as Init Servlet.
		InputStream is = PaymentWithSavedCardServlet.class
				.getResourceAsStream("/sdk_config.properties");

		try {
			PayPalResource.initConfig(is);
		} catch (PayPalRESTException e) {
			LOGGER.error(e.getMessage());
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	// ##Create
	// Sample showing to create a Payment using
	// Saved CreditCard as a FundingInstrument
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// ###CreditCard
		// A resource representing a credit card that can be
		// used to fund a payment.
		CreditCardToken creditCardToken = new CreditCardToken();
		creditCardToken.setCreditCardId("CARD-5BT058015C739554AKE2GCEI");

		// ###Details
		// Let's you specify details of a payment amount.
		Details details = new Details();
		details.setShipping("1");
		details.setSubtotal("5");
		details.setTax("1");

		// ###Amount
		// Let's you specify a payment amount.
		Amount amount = new Amount();
		amount.setCurrency("USD");
		// Total must be equal to the sum of shipping, tax and subtotal.
		amount.setTotal("7");
		amount.setDetails(details);

		// ###Transaction
		// A transaction defines the contract of a
		// payment - what is the payment for and who
		// is fulfilling it. Transaction is created with
		// a `Payee` and `Amount` types
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction
				.setDescription("This is the payment transaction description.");

		// The Payment creation API requires a list of
		// Transaction; add the created `Transaction`
		// to a List
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		// ###FundingInstrument
		// A resource representing a Payeer's funding instrument.
		// In this case, a Saved Credit Card can be passed to
		// charge the payment.
		FundingInstrument fundingInstrument = new FundingInstrument();
		fundingInstrument.setCreditCardToken(creditCardToken);

		// The Payment creation API requires a list of
		// FundingInstrument; add the created `FundingInstrument`
		// to a List
		List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
		fundingInstrumentList.add(fundingInstrument);

		// ###Payer
		// A resource representing a Payer that funds a payment
		// Use the List of `FundingInstrument` and the Payment Method
		// as 'credit_card'
		Payer payer = new Payer();
		payer.setFundingInstruments(fundingInstrumentList);
		payer.setPaymentMethod("credit_card");

		// ###Payment
		// A Payment Resource; create one using
		// the above types and intent as 'sale'
		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		try {
			// ###AccessToken
			// Retrieve the access token from
			// OAuthTokenCredential by passing in
			// ClientID and ClientSecret
			// It is not mandatory to generate Access Token on a per call basis.
			// Typically the access token can be generated once and
			// reused within the expiry window
			String accessToken = GenerateAccessToken.getAccessToken();

			// ### APIContext
			// APIContext which takes 'Access Token'
			// argument
			APIContext apiContext = new APIContext(accessToken);
			// Use this variant if you want to pass in a request id  
			// that is meaningful in your application, ideally 
			// a order id.
			/* 
			 * String requestId = Long.toString(System.nanoTime();
			 * APIContext apiContext = new APIContext(accessToken, requestId ));
			 */
			
			// Create a payment by posting to the APIService
			// using a valid AccessToken
			// The return object contains the status;
			Payment createdPayment = payment.create(apiContext);
			LOGGER.info("Created payment with id = " + createdPayment.getId()
					+ " and status = " + createdPayment.getState());
			req.setAttribute("response", Payment.getLastResponse());
		} catch (PayPalRESTException e) {
			req.setAttribute("error", e.getMessage());
		}
		req.setAttribute("request", Payment.getLastRequest());
		req.getRequestDispatcher("response.jsp").forward(req, resp);
	}
}
