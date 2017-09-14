package com.vyoms.whatsapp.type;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.vyoms.whatsapp.implementation.WhatsAppImplementation;
import com.vyoms.whatsapp.model.AgentMaster;
import com.vyoms.whatsapp.model.EmpMaster;
import com.vyoms.whatsapp.model.PinMaster;
import com.vyoms.whatsapp.model.Policy;
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
		String reply="No valid input";
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
			rasa_Response=(new validate_Response()).check_response(msg);
		}catch(ConnectException e){
			reply="Hi,"+from+" Unnable to connect with RASA.";
			sendMessage(from, reply);
			policy.get(from).setStart(false);
			policy.remove(from);
			reply="Hi,"+from+" Your session has ben teminated Please ping us again \n Thank You !! ";
			sendMessage(from, reply);
		}
		
		if (!policy.containsKey(from) && rasa_Response.equalsIgnoreCase("great"))
		{
			final Policy saveImageForAgent = new Policy();
			saveImageForAgent.setStart(true);
			policy.put(from, saveImageForAgent);
			//reply="Hi,"+from+" Please Enter the customer name !!";
			reply="Hi, "+from+"Hi Varun, how can I help you today?"
					+ "\nPlease select from following options."
					+ "\n1. Buy new Motor Insurance"
					+ "\n2. Renew motor insurance"
					+ "\n3. Exit";
			sendMessage(from, reply);
			msg="";
		}
		System.out.println("Rasa Responce="+rasa_Response);
		policy.get(from).setOption(rasa_Response);
		if(policy.containsKey(from) && policy.get(from).isStart() && policy.get(from).getOption().equalsIgnoreCase("Buy Motor Insurance")){
			reply="Please enter Chassis Number or attach a vehicle registration card (Please upload image format)";
			sendMessage(from, msg);
		}
		if (policy.containsKey(from) && policy.get(from).isStart() && policy.get(from).getImageOption()==null && policy.get(from).getOption().equalsIgnoreCase("photo"))
		{
			policy.get(from).setImageOption(rasa_Response);
			reply="Currently, I am in learning phase, so you will get a slight delay in response, kindly cooperate with me.";
			sendMessage(from, reply);
		}
		if(policy.containsKey(from) && !policy.get(from).isChequeImage() && policy.get(from).isStart() && rasa_Response.equalsIgnoreCase("photo"))
		{
			String destination="C:\\Users\\HP LAPTOP\\Desktop\\AutoomationEdgeData\\QatarData\\ImagePath\\Imeges";
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
			downloadImage();
			System.out.println(downloadFilePath.getTheNewestFile(downloadFilepath, "jpeg"));
			//System.out.println(downloadFilePath.getTheNewestFile(downloadFilepath, "jpeg").toString());
			
			//downloadFilePath.copyFile_Directory(downloadFilePath.getTheNewestFile(downloadFilepath, "jpeg").toString(), from, destination);
			FileUtils.copyFileToDirectory(new File(downloadFilePath.getTheNewestFile(downloadFilepath, "jpeg").toString()), new File(destination));
			policy.get(from).setChequeImage(true);
			
			reply="Hi,"+from+" Image is saved into "+destination+"\n Do You want to upload another image (Y/N)";
			sendMessage(from, reply);
			
			
		}
		
		if(policy.containsKey(from) && policy.get(from).getOption().equalsIgnoreCase("Renew motor insurance")){
			reply="Please enter Chassis Number or attach a vehicle registration card (Please upload image format)";
			sendMessage(from, msg);
		}
		if(policy.containsKey(from) && policy.get(from).getOption().equalsIgnoreCase("Exit")){
			policy.get(from).setStart(false);
			policy.remove(from);
			reply="Hi,"+from+" Your session has ben teminated Please ping us again \n Thank You !! ";
			sendMessage(from, reply);
		}
		
		
		
		
		
		
		
		if (policy.containsKey(from)&& policy.get(from).isStart() && msg.equalsIgnoreCase("exit"))
		{
			policy.get(from).setStart(false);
			policy.remove(from);
			reply="Hi,"+from+" Your session has ben teminated Please ping us again \n Thank You !! ";
			sendMessage(from, reply);
		}
		else {
			//reply="Invalid Option\n"+reply;
			sendMessage(from, reply);
			
		}

		return removeSpecialCharacter(reply);
	}

	public boolean processMessage(String key) {
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
								System.out.println("Starting Message=" + chatTitle.getAttribute("title") + " "
										+ removeSpecialCharacter(lastMsg.getAttribute("title")));
							}
							String message = messageReply(chatTitle.getAttribute("title"),
									removeSpecialCharacter(lastMsg.getAttribute("title")));
							WebElement archi = driver
									.findElement(By.xpath("//*[@id='pane-side']/div/div/div/div[1]/div/div/div[2]"));
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
