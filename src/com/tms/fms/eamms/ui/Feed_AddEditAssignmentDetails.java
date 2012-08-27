package com.tms.fms.eamms.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.storage.StorageFile;
import kacang.services.storage.StorageService;
import kacang.stdui.Button;
import kacang.stdui.DatePopupField;
import kacang.stdui.FileUpload;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.Panel;
import kacang.stdui.SelectBox;
import kacang.stdui.TextBox;
import kacang.stdui.TextField;
import kacang.stdui.validator.ValidatorNotEmpty;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;
import kacang.util.UuidGenerator;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.eamms.model.EammsAssignment;
import com.tms.fms.eamms.model.EammsFeedsModule;
import com.tms.fms.util.WidgetUtil;
import com.tms.fms.validator.ValidatorSelectBox;

public class Feed_AddEditAssignmentDetails extends Form
{
	public static final String PATH = "feedsAssignment/attachment/";

	public static final String MODE_ADD = "ADD";
	public static final String MODE_EDIT = "EDIT";
	public static final String MODE_NETWORK = "NETWORK";
	
	private String mode;
	private String assignId;
	private String feedsDetailsId;
	private String attchPath;
	
	private Label assignmentId;
	private SelectBox feedTitleSb;
	private DatePopupField requiredDate;
	private DatePopupField requiredDateFr;
	private DatePopupField requiredDateTo;
	private SelectBox frhourSb;
	private SelectBox frMinSb;
	private SelectBox toHourSb;
	private SelectBox toMinSb;
	private SelectBox timeZoneSb;
	private SelectBox totalReqTimeSb;
	private TextField reqTimeTx;
	private TextField remarks;
	
	//network view
	private Label feedTitle_nw;
	private Label requiredDate_nw;
	private Label requiredTime_nw;
	private Label totalTimeReq_nw;
	private Label remarks_nw;
	private SelectBox bookingStatusSb;
	private TextBox networkRemarks;
	private FileUpload attchment;
	private Label attchDisplay;
	private SelectBox statusSb;
	
	private Button submit_Continue;
	private Button submit_finish;
	private Button cancel;
	private Button update;
	
	private String blockBooking;
	
	public void init()
	{
		intiForm();
	}
	
	public void onRequest(Event evt)
	{
		intiForm();
		if(mode != null && (mode.equals(MODE_EDIT) || mode.equals(MODE_NETWORK)))
		{
			loadForm();
		}
	}
	
	public void intiForm()
	{
		Application app = Application.getInstance();
		
		removeChildren();
		Panel pane = new Panel("pane");
		pane.setWidth("90%");
		pane.setColumns(2);
		
		if(mode != null && (mode.equals(MODE_EDIT) || mode.equals(MODE_NETWORK)))
		{
			assignmentId = new Label("assignmentId", ""); 
			
			pane.addChild(new Label("assignmentIdLab", bold(app.getMessage("eamms.feed.list.msg.assignId") + "*")));
			pane.addChild(assignmentId);
		}
		
		if(mode != null && (mode.equals(MODE_ADD) || mode.equals(MODE_EDIT)))
		{
			feedTitleSb = new SelectBox("feedTitleSb");
			//feedTitleSb.setOnChange("changeDateInput(this)");
			
			requiredDate = new DatePopupField("requiredDate");
			requiredDate.setDate(new Date());
			requiredDate.setOptional(true);
			
			requiredDateFr = new DatePopupField("requiredDateFr");
			requiredDateFr.setDate(new Date());
			requiredDateFr.setOptional(true);
			
			requiredDateTo = new DatePopupField("requiredDateTo");
			requiredDateTo.setDate(new Date());
			requiredDateTo.setOptional(true);
			
			frhourSb = new SelectBox("frhourSb");
			frMinSb = new SelectBox("frMinSb");
			toHourSb = new SelectBox("toHourSb");
			toMinSb = new SelectBox("toMinSb");
			timeZoneSb = new SelectBox("timeZoneSb");
			reqTimeTx = new TextField("reqTimeTx");
			totalReqTimeSb = new SelectBox("totalReqTimeSb");
			remarks = new TextField("remarks");
			
			feedTitleSb.addChild(new ValidatorSelectBox("validator_bookingStatusSb", app.getMessage("eamms.feed.msg.vne.feedTitle"), "-1"));
			reqTimeTx.addChild(new ValidatorNotEmpty("validator_reqTimeTx", app.getMessage("eamms.feed.msg.vne.totalTimeReq")));
			remarks.addChild(new ValidatorNotEmpty("Validator_remarks", app.getMessage("eamms.feed.msg.vne.remarks")));
			
			pane.addChild(new Label("feedTitleLab", bold(app.getMessage("eamms.feed.list.msg.tvroTitle") + "*")));
			pane.addChild(feedTitleSb);
			
			pane.addChild(new Label("requiredDateFrLab", bold(app.getMessage("eamms.feed.list.msg.requiredDateFr") + "*")));
			pane.addChild(requiredDateFr);
			
			pane.addChild(new Label("requiredDateToLab", bold(app.getMessage("eamms.feed.list.msg.requiredDateTo") + "*")));
			pane.addChild(requiredDateTo);
			
			pane.addChild(new Label("requiredDateLab", bold(app.getMessage("eamms.feed.list.msg.requiredDate") + "*")));
			pane.addChild(requiredDate);
			
			pane.addChild(new Label("requiredTimeLab", bold(app.getMessage("eamms.feed.list.msg.requiredTime") + "*")));
			Panel requiredTimePane = new Panel("requiredTimePane");
			requiredTimePane.addChild(new Label("fromLab", app.getMessage("eamms.feed.list.msg.from")));
			requiredTimePane.addChild(frhourSb);
			requiredTimePane.addChild(frMinSb);
			requiredTimePane.addChild(new Label("toLab", app.getMessage("eamms.feed.list.msg.to")));
			requiredTimePane.addChild(toHourSb);
			requiredTimePane.addChild(toMinSb);
			pane.addChild(requiredTimePane);
			
			pane.addChild(new Label("timeZoneSbLab", bold(app.getMessage("eamms.feed.list.msg.timeZone") + "*")));
			pane.addChild(timeZoneSb);
			
			pane.addChild(new Label("totalReqTimeLab", bold(app.getMessage("eamms.feed.list.msg.totalTimeReq") + "*")));
			Panel totalReqTimeSbPane = new Panel("totalReqTimeSbPane");
			totalReqTimeSbPane.addChild(reqTimeTx);
			totalReqTimeSbPane.addChild(totalReqTimeSb);
			pane.addChild(totalReqTimeSbPane);
			
			pane.addChild(new Label("remarksLab", bold(app.getMessage("eamms.feed.list.msg.remarks") + "*")));
			pane.addChild(remarks);
			
			submit_Continue = new Button("submit_Continue", app.getMessage("eamms.feed.list.button.subNCon"));
			submit_finish = new Button("submit_finish", app.getMessage("eamms.feed.list.button.subNFin"));
			cancel = new Button(Form.CANCEL_FORM_ACTION, app.getMessage("eamms.feed.list.button.cancel"));
			
			pane.addChild(new Label("dummyLab1", ""));
			Panel buttonPane = new Panel("buttonPane");
			buttonPane.setColumns(3);
			buttonPane.addChild(submit_Continue);
			buttonPane.addChild(submit_finish);
			buttonPane.addChild(cancel);
			pane.addChild(buttonPane);
		}
		else if(mode != null && mode.equals(MODE_NETWORK))
		{
			feedTitle_nw = new Label("feedTitle_nw", "");
			requiredDate_nw = new Label("requiredDate_nw", "");
			requiredTime_nw = new Label("requiredTime_nw", "");
			totalTimeReq_nw = new Label("totalTimeReq_nw", "");
			remarks_nw = new Label("remarks_nw", "");
			
			bookingStatusSb = new SelectBox("bookingStatusSb");
			networkRemarks = new TextBox("networkRemarks");
			attchment = new FileUpload("attchment");
			attchDisplay = new Label("attchDisplay", "");
			statusSb = new SelectBox("statusSb");
			
			networkRemarks.addChild(new ValidatorNotEmpty("validator_networkRemarks", app.getMessage("eamms.feed.msg.vne.networkRemark")));
			bookingStatusSb.addChild(new ValidatorSelectBox("validator_bookingStatusSb", app.getMessage("eamms.feed.msg.vne.bookingStatus"), "-1"));
			statusSb.addChild(new ValidatorSelectBox("validator_statusSb", app.getMessage("eamms.feed.msg.vne.status"), "-1"));
			
			pane.addChild(new Label("feedTitleLab", bold(app.getMessage("eamms.feed.list.msg.tvroTitle"))));
			pane.addChild(feedTitle_nw);
			
			pane.addChild(new Label("requiredDateLab", bold(app.getMessage("eamms.feed.list.msg.requiredDate"))));
			pane.addChild(requiredDate_nw);
			
			pane.addChild(new Label("requiredTimeLab", bold(app.getMessage("eamms.feed.list.msg.requiredTime"))));
			pane.addChild(requiredTime_nw);
			
			pane.addChild(new Label("totalReqTimeLab", bold(app.getMessage("eamms.feed.list.msg.requiredTime"))));
			pane.addChild(totalTimeReq_nw);
			
			pane.addChild(new Label("remarksLab", bold(app.getMessage("eamms.feed.list.msg.requiredTime"))));
			pane.addChild(remarks_nw);
			
			pane.addChild(new Label("bookingStatusLab", bold(app.getMessage("eamms.feed.list.msg.bookingStat") + "*")));
			pane.addChild(bookingStatusSb);
			
			pane.addChild(new Label("networkRemarksLab", bold(app.getMessage("eamms.feed.list.msg.networkRemarks") + "*")));
			pane.addChild(networkRemarks);
			
			pane.addChild(new Label("attchmentLab", bold(app.getMessage("eamms.feed.list.msg.attc"))));
			Panel attchPane = new Panel("attchPane");
			attchPane.setColumns(2);
			attchPane.addChild(attchment);
			attchPane.addChild(attchDisplay);
			pane.addChild(attchPane);
			
			pane.addChild(new Label("statusSbLab", bold(app.getMessage("eamms.feed.list.msg.status") + "*")));
			pane.addChild(statusSb);
			
			update = new Button("update", app.getMessage("eamms.feed.list.button.update"));
			pane.addChild(new Label("dummyLab1", ""));
			pane.addChild(update);
		}
		
		addChild(pane); 
		initSelectBox();
	}
	
	public void loadForm()
	{
		if(assignId != null && !assignId.equals(""))
		{
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			Collection col = em.getAssignments(null, assignId);
			if(col != null && !col.isEmpty())
			{
				EammsAssignment assignObj = (EammsAssignment) col.iterator().next();
				if(assignObj != null)
				{
					assignmentId.setText(assignObj.getAssignmentId());
					
					if(mode != null && mode.equals(MODE_EDIT))
					{
						/*if("1".equals(blockBooking))
						{
							requiredDateFr.setDate(assignObj.getRequiredDateFrom());
							requiredDateTo.setDate(assignObj.getRequiredDateTo());
						}
						else
						{
							requiredDate.setDate(assignObj.getRequiredDate());
						}*/
						requiredDateFr.setDate(assignObj.getRequiredDateFrom());
						requiredDateTo.setDate(assignObj.getRequiredDateTo());
						
						feedTitleSb.setSelectedOption(assignObj.getTvroServiceId());
						frhourSb.setSelectedOption((String) assignObj.getProperty("hourFrStr"));
						frMinSb.setSelectedOption((String) assignObj.getProperty("minFrStr"));
						toHourSb.setSelectedOption((String) assignObj.getProperty("hourToStr"));
						toMinSb.setSelectedOption((String) assignObj.getProperty("minToStr"));
						timeZoneSb.setSelectedOption(assignObj.getTimezone());
						reqTimeTx.setValue(assignObj.getTotalTimeReq());
						totalReqTimeSb.setSelectedOption(assignObj.getTimeMeasure());
						remarks.setValue(assignObj.getRemarks());
					}
					else if(mode != null && mode.equals(MODE_NETWORK))
					{
						String reqTime = assignObj.getProperty("hourFrStr") + ":" + assignObj.getProperty("minFrStr") + " - " +
							assignObj.getProperty("hourToStr") + ":" + assignObj.getProperty("minToStr");
						
						/*if("1".equals(blockBooking))
						{
							requiredDate_nw.setText((String) assignObj.getProperty("requiredDateRangeStr"));
						}
						else
						{
							requiredDate_nw.setText((String) assignObj.getProperty("requiredDateStr"));
						}*/
						requiredDate_nw.setText((String) assignObj.getProperty("requiredDateRangeStr"));
						feedTitle_nw.setText((String) assignObj.getProperty("feedTitle"));
						requiredTime_nw.setText(reqTime);
						totalTimeReq_nw.setText((String) assignObj.getProperty("totalReqTime_measure"));
						remarks_nw.setText(assignObj.getRemarks());
						
						bookingStatusSb.setSelectedOption(assignObj.getBookingStatus());
						networkRemarks.setValue(assignObj.getNetworkRemarks());
						
						statusSb.setSelectedOption(assignObj.getStatus());
						attchPath = assignObj.getAttachment();
						attchDisplay.setText("");
						if(attchPath != null && !attchPath.equals(""))
						{
							try
							{
								StorageService storageService = (StorageService) Application.getInstance().getService(StorageService.class);
								if(storageService.get(new StorageFile(attchPath)) != null)
								{
									StorageFile sf = storageService.get(new StorageFile(attchPath));
									attchDisplay.setText("<a href='/storage/"+ attchPath + "' target='_blank'>" + sf.getName() + "</a>");
								}
							}
							catch (FileNotFoundException e)
							{
								attchDisplay.setText("attachment not found ..");
							}
							catch (Exception e) 
							{
								Log.getLog(getClass()).error(e.toString(), e);
							}
						}
					}
				}
			}
		}
	}
	
	public Forward onSubmit(Event evt)
	{
		Forward fwd = super.onSubmit(evt);
		
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		String buttonClicked = findButtonClicked(evt);
		if(buttonClicked != null && update != null && buttonClicked.equals(update.getAbsoluteName()))
		{
			if(attchment != null && attchment.getValue() != null && !attchment.getValue().equals(""))
			{
				double sizeLimit = 102400000; //100Mb default
				if(!em.isFileSizeValid(sizeLimit, attchment, evt))
				{
					attchment.setInvalid(true);
					this.setInvalid(true);
					
					return new Forward("invalidFileSize");
				}
			}
		}
		else if(buttonClicked != null && ( (submit_Continue != null && buttonClicked.equals(submit_Continue.getAbsoluteName())) || 
				(submit_finish != null && buttonClicked.equals(submit_finish.getAbsoluteName()))))
		{
			String tvroServiceId = WidgetUtil.getSbValue(feedTitleSb);
			/*boolean blockBooking = em.isBlockbookingTvroService(tvroServiceId);
			if(blockBooking)
			{
				Date reqDateFr = requiredDateFr.getDate();
				Date reqDateTo = requiredDateTo.getDate();
				
				if(reqDateFr == null || reqDateTo == null)
				{
					requiredDateFr.setInvalid(true);
					requiredDateTo.setInvalid(true);
					this.setInvalid(true);
				}
				else if(reqDateFr != null && reqDateTo != null)
				{
					if(!em.isWithinTvroFeedDateRange(tvroServiceId, reqDateFr, reqDateTo))
					{
						requiredDateFr.setInvalid(true);
						requiredDateTo.setInvalid(true);
						
						this.setInvalid(true);
						return new Forward("outOfrequiredDateRange");
					}
				}
			}
			else
			{
				Date reqDate = requiredDate.getDate();
				if(reqDate == null)
				{
					requiredDate.setInvalid(true);
					this.setInvalid(true);
				}
				else if(reqDate != null && !reqDate.equals(""))
				{
					if(!em.isWithinTvroFeedDateRange(tvroServiceId, reqDate, reqDate))
					{
						requiredDate.setInvalid(true);
						this.setInvalid(true);
						
						return new Forward("outOfrequiredDateRange");
					}
				}
			}*/
			Date reqDateFr = requiredDateFr.getDate();
			Date reqDateTo = requiredDateTo.getDate();
			
			if(reqDateFr == null || reqDateTo == null)
			{
				requiredDateFr.setInvalid(true);
				requiredDateTo.setInvalid(true);
				this.setInvalid(true);
			}
			else if(reqDateFr != null && reqDateTo != null)
			{
				if(!em.isWithinTvroFeedDateRange(tvroServiceId, reqDateFr, reqDateTo))
				{
					requiredDateFr.setInvalid(true);
					requiredDateTo.setInvalid(true);
					
					this.setInvalid(true);
					return new Forward("outOfrequiredDateRange");
				}
			}
			
			String reqTime = (String) reqTimeTx.getValue();
			if(reqTime != null && !reqTime.equals(""))
			{
				try
				{
					Integer.parseInt(reqTime);
				}
				catch(Exception e)
				{
					reqTimeTx.setInvalid(true);
					this.setInvalid(true);
				}
			}
		}
		
		return fwd;
	}
	
	public Forward onValidate(Event evt)
	{
		EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
		
		String buttonClicked = findButtonClicked(evt);
		if(buttonClicked != null && ( (submit_Continue != null && buttonClicked.equals(submit_Continue.getAbsoluteName())) || 
				(submit_finish != null && buttonClicked.equals(submit_finish.getAbsoluteName()))))
		{
			String reqTimeFr = WidgetUtil.getSbValue(frhourSb) + WidgetUtil.getSbValue(frMinSb);
			String reqTimeTo = WidgetUtil.getSbValue(toHourSb) + WidgetUtil.getSbValue(toMinSb);
			
			EammsAssignment assignObj = new EammsAssignment();
			assignObj.setId(UuidGenerator.getInstance().getUuid());
			assignObj.setAssignmentId(assignId);
			assignObj.setTvroServiceId(WidgetUtil.getSbValue(feedTitleSb));
			assignObj.setFeedsDetailsId(feedsDetailsId);
			assignObj.setRequiredTimeFrom(reqTimeFr);
			assignObj.setRequiredTimeTo(reqTimeTo);
			assignObj.setTimezone(WidgetUtil.getSbValue(timeZoneSb));
			assignObj.setTotalTimeReq((String)reqTimeTx.getValue());
			assignObj.setTimeMeasure(WidgetUtil.getSbValue(totalReqTimeSb));
			assignObj.setRemarks((String)remarks.getValue());
			assignObj.setCreatedBy(Application.getInstance().getCurrentUser().getId());
			assignObj.setCreatedDate(new Date());
			
			/*boolean blockBooking = em.isBlockbookingTvroService(assignObj.getTvroServiceId());
			if(blockBooking)
			{
				assignObj.setRequiredDate(null);
				assignObj.setRequiredDateFrom(requiredDateFr.getDate());
				assignObj.setRequiredDateTo(requiredDateTo.getDate());
			}
			else
			{
				assignObj.setRequiredDate(requiredDate.getDate());
				assignObj.setRequiredDateFrom(null);
				assignObj.setRequiredDateTo(null);
			}*/
			assignObj.setRequiredDateFrom(requiredDateFr.getDate());
			assignObj.setRequiredDateTo(requiredDateTo.getDate());
			
			em.saveAssignment(assignObj);
			if(buttonClicked.equals(submit_finish.getAbsoluteName()))
			{
				return new Forward("submitted");
			}
			return new Forward("continue");
		}
		else if(buttonClicked != null && update != null && buttonClicked.equals(update.getAbsoluteName()))
		{
			EammsAssignment assignObj = new EammsAssignment();
			assignObj.setAssignmentId(assignId);
			assignObj.setBookingStatus(WidgetUtil.getSbValue(bookingStatusSb));
			assignObj.setNetworkRemarks((String)networkRemarks.getValue());
			assignObj.setStatus(WidgetUtil.getSbValue(statusSb));
			
			String path = saveAttch(attchment, evt);
			if(path != null && !path.equals(""))
			{
				attchPath = path;
			}
			assignObj.setAttachment(attchPath);
			
			em.updateAssignment(assignObj);
			return new Forward("updated");
		}
		
		return super.onValidate(evt);
	}
	
	private void initSelectBox()
	{
		if(mode != null && mode.equals(MODE_NETWORK))
		{
			SequencedHashMap bMap = new SequencedHashMap();
			bMap.put("-1", Application.getInstance().getMessage("eamms.feed.list.opt.plsSelect"));
			bMap.put(EammsFeedsModule.NETWORK_BOOKING_STATUS_CONFIRMED, 
					Application.getInstance().getMessage("eamms.feed.list.bookS.opt.confirmed"));
			bMap.put(EammsFeedsModule.NETWORK_BOOKING_STATUS_PENDING, 
					Application.getInstance().getMessage("eamms.feed.list.bookS.opt.pending"));
			bMap.put(EammsFeedsModule.NETWORK_BOOKING_STATUS_PROCESSED, 
					Application.getInstance().getMessage("eamms.feed.list.bookS.opt.processed"));
			
			SequencedHashMap sMap = new SequencedHashMap();
			sMap.put("-1", Application.getInstance().getMessage("eamms.feed.list.opt.plsSelect"));
			sMap.put(EammsFeedsModule.NETWORK_STATUS_OF_ASSIGNMENT_ALLOCATED, 
					Application.getInstance().getMessage("eamms.feed.list.statusAssign.opt.allocated"));
			sMap.put(EammsFeedsModule.NETWORK_STATUS_OF_ASSIGNMENT_CANCELLED, 
					Application.getInstance().getMessage("eamms.feed.list.statusAssign.opt.cancelled"));
			
			bookingStatusSb.setOptionMap(bMap);
			statusSb.setOptionMap(sMap);
		}
		else if(mode != null && (mode.equals(MODE_ADD) || mode.equals(MODE_EDIT)))
		{
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			Collection colV = em.getTvroServiceIds(feedsDetailsId);
			if(colV != null && !colV.isEmpty())
			{
				for(Iterator itr = colV.iterator(); itr.hasNext();)
				{
					DefaultDataObject obj = (DefaultDataObject) itr.next();
					feedTitleSb.addOption(obj.getId(), (String)obj.getProperty("feedTitle"));
				}
			}
			
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
				frhourSb.addOption(hr, hr);
				toHourSb.addOption(hr, hr);
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
				frMinSb.addOption(min, min);
				toMinSb.addOption(min, min);
			}
			
			timeZoneSb.addOption(EammsFeedsModule.TIME_ZONE_MST, Application.getInstance().getMessage("eamms.feed.list.timeZ.opt.mst"));
			timeZoneSb.addOption(EammsFeedsModule.TIME_ZONE_GMT, Application.getInstance().getMessage("eamms.feed.list.timeZ.opt.gmt"));
			
			totalReqTimeSb.addOption(EammsFeedsModule.TIME_REQ_MIN, Application.getInstance().getMessage("eamms.feed.list.opt.min"));
			totalReqTimeSb.addOption(EammsFeedsModule.TIME_REQ_HOUR, Application.getInstance().getMessage("eamms.feed.list.opt.hour"));
		}
	}
	
	private String saveAttch(FileUpload attchment, Event evt)
	{
		if(attchment.getValue() != null && !attchment.getValue().equals(""))
		{
			try
			{
				StorageService storageService = (StorageService) Application.getInstance().getService(StorageService.class);
				StorageFile sf = attchment.getStorageFile(evt.getRequest());
				if (sf != null) 
				{
					String storagePath = PATH + "/" + assignId;
					sf.setParentDirectoryPath(storagePath);
					storageService.store(sf);
					
					return sf.getAbsolutePath();
				}
			}
			catch (IOException e)
			{ 
				Log.getLog(getClass()).error(e.toString(), e);
			} 
			catch (Exception e) 
			{
				Log.getLog(getClass()).error(e.toString(), e);
			}
		}
		return "";
	}
	
	private String bold(String str)
	{
		str = "<b>" + str + "</b>";
		return str;
	}
	
	public String getDefaultTemplate()
	{
		return "fms/eamms/feed_addEditAssignmentTemplate";
		//return super.getDefaultTemplate();
	}

	public String getMode()
	{
		return mode;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getAssignId()
	{
		return assignId;
	}

	public void setAssignId(String assignId)
	{
		this.assignId = assignId;
	}

	public String getFeedsDetailsId()
	{
		return feedsDetailsId;
	}

	public void setFeedsDetailsId(String feedsDetailsId)
	{
		this.feedsDetailsId = feedsDetailsId;
	}

	public String getBlockBooking()
	{
		return blockBooking;
	}

	public void setBlockBooking(String blockBooking)
	{
		this.blockBooking = blockBooking;
	}
}
