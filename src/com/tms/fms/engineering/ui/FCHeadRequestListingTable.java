package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

public class FCHeadRequestListingTable extends Table {
	
	//protected SelectBox sbRequestType;
	protected SelectBox sbStatus;
	
	public DatePopupField requiredField;
	public DatePopupField requiredFieldTo;
	public SelectBox sbDepartment;

	public FCHeadRequestListingTable() {
	}

	public FCHeadRequestListingTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new FCHeadRequestListingTableModel());
		setWidth("100%");
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/engineering/filterFormTemplate");
	}

	public void onRequest(Event event) {
		setModel(new FCHeadRequestListingTableModel());
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/engineering/filterFormTemplate");
	}

	class FCHeadRequestListingTableModel extends TableModel {
		FCHeadRequestListingTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			//addAction(new TableAction("add", app.getMessage("fms.facility.add")));
			//addAction(new TableAction("delete", app.getMessage("fms.facility.delete"), app.getMessage("fms.facility.msg.confirmDelete")));

			// table columns
			TableColumn requestId = new TableColumn("requestIdWithLink", app.getMessage("fms.request.label.requestId"));
			requestId.setEscapeXml(false);
			addColumn(requestId);
			
			TableColumn title = new TableColumn("title", app.getMessage("fms.request.label.requestTitle"));
			addColumn(title);
			
			TableColumn servicesCSV = new TableColumn("servicesCSV", app.getMessage("fms.request.label.servicesRequired"), false);
			addColumn(servicesCSV);
			
			TableColumn createdOn = new TableColumn("submittedDate", app.getMessage("fms.request.label.dateSubmitted"));
			createdOn.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(createdOn);
			
			TableColumn status = new TableColumn("status", app.getMessage("fms.facility.table.status"));
			//status.setFormat(new TableStringFormat(EngineeringModule.FC_HEAD_STATUS_MAP));
			//status.setFormat(new TableStringFormat(EngineeringModule.FC_HEAD_STATUS_MAP));
			status.setFormat(new TableFormat(){
				public String format(Object obj){
					String status = (String)obj;	
					String state = null;
					if (!"".equals(status) || !status.equals(null)){						
						try{						
							state = (String) EngineeringModule.FC_HEAD_STATUS_MAP.get(status);
						}catch(Exception e){}
					} else {
						state = " - ";
					}
					
					if(null == state)
						state = " - ";
					
					if(status.equals(EngineeringModule.REJECTED_STATUS)){
						String color = "<font color='red'>"+state+"</font>";
						state = color;
					}
		    		return state;							
				}
			});
			addColumn(status);
			
			TableColumn requestedBy = new TableColumn("createdUserName", app.getMessage("fms.facility.table.requestedBy"));
			addColumn(requestedBy);
			
			TableColumn pic = new TableColumn("fullName", app.getMessage("fms.facility.table.pic"));
			addColumn(pic);

			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
/*			TableFilter tfRequestType = new TableFilter("requestType");
			sbRequestType=new SelectBox("sbRequestType");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",app.getMessage("fms.request.label.requestType"));
			typeMap.putAll(EngineeringModule.REQUEST_TYPE_MAP);
			sbRequestType.setOptionMap(typeMap);
			tfRequestType.setWidget(sbRequestType);
			addFilter(tfRequestType);
*/			
			TableFilter tfStatus = new TableFilter("status");
			SequencedHashMap statusMap=new SequencedHashMap();
			statusMap.put("-1",app.getMessage("fms.facility.table.status"));
			statusMap.putAll(EngineeringModule.FC_HEAD_STATUS_MAP);
//			statusMap.putAll(EngineeringModule.FC_HEAD_STATUS_NO_CANCEL_MAP);
			sbStatus=new SelectBox("sbStatus");
			sbStatus.setOptionMap(statusMap);
			tfStatus.setWidget(sbStatus);
			addFilter(tfStatus);
			
			TableFilter tfDepartment = new TableFilter("tfDepartment");
			sbDepartment = new SelectBox("sbDepartment");
		    sbDepartment.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.allDepartment", "All Department"));
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
						
						
			requiredField = new DatePopupField("requiredDate");
			requiredField.setOptional(true);

			TableFilter requiredDate = new TableFilter("requiredDate");
			requiredDate.setWidget(requiredField);
			addFilter(requiredDate);
			
			requiredFieldTo = new DatePopupField("requiredDateTo");
			requiredFieldTo.setOptional(true);

			TableFilter requiredDateTo = new TableFilter("requiredDateTo");
			requiredDateTo.setWidget(requiredFieldTo);
			addFilter(requiredDateTo);

		}
		
		public String getDepartment() {
			String returnValue = "-1";
			List lstDepartment = (List)getFilterValue("tfDepartment");
			if (lstDepartment.size() > 0) {returnValue = (String)lstDepartment.get(0);}
			return returnValue;
		}

		public Collection getTableRows() {
			String searchBy = "";
			String status 	= ""; 
			searchBy 		= (String) getFilterValue("searchBy");
			status 			= WidgetUtil.getSbValue(sbStatus);
			Date requiredDate = requiredField.getDate();
			Date requiredDateTo = requiredFieldTo.getDate();
			
			EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
			try {
				return module.getFCHeadAllRequest(requiredDate, requiredDateTo, getDepartment(), searchBy, status, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			String status 	= ""; 
			searchBy 		= (String) getFilterValue("searchBy");
			status 			= WidgetUtil.getSbValue(sbStatus);
			Date requiredDate = requiredField.getDate();
			Date requiredDateTo = requiredFieldTo.getDate();
			
			EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
			try {
				return dao.selectFCHeadAllRequestCount(requiredDate, requiredDateTo, getDepartment(), searchBy, status);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		/*public String getTableRowKey() {
				return "requestId";
		}*/
/*
		public Forward processAction(Event event, String action,String[] selectedKeys) {
			if ("add".equals(action)){
				return new Forward("ADD");
			}
			else if ("delete".equals(action)) {
				try {
					for (int i = 0; i < selectedKeys.length; i++){
						module.deleteWorkingProfile(selectedKeys[i]);
					}
					
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
					return new Forward("NOTDELETED");
				}
			}
			return super.processAction(event, action, selectedKeys);
			
		}*/
	}

}
