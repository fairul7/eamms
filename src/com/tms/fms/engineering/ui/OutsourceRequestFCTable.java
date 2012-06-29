package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;

/**
 * 
 * @author fahmi
 *
 */
public class OutsourceRequestFCTable extends Table{
	protected String requestId;
	private int totalRows=0;
	
	public void init(){
		super.init();
        setPageSize(20);
        setWidth("100%");
		setModel(new OutSourceRequestFCTableModel());		
	}
	
	public class OutSourceRequestFCTableModel extends TableModel {
		public OutSourceRequestFCTableModel(){
			Application app = Application.getInstance();
			FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
			
			TableColumn tcOutsourceType = new TableColumn("outsourceType", app.getMessage("fms.facility.table.label.outsourceType", "Outsource Type"));
			Map mapOutsourceType = new HashMap(); 
			mapOutsourceType.put("C", "Company"); 
			mapOutsourceType.put("I", "Individual");
			tcOutsourceType.setFormat(new TableStringFormat(mapOutsourceType));
			tcOutsourceType.setUrl("outsourceRequestFC.jsp?requestId="+getRequestId());
			tcOutsourceType.setUrlParam("outsourceId");
			addColumn(tcOutsourceType);		
			
			TableColumn tcName = new TableColumn("clientName", app.getMessage("fms.facility.table.label.name", "Name"));
			addColumn(tcName);		
			
			TableColumn tcEstimatedCost = new TableColumn("estimatedCost", app.getMessage("fms.facility.table.label.estimatedCost", "Estimated Cost") + " (" +app.getMessage("fms.request.label.currency")+") ");
			addColumn(tcEstimatedCost);		
			
			TableColumn tcActualCost = new TableColumn("actualCost", app.getMessage("fms.facility.table.label.actualCost", "Actual Cost")+ " (" +app.getMessage("fms.request.label.currency")+") ");
			addColumn(tcActualCost);		
			
			TableFilter filterSearch = new TableFilter("search");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			filterSearch.setWidget(searchText);
			addFilter(filterSearch);
			
			TableFilter filterOutsourceType = new TableFilter("filterOutsourceType");
			SelectBox sbOutsourceType = new SelectBox("sbOutsourceType");
			sbOutsourceType.setOptions("-1=" + Application.getInstance().getMessage("fms.setup.status", "Status"));
			sbOutsourceType.setOptions("1=" + Application.getInstance().getMessage("fms.setup.active", "Active"));
			sbOutsourceType.setOptions("0=" + Application.getInstance().getMessage("fms.setup.inactive", "Inactive"));
			filterOutsourceType.setWidget(sbOutsourceType);
			addFilter(filterOutsourceType);
			
			addAction(new TableAction("add", Application.getInstance().getMessage("outsource.listing.add", "Add New")));
			addAction(new TableAction("delete", Application.getInstance().getMessage("outsource.listing.delete", "Delete Selected")));
			try {
				int x = 0;
				x = fcm.getOutsourceCount("", "-1", requestId, "clientName", true, 0, -1);
				if (x > 0) {
					addAction(new TableAction("sendEmail", Application.getInstance().getMessage("outsource.button.sendEmail", "Send Email Notification")));
				} 
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
			}
			
		    		
		}		
		
		public String getTableRowKey(){
			return "outsourceId";
		}
		
		public String getSearchText(){
			return (String) getFilterValue("search");
		}
		
		public String getOutsourceType() {
			String returnValue = "-1";
			List lstOutsourceType = (List)getFilterValue("filterOutsourceType");
			if (lstOutsourceType.size() > 0) {returnValue = (String)lstOutsourceType.get(0);}
			return returnValue;
		}
		
		@Override
		public Collection getTableRows() {
			Application app = Application.getInstance();
			String searchBy = "";
			String outsourceType = "";

			FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
			
			try {
				return fcm.getOutsource(getSearchText(), getOutsourceType(), getRequestId(), getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return new ArrayList();
			}
		}

		@Override
		public int getTotalRowCount() {
			Application app = Application.getInstance();
			String searchBy = "";
			String outsourceType = "";

			FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)app.getModule(FacilitiesCoordinatorModule.class);
			
			try {
				return fcm.getOutsourceCount(getSearchText(), getOutsourceType(), getRequestId(), getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString(), e);
				return totalRows;
			}
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			FacilitiesCoordinatorModule fcm = (FacilitiesCoordinatorModule)Application.getInstance().getModule(FacilitiesCoordinatorModule.class);
			EngineeringModule eModule = (EngineeringModule)Application.getInstance().getModule(EngineeringModule.class);
			
			if ("delete".equals(action)){
		    	
		    	for (int i=0; i<selectedKeys.length; i++){
		 			fcm.deleteOutsource(selectedKeys[i]);
		 		}		    
		    	return new Forward("delete", "outsourceRequestFC.jsp?requestId=" + getRequestId(), true);
			} else if ("add".equals(action)){
				return new Forward("add", "outsourceRequestFC.jsp?requestId=" + getRequestId(), true);
			} else if ("sendEmail".equals(action)){
				eModule.sendAcceptanceEmail(getRequestId(), true);
				return new Forward("sendEmail");
			}
			
			return super.processAction(event, action, selectedKeys); 	
		}
	}
	
	public void onRequest(Event event) {
		//setRequestId(event.getRequest().getParameter("requestId"));
		init();
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	
}
