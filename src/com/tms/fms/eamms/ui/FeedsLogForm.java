package com.tms.fms.eamms.ui;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.Button;
import kacang.stdui.ButtonGroup;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.FormField;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.Radio;
import kacang.stdui.SelectBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;

import com.tms.fms.eamms.model.EammsFeedsModule;
import com.tms.fms.eamms.model.FeedsLogObject;
import com.tms.fms.eamms.model.SingleRequestSelectBox;
import com.tms.fms.engineering.ui.SingleProgramSelectBox;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;

public class FeedsLogForm extends Form
{
	public static final String AUTO = "AUTO";
	public static final String PROGRAM_ID_EMPTY = "Empty program ID";
	public static final String REQUEST_ID_EMPTY = "Empty request ID";
	public static final String ADD = "add";
	public static final String EDIT = "edit";

	public static final String FEED_STATUS_TRANSMITTED = "Transmitted";
	public static final String FEED_STATUS_NOT_UTILIZE = "Not Utilized";
	
	private String feedLogId;
	private String mode;
	private boolean runInit;

	private Label labFeedLogId;
	private ButtonGroup adhocGrp;
	private Radio radio_yes;
	private Radio radio_no;
	private DatePopupField dPopup_date;
	private SingleProgramSelectBox popup_program;
	private SingleRequestSelectBox popup_request;
	private SelectBox sbAssignId_reqTime;
	private TextField txLocation;
	private SelectBox sbStation;
	private DatePopupField dPop_timeIn;
	private DatePopupField dPop_timeOut;
	private SelectBox sbTimeInHour;
	private SelectBox sbTimeInMin;
	private SelectBox sbTimeOutHour;
	private SelectBox sbTimeOutMin;
	private TextField txEbNum;
	private SelectBox sbAss_1;
	private SelectBox sbAss_2;
	private SelectBox sbMcr;
	private TextField txNews;
	private TextField txStringer;
	private SelectBox sbTelco;
	private TextField txRemarks;
	private SelectBox sbStatus;
	
	private Label labProgramLab;
	private Label labProgram;
	private Label labRequest;
	
	private Button submit_continue;
	private Button cont;
	private Button submit;
	private Button back;
	private Button cancel;
	
	private Panel mainPane;
	
	private String programId;
	private String programName;
	private String requestId;
	private String requestTitle;
	
	private String backToEdit;
	
	public void init()
	{
		runInit = false;
		backToEdit = "0";
		
		reset();
		initForm();
		initSelectbox();
	}
	
	public void onRequest(Event evt)
	{
		if(runInit)
		{
			init();
		}
		
		if(FeedsLogForm.EDIT.equals(mode))
		{
			submit_continue.setHidden(true);
			loadForm();
			setChildren();
		}
		else if(FeedsLogForm.ADD.equals(mode))
		{
			setChildren();
		}
	}
	
	public void initForm()
	{
		Application app = Application.getInstance();
		
		labFeedLogId = new Label("labFeedLogId", FeedsLogForm.AUTO);
		
		radio_yes = new Radio("radio_yes", Application.getInstance().getMessage("eamms.feed.log.msg.yes"));
		radio_no = new Radio("radio_no", Application.getInstance().getMessage("eamms.feed.log.msg.no"));
		radio_no.setChecked(true);
		adhocGrp = new ButtonGroup("ButtonGroup");
		adhocGrp.addButton(radio_yes);
		adhocGrp.addButton(radio_no);
		
		dPopup_date = new DatePopupField("dPopup_date");
		dPopup_date.setDate(new Date());
		dPopup_date.setFormat("dd-MM-yyyy");
		
		labProgramLab =  new Label("labProgramLab", "");
		popup_program = new SingleProgramSelectBox("popup_program");
		labProgram = new Label("labProgram", "");
		popup_request = new SingleRequestSelectBox("popup_request");
		labRequest = new Label("labRequest", "");
		sbAssignId_reqTime = new SelectBox("sbAssignId_reqTime");
		
		txLocation = new TextField("txLocation");
		txLocation.setMaxlength("255");
		
		sbStation = new SelectBox("sbStation");
		
		dPop_timeIn = new DatePopupField("dPop_timeIn");
		dPop_timeIn.setDate(new Date());
		dPop_timeIn.setOptional(true);
		
		dPop_timeOut = new DatePopupField("dPop_timeOut");
		dPop_timeOut.setDate(new Date());
		dPop_timeOut.setOptional(true);
		
		sbTimeInHour = new SelectBox("sbTimeInHour");
		sbTimeInMin = new SelectBox("sbTimeInMin");
		sbTimeOutHour = new SelectBox("sbTimeOutHour");
		sbTimeOutMin = new SelectBox("sbTimeOutMin");
		
		txEbNum = new TextField("txEbNum");
		txEbNum.setMaxlength("10");
		
		sbAss_1 = new SelectBox("sbAss_1");
		sbAss_2 = new SelectBox("sbAss_2");
		sbMcr = new SelectBox("sbMcr");
		
		txNews = new TextField("txNews");
		txNews.setMaxlength("100");
		
		txStringer = new TextField("txStringer");
		txStringer.setMaxlength("100");
		
		txRemarks = new TextField("txRemarks");
		txRemarks.setMaxlength("255");
		
		sbTelco = new SelectBox("sbTelco");
		sbStatus = new SelectBox("sbStatus");
		
		cont = new Button("cont", app.getMessage("eamms.feed.list.button.continue"));
		back = new Button("back", app.getMessage("eamms.feed.log.button.back"));
		submit_continue = new Button("submit_continue", app.getMessage("eamms.feed.list.button.subNCon"));
		submit = new Button("submit", app.getMessage("eamms.feed.list.button.submit"));
		cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("eamms.feed.list.button.cancel"));
	}
	
	private void setChildren()
	{
		Application app = Application.getInstance();
		removeChildren();
		
		radio_yes.setTemplate(radio_yes.getDefaultTemplate());
		radio_no.setTemplate(radio_no.getDefaultTemplate());
		
		mainPane = new Panel("mainPane");
		mainPane.setColumns(2);
		
		mainPane.addChild(new Label("feedLogIdLab", bold(app.getMessage("eamms.feed.log.msg.feedsLogId"))));
		mainPane.addChild(labFeedLogId);
		
		mainPane.addChild(new Label("adhocGrpLab", bold(app.getMessage("eamms.feed.log.msg.isAdhocFeed")) + "*"));
		mainPane.addChild(adhocGrp);
		
		mainPane.addChild(new Label("dPopup_dateLab", bold(app.getMessage("eamms.feed.log.msg.date")) + "*"));
		mainPane.addChild(dPopup_date);
		
		Panel buttonPane = new Panel("buttonPane");
		buttonPane.setColumns(4);
		if(programId == null || programId.equals("") || "1".equals(backToEdit))
		{
			popup_program.init();
			if("1".equals(backToEdit) && !programName.equals("-"))
			{
				popup_program.addOption(programId, programName);
				popup_program.setSelectedOption(programId);
			}
			labProgramLab.setText( bold(app.getMessage("eamms.feed.log.msg.program")) + "*");
			mainPane.addChild(labProgramLab);
			mainPane.addChild(popup_program);
		}
		else if((programId != null && !programId.equals("")) || FeedsLogForm.PROGRAM_ID_EMPTY.equals(programId) || "0".equals(backToEdit) || "2".equals(backToEdit))
		{
			radio_yes.setTemplate("fms/eamms/disableRadio");
			radio_no.setTemplate("fms/eamms/disableRadio");
			
			labProgramLab.setText( bold(app.getMessage("eamms.feed.log.msg.program")));
			mainPane.addChild(labProgramLab);
			labProgram.setText(programName);
			mainPane.addChild(labProgram);
			
			if(requestId == null || requestId.equals("") || "2".equals(backToEdit))
			{
				mainPane.addChild(new Label("popup_requestLab", bold(app.getMessage("eamms.feed.log.msg.reqsId_reqsTitle")) + (radio_no.isChecked() ? "" : "*")));
				popup_request.setProgramId(FeedsLogForm.PROGRAM_ID_EMPTY.equals(programId) ? null : programId);
				popup_request.init();
				if("2".equals(backToEdit) && !requestTitle.equals("-"))
				{
					popup_request.addOption(requestId, requestTitle);
					popup_request.setSelectedOption(requestId);
				}
				mainPane.addChild(popup_request);
				
				buttonPane.addChild(cont);
				buttonPane.addChild(back);
			}
			else
			{
				mainPane.addChild(new Label("popup_requestLab", bold(app.getMessage("eamms.feed.log.msg.reqsId_reqsTitle"))));
				labRequest.setText(requestTitle);
				mainPane.addChild(labRequest);
			}
		}
			
		if((requestId != null && !requestId.equals("") || FeedsLogForm.REQUEST_ID_EMPTY.equals(requestId)) && !("1".equals(backToEdit) || "2".equals(backToEdit)))
		{
			if(!FeedsLogForm.REQUEST_ID_EMPTY.equals(requestId))
			{
				initAssignSb(requestId);
				mainPane.addChild(new Label("sbAssignId_reqTimeLab", bold(app.getMessage("eamms.feed.log.msg.assignId_reqTime"))));
				mainPane.addChild(sbAssignId_reqTime);
			}
			
			mainPane.addChild(new Label("txLocationLab", bold(app.getMessage("eamms.feed.log.msg.location")) + "*"));
			mainPane.addChild(txLocation);
			
			mainPane.addChild(new Label("sbStationLab", bold(app.getMessage("eamms.feed.log.msg.station")) + "*"));
			mainPane.addChild(sbStation);
			
			mainPane.addChild(new Label("timeInLab", bold(app.getMessage("eamms.feed.log.msg.timeIn")) + "*"));
			Panel timeInPane = new Panel("timeInPane");
			timeInPane.setColumns(3);
			timeInPane.addChild(dPop_timeIn);
			timeInPane.addChild(sbTimeInHour);
			timeInPane.addChild(sbTimeInMin);
			mainPane.addChild(timeInPane);
			
			mainPane.addChild(new Label("timeOutLab", bold(app.getMessage("eamms.feed.log.msg.timeOut")) + "*"));
			Panel timeOutPane = new Panel("timeOutPane");
			timeOutPane.setColumns(3);
			timeOutPane.addChild(dPop_timeOut);
			timeOutPane.addChild(sbTimeOutHour);
			timeOutPane.addChild(sbTimeOutMin);
			mainPane.addChild(timeOutPane);
			
			mainPane.addChild(new Label("txEbNumLab", bold(app.getMessage("eamms.feed.log.msg.ebNum"))));
			mainPane.addChild(txEbNum);
			
			mainPane.addChild(new Label("sbAssLab", bold(app.getMessage("eamms.feed.log.msg.assAv")) + "*"));
			Panel assAvPane = new Panel("assAvPane");
			assAvPane.setColumns(3);
			assAvPane.addChild(sbAss_1);
			assAvPane.addChild(new Label("sbAssLab2", "/"));
			assAvPane.addChild(sbAss_2);
			mainPane.addChild(assAvPane);
			
			mainPane.addChild(new Label("sbMcrLab", bold(app.getMessage("eamms.feed.log.msg.mcr"))));
			mainPane.addChild(sbMcr);
			
			mainPane.addChild(new Label("txNewsLab", bold(app.getMessage("eamms.feed.log.msg.news"))));
			mainPane.addChild(txNews);
			
			mainPane.addChild(new Label("txStringerLab", bold(app.getMessage("eamms.feed.log.msg.stringer"))));
			mainPane.addChild(txStringer);
			
			mainPane.addChild(new Label("sbTelcoLab", bold(app.getMessage("eamms.feed.log.msg.telco")) + "*"));
			mainPane.addChild(sbTelco);
			
			mainPane.addChild(new Label("txRemarksLab", bold(app.getMessage("eamms.feed.log.msg.remarks"))));
			mainPane.addChild(txRemarks);
			
			mainPane.addChild(new Label("sbStatusLab", bold(app.getMessage("eamms.feed.log.msg.status")) + "*"));
			mainPane.addChild(sbStatus);
			
			buttonPane.addChild(submit_continue);
			buttonPane.addChild(submit);
			buttonPane.addChild(back);
			buttonPane.addChild(cancel);
		}
		else
		{
			buttonPane.addChild(cont);
		}
		mainPane.addChild(new Label("dummy1", ""));
		mainPane.addChild(buttonPane);
		
		addChild(mainPane);
	}
	
	public void loadForm()
	{
		if(feedLogId != null && !feedLogId.equals(""))
		{
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			Collection col = em.getFeedsLog(feedLogId);
			if(col != null && !col.isEmpty())
			{
				FeedsLogObject obj = (FeedsLogObject) col.iterator().next();
				if(obj != null)
				{
					radio_yes.setChecked(false);
					radio_no.setChecked(false);
					if(EammsFeedsModule.FEED_LOG_ADHOC.equals(obj.getAdhoc()))
					{
						radio_yes.setChecked(true);
					}
					else
					{
						radio_no.setChecked(true);
					}
					
					programId = obj.getProgram();
					programName = "-";
					if(programId != null && !programId.equals(""))
					{
						programName = SingleProgramSelectBox.getName(programId);
					}
					else
					{
						programId = FeedsLogForm.PROGRAM_ID_EMPTY;
					}
					
					requestId = obj.getRequestId();
					requestTitle = "-";
					if(requestId != null && !requestId.equals(""))
					{
						requestTitle = em.getReqTitle(requestId);
						
						initAssignSb(requestId);
						sbAssignId_reqTime.setSelectedOption(obj.getAssignmentId());
					}
					else
					{
						requestId = FeedsLogForm.REQUEST_ID_EMPTY;
					}
					
					dPopup_date.setDate(obj.getDate());
					dPop_timeIn.setDate(obj.getDateIn());
					dPop_timeOut.setDate(obj.getDateOut());
					
					txEbNum.setValue(obj.getEbNo());
					txLocation.setValue(obj.getLocation());
					txNews.setValue(obj.getNews());
					txRemarks.setValue(obj.getRemarks());
					txStringer.setValue(obj.getStringer());
					
					sbAss_1.setSelectedOption(obj.getAssAV1());
					sbAss_2.setSelectedOption(obj.getAssAV2());
					sbMcr.setSelectedOption(obj.getMcr());
					sbStation.setSelectedOption(obj.getStation());
					sbStatus.setSelectedOption(obj.getStatus());
					sbTelco.setSelectedOption(obj.getTelco());
					
					String inHr = "";
					String inMin = "";
					String timeIn = obj.getTimeIn();
					if(timeIn != null && timeIn.length() == 4)
					{
						inHr = timeIn.substring(0, 2);
						inMin = timeIn.substring(2, 4);
						
						sbTimeInHour.setSelectedOption(inHr);
						sbTimeInMin.setSelectedOption(inMin);
					}
					
					String outHr = "";
					String outMin = "";
					String timeOut = obj.getTimeOut();
					if(timeIn != null && timeIn.length() == 4)
					{
						outHr = timeOut.substring(0, 2);
						outMin = timeOut.substring(2, 4);
						
						sbTimeOutHour.setSelectedOption(outHr);
						sbTimeOutHour.setSelectedOption(outMin);
					}
				}
			}
		}
	}
	
	public Forward onSubmit(Event evt)
	{
		Forward fwd = super.onSubmit(evt);
		Application app = Application.getInstance();
		String errMsg = "<font color='red'><br/>" + app.getMessage("eamms.feed.msg.vne.reqMsg") + "</font>";
		
		String buttonClicked = findButtonClicked(evt);
		if(cont.getAbsoluteName().equals(buttonClicked))
		{
			popup_program.removeChildren();
			if(radio_no.isChecked())
			{
				String prog = (String) popup_program.getSelectedId(); 
				boolean programInvalid = false;
				if((prog == null || prog.equals("")) && (programId == null || programId.equals("")))
				{
					programInvalid = true;
				}
				else if ("1".equals(backToEdit))
				{
					if((prog == null || prog.equals("")))
					{
						programInvalid = true;
					}
				}
				
				if(programInvalid)
				{
					popup_program.init();
					popup_program.addChild(new Label("popup_program_errorLab", errMsg));
					popup_program.setInvalid(true);
					this.setInvalid(true);
				}
				
				popup_request.removeChildren();
				String req = (String) popup_request.getSelectedId(); 
				if((req == null || req.equals("")) && (programId != null && !programId.equals("")))
				{
					if(!"1".equals(backToEdit))
					{
						popup_request.setProgramId(FeedsLogForm.PROGRAM_ID_EMPTY.equals(programId) ? null : programId);
						popup_request.init();
						popup_request.addChild(new Label("popup_request_errorLab", errMsg));
						popup_request.setInvalid(true);
						this.setInvalid(true);
					}
				}
			}
		}
		
		if(submit_continue.getAbsoluteName().equals(buttonClicked) || submit.getAbsoluteName().equals(buttonClicked))
		{
			Date timeIn = dPop_timeIn.getDate();
			dPop_timeIn.removeChildren();
			if(timeIn == null)
			{
				dPop_timeIn.addChild(new Label("dPop_timeIn_errorLab", errMsg));
				dPop_timeIn.setInvalid(true);
				this.setInvalid(true);
			}
			
			Date timeOut = dPop_timeOut.getDate();
			dPop_timeOut.removeChildren();
			if(timeOut == null)
			{
				dPop_timeOut.addChild(new Label("dPop_timeOut_errorLab", errMsg));
				dPop_timeOut.setInvalid(true);
				this.setInvalid(true);
			}
			
			ValidatorNotEmpty vneTx = new ValidatorNotEmpty("validator_tx");
			
			txLocation.removeChildren();
			if(!vneTx.validate(txLocation))
			{
				txLocation.addChild(new Label("txLocation_errorLab", errMsg));
				txLocation.setInvalid(true);
				this.setInvalid(true);
			}
			
			validateSb(sbTimeInHour, errMsg);
			validateSb(sbTimeInMin, errMsg);
			validateSb(sbTimeOutHour, errMsg);
			validateSb(sbTimeOutMin, errMsg);
			validateSb(sbStation, errMsg);
			validateSb(sbAss_1, errMsg);
			validateSb(sbAss_2, errMsg);
			validateSb(sbMcr, errMsg);
			validateSb(sbTelco, errMsg);
			validateSb(sbStatus, errMsg);
		}
		return fwd;
	}
	
	private void validateSb(FormField formField, String errMsg)
	{
		ValidatorSelectBox vneSb = new ValidatorSelectBox("validator_vneSb", "", "-1");
		
		formField.removeChildren();
		if(!vneSb.validate(formField))
		{
			formField.addChild(new Label( formField.getName() + "_errorLab", errMsg));
			formField.setInvalid(true);
			this.setInvalid(true);
		}
	}
	
	public Forward onValidate(Event evt)
	{
		Forward fwd = super.onValidate(evt);
		String buttonClicked = findButtonClicked(evt);
		if(cont.getAbsoluteName().equals(buttonClicked))
		{
			if((programId == null || programId.equals("")) || "1".equals(backToEdit))
			{
				programId = popup_program.getSelectedId();
				programName = (String) popup_program.getName(programId);
				if(programId == null || programId.equals(""))
				{
					programId = FeedsLogForm.PROGRAM_ID_EMPTY;
				}
			}
			else
			{
				requestId = popup_request.getSelectedId();
				requestTitle = requestId != null && !requestId.equals("") ? popup_request.getName(requestId) : "-";
				if(requestId == null || requestId.equals(""))
				{
					requestId = FeedsLogForm.REQUEST_ID_EMPTY;
				}
			}
			
			if ("1".equals(backToEdit))
			{
				backToEdit = "2";
			}
			else if ("2".equals(backToEdit))
			{
				backToEdit = "0";
			}
			
			return new Forward("continue");
		}
		else if(back.getAbsoluteName().equals(buttonClicked))
		{
			backToEdit = "1";
			return new Forward("back");
		}
		else if(submit_continue.getAbsoluteName().equals(buttonClicked) || submit.getAbsoluteName().equals(buttonClicked))
		{
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			FeedsLogObject fObj = constructFeedsLogObj();
			
			if(feedLogId != null && !feedLogId.equals(""))
			{
				fObj.setFeedLogId(feedLogId);
				em.updateFeedsLog(fObj);
			}
			else
			{
				String feedLogId = em.generateNewFeedsLogId();
				fObj.setFeedLogId(feedLogId);
				em.insertFeedsLog(fObj);
			}
			
			if(submit_continue.getAbsoluteName().equals(buttonClicked))
			{
				return new Forward("submit_continue");
			}
			else 
			{
				reset();
				init();
				return new Forward("submitted");
			}
		}
		return fwd;
	}
	
	private FeedsLogObject constructFeedsLogObj()
	{
		FeedsLogObject obj = new FeedsLogObject();
		String inHr = WidgetUtil.getSbValue(sbTimeInHour);
		String inMin = WidgetUtil.getSbValue(sbTimeInMin);
		String outHr = WidgetUtil.getSbValue(sbTimeOutHour);
		String outMin = WidgetUtil.getSbValue(sbTimeOutHour);
		
		obj.setAdhoc((radio_yes.isChecked() ? EammsFeedsModule.FEED_LOG_ADHOC : EammsFeedsModule.FEED_LOG_NON_ADHOC));
		obj.setAssAV1(WidgetUtil.getSbValue(sbAss_1));
		obj.setAssAV2(WidgetUtil.getSbValue(sbAss_2));
		obj.setAssignmentId(WidgetUtil.getSbValue(sbAssignId_reqTime));
		obj.setDate(dPopup_date.getDate());
		obj.setDateIn(dPop_timeIn.getDate());
		obj.setDateOut(dPop_timeOut.getDate());
		obj.setEbNo((String)txEbNum.getValue());
		obj.setFeedLogId(feedLogId);
		obj.setLocation((String)txLocation.getValue());
		obj.setMcr(WidgetUtil.getSbValue(sbMcr));
		obj.setNews((String)txNews.getValue());
		obj.setProgram(FeedsLogForm.PROGRAM_ID_EMPTY.equals(programId) ? null : programId);
		obj.setRemarks((String)txRemarks.getValue());
		obj.setRequestId(FeedsLogForm.REQUEST_ID_EMPTY.equals(requestId) ? null : requestId);
		obj.setStation(WidgetUtil.getSbValue(sbStation));
		obj.setStatus(WidgetUtil.getSbValue(sbStatus));
		obj.setStringer((String)txStringer.getValue());
		obj.setTelco(WidgetUtil.getSbValue(sbTelco));
		obj.setTimeIn(inHr + inMin);
		obj.setTimeOut(outHr + outMin);
		obj.setCreatedBy(Application.getInstance().getCurrentUser().getId());
		obj.setCreatedDate(new Date());
		obj.setModifiedBy(obj.getCreatedBy());
		obj.setModifiedDate(obj.getCreatedDate());
		
		return obj;
	}

	private void initSelectbox()
	{
		Application app = Application.getInstance();
		EammsFeedsModule em = (EammsFeedsModule) app.getModule(EammsFeedsModule.class);
		
		for(int i = 0; i < 24; i++)
		{
			String hr = "";
			if(String.valueOf(i).length() == 1)
			{
				hr = "0" + i;
			}
			else
			{
				hr = "" + i;
			}
			sbTimeInHour.addOption(hr, hr);
			sbTimeOutHour.addOption(hr, hr);
		}
		
		for(int i = 0; i < 60; i++)
		{
			String min = "";
			if(String.valueOf(i).length() == 1)
			{
				min = "0" + i;
			}
			else
			{
				min = "" + i;
			}
			sbTimeInMin.addOption(min, min);
			sbTimeOutMin.addOption(min, min);
		}
		
		Collection scol = em.getSetupTable("fms_feed_station", null);
		if(scol != null && !scol.isEmpty())
		{
			for(Iterator itr = scol.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				sbStation.addOption(obj.getId(), (String)obj.getProperty("c_name"));
			}
		}
		
		for(int i = 1; i <= 5; i++)
		{
			sbAss_1.addOption(String.valueOf(i), String.valueOf(i));
			sbAss_2.addOption(String.valueOf(i), String.valueOf(i));
		}
		
		String mcr = "'" + app.getProperty("MCR") + "'";
		String intern = "'" + app.getProperty("Intern") + "'";
		String grpIds = mcr + ", " + intern;
		Collection gCol = em.getUserBelongToGroup(grpIds);
		if(gCol != null && !gCol.isEmpty())
		{
			for(Iterator itr = gCol.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				sbMcr.addOption(obj.getId(), (String)obj.getProperty("fullname"));
			}
		}
		
		sbTelco.addOption("-1", app.getMessage("eamms.feed.list.opt.plsSelect"));
		Collection tcol = em.getSetupTable("fms_feed_telco", null);
		if(tcol != null && !tcol.isEmpty())
		{
			for(Iterator itr = tcol.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				sbTelco.addOption(obj.getId(), (String)obj.getProperty("c_name"));
			}
		}
		
		sbStatus.addOption(FeedsLogForm.FEED_STATUS_TRANSMITTED, FeedsLogForm.FEED_STATUS_TRANSMITTED);
		sbStatus.addOption(FeedsLogForm.FEED_STATUS_NOT_UTILIZE, FeedsLogForm.FEED_STATUS_NOT_UTILIZE);
	}
	
	private void initAssignSb(String requestId)
	{
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		Collection aCol = em.getFmsAssignments(requestId);
		
		sbAssignId_reqTime.removeAllOptions();
		sbAssignId_reqTime.addOption("-1", Application.getInstance().getMessage("eamms.feed.list.opt.plsSelect"));
		if(aCol != null && !aCol.isEmpty())
		{
			for(Iterator itr = aCol.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				String assignmentId = obj.getId();
				String code = (String) obj.getProperty("code");
				String fromTime = (String) obj.getProperty("fromTime");
				String toTime = (String) obj.getProperty("toTime");
				
				String label = code + " | " + fromTime + " - " + toTime;
				sbAssignId_reqTime.addOption(assignmentId, label);
			}
		}
	}
	
	private void reset()
	{
		programId = null;
		requestId = null;
		programName = "";
		requestTitle = "";
	}
	
	private String bold(String str)
	{
		str = "<b>" + str + "</b>";
		return str;
	}
	
	public String getDefaultTemplate()
	{
		return super.getDefaultTemplate(); 
	}
	
	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getFeedLogId()
	{
		return feedLogId;
	}

	public void setFeedLogId(String feedLogId)
	{
		this.feedLogId = feedLogId;
	}

	public String getProgramId()
	{
		return programId;
	}

	public void setProgramId(String programId)
	{
		this.programId = programId;
	}

	public String getRequestId()
	{
		return requestId;
	}

	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}

	public boolean isRunInit()
	{
		return runInit;
	}

	public void setRunInit(boolean runInit)
	{
		this.runInit = runInit;
	}
}
