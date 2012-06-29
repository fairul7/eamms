package com.tms.fms.engineering.ui;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.model.DaoException;
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

import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.facility.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.util.WidgetUtil;

public class RequestTable extends Table {
	
	protected SelectBox sbRequestType;
	protected SelectBox sbStatus;

	public RequestTable() {
	}

	public RequestTable(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new RequestTableModel());
		setWidth("100%");
	}
	public void onRequest(Event event) {
		setModel(new RequestTableModel());
	}

	class RequestTableModel extends TableModel {
		RequestTableModel() {
			removeChildren();
			Application app = Application.getInstance();
			addAction(new TableAction("add", app.getMessage("fms.facility.add")));
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
			//status.setFormat(new TableStringFormat(EngineeringModule.STATUS_MAP));
			status.setFormat(new TableFormat(){
				public String format(Object obj){
					String status = (String)obj;	
					String state = null;
					if (!"".equals(status) || !status.equals(null)){						
						try{						
							state = (String) EngineeringModule.STATUS_MAP.get(status);
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
			
			TableFilter tfRequestType = new TableFilter("requestType");
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

		}

		public Collection getTableRows() {
			String searchBy = "";
			String reqType="";
			String status="";
			searchBy = (String) getFilterValue("searchBy");
			reqType = WidgetUtil.getSbValue(sbRequestType);
			status=WidgetUtil.getSbValue(sbStatus);
			EngineeringModule module = (EngineeringModule) Application.getInstance().getModule(EngineeringModule.class);
			try {
				return module.getRequest(searchBy,reqType,status, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			String reqType="";
			String status="";
			searchBy = (String) getFilterValue("searchBy");
			reqType = WidgetUtil.getSbValue(sbRequestType);
			status=WidgetUtil.getSbValue(sbStatus);

			EngineeringDao dao = (EngineeringDao) Application.getInstance().getModule(EngineeringModule.class).getDao();
			try {
				return dao.selectRequestCount(searchBy,reqType,status);
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		/*public String getTableRowKey() {
				return "requestId";
		}*/

		public Forward processAction(Event event, String action,String[] selectedKeys) {
			if ("add".equals(action)){
				return new Forward("ADD");
			}
			/*else if ("delete".equals(action)) {
				try {
					for (int i = 0; i < selectedKeys.length; i++){
						module.deleteWorkingProfile(selectedKeys[i]);
					}
					
				} catch (Exception e) {
					Log.getLog(getClass()).error(e.toString());
					return new Forward("NOTDELETED");
				}
			}*/
			return super.processAction(event, action, selectedKeys);
			
		}
	}

}
