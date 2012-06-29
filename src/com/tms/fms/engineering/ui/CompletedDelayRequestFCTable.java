package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.FacilitiesCoordinatorModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

/**
 * @author fahmi
 *
 */
@SuppressWarnings("serial")
public class CompletedDelayRequestFCTable extends Table {
	
	protected SelectBox sbRequestType;
	protected SelectBox sbStatus;

	public CompletedDelayRequestFCTable() {
	}

	public CompletedDelayRequestFCTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new CompletedDelayRequestFCTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new CompletedDelayRequestFCTableModel());
	}

	class CompletedDelayRequestFCTableModel extends TableModel {
		CompletedDelayRequestFCTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns			
			TableColumn requestId = new TableColumn("requestId", app.getMessage("fms.request.label.requestId"));
			requestId.setUrl("requestDetails.jsp");
			requestId.setUrlParam("requestId");
			addColumn(requestId);
			
			TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"));
			addColumn(title);
			
//			TableColumn department = new TableColumn("department", app.getMessage("fms.request.label.department"));
//			addColumn(department);
			
			TableColumn servicesCSV = new TableColumn("servicesCSV", app.getMessage("fms.request.label.servicesRequired"), false);
			addColumn(servicesCSV);
			
			TableColumn createdOn = new TableColumn("submittedDate", app.getMessage("fms.request.label.dateSubmitted"));
			createdOn.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(createdOn);
			
			TableColumn status = new TableColumn("status", app.getMessage("fms.facility.table.status"));
			status.setFormat(new TableStringFormat(EngineeringModule.STATUS_MAP));
			addColumn(status);
			
			TableColumn requestedBy = new TableColumn("createdUserName", app.getMessage("fms.facility.table.requestedBy"));
			addColumn(requestedBy);			

			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			TableFilter tfDepartment = new TableFilter("tfDepartment");
			SelectBox sbDepartment = new SelectBox("sbDepartment");
		    sbDepartment.setOptions("-1=" + Application.getInstance().getMessage("fms.request.label.department", "Department"));
		    try {
		    	FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
                 
                Collection lstDepartment = manager.getDepartment("", "name", false, 0, -1);
			    if (lstDepartment.size() > 0) {
			    	for (Iterator i=lstDepartment.iterator(); i.hasNext();) {
			        	FMSDepartment dept = (FMSDepartment)i.next();
			        	sbDepartment.setOptions(dept.getId()+"="+ dept.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			tfDepartment.setWidget(sbDepartment);
			addFilter(tfDepartment);
//						
//			TableFilter tfStatus = new TableFilter("status");
//			SequencedHashMap statusMap=new SequencedHashMap();
//			statusMap.put("-1",app.getMessage("fms.facility.table.status"));
//			statusMap.putAll(EngineeringModule.STATUS_MAP);
//			sbStatus=new SelectBox("sbStatus");
//			sbStatus.setOptionMap(statusMap);
//			sbStatus.setSelectedOption(EngineeringModule.APPLIED_CANCELLATION);
//			tfStatus.setWidget(sbStatus);
//			addFilter(tfStatus);
		}
		
		public String getDepartment() {
			String returnValue = "-1";
			List lstDepartment = (List)getFilterValue("tfDepartment");
			if (lstDepartment.size() > 0) {returnValue = (String)lstDepartment.get(0);}
			return returnValue;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Collection getTableRows() {
			String searchBy = "";
			String status = "";
			searchBy = (String) getFilterValue("searchBy");
			//status=WidgetUtil.getSbValue(sbStatus);
			status = EngineeringModule.LATE_STATUS;
			
			Application application = Application.getInstance();
			FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule) application.getModule(FacilitiesCoordinatorModule.class);
			
			try {
				return mod.getRequest(searchBy, getDepartment(), status, getSort(), isDesc(), getStart(), getRows());
			} catch(DaoException de){
				return new ArrayList();
			}	
		}

		@Override
		public int getTotalRowCount() {
			String searchBy = "";
			String status = "";
			searchBy = (String) getFilterValue("searchBy");
			//status=WidgetUtil.getSbValue(sbStatus);
			status = EngineeringModule.LATE_STATUS;
			
			Application application = Application.getInstance();
			FacilitiesCoordinatorModule mod = (FacilitiesCoordinatorModule) application.getModule(FacilitiesCoordinatorModule.class);
			
			return mod.selectRequestCount(searchBy, getDepartment(), status);
		}

		public Forward processAction(Event event, String action,String[] selectedKeys) {
			return super.processAction(event, action, selectedKeys);
			
		}
	}

}
