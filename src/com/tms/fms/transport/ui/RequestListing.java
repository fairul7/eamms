package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.DatePopupField;
import kacang.stdui.Form;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.EngineeringDao;
import com.tms.fms.engineering.model.EngineeringModule;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.Status;
import com.tms.fms.transport.model.TransportDao;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleRequest;
import com.tms.fms.util.WidgetUtil;

public class RequestListing extends Table
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
        	//name,description,HOD,status
        	Application application = Application.getInstance();
        	
            TableColumn deleteColumn = null;
            TableColumn reqColumn = new TableColumn("id",Application.getInstance().getMessage("fms.tran.requestId","Request Id"),true);
            reqColumn.setUrl("");
            reqColumn.setUrlParam("id");
            addColumn(reqColumn);
            
            TableColumn progTab = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.tran.requestProgram","Program"),true);
            addColumn(progTab);
                        
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
            
            TableColumn purposeTab = new TableColumn("purpose",Application.getInstance().getMessage("fms.tran.requestPurpose","Request"),true);                       
            addColumn(purposeTab);
            
            TableColumn desTab = new TableColumn("destination",Application.getInstance().getMessage("fms.label.destination","Request"),true);                       
            addColumn(desTab);
            
            ////			
            TableColumn vehicleTab = new TableColumn("id",Application.getInstance().getMessage("fms.tran.requestVehicle"), false);
            vehicleTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					
					Application app = Application.getInstance();
					SecurityService service = (SecurityService) app.getService(SecurityService.class);					
					String id = (String)obj;
					
					String vehicle = "";
					//Log.getLog(getClass()).info(vehicle);
					
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
					TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
					String statusNo = (String)obj;					
					String status = "";
					
					status = tm.selectStatus(statusNo);
					
					if(statusNo.equals(SetupModule.REJECTED_STATUS)){
						String color = "<font color='red'>"+status+"</font>";
						status = color;
					}
					return status;		
					
				}
			});
			addColumn(statusTab);
			
			TableColumn userTab = new TableColumn("requestBy",Application.getInstance().getMessage("fms.label.requestedBy","Request By"),true);
            userTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
					Application app = Application.getInstance();
					//SecurityService service = (SecurityService) app.getService(SecurityService.class);					
					String userid = (String)obj;				
					String username = "";
					String deptId = "";
					String deptname = "";
					String userndept = "";
					FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
					FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);
					
					try{
						SecurityService service = (SecurityService) app.getService(SecurityService.class);	
						username = service.getUser(userid).getName();
			    		
						//dept
						deptId = FRM.getUserDepartment(userid);
						FMSDepartment FD = FDM.getselectFMSDepartment(deptId);
						deptname = FD.getName();
						userndept = username + "<BR/>(" + deptname + ")";
					}catch(Exception e){
						
					}
		    		
		    		return userndept;		
					
				}
			});
            addColumn(userTab);
			
			TableColumn picNameTab = new TableColumn("id",Application.getInstance().getMessage("fms.facility.table.pic"), false);
			picNameTab.setFormat(new TableFormat(){
				
				public String format(Object obj){					
					TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
					String reqid = (String)obj;		
					String PICname = "";
					
					Collection colltrail = new ArrayList();
					if(reqid != null || "".equals(reqid)){
						
						try{
							colltrail = tm.getStatusTrailByStatus(reqid, SetupModule.ASSIGNED_STATUS);						
						}catch(Exception er){}
					
					
						for(Iterator it = colltrail.iterator(); it.hasNext(); ){
							
							Status status = (Status) it.next();
							PICname = status.getCreatedBy();
						}						
					}
					
					return PICname;					
				}
			});
			addColumn(picNameTab);     
            
            
            
			SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
            TableFilter deptFilter = new TableFilter("deptFilter");
            addFilter(deptFilter);
            
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
			
			TableFilter tfDepartment = new TableFilter("tfDepartment");
			SelectBox sbDepartment = new SelectBox("sbDepartment");
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
            
            TableFilter filterStatus = new TableFilter("filterStatus");
		 	sbStatus=new SelectBox("sbStatus");
            TransportModule TM = (TransportModule) Application.getInstance().getModule(TransportModule.class);
			
            sbStatus.addOption("-1","-- All Status --");
            sbStatus.addOption(sm.PROCESS_STATUS , TM.selectStatus(sm.PROCESS_STATUS));	
            sbStatus.addOption(sm.REJECTED_STATUS , TM.selectStatus(sm.REJECTED_STATUS));
            sbStatus.addOption(sm.ASSIGNED_STATUS , TM.selectStatus(sm.ASSIGNED_STATUS));
            sbStatus.addOption(sm.OUTSOURCED_STATUS , TM.selectStatus(sm.OUTSOURCED_STATUS));
						
			
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
                	 //Log.getLog(getClass()).info(selectedKeys[i]);
                	 statusDept(selectedKeys[i],manager.ACTIVE_DEPT);
                     
                 }
        	 }
        	 
        	 if("inactive".equals(action)){                 
                 int pageRequired=0;                 
                 for(int i=0;i<selectedKeys.length;i++)
                 {
                	 //Log.getLog(getClass()).info(selectedKeys[i]);
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
        	String assignedBy = null;
            try{    
                List sb = (List)getFilterValue("filterStatus");
                String selected = "-1";		//default to pending
                

                boolean showPrivate =true;
                if(sb!=null&&sb.size()>0)
                    selected = (String)sb.get(0);
                if(selected!=null && selected.equals("-1"))
                    selected = null;
                
                      
                Date requiredDate = requiredField.getDate();
    			Date requiredDateTo = requiredFieldTo.getDate();
    			String status=WidgetUtil.getSbValue(sbStatus);
    			
                TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
                
                String userId = getWidgetManager().getUser().getId();               
                col = tm.getAllComingRequest(requiredDate, requiredDateTo, getDepartment(), (String)getFilterValue("deptFilter"),selected, getSort(),null,isDesc(), getStart(), getRows());                
                
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
        	TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
        	 List sb = (List)getFilterValue("filterStatus");
             String selected = "-1";		//default to pending
             

             boolean showPrivate =true;
             if(sb!=null&&sb.size()>0)
                 selected = (String)sb.get(0);
             if(selected!=null && selected.equals("-1"))
                 selected = null;
             
            Date requiredDate = requiredField.getDate();
 			Date requiredDateTo = requiredFieldTo.getDate();
 			String status=WidgetUtil.getSbValue(sbStatus);
 			
        	try{
        		return dao.selectCountAllComingRequest(requiredDate, requiredDateTo, getDepartment(), (String)getFilterValue("deptFilter"), selected, null);
        	}catch(Exception er){
        		Log.getLog(getClass()).error(er.toString());
        		return 0;
        	}
            //return totalRows;
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
