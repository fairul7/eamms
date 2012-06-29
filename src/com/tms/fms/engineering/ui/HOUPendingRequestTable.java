package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.engineering.model.Service;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.util.WidgetUtil;

public class HOUPendingRequestTable extends Table {
	
	//protected SelectBox sbRequestType;
	//protected SelectBox sbStatus;
	protected SelectBox sbDepartment;
	protected DatePopupField requiredField;
	protected int count;
	
	public DatePopupField requiredFieldTo;
	
	public HOUPendingRequestTable() {
	}

	public HOUPendingRequestTable(String s) {
		super(s);
	}

	public void onRequest(Event event) {
		setModel(new HOUPendingRequestTableModel());
		Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/engineering/houPendingRequestTemplate");
		setCurrentPage(1);
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		setModel(new HOUPendingRequestTableModel());
		setWidth("100%");
		
		Form filterTemplate = super.getFilterForm();
		filterTemplate.setTemplate("fms/engineering/houPendingRequestTemplate");
	}

	class HOUPendingRequestTableModel extends TableModel {
		HOUPendingRequestTableModel() {
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
			addColumn(requiredFrom);
			
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
			
			/*TableColumn status = new TableColumn("status", app.getMessage("fms.facility.table.status"));
			status.setFormat(new TableStringFormat(EngineeringModule.STATUS_MAP));
			addColumn(status);*/
			
			TableColumn indicator = new TableColumn("requestId", "");
			indicator.setFormat(new TableFormat(){
				public String format(Object obj){
					String requestId = obj.toString();	
					String indicator="";
					UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
					Collection list = module.getManpowerDetails(requestId, Application.getInstance().getCurrentUser().getId());
					boolean incomplete = false;
					UnitHeadModule uModule=(UnitHeadModule)Application.getInstance().getModule(UnitHeadModule.class);
					if(list!=null && list.size()>0){
						for (Iterator iterator = list.iterator(); iterator.hasNext();) {
							Map mp = (Map) iterator.next();
								if(mp.get("status").toString().equals("N")){
									incomplete=true;
									break;
								}
						}
						
						if(!incomplete){
							indicator="<span align=\"center\"><img align=\"center\" src=\"/ekms/images/icn_check.png\"></span>";
						}else{
							indicator="<span align=\"center\"><img align=\"center\" src=\"/ekms/images/icn_delete.png\"></span>";
						}
					} else {
						EngineeringDao eDao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
						Collection services = new ArrayList();
						try {
							services = eDao.getRequestServices(requestId);

							if (services != null && services.size() > 0) {
								for (Iterator iterator = services.iterator(); iterator.hasNext();) {
									Service object = (Service) iterator.next();
									if (object.getServiceId().equals("4")) {
										incomplete = true;
										break;
									}
								}
								if (incomplete) {
									indicator = "<span align=\"center\"><img align=\"center\" src=\"/ekms/images/icn_delete.png\"></span>";
								} else {
									indicator = "";
								}
							} else {
								indicator = "<span align=\"center\"><img align=\"center\" src=\"/ekms/images/icn_delete.png\"></span>";
							}
						} catch (DaoException e) {
							Log.getLog(getClass()).error("Unable to get service type for request");
						}
					}
		    		return indicator;							
				}
			});
			addColumn(indicator);

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
			
			Label dateLbl = new Label("dateLbl", "<b>" + Application.getInstance().getMessage("fms.facility.label.requiredDate") +
			Application.getInstance().getMessage("general.from") + "</b>");
			dateLbl.setAlign("right");
			addChild(dateLbl);
			
			requiredField = new DatePopupField("requiredDate");
			requiredField.setOptional(true);

			TableFilter requiredDate = new TableFilter("requiredDate");
			requiredDate.setWidget(requiredField);
			addFilter(requiredDate);
			
/*			TableFilter tfRequestType = new TableFilter("requestType");
			sbRequestType=new SelectBox("sbRequestType");
			SequencedHashMap typeMap=new SequencedHashMap();
			typeMap.put("-1",app.getMessage("fms.request.label.requestType"));
			typeMap.putAll(EngineeringModule.REQUEST_TYPE_MAP);
			sbRequestType.setOptionMap(typeMap);
			tfRequestType.setWidget(sbRequestType);
			addFilter(tfRequestType);
			
			TableFilter tfStatus = new TableFilter("status");
			SequencedHashMap statusMap=new SequencedHashMap();
			statusMap.put("-1",app.getMessage("fms.facility.table.status"));
			statusMap.putAll(EngineeringModule.STATUS_MAP);
			sbStatus=new SelectBox("sbStatus");
			sbStatus.setOptionMap(statusMap);
			tfStatus.setWidget(sbStatus);
			addFilter(tfStatus);
*/
			
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
			UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			String userId = Application.getInstance().getCurrentUser().getId();
			Collection col = new ArrayList();
			
			Date requiredDateTo = requiredFieldTo.getDate();
			try {
				//return module.getHOURequest(searchBy, EngineeringModule.ASSIGNMENT_STATUS, dept, getSort(), isDesc(), getStart(), getRows());
				//col = module.getMyHOURequest(searchBy, requiredDate, EngineeringModule.ASSIGNMENT_STATUS, dept, userId, getSort(), isDesc(), getStart(), getRows());
				col = module.getMyHOURequestList(requiredDateTo, searchBy, requiredDate, EngineeringModule.ASSIGNMENT_STATUS, dept, userId, getSort(), isDesc(), getStart(), getRows());
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
			String userId = Application.getInstance().getCurrentUser().getId();
			UnitHeadModule module = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			
			Date requiredDateTo = requiredFieldTo.getDate();
//			try {
				//return module.countMyHOURequest(searchBy, EngineeringModule.ASSIGNMENT_STATUS, dept, userId, getSort(), isDesc());
				//return module.countMyHOURequest();
				return module.countHOURequestList(requiredDateTo, searchBy, requiredDate, EngineeringModule.ASSIGNMENT_STATUS, dept, userId);
//			} catch (DaoException e) {
//				Log.getLog(getClass()).error(e.toString());
//				return 0;
//			}
			//return count;
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