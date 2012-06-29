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
import kacang.stdui.DatePopupField;
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

import com.tms.ekms.manpowertemp.model.ManpowerLeaveObject;
import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentDao;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.engineering.model.UnitHeadDao;
import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.AssignmentObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;

public class TodayListing extends Table
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
            TableColumn reqColumn = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.assgId","Assignment Id"),true);
            reqColumn.setUrl("");
            reqColumn.setUrlParam("flagId");
            addColumn(reqColumn);
            
            TableColumn userTab = new TableColumn("requestBy",Application.getInstance().getMessage("fms.label.requestBy","Requestor"),true);
            userTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
					Application app = Application.getInstance();
					SecurityService service = (SecurityService) app.getService(SecurityService.class);			
					FMSRegisterManager FRM = (FMSRegisterManager) app.getModule(FMSRegisterManager.class);
					FMSDepartmentManager FDM = (FMSDepartmentManager) app.getModule(FMSDepartmentManager.class);
					String userid = (String)obj;	
					String deptId = "";
					String department = "";
					String username = "";	
					String userdept = "";
					try{						
						username = service.getUser(userid).getName(); 
						deptId = FRM.getUserDepartment(userid);
						FMSDepartment FD = FDM.getselectFMSDepartment(deptId);
						department = FD.getName();
					}catch(Exception e){}
		    		
					userdept = username + "<br/> (" + department + ")";
					
		    		return userdept;						
				}
			});
            
            addColumn(userTab);
            
            TableColumn reqTab = new TableColumn("vehicle_num",Application.getInstance().getMessage("fms.label.outsourceVehicleNo","Request"),true);                       
            addColumn(reqTab);
            
            TableColumn purposeTab = new TableColumn("purpose",Application.getInstance().getMessage("fms.tran.requestPurpose","Request"),true);                       
            addColumn(purposeTab);
            
            TableColumn desTab = new TableColumn("destination",Application.getInstance().getMessage("fms.label.destination","Request"),true);                       
            addColumn(desTab);
            
            TableColumn driverTab = new TableColumn("id",Application.getInstance().getMessage("fms.label.driverAssigned","Drivers"), false);            
            driverTab.setFormat(new TableFormat(){
            	
            	public String format(Object obj){					
					
            		String id = (String) obj;
					TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
					
					//driver
			        String drivers = "";
			        ManpowerLeaveObject ML = new ManpowerLeaveObject();        
			        try{
			        	//Collection col = TM.getDriverByRequestId(reqId);
			        	Collection col = TM.getDriverByAssgId(id);
			        	for(Iterator it = col.iterator(); it.hasNext(); ){
			        		ML = (ManpowerLeaveObject) it.next();
			        		//drivers += ML.getManpowerName()+",<br/>";
			        		if("".equals(drivers)){
			    				drivers += ML.getManpowerName();
			    			} else {
			    				drivers += ", <br />" + ML.getManpowerName();
			    			}
			        	}
			        	
			        }catch(Exception er){}
							    		
		    		return drivers;							
				}
			});
            addColumn(driverTab); 
            
            TableColumn timeNdate = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.datetimeRequired","Require Time"), false);            
            timeNdate.setFormat(new TableFormat(){
            		public String format(Object obj){		
            			
            			
            			TransportRequest TR = new TransportRequest();            	        
            	        TransportModule TM = (TransportModule)Application.getInstance().getModule(TransportModule.class);
            	        String timeDate = "";
            	        
            	        if(!(obj == null || "".equals(obj))){
	            	        String assId = (String) obj;
	            	        
	            	        
	            	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	            	        SimpleDateFormat stf = new SimpleDateFormat("k:mm");
	            	        
	            	        
	            	        try{
	            	        	//TR = TM.selectTransportAssignmentByAssignmentId(assId);
	            	        	Collection coll = TM.getAssignmentByAssignmentId(assId);
	            	        	for(Iterator it = coll.iterator(); it.hasNext(); ){
	            	        		TR = (TransportRequest) it.next();	            	   				   
            	   			 	}		  
	            	        
	            	        String dateStart = sdf.format(TR.getStartDate());
	            	        String dateEnd = sdf.format(TR.getEndDate());
	            	        
	            	        String timeStart = stf.format(TR.getStartDate());
	            	        String timeEnd = stf.format(TR.getEndDate());
	            	        
	            	        timeDate = dateStart + " [" + timeStart+"] - <br/>" + dateEnd + " [" + timeEnd+"]";
	            	        }catch(Exception er){}
	            	        }
            	        return timeDate;							
				}
			});
            addColumn(timeNdate);        
              
            TableColumn statusTab = new TableColumn("flagId",Application.getInstance().getMessage("fms.label.status","Status"), false);            
            statusTab.setFormat(new TableFormat(){
            		public String format(Object obj){
										
            			String status = "";
	            		if(!(obj == null || "".equals(obj))){
							String flagId = (String)obj;
							String vehicle_num = "";
							Collection coll = new ArrayList();
							SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
							TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
								
							
							try{
								TransportRequest tr = tm.getTransportByFlagId(flagId);
								vehicle_num = tr.getVehicle_num();
								coll = tm.getAssignmentDetailFlagIdVehicleNo(flagId, vehicle_num);
							}catch(Exception er){
								
							}
							
							if(coll.size() > 0){
								for(Iterator it = coll.iterator(); it.hasNext(); ){
									AssignmentObject ao = (AssignmentObject) it.next();							
									status = tm.selectStatus(ao.getStatus());								
								}							
							}
							
							else
								status = tm.selectStatus(sm.NEW_STATUS);
		            		}
							    		
		    		return status;				
					
				}
			});
            addColumn(statusTab); 
            /*
            TableColumn statusTab = new TableColumn("id",Application.getInstance().getMessage("fms.label.status","Status"), false);            
            statusTab.setFormat(new TableFormat(){
            		public String format(Object obj){
										
					String id = (String)obj;
					String status = "";
					Collection coll = new ArrayList();
					SetupModule sm = (SetupModule) Application.getInstance().getModule(SetupModule.class);
					TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
						
					try{
						coll = tm.getAssignmentDetailRequestId(id);
					}catch(Exception er){
						
					}
					
					if(coll.size() > 0){
						for(Iterator it = coll.iterator(); it.hasNext(); ){
							AssignmentObject ao = (AssignmentObject) it.next();							
							status = tm.selectStatus(ao.getStatus());								
						}							
					}
					
					else
						status = tm.selectStatus(sm.NEW_STATUS);
					
							    		
		    		return status;				
					
				}
			});
            addColumn(statusTab);  	
            */
            
          ////
            TableFilter searchFilter = new TableFilter("deptFilter");
            addFilter(searchFilter);
            
			TableFilter tfDepartment = new TableFilter("tfDepartment");
			SelectBox sbDepartment = new SelectBox("sbDepartment");
		    sbDepartment.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.allDepartment", "All Department"));
		    try {
		    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
				Collection lstDepartment = dao.selectDepartment();
			    if (lstDepartment.size() > 0) {
			    	for (Iterator i=lstDepartment.iterator(); i.hasNext();) {
			        	FMSDepartment o = (FMSDepartment)i.next();
			        	sbDepartment.setOptions(o.getId()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			tfDepartment.setWidget(sbDepartment);
			addFilter(tfDepartment);
			////           
            
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
                List sb = (List)getFilterValue("tfDepartment");
                String selected = "-1";		//default to pending
                

                boolean showPrivate =true;
                if(sb!=null&&sb.size()>0)
                    selected = (String)sb.get(0);
                if(selected!=null && selected.equals("-1"))
                    selected = null;
                
                               
                TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);
                
                               
                Date startDate = new Date();
                //Date endDate = new Date();                
                
                Calendar start = Calendar.getInstance();
                start.setTime(startDate);                
                start.set(Calendar.HOUR_OF_DAY, 00);
                start.set(Calendar.MINUTE, 00);
                start.set(Calendar.SECOND, 00);   
                
//                Calendar end = Calendar.getInstance();
//                end.setTime(endDate);                
//                end.set(Calendar.HOUR_OF_DAY, 23);
//                end.set(Calendar.MINUTE, 59);
//                end.set(Calendar.SECOND, 59);      
                              
//                endDate = end.getTime();
                startDate = start.getTime();
                String userId = getWidgetManager().getUser().getId();
                UnitHeadDao unitDao = (UnitHeadDao) Application.getInstance().getModule(UnitHeadModule.class).getDao();
    		 	int settingValue = unitDao.selectAssignmentSettingValue();
    		 	
                col = tm.getAssignment(settingValue, (String)getFilterValue("deptFilter"),selected, startDate, null, getSort(),null,isDesc(), getStart(), getRows());
                totalRows = tm.getCountAssignment(settingValue, (String)getFilterValue("deptFilter"), selected, startDate, null, null);
                
                
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
