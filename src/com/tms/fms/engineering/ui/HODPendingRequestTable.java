package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.SetupModule;

public class HODPendingRequestTable extends Table {
	
	//protected SelectBox sbRequestType;
	//protected SelectBox sbStatus;

	public HODPendingRequestTable() {
	}

	public HODPendingRequestTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new HODPendingRequestTableModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new HODPendingRequestTableModel());
	}

	class HODPendingRequestTableModel extends TableModel {
		HODPendingRequestTableModel() {
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
			
			TableColumn requestedBy = new TableColumn("createdUserName", app.getMessage("fms.facility.table.requestedBy"));
			addColumn(requestedBy);	
			
			/*TableColumn status = new TableColumn("status", app.getMessage("fms.facility.table.status"));
			status.setFormat(new TableStringFormat(EngineeringModule.STATUS_MAP));
			addColumn(status);*/

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
			
			TableFilter tfStatus = new TableFilter("status");
			SequencedHashMap statusMap=new SequencedHashMap();
			statusMap.put("-1",app.getMessage("fms.facility.table.status"));
			statusMap.putAll(EngineeringModule.STATUS_MAP);
			sbStatus=new SelectBox("sbStatus");
			sbStatus.setOptionMap(statusMap);
			tfStatus.setWidget(sbStatus);
			addFilter(tfStatus);
*/
		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
			try {
				return module.getHODRequest(searchBy,getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			
			EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
			try {
				return dao.selectHODRequestCount(searchBy);
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
