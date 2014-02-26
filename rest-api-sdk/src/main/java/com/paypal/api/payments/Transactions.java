package com.paypal.api.payments;

import com.paypal.core.rest.JSONFormatter;
import com.paypal.api.payments.Amount;
import java.util.Map;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.paypal.core.rest.HttpMethod;
import com.paypal.core.rest.RESTUtil;
import com.paypal.core.rest.QueryParameters;
import com.paypal.core.rest.APIContext;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class Transactions  {

	/**
	 * Amount being collected.
	 */
	private Amount amount;
	
	/**
	 * Default Constructor
	 */
	public Transactions() {
	}

	/**
	 * Parameterized Constructor
	 */
	public Transactions(Amount amount) {
		this.amount = amount;
	}
	

	/**
	 * Setter for amount
	 */
	public Transactions setAmount(Amount amount) {
		this.amount = amount;
		return this;
	}
	
	/**
	 * Getter for amount
	 */
	public Amount getAmount() {
		return this.amount;
	}

	/**
	 * Returns a JSON string corresponding to object state
	 * 
	 * @return JSON representation
	 */
	public String toJSON() {
		return JSONFormatter.toJSON(this);
	}

	@Override
	public String toString() {
		return toJSON();
	}
}