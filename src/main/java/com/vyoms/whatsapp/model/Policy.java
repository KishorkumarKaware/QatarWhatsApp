package com.vyoms.whatsapp.model;

public class Policy {

	//public VehicalDetails vehicalDetail = null;

	private String chassis_no = null;

	private String model = null;

	private String make = null;

	private String engine_no  = null;

	private String year = null; 

	private String cylinders = null;

	private String vehicalValue = null;

	private String email = null;

	private String contactNo = null;

	public boolean agent = false;

	public String PDFOption = null;

	private String agent_Im_Id;

	public boolean attachment=false;

	public boolean cheque = false;

	public boolean chequePDF = false;

	public boolean convert = false;

	public boolean isCover = false;

	public String cover;

	private String defect_description;

	public boolean intermediate = false;

	private String issue;

	private String message=null;

	public String name = null;

	public boolean offline = false;

	public boolean pdf = false;

	public String pin = null;

	public boolean pincode = false;

	public boolean plan = false;

	public String planName = null;

	public String  planPrem = null;

	private String policyQuoteNo;

	public String prem = null;

	private String product;

	private String remark;

	private String result=null;

	private String slipNo;

	public boolean start = false;

	public String stateCity = null;

	public String sumInsured = null;

	private String trans;

	public String emailId = null;

	public String UCVId = null;

	private String samaccount;

	private String aeReqNumber;

	private String aeRequestStatus;

	private String customerName=null;

	private String option ="";

	private boolean flag = false;
	
	private boolean manual = false;
	
	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getChassis_no() {
		return chassis_no;
	}

	public void setChassis_no(String chassis_no) {
		this.chassis_no = chassis_no;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getEngine_no() {
		return engine_no;
	}

	public void setEngine_no(String engine_no) {
		this.engine_no = engine_no;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCylinders() {
		return cylinders;
	}

	public void setCylinders(String cylinders) {
		this.cylinders = cylinders;
	}

	public String getVehicalValue() {
		return vehicalValue;
	}

	public void setVehicalValue(String vehicalValue) {
		this.vehicalValue = vehicalValue;
	}

	/*public VehicalDetails getVehicalDetail() {
		return vehicalDetail;
	}

	public void setVehicalDetail(VehicalDetails vehicalDetail) {
		this.vehicalDetail = vehicalDetail;
	}*/

	public String getDefect_description() {
		return defect_description;
	}

	public void setDefect_description(String defect_description) {
		this.defect_description = defect_description;
	}


	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}

	public String getSamaccount() {
		return samaccount;
	}

	public void setSamaccount(String samaccount) {
		this.samaccount = samaccount;
	}

	public String getAeReqNumber() {
		return aeReqNumber;
	}

	public void setAeReqNumber(String aeReqNumber) {
		this.aeReqNumber = aeReqNumber;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Policy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPlanPrem() {
		return planPrem;
	}

	public void setPlanPrem(String planPrem) {
		this.planPrem = planPrem;
	}

	public String get_defect_description() {
		// TODO Auto-generated method stub
		return defect_description;
	}

	public String getAeRequestNumber() {
		// TODO Auto-generated method stub
		return aeReqNumber;
	}

	public String getAeRequestStatus() {
		// TODO Auto-generated method stub
		return aeRequestStatus;
	}

	public  String getAgent_Im_Id() {

		return agent_Im_Id;
	}

	public boolean getAttachment() {
		// TODO Auto-generated method stub
		return attachment;
	}

	public String getCustomerName() {
		// TODO Auto-generated method stub
		return customerName;
	}

	public String getEmailId() {
		// TODO Auto-generated method stub
		return emailId;
	}

	public String getPDFOption() {
		// TODO Auto-generated method stub
		return this.PDFOption;
	}

	public String getIssueRelated()
	{
		return issue;
	}

	public String getMessage() {
		return message;
	}
	public String getPin() {
		return pin;
	}

	public String getPlanName() {
		return planName;
	}

	public String getPolicyQuoteNo() {
		// TODO Auto-generated method stub
		return policyQuoteNo;
	}

	public String getPrem() {
		return prem;
	}

	public String getProduct()
	{
		return product;
	}

	public String getRemark() {
		// TODO Auto-generated method stub
		return remark;
	}

	public String getResult() {
		return result;
	}

	public String getSamAccountName() {
		// TODO Auto-generated method stub
		return samaccount;
	}

	public String getSlipNo() {
		// TODO Auto-generated method stub
		return slipNo;
	}

	public String getStateCity() {
		return stateCity;
	}

	public String getSumInsured() {
		return sumInsured;
	}

	public String getTransaction() {
		// TODO Auto-generated method stub
		return trans;

	}

	public String getUCVId() {
		// TODO Auto-generated method stub
		return UCVId;
	}

	public boolean isAgent() {
		return agent;
	}

	public boolean isCheque() {
		return cheque;
	}

	public boolean isChequePDF() {
		return chequePDF;
	}

	public boolean isConvert() {
		return convert;
	}

	public boolean isIntermediate() {
		return intermediate;
	}

	public boolean isOffline() {
		return offline;
	}

	public boolean isPdf() {
		return pdf;
	}

	public boolean isPincode() {
		return pincode;
	}

	public boolean isPlan() {
		return plan;
	}

	public boolean isStart() {
		return start;
	}

	public void set_defect_description(String defect_description) {
		// TODO Auto-generated method stub
		this.defect_description=defect_description;
	}
	public void setAeRequestNumber(String aeReqNumber) {
		// TODO Auto-generated method stub
		this.aeReqNumber=aeReqNumber;
	}
	public String setAeRequestStatus(String aeRequestStatus ) {
		return this.aeRequestStatus=aeRequestStatus;
	}
	public void setAgent(boolean agent) {
		this.agent = agent;
	}
	public  void setAgent_Im_Id(String agent_Im_Id) {

		this.agent_Im_Id = agent_Im_Id;
	}
	public void setAttachment(boolean attachment) {
		// TODO Auto-generated method stub
		this.attachment=attachment;
	}
	public void setCheque(boolean cheque) {
		this.cheque = cheque;
	}
	public void setChequePDF(boolean chequePDF) {
		this.chequePDF = chequePDF;
	}
	public void setConvert(boolean convert) {
		this.convert = convert;
	}
	public String setCustomerName(String customerName ) {
		return this.customerName=customerName;
	}

	public void setEmailId(String emailId) {
		// TODO Auto-generated method stub
		this.emailId = emailId;
	}

	public String setPDFOption(String b) {
		// TODO Auto-generated method stub
		return this.PDFOption=b;

	}

	public void setIntermediate(boolean intermediate) {
		this.intermediate = intermediate;
	}

	public void setIssueRelated(String issue)
	{
		this.issue=issue;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}
	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public void setPincode(boolean pincode) {
		this.pincode = pincode;
	}

	public void setPlan(boolean plan) {
		this.plan = plan;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public void setPolicyQuoteNo(String policyQuoteNo) {
		// TODO Auto-generated method stub
		this.policyQuoteNo=policyQuoteNo;
	}

	public void setPrem(String prem) {
		this.prem = prem;
	}

	public void setProduct(String product)
	{
		this.product=product;
	}

	public void setRemark(String remark) {
		this.remark=remark;
		// TODO Auto-generated method stub

	}

	public void setResult(String result) {
		this.result = result;
	}

	public String setSamAccount(String samaccount) {
		return this.samaccount=samaccount;
	}

	public boolean isCover() {
		return isCover;
	}

	public void setCover(boolean isCover) {
		this.isCover = isCover;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public void setSlipNo(String slipNo) {
		// TODO Auto-generated method stub
		this.slipNo=slipNo;
	}
	public void setStart(boolean start) {
		this.start = start;
	}

	public void setStateCity(String stateCity) {
		this.stateCity = stateCity;
	}
	public void setSumInsured(String sumInsured) {
		this.sumInsured = sumInsured;
	}

	public void setTransaction(String trans) {
		// TODO Auto-generated method stub
		this.trans=trans;

	}
	public void setUCVId(String UCVId) {
		// TODO Auto-generated method stub
		this.UCVId = UCVId;
	}




}
