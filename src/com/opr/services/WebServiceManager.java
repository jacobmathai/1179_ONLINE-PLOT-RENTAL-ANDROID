package com.opr.services;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.opr.utils.Appconstants;

public class WebServiceManager {
	String SOAP_ACTION = "";
	String response = "";

	public String userLogin(String username, String password) {

		SOAP_ACTION = Appconstants.NAME_SPACE + Appconstants.LOGIN;
		SoapObject object = new SoapObject(Appconstants.NAME_SPACE,
				Appconstants.LOGIN);
		object.addProperty(Appconstants.USERNAME, username);
		object.addProperty(Appconstants.PASSWORD, password);
		String result = upload(object);
		Log.d("TAG", result);
		return result;
	}

	private String upload(SoapObject object) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(object);
		HttpTransportSE transport = new HttpTransportSE(Appconstants.URL);

		try {
			transport.call(SOAP_ACTION, envelope);
			SoapPrimitive primitive = (SoapPrimitive) envelope.getResponse();
			response = primitive.toString();
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return "error";
		} catch (NullPointerException e) {
			e.printStackTrace();
			return "error";
		} catch (ClassCastException e) {
			e.printStackTrace();
			return "error";
		}
	}

	public String registerUser(String JSONdata) {
		// Log.d("TAG", "data : " + JSONdata);
		SOAP_ACTION = Appconstants.NAME_SPACE + Appconstants.REGISTER;
		SoapObject soapObject = new SoapObject(Appconstants.NAME_SPACE,
				Appconstants.REGISTER);
		soapObject.addProperty(Appconstants.JSONdata, JSONdata);
		String result = upload(soapObject);
		Log.d("TAG", result);
		return result;
	}

	public String registerGcm(String username, String gcmKey) {
		SOAP_ACTION = Appconstants.NAME_SPACE + Appconstants.UPDATE_GCM;
		SoapObject object = new SoapObject(Appconstants.NAME_SPACE,
				Appconstants.UPDATE_GCM);
		object.addProperty(Appconstants.USERNAME, username);
		object.addProperty(Appconstants.GCM, gcmKey);
		Log.d("GCM", "key :" + gcmKey);
		String result = upload(object);
		return result;

	}

	public String SearchForProperty(String data) {
		SOAP_ACTION = Appconstants.NAME_SPACE + Appconstants.SEARCH;
		SoapObject object = new SoapObject(Appconstants.NAME_SPACE,
				Appconstants.SEARCH);
		object.addProperty(Appconstants.SEARCH_DATA, data);
		String result = upload(object);
		return result;
	}

	public String bookproperty(String username, String propertyId) {
		SOAP_ACTION = Appconstants.NAME_SPACE + Appconstants.BOOK_PROPERTY;
		SoapObject newobject = new SoapObject(Appconstants.NAME_SPACE,
				Appconstants.BOOK_PROPERTY);
		newobject.addProperty(Appconstants.PROPERTY_ID, propertyId);
		newobject.addProperty(Appconstants.USERNAME, username);
		Log.d("TAG", propertyId);
		String result = upload(newobject);
		Log.d("TAG", result);
		return result;
	}

	public String bookPlot(String username, String plotId) {
		SOAP_ACTION = Appconstants.NAME_SPACE + Appconstants.BOOK_PLOT;
		SoapObject object = new SoapObject(Appconstants.NAME_SPACE, Appconstants.BOOK_PLOT);
		object.addProperty(Appconstants.USERNAME, username);
		object.addProperty(Appconstants.PROPERTY_ID, plotId);
		String result = upload(object);
		return result;
	}

}
