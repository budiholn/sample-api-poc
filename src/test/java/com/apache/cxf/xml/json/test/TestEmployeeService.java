package com.apache.cxf.xml.json.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

public class TestEmployeeService {

	/**
	 * main method to invoke test method
	 * @param args
	 */
	public static void main(String[] args) {

		/*String requestURL = "http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/addemployee";
		String httpMethod = HttpMethod.POST;
		String contentType = MediaType.APPLICATION_XML;
		String accept = MediaType.APPLICATION_FORM_URLENCODED;
		String requestParam = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+				"<EmployeeType xmlns=\"http://shap.com/Employee\">"
				+					"<eno>344</eno>"
				+					"<name>Ian Healy</name>"
				+					"<salary>50</salary>"
				+ 				"</EmployeeType>";

		String[] requestParams = {requestURL, httpMethod, requestParam, contentType, accept};*/


		String requestURL = "http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/getemployee/238";
		String httpMethod = HttpMethod.GET;
		String contentType = MediaType.APPLICATION_FORM_URLENCODED;
		String accept = MediaType.APPLICATION_JSON;
		String requestString =  "{" 
				+  		"\"eno\": 564," 
				+  		"\"name\": \"Graham Thorpe\","
				+  		"\"salary\": 45,"
				+  	"}";

		String[] requestParams = null;
		requestParams = new String[]{requestURL, httpMethod, contentType, accept, requestString};

		String responseFromService = testEmployeeService(requestParams);
		System.out.println("Response String: " + responseFromService);
	}

	/**
	 * This method uses HttpURLConnection to invoke exposed Restful web service and returns the response string to the calling client
	 * @param requestParams
	 * @return
	 */
	public static String testEmployeeService(String[] requestParams) {

		// local variables
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		OutputStreamWriter outputStreamWriter = null;
		String responseMessageFromServer = null;
		String responseXML = null; 

		try {   
			url = new URL(requestParams[0]);
			httpURLConnection = (HttpURLConnection) url.openConnection();   
			httpURLConnection.setRequestMethod(requestParams[1]);   
			httpURLConnection.setRequestProperty("Content-Type", requestParams[2]);
			httpURLConnection.setRequestProperty("Accept", requestParams[3]);
			httpURLConnection.setDoOutput(true);

			/*outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
			outputStreamWriter.write(requestParams[4]);
			outputStreamWriter.flush();*/

			System.out.println("Response code: " + httpURLConnection.getResponseCode());

			if (httpURLConnection.getResponseCode() == 200) {

				responseMessageFromServer = httpURLConnection.getResponseMessage();
				System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);
				responseXML = getResponseXML(httpURLConnection);
			} 
		}
		catch(Exception  ex){
			ex.printStackTrace();
		}
		finally{
			httpURLConnection.disconnect();
		}
		return responseXML; 
	}

	/**
	 * This method is used to get response XML from the HTTP GET Request created for Authorization WireKey
	 * @param httpURLConnection
	 * @return stringBuffer.toString()
	 * @throws IOException
	 */
	private static String getResponseXML(HttpURLConnection httpURLConnection) throws IOException{

		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		String readSingleLine = null;

		try{
			// read the response stream AND buffer the result into a StringBuffer
			inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);

			// reading the XML response content line BY line 
			while ((readSingleLine = bufferedReader.readLine()) != null) {
				stringBuffer.append(readSingleLine);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			// finally close all operations
			bufferedReader.close();
			httpURLConnection.disconnect();
		}
		return stringBuffer.toString();
	}
}