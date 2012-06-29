package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.SequencedHashMap;

import kacang.Application;
import kacang.services.storage.StorageFile;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.CheckBox;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorIsNumeric;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.messaging.model.MessagingModule;
import com.tms.hr.recruit.model.ApplicantObj;

public class JobAppAdditional extends Form{
	public static final String FORWARD_SAVED = "additionalSave";
	public static final String FORWARD_ERROR = "error";
	
	private String removeFile; 
	private boolean removeFileClose;
	private FileUpload resumePath;
	private Label lblRecFileUpload;
	private SelectBox sbCurrency;
	private TextField txtSalary;
	private Radio radioTravelNo;
	private Radio radioTravelYes;
	private Radio radioTravelModerate;
	private ButtonGroup radioGroupTravel;
	
	private Radio radioRelocateWill;
	private Radio radioRelocatelNo;
	private ButtonGroup radioGroupRelocate;
	
	private Radio radioTransportNo;
	private Radio radioTransportYes;
	private ButtonGroup radioGroupTransport;
	private CheckBox cbNego;
	
	private ApplicantObj applicantObj;
	private Collection additionalCol;
	private Map mapCurrency = new LinkedHashMap();
	
	private Panel btnPanel;
	private Button btnSave;
	private Button btnReset;
	
	//validator
	private ValidatorIsNumeric expSalaryVIN = new ValidatorIsNumeric("expSalaryVIN","");		
	private boolean boolsbCurrency;// checking if the selectbox currency is selected or not
	
	public void init(){
		initForm();
	}
	
	public void initForm(){
		setMethod("POST");
		setColumns(2);
		
    	Application app = Application.getInstance();
    	Label lblUploadResume= new Label("lblUploadResume", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.uploadResume") + "</span>");
    	lblUploadResume.setAlign("right");
    	resumePath = new FileUpload("resumePath");
    	lblRecFileUpload = new Label("lblRecFileUpload", "");
    	addChild(lblUploadResume);
    	addChild(resumePath);
    	addChild(lblRecFileUpload);
    	
    	Label lblExpectedSalary= new Label("lblExpectedSalary", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.expectedSalary") + "</span>");
    	lblExpectedSalary.setAlign("right");
    	sbCurrency = new SelectBox("sbCurrency", mapCurrency, null, false, 1);    	
    	getCurrencyMapping(sbCurrency);
    	
    	addChild(lblExpectedSalary);
    	addChild(sbCurrency);
    	
    	txtSalary = new TextField("txtSalary");
    	txtSalary.setSize("10");
    	txtSalary.setMaxlength("10");
    	addChild(txtSalary);
    	
    	cbNego = new CheckBox("cbNego",  app.getMessage("recruit.general.label.negotiable"));
    	addChild(cbNego);
    	
    	Label lblTravel= new Label("lblTravel", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.travel") + "</span>");
    	lblTravel.setAlign("right");
    	radioTravelNo= new Radio("No", app.getMessage("recruit.general.label.no"));
    	radioTravelYes = new Radio("Light", app.getMessage("recruit.general.label.light"));
    	radioTravelModerate = new Radio("Moderate", app.getMessage("recruit.general.label.moderate"));
    	
    	radioGroupTravel = new ButtonGroup("radioGroupTravel");
    	radioGroupTravel.setType(ButtonGroup.RADIO_TYPE);
    	radioGroupTravel.setColumns(3);
    	radioGroupTravel.addButton(radioTravelNo);
    	radioGroupTravel.addButton(radioTravelYes);
    	radioGroupTravel.addButton(radioTravelModerate);
    	addChild(lblTravel);
    	addChild(radioGroupTravel);
    	
    	Label lblRelocate= new Label("lblRelocate", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.relocate") + "</span>");
    	lblRelocate.setAlign("right");
    	radioRelocatelNo = new Radio("No", app.getMessage("recruit.general.label.no"));
    	radioRelocateWill= new Radio("Will Consider", app.getMessage("recruit.general.label.radioRelocateWill"));
    
    	radioGroupRelocate = new ButtonGroup("radioGroupRelocate");
    	radioGroupRelocate.setType(ButtonGroup.RADIO_TYPE);
    	radioGroupRelocate.setColumns(2);
    	radioGroupRelocate.addButton(radioRelocateWill);
    	radioGroupRelocate.addButton(radioRelocatelNo);
    	addChild(lblRelocate);
    	addChild(radioGroupRelocate);
    	
    	Label lblTransport= new Label("lblTransport", "<span class=\"fieldTitle\">" + app.getMessage("recruit.general.label.transport") + "</span>");
    	lblTransport.setAlign("right");
    	radioTransportNo = new Radio("Yes", app.getMessage("recruit.general.label.no"));
    	radioTransportYes= new Radio("No", app.getMessage("recruit.general.label.yes"));
    
    	radioGroupTransport = new ButtonGroup("radioGroupTransport");
    	radioGroupTransport.setType(ButtonGroup.RADIO_TYPE);
    	radioGroupTransport.setColumns(2);
    	radioGroupTransport.addButton(radioTransportNo);
    	radioGroupTransport.addButton(radioTransportYes);
    	addChild(lblTransport);
    	addChild(radioGroupTransport);
    
    	btnSave = new Button("btnSave", app.getMessage("recruit.general.label.save","Save"));
    	btnReset = new Button("btnReset", app.getMessage("recruit.general.label.reset","Reset"));
    	
    	btnPanel = new Panel("btnPanel");
    	btnPanel.setColspan(2);
    	btnPanel.addChild(btnSave);
    	btnPanel.addChild(btnReset);
    	
    	Label lblspace = new Label("lblspace","");
    	addChild(lblspace);
    	addChild(btnPanel);
	}
	
	public void getCurrencyMapping(SelectBox ff){
		mapCurrency = getCurrencyMap();
		ff.setOptionMap(mapCurrency);
	}
	
	public Map getCurrencyMap(){
		Application app = Application.getInstance();
		mapCurrency.put("---", app.getMessage("recruit.general.hierachy.selectCurrency") );
    	mapCurrency.put("sa1", app.getMessage("recruit.general.label.sa1") );
    	mapCurrency.put("sa2", app.getMessage("recruit.general.label.sa2") );
    	mapCurrency.put("sa3", app.getMessage("recruit.general.label.sa3") );
    	mapCurrency.put("sa4", app.getMessage("recruit.general.label.sa4") );
    	mapCurrency.put("sa5", app.getMessage("recruit.general.label.sa5") );
    	mapCurrency.put("sa6", app.getMessage("recruit.general.label.sa6") );
    	mapCurrency.put("sa7", app.getMessage("recruit.general.label.sa7") );
    	mapCurrency.put("sa8", app.getMessage("recruit.general.label.sa8") );
    	mapCurrency.put("sa9", app.getMessage("recruit.general.label.sa9") );
    	mapCurrency.put("sa10", app.getMessage("recruit.general.label.sa10") );
    	mapCurrency.put("sa11", app.getMessage("recruit.general.label.sa11") );
    	mapCurrency.put("sa12", app.getMessage("recruit.general.label.sa12") );
    	mapCurrency.put("sa13", app.getMessage("recruit.general.label.sa13") );
    	mapCurrency.put("sa14", app.getMessage("recruit.general.label.sa14") );
    	mapCurrency.put("sa15", app.getMessage("recruit.general.label.sa15") );
    	
		return mapCurrency;
	}
	
	public Forward onSubmit(Event evt) {
		Forward forward = super.onSubmit(evt); 
		String action = findButtonClicked(evt);
		boolean flag = false;
		applicantObj = new ApplicantObj();
		 if(action.equals(btnReset.getAbsoluteName())){
			 if(additionalCol!=null && additionalCol.size() > 0){
				 additionalCol=null;
			 }
			 setFieldReset();
			 //set obj flag status
			 applicantObj.setByPassValidation("yes");
		 }else{
			 JobAppPersonal jap = new JobAppPersonal();
			 boolsbCurrency=jap.validateSelectBox(sbCurrency, "recruit.general.hierachy.selectCurrency", "---");
			 if(!boolsbCurrency){
				 if(!expSalaryVIN.validate(txtSalary)){
					 txtSalary.setInvalid(true);
					 flag=true;
				 }
			 }
			 
			 if(resumePath.getValue()!=null && !resumePath.getValue().equals("")){
				 applicantObj.setResumePath((String)resumePath.getValue());
				 //Map attachmentMap = getAttachmentMapFromSession(evt);
				 try{
					 StorageFile sf = resumePath.getStorageFile(evt.getRequest());
					 if(sf!=null){	
						if(sf.getName().toLowerCase().endsWith("doc") || sf.getName().toLowerCase().endsWith("pdf")) {
				             applicantObj.setStorageFile(sf);
				             lblRecFileUpload.setText("<li>"+resumePath.getValue() + 
				            		 " [<a href='?remove=yes'>Remove</a>]");
				              
				             setRemoveFileClose(true);
				             
				             //add sf to session
				             evt.getRequest().getSession().setAttribute("ssf", sf);
				             
				             /*attachmentMap.put(sf.getName(), "-data in storage service-");
				             setAttachmentMapToSession(evt, attachmentMap);*/
						}else{
							resumePath.setInvalid(true);
							flag=true;
						}
					}
				}catch(Exception error){
					System.out.println("file may corrupted "+ error);	
				}
			 }
			 
			 if(flag || this.isInvalid()){
				 setInvalid(true);
				 applicantObj.setByPassValidation("no"); //set obj status
			 }
		 }
		
		return forward;
	}
	
	public Forward onValidate(Event evt) {
		String action = findButtonClicked(evt);
		
		 if(action.equals(btnSave.getAbsoluteName())){
			 String selectedCurrency= (String) sbCurrency.getSelectedOptions().keySet().iterator().next(); //set key
			 String CurrencyDesc = mapCurrency.get(selectedCurrency).toString(); //set value
			 
			 //applicantObj = new ApplicantObj();
			 additionalCol = new ArrayList();
			 
			 if(evt.getRequest().getSession().getAttribute("ssf")!=null && !evt.getRequest().getSession().getAttribute("ssf").equals("")){
				 applicantObj.setStorageFile((StorageFile)evt.getRequest().getSession().getAttribute("ssf"));
			 }
			 
			 applicantObj.setExpectedTypeSalary(selectedCurrency);
			 applicantObj.setExpectedTypeSalaryDesc(CurrencyDesc);
			 
			 if(!boolsbCurrency){
				 applicantObj.setExpectedSalary(Float.parseFloat(txtSalary.getValue().toString()));
			 }
			 
			 if(cbNego.isChecked()){
				 applicantObj.setNegotiable(true);
			 }else{
				 applicantObj.setNegotiable(false);
			 }
			 
			 if(!radioTravelNo.isChecked() && !radioTravelYes.isChecked() && !radioTravelModerate.isChecked())
				 applicantObj.setWillingTravel("-");
			 else	 
				 applicantObj.setWillingTravel(getSelectedRadio(radioTravelNo, radioTravelYes, radioTravelModerate));	 
			 
			 if(!radioRelocateWill.isChecked() && !radioRelocatelNo.isChecked())
				 applicantObj.setWillingRelocate("-");
			 else	 
			 	applicantObj.setWillingRelocate(getSelectedRadio(radioRelocateWill, radioRelocatelNo, null));
			 
			 if(!radioTransportNo.isChecked() && !radioTransportYes.isChecked())
				applicantObj.setOwnTransport("-");
			 else	 
			 	applicantObj.setOwnTransport(getSelectedRadio(radioTransportNo, radioTransportYes, null));
			 
			 applicantObj.setByPassValidation("yes");
			 additionalCol.add(applicantObj);
		 
			 return new Forward(FORWARD_SAVED);
    	 }else
    		 return new Forward(FORWARD_ERROR);
	}
	
	public String getSelectedRadio(Radio name1,Radio name2, Radio name3){
		String key="";
			if(name1.isChecked()){
					key = name1.getText();
			}else if(name2.isChecked()){
					key = name2.getText();
			}else if(name3.isChecked()){
					key = name3.getText();
			}
		
		return key;
	}
	
	public void setFieldReset(){
		setRemoveFileClose(false);
		resumePath.setValue("");
		sbCurrency.setSelectedOption("---");
		txtSalary.setValue("");
		cbNego.setChecked(false);
		 
		radioTravelNo.setChecked(false);
		radioTravelYes.setChecked(false);
		radioTravelModerate.setChecked(false);
		radioRelocateWill.setChecked(false);
		radioRelocatelNo.setChecked(false);
		radioTransportNo.setChecked(false);
		radioTransportYes.setChecked(false);
	}
	
	public void onRequest(Event evt) {
		if(getRemoveFile()!=null && getRemoveFile().equals("yes")){
			//resumePath.setValue(null);
			applicantObj.setStorageFile(null);
			evt.getRequest().getSession().removeAttribute("ssf");
			setRemoveFileClose(false);
			setRemoveFile("no");
		}
		
		/*if(evt.getRequest().getAttribute("attachmentMap")!= null && !evt.getRequest().getAttribute("attachmentMap").equals("")){
			setRemoveFileClose(true);
		}*/
	}
	
	public String getDefaultTemplate() {
		return "recruit/jobAppAdditional";
	}
	
	 /*public static Map getAttachmentMapFromSession(Event event) {
	        Map attachmentMap;
	        HttpSession session;

	        session = event.getRequest().getSession();
	        attachmentMap = (Map) session.getAttribute("attachmentMap");
	        if(attachmentMap==null) {
	            attachmentMap = new SequencedHashMap();
	            session.setAttribute("attachmentMap", attachmentMap);
	        }
	        return attachmentMap;
	 }
	
	 public static void setAttachmentMapToSession(Event event, Map attachmentMap) {
	        HttpSession session;

	        session = event.getRequest().getSession();
	        if(attachmentMap != null) {
	            session.setAttribute("attachmentMap", attachmentMap);
	        }
	 }*/
	 
	//getter setter
	public CheckBox getCbNego() {
		return cbNego;
	}

	public void setCbNego(CheckBox cbNego) {
		this.cbNego = cbNego;
	}

	public Map getMapCurrency() {
		return mapCurrency;
	}

	public void setMapCurrency(Map mapCurrency) {
		this.mapCurrency = mapCurrency;
	}

	public ButtonGroup getRadioGroupRelocate() {
		return radioGroupRelocate;
	}

	public void setRadioGroupRelocate(ButtonGroup radioGroupRelocate) {
		this.radioGroupRelocate = radioGroupRelocate;
	}

	public ButtonGroup getRadioGroupTransport() {
		return radioGroupTransport;
	}

	public void setRadioGroupTransport(ButtonGroup radioGroupTransport) {
		this.radioGroupTransport = radioGroupTransport;
	}

	public ButtonGroup getRadioGroupTravel() {
		return radioGroupTravel;
	}

	public void setRadioGroupTravel(ButtonGroup radioGroupTravel) {
		this.radioGroupTravel = radioGroupTravel;
	}

	public Radio getRadioRelocatelNo() {
		return radioRelocatelNo;
	}

	public void setRadioRelocatelNo(Radio radioRelocatelNo) {
		this.radioRelocatelNo = radioRelocatelNo;
	}

	public Radio getRadioRelocateWill() {
		return radioRelocateWill;
	}

	public void setRadioRelocateWill(Radio radioRelocateWill) {
		this.radioRelocateWill = radioRelocateWill;
	}

	public Radio getRadioTransportNo() {
		return radioTransportNo;
	}

	public void setRadioTransportNo(Radio radioTransportNo) {
		this.radioTransportNo = radioTransportNo;
	}

	public Radio getRadioTransportYes() {
		return radioTransportYes;
	}

	public void setRadioTransportYes(Radio radioTransportYes) {
		this.radioTransportYes = radioTransportYes;
	}

	public Radio getRadioTravelModerate() {
		return radioTravelModerate;
	}

	public void setRadioTravelModerate(Radio radioTravelModerate) {
		this.radioTravelModerate = radioTravelModerate;
	}

	public Radio getRadioTravelNo() {
		return radioTravelNo;
	}

	public void setRadioTravelNo(Radio radioTravelNo) {
		this.radioTravelNo = radioTravelNo;
	}

	public Radio getRadioTravelYes() {
		return radioTravelYes;
	}

	public void setRadioTravelYes(Radio radioTravelYes) {
		this.radioTravelYes = radioTravelYes;
	}

	public FileUpload getResumePath() {
		return resumePath;
	}

	public void setResumePath(FileUpload resumePath) {
		this.resumePath = resumePath;
	}

	public SelectBox getSbCurrency() {
		return sbCurrency;
	}

	public void setSbCurrency(SelectBox sbCurrency) {
		this.sbCurrency = sbCurrency;
	}

	public TextField getTxtSalary() {
		return txtSalary;
	}

	public void setTxtSalary(TextField txtSalary) {
		this.txtSalary = txtSalary;
	}

	public Collection getAdditionalCol() {
		return additionalCol;
	}

	public void setAdditionalCol(Collection additionalCol) {
		this.additionalCol = additionalCol;
	}

	public Button getBtnReset() {
		return btnReset;
	}

	public void setBtnReset(Button btnReset) {
		this.btnReset = btnReset;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public ApplicantObj getApplicantObj() {
		return applicantObj;
	}

	public void setApplicantObj(ApplicantObj applicantObj) {
		this.applicantObj = applicantObj;
	}

	public Label getLblRecFileUpload() {
		return lblRecFileUpload;
	}

	public void setLblRecFileUpload(Label lblRecFileUpload) {
		this.lblRecFileUpload = lblRecFileUpload;
	}

	public String getRemoveFile() {
		return removeFile;
	}

	public void setRemoveFile(String removeFile) {
		this.removeFile = removeFile;
	}

	public boolean isRemoveFileClose() {
		return removeFileClose;
	}

	public void setRemoveFileClose(boolean removeFileClose) {
		this.removeFileClose = removeFileClose;
	}
	
	
}
