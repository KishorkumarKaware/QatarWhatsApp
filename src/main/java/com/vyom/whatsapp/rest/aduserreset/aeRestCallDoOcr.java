package com.vyom.whatsapp.rest.aduserreset;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.SystemOutLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automationedge.aeagent.exceptions.AEAgentAuthenticationFailed;
import com.automationedge.aeagent.rest.AESessionContext;
import com.automationedge.aeagent.util.AEAgentAppProperties;
import com.automationedge.aeagent.util.AEServerUtil;
import com.automationedge.aeagent.util.HostDetails;
import com.automationedge.aeutil.common.AEConstants;
import com.automationedge.aeutil.common.AEHttpClient;
import com.automationedge.aeutil.exception.AEUtilsException;
import com.automationedge.model.AuditLog;
import com.automationedge.model.AuthenticatedResponse;
import com.automationedge.model.AutomationRequest;
import com.automationedge.model.WorkletParameter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class aeRestCallDoOcr {
	private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/JSON;charset=UTF-8";
	private ObjectMapper mapper = new ObjectMapper();
	private String fileId;
	private static Logger LOGGER = LoggerFactory.getLogger(aeRestCallDoOcr.class.getName());
	private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

	private static final String CONTENT_TYPE = "Content-Type";

	
	private final String urlRaiseRequest = "http://10.41.16.61:8080/aeengine/rest/execute";
	// POST action URL
	// private final String urlPOST =
	// "http://192.168.1.194:8888/aeengine/rest/authenticate";
	private final static String urlPOST = "http://10.41.16.61:8080/aeengine/rest/authenticate";
	//
	private final static String urlUpload = "http://10.41.16.61:8080/aeengine/rest/file/upload?workflow_id=17";
	String inputLine;
	private final static String aeUsername="adminae";
	private final static String aePass="Admin123#";
	private static String automationRequestId = null;
	private static  String result=null;
	/*
	public static void main(String[] args) throws Exception {

		FileUploadTest test = new FileUploadTest();
		test.authenticate();
		File zipFile = new File("D:/Finesse/Documents/8149360340/vehicle_registration/sample3.pdf");
		test.uploadFile(zipFile);
		String fid=test.uploadFile(zipFile);
		System.out.println(fid);
		String aeRequestId=test.executeRequest(fid);
		//String aeRequestId="111";
		String result=test.getStatus(aeRequestId);
		while (result.contentEquals("Complete") || result.contentEquals("Failure")||result.contentEquals("ExecutionStarted")||result.contentEquals("New"))
		{
			result=test.getStatus(aeRequestId);
			System.out.println("Checked Status for "+aeRequestId+" :- "+ result);
		}
		System.out.println("Final Result :- "+result);

	}
*/
	public static String DoOCR(String fileName) throws Exception {

		aeRestCallDoOcr test = new aeRestCallDoOcr();
		test.authenticate();
		File zipFile = new File(fileName);
		test.uploadFile(zipFile);
		String fid=test.uploadFile(zipFile);
		System.out.println(fid);
		String aeRequestId=test.executeRequest(fid);
		//String aeRequestId="111";
		result=test.getStatus(aeRequestId);
		while (result.contentEquals("Complete") || result.contentEquals("Failure")||result.contentEquals("ExecutionStarted")||result.contentEquals("New"))
		{
			result=test.getStatus(aeRequestId);
			System.out.println("Checked Status for "+aeRequestId+" :- "+ result);
		}
		System.out.println("Final Result :- "+result);
		return result;
	}
	
	private String executeRequest(String fileId) throws UnsupportedCharsetException, ClientProtocolException, IOException, ParseException {
		AutomationRequest request = new AutomationRequest();
		request.setOrgCode("POCAE");
		request.setWorkflowName("GetVehicleDetails");
		request.setUserId("whatsapp");
		request.setSourceId(null);
		request.setSource("whatsapp");
		request.setResponseMailSubject(null);

		List<WorkletParameter> params = new ArrayList<>();
		WorkletParameter param1 = new WorkletParameter();
		param1.setName("InputFile");
		param1.setValue(fileId);
		param1.setType("File");
		param1.setOrder(1);
		param1.setSecret(false);
		param1.setOptional(false);
		param1.setDefaultValue(null);
		param1.setDisplayName("OCRInput");
		params.add(param1);
		
		request.setParams(params);
		HttpPost httpPost = null;

		httpPost = new HttpPost(urlRaiseRequest);
		Header xHeader = new BasicHeader("X-session-token", AESessionContext.getInstance().getToken());
		httpPost.addHeader(xHeader);

		CloseableHttpResponse httpResponse = null;
		Header header = new BasicHeader(CONTENT_TYPE, "application/json;charset=UTF-8");
		httpPost.setHeader(header);

		HttpEntity entity = new StringEntity(mapper.writeValueAsString(request),
				ContentType.create(APPLICATION_JSON_CHARSET_UTF_8));
		httpPost.setEntity(entity);

		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		
		httpResponse = httpClient.execute(httpPost);
		HttpEntity responseEntity = httpResponse.getEntity();
		String theString = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
		System.out.println("Reply for request :- "+theString);
	
		JSONParser parser = new JSONParser();
		JSONObject object = (JSONObject) parser.parse(theString);
		
		if (object.containsKey("automationRequestId")) {
			//JSONArray jarray = new JSONArray (object.toString().replace("{", "[").replace("}", "]"));
			String array[]=theString.replace("{","").replace("}", "").split(",");
			
			automationRequestId=array[2].replace("\"automationRequestId\":", "");
		}
		
		System.out.println("automationRequestId :- "+automationRequestId);
		
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Error");
		} else
			System.out.println("Success");

		return automationRequestId;
		
		
		
	}

	public void authenticate() throws Exception {

		HttpPost httpPost = new HttpPost(urlPOST);

		Header header = new BasicHeader(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED);

		httpPost.setHeader(header);
		// String encrypted = cryptUtil.encrypt(password);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", aeUsername));
		params.add(new BasicNameValuePair("password", aePass));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);

		httpPost.setEntity(entity);
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		HttpEntity responseEntity = response.getEntity();
		String theString = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
		AuthenticatedResponse aResponse = mapper.readValue(theString, AuthenticatedResponse.class);
		String token = aResponse.getSessionToken();
		System.out.println("Got Token :-" +token);
		if (StringUtils.isBlank(token)) {
			LOGGER.error(theString);
			throw new AEAgentAuthenticationFailed();
		}
		AESessionContext.getInstance().setToken(token);

		LOGGER.info(theString);

	}

	public String uploadFile(File zipFile) {
		HttpPost httpPost = null;
		httpPost = new HttpPost(urlUpload);
		Header xHeader = new BasicHeader("X-session-token", AESessionContext.getInstance().getToken());
		httpPost.addHeader(xHeader);

		CloseableHttpResponse httpResponse = null;
		try {

			if (!zipFile.exists()) {
				throw new RuntimeException("Specified output file <" + zipFile.getAbsolutePath() + "> does not exist.");
			}
			// application/zip
			FileBody fileBody = new FileBody(zipFile);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addPart("file", fileBody);
			HttpEntity multipart = builder.build();
			httpPost.setEntity(multipart);
			CloseableHttpClient httpClient = getHttpClient();
			httpResponse = httpClient.execute(httpPost);
			HttpEntity responseEntity = httpResponse.getEntity();
			String theString = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			System.out.println(theString);
			LOGGER.info("Upload file REST response status: " + httpResponse.getStatusLine());
			JSONParser parser = new JSONParser();
			JSONObject object = (JSONObject) parser.parse(theString);

			if (object.containsKey("fileId")) {
				fileId = (String) object.get("fileId");
			}

			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				return fileId;

			} else {
				LOGGER.error("Failed to upload a file on AE server");
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					String error = EntityUtils.toString(httpEntity);
					LOGGER.error("Error details: \n" + error);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (httpResponse != null)
				try {
					httpResponse.close();
				} catch (IOException e) {
					LOGGER.error("", e);
				}
		}

		return fileId;

	}

	private static CloseableHttpClient getHttpClient() {
		try {
			return AEHttpClient.getHttpClient(urlPOST);
		} catch (AEUtilsException e) {
			LOGGER.error("Error getting http client", e);
			return null;
		}
	}
	 public String getStatus(String aeRequestNo) throws Exception
	    {
	    	 URL aeResultUrl = new URL("http://10.41.16.61:8080/aeengine/rest/workflowinstances/"+aeRequestNo.trim());

	         // Send post request
	         HttpURLConnection conn = (HttpURLConnection) aeResultUrl.openConnection();

	         // basic reuqest header to simulate a browser request

	         conn.setRequestMethod("GET");
	         conn.setRequestProperty("X-session-token",AESessionContext.getInstance().getToken());
	         conn.setRequestProperty("Content-Type","application/json");
	         conn.setDoOutput(true);
	              
	         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         String str1 = null;
	 		//String inputLine,str = null;
	         while ((inputLine = in.readLine()) != null)
	             	str1=inputLine;
	         System.out.println(str1);
	         String workflowResponse=null;
	         String status=null;
			try{
	        	 JSONParser parser = new JSONParser();
	 			JSONObject object = (JSONObject) parser.parse(str1);
	 			if (object.containsKey("workflowResponse")) {
	 				workflowResponse = (String) object.get("workflowResponse");
				}
	 			if (object.containsKey("status")) {
	 				status = (String) object.get("status");
				}
	        	 //workflowResponse=new JSONObject(new JSONObject(str1).getString("workflowResponse").toString()).getString("message");;
	         	}
	         catch(JSONException e)
	         {
	        	 
	         }
	        // String status=new JSONObject(str1).getString("status").toString();
	         System.out.println("workflowResponse :"+workflowResponse);
	         System.out.println("status :"+status);
	         in.close();
	         //String rep=workflowResponse;
	         if (status.contentEquals("Complete"))
	         {
	        	 JSONParser parser = new JSONParser();
	        	 JSONObject object = (JSONObject) parser.parse(workflowResponse);
	        	 System.out.println("returning :- "+object.get("message").toString());
	        	 return object.get("message").toString();
	         }
	         else
	         {
	        	 return status;
	         }
	        	 
			
	    }

}
