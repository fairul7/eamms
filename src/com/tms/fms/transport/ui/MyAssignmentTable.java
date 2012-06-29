package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.collab.resourcemanager.model.ResourceManager;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.AssignmentObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.util.WidgetUtil;

public class MyAssignmentTable extends Table
{
	private Collection col = null;
	private boolean displayDeleted = false;
	private Map cmap;
	boolean approvePermission = false;
	int totalRows = 0;


	TableColumn statusColumn;
	Label lbldateFrom;
	Label lbldateTo;
	private SelectBox deptList;
	private SelectBox statusIsActive;

	DeptTableModel tm ;
	String show = null;

	public void init()
	{
		super.init();
		tm = new DeptTableModel();
		setModel(tm);
		setWidth("100%");
		setNumbering(true);
	}

	public void onRequest(Event event)
	{
		super.onRequest(event);
		show = event.getRequest().getParameter("show");		
	}

	public class DeptTableModel extends TableModel{

		public DeptTableModel() {
			//name,description,HOD,status
			Application application = Application.getInstance();

			TableColumn deleteColumn = null;
			TableColumn reqColumn = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.assgId","Assg Id"),true);
			reqColumn.setUrl("");
			reqColumn.setUrlParam("id");
			addColumn(reqColumn);

			TableColumn progTab = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.label.transport.requestTitle","Program"),true);           
			addColumn(progTab);

			TableColumn servTab = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.label.transport.serviceType","Program"),true);           
			servTab.setFormat(new TableFormat(){
				public String format(Object obj){

					String type = "Transport";

					return type;		

				}
			});

			addColumn(servTab);

			TableColumn timeNdate = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.datetimeRequired","Require Time"),true);            
			timeNdate.setFormat(new TableFormat(){
				public String format(Object obj){		


					TransportRequest tr = new TransportRequest();            	        
					TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
					String timeDate = "";

					if(!(obj == null || "".equals(obj))){
						String assId = (String) obj;	            	        

						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						SimpleDateFormat stf = new SimpleDateFormat("k:mm");	            	        

						try{
							//TR = TM.selectTransportAssignmentByAssignmentId(assId);
							tr = tm.getTransportAssignmentByAssgId(assId);

							String dateStart = sdf.format(tr.getStartDate());
							String dateEnd = sdf.format(tr.getEndDate());

							String timeStart = stf.format(tr.getStartDate());
							String timeEnd = stf.format(tr.getEndDate());

							timeDate = dateStart + " [" + timeStart+"] - " + dateEnd + " [" + timeEnd+"]";
						}catch(Exception er){}
					}
					return timeDate;							
				}
			});
			addColumn(timeNdate);        


			TableColumn statusTab = new TableColumn("status",Application.getInstance().getMessage("fms.label.status","Status"),true);            
			statusTab.setFormat(new TableFormat(){
				public String format(Object obj){

					String status = (String)obj;					
					SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);

					if(status == null || "".equals(status))
						status = tm.selectStatus(sm.NEW_STATUS);
					else
						status = tm.selectStatus(status);

					return status;				

				}
			});
			addColumn(statusTab);  		

			SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
			TableFilter deptFilter = new TableFilter("deptFilter");
			addFilter(deptFilter);

			TableFilter filterStatus = new TableFilter("filterStatus");
			statusIsActive = new SelectBox("reqStatus");
			TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);

			statusIsActive.addOption("-1","-- All Status --");
			statusIsActive.addOption(sm.NEW_STATUS, TM.selectStatus(sm.NEW_STATUS));			
			statusIsActive.addOption(sm.UNFULFILLED_STATUS , TM.selectStatus(sm.UNFULFILLED_STATUS));	
			statusIsActive.addOption(sm.COMPLETED_STATUS , TM.selectStatus(sm.COMPLETED_STATUS));	


			filterStatus.setWidget(statusIsActive);
			addFilter(filterStatus);

		}

		public Collection getTableRows() {
			try{
				//String cat = null;

				//List sb = (List)getFilterValue("filterStatus");
				String selected = WidgetUtil.getSbValue(statusIsActive);


				boolean showPrivate =true;
				//                if(sb!=null&&sb.size()>0)
				//                    selected = (String)sb.get(0);
				if(selected!=null && selected.equals("-1"))
					selected = null;


				//for assignment
				Date dateAssignment = getDateToEndTime(new Date()); 
				String userId =  getWidgetManager().getUser().getId();

				Calendar calAssignment = Calendar.getInstance();
				calAssignment.setTime(dateAssignment);
				calAssignment.add(Calendar.DATE, 2);		//normal user is restricted to view < 2day assignment
				dateAssignment = calAssignment.getTime();

				//get HOD permission 
				Application app = Application.getInstance();
				String department = null;
				boolean transApproval = false;
				FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
				try{
					department = FRM.getUserDepartment(userId);
				}catch(Exception e){}
				FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);                	                	
				transApproval = FDM.userIsHOD(userId, department);	    
				if(transApproval){            	    	
					//if hod can view 30days assignment        	    	
					Calendar end = Calendar.getInstance();
					end.setTime(dateAssignment);    		            		        
					int lastDate = end.getActualMaximum(Calendar.DATE);
					int now = end.get(Calendar.DAY_OF_MONTH);
					int addDays = lastDate - now;
					end.add(Calendar.DATE, addDays);
					dateAssignment = end.getTime();	    	
				}

				TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);

				totalRows = tm.getMyAssignment((String)getFilterValue("deptFilter"), selected, userId, dateAssignment, getSort(),isDesc(), 0, -1).size();
				col = tm.getMyAssignment((String)getFilterValue("deptFilter"),selected, userId, dateAssignment, getSort(),isDesc(),getStart(), getRows());

			}catch(Exception e){
				Log.getLog(getClass()).error(e);
			}
			return col;
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys)
		{

			//problem when select dept for active/inactive

			FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);

			if("add".equals(action))
				return new Forward("Add New");

			if("active".equals(action)){

				int pageRequired=0;

				for(int i=0;i<selectedKeys.length;i++)
				{
					Log.getLog(getClass()).info(selectedKeys[i]);
					statusDept(selectedKeys[i],manager.ACTIVE_DEPT);

				}
			}

			if("inactive".equals(action)){                 
				int pageRequired=0;                 
				for(int i=0;i<selectedKeys.length;i++)
				{
					Log.getLog(getClass()).info(selectedKeys[i]);
					statusDept(selectedKeys[i],manager.INACTIVE_DEPT);

				}
			}

			return super.processAction(evt,action,selectedKeys);
		}

		public void statusDept(String deptId,String status){

			FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
			try{
				manager.updateDeptStatus(deptId, status);
			}catch(Exception e){
				Log.getLog(getClass()).error(e);
			}
		}

		protected Date getDateToEndTime(Date xDate){

			Calendar endTime = Calendar.getInstance();
			endTime.setTime(xDate);
			endTime.set(Calendar.HOUR_OF_DAY, 23);
			endTime.set(Calendar.MINUTE, 59);
			endTime.set(Calendar.SECOND, 59); 
			xDate = endTime.getTime();

			return xDate;      
		}

		public String getTableRowKey()
		{
			return "id";
		}

		public int getTotalRowCount()
		{
			return totalRows;
		}
	}

	public Label getLbldateFrom() {
		return lbldateFrom;
	}

	public void setLbldateFrom(Label lbldateFrom) {
		this.lbldateFrom = lbldateFrom;
	}

	public Label getLbldateTo() {
		return lbldateTo;
	}

	public void setLbldateTo(Label lbldateTo) {
		this.lbldateTo = lbldateTo;
	}
	
}
