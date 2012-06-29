package com.tms.fms.transport.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.DriverVehicleObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;

public class AssignmentTable extends Table
{
    DeptTableModel tm ;
    String show = null;
    String requestId = null;
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
        //requestId = event.getRequest().getParameter("requestId");
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
            TableColumn reqColumn = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.assgId","Assignment Id"),true);
            reqColumn.setUrl("");
            reqColumn.setUrlParam("id");
            addColumn(reqColumn);
            
            TableColumn reqTab = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.assDateTime","Request"),true);
            reqTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
					Application app = Application.getInstance();
					TransportModule tm = (TransportModule)Application.getInstance().getModule(TransportModule.class);
					TransportRequest tr = new TransportRequest();
					String id = (String)obj;
					String requestDate = "";
					if(id != null){					
					String startdaterequest = "";
					String starttimerequest = "";
					String enddaterequest = "";
					String endtimerequest = "";
					 
					
					////
    				try{		
    					 
    					 Collection coll = tm.getAssignmentByAssignmentId(id);
    					 for(Iterator it = coll.iterator(); it.hasNext(); ){
    						 tr = (TransportRequest) it.next();
    					 }
    					 
        				 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        				 SimpleDateFormat stf = new SimpleDateFormat("k:mm");
        				 startdaterequest = sdf.format(tr.getStartDate());
        			     starttimerequest = "["+stf.format(tr.getStartDate())+"]";
        			     enddaterequest = sdf.format(tr.getEndDate());
        			     endtimerequest = "["+stf.format(tr.getEndDate())+"]";
        			           
        			     requestDate = startdaterequest + starttimerequest + " - " + enddaterequest + endtimerequest;
        				 }catch(Exception er){
        					 Log.getLog(getClass()).error("Error converting SimpleDateFormat:"+er);
        				 }
    				////
					}
		    		return requestDate;		
					
				}
			});
            
            addColumn(reqTab);
           
            
            TableColumn vehicleTab = new TableColumn("id",Application.getInstance().getMessage("fms.label.vehiclesAssigned","Program"),true);
            vehicleTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					Application app = Application.getInstance();
					String id = (String)obj;	
					String vehicles = "-";
					String vehicle = "";
					if(id != null){
						TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
						try{
							Collection coll = TM.getAssignmentVehicles(id);
							if(coll.size() > 0){
								vehicles = "";
								for(Iterator it = coll.iterator(); it.hasNext(); ){
									TransportRequest tr = (TransportRequest) it.next();
									vehicle = tr.getVehicle_num() +
																	
									" <a href=\"\" onClick=\"javascript:window.open('deleteVehicle.jsp?id="+id+"&vehicle_num="+tr.getVehicle_num()+"','approveRes','scrollbars=yes,resizable=yes,width=350,height=186');return false;\" onMouseOver=\"window.status='Approve';return (true);\">" + app.getMessage("scheduling.label.delete", "Delete") + "</a>" +
									"<br><br>";													
									
									vehicles += vehicle;
								}
							}
							
						}catch(Exception er){}
						
					}
		    		return vehicles;		
					
				}
			});
            addColumn(vehicleTab);
            
            
            TableColumn driverTab = new TableColumn("id",Application.getInstance().getMessage("fms.label.driverAssigned","Program"),true);
            driverTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
					Application app = Application.getInstance();
					String id = (String)obj;	
					String drivers = "-";
					String driver = "";
					String driverVehicle = "";
					if(id != null){
						TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
						try{
							Collection coll = TM.getDriverByAssignmentId(id);
							if(coll.size() > 0){
								drivers = "";
								for(Iterator it = coll.iterator(); it.hasNext(); ){
									ManpowerLeaveObject man = (ManpowerLeaveObject) it.next();
									DriverVehicleObject dvObject = TM.selectDriverVehicle(man.getManpowerId(), id);
																		
									if(dvObject != null)
										driverVehicle = " [ " + dvObject.getVehicle_num() + " ] &nbsp;&nbsp; ";
									else
										driverVehicle = " <a href=\"\" onClick=\"javascript:window.open('assignDriverVehicleForm.jsp?id="+id+"&userId="+man.getManpowerId()+"', 'approveRes','scrollbars=yes,resizable=yes,width=500,height=430');return false;\" onMouseOver=\"window.status='Approve';return (true);\">" + app.getMessage("fms.tran.assignToVehicle", "Assign to Vehicle") + "</a> &nbsp;&nbsp;";
									
									driver = security.getUser(man.getManpowerId()).getName() + driverVehicle + 
									" <a href=\"\" onClick=\"javascript:window.open('deleteUser.jsp?id="+id+"&userId="+man.getManpowerId()+"','approveRes','scrollbars=yes,resizable=yes,width=350,height=186');return false;\" onMouseOver=\"window.status='Approve';return (true);\">" + app.getMessage("scheduling.label.delete", "Delete") + "</a>" +
									"<br><br>";
									
									drivers += driver;
								}
							}
						}catch(Exception er){}
						
					}
		    		return drivers;		
					
				}
			});
            addColumn(driverTab);


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
			//addColumn(statusTab); TODO need this?            
            
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
            	            	
                //String cat = null;
            	
                List sb = (List)getFilterValue("filterStatus");
                String selected = show;
                
              //filter by dept
                String department = "";
                String userId = getWidgetManager().getUser().getId();

                boolean showPrivate =true;
                if(sb!=null&&sb.size()>0)
                    selected = (String)sb.get(0);
                if(selected!=null && selected.equals("-1"))
                    selected = null;
                
                               
                TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
                

                totalRows = tm.getAssignmentListing((String)getFilterValue("deptFilter"),getSort(),userId, requestId, isDesc(), 0, -1).size();
                col = tm.getAssignmentListing((String)getFilterValue("deptFilter"),getSort(),userId, requestId, isDesc(),getStart(), getRows());
                
                
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
    
}
