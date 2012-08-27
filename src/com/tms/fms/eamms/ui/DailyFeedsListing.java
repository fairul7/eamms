package com.tms.fms.eamms.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.model.DefaultDataObject;
import kacang.stdui.DatePopupField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;

import com.tms.fms.eamms.model.EammsFeedsModule;
import com.tms.fms.util.WidgetUtil;

public class DailyFeedsListing extends Table 
{
	protected DatePopupField sbDateTo;
	protected DatePopupField sbDateFr;
	protected SelectBox sbNetworkStatus;

	public DailyFeedsListing() {
	}

	public DailyFeedsListing(String s) {
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

			TableColumn requestId = new TableColumn("requestId", app.getMessage("eamms.feed.list.msg.requestId"));
			requestId.setUrlParam("requestId");
			addColumn(requestId);
			
			TableColumn program = new TableColumn("programName", app.getMessage("eamms.feed.list.msg.program"));
			addColumn(program);
			
			TableColumn requestTitle = new TableColumn("title", app.getMessage("eamms.feed.list.msg.requestTitle"));
			addColumn(requestTitle);
			
			TableColumn requiredDate = new TableColumn("requiredDateRange", app.getMessage("eamms.feed.list.msg.requiredDate"));
			requiredDate.setSortable(false);
			addColumn(requiredDate);
			
			TableColumn feedType = new TableColumn("feedType", app.getMessage("eamms.feed.list.msg.feedType"));
			addColumn(feedType);
			
			TableColumn networkStatus = new TableColumn("networkStatus", app.getMessage("eamms.feed.list.msg.networkStatus"));
			addColumn(networkStatus);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter datefrom = new TableFilter("datefrom");
			sbDateFr = new DatePopupField("sbDateFrom");
			sbDateFr.setOptional(true);
			//sbDateFr.setValue(EammsFeedsModule.DATE_FROM);
			datefrom.setWidget(sbDateFr);
			addFilter(datefrom);
			
			TableFilter dateTo = new TableFilter("dateTo");
			sbDateTo = new DatePopupField("sbDateTo");
			sbDateTo.setOptional(true);
			//sbDateTo.setValue(EammsFeedsModule.DATE_TO);
			dateTo.setWidget(sbDateTo);
			addFilter(dateTo);
			
			initSelectBox();
			TableFilter nwStatus = new TableFilter("nwStatus");
			nwStatus.setWidget(sbNetworkStatus);
			addFilter(nwStatus);
		}

		public Collection getTableRows()
		{
			String searchBy = (String) getFilterValue("searchBy");
			String nwStatus = WidgetUtil.getSbValue(sbNetworkStatus);
			String dateFr = (String) sbDateFr.getValue();
			String dateTo = (String) sbDateTo.getValue();

			dateFr = dateFr != null && !dateFr.equals(EammsFeedsModule.DATE_FROM) ? dateFr : null;
			dateTo = dateTo != null && !dateTo.equals(EammsFeedsModule.DATE_TO) ? dateTo : null;
			
			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			Collection result = em.getEammsFeedsDetails(searchBy, nwStatus, dateFr, dateTo, getSort(), isDesc(), getStart(), getRows());
			
			em.processFeedsDetails(result);
			return result;
		}

		public int getTotalRowCount() 
		{
			String searchBy = (String) getFilterValue("searchBy");
			String nwStatus = WidgetUtil.getSbValue(sbNetworkStatus);
			String dateFr = (String) sbDateFr.getValue();
			String dateTo = (String) sbDateTo.getValue();

			dateFr = dateFr != null && !dateFr.equals(EammsFeedsModule.DATE_FROM) ? dateFr : null;
			dateTo = dateTo != null && !dateTo.equals(EammsFeedsModule.DATE_TO) ? dateTo : null;

			EammsFeedsModule em = (EammsFeedsModule) Application.getInstance().getModule(EammsFeedsModule.class);
			int result = em.getCountEammsFeedsDetails(searchBy, nwStatus, dateFr, dateTo);
			
			return result;
		}
	}
	
	private void initSelectBox()
	{
		Application app = Application.getInstance();
		EammsFeedsModule em = (EammsFeedsModule) app.getModule(EammsFeedsModule.class);
		
		sbNetworkStatus = new SelectBox("sbNetworkStatus");
		sbNetworkStatus.addOption("-1", app.getMessage("eamms.feed.list.ns.opt.networkStatus"));
		Collection col = em.getSetupTable("fms_feed_network_status", null);
		if(col != null && !col.isEmpty())
		{
			for(Iterator itr = col.iterator(); itr.hasNext();)
			{
				DefaultDataObject obj = (DefaultDataObject) itr.next();
				sbNetworkStatus.addOption(obj.getId(), (String)obj.getProperty("c_name"));
			}
		}
	}
}
