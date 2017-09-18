package com.vyoms.whatsapp.type;
import java.io.File;

import java.io.IOException;

import java.net.ConnectException;

import java.util.Date;

import java.util.HashMap;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;

import org.openqa.selenium.Keys;

import org.openqa.selenium.NoSuchElementException;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import com.vyom.rasa.integration.validate_Response;
import com.vyom.whatsapp.rest.aduserreset.aeRestCallDoOcr;
import com.vyoms.whatsapp.implementation.WhatsAppImplementation;

import com.vyoms.whatsapp.model.AgentMaster;

import com.vyoms.whatsapp.model.EmpMaster;

import com.vyoms.whatsapp.model.PinMaster;

import com.vyoms.whatsapp.model.Policy;
import com.vyoms.whatsapp.model.VehicalDetails;
import com.vyoms.whatsapp.service.AgentMasterService;

import com.vyoms.whatsapp.service.EmpMasterService;

import com.vyoms.whatsapp.service.PinMasterService;

import com.vyoms.whatsapp.util.Constants;

import com.vyoms.whatsapp.util.DriverUtility;

import util.downloadFilePath;

public class WhatsApp implements WhatsAppImplementation {


	public static Actions actions;

	public static int browserType;

	public static WebDriver driver = null;

	public static boolean init = false;

	public static HashMap<String, String> msgs = null;

	public static boolean inLoop = false;

	public static HashMap<String, Date> lastReply = new HashMap<>();

	public static String rasa_Response=null;

	/*

	 * public WhatsApp(PinMasterService pinService, AgentMasterService

	 * agentService, EmpMasterService empService) { //pinMasterService =

	 * pinService; //agentMasterService = agentService; //empMasterService =

	 * empService; }

	 */

	public static String downloadImage() throws InterruptedException {

		Thread.sleep(2000);

		driver.switchTo().defaultContent();

		WebDriverWait wait = new WebDriverWait(driver, 15);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main")));

		WebElement main = driver.findElement(By.id("main"));

		String result = null;

		List<WebElement> msgs = main.findElements(By.className("image-thumb"));

		WebElement msg = msgs.get(msgs.size() - 1);

		try {

			wait = new WebDriverWait(driver, 15);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("spinner-container")));

			WebElement resultImg = msg.findElement(By.tagName("img"));

			resultImg.click();

			driver.switchTo().defaultContent();

			wait = new WebDriverWait(driver, 15);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("app")));

			WebElement app = driver.findElement(By.id("app"));

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("media-panel-header")));

			WebElement mediaPanelHeader = app.findElement(By.className("media-panel-header"));////*[@id="app"]/div/span[2]/div/div/div[2]/div[1]

			//WebElement mediaPanelHeaderpan = (WebElement) app.findElement(By.className("menu menu-horizontal media-panel-tools"));

			//WebElement downloadpanel = (WebElement) mediaPanelHeader.findElement(By.tagName("menu menu-horizontal media-panel-tools"));

			WebElement hover =  mediaPanelHeader.findElement(By.xpath("div[2]"));

			//System.out.println(hover.size());

			WebElement downloadButton =  hover.findElement(By.xpath("div[4]"));

			//System.out.println(downloadButton.size());

			try {

				downloadButton.click();

			} catch (Exception e) {

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='app']/div/span[2]/div/div/div[2]/div[1]/div[2]/div[4]/div")));

				driver.findElement(By.xpath("//*[@id='app']/div/span[2]/div/div/div[2]/div[1]/div[2]/div[4]/div")).click();

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

			WebElement closeButton = mediaPanelHeader.findElement(By.tagName("button"));

			closeButton.click();

		} catch (Exception e) {

			try {

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='app']/div/span[2]/div/div/div[2]/div[1]/div[2]/div[4]/div")));

				driver.findElement(By.xpath("//*[@id='app']/div/span[2]/div/div/div[2]/div[1]/div[2]/div[4]/div")).click();

			} catch (Exception e1) {

				// TODO Auto-generated catch block

				e1.printStackTrace();

			}

		}

		System.out.println("Message=" + result);

		Thread.sleep(2000);

		return result;

	}



	public long SourceIDSeq = 94;//113;



	AgentMasterService agentMasterService;

	public HashMap<String, AgentMaster> agents;

	String downloadFilepath = Constants.downloadFilepath;

	public HashMap<String, EmpMaster> employees;

	EmpMasterService empMasterService;

	public String gTickets;

	public HashMap<String, PinMaster> pinCodes;

	PinMasterService pinMasterService;

	public HashMap<String, Policy> policy;

	protected String rep;

	// Changes for Intermidate Id

	public HashMap<String, String> validIntermediateIds;
	/*

                public WhatsApp(PinMasterService pinMasterService, AgentMasterService agentMasterService,

                                                EmpMasterService empMasterService) {

                                // TODO Auto-generated constructor stub

                }*/

	public WhatsApp() {

		// TODO Auto-generated constructor stub

	}

	public void cleanBrowser() {

		String cmd = "Taskkill /IM chromedriver.exe /F";

		try {

			Process p = Runtime.getRuntime().exec(cmd);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public String getFileName(String filePath) {

		String fileName = new StringBuffer().append(" ")

				.append(filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length())).append(" ").toString();

		return fileName.replaceAll("\\P{Print}", "").trim();

	}

	public String getMessage(String msg) {

		String message = new StringBuffer().append(" ").append(msg).append(" ").toString();

		return message.replaceAll("\\P{Print}", "").trim();

	}

	public void init(int bType) {

		if (init == false) {

			System.out.println("Startig INIT ");

			driver = DriverUtility.getDriver(bType, downloadFilepath);

			actions = new Actions(driver);

			msgs = new HashMap<String, String>();

			policy = new HashMap<>();

			browserType = bType;

		}

	}

	// Changes for Intermidate Id

	public Boolean isValidIntermediateId(String id) {

		boolean result = false;

		if (validIntermediateIds.containsKey(id))

			result = true;

		return result;

	}

	public String messageReply(String from, String msg) throws Exception {

		from=from.replace("+91", "").replace(" ", "");

		String reply="Sorry! You have entered invalid input.";

		Pattern agent_Im_Id_pattrn = Pattern.compile("[a-zA-Z]{4}\\d{5}");

		// agent_Im_Id.matcher(msg);

		Matcher agent_Im_Id = agent_Im_Id_pattrn.matcher(msg);

		Pattern intermediateIdPattern = Pattern.compile("(\\d{12})");

		intermediateIdPattern.matcher(msg);


		Pattern.compile(

				"(([A-D]{1}|[a-d]{1})[1-4]{1})|(([A-D]{1}|[a-d]{1})[1-4]{1}[$]{1}[0-9]{1,7})|(([A-D]{1}|[a-d]{1})[1-4]{1}[@]{1}[0-9]{1,5})|(([A-D]{1}|[a-d]{1})[1-4]{1}[$]{1}[0-9]{1,7}[@]{1}[0-9]{1,5})|(([A-D]{1}|[a-d]{1})[1-4]{1}[@]{1}[0-9]{1,5}[$]{1}[0-9]{1,7})");

		from.split(" ");



		try{

			System.out.println("massage from "+from+" is "+msg);

			if(msg.endsWith("pages")){

				msg = "photo";

			}
		
			if((msg.contains("@")) || msg.equalsIgnoreCase("i") || msg.equalsIgnoreCase("ii") || msg.equalsIgnoreCase("iii") || msg.equalsIgnoreCase("no thanks") || msg.contains("91"))
			{

				if(msg.contains("@") || msg.contains(".com"))
				{
					if(!getMail(msg)){
						reply ="Sorry! Enterd Email ID is invalid.";
						sendMessage(from, reply);
					}else
					{
						rasa_Response = msg;
					}
				}else if(msg.contains("91")){

					String[] str = msg.split(" ");

					for(int i=0; i<str.length; i++){
						str[i] =str[i].replace("+", "");
						if(str[i].contains("91")){
							msg = "+"+str[i] +" "+ str[i+1];
							break;
						}
					}
					System.out.println("Contact number="+msg);
					policy.get(from).setContactNo(msg);

				}else
				{
					rasa_Response = msg;
				}
			}else if(msg.contains("Chassis") || msg.contains("chassis"))
			{
				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				policy.get(from).setChassis_no(rasa_Response);
				System.out.println("Chassis Number="+rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Please provide all the other details as well (eg Engine number is CAB2324).";
					sendMessage(from, reply);
				}
			}else if(msg.contains("Engine") || msg.contains("engine"))
			{

				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				System.out.println("Engine="+rasa_Response);
				policy.get(from).setEngine_no(rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Please provide all the other details as well (eg Model is Sunny).";
					sendMessage(from, reply);
				}
			}else if(msg.contains("model") || msg.contains("Model"))
			{

				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				System.out.println("Model="+rasa_Response);
				policy.get(from).setModel(rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Please provide all the other details as well (eg Make is Nissan).";
					sendMessage(from, reply);
				}
			}else if(msg.contains("Make") || msg.contains("make"))
			{

				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				System.out.println("Make="+rasa_Response);
				policy.get(from).setMake(rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Please provide all the other details as well (eg Year is 2000).";
					sendMessage(from, reply);
				}
			}else if(msg.contains("year") || msg.contains("Year"))
			{

				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				System.out.println("Year="+rasa_Response);
				policy.get(from).setYear(rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Please provide all the other details as well (eg Cylinders are 3).";
					sendMessage(from, reply);
				}
			}else if(msg.contains("Cylinders") || msg.contains("cylinders") || msg.contains("Cylinders") || msg.contains("cylinder"))
			{
				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				System.out.println("Cylinders="+rasa_Response);
				policy.get(from).setCylinders(rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Please provide all the other details as well (eg Vehicle value is 560000).";
					sendMessage(from, reply);
				}
			}else if(msg.contains("value") || msg.contains("Value"))
			{

				String[] temp = msg.split(" ");
				msg = temp[temp.length-1];
				rasa_Response =msg;
				System.out.println("Approx value="+rasa_Response);
				policy.get(from).setVehicalValue(rasa_Response);
				if(!policy.get(from).isPdf()){
					reply ="Thank you! Please type \"y\" to contenue.";
					sendMessage(from, reply);
				}
				
			}else if(msg.contains("no thanks") || msg.contains("No thanks"))
			{

				rasa_Response =msg;
				
			}else
			{
				rasa_Response=(new validate_Response()).check_response(msg);
				System.out.println("Rasa default responce="+rasa_Response);
			}


		}catch(ConnectException e){

			reply="Hi,"+from+" Unnable to connect with RASA.";

			sendMessage(from, reply);

			policy.get(from).setStart(false);

			policy.remove(from);

			reply="Hi,"+from+" Your session has ben teminated Please ping us again \n Thank You !! ";

			sendMessage(from, reply);

		}
		catch(Exception e){}


		if ((!policy.containsKey(from) && (rasa_Response.equalsIgnoreCase("greet") || rasa_Response.equalsIgnoreCase("hi")) && from.contains("8698206727")  || from.contains("8149360340")))
		{

			final Policy saveImageForAgent = new Policy();

			saveImageForAgent.setStart(true);

			policy.put(from, saveImageForAgent);

			//reply="Hi,"+from+" Please Enter the customer name !!";

			reply="Hi "+from+", how can I help you today?"

                        + "\nPlease select from following options."

                        + "\n1. Buy new Motor Insurance"

                        + "\n2. Renew motor insurance"

                        + "\n3. Type 'Exit' to terminate the conversation any time";

			sendMessage(from, reply);

			msg="";

		}

		System.out.println("Rasa Responce="+rasa_Response);

		if(policy.containsKey(from) && policy.get(from).isStart() && rasa_Response.equalsIgnoreCase("Buy new Motor Insurance"))
		{
			policy.get(from).setOption(rasa_Response);
			reply="Please enter the chassis number or attach a vehicle registration card (Please upload image in pdf format)";

			sendMessage(from, msg);

		}

		/*if (policy.containsKey(from) && policy.get(from).isStart() && policy.get(from).getPDFOption()==null && rasa_Response.equalsIgnoreCase("photo"))

		{

			policy.get(from).setPDFOption(rasa_Response);

			reply="Currently, I am in learning phase, so you will get a slight delay in response, kindly cooperate with me.";

			sendMessage(from, reply);
		}*/

		if(policy.containsKey(from) && !policy.get(from).isChequePDF() && policy.get(from).isStart() && rasa_Response.contains("photo"))

		{
			policy.get(from).setPDFOption(rasa_Response);

			String destination="D:\\Finesse\\Documents\\"+"\\"+from+"\\"+"\\vehicle_registration\\";

			System.out.println("Downloading Image...");

			File file = new File (downloadFilepath);     

			String[] myFiles;   

			if(file.isDirectory()){

				myFiles = file.list();

				for (int i=0; i<myFiles.length; i++) {

					File myFile = new File(file, myFiles[i]);

					myFile.delete();
				}
			}

			//downloadImage();

			boolean   r= downloadDoc();
			//Thread.sleep(2000);
			try{
				//System.out.println(downloadFilePath.getTheNewestFile(downloadFilepath, "jpeg").toString());

				//downloadFilePath.copyFile_Directory(downloadFilePath.getTheNewestFile(downloadFilepath, "pdf").toString(), from, destination);

				//FileUtils.copyFileToDirectory(new File(downloadFilePath.getTheNewestFile(downloadFilepath, "pdf").toString()), new File(destination));

				FileUtils.copyFileToDirectory(new File (downloadFilePath.getTheNewestFile(Constants.downloadFilepath, "pdf").toString()), new File(destination));

				
				System.out.println(downloadFilePath.getTheNewestFile(destination, "pdf"));

				policy.get(from).setChequePDF(true);
				if(r)
				{
					reply="Hi,"+from+" PDF is successfully saved";//+"\n Do You want to upload another image (Y/N)";
					policy.get(from).setPdf(true);
				}else{
					reply ="Sorry! I missed this file. Can please upload file again";
				}
				
				sendMessage(from, reply);
				
				reply="Hi,"+from+" Your Pdf is processing.......Please wait...";

			}catch(Exception e){

				reply ="Sorry! I missed this file. Can you please upload file again";
			}

			sendMessage(from, reply);
			String result = aeRestCallDoOcr.DoOCR(downloadFilePath.getTheNewestFile(destination, "pdf").toString());
			
			System.out.println("Result from OCR="+ result);
			if(result.contains("error") || result.contains("error:")){
				reply = result;
				sendMessage(from, reply);
			}else{
				//result =result.replace("|", "");
				String[] ocrResult = result.split("\\|");
				System.out.println(ocrResult[0]);
				for(int i=0;i< ocrResult.length;i++){
					System.out.println("ocr Result="+ocrResult[i]);
					String[] keypair=ocrResult[i].split(",");
					if(keypair[0].equalsIgnoreCase("owner")){
						policy.get(from).setName(keypair[1]);
					}else if(keypair[0].equalsIgnoreCase("ChassisNo")){
						policy.get(from).setChassis_no(keypair[1]);
					}else if(keypair[0].equalsIgnoreCase("EngineNo")){
						policy.get(from).setEngine_no(keypair[1]);
					}else if(keypair[0].equalsIgnoreCase("Model")){
						policy.get(from).setModel(keypair[1]);
					}else if(keypair[0].equalsIgnoreCase("year")){
						policy.get(from).setYear(keypair[1]);
					}else if(keypair[0].equalsIgnoreCase("Make")){
						policy.get(from).setMake(keypair[1]);
					}else if(keypair[0].equalsIgnoreCase("Cylinders")){
						policy.get(from).setCylinders(keypair[1]);
					}else if(keypair[0].toLowerCase().contains("value")){
						policy.get(from).setVehicalValue(keypair[1]);
					}
				}
				
			}
			
			
			/*policy.get(from).setChassis_no("sdhg8389439");
			policy.get(from).setEngine_no("9348549864");
			policy.get(from).setMake("akjsdh");
			policy.get(from).setModel("sdj");
			policy.get(from).setCylenders(null);
			policy.get(from).setVehicalValue("2014");
			policy.get(from).setModel("sunny");
			policy.get(from).setYear(null);*/
			if(policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance")){
				reply ="Dear Customer, your vehicle details from your given document are as follow \\-"
						+"\nChassis Number: "+policy.get(from).getChassis_no()
						+"\nEngine Number: "+policy.get(from).getEngine_no()
						+"\nMake: "+policy.get(from).getMake()
						+"\nModel: "+policy.get(from).getModel()
						+"\nYear: "+policy.get(from).getYear()
						+"\nCylinders: "+policy.get(from).getCylinders()
						+"\nApprox. Vehicle Value: QAR "+policy.get(from).getVehicalValue()
						+"\nType \"y\"to confirm the details (or) Type \"n\" to modify the details";
			}else
			{
				reply ="Dear Customer, your vehicle details from your given document are as follow \\- "
						+"\nPolicy Number: "+policy.get(from).getPolicyQuoteNo()
						+"\nChassis Number: "+policy.get(from).getChassis_no()
						+"\nEngine Number: "+policy.get(from).getEngine_no()
						+"\nMake: "+policy.get(from).getMake()
						+"\nModel: "+policy.get(from).getModel()
						+"\nYear: "+policy.get(from).getYear()
						+"\nCylinders: "+policy.get(from).getCylinders()
						+"\nApprox. Vehicle Value: QAR "+policy.get(from).getVehicalValue()  
						+"\nType \"y\" to confirm the details (or) Type \"n\" to modify the details";
			}
			sendMessage(from, reply);
		}

		if(policy.containsKey(from) && rasa_Response.equalsIgnoreCase("no") && policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance"))
		{
			reply="Fine..! Which field you want to modify?. Please specify the field name and related value one by one(eg. Chassis number is WAUJC6K9N027861) or upload file again.";
			sendMessage(from, reply);
		}		
		if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getChassis_no() == null))
		{
			reply="Please provide your correct Chassis Number.(eg. Chassis number is WAUJC6K9N027861)";
			sendMessage(from, reply);
		}else if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getEngine_no() == null || rasa_Response.contains("Chassis") || rasa_Response.contains("chassis")))
		{
			reply="Please provide your correct Engine Number.(eg. Engine Number is CAB02294)";
			sendMessage(from, reply);
		}else if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getMake() == null || rasa_Response.contains("Engine") || rasa_Response.contains("engine")))
		{
			reply="Please provide manufactorar of  your vehicle (Make).(eg. Make is Nissan)";
			sendMessage(from, reply);
		}else if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getModel() == null || rasa_Response.contains("Make") || rasa_Response.contains("make")))
		{
			reply="Please provide your vehicle’s model. ( eg. model is Sunny)";
			sendMessage(from, reply);
		}else if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getYear() == null || rasa_Response.contains("Model") || rasa_Response.contains("model")))
		{
			//policy.get(from).setYear("2005");
			reply="Please provide manufacturing year of your vehicle. ( eg. year is 2000)";
			sendMessage(from, reply);
		}else if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getCylinders() == null || rasa_Response.contains("Year") || rasa_Response.contains("year")))
		{
			//policy.get(from).setCylenders("4");
			reply="How many cylinders are there in your vehicle? (eg. Cylinders are 4)";
			sendMessage(from, reply);
		}else if((policy.get(from).isPdf() && policy.containsKey(from) && policy.get(from).getVehicalValue() == null || rasa_Response.contains("Cylinders") || rasa_Response.contains("cylinders")))
		{
			reply="Provide is your vehicle's approx value. (eg. Approx value is 35000)";
			sendMessage(from, reply);
		}else if((policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance") || policy.get(from).getOption().equalsIgnoreCase("Renew motor insurance")) && (policy.containsKey(from) && ((rasa_Response.equalsIgnoreCase("no") || policy.get(from).isPdf()) && !policy.get(from).isFlag())))
		{
			policy.get(from).setFlag(true);
			if(policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance")){
				reply ="Dear Customer, your vehicle details from your given document are as follow \\-"
						+"\nChassis Number: "+policy.get(from).getChassis_no()
						+"\nEngine Number: "+policy.get(from).getEngine_no()
						+"\nMake: "+policy.get(from).getMake()
						+"\nModel: "+policy.get(from).getModel()
						+"\nYear: "+policy.get(from).getYear()
						+"\nCylinders: "+policy.get(from).getCylinders()
						+"\nApprox. Vehicle Value: QAR "+policy.get(from).getVehicalValue()
						+"\nType \"y\" to confirm the details (or) Type \"n\" to modify the details";
			}else
			{
				reply ="Dear Customer, your vehicle details from your given document are as follow \\- "
						+"\nPolicy Number: "+policy.get(from).getPolicyQuoteNo()
						+"\nChassis Number: "+policy.get(from).getChassis_no()
						+"\nEngine Number: "+policy.get(from).getEngine_no()
						+"\nMake: "+policy.get(from).getMake()
						+"\nModel: "+policy.get(from).getModel()
						+"\nYear: "+policy.get(from).getYear()
						+"\nCylinders: "+policy.get(from).getCylinders()
						+"\nApprox. Vehicle Value: QAR "+policy.get(from).getVehicalValue()  
						+"\nType \"y\" to confirm the details (or) Type \"n\" to modify the details";
			}
			sendMessage(from, reply);
		}

		/*rasa_Response = rasa_Response.replace("and","").replace(",", "");
		String msg1[]=rasa_Response.split(" ");*/
		Random random=new Random();
		if((policy.containsKey(from) && rasa_Response.equalsIgnoreCase("yes")) || (policy.containsKey(from) && rasa_Response.contains("@") && rasa_Response.contains(".com")))// || (policy.containsKey(from) && policy.get(from).getContactNo() != null))
		{
			if(rasa_Response.equalsIgnoreCase("yes") && policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance") && policy.get(from).getContactNo() == null)
			{
				policy.get(from).setContactNo("");
				reply ="Please enter your contact number with country code.(+91 8899456251)";
				sendMessage(from, reply);

			}else if(!(rasa_Response.contains("@")) && (policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance")) && policy.get(from).getContactNo().contains("91") )
			{
				reply ="Please enter your Email Id.";
				sendMessage(from, reply);

			}else
			{
				policy.get(from).setPolicyQuoteNo(""+random.nextInt(100000000+846572));
				if(policy.get(from).getOption().equalsIgnoreCase("Buy new Motor Insurance"))
				{
					reply="Dear Custemer, Here is your Quotation with Reference No: "+policy.get(from).getPolicyQuoteNo()
							+"\n"
							+"\nComprehensive Insurance"
							+"\na.  Basic Plan QR 1,250 (Click here to view the Basic Plan details.)"
							+"\nb.  Premium Plan QR 1,500 (Click here to view the Premium Plan details.)"
							+"\nc.  Premium Plus Plan QR 1,800 (Click here to view the Premium Plus Plan details.)"
							+"\n  Third Party Insurance"
							+"\nd.  TP Basic QR 400 "
							+"\ne.  continue with default"                                                  
							+"\n   Please enter your selected plan number ";
				}else
				{
					reply="\nDear Custemer, Here is your Quotation with Reference No: "+policy.get(from).getPolicyQuoteNo()
							+"\n"
							+"\nComprehensive Insurance"
							+"\na.  Basic Plan: QR 1,250 (Click here to view the Basic Plan details.)"
							+"\nb.  Premium Plan: QR 1,500 (Click here to view the Premium Plan details.)"
							+"\nc.Premium Plus Plan: QR 1,800 (Click here to view the Premium Plus Plan details.)"
							+"\n    Third Party Insurance"
							+"\nd.  TP Basic: QR 400 "
							+"\ne.  continue with default"                                                  
							+"\n Please Select plan for renewal and enter your selected plan number";
				}
				sendMessage(from, reply);
			}
		}

		if(policy.containsKey(from) && (rasa_Response.equalsIgnoreCase("Basic Plan QR 1250") || rasa_Response.equalsIgnoreCase("Premium Plan QR 1500")  || rasa_Response.equalsIgnoreCase("Premium Plus QR 1800") || rasa_Response.equalsIgnoreCase("TP Basic QR 400") || rasa_Response.equalsIgnoreCase("continue with default")))
		{
			policy.get(from).setPlanName(rasa_Response);
			String[] p= rasa_Response.split(" ");
			policy.get(from).setPlanPrem(p[p.length-1]);
			reply="Would you like to add any of below optional covers?" 
					+"\n"
					+"\n  I. Off Road Cover: QR 150 (Covers during off roads like sand dunes etc.)"
					+"\n II. Geographical extension: QR 200"
					+"\nIII. Social Responsibility: QR 150";
			sendMessage(from, msg);
		}

		boolean flag =false;

		if(policy.containsKey(from) && rasa_Response.equalsIgnoreCase("no  thanks"))
		{
			policy.get(from).isCover =false;
			flag = true;
		}
		int prem = 0;
		try{

			prem = Integer.valueOf((String)policy.get(from).getPlanPrem());
			policy.get(from).setPlanPrem(""+prem);

		}catch(Exception e){}
		if((policy.containsKey(from) && ((rasa_Response.equalsIgnoreCase("i") || rasa_Response.equalsIgnoreCase("ii") || rasa_Response.equalsIgnoreCase("iii")))) || ((policy.containsKey(from) && (rasa_Response.equalsIgnoreCase("no  thanks")))))
		{
			if(flag == true)
			{
				reply="Ok fine...!";

				sendMessage(from, reply);
			}
			if(rasa_Response.equalsIgnoreCase("i")){
				prem = prem + 150;
			}else if(rasa_Response.equalsIgnoreCase("ii")){
				prem = prem + 200;
			}else if(rasa_Response.equalsIgnoreCase("iii")){
				prem = prem + 150;
			}
			policy.get(from).setPlanPrem(""+prem);
			reply="Your total Premium: QR "+prem
					+"\nPlease click on below link to pay the Premium using your Credit/Debit Cards"
					+"\nhttps://www.qic-insured.com/PayLink.do?RefNo=1701000023"
					+"\n"
					+"\nUpon confirmation of payment, documents will be emailed to kishor@kumar.com"
					+"\n"
					+"\nThank you for your valuable time ! Ping us again and we are at your service 24x7.";
			sendMessage(from, reply);

			reply ="Type 'EXIT' to terminate the conversation.";
			sendMessage(from, reply);
		}

		if(policy.containsKey(from) && rasa_Response.equalsIgnoreCase("Renew motor insurance"))

		{
			policy.get(from).setOption(rasa_Response);
			reply="Please enter policy Number or attach a Policy detail pdf.(Please upload image format)";

			sendMessage(from, msg);
		}

		if (policy.containsKey(from)&& policy.get(from).isStart() && msg.equalsIgnoreCase("exit"))
		{
			policy.get(from).setStart(false);
			policy.remove(from);
			reply="Good bye..! Have a nice day.";
			sendMessage(from, reply);
		}
		else if(reply.equalsIgnoreCase("Sorry! You have entered invalid input.") && !(from.equals("8698206727"))  || !(from.equals("8698206727"))){
			//reply="Invalid Option\n"+reply;
			policy.get(from).setStart(false);
			policy.remove(from);
		}else
		{
			sendMessage(from, reply);
		}
		/*
		if (policy.containsKey(from)&& policy.get(from).isStart() && msg.equalsIgnoreCase("exit"))
		{

			policy.get(from).setStart(false);

			policy.remove(from);

			reply="Hi,"+from+" Your session has ben teminated Please ping us again \n Thank You !! ";

			sendMessage(from, reply);
		}else
		{
			//reply="Invalid Option\n"+reply;

			sendMessage(from, reply);
		}*/
		return removeSpecialCharacter(reply);

	}

	private static boolean getMail(String email )
	{

		String[] str = email.split(" ");

		for(int i=0; i<str.length; i++){
			if(str[i].contains("@") || str[i].contains(".")){
				email = str[i];
			}
		}
		System.out.println("Emial Id="+email);
		boolean flag1 = true;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		boolean flag5 = false;
		boolean flag6 = false;
		int count = 0;

		int temp = email.length();
		if(email.contains("@")){
			flag2=true;
			int a=email.indexOf("@");
			for(int i=a;i<temp;i++){
				if(email.charAt(i)=='.'){
					flag3=true; 
					count=count+1;
				}
			}
			if(count<1||count>2){
				flag4=false;
			}
			else{
				flag4=true;
			}
		}
		else{
			flag2 =false;
			System.out.println("No @ symbol present");
			return false;
		}


		//Condition 3
		if(email.matches("[A-Z a-z 0-9_]+@.*")) //Unable to get the right RegEx here!
			flag5 = true;
		else
			flag5 = false;

		//Condition4
		if(!Character.isUpperCase(email.charAt(0))==true)
			flag6 = true;
		else
			flag6=false;

		if(flag1==true && flag2==true && flag3==true && flag4==true && flag5==true &&flag6==true){
			System.out.println("Email ID is valid");
			return true;
		}	    else{
			if(flag1==false){
				System.out.println("Inavlid length of Email ID");
				return false;
			}
			if(flag2==false||flag3==false||flag4==false){
				System.out.println("Invalid Position of Special Characters");
				return false;
			}
			if(flag5==false){
				System.out.println("Invalid combination for username");
				return false;
			}
			if(flag6==false){
				System.out.println("Invalid case of first letter");
				return false;
			}
		}
		return true;
	}

	public boolean processMessage(String key)
	{

		Date d1 = lastReply.get(key);

		if (d1 == null)

			return true;

		Date d2 = new Date();

		long seconds = (d2.getTime() - d1.getTime()) / 1000;

		if (seconds > 1) {

			return true;

		}

		return false;

	}



	public String readLastMessage() throws InterruptedException {

		Thread.sleep(2000);

		driver.switchTo().defaultContent();

		WebElement main = driver.findElement(By.id("main"));

		String result = null;

		List<WebElement> msgs = main.findElements(By.className("message-list"));

		WebElement msg = msgs.get(msgs.size() - 1);

		try {

			result = msg.findElement(By.className("message-text")).findElements(By.tagName("span")).get(1).getText();

		} catch (Exception e) {

			result = msg.findElement(By.className("message-system-e2e")).findElement(By.className("emojitext"))

					.getText();

			if (result.equals("Messages you text to this chat and calls are secured with end-to-end encryption.")) {

				msg = msgs.get(msgs.size() - 2);

				result = msg.findElement(By.className("message-text")).findElements(By.tagName("span")).get(1)

						.getText();

			}

		}

		System.out.println("Message=" + result);

		return result;

	}



	public String removeSpecialCharacter(String temp) {

		try {

			return temp.replaceAll("\\P{Print}", "").trim();

		} catch (Exception e) {

			return temp;

		}

	}

	public void sendDocument(String title, String docPath) throws InterruptedException {

		driver.switchTo().defaultContent();

		WebDriverWait wait = new WebDriverWait(driver, 30);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main")));

		WebElement main = driver.findElement(By.id("main"));

		WebElement chatControl = main.findElement(By.className("pane-chat-controls"));

		List<WebElement> items = chatControl.findElements(By.className("menu-item"));

		WebElement attachment = items.get(1).findElement(By.tagName("button"));

		attachment.click();



		WebElement docAttachment = items.get(1).findElements(By.tagName("input")).get(1);

		docAttachment.sendKeys(docPath);



		wait = new WebDriverWait(driver, 30);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("drawer-body")));

		WebElement drawerBody = driver.findElement(By.className("drawer-body"));



		wait = new WebDriverWait(driver, 30);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("drawer-controls")));

		WebElement textButton = drawerBody.findElement(By.className("drawer-controls"))

				.findElement(By.tagName("button"));

		textButton.click();

	}

	public void sendImage(String title, String imgPath, String msg) throws InterruptedException {

		driver.switchTo().defaultContent();

		WebDriverWait wait = new WebDriverWait(driver, 15);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main")));

		WebElement main = driver.findElement(By.id("main"));

		WebElement chatControl = main.findElement(By.className("pane-chat-controls"));

		List<WebElement> items = chatControl.findElements(By.className("menu-item"));

		WebElement attachment = items.get(1).findElement(By.tagName("button"));

		attachment.click();



		WebElement imageAttachment = items.get(1).findElements(By.tagName("input")).get(0);

		imageAttachment.sendKeys(imgPath);



		wait = new WebDriverWait(driver, 15);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("drawer-body")));

		WebElement drawerBody = driver.findElement(By.className("drawer-body"));



		wait = new WebDriverWait(driver, 15);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("input-wrapper")));



		WebElement inputWrapper = drawerBody.findElement(By.className("input-wrapper"));

		WebElement inputEmoji = drawerBody.findElement(By.className("input-emoji"));

		WebElement inputText = inputEmoji.findElement(By.tagName("div"));

		inputWrapper.click();

		inputText.sendKeys(msg);



		WebElement textButton = drawerBody.findElement(By.className("drawer-controls"))

				.findElement(By.tagName("button"));

		textButton.click();

	}

	public void sendMessage(String title, String msg) throws InterruptedException {

		Thread.sleep(1000);

		driver.switchTo().defaultContent();

		WebDriverWait wait = new WebDriverWait(driver, 15);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main")));

		WebElement main = driver.findElement(By.id("main"));

		WebElement messageList = main.findElement(By.className("message-list"));

		List<WebElement> messages = messageList.findElements(By.className("msg"));

		String lastMessage = null;

		if (messages.size() >= 1) {

			WebElement message = messages.get(messages.size() - 1);

			WebElement msgLast = null;

			try {

				msgLast = message.findElement(By.className("message-text"));

			} catch (ElementNotFoundException e) {

				// TODO: handle exception

			} catch (NoSuchElementException e) {

			}

			try {

				lastMessage = msgLast.findElements(By.tagName("span")).get(1).getText();

			} catch (NullPointerException e) {

				// TODO: handle exception
			}

		}

		boolean alreadySent = false;

		if (lastMessage != null && removeSpecialCharacter(lastMessage).equals(removeSpecialCharacter(msg))) {

			alreadySent = true;

		}

		if (!alreadySent) {

			WebElement blockCompose = main.findElement(By.className("block-compose"));

			WebElement inputTextDiv = blockCompose.findElement(By.className("input-container"));

			wait = new WebDriverWait(driver, 15);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("input")));

			WebElement inputText = inputTextDiv.findElement(By.className("input"));

			inputText.click();

			inputText.clear();

			inputText.sendKeys(msg.replaceAll("\n", Keys.chord(Keys.SHIFT, Keys.ENTER)));

			// inputText.sendKeys(msg);please wait while we are genrating your

			// quote \n(you shall receive an email for the same )

			WebElement button = blockCompose.findElements(By.tagName("button")).get(1);

			button.click();

			System.out.println("Sent Message=" + msg);

			msgs.put(title, removeSpecialCharacter(msg));

		}

	}

	/* Download PDF */

	public boolean downloadDoc() throws InterruptedException {

		Thread.sleep(2000);

		driver.switchTo().defaultContent();

		WebDriverWait wait = new WebDriverWait(driver, 15);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("main")));



		WebElement main = driver.findElement(By.id("main"));

		boolean result = false;

		List<WebElement> msgs = main.findElements(By.className("document-body"));

		WebElement msg = msgs.get(msgs.size() - 1);

		try {

			wait = new WebDriverWait(driver, 15);

			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("spinner-container")));

			WebElement resultDoc = msg.findElement(By.className("doc-state"));

			resultDoc.click();

			result =true;
			//result = msg.findElement(By.className("message-system-e2e")).findElement(By.className("emojitext")).getText();

		} catch (NoSuchElementException e) {


		}

		//System.out.println("Message=" + result);

		Thread.sleep(3000);

		return result;

	}


	@Override

	public void userList() throws Exception {

		if (driver == null) {

			WhatsApp.driver = DriverUtility.getDriver(browserType, downloadFilepath);

			init = false;

		}

		if (init == false) {

			driver.get("https://web.whatsapp.com/");

			WebDriverWait wait = new WebDriverWait(driver, 60);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("side")));

			init = true;

		}

		inLoop = false;

		if (inLoop == false) {

			inLoop = true;

			driver.switchTo().defaultContent();

			WebElement side = driver.findElement(By.id("side"));

			WebElement paneSide = side.findElement(By.id("pane-side"));

			List<WebElement> chats = paneSide.findElements(By.className("chat-body"));// ("div")).findElement(By.tagName("div")).findElement(By.tagName("div")).findElements(By.tagName("div"));

			for (WebElement chat : chats) {

				// System.out.println(chats.size());

				// chats = paneSide.findElements(By.className("chat-body"));

				try {

					try {

						actions.moveToElement(chat).click().perform();

						// chat.click();

					} catch (Exception e1) {

						// TODO Auto-generated catch block

						chat.click();

						actions.moveToElement(chat).click().perform();

					}

					WebElement chatTitle = chat.findElement(By.className("chat-main"))

							.findElement(By.className("chat-title")).findElement(By.tagName("span"));

					WebElement chatSecondary = chat.findElement(By.className("chat-secondary"))

							.findElement(By.className("chat-status"));// last-msg

					WebElement lastMsg = chatSecondary.findElement(By.className("last-msg"));

					String chatUnreadCount = chat.findElement(By.className("chat-secondary"))

							.findElement(By.className("chat-meta")).findElement(By.tagName("span")).getText();

					if (!chatUnreadCount.equals("") && !msgs.containsKey(chatTitle.getAttribute("title"))) {

						msgs.put(chatTitle.getAttribute("title"),

								removeSpecialCharacter("Old" + lastMsg.getAttribute("title")));

					}

					actions.moveToElement(chat).click().perform();



					// System.out.println(chatTitle.getAttribute("title") + "="

					// + lastMsg.getAttribute("title"));

					WebDriverWait wait = new WebDriverWait(driver, 15);

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("chat-secondary")));

					if (!chatTitle.getAttribute("title").contains(",")) {

						if (processMessage(chatTitle.getAttribute("title"))

								&& msgs.containsKey(chatTitle.getAttribute("title"))) {

							if ((!msgs.get(chatTitle.getAttribute("title"))

									.equals(removeSpecialCharacter(lastMsg.getAttribute("title")))

									&& removeSpecialCharacter(lastMsg.getAttribute("title")).length() != 0)

									|| msgs.get(chatTitle.getAttribute("title")).equals(removeSpecialCharacter("Photo"))

									|| msgs.get(chatTitle.getAttribute("title")).equals(removeSpecialCharacter("GIF"))

									|| (msgs.get(chatTitle.getAttribute("title")).contains(removeSpecialCharacter("/"))

											&& removeSpecialCharacter(lastMsg.getAttribute("title")).length() == 21)) {

								try {



									System.out.println("Old Message="
											+ msgs.get(chatTitle.getAttribute("title")).length() + " New Message= "
											+ removeSpecialCharacter(lastMsg.getAttribute("title")).length());

									actions.moveToElement(chat).click().perform();

									if (removeSpecialCharacter(lastMsg.getAttribute("title")).equals(

											"?Messages you text to this chat and calls are secured with end-to-end encryption.")) {

										chat.click();

										// Thread.sleep(2000);

									}

									msgs.put(chatTitle.getAttribute("title"),

											removeSpecialCharacter(lastMsg.getAttribute("title")));

									// if

									// (chatTitle.getAttribute("title").contains("pritish"))

									// {

									String a = chatTitle.getAttribute("title");

									try {

										chat.click();

										System.out.println(a);

									} catch (Exception e) {

										// TODO Auto-generated catch block

										actions.moveToElement(chat).click().perform();

										chat.click();

										System.out.println(chatTitle.getAttribute("title"));

										e.printStackTrace();

									}

									String message = messageReply(chatTitle.getAttribute("title"),

											removeSpecialCharacter(lastMsg.getAttribute("title")));



									lastReply.put(chatTitle.getAttribute("title"), new Date());

									Thread.sleep(1000);

									if (!removeSpecialCharacter(lastMsg.getAttribute("title")).equals("Photo")

											&& !removeSpecialCharacter(lastMsg.getAttribute("title")).equals("GIF")

											&& !(removeSpecialCharacter(lastMsg.getAttribute("title")).contains("/")

													&& removeSpecialCharacter(lastMsg.getAttribute("title"))

													.length() == 21)) {

										WebElement archi = driver.findElement(

												By.xpath("//*[@id='pane-side']/div/div/div/div/div/div/div[2]"));

										WebElement archTitle = archi.findElement(By.xpath(

												"//*[@id='pane-side']/div/div/div/div[1]/div/div/div[2]/div[1]/div[1]/span"));

										System.out.println(a.equals(archTitle.getText().toString().trim()));



										if (a.equals(archTitle.getText().toString().trim())) {

											actions.moveToElement(archi).click().perform();

											archi.click();

											actions.contextClick(archi).build().perform();

											wait.until(ExpectedConditions

													.visibilityOfElementLocated((By.className("dropdown"))));

											WebElement arch = driver.findElement(By.className("dropdown"));

											List<WebElement> archive = arch.findElements(By.tagName("li"));

											archive.get(0).click();

										} else {

											actions.moveToElement(chat).click().perform();

											chat.click();

											actions.contextClick(chat).build().perform();

											wait.until(ExpectedConditions

													.visibilityOfElementLocated((By.className("dropdown"))));

											WebElement arch = driver.findElement(By.className("dropdown"));

											List<WebElement> archive = arch.findElements(By.tagName("li"));

											archive.get(0).click();

										}
									}

								} catch (Exception e) {

									// TODO Auto-generated catch block

									e.printStackTrace();

								}

								// }



							} else {

								if (!removeSpecialCharacter(lastMsg.getAttribute("title")).equals("Photo")

										&& !removeSpecialCharacter(lastMsg.getAttribute("title")).equals("GIF")

										&& !(removeSpecialCharacter(lastMsg.getAttribute("title")).contains("/")

												&& removeSpecialCharacter(lastMsg.getAttribute("title"))

												.length() == 21)) {



								}

							}

						} else {

							actions.moveToElement(chat).click().perform();

							if (removeSpecialCharacter(lastMsg.getAttribute("title")).equals(

									"?Messages you text to this chat and calls are secured with end-to-end encryption.")) {

								chat.click();

								// Thread.sleep(2000);

							}

							System.out.println(

									"Length=" + removeSpecialCharacter(lastMsg.getAttribute("title")).length());

							if (removeSpecialCharacter(lastMsg.getAttribute("title")).length() > 0) {

								msgs.put(chatTitle.getAttribute("title"),

										removeSpecialCharacter(lastMsg.getAttribute("title")));
								lastReply.put(chatTitle.getAttribute("title"), new Date());

								System.out.println("Starting Message=" + chatTitle.getAttribute("title") + " "+ removeSpecialCharacter(lastMsg.getAttribute("title")));

							}

							String message = messageReply(chatTitle.getAttribute("title"),

									removeSpecialCharacter(lastMsg.getAttribute("title")));

							WebElement archi = driver.findElement(By.xpath("//*[@id='pane-side']/div/div/div/div[1]/div/div/div[2]"));

							actions.moveToElement(archi).click().perform();

							chat.click();

							actions.contextClick(archi).build().perform();

							wait.until(ExpectedConditions.visibilityOfElementLocated((By.className("dropdown"))));

							WebElement arch = driver.findElement(By.className("dropdown"));

							List<WebElement> drop = arch.findElements(By.tagName("li"));

							/* WebElement archive; */

							try {

								drop.get(0).click();

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				} catch (Exception e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}
			}

			inLoop = false;
		}

		// System.out.println("HDFC LOGGED IN");

		Thread.sleep(1500);

		// userList();
	}

}
