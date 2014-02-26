package com.paypal.api.payments;

import com.paypal.core.rest.JSONFormatter;
import java.util.Map;
import com.paypal.api.payments.Address;
import com.paypal.core.rest.PayPalRESTException;
import com.paypal.core.rest.PayPalResource;
import com.paypal.core.rest.HttpMethod;
import com.paypal.core.rest.RESTUtil;
import com.paypal.core.rest.QueryParameters;
import com.paypal.core.rest.APIContext;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class ShippingAddress extends Address {

	/**
	 * Name of the recipient at this address.
	 */
	private String recipientName;
	
	/**
	 * Default Constructor
	 */
	public ShippingAddress() {
	}

	/**
	 * Parameterized Constructor
	 */
	public ShippingAddress(String recipientName) {
		this.recipientName = recipientName;
	}
	

	/**
	 * Setter for recipientName
	 */
	public ShippingAddress setRecipientName(String recipientName) {
		this.recipientName = recipientName;
		return this;
	}
	
	/**
	 * Getter for recipientName
	 */
	public String getRecipientName() {
		return this.recipientName;
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