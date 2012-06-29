package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
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

import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.VehicleRequest;

public class TransAdminListing extends Table
{
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
            
            
            TableColumn reqTab = new TableColumn("updatedDate",Application.getInstance().getMessage("fms.tran.requestDate","Request"),true);
            reqTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
            		String daterequest = "";
					if(!(obj == null || "".equals(obj))){
					Date date = (Date)obj;		
					
					
					////
    				try{					
        				 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        				 daterequest = sdf.format(date);
        			     
        				 }catch(Exception er){
        					 Log.getLog(getClass()).error("Error converting SimpleDateFormat:"+er);
        				 }
    				////
					}
		    		return daterequest;		
					
				}
			});
            
            addColumn(reqTab);
            
            ////			
            TableColumn vehicleTab = new TableColumn("id",Application.getInstance().getMessage("fms.tran.requestVehicle"),true);
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
	
			TableColumn statusTab = new TableColumn("status",Application.getInstance().getMessage("fms.tran.setup.status"),true);
            statusTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					String statusNo = (String)obj;					
					String status = "";
					
					TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);					
					status = TM.selectStatus(statusNo);					
					
					
					
		    		return status;		
					
				}
			});
			addColumn(statusTab);               
            
            
            
			SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
            TableFilter deptFilter = new TableFilter("deptFilter");
            addFilter(deptFilter);
            
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
        
        public Collection getTableRows()
        {
            try{    
                
                String selected = "P";		//default to pending
                
                //filter by dept
                String department = "";
                FMSRegisterManager FRM = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
                String userId = getWidgetManager().getUser().getId();
            	try{
            		department = FRM.getUserDepartment(userId);
            	}catch(Exception e){
            		Log.getLog(getClass()).error(e.toString(), e);
            	}
            	
                TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
                                
                col = tm.getAllTranRequest((String)getFilterValue("deptFilter"),selected, department, getSort(),null,isDesc(),getStart(),getRows());
                totalRows = tm.getAllCountTranRequest((String)getFilterValue("deptFilter"), selected, department, null);
                
                
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
