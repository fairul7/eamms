package com.tms.collab.isr.report.ui;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.stdui.DatePopupField;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;

import org.apache.commons.collections.SequencedHashMap;

import com.tms.collab.isr.report.model.ReportModel;
import com.tms.collab.isr.setting.model.ConfigDetailObject;
import com.tms.collab.isr.setting.model.ConfigModel;
import com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject;
import com.tms.hr.orgChart.model.OrgChartHandler;

public class PriorityReportTable extends Table{
	
	private Date fromDate;
	private Date toDate;
	
	public PriorityReportTable(){
		
	}
	
	public PriorityReportTable(String name){
		super(name);
	}
	
	public void init(){
		super.init();
        setPageSize(50);
        setModel(new PriorityReportTableModel());
        setWidth("100%");
	}
	
	public class PriorityReportTableModel extends TableModel{
		
		private DatePopupField dfFromDate;
		private DatePopupField dfToDate;
		private SelectBox sbPriority;
		private SelectBox sbRequestingDept;
		private SelectBox sbReceivingDept;
		
		public PriorityReportTableModel(){
			
			Application app = Application.getInstance();
			
			TableColumn reqDept = new TableColumn("reqDept", app.getMessage("isr.label.reqDept"));
			addColumn(reqDept);
			
			TableColumn recDept = new TableColumn("recDept", app.getMessage("isr.label.recDept"));
			addColumn(recDept);
			
			TableColumn status = new TableColumn("priorityByAdmin", app.getMessage("isr.label.adminPriority"));
			status.setUrlParam("reportLink");
			addColumn(status);
			
			TableColumn noOfReq = new TableColumn("noOfReq", app.getMessage("isr.label.noOfReq"));
			addColumn(noOfReq);
			
			dfFromDate = new DatePopupField("fromDate");
			dfFromDate.setOptional(true);
			dfFromDate.setSize("7");
			TableFilter fromDateFilter = new TableFilter("fromDateFilter");
			fromDateFilter.setWidget(dfFromDate);
			addFilter(fromDateFilter);
			
			dfToDate = new DatePopupField("toDate");
			dfToDate.setOptional(true);
			dfToDate.setSize("7");
			TableFilter toDateFilter = new TableFilter("toDateFilter");
			toDateFilter.setWidget(dfToDate);
			addFilter(toDateFilter);
			
			Calendar now = Calendar.getInstance();
			dfToDate.setDate(now.getTime());
			toDate = now.getTime();
			
			now.add(Calendar.MONTH, -1);
			dfFromDate.setDate(now.getTime());
			fromDate = now.getTime();
			
			sbPriority = new SelectBox("sbPriority");
			sbPriority.setOptionMap(getPriorityOptionsMap());
			TableFilter statusFilter = new TableFilter("priorityFilter");
			statusFilter.setWidget(sbPriority);
			addFilter(statusFilter);
			
			sbRequestingDept = new SelectBox("sbRequestingDept");
			sbRequestingDept.setOptionMap(getAttendingDeptOptionsMap());
			TableFilter requestingDeptFilter = new TableFilter("requestingDeptFilter");
			requestingDeptFilter.setWidget(sbRequestingDept);
			addFilter(requestingDeptFilter);
			
			sbReceivingDept = new SelectBox("sbReceivingDept");
			sbReceivingDept.setOptionMap(getAttendingDeptOptionsMap());
			TableFilter receivingDeptFilter = new TableFilter("receivingDeptFilter");
			receivingDeptFilter.setWidget(sbReceivingDept);
			addFilter(receivingDeptFilter);
			
			addAction(new TableAction("print", "Print"));
            //addAction(new TableAction("exportCsv", "Export to CSV"));
			
		}
		
		public Map getPriorityOptionsMap() {
			Application application = Application.getInstance();
			ConfigModel model = (ConfigModel)application.getModule(ConfigModel.class);
			ConfigDetailObject config = new ConfigDetailObject();
			Map optionsMap = new SequencedHashMap();
			Collection cols = model.getConfigDetailsByType(ConfigDetailObject.PRIORITY, null);
			
			optionsMap.put("", "---All Priority---");
			for(Iterator i=cols.iterator(); i.hasNext();) {
				config = (ConfigDetailObject) i.next();
				if(config.getConfigDetailName() != null && !"".equals(config.getConfigDetailName())) {
					optionsMap.put(config.getConfigDetailName(), config.getConfigDetailName());
				}
			}
			
			return optionsMap;
	    }
		
		private Map getAttendingDeptOptionsMap() {
	    	Map optionsMap = new SequencedHashMap();
	    	
	    	Application application = Application.getInstance();
	    	OrgChartHandler orgChartModel = (OrgChartHandler) application.getModule(OrgChartHandler.class);
	    	
	    	Collection deptCols = orgChartModel.selectDepartmentCountryAssociativity(null, null, null, "countryDesc, deptDesc", false, 0, -1);
	    	optionsMap.put("", "---" + application.getMessage("isr.label.allDepartment") + "---");
	    	for(Iterator i=deptCols.iterator(); i.hasNext();) {
	    		DepartmentCountryAssociativityObject obj = (DepartmentCountryAssociativityObject) i.next();
	    		optionsMap.put(obj.getAssociativityId(), 
    					obj.getCountryDesc() + " - " + obj.getDeptDesc());
	    	}
	    	
	    	return optionsMap;
	    }
		
		public int getTotalRowCount(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			
			String fromDate = (String) getFilterValue("fromDateFilter");
			String toDate = (String) getFilterValue("toDateFilter");
			
			String priority = new String();
			SelectBox tempPriority = (SelectBox) getFilter("priorityFilter").getWidget();
			List priorityList = (List) tempPriority.getValue();
            if (priorityList.size() > 0) {
            	priority = (String) priorityList.get(0);
            } else {
            	priority = "";
            }
            
			String reqDept = new String();
			SelectBox tempReqDept = (SelectBox) getFilter("requestingDeptFilter").getWidget();
			List reqDeptList = (List) tempReqDept.getValue();
            if (reqDeptList.size() > 0) {
            	reqDept = (String) reqDeptList.get(0);
            } else {
            	reqDept = "";
            }
			
			String recDept = new String();
			SelectBox tempRecDept = (SelectBox) getFilter("receivingDeptFilter").getWidget();
			List recDeptList = (List) tempRecDept.getValue();
            if (recDeptList.size() > 0) {
            	recDept = (String) recDeptList.get(0);
            } else {
            	recDept = "";
            }
			
			return model.getPriorityReportListingCount(fromDate, toDate, priority, reqDept, recDept);
		}
		
		public Collection getTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			
			String tempFromDate = (String) getFilterValue("fromDateFilter");
			String tempToDate = (String) getFilterValue("toDateFilter");
			
			String priority = new String();
			SelectBox tempPriority = (SelectBox) getFilter("priorityFilter").getWidget();
			List priorityList = (List) tempPriority.getValue();
            if (priorityList.size() > 0) {
            	priority = (String) priorityList.get(0);
            } else {
            	priority = "";
            }
            
			String reqDept = new String();
			SelectBox tempReqDept = (SelectBox) getFilter("requestingDeptFilter").getWidget();
			List reqDeptList = (List) tempReqDept.getValue();
            if (reqDeptList.size() > 0) {
            	reqDept = (String) reqDeptList.get(0);
            } else {
            	reqDept = "";
            }
			
			String recDept = new String();
			SelectBox tempRecDept = (SelectBox) getFilter("receivingDeptFilter").getWidget();
			List recDeptList = (List) tempRecDept.getValue();
            if (recDeptList.size() > 0) {
            	recDept = (String) recDeptList.get(0);
            } else {
            	recDept = "";
            }
			
            DatePopupField tempFilterFromDate = (DatePopupField)getFilter("fromDateFilter").getWidget();
            DatePopupField tempFilterToDate = (DatePopupField)getFilter("toDateFilter").getWidget();
			
			fromDate = tempFilterFromDate.getDate();
			toDate = tempFilterToDate.getDate();
            
            Application.getThreadRequest().getSession().setAttribute("reportData", getAllTableRows());
            Application.getThreadRequest().getSession().setAttribute("reportFromDate", fromDate);
            Application.getThreadRequest().getSession().setAttribute("reportToDate", toDate);
            Application.getThreadRequest().getSession().setAttribute("reportReqDept", reqDept);
            Application.getThreadRequest().getSession().setAttribute("reportRecDept", recDept);
            
			return model.getPriorityReportListing(tempFromDate, tempToDate, priority, reqDept, recDept, getSort(), isDesc(), getStart(), getRows()); 
		}
		
		public Collection getAllTableRows(){
			ReportModel model = (ReportModel)Application.getInstance().getModule(ReportModel.class);
			
			String fromDate = (String) getFilterValue("fromDateFilter");
			String toDate = (String) getFilterValue("toDateFilter");
			
			String status = new String();
			SelectBox tempStatus = (SelectBox) getFilter("priorityFilter").getWidget();
			List statusList = (List) tempStatus.getValue();
            if (statusList.size() > 0) {
            	status = (String) statusList.get(0);
            } else {
            	status = "";
            }
            
			String reqDept = new String();
			SelectBox tempReqDept = (SelectBox) getFilter("requestingDeptFilter").getWidget();
			List reqDeptList = (List) tempReqDept.getValue();
            if (reqDeptList.size() > 0) {
            	reqDept = (String) reqDeptList.get(0);
            } else {
            	reqDept = "";
            }
			
			String recDept = new String();
			SelectBox tempRecDept = (SelectBox) getFilter("receivingDeptFilter").getWidget();
			List recDeptList = (List) tempRecDept.getValue();
            if (recDeptList.size() > 0) {
            	recDept = (String) recDeptList.get(0);
            } else {
            	recDept = "";
            }
			
			return model.getPriorityReportListing(fromDate, toDate, status, reqDept, recDept, getSort(), isDesc(), 0, -1); 
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			
			if(action.equalsIgnoreCase("print")){
				
				DatePopupField tempFilterFromDate = (DatePopupField)getFilter("fromDateFilter").getWidget();
				DatePopupField tempFilterToDate = (DatePopupField)getFilter("toDateFilter").getWidget();
				
				fromDate = tempFilterFromDate.getDate();
				toDate = tempFilterToDate.getDate();
				
				return new Forward("print");
			}
			/*else if(action.equalsIgnoreCase("exportCsv")){
				evt.getRequest().getSession().setAttribute("reportData", getAllTableRows());
				return new Forward("csv");
			}*/
			
			return new Forward("error");
		}
		
		public String getTableRowKey() {
            return "id";
        }
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getDefaultTemplate(){
		return "isr/priorityReport";
	}

}
