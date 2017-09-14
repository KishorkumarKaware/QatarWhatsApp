/**
 * 
 */
/**
 * @author admin
 *
 */
package com.vyom.whatsapp.rest.aduserreset;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * HTTP GET and POST example
 *
 * @author iampayload
 *
 */
public class aeRestCallADPassrest {

    // Main class
    public static void main(String[] args) throws Exception {

        aeRestCallADPassrest http = new aeRestCallADPassrest();

        //System.out.println("Testing send HTTP GET request   HTML output is below \n");

       
       // System.out.println( "--------------------------------------------------------------------------------------------------------");

        System.out.println("Raising Request in AE  \n");
        String token = http.getToken();
        System.out.println("Got Token :"+token);
        //String ticket_no=http.raiseRequest("nil", "002258",token);
        
        http.getStatus("678",token);
        
        
        

    }
        private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.04";

    // GET action URL, query string appended to the URL as ?stype=models
    private final String urlRaiseRequest = "http://10.41.11.12:8080/vae/rest/execute";

    // Post data or a payload
    
    // POST action URL
    private final String urlPOST = "http://10.41.11.12:8080/vae/rest/authenticate";
    String inputLine;

    public String getStatus(String aeRequestNo,String token) throws Exception
    {
    	 URL aeResultUrl = new URL("http://10.41.11.12:8080/vae/rest/workflowinstances/"+aeRequestNo.trim());

         // Send post request
         HttpURLConnection conn = (HttpURLConnection) aeResultUrl.openConnection();

         // basic reuqest header to simulate a browser request

         conn.setRequestMethod("GET");
         conn.setRequestProperty("X-session-token",token);
         conn.setRequestProperty("Content-Type","application/json");
         conn.setDoOutput(true);
              
         BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         String str1 = null;
 		//String inputLine,str = null;
         while ((inputLine = in.readLine()) != null)
             	str1=inputLine;
         System.out.println(str1);
         String workflowResponse=null;
         try{
        	 
        	 workflowResponse=new JSONObject(new JSONObject(str1).getString("workflowResponse").toString()).getString("message");;
         	}
         catch(JSONException e)
         {
        	 
         }
         String status=new JSONObject(str1).getString("status").toString();
         System.out.println("workflowResponse :"+workflowResponse);
         System.out.println("status :"+status);
         in.close();
         String rep=workflowResponse;
		return rep;
    }


    // HTTP POST request
    public String getToken() throws Exception {

        // POST example URL
        URL obj = new URL(urlPOST);

        // Send post request
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Basic reuqest header to simulate a browser request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Upgrade-Insecure-Requests", "1");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        String postDataBody = "username=admin@automationedge.com&password=vY78_h$ia";
        // Payload
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        // POST data added to the request as a part of body
        wr.writeBytes(postDataBody);
        wr.flush();
        wr.close();

        // Reading the HTML output of the POST HTTP request
        //int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String str = null;
        while ((inputLine = in.readLine()) != null)
        {
           str=inputLine;
        }
        
        String sessionToken = new JSONObject(str).getString("sessionToken");
       // System.out.println(success);
        //System.out.println(sessionToken);
        in.close();
        
        return sessionToken;
    }
    // HTTP GET request
    public String raiseRequest(String samName, String SourceID,String token ) throws Exception {
    	String RequestJSON="{"
        		+ "\"orgCode\": \"DEMO\","
        		+ "\"workflowName\": \"Reset password of ADuser\","
        		+ "\"userId\": \"Whatsapp\","
        		+ "\"sourceId\": null,"
        		+ "\"source\": \"Postman\","
        		+ "\"responseMailSubject\": \"null\","
        		+ "\"params\": ["
        		+ "{"
        		+ "\"name\": \"samaccountname\","
        		+ "\"value\":  \""+samName+"\","
        		+ "\"type\": \"String\","
        		+ "\"order\": 1,"
        		+ "\"secret\": false,"
        		+ "\"optional\": false,"
        		+ "\"displayName\": \"SamaccountName 1\""
        		+ "}"
        		+"]"
        		+ "}"
        		+ "";

        URL url = new URL(urlRaiseRequest);

        // Send post request
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // basic reuqest header to simulate a browser request
        con.setRequestMethod("GET");
        con.setRequestProperty("X-session-token",token);
        con.setRequestProperty("Content-Type","application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        
        // POST data added to the request as a part of body
        wr.writeBytes(RequestJSON);
        wr.flush();
        wr.close();
        // Reading the HTML output of the POST HTTP request
        //int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine,str = null;
        while ((inputLine = in.readLine()) != null)
            	str=inputLine;
        in.close();
        System.out.println(str);
        String aeRequestNo=new JSONObject(str).getBigInteger("automationRequestId").toString();
        String aeRequestStatus=new JSONObject(str).getString("responseCode").toString();
        
        System.out.println("Request Number:- " +aeRequestNo);
        System.out.println("Request Status:- " +aeRequestStatus);
       // Get Result From AE
        return aeRequestNo;
       
    }
}