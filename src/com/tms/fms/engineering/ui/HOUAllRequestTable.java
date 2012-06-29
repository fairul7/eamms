package com.tms.fms.engineering.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

public class HOUAllRequestTable extends Table {
	
	//protected SelectBox sbRequestType;
	protected SelectBox sbStatus;
	protected DatePopupField requiredField;
	protected SelectBox sbDepartment;
	protected int count;
	
	public DatePopupField requiredFieldTo;
	
	public HOUAllRequestTable() {
	}

	public HOUAllRequestTable(String s) {
		super(s);
	}
	
	public void onRequest(Event event) {
		setModel(new HOUAllRequestTableModel());
		Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/engineering/houAllRequestTemplate");
		setCurrentPage(1);
	}
	public void init() {
		super.init();
		setPageSize(20);
		setModel(new HOUAllRequestTableModel());
		setWidth("100%");
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/engineering/houAllRequestTemplate");

	}
	
	class HOUAllRequestTableModel extends TableModel {
		HOUAllRequestTableModel() {
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
			
			TableColumn requiredFrom = new TableColumn("requiredDateFrom", app.getMessage("fms.facility.label.requiredDate"));
			//requiredFrom.setFormat(new TableDateFormat(SetupModule.DATE_FORMAT));
			addColumn(requiredFrom);
			
			TableColumn status = new TableColumn("status", app.getMessage("fms.facility.table.status"));
			//status.setFormat(new TableStringFormat(EngineeringModule.STATUS_MAP));
			//UNIT_HEAD_STATUS_MAP
			status.setFormat(new TableFormat(){
				public String format(Object obj){
					String status = (String)obj;	
					String state = null;
					if (!"".equals(status) || !status.equals(null)){						
						try{						
							state = (String) EngineeringModule.UNIT_HEAD_STATUS_MAP.get(status);
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
			
			TableColumn state = new TableColumn("state", app.getMessage("fms.facility.table.state"));
			//state.setFormat(new TableStringFormat(EngineeringModule.STATE_MAP));
			state.setFormat(new TableFormat(){
				public String format(Object obj){
					String status = (String)obj;	
					String state = null;
					if (!"".equals(status) || !status.equals(null)){						
						try{						
							state = (String) EngineeringModule.STATE_MAP.get(status);
						}catch(Exception e){}
					} else {
						state = " - ";
					}
					
					if(null == state)
						state = " - ";
					
					if(status.equals(EngineeringModule.STATE_ADHOC) || status.equals(EngineeringModule.STATE_LATE)){
						String color = "<font color='red'>"+state+"</font>";
						state = color;
					}
		    		return state;							
				}
			});
			addColumn(state);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);
			
			FMSDepartmentManager deptManager=(FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class);
			TableFilter tfDepartment = new TableFilter("department");
			sbDepartment=new SelectBox("sbDepartment");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",app.getMessage("fms.facility.table.allDepartment"));
			try {
				typeMap.putAll(deptManager.getFMSDepartments());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.getMessage(), e);
			}
			sbDepartment.setOptionMap(typeMap);
			tfDepartment.setWidget(sbDepartment);
			addFilter(tfDepartment);
			
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
			statusMap.putAll(EngineeringModule.UNIT_HEAD_STATUS_MAP);
			sbStatus=new SelectBox("sbStatus");
			sbStatus.setOptionMap(statusMap);
			tfStatus.setWidget(sbStatus);
			addFilter(tfStatus);
			
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

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Date requiredDate = requiredField.getDate();
			String dept = WidgetUtil.getSbValue(sbDepartment);
			String status = WidgetUtil.getSbValue(sbStatus);
			UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			String userId = Application.getInstance().getCurrentUser().getId();
			Collection col = new ArrayList();
			Date requiredDateTo = requiredFieldTo.getDate();
			try {
				//return module.getHOURequest(searchBy, status, dept, getSort(), isDesc(), getStart(), getRows());
				//col = module.getMyHOURequest(searchBy, requiredDate, status, dept, userId, getSort(), isDesc(), getStart(), getRows());
				col = module.getMyHOURequestList(requiredDateTo, searchBy, requiredDate, status, dept, userId, getSort(), isDesc(), getStart(), getRows());
				//count = col.size();
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
			return col;
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			Date requiredDate = requiredField.getDate();
			String dept = WidgetUtil.getSbValue(sbDepartment);
			String status = WidgetUtil.getSbValue(sbStatus);
			String userId = Application.getInstance().getCurrentUser().getId();
			UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			Date requiredDateTo = requiredFieldTo.getDate();
//			try {
				//return module.countMyHOURequest(searchBy, status, dept, userId, getSort(), isDesc());
				//return module.countMyHOURequest();
				return module.countHOURequestList(requiredDateTo, searchBy, requiredDate, status, dept, userId);
//			} catch (DaoException e) {
//				Log.getLog(getClass()).error(e.toString());
//				return 0;
//			}
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