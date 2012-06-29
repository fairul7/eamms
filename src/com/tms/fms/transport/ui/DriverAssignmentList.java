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
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.setup.model.SetupModule;
import com.tms.fms.transport.model.AssignmentObject;
import com.tms.fms.transport.model.TransportModule;
import com.tms.fms.transport.model.TransportRequest;

public class DriverAssignmentList extends Table
{
    DeptTableModel tm ;
    String show = null;
    
    private DatePopupField startDate;
    private DatePopupField endDate;
    
    
    public void init()
    {
        super.init();
        tm = new DeptTableModel();
        setModel(tm);
        setWidth("100%");
        setNumbering(true);
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());                
        start.set(Calendar.DAY_OF_MONTH, -30);
        startDate.setDate(start.getTime());
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
        
       
               
        public DeptTableModel()
        {

        	//name,description,HOD,status
        	Application application = Application.getInstance();
        	
            TableColumn deleteColumn = null;
            TableColumn reqColumn = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.assgId","Assg Id"),true);
            reqColumn.setUrl("");
            reqColumn.setUrlParam("flagId");
            addColumn(reqColumn);
            
            TableColumn userTab = new TableColumn("driver",Application.getInstance().getMessage("fms.label.driverAssigned","Driver"),true);
            /*
           	TableColumn userTab = new TableColumn("manpowerId",Application.getInstance().getMessage("fms.label.driverAssigned","Driver"),true);
            userTab.setFormat(new TableFormat(){
            		public String format(Object obj){
					
					Application app = Application.getInstance();
					SecurityService service = (SecurityService) app.getService(SecurityService.class);		
					String userid = (String)obj;				
					String username = "";
					try{						
						username = service.getUser(userid).getName();			    								
					}catch(Exception e){
						
					}		    		
		    		return username;
				}
			});
			*/
            addColumn(userTab);
            
            TableColumn progTab = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.label.transport.requestTitle","Program"),true);           
            addColumn(progTab);
                        
            TableColumn servTab = new TableColumn("requestTitle",Application.getInstance().getMessage("fms.label.transport.serviceType","Program"), false);           
            servTab.setFormat(new TableFormat(){
        		public String format(Object obj){
				
				String type = "Transport";
				
	    		return type;		
				
			}
		});
        
        addColumn(servTab);
         
            TableColumn timeNdate = new TableColumn("id",Application.getInstance().getMessage("fms.label.transport.datetimeRequired","Require Time"), false);            
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
                    			
            startDate = new DatePopupField("startDate");
            startDate.setSize("10");            
            endDate = new DatePopupField("endDate");
            endDate.setSize("10");		
			
			TableFilter dateFr = new TableFilter("dateFr");
            dateFr.setWidget(lbldateFrom);
            TableFilter dateT = new TableFilter("dateT");
            dateT.setWidget(lbldateTo);
           

            endDate.setDate(new Date());                       
            TableFilter sFilter = new TableFilter("startDate");
            TableFilter eFilter = new TableFilter("endDate");            
            sFilter.setWidget(startDate);
            eFilter.setWidget(endDate);     

            addFilter(sFilter);
            addFilter(eFilter);
            
            
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {        	
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
                ////
                Date start1 = startDate.getDate();
            	Date end1 = endDate.getDate();
            	if(start1.after(end1)){
            		setInvalid(true);
            		startDate.setInvalid(true);
            		endDate.setInvalid(true);
            	}
                ////           
            
                TransportModule tm = (TransportModule) Application.getInstance().getModule(TransportModule.class);                        
                
                Calendar start = Calendar.getInstance();
                start.setTime(start1);                
                //start.set(Calendar.DAY_OF_MONTH, -30);
                start.set(Calendar.HOUR_OF_DAY, 00);
                start.set(Calendar.MINUTE, 00);
                start.set(Calendar.SECOND, 00);   
                
                Calendar end = Calendar.getInstance();
                end.setTime(end1);                
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);      
                              
                end1 = end.getTime();
                start1 = start.getTime();
                String userId = getWidgetManager().getUser().getId();
                
                col = tm.getDriverAssignments((String)getFilterValue("deptFilter"), start1, end1, getSort(),null,isDesc(), getStart(), getRows());
                totalRows = tm.getCountDriverAssignments((String)getFilterValue("deptFilter"), start1, end1, null);
                
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
