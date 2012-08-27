package com.tms.fms.eamms.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.services.security.Group;
import kacang.services.security.SecurityService;
import kacang.stdui.DatePopupField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.eamms.model.EammsFeedsModule;
import com.tms.fms.eamms.model.FeedsLogObject;
import com.tms.fms.util.WidgetUtil;

public class DailyFeedsLogListing extends Table 
{
	protected DatePopupField sbDateTo;
	protected DatePopupField sbDateFr;
	protected SelectBox sbTelco;
	protected SelectBox sbAdhoc;
	
	Collection<FeedsLogObject> feedsLogCol;

	public DailyFeedsLogListing() {
	}

	public DailyFeedsLogListing(String s) {
		super(s);
	}

	public void init() 
	{
		setModel(new DailyFeedsListingModel());
		setWidth("100%");
	}
	
	public void onRequest(Event event) 
	{
		init();
	}

	class DailyFeedsListingModel extends TableModel
	{
		DailyFeedsListingModel() 
		{
			removeChildren();
			Application app = Application.getInstance();

			TableColumn feedLogId = new TableColumn("feedLogId", app.getMessage("eamms.feed.log.msg.feedsLogId"));
			feedLogId.setUrlParam("id");
			addColumn(feedLogId);
			
			TableColumn date = new TableColumn("date", app.getMessage("eamms.feed.log.msg.date"));
			date.setFormat(new TableDateFormat("dd MMM yyyy"));
			addColumn(date);
			
			TableColumn program = new TableColumn("programName", app.getMessage("eamms.feed.log.msg.program"));
			addColumn(program);
			
			TableColumn reqId_requestTitle = new TableColumn("title", app.getMessage("eamms.feed.log.msg.reqsId_reqsTitle"));
			addColumn(reqId_requestTitle);
			
			TableColumn assignmentId_reqTime = new TableColumn("assign_time", app.getMessage("eamms.feed.log.msg.assignId_reqTime"));
			assignmentId_reqTime.setSortable(false);
			addColumn(assignmentId_reqTime);
			
			TableColumn location = new TableColumn("location", app.getMessage("eamms.feed.log.msg.location"));
			addColumn(location);
			
			TableColumn station = new TableColumn("stationName", app.getMessage("eamms.feed.log.msg.station"));
			addColumn(station);
			
			TableColumn timeIn = new TableColumn("dateTimeIn", app.getMessage("eamms.feed.log.msg.timeIn"));
			addColumn(timeIn);
			
			TableColumn TimeOut = new TableColumn("dateTimeOut", app.getMessage("eamms.feed.log.msg.timeOut"));
			addColumn(TimeOut);
			
			TableColumn ebNum = new TableColumn("ebNo", app.getMessage("eamms.feed.log.msg.ebNum"));
			ebNum.setSortable(false);
			addColumn(ebNum);
			
			TableColumn assAV = new TableColumn("assAV", app.getMessage("eamms.feed.log.msg.assAv"));
			assAV.setSortable(false);
			addColumn(assAV);
			
			TableColumn mcr = new TableColumn("mcrName", app.getMessage("eamms.feed.log.msg.mcr"));
			addColumn(mcr);
			
			TableColumn news = new TableColumn("news", app.getMessage("eamms.feed.log.msg.news"));
			addColumn(news);
			
			TableColumn stringer = new TableColumn("stringer", app.getMessage("eamms.feed.log.msg.stringer"));
			addColumn(stringer);
			
			TableColumn telco = new TableColumn("telcoName", app.getMessage("eamms.feed.log.msg.telco"));
			addColumn(telco);
			
			TableColumn remarks = new TableColumn("remarks", app.getMessage("eamms.feed.log.msg.remarks"));
			remarks.setSortable(false);
			addColumn(remarks);
			
			TableColumn status = new TableColumn("status", app.getMessage("eamms.feed.log.msg.status"));
			addColumn(status);
			
			TableColumn adhoc = new TableColumn("adhoc", app.getMessage("eamms.feed.log.msg.adhoc"));
			adhoc.setFormat(new TableFormat(){

				public String format(Object value)
				{
					if(EammsFeedsModule.FEED_LOG_ADHOC.equals(value))
					{
						return Application.getInstance().getMessage("eamms.feed.log.adHoc.adhoc");
					}
					return Application.getInstance().getMessage("eamms.feed.log.adHoc.nonAdhoc");
				}});
			addColumn(adhoc);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter datefrom = new TableFilter("datefrom");
			sbDateFr = new DatePopupField("sbDateFrom");
			sbDateFr.setOptional(true);
			datefrom.setWidget(sbDateFr);
			addFilter(datefrom);
			
			TableFilter dateTo = new TableFilter("dateTo");
			sbDateTo = new DatePopupField("sbDateTo");
			sbDateTo.setOptional(true);
			dateTo.setWidget(sbDateTo);
			addFilter(dateTo);
			
			initSelectBox();
			TableFilter telcoFilter = new TableFilter("telcoFilter");
			telcoFilter.setWidget(sbTelco);
			addFilter(telcoFilter);
			
			TableFilter adhocFilter = new TableFilter("adhocFilter");
			adhocFilter.setWidget(sbAdhoc);
			addFilter(adhocFilter);
			
			if(isAllowedGroup())
			{
				addAction(new TableAction("exportFeeds", app.getMessage("eamms.feed.log.button.expToCsv")));
			}
			addAction(new TableAction("new", app.getMessage("eamms.feed.log.button.new")));
		}

		public Collection getTableRows()
		{
			String searchBy = (String) getFilterValue("searchBy");
			String telco = WidgetUtil.getSbValue(sbTelco);
			String adhoc = WidgetUtil.getSbValue(sbAdhoc);
			Date dateFr = sbDateFr.getDate();
			Date dateTo = sbDateTo.getDate();

			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			feedsLogCol = em.selectFeedsLog(searchBy, telco, adhoc, dateFr, dateTo, 
					getSort(), isDesc(), getStart(), getRows());
			
			return feedsLogCol;
		}

		public int getTotalRowCount() 
		{
			String searchBy = (String) getFilterValue("searchBy");
			String telco = WidgetUtil.getSbValue(sbTelco);
			String adhoc = WidgetUtil.getSbValue(sbAdhoc);
			Date dateFr = sbDateFr.getDate();
			Date dateTo = sbDateTo.getDate();

			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			int result = em.selectCountFeedsLog(searchBy, telco, adhoc, dateFr, dateTo);
			
			return result;
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) 
		{
			if ("exportFeeds".equals(action))
			{
				Date dateFr = sbDateFr.getDate();
				Date dateTo = sbDateTo.getDate();
				
				String df = "";
				String dt = "";
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				try
				{
					df = dateFr != null ? sdf.format(dateFr) : "-";
					dt = dateTo != null ? sdf.format(dateTo) : "-";
				}
				catch(Exception e){}
				
				event.getRequest().getSession().setAttribute("feedsLogCol", feedsLogCol);
				event.getRequest().getSession().setAttribute("filterDateFr", df);
				event.getRequest().getSession().setAttribute("filterDateTo", dt);
				
				return new Forward("exportToCsv");
			}
			else if("new".equals(action))
			{
				return new Forward("addNewFeedsLog");
			}
			return super.processAction(event, action, selectedKeys);
		}
	}
	
	private void initSelectBox()
	{
		Application app = Application.getInstance();
		EammsFeedsModule em = (EammsFeedsModule) app.getModule(EammsFeedsModule.class);
		
		sbTelco = new SelectBox("sbTelco");
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
		
		sbAdhoc = new SelectBox("sbAdhoc");
		sbAdhoc.addOption("-1", app.getMessage("eamms.feed.log.adHoc.all"));
		sbAdhoc.addOption(EammsFeedsModule.FEED_LOG_ADHOC, app.getMessage("eamms.feed.log.adHoc.adhoc"));
		sbAdhoc.addOption(EammsFeedsModule.FEED_LOG_NON_ADHOC, app.getMessage("eamms.feed.log.adHoc.nonAdhoc"));
	}
	
	private boolean isAllowedGroup()
	{
		SecurityService ss = (SecurityService) Application.getInstance().getService(SecurityService.class);
		String currentUserId =  Application.getInstance().getCurrentUser().getId();
		Collection<Group> groups = null;
		try
		{
			groups = ss.getUserGroups(currentUserId);
		}
		catch(Exception e)
		{
			Log.getLog(getClass()).error(e, e);
		}
		
		for(Group grp : groups)
		{
			String groupId = grp.getId();
			if(Application.getInstance().getProperty("Administrator").equals(groupId))
			{
				return true;
			}
			else if(Application.getInstance().getProperty("UnitHeadNetwork").equals(groupId))
			{
				return true;
			}
		}
		return false;
	}
}
