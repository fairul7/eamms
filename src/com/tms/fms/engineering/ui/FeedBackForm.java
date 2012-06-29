package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import kacang.Application;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Radio;
import kacang.stdui.ResetButton;
import kacang.stdui.TextBox;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.UnitHeadModule;

public class FeedBackForm extends Form {
	
	protected String requestId;
	protected EngineeringRequest request=new EngineeringRequest();
	 
	protected Button submit;
	protected Button cancel;
	protected ResetButton rsButton;
	private Label lbQuestions[];	
	
	private Label lbSCPQuestions[];
	protected Radio[] scpAnswer0, scpAnswer1, scpAnswer2, scpAnswer3, scpAnswer4, scpAnswer5, scpAnswer6, scpAnswer7;
	
	private Label lbPOSTQuestions[];
	protected Radio[] postAnswer0, postAnswer1, postAnswer2, postAnswer3, postAnswer4, postAnswer5, postAnswer6, postAnswer7;
	
	private Label lbVTRQuestions[];
	protected Radio[] vtrAnswer0, vtrAnswer1, vtrAnswer2, vtrAnswer3, vtrAnswer4, vtrAnswer5, vtrAnswer6, vtrAnswer7;
	
	private Label lbMANQuestions[];
	protected Radio[] manAnswer0, manAnswer1, manAnswer2, manAnswer3, manAnswer4, manAnswer5, manAnswer6, manAnswer7;
	
	private Label lbSTDQuestions[];
	protected Radio[] stdAnswer0, stdAnswer1, stdAnswer2, stdAnswer3, stdAnswer4, stdAnswer5, stdAnswer6, stdAnswer7;
	
	private Label lbOTHQuestions[];
	protected Radio[] othAnswer0, othAnswer1, othAnswer2, othAnswer3, othAnswer4, othAnswer5, othAnswer6, othAnswer7;
	
	private Label lbTVROQuestions[];
	protected Radio[] tvroAnswer0, tvroAnswer1, tvroAnswer2, tvroAnswer3, tvroAnswer4, tvroAnswer5, tvroAnswer6, tvroAnswer7;
	
	protected TextBox remarks;
	protected boolean viewMode=true;
	protected boolean isHod=false;
	protected boolean isFC=false;
	protected boolean isFCHead=false;
	protected boolean isHOU=false;
	
	protected Double totalInternalRate = 0.0;
	protected Double totalExternalRate = 0.0;
	
	protected String requestTypeLabel = "";
	
	public FeedBackForm() {
	}

	public FeedBackForm(String s) {super(s);}

	public void init() {
		Application app=Application.getInstance();
		submit = new Button("submit", app.getMessage("fms.facility.submit"));
		cancel = new Button("cancel", app.getMessage("fms.facility.cancel"));
		rsButton = new ResetButton("Reset");
		rsButton.setText("Reset");
				
		remarks=new TextBox("remarks");
		remarks.setSize("80");
		remarks.setRows("5");
		
		//Requestor
		addChild(submit);
		addChild(cancel);
		addChild(rsButton);
		addChild(remarks);
	}
	
	public void onRequest(Event arg0) {
		String userId=Application.getInstance().getCurrentUser().getId();
		isHod=EngineeringModule.isHOD(userId);
		isFCHead=EngineeringModule.isFCHead(userId);
		isFC=FacilitiesCoordinatorModule.isFC(userId);
		isHOU=UnitHeadModule.isUnitApprover(userId);
		init();
		populateRequest();
		
	}
	
	private void populateRequest(){
		setMethod("post");
		EngineeringModule module=(EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		request=module.getRequestWithService(requestId);
		request.setCreatedUserName(request.getCreatedUserName());
		request.setApprovedBy(request.getApproverUserName());
		
		viewMode=request.isViewMode();
		requestTypeLabel = (String)EngineeringModule.REQUEST_TYPE_MAP.get(request.getRequestType());
		totalInternalRate = 0.0;
		totalExternalRate = 0.0;
		
		totalInternalRate = module.getTotalRate(requestId, "I");
		totalExternalRate = module.getTotalRate(requestId, "E");
		
		Collection services=request.getServices();		
		
		int count=0;
		for (Iterator itrx = services.iterator(); itrx.hasNext();count++){
			Service service = (Service) itrx.next();
			
			if (ServiceDetailsForm.SERVICE_SCPMCP.equals(service.getServiceId())) {
				
				lbSCPQuestions = new Label[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer0 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer1 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer2 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer3 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer4 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer5 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer6 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				scpAnswer7 = new Radio[EngineeringModule.QUESTIONS_SCP_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_SCP_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbSCPQuestions[x] = new Label("lbQuestionSCP"+key);
					lbSCPQuestions[x].setText((String)EngineeringModule.QUESTIONS_SCP_MAP.get(key));
					addChild(lbSCPQuestions[x]);
					
					scpAnswer0[x] = new Radio("asw0" + x);
					scpAnswer0[x].setText("Not Applicable");
					scpAnswer0[x].setGroupName("SCP"+x);
					addChild(scpAnswer0[x]);
					
					scpAnswer1[x] = new Radio("asw1" + x);
					scpAnswer1[x].setText("1");
					scpAnswer1[x].setGroupName("SCP"+x);
					addChild(scpAnswer1[x]);
					
					scpAnswer2[x] = new Radio("asw2" + x);
					scpAnswer2[x].setText("2");
					scpAnswer2[x].setGroupName("SCP"+x);
					addChild(scpAnswer2[x]);
					
					scpAnswer3[x] = new Radio("asw3" + x);
					scpAnswer3[x].setText("3");
					scpAnswer3[x].setGroupName("SCP"+x);
					addChild(scpAnswer3[x]);
					
					scpAnswer4[x] = new Radio("asw4" + x);
					scpAnswer4[x].setText("4");
					scpAnswer4[x].setGroupName("SCP"+x);
					addChild(scpAnswer4[x]);
					
					scpAnswer5[x] = new Radio("asw5" + x);
					scpAnswer5[x].setText("5");
					scpAnswer5[x].setGroupName("SCP"+x);
					addChild(scpAnswer5[x]);
					
					scpAnswer6[x] = new Radio("asw6" + x);
					scpAnswer6[x].setText("6");
					scpAnswer6[x].setGroupName("SCP"+x);
					addChild(scpAnswer6[x]);
					
					scpAnswer7[x] = new Radio("asw7" + x);
					scpAnswer7[x].setText("7");
					scpAnswer7[x].setGroupName("SCP"+x);
					addChild(scpAnswer7[x]);
					
				}
				
			} else if (ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(service.getServiceId())) {
				
				lbPOSTQuestions = new Label[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer0 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer1 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer2 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer3 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer4 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer5 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer6 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				postAnswer7 = new Radio[EngineeringModule.QUESTIONS_POST_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_POST_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbPOSTQuestions[x] = new Label("lbQuestionPOST"+key);
					lbPOSTQuestions[x].setText((String)EngineeringModule.QUESTIONS_POST_MAP.get(key));
					addChild(lbPOSTQuestions[x]);
					
					postAnswer0[x] = new Radio("post0" + x);
					postAnswer0[x].setText("Not Applicable");
					postAnswer0[x].setGroupName("post"+x);
					addChild(postAnswer0[x]);
					
					postAnswer1[x] = new Radio("post1" + x);
					postAnswer1[x].setText("1");
					postAnswer1[x].setGroupName("post"+x);
					addChild(postAnswer1[x]);
					
					postAnswer2[x] = new Radio("post2" + x);
					postAnswer2[x].setText("2");
					postAnswer2[x].setGroupName("post"+x);
					addChild(postAnswer2[x]);
					
					postAnswer3[x] = new Radio("post3" + x);
					postAnswer3[x].setText("3");
					postAnswer3[x].setGroupName("post"+x);
					addChild(postAnswer3[x]);
					
					postAnswer4[x] = new Radio("post4" + x);
					postAnswer4[x].setText("4");
					postAnswer4[x].setGroupName("post"+x);
					addChild(postAnswer4[x]);
					
					postAnswer5[x] = new Radio("post5" + x);
					postAnswer5[x].setText("5");
					postAnswer5[x].setGroupName("post"+x);
					addChild(postAnswer5[x]);
					
					postAnswer6[x] = new Radio("post6" + x);
					postAnswer6[x].setText("6");
					postAnswer6[x].setGroupName("post"+x);
					addChild(postAnswer6[x]);
					
					postAnswer7[x] = new Radio("post7" + x);
					postAnswer7[x].setText("7");
					postAnswer7[x].setGroupName("post"+x);
					addChild(postAnswer7[x]);
					
				}
				
			} else if (ServiceDetailsForm.SERVICE_VTR.equals(service.getServiceId())) {
				
				lbVTRQuestions = new Label[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer0 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer1 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer2 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer3 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer4 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer5 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer6 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				vtrAnswer7 = new Radio[EngineeringModule.QUESTIONS_VTR_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_VTR_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbVTRQuestions[x] = new Label("lbQuestionVTR"+key);
					lbVTRQuestions[x].setText((String)EngineeringModule.QUESTIONS_VTR_MAP.get(key));
					addChild(lbVTRQuestions[x]);
					
					vtrAnswer0[x] = new Radio("vtr0" + x);
					vtrAnswer0[x].setText("Not Applicable");
					vtrAnswer0[x].setGroupName("vtr"+x);
					addChild(vtrAnswer0[x]);
					
					vtrAnswer1[x] = new Radio("vtr1" + x);
					vtrAnswer1[x].setText("1");
					vtrAnswer1[x].setGroupName("vtr"+x);
					addChild(vtrAnswer1[x]);
					
					vtrAnswer2[x] = new Radio("vtr2" + x);
					vtrAnswer2[x].setText("2");
					vtrAnswer2[x].setGroupName("vtr"+x);
					addChild(vtrAnswer2[x]);
					
					vtrAnswer3[x] = new Radio("vtr3" + x);
					vtrAnswer3[x].setText("3");
					vtrAnswer3[x].setGroupName("vtr"+x);
					addChild(vtrAnswer3[x]);
					
					vtrAnswer4[x] = new Radio("vtr4" + x);
					vtrAnswer4[x].setText("4");
					vtrAnswer4[x].setGroupName("vtr"+x);
					addChild(vtrAnswer4[x]);
					
					vtrAnswer5[x] = new Radio("vtr5" + x);
					vtrAnswer5[x].setText("5");
					vtrAnswer5[x].setGroupName("vtr"+x);
					addChild(vtrAnswer5[x]);
					
					vtrAnswer6[x] = new Radio("vtr6" + x);
					vtrAnswer6[x].setText("6");
					vtrAnswer6[x].setGroupName("vtr"+x);
					addChild(vtrAnswer6[x]);
					
					vtrAnswer7[x] = new Radio("vtr7" + x);
					vtrAnswer7[x].setText("7");
					vtrAnswer7[x].setGroupName("vtr"+x);
					addChild(vtrAnswer7[x]);
					
				}
			} else if (ServiceDetailsForm.SERVICE_MANPOWER.equals(service.getServiceId())) {
				
				lbMANQuestions = new Label[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer0 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer1 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer2 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer3 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer4 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer5 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer6 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				manAnswer7 = new Radio[EngineeringModule.QUESTIONS_MAN_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_MAN_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbMANQuestions[x] = new Label("lbQuestionman"+key);
					lbMANQuestions[x].setText((String)EngineeringModule.QUESTIONS_MAN_MAP.get(key));
					addChild(lbMANQuestions[x]);
					
					manAnswer0[x] = new Radio("man0" + x);
					manAnswer0[x].setText("Not Applicable");
					manAnswer0[x].setGroupName("man"+x);
					addChild(manAnswer0[x]);
					
					manAnswer1[x] = new Radio("man1" + x);
					manAnswer1[x].setText("1");
					manAnswer1[x].setGroupName("man"+x);
					addChild(manAnswer1[x]);
					
					manAnswer2[x] = new Radio("man2" + x);
					manAnswer2[x].setText("2");
					manAnswer2[x].setGroupName("man"+x);
					addChild(manAnswer2[x]);
					
					manAnswer3[x] = new Radio("man3" + x);
					manAnswer3[x].setText("3");
					manAnswer3[x].setGroupName("man"+x);
					addChild(manAnswer3[x]);
					
					manAnswer4[x] = new Radio("man4" + x);
					manAnswer4[x].setText("4");
					manAnswer4[x].setGroupName("man"+x);
					addChild(manAnswer4[x]);
					
					manAnswer5[x] = new Radio("man5" + x);
					manAnswer5[x].setText("5");
					manAnswer5[x].setGroupName("man"+x);
					addChild(manAnswer5[x]);
					
					manAnswer6[x] = new Radio("man6" + x);
					manAnswer6[x].setText("6");
					manAnswer6[x].setGroupName("man"+x);
					addChild(manAnswer6[x]);
					
					manAnswer7[x] = new Radio("man7" + x);
					manAnswer7[x].setText("7");
					manAnswer7[x].setGroupName("man"+x);
					addChild(manAnswer7[x]);
					
				}
			} else if (ServiceDetailsForm.SERVICE_STUDIO.equals(service.getServiceId())) {
				
				lbSTDQuestions = new Label[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer0 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer1 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer2 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer3 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer4 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer5 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer6 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				stdAnswer7 = new Radio[EngineeringModule.QUESTIONS_STD_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_STD_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbSTDQuestions[x] = new Label("lbQuestionstd"+key);
					lbSTDQuestions[x].setText((String)EngineeringModule.QUESTIONS_STD_MAP.get(key));
					addChild(lbSTDQuestions[x]);
					
					stdAnswer0[x] = new Radio("std0" + x);
					stdAnswer0[x].setText("Not Applicable");
					stdAnswer0[x].setGroupName("std"+x);
					addChild(stdAnswer0[x]);
					
					stdAnswer1[x] = new Radio("std1" + x);
					stdAnswer1[x].setText("1");
					stdAnswer1[x].setGroupName("std"+x);
					addChild(stdAnswer1[x]);
					
					stdAnswer2[x] = new Radio("std2" + x);
					stdAnswer2[x].setText("2");
					stdAnswer2[x].setGroupName("std"+x);
					addChild(stdAnswer2[x]);
					
					stdAnswer3[x] = new Radio("std3" + x);
					stdAnswer3[x].setText("3");
					stdAnswer3[x].setGroupName("std"+x);
					addChild(stdAnswer3[x]);
					
					stdAnswer4[x] = new Radio("std4" + x);
					stdAnswer4[x].setText("4");
					stdAnswer4[x].setGroupName("std"+x);
					addChild(stdAnswer4[x]);
					
					stdAnswer5[x] = new Radio("std5" + x);
					stdAnswer5[x].setText("5");
					stdAnswer5[x].setGroupName("std"+x);
					addChild(stdAnswer5[x]);
					
					stdAnswer6[x] = new Radio("std6" + x);
					stdAnswer6[x].setText("6");
					stdAnswer6[x].setGroupName("std"+x);
					addChild(stdAnswer6[x]);
					
					stdAnswer7[x] = new Radio("std7" + x);
					stdAnswer7[x].setText("7");
					stdAnswer7[x].setGroupName("std"+x);
					addChild(stdAnswer7[x]);
					
				}
			} else if (ServiceDetailsForm.SERVICE_OTHER.equals(service.getServiceId())) {
				
				lbOTHQuestions = new Label[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer0 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer1 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer2 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer3 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer4 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer5 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer6 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				othAnswer7 = new Radio[EngineeringModule.QUESTIONS_OTH_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_OTH_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbOTHQuestions[x] = new Label("lbQuestionoth"+key);
					lbOTHQuestions[x].setText((String)EngineeringModule.QUESTIONS_OTH_MAP.get(key));
					addChild(lbOTHQuestions[x]);
					
					othAnswer0[x] = new Radio("oth0" + x);
					othAnswer0[x].setText("Not Applicable");
					othAnswer0[x].setGroupName("oth"+x);
					addChild(othAnswer0[x]);
					
					othAnswer1[x] = new Radio("oth1" + x);
					othAnswer1[x].setText("1");
					othAnswer1[x].setGroupName("oth"+x);
					addChild(othAnswer1[x]);
					
					othAnswer2[x] = new Radio("oth2" + x);
					othAnswer2[x].setText("2");
					othAnswer2[x].setGroupName("oth"+x);
					addChild(othAnswer2[x]);
					
					othAnswer3[x] = new Radio("oth3" + x);
					othAnswer3[x].setText("3");
					othAnswer3[x].setGroupName("oth"+x);
					addChild(othAnswer3[x]);
					
					othAnswer4[x] = new Radio("oth4" + x);
					othAnswer4[x].setText("4");
					othAnswer4[x].setGroupName("oth"+x);
					addChild(othAnswer4[x]);
					
					othAnswer5[x] = new Radio("oth5" + x);
					othAnswer5[x].setText("5");
					othAnswer5[x].setGroupName("oth"+x);
					addChild(othAnswer5[x]);
					
					othAnswer6[x] = new Radio("oth6" + x);
					othAnswer6[x].setText("6");
					othAnswer6[x].setGroupName("oth"+x);
					addChild(othAnswer6[x]);
					
					othAnswer7[x] = new Radio("oth7" + x);
					othAnswer7[x].setText("7");
					othAnswer7[x].setGroupName("oth"+x);
					addChild(othAnswer7[x]);
					
				}
			} else if (ServiceDetailsForm.SERVICE_TVRO.equals(service.getServiceId())) {
				
				lbTVROQuestions = new Label[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer0 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer1 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer2 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer3 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer4 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer5 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer6 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				tvroAnswer7 = new Radio[EngineeringModule.QUESTIONS_TVRO_MAP.size()];
				
				int x=0;			
				for(Iterator itr=EngineeringModule.QUESTIONS_TVRO_MAP.keySet().iterator();itr.hasNext();x++){
					String key=(String)itr.next();
					lbTVROQuestions[x] = new Label("lbQuestiontvro"+key);
					lbTVROQuestions[x].setText((String)EngineeringModule.QUESTIONS_TVRO_MAP.get(key));
					addChild(lbTVROQuestions[x]);
					
					tvroAnswer0[x] = new Radio("tvro0" + x);
					tvroAnswer0[x].setText("Not Applicable");
					tvroAnswer0[x].setGroupName("tvro"+x);
					addChild(tvroAnswer0[x]);
					
					tvroAnswer1[x] = new Radio("tvro1" + x);
					tvroAnswer1[x].setText("1");
					tvroAnswer1[x].setGroupName("tvro"+x);
					addChild(tvroAnswer1[x]);
					
					tvroAnswer2[x] = new Radio("tvro2" + x);
					tvroAnswer2[x].setText("2");
					tvroAnswer2[x].setGroupName("tvro"+x);
					addChild(tvroAnswer2[x]);
					
					tvroAnswer3[x] = new Radio("tvro3" + x);
					tvroAnswer3[x].setText("3");
					tvroAnswer3[x].setGroupName("tvro"+x);
					addChild(tvroAnswer3[x]);
					
					tvroAnswer4[x] = new Radio("tvro4" + x);
					tvroAnswer4[x].setText("4");
					tvroAnswer4[x].setGroupName("tvro"+x);
					addChild(tvroAnswer4[x]);
					
					tvroAnswer5[x] = new Radio("tvro5" + x);
					tvroAnswer5[x].setText("5");
					tvroAnswer5[x].setGroupName("tvro"+x);
					addChild(tvroAnswer5[x]);
					
					tvroAnswer6[x] = new Radio("tvro6" + x);
					tvroAnswer6[x].setText("6");
					tvroAnswer6[x].setGroupName("tvro"+x);
					addChild(tvroAnswer6[x]);
					
					tvroAnswer7[x] = new Radio("tvro7" + x);
					tvroAnswer7[x].setText("7");
					tvroAnswer7[x].setGroupName("tvro"+x);
					addChild(tvroAnswer7[x]);
					
				}
			}
			
		}
	}
	
	public Forward onSubmit(Event evt) {
		String buttonName = findButtonClicked(evt);
		kacang.ui.Forward result = super.onSubmit(evt);
		if (buttonName != null && cancel.getAbsoluteName().equals(buttonName)) {
			return new Forward(Form.CANCEL_FORM_ACTION, "requestListing.jsp", true);
		} else {return result;}
	}
	
	@Override
	public Forward onValidate(Event arg0) {
		String buttonName = findButtonClicked(arg0);
		if (buttonName != null && submit.getAbsoluteName().equals(buttonName)) {
			EngineeringModule module	= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			Collection col = new ArrayList();
			
			
			Collection services=request.getServices();		
			
			int count=0;
			for (Iterator itrx = services.iterator(); itrx.hasNext();count++){
				Service service = (Service) itrx.next();
				
				if (ServiceDetailsForm.SERVICE_SCPMCP.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_SCP_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_SCPMCP);
						serviceFeedback.put("question", key);
						
						if (scpAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (scpAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (scpAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (scpAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (scpAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (scpAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (scpAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (scpAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				} else if (ServiceDetailsForm.SERVICE_POSTPRODUCTION.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_POST_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_POSTPRODUCTION);
						serviceFeedback.put("question", key);
						
						if (postAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (postAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (postAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (postAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (postAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (postAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (postAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (postAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				} else if (ServiceDetailsForm.SERVICE_VTR.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_VTR_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_VTR);
						serviceFeedback.put("question", key);
						
						if (vtrAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (vtrAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (vtrAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (vtrAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (vtrAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (vtrAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (vtrAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (vtrAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				} else if (ServiceDetailsForm.SERVICE_MANPOWER.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_MAN_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_MANPOWER);
						serviceFeedback.put("question", key);
						
						if (manAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (manAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (manAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (manAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (manAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (manAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (manAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (manAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				} else if (ServiceDetailsForm.SERVICE_STUDIO.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_STD_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_STUDIO);
						serviceFeedback.put("question", key);
						
						if (stdAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (stdAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (stdAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (stdAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (stdAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (stdAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (stdAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (stdAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				} else if (ServiceDetailsForm.SERVICE_OTHER.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_OTH_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_OTHER);
						serviceFeedback.put("question", key);
						
						if (othAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (othAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (othAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (othAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (othAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (othAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (othAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (othAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				} else if (ServiceDetailsForm.SERVICE_TVRO.equals(service.getServiceId())) {
					int x=0;			
					for(Iterator itr=EngineeringModule.QUESTIONS_TVRO_MAP.keySet().iterator();itr.hasNext();x++){
						String key=(String)itr.next();
						HashMap serviceFeedback = new HashMap();
						serviceFeedback.put("requestId", request.getRequestId());
						serviceFeedback.put("serviceType", ServiceDetailsForm.SERVICE_TVRO);
						serviceFeedback.put("question", key);
						
						if (tvroAnswer0[x].isChecked()){
							serviceFeedback.put("answer", "0");
						} else if (tvroAnswer1[x].isChecked()){
							serviceFeedback.put("answer", "1");
						} else if (tvroAnswer2[x].isChecked()){
							serviceFeedback.put("answer", "2");
						} else if (tvroAnswer3[x].isChecked()){
							serviceFeedback.put("answer", "3");
						} else if (tvroAnswer4[x].isChecked()){
							serviceFeedback.put("answer", "4");
						} else if (tvroAnswer5[x].isChecked()){
							serviceFeedback.put("answer", "5");
						} else if (tvroAnswer6[x].isChecked()){
							serviceFeedback.put("answer", "6");
						} else if (tvroAnswer7[x].isChecked()){
							serviceFeedback.put("answer", "7");
						}
						
						col.add(serviceFeedback);
					}
				}
			}
			
			// insert into feedback table
			module.insertFeedBack(col);
			
			// change request status into close
			module.closeRequest(request.getRequestId(), (String)remarks.getValue());
			
			return new Forward("SUBMITTED");
		}
		
		return new Forward("");
	}
	
	public String getDefaultTemplate() {
		return "fms/engineering/feedBackFormTemplate";
	}
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public EngineeringRequest getRequest() {
		return request;
	}

	public void setRequest(EngineeringRequest request) {
		this.request = request;
	}

	public Button getCancel() {
		return cancel;
	}

	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}

	public Button getSubmit() {
		return submit;
	}

	public void setSubmit(Button submit) {
		this.submit = submit;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean getIsHod() {
		return isHod;
	}

	public TextBox getRemarks() {
		return remarks;
	}

	public void setRemarks(TextBox remarks) {
		this.remarks = remarks;
	}

	public boolean getIsFC() {
		return isFC;
	}

	public void setFC(boolean isFC) {
		this.isFC = isFC;
	}

	public boolean getIsFCHead() {
		return isFCHead;
	}

	public void setFCHead(boolean isFCHead) {
		this.isFCHead = isFCHead;
	}

	public void setHod(boolean isHod) {
		this.isHod = isHod;
	}

	public Double getTotalInternalRate() {
		return totalInternalRate;
	}

	public void setTotalInternalRate(Double totalInternalRate) {
		this.totalInternalRate = totalInternalRate;
	}

	public Double getTotalExternalRate() {
		return totalExternalRate;
	}

	public void setTotalExternalRate(Double totalExternalRate) {
		this.totalExternalRate = totalExternalRate;
	}

	public String getRequestTypeLabel() {
		return requestTypeLabel;
	}

	public void setRequestTypeLabel(String requestTypeLabel) {
		this.requestTypeLabel = requestTypeLabel;
	}
	
	public boolean getIsHOU() {
		return isHOU;
	}

	public void setHOU(boolean isHOU) {
		this.isHOU = isHOU;
	}
	
	public Label[] getLbSCPQuestions() {
		return lbSCPQuestions;
	}

	public void setLbSCPQuestions(Label[] lbSCPQuestions) {
		this.lbSCPQuestions = lbSCPQuestions;
	}

	public Label[] getLbQuestions() {
		return lbQuestions;
	}

	public void setLbQuestions(Label[] lbQuestions) {
		this.lbQuestions = lbQuestions;
	}

	public Radio[] getScpAnswer0() {
		return scpAnswer0;
	}

	public void setScpAnswer0(Radio[] scpAnswer0) {
		this.scpAnswer0 = scpAnswer0;
	}

	public Radio[] getScpAnswer1() {
		return scpAnswer1;
	}

	public void setScpAnswer1(Radio[] scpAnswer1) {
		this.scpAnswer1 = scpAnswer1;
	}

	public Radio[] getScpAnswer2() {
		return scpAnswer2;
	}

	public void setScpAnswer2(Radio[] scpAnswer2) {
		this.scpAnswer2 = scpAnswer2;
	}

	public Radio[] getScpAnswer3() {
		return scpAnswer3;
	}

	public void setScpAnswer3(Radio[] scpAnswer3) {
		this.scpAnswer3 = scpAnswer3;
	}

	public Radio[] getScpAnswer4() {
		return scpAnswer4;
	}

	public void setScpAnswer4(Radio[] scpAnswer4) {
		this.scpAnswer4 = scpAnswer4;
	}

	public Radio[] getScpAnswer5() {
		return scpAnswer5;
	}

	public void setScpAnswer5(Radio[] scpAnswer5) {
		this.scpAnswer5 = scpAnswer5;
	}

	public Radio[] getScpAnswer6() {
		return scpAnswer6;
	}

	public void setScpAnswer6(Radio[] scpAnswer6) {
		this.scpAnswer6 = scpAnswer6;
	}

	public Radio[] getScpAnswer7() {
		return scpAnswer7;
	}

	public void setScpAnswer7(Radio[] scpAnswer7) {
		this.scpAnswer7 = scpAnswer7;
	}

	public Label[] getLbPOSTQuestions() {
		return lbPOSTQuestions;
	}

	public void setLbPOSTQuestions(Label[] lbPOSTQuestions) {
		this.lbPOSTQuestions = lbPOSTQuestions;
	}

	public Radio[] getPostAnswer0() {
		return postAnswer0;
	}

	public void setPostAnswer0(Radio[] postAnswer0) {
		this.postAnswer0 = postAnswer0;
	}

	public Radio[] getPostAnswer1() {
		return postAnswer1;
	}

	public void setPostAnswer1(Radio[] postAnswer1) {
		this.postAnswer1 = postAnswer1;
	}

	public Radio[] getPostAnswer2() {
		return postAnswer2;
	}

	public void setPostAnswer2(Radio[] postAnswer2) {
		this.postAnswer2 = postAnswer2;
	}

	public Radio[] getPostAnswer3() {
		return postAnswer3;
	}

	public void setPostAnswer3(Radio[] postAnswer3) {
		this.postAnswer3 = postAnswer3;
	}

	public Radio[] getPostAnswer4() {
		return postAnswer4;
	}

	public void setPostAnswer4(Radio[] postAnswer4) {
		this.postAnswer4 = postAnswer4;
	}

	public Radio[] getPostAnswer5() {
		return postAnswer5;
	}

	public void setPostAnswer5(Radio[] postAnswer5) {
		this.postAnswer5 = postAnswer5;
	}

	public Radio[] getPostAnswer6() {
		return postAnswer6;
	}

	public void setPostAnswer6(Radio[] postAnswer6) {
		this.postAnswer6 = postAnswer6;
	}

	public Radio[] getPostAnswer7() {
		return postAnswer7;
	}

	public void setPostAnswer7(Radio[] postAnswer7) {
		this.postAnswer7 = postAnswer7;
	}

	public Label[] getLbVTRQuestions() {
		return lbVTRQuestions;
	}

	public void setLbVTRQuestions(Label[] lbVTRQuestions) {
		this.lbVTRQuestions = lbVTRQuestions;
	}

	public Radio[] getVtrAnswer0() {
		return vtrAnswer0;
	}

	public void setVtrAnswer0(Radio[] vtrAnswer0) {
		this.vtrAnswer0 = vtrAnswer0;
	}

	public Radio[] getVtrAnswer1() {
		return vtrAnswer1;
	}

	public void setVtrAnswer1(Radio[] vtrAnswer1) {
		this.vtrAnswer1 = vtrAnswer1;
	}

	public Radio[] getVtrAnswer2() {
		return vtrAnswer2;
	}

	public void setVtrAnswer2(Radio[] vtrAnswer2) {
		this.vtrAnswer2 = vtrAnswer2;
	}

	public Radio[] getVtrAnswer3() {
		return vtrAnswer3;
	}

	public void setVtrAnswer3(Radio[] vtrAnswer3) {
		this.vtrAnswer3 = vtrAnswer3;
	}

	public Radio[] getVtrAnswer4() {
		return vtrAnswer4;
	}

	public void setVtrAnswer4(Radio[] vtrAnswer4) {
		this.vtrAnswer4 = vtrAnswer4;
	}

	public Radio[] getVtrAnswer5() {
		return vtrAnswer5;
	}

	public void setVtrAnswer5(Radio[] vtrAnswer5) {
		this.vtrAnswer5 = vtrAnswer5;
	}

	public Radio[] getVtrAnswer6() {
		return vtrAnswer6;
	}

	public void setVtrAnswer6(Radio[] vtrAnswer6) {
		this.vtrAnswer6 = vtrAnswer6;
	}

	public Radio[] getVtrAnswer7() {
		return vtrAnswer7;
	}

	public void setVtrAnswer7(Radio[] vtrAnswer7) {
		this.vtrAnswer7 = vtrAnswer7;
	}

	public Label[] getLbMANQuestions() {
		return lbMANQuestions;
	}

	public void setLbMANQuestions(Label[] lbMANQuestions) {
		this.lbMANQuestions = lbMANQuestions;
	}

	public Radio[] getManAnswer0() {
		return manAnswer0;
	}

	public void setManAnswer0(Radio[] manAnswer0) {
		this.manAnswer0 = manAnswer0;
	}

	public Radio[] getManAnswer1() {
		return manAnswer1;
	}

	public void setManAnswer1(Radio[] manAnswer1) {
		this.manAnswer1 = manAnswer1;
	}

	public Radio[] getManAnswer2() {
		return manAnswer2;
	}

	public void setManAnswer2(Radio[] manAnswer2) {
		this.manAnswer2 = manAnswer2;
	}

	public Radio[] getManAnswer3() {
		return manAnswer3;
	}

	public void setManAnswer3(Radio[] manAnswer3) {
		this.manAnswer3 = manAnswer3;
	}

	public Radio[] getManAnswer4() {
		return manAnswer4;
	}

	public void setManAnswer4(Radio[] manAnswer4) {
		this.manAnswer4 = manAnswer4;
	}

	public Radio[] getManAnswer5() {
		return manAnswer5;
	}

	public void setManAnswer5(Radio[] manAnswer5) {
		this.manAnswer5 = manAnswer5;
	}

	public Radio[] getManAnswer6() {
		return manAnswer6;
	}

	public void setManAnswer6(Radio[] manAnswer6) {
		this.manAnswer6 = manAnswer6;
	}

	public Radio[] getManAnswer7() {
		return manAnswer7;
	}

	public void setManAnswer7(Radio[] manAnswer7) {
		this.manAnswer7 = manAnswer7;
	}

	public Label[] getLbSTDQuestions() {
		return lbSTDQuestions;
	}

	public void setLbSTDQuestions(Label[] lbSTDQuestions) {
		this.lbSTDQuestions = lbSTDQuestions;
	}

	public Radio[] getStdAnswer0() {
		return stdAnswer0;
	}

	public void setStdAnswer0(Radio[] stdAnswer0) {
		this.stdAnswer0 = stdAnswer0;
	}

	public Radio[] getStdAnswer1() {
		return stdAnswer1;
	}

	public void setStdAnswer1(Radio[] stdAnswer1) {
		this.stdAnswer1 = stdAnswer1;
	}

	public Radio[] getStdAnswer2() {
		return stdAnswer2;
	}

	public void setStdAnswer2(Radio[] stdAnswer2) {
		this.stdAnswer2 = stdAnswer2;
	}

	public Radio[] getStdAnswer3() {
		return stdAnswer3;
	}

	public void setStdAnswer3(Radio[] stdAnswer3) {
		this.stdAnswer3 = stdAnswer3;
	}

	public Radio[] getStdAnswer4() {
		return stdAnswer4;
	}

	public void setStdAnswer4(Radio[] stdAnswer4) {
		this.stdAnswer4 = stdAnswer4;
	}

	public Radio[] getStdAnswer5() {
		return stdAnswer5;
	}

	public void setStdAnswer5(Radio[] stdAnswer5) {
		this.stdAnswer5 = stdAnswer5;
	}

	public Radio[] getStdAnswer6() {
		return stdAnswer6;
	}

	public void setStdAnswer6(Radio[] stdAnswer6) {
		this.stdAnswer6 = stdAnswer6;
	}

	public Radio[] getStdAnswer7() {
		return stdAnswer7;
	}

	public void setStdAnswer7(Radio[] stdAnswer7) {
		this.stdAnswer7 = stdAnswer7;
	}

	public Label[] getLbOTHQuestions() {
		return lbOTHQuestions;
	}

	public void setLbOTHQuestions(Label[] lbOTHQuestions) {
		this.lbOTHQuestions = lbOTHQuestions;
	}

	public Radio[] getOthAnswer0() {
		return othAnswer0;
	}

	public void setOthAnswer0(Radio[] othAnswer0) {
		this.othAnswer0 = othAnswer0;
	}

	public Radio[] getOthAnswer1() {
		return othAnswer1;
	}

	public void setOthAnswer1(Radio[] othAnswer1) {
		this.othAnswer1 = othAnswer1;
	}

	public Radio[] getOthAnswer2() {
		return othAnswer2;
	}

	public void setOthAnswer2(Radio[] othAnswer2) {
		this.othAnswer2 = othAnswer2;
	}

	public Radio[] getOthAnswer3() {
		return othAnswer3;
	}

	public void setOthAnswer3(Radio[] othAnswer3) {
		this.othAnswer3 = othAnswer3;
	}

	public Radio[] getOthAnswer4() {
		return othAnswer4;
	}

	public void setOthAnswer4(Radio[] othAnswer4) {
		this.othAnswer4 = othAnswer4;
	}

	public Radio[] getOthAnswer5() {
		return othAnswer5;
	}

	public void setOthAnswer5(Radio[] othAnswer5) {
		this.othAnswer5 = othAnswer5;
	}

	public Radio[] getOthAnswer6() {
		return othAnswer6;
	}

	public void setOthAnswer6(Radio[] othAnswer6) {
		this.othAnswer6 = othAnswer6;
	}

	public Radio[] getOthAnswer7() {
		return othAnswer7;
	}

	public void setOthAnswer7(Radio[] othAnswer7) {
		this.othAnswer7 = othAnswer7;
	}

	public Label[] getLbTVROQuestions() {
		return lbTVROQuestions;
	}

	public void setLbTVROQuestions(Label[] lbTVROQuestions) {
		this.lbTVROQuestions = lbTVROQuestions;
	}

	public Radio[] getTvroAnswer0() {
		return tvroAnswer0;
	}

	public void setTvroAnswer0(Radio[] tvroAnswer0) {
		this.tvroAnswer0 = tvroAnswer0;
	}

	public Radio[] getTvroAnswer1() {
		return tvroAnswer1;
	}

	public void setTvroAnswer1(Radio[] tvroAnswer1) {
		this.tvroAnswer1 = tvroAnswer1;
	}

	public Radio[] getTvroAnswer2() {
		return tvroAnswer2;
	}

	public void setTvroAnswer2(Radio[] tvroAnswer2) {
		this.tvroAnswer2 = tvroAnswer2;
	}

	public Radio[] getTvroAnswer3() {
		return tvroAnswer3;
	}

	public void setTvroAnswer3(Radio[] tvroAnswer3) {
		this.tvroAnswer3 = tvroAnswer3;
	}

	public Radio[] getTvroAnswer4() {
		return tvroAnswer4;
	}

	public void setTvroAnswer4(Radio[] tvroAnswer4) {
		this.tvroAnswer4 = tvroAnswer4;
	}

	public Radio[] getTvroAnswer5() {
		return tvroAnswer5;
	}

	public void setTvroAnswer5(Radio[] tvroAnswer5) {
		this.tvroAnswer5 = tvroAnswer5;
	}

	public Radio[] getTvroAnswer6() {
		return tvroAnswer6;
	}

	public void setTvroAnswer6(Radio[] tvroAnswer6) {
		this.tvroAnswer6 = tvroAnswer6;
	}

	public Radio[] getTvroAnswer7() {
		return tvroAnswer7;
	}

	public void setTvroAnswer7(Radio[] tvroAnswer7) {
		this.tvroAnswer7 = tvroAnswer7;
	}

	public ResetButton getRsButton() {
		return rsButton;
	}

	public void setRsButton(ResetButton rsButton) {
		this.rsButton = rsButton;
	}

	
	
}
