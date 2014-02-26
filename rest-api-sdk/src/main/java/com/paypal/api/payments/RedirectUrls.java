package com.paypal.api.payments;

import com.paypal.core.rest.JSONFormatter;
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

public class RedirectUrls  {

	/**
	 * Url where the payer would be redirected to after approving the payment.
	 */
	private String returnUrl;
	
	/**
	 * Url where the payer would be redirected to after canceling the payment.
	 */
	private String cancelUrl;
	
	/**
	 * Default Constructor
	 */
	public RedirectUrls() {
	}


	/**
	 * Setter for returnUrl
	 */
	public RedirectUrls setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}
	
	/**
	 * Getter for returnUrl
	 */
	public String getReturnUrl() {
		return this.returnUrl;
	}


	/**
	 * Setter for cancelUrl
	 */
	public RedirectUrls setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
		return this;
	}
	
	/**
	 * Getter for cancelUrl
	 */
	public String getCancelUrl() {
		return this.cancelUrl;
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