package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axis.collections.SequencedHashMap;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
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

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.util.WidgetUtil;

public class NewRequestListing extends Table
{
	protected DatePopupField requiredField;
	protected DatePopupField requiredFieldTo;
	protected SelectBox sbRequestType;
	protected SelectBox sbStatus;
	
    DeptTableModel tm ;
    String show = null;
    public void init()
    {
        super.init();
        tm = new DeptTableModel();
        setModel(tm);
        setWidth("100%");
        setNumbering(true);
        Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/transport/incomingTransportFilter");
    }
   
    public void onRequest(Event event)
    {
        super.onRequest(event);
        setModel(new DeptTableModel());
        show = event.getRequest().getParameter("show");		
        Form filterform = super.getFilterForm();
		filterform.setTemplate("fms/transport/incomingTransportFilter");
    }

    public class DeptTableModel extends TableModel{
        private Collection col = null;
        private boolean displayDeleted = false;
        private Map cmap;
        boolean approvePermission = false;
        int totalRows = 0;
        
       
        TableColumn statusColumn;
        Label lbldateFrom;
        Label lbldateTo;
        private SelectBox deptList;
        
               
        public DeptTableModel()
        {
        	Application application = Application.getInstance();
        	
            TableColumn deleteColumn = null;
            TableColumn reqColumn = new TableColumn("id",Application.getInstance().getMessage("fms.tran.requestId","Request Id"),true);
            reqColumn.setUrl("");
            reqColumn.setUrlParam("id");
            addColumn(reqColumn);
            
            TableColumn progTab = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.tran.requestProgram","Program"),true);
            addColumn(progTab);
            
            TableColumn deptTab = new TableColumn("requestBy",Application.getInstance().getMessage("fms.label.department","Dept"), false);
            deptTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
					Application app = Application.getInstance();					
					String userid = (String)obj;					
					String deptId = "";
					String deptname = "";
					FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
					FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);
					
					try{
						deptId = FRM.getUserDepartment(userid);
						FMSDepartment FD = FDM.getselectFMSDepartment(deptId);
						deptname = FD.getName();
			    		
					}catch(Exception e){
						
					}
		    		
		    		return deptname;		
					
				}
			});
            addColumn(deptTab);
            
            
            TableColumn reqTab = new TableColumn("requestDate",Application.getInstance().getMessage("fms.tran.requestDate","Request"),true);
            reqTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
					Application app = Application.getInstance();
					//SecurityService service = (SecurityService) app.getService(SecurityService.class);					
					Date date = (Date)obj;		
					String daterequest = "";
					
					////
    				try{					
        				 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        				 daterequest = sdf.format(date);
        			     
        				 }catch(Exception er){
        					 Log.getLog(getClass()).error("Error converting SimpleDateFormat:"+er);
        				 }
    				////
					
		    		return daterequest;		
					
				}
			});
            
            addColumn(reqTab);
            
            TableColumn reqTab2 = new TableColumn("requiredDateTime",Application.getInstance().getMessage("fms.facility.label.requiredDate"));
            addColumn(reqTab2);
            ////			
            TableColumn vehicleTab = new TableColumn("id",Application.getInstance().getMessage("fms.tran.requestVehicle"), false);
            vehicleTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					
					Application app = Application.getInstance();
					SecurityService service = (SecurityService) app.getService(SecurityService.class);					
					String id = (String)obj;
					
					String vehicle = "";
					
					
					try{
						TransportModule tran = (TransportModule) Application.getInstance().getModule(TransportModule.class);        		
			    		Collection vehicles = tran.getVehicles(id);
			    		for(Iterator it = vehicles.iterator(); it.hasNext(); ){
			    			VehicleRequest vr = (VehicleRequest) it.next();
			    			String name = vr.getName();		
			    			//vehicle += name +", ";		    
			    			if("".equals(vehicle)){
			    				vehicle += name;
			    			} else {
			    				vehicle += ", " + name;
			    			}
			    		}
					}catch(Exception e){
						
					}
		    		
		    		return vehicle;		
					
				}
			});
			addColumn(vehicleTab);            
            ////
			
			
	
            TableColumn statusTab = new TableColumn("status",Application.getInstance().getMessage("fms.tran.setup.status"),true);
            statusTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					String statusNo = (String)obj;					
					String status = "";
					
					
					if(statusNo.equals(SetupModule.REJECTED_STATUS)){
						String color = "<font color='red'>"+status+"</font>";
						status = color;
					}
					TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);					
					status = TM.selectStatus(statusNo);					
					
					
					
		    		return status;		
					
				}
			});
			addColumn(statusTab);            
			
			TableColumn stateTab = new TableColumn("statusRequest",Application.getInstance().getMessage("fms.tran.setup.state"),true);
			stateTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					String status = (String)obj;				
					String state = null;	
					
					try{
					TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);					
					state = TM.selectStatus(status);
					}catch(Exception e){}
					
					if(null == state)
						state = " - ";
					
					if(status.equals(SetupModule.LATE_STATUS) || status.equals(SetupModule.ADHOC_STATUS)){
						String color = "<font color='red'>"+state+"</font>";
						state = color;
					}
					
		    		return state;							
				}
			});
			addColumn(stateTab); 
            
            
            
			SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
            TableFilter deptFilter = new TableFilter("deptFilter");
            addFilter(deptFilter);
            
            Calendar now = Calendar.getInstance();
            requiredField = new DatePopupField("requiredDate");
			requiredField.setOptional(true);
			requiredField.setDate(now.getTime());

			TableFilter requiredDate = new TableFilter("requiredDate");
			requiredDate.setWidget(requiredField);
			addFilter(requiredDate);
			
			now.add(Calendar.DATE, 7);
			requiredFieldTo = new DatePopupField("requiredDateTo");
			requiredFieldTo.setOptional(true);
			requiredFieldTo.setDate(now.getTime());

			TableFilter requiredDateTo = new TableFilter("requiredDateTo");
			requiredDateTo.setWidget(requiredFieldTo);
			addFilter(requiredDateTo);
			
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
			
			 TableFilter filterStatus = new TableFilter("filterStatus");
			 	sbStatus=new SelectBox("sbStatus");
	            TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
				
	            sbStatus.addOption("-1","-- All Status --");
	            sbStatus.addOption(sm.PROCESS_STATUS , TM.selectStatus(sm.PROCESS_STATUS));	
	            sbStatus.addOption(sm.REJECTED_STATUS , TM.selectStatus(sm.REJECTED_STATUS));
	            sbStatus.addOption(sm.ASSIGNED_STATUS , TM.selectStatus(sm.ASSIGNED_STATUS));
	            sbStatus.addOption(sm.OUTSOURCED_STATUS , TM.selectStatus(sm.OUTSOURCED_STATUS));
				sbStatus.setSelectedOption(sm.PROCESS_STATUS);
				
				filterStatus.setWidget(sbStatus);
				addFilter(filterStatus);
            
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
        
        public String getDepartment() {
			String returnValue = "-1";
			List lstDepartment = (List)getFilterValue("tfDepartment");
			if (lstDepartment.size() > 0) {returnValue = (String)lstDepartment.get(0);}
			return returnValue;
		}
        
        public Collection getTableRows()
        {
            try{
                String selected = "S";		//default to pending
                
                String department = "";
                String userId = getWidgetManager().getUser().getId();          
                TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
                Date requiredDate = requiredField.getDate();
    			Date requiredDateTo = requiredFieldTo.getDate();
    			requiredDate.setHours(0);
    			requiredDate.setMinutes(0);
    			requiredDate.setSeconds(0);
    			
    			String status = "";
    			status=WidgetUtil.getSbValue(sbStatus);
                
                col = tm.getAllTranRequest(requiredDate, requiredDateTo, status, (String)getFilterValue("deptFilter"),selected, getDepartment(), getSort(),null,isDesc(), getStart(), getRows());
                totalRows = tm.getAllCountTranRequest(requiredDate, requiredDateTo,  status, (String)getFilterValue("deptFilter"), selected, getDepartment(), null);
                
                
            }catch(Exception e){
                Log.getLog(getClass()).error(e);
            }
            return col;
        }

        public String getTableRowKey()
        {
            return "id";
        }

        public int getTotalRowCount()
        {
            return totalRows;
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
}
