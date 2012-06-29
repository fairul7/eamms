package com.tms.collab.isr.setting.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.CheckBox;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.collab.isr.setting.model.EmailSetting;

public class EmailSettingForm extends Form {
	

	
	private String[] emailFor = new String[] {
			EmailSetting.DAILY_DIGEST, EmailSetting.REMARKS,
			EmailSetting.NEW_REQUEST, EmailSetting.RESOLUTION,
			EmailSetting.CLARIFICATION, EmailSetting.ASSIGNMMENT
	};
	private String[]defaultEmailBody = new String[]{
			EmailSetting.dailyDigestDefaultBody, EmailSetting.remarksDefaultBody,
			EmailSetting.newRequestDefaultBody, EmailSetting.resolutionDefaultBody,
			EmailSetting.clarificationDefaultBody, EmailSetting.assignmentDefaultBody			
	};
	private String[]defaultEmailSubject = new String[] {
			EmailSetting.dailyDigestDefaultSubject, EmailSetting.remarksDefaultSubject,
			EmailSetting.newRequestDefaultSubject, EmailSetting.resolutionDefaultSubject,
			EmailSetting.clarificationDefaultSubject, EmailSetting.assigmentDefaultSubject
	};

	protected TextBox body[];
	protected TextField subject[];
	protected ValidatorNotEmpty subjectNotEmptyVali[];
	protected ValidatorNotEmpty bodyNotEmptyVali[];
	protected Label lblEmlFor[];
	protected Label lblEml[];
	protected CheckBox cbxEmail;
	protected CheckBox cbxMemo;
	protected Label lblEmail;
	protected Label lblMemo;
	protected Label lblNotifyMethod;
	protected Panel p1[];
	protected Panel p2[];
	protected Panel p3;
	protected Button btnSubmit;
	private Collection emailSettings;
	public static final String FORWARD_SUCCESS = "success";
    public static final String FORWARD_ERROR = "error";
    public static final String FORWARD_ERRORNOTIFY = "errorNotify";
	
	public void init(){
		body = new TextBox[emailFor.length];
		subject = new TextField[emailFor.length];
		lblEmlFor = new Label[emailFor.length];
		lblEml = new Label[emailFor.length];

		
		p1 = new Panel[emailFor.length];
		p2 = new Panel[emailFor.length];
		
		subjectNotEmptyVali = new ValidatorNotEmpty[emailFor.length];
		bodyNotEmptyVali = new ValidatorNotEmpty[emailFor.length];
		
		initForm();
	}
	
	public void onRequest(Event evt) {
		ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
		emailSettings = model.selectEmailSettings(emailFor);
		
		populateForm();
	}
	
	public Forward onValidate(Event event) {
		
		String button = findButtonClicked(event);
        button = button == null ? "" : button;
        boolean isSuccess = true;
        
        if (button.endsWith("btnSubmit")) {
        	ArrayList emlSettingList = new ArrayList();
    		
    		for(int i=0; i<emailFor.length; i++){
    			EmailSetting st = new EmailSetting();
    			st.setEmailFor(emailFor[i]);
    			st.setEmailSubject(subject[i].getValue().toString());
    			st.setEmailBody(body[i].getValue().toString());
    			
    			if (cbxEmail.isChecked() && cbxMemo.isChecked())
    				st.setNotifyMethod("b");
    			else if (cbxEmail.isChecked())
    				st.setNotifyMethod("e");
    			else if (cbxMemo.isChecked())
    				st.setNotifyMethod("m");
    			else 
    				return new Forward(FORWARD_ERRORNOTIFY);
    			
    			emlSettingList.add(st);
    		}
    		
    		ConfigModel model = (ConfigModel)Application.getInstance().getModule(ConfigModel.class);
    		isSuccess = model.updateEmailSettings(emlSettingList);
        }
		
		if(isSuccess) 
			return new Forward(FORWARD_SUCCESS);
		else 
			return new Forward(FORWARD_ERROR);
	}
	
	public void populateForm(){
		
		Iterator i = emailSettings.iterator();

		while(i.hasNext()){
			EmailSetting eml = (EmailSetting)i.next();
				
			for(int j=0; j<emailFor.length; j++){
				if(eml.getEmailFor().equals(emailFor[j])){
					subject[j].setValue((String)eml.getEmailSubject());
					body[j].setValue((String)eml.getEmailBody());					
				}
			}
			
			if (eml.getNotifyMethod().toString().equals("m"))
				cbxEmail.setChecked(false);
			else if (eml.getNotifyMethod().toString().equals("e"))
				cbxMemo.setChecked(false);
		}
		
	}
	
	public void initForm(){
		setMethod("POST");
		removeChildren();
		setColumns(2);
		Application application = Application.getInstance();
		
		for(int i=0; i<emailFor.length; i++){
			lblEmlFor[i] = new Label("lbl"+i, "<span class=\"fieldTitle\">" + application.getMessage(emailFor[i]) + " *" + "</span>");
			lblEmlFor[i].setAlign("right");
			addChild(lblEmlFor[i]);
			
			lblEml[i] = new Label("lblEml"+i, application.getMessage("isr.label.emailSubject"));
			
			subjectNotEmptyVali[i] = new ValidatorNotEmpty("sbjNotEmpty"+i, application.getMessage("isr.validator.notEmpty"));
			subject[i] = new TextField("tf"+i);
			subject[i].setValue( application.getMessage(defaultEmailSubject[i].toString()));
			subject[i].addChild(subjectNotEmptyVali[i]);
			
			p2[i] = new Panel("p2"+i);
			p2[i].setColumns(2);
			p2[i].addChild(lblEml[i]);
			p2[i].addChild(subject[i]);	
				
			bodyNotEmptyVali[i] = new ValidatorNotEmpty("bodyNotEmpty"+i, application.getMessage("isr.validator.notEmpty"));
			body[i] = new TextBox("txt1"+i);
			body[i].setValue( application.getMessage(defaultEmailBody[i].toString()));
			body[i].addChild(bodyNotEmptyVali[i]);
			body[i].setCols("60");
			body[i].setRows("3");
			
			p1[i] = new Panel("p1"+i);
			p1[i].setColumns(1);
			p1[i].addChild(p2[i]);
			p1[i].addChild(body[i]);
		//	p1[i].addChild(p3[i]);
			
			addChild(p1[i]);
		}
		
		lblNotifyMethod = new Label("lblNotifyMethod",  "<span class=\"fieldTitle\">" +application.getMessage("isr.label.notifyMethod") + " *" + "</span>");
		lblNotifyMethod.setAlign("right");
		addChild(lblNotifyMethod);
		lblEmail = new Label("lblEmail", application.getMessage("isr.label.email"));
		lblMemo = new Label("lblMemo", application.getMessage("isr.label.memo"));
		cbxEmail = new CheckBox("cbxEmail");
		cbxEmail.setChecked(true);
		cbxMemo = new CheckBox("cbxMemo");
		cbxMemo.setChecked(true);
		
		p3 = new Panel ("p3");
		p3.setColumns(4);
		p3.addChild(cbxEmail);
		p3.addChild(lblEmail);
		p3.addChild(cbxMemo);
		p3.addChild(lblMemo);
		addChild(p3);
		
		addChild(new Label("dunnylbl", ""));	
		
		btnSubmit = new Button("btnSubmit", application.getMessage("isr.label.submit", "Submit"));
		btnSubmit.setOnClick("return confirmSubmit()");
		addChild(btnSubmit);
	}
	
	public TextBox[] getBody() {
		return body;
	}
	public ValidatorNotEmpty[] getBodyNotEmptyVali() {
		return bodyNotEmptyVali;
	}
	public Button getBtnSubmit() {
		return btnSubmit;
	}
	public Collection getEmailSettings() {
		return emailSettings;
	}
	public Label[] getLblEml() {
		return lblEml;
	}
	public Label[] getLblEmlFor() {
		return lblEmlFor;
	}
	public Panel[] getP1() {
		return p1;
	}
	public Panel[] getP2() {
		return p2;
	}
	public TextField[] getSubject() {
		return subject;
	}
	public ValidatorNotEmpty[] getSubjectNotEmptyVali() {
		return subjectNotEmptyVali;
	}
}
