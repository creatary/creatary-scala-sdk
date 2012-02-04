package com.creatary;

import com.creatary.internal.CreataryException;

import com.creatary.api.ChargeRequest;
import com.creatary.api.LocationResponse;
import com.creatary.api.Sms;

public class ScalaJavaDemo {

	public static void main(String[] args) {
		Creatary creatary = new Creatary("telcoassetmarketplace.com");
		String access_token = "valid_access_token";
		// sending sms
		try {
			Sms sms = new Sms("Hello world", null, null);
			creatary.send(sms, access_token);
		} catch (CreataryException e) {
			System.out.println("something wrong");
		}

		// fetching location
		LocationResponse location = null;
		try {
			location = creatary.findLocation(access_token);
		} catch (CreataryException e) {
			System.out.println("something wrong");
		}

		System.out.println(location);

		// requesting charging
		try {
			ChargeRequest chargeReq = new ChargeRequest("CODE", null, "10");
			creatary.charge(chargeReq, access_token);
		} catch (CreataryException e) {
			System.out.println("something wrong");
		}

	}
}
