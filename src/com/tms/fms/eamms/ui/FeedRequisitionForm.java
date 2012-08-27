package com.tms.fms.eamms.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.UuidGenerator;

import com.tms.fms.eamms.model.EammsFeedsDetails;
import com.tms.fms.eamms.model.EammsFeedsModule;
import com.tms.fms.eamms.model.StatusTrail;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.EngineeringRequest;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.ui.ServiceDetailsForm;
import com.tms.fms.util.WidgetUtil;

public class FeedRequisitionForm extends Form 
{
	public static final String READ_ONLY = "readOnly";
	public static final String ENGINEERING_COORDINATOR_VIEW = "engCoordinatorView";
	public static final String UNIT_HEAD_ENG_VIEW = "unitHeadEngView";
	public static final String UNIT_HEAD_NET_VEIW = "unitHeadNetView";
	public static final String NETWORK_ENGINEER_VIEW = "networkEngView";
	//public static final String
	
	private String requestTitle;
	private String requestId;
	private String feedsDetailsId;
	private ServiceDetailsForm serviceForms;
	private FeedsAssignmentDetail assignmentDetailForm;
	private StatusTrailForm statusTrail;
	
	private Label ecAssigned;
	private Label requestedBy;
	private Label deptUnit;
	private Label staffNo;
	private Label requestDate;
	private Label program;
	
	private Label locationLab;
	private Label telcoLab;
	private Label obLinkLab;
	private TextField locationTx;
	private SelectBox telcoSb;
	private SelectBox obLinkSb;
	
	private Button submit;
	private Button cancel;
	private Button approve;
	private Button reject;
	private Button verifyRequest;
	
	private String mode;
	
	public void init()
	{
		initForm();
	}
	
	public void onRequest(Event evt)
	{
		initForm();
		loadForm();
		assignmentDetailForm.onRequest(evt);
	}
	
	public void initForm()
	{
		removeChildren();
		Application app = Application.getInstance();
		EngineeringModule module= (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
		EngineeringRequest request = module.getRequestWithService(requestId);
		
		serviceForms = new ServiceDetailsForm("serviceForms");
		serviceForms.setTemplate("fms/eamms/tvroServiceTemplateForEamms");
		if(request != null) 
		{
			requestTitle = request.getTitle();
			String reqFr = "";
			String reqTo = "";
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				reqFr = request.getRequiredFrom() != null ? sdf.format(request.getRequiredFrom()) : "";
				reqTo = request.getRequiredTo() != null ? sdf.format(request.getRequiredTo()) : "";
			}
			catch(Exception e){}
			
			Collection services = request.getServices();
			for(Iterator itr = services.iterator();itr.hasNext();)
			{
				Service service=(Service)itr.next();
				if(service != null && service.getServiceId().equals(ServiceDetailsForm.SERVICE_TVRO))
				{
					service.setProperty("requestTitle", requestTitle);
					service.setProperty("requestReqDateRange", reqFr + " - " + reqTo);
					serviceForms.setService(service);
					serviceForms.setServiceId(service.getServiceId());
					serviceForms.setViewMode(true);
					serviceForms.setRemoveLink(true);
					serviceForms.setRequestId(requestId);				
					serviceForms.setHideTitle(true);
					
					break;
				}
			}
		}
		addChild(serviceForms);
		
		Panel titleA = new Panel("titleA");
		titleA.setColumns(2);
		titleA.addChild(new Label("requesterDetail", bold("A. " + app.getMessage("eamms.feed.list.msg.requesterDetails").toUpperCase())));
		addChild(titleA);
		
		Panel requesterDetailPane = new Panel("requesterDetailPane");
		Panel requesterDetailPane1 = new Panel("requesterDetailPane1");
		Panel requesterDetailPane2 = new Panel("requesterDetailPane2");
		requesterDetailPane.setColumns(2);
		requesterDetailPane1.setColumns(2);
		requesterDetailPane2.setColumns(2);
		requesterDetailPane.setWidth("80%");
		requesterDetailPane1.setWidth("100%");
		requesterDetailPane2.setWidth("100%");
		
		ecAssigned = new Label("ecAssigned", "");
		requestedBy = new Label("requestedBy", "");
		deptUnit = new Label("deptUnit", "");
		staffNo = new Label("staffNo", "");
		requestDate = new Label("requestDate", "");
		program = new Label("program", "");
		
		locationTx = new TextField("locationTx");
		telcoSb = new SelectBox("telcoSb");
		obLinkSb = new SelectBox("obLinkSb");
		
		locationLab = new Label("locationLab", "");
		telcoLab = new Label("telcoLab", "");
		obLinkLab = new Label("obLinkLab", "");
		
		requesterDetailPane1.addChild(new Label("ecAssignedLab", bold(app.getMessage("eamms.feed.list.msg.ecAssigned"))));
		requesterDetailPane1.addChild(ecAssigned);
		requesterDetailPane1.addChild(new Label("requestedByLab", bold(app.getMessage("eamms.feed.list.msg.requestedBy"))));
		requesterDetailPane1.addChild(requestedBy);
		requesterDetailPane1.addChild(new Label("deptUnitLab", bold(app.getMessage("eamms.feed.list.msg.dept"))));
		requesterDetailPane1.addChild(deptUnit);
		requesterDetailPane1.addChild(new Label("locationLab", bold(app.getMessage("eamms.feed.list.msg.location"))));
		Panel locationPane = new Panel("locationPane");
		locationPane.setColumns(2);
		locationPane.addChild(locationTx);
		locationPane.addChild(locationLab);
		requesterDetailPane1.addChild(locationPane);
		requesterDetailPane1.addChild(new Label("telcoLab",bold(app.getMessage("eamms.feed.list.msg.telco"))));
		Panel telcoPane = new Panel("telcoPane");
		telcoPane.setColumns(2);
		telcoPane.addChild(telcoSb);
		telcoPane.addChild(telcoLab);
		requesterDetailPane1.addChild(telcoPane);
		
		requesterDetailPane2.addChild(new Label("dummyLab2", "&nbsp;"));
		requesterDetailPane2.addChild(new Label("dummyLab3", "&nbsp;"));
		requesterDetailPane2.addChild(new Label("staffNoLab",bold(app.getMessage("eamms.feed.list.msg.staffNo"))));
		requesterDetailPane2.addChild(staffNo);
		requesterDetailPane2.addChild(new Label("requestDateLab",bold(app.getMessage("eamms.feed.list.msg.requestedDate"))));
		requesterDetailPane2.addChild(requestDate);
		requesterDetailPane2.addChild(new Label("programLab",bold(app.getMessage("eamms.feed.list.msg.program"))));
		requesterDetailPane2.addChild(program);
		requesterDetailPane2.addChild(new Label("obLinkLab",bold(app.getMessage("eamms.feed.list.msg.obLink"))));
		Panel oblinkPane = new Panel("oblinkPane");
		oblinkPane.setColumns(2);
		oblinkPane.addChild(obLinkSb);
		oblinkPane.addChild(obLinkLab);
		requesterDetailPane2.addChild(oblinkPane);
		
		requesterDetailPane.addChild(requesterDetailPane1);
		requesterDetailPane.addChild(requesterDetailPane2);
		addChild(requesterDetailPane);
		
		assignmentDetailForm = new FeedsAssignmentDetail("assignmentDetailForm"); 
		assignmentDetailForm.setRequestId(requestId);
		addChild(assignmentDetailForm);
		
		submit = new Button("submit", Application.getInstance().getMessage("eamms.feed.list.button.submit"));
		approve = new Button("approve", Application.getInstance().getMessage("eamms.feed.list.button.approve"));
		reject = new Button("reject", Application.getInstance().getMessage("eamms.feed.list.button.reject"));
		verifyRequest = new Button("verifyRequest", Application.getInstance().getMessage("eamms.feed.list.button.verifyReq"));
		cancel = new Button(Form.CANCEL_FORM_ACTION, Application.getInstance().getMessage("eamms.feed.list.button.cancel"));
		
		Panel buttonPane = new Panel("buttonPane");
		buttonPane.addChild(submit);
		buttonPane.addChild(approve);
		buttonPane.addChild(reject);
		buttonPane.addChild(verifyRequest);
		buttonPane.addChild(cancel);
		addChild(buttonPane);
		
		addChild(new Label("dummyLab1", "<br><br><br><br>"));
		statusTrail = new StatusTrailForm("statusTrail");		
		addChild(statusTrail);
		
		initSelectbox();
	}
	
	public void loadForm()
	{
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		
		EammsFeedsDetails fdObj = em.getFeedsRequestDetails(requestId);
		if(fdObj != null)
		{
			feedsDetailsId = fdObj.getId();
			statusTrail.setFeedsDetailsId(feedsDetailsId);
			statusTrail.init();
			
			ecAssigned.setText((String)fdObj.getProperty("assignedEcStr"));
			requestedBy.setText(fdObj.getSubmittedBy());
			deptUnit.setText((String)fdObj.getProperty("department"));
			staffNo.setText((String)fdObj.getProperty("staffId"));
			program.setText((String)fdObj.getProperty("programName"));
			
			requestDate.setText("");
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			Date requestedDate = fdObj.getRequestedDate();
			if(requestedDate != null)
			{
				try
				{
					requestDate.setText(sdf.format(requestedDate));
				}
				catch(Exception e){}
			}
			
			String nwStatus =  fdObj.getNetworkStatus();
			if(FeedRequisitionForm.ENGINEERING_COORDINATOR_VIEW.equals(mode))
			{
				if("01".equals(nwStatus) || "08".equals(nwStatus)) // New / Rejected
				{
					locationLab.setHidden(true);
					telcoLab.setHidden(true);
					obLinkLab.setHidden(true);
					
					approve.setHidden(true);
					reject.setHidden(true);
					verifyRequest.setHidden(true);
					
					if("01".equals(nwStatus))
					{
						statusTrail.setHidden(true);
					}
					
					locationTx.setValue(fdObj.getLocation());
					telcoSb.setSelectedOption((String)fdObj.getProperty("telcoId"));
					obLinkSb.setSelectedOption((String)fdObj.getProperty("oblinkId"));
				}
				else
				{
					mode = FeedRequisitionForm.READ_ONLY;
				}
			}
			else if(FeedRequisitionForm.UNIT_HEAD_ENG_VIEW.equals(mode))
			{
				if("02".equals(nwStatus)) // Submitted
				{
					locationTx.setHidden(true);
					telcoLab.setHidden(true);
					obLinkLab.setHidden(true);
					
					assignmentDetailForm.setViewMode(true); 
					
					submit.setHidden(true);
					approve.setHidden(true);
					reject.setHidden(true);
					
					locationLab.setText(fdObj.getLocation());
					telcoSb.setSelectedOption((String)fdObj.getProperty("telcoId"));
					obLinkSb.setSelectedOption((String)fdObj.getProperty("oblinkId"));
				}
				else
				{
					mode = FeedRequisitionForm.READ_ONLY;
				}
			}
			else if(FeedRequisitionForm.UNIT_HEAD_NET_VEIW.equals(mode))
			{
				if("03".equals(nwStatus)) // Unit head Verified
				{
					locationTx.setHidden(true);
					telcoLab.setHidden(true);
					obLinkLab.setHidden(true);
					
					assignmentDetailForm.setShowsNetworkColumns(true);
					
					submit.setHidden(true);
					verifyRequest.setHidden(true);
					
					locationLab.setText(fdObj.getLocation());
					telcoSb.setSelectedOption((String)fdObj.getProperty("telcoId"));
					obLinkSb.setSelectedOption((String)fdObj.getProperty("oblinkId"));
				}
				else
				{
					mode = FeedRequisitionForm.READ_ONLY;
				}
			}
			else if(FeedRequisitionForm.NETWORK_ENGINEER_VIEW.equals(mode))
			{
				if("04".equals(nwStatus) || "05".equals(nwStatus)) // Network Approved / Network Processed
				{
					locationTx.setHidden(true);
					telcoSb.setHidden(true);
					obLinkSb.setHidden(true);
					
					assignmentDetailForm.setViewMode(true); 
					assignmentDetailForm.setNetworkView(true);
					assignmentDetailForm.setShowsNetworkColumns(true);
					
					approve.setHidden(true);
					reject.setHidden(true);
					verifyRequest.setHidden(true);
					
					locationLab.setText(fdObj.getLocation());
					telcoLab.setText(fdObj.getTelco());
					obLinkLab.setText(fdObj.getOblink());
				}
				else
				{
					mode = FeedRequisitionForm.READ_ONLY;
				}
			}
			else
			{
				mode = FeedRequisitionForm.READ_ONLY;
			}
			
			if(FeedRequisitionForm.READ_ONLY.equals(mode))
			{
				locationTx.setHidden(true);
				telcoSb.setHidden(true);
				obLinkSb.setHidden(true);
				
				assignmentDetailForm.setRemoveLink(true);
				assignmentDetailForm.setViewMode(true); 
				assignmentDetailForm.setShowsNetworkColumns(true);
				
				submit.setHidden(true);
				approve.setHidden(true);
				reject.setHidden(true);
				verifyRequest.setHidden(true);
				
				locationLab.setText(fdObj.getLocation());
				telcoLab.setText(fdObj.getTelco());
				obLinkLab.setText(fdObj.getOblink());
			}			
		}
	}
	
	public Forward onValidate(Event evt)
	{
		Forward fwd = super.onValidate(evt);
		
		String telco = WidgetUtil.getSbValue(telcoSb);
		String obLink = WidgetUtil.getSbValue(obLinkSb);
		String location = (String) locationTx.getValue();
		
		Map emailArgsMap = new HashMap();
		emailArgsMap.put("requestId", requestId);
		emailArgsMap.put("requestTitle", requestTitle); 
		
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		String buttonClicked = findButtonClicked(evt);
		if(submit.getAbsoluteName().equals(buttonClicked))
		{
			DefaultDataObject obj = new DefaultDataObject();
			obj.setId(feedsDetailsId);
			obj.setProperty("oblink", "-1".equals(obLink) ? "" : obLink);
			obj.setProperty("telco", "-1".equals(telco) ? "" : telco);
			obj.setProperty("location", location);
			
			if(FeedRequisitionForm.ENGINEERING_COORDINATOR_VIEW.equals(mode))
			{
				obj.setProperty("submittedBy", Application.getInstance().getCurrentUser().getId());
				obj.setProperty("mode", FeedRequisitionForm.ENGINEERING_COORDINATOR_VIEW);
				obj.setProperty("networkStatus", "02"); // Submitted
			}
			else if (FeedRequisitionForm.NETWORK_ENGINEER_VIEW.equals(mode))
			{
				obj.setProperty("mode", FeedRequisitionForm.NETWORK_ENGINEER_VIEW);
				if(em.isAllAssignmentsHvStatus(feedsDetailsId))
				{
					obj.setProperty("networkStatus", "06"); // Closed
				}
				else
				{
					obj.setProperty("networkStatus", "05"); // Network Processed
				}
			}
			
			em.updateFeedsRequestDetails(obj);
			insertStatusTrail(obj);
			
			emailArgsMap.put("status", obj.getProperty("networkStatus"));
			em.feeds_sendMail(emailArgsMap);
			
			return new Forward("submitted");
		}
		else if(verifyRequest.getAbsoluteName().equals(buttonClicked))
		{
			DefaultDataObject obj = new DefaultDataObject();
			obj.setId(feedsDetailsId);
			obj.setProperty("oblink", "-1".equals(obLink) ? "" : obLink);
			obj.setProperty("telco", "-1".equals(telco) ? "" : telco);
			obj.setProperty("mode", FeedRequisitionForm.UNIT_HEAD_ENG_VIEW);
			obj.setProperty("networkStatus", "03"); // Unit Head Verified
			
			em.updateFeedsRequestDetails(obj);
			insertStatusTrail(obj);
			
			emailArgsMap.put("status", obj.getProperty("networkStatus"));
			em.feeds_sendMail(emailArgsMap);
			
			return new Forward("verified");
		}
		else if(approve.getAbsoluteName().equals(buttonClicked))
		{
			DefaultDataObject obj = new DefaultDataObject();
			obj.setId(feedsDetailsId);
			obj.setProperty("oblink", "-1".equals(obLink) ? "" : obLink);
			obj.setProperty("telco", "-1".equals(telco) ? "" : telco);
			obj.setProperty("mode", FeedRequisitionForm.UNIT_HEAD_NET_VEIW);
			obj.setProperty("networkStatus", "04"); // Network Approved
			
			em.updateFeedsRequestDetails(obj);
			insertStatusTrail(obj);
			
			emailArgsMap.put("status", obj.getProperty("networkStatus"));
			em.feeds_sendMail(emailArgsMap);
			
			return new Forward("approved");
		}
		else if(reject.getAbsoluteName().equals(buttonClicked))
		{
			DefaultDataObject obj = new DefaultDataObject();
			obj.setId(feedsDetailsId);
			obj.setProperty("mode", FeedRequisitionForm.UNIT_HEAD_NET_VEIW);
			obj.setProperty("networkStatus", "08"); // Rejected
			
			em.updateFeedsRequestDetails(obj);
			insertStatusTrail(obj);
			
			emailArgsMap.put("status", obj.getProperty("networkStatus"));
			em.feeds_sendMail(emailArgsMap);
			
			return new Forward("rejected");
		}
		return fwd;
	}
	
	private void initSelectbox()
	{
		Application app = Application.getInstance();
		EammsFeedsModule em = (EammsFeedsModule) app.getModule(EammsFeedsModule.class);
		
		telcoSb.addOption("-1", app.getMessage("eamms.feed.list.opt.plsSelect"));
		Collection tcol = em.getSetupTable("fms_feed_telco", null);
		if(tcol != null && !tcol.isEmpty())
		{
			for(Iterator itr = tcol.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				telcoSb.addOption(obj.getId(), (String)obj.getProperty("c_name"));
			}
		}
		
		obLinkSb.addOption("-1", app.getMessage("eamms.feed.list.opt.plsSelect"));
		Collection ocol = em.getSetupTable("fms_feed_oblink", null);
		if(ocol != null && !ocol.isEmpty())
		{
			for(Iterator itr = ocol.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				obLinkSb.addOption(obj.getId(), (String)obj.getProperty("c_name"));
			}
		}
	}
	
	private void insertStatusTrail(DefaultDataObject obj)
	{
		StatusTrail stObj = new StatusTrail();
		stObj.setId(UuidGenerator.getInstance().getUuid());
		stObj.setFeedsDetailsId(obj.getId());
		stObj.setStatus((String)obj.getProperty("networkStatus"));
		stObj.setCreatedBy(Application.getInstance().getCurrentUser().getId());
		stObj.setCreatedDate(new Date());
		
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		em.insertStatusTrail(stObj);
	}
	
	public String getDefaultTemplate()
	{
		return super.getDefaultTemplate();
	}

	private String bold(String str)
	{
		str = "<b>" + str + "</b>";
		return str;
	}
	
	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}

	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getRequestId()
	{
		return requestId;
	}
}
