package com.opr.utils;

public interface Appconstants {
	// Webservice constants
	String NAME_SPACE = "http://service.opr.com/";
	String IP = "192.168.1.109";
	String URL = "http://" + IP + ":8084/OnlinePlotRental/OprWebService?wsdl";
	String IMAGE_FOLDER = "http://" + IP + ":8084/OnlinePlotRental/";

	// Webservice methods
	String LOGIN = "doLogin";
	String REGISTER = "doRegister";
	String GCM = "key";
	String UPDATE_GCM = "updateGcmKey";
	String BOOK_PROPERTY = "bookProperty";
	String BOOK_PLOT = "";

	// other values
	String USERNAME = "username";
	String PASSWORD = "password";
	String NAME = "name";
	String CITY = "city";
	String PHONE = "phone";
	String EMAIL = "email";
	String JSONdata = "JSONdata";
	String MAX_AMOUNT = "max_amount";
	String BEDROOMS = "no_of_bedrooms";
	String PROPERTY_ID = "propertyId";

	String REGISTER_MESSAGE = "Registered Successfully!";
	String NETWORK_ERROR = "Network Error";
	String REGISTER_FAILED = "Registration Failed!";
	String HOUSE = "SINGLE_HOUSE";
	String APPARTMENT = "appartment";
	String VILLA = "villa";
	String FLAT = "flat";
	String PROPERTY_TYPE = "property_type";
	String SEARCH = "";
	String SEARCH_DATA = "search_data";
	String BUNGALOW = "bungalow";
	String OFFICE = "office_building";
	String SALE = "For_sale";
	String CONTRACT_TYPE = "contract_type";
	String RESULT = "result";

	// Servlets URL

	String SERVLET = "http://" + IP
			+ ":8084/OnlinePlotRental/PropertyFetchingServlet?json=";
	String TEST_SERVLET = "http://" + IP
			+ ":8084/OnlinePlotRental/TestServlet?json=";
	String DETAILS_SERVLET = "http://" + IP
			+ ":8084/OnlinePlotRental/PropertyDetailsServlet?prop_id=";
	String VIDEO_SERVLET = "http://" + IP
			+ ":8084/OnlinePlotRental/VideoFetchingServlet?prop_id=";
	String PLOT_FETCH = "http://" + IP
			+ ":8084/OnlinePlotRental/PlotFetchingServlet?json=";

	String API_KEY_FOR_PLACE_SEARCH = "AIzaSyBcJj9lOY1dmQ8MOmC7v6UJSgNK_QIiKO0";
	
	String RENT = "For_rent";
	String PROPERTY_TITLE = "propertyName";
	String LOCATTION = "location";
}
