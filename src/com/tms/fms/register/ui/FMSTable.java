package com.tms.fms.register.ui;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kacang.Application;
import kacang.model.DaoException;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.Label;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;
import com.tms.fms.register.model.FMSRegisterManager;
import com.tms.fms.transport.model.FmsNotification;

public class FMSTable extends Table
{
    RegisterTableModel tm ;
    private String id;
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void init()
    {
        super.init();
        tm = new RegisterTableModel();
        setModel(tm);
        setWidth("100%");
        setNumbering(true);

    }
   
    public void onRequest(Event event)
    {
        super.onRequest(event);
    }

    public class RegisterTableModel extends TableModel{
        private Collection col = null;
        private Map cmap;
        boolean approvePermission = false;
        int totalRows = 0;
       
        TableColumn statusColumn;
        Label lbldateFrom;
        Label lbldateTo;
        private SelectBox deptList;
        
               
        public RegisterTableModel()
        {        	
        	Application application = Application.getInstance();
        	
            TableColumn nameColumn = new TableColumn("username",Application.getInstance().getMessage("security.label.name","Name"),true);
            nameColumn.setUrl("");
            nameColumn.setUrlParam("id");
            addColumn(nameColumn);
            
            TableColumn emailTab = new TableColumn("email1",Application.getInstance().getMessage("security.label.email","Email"),true);
            emailTab.setFormat(new TableDateFormat("dd MMM, yyyy"));
            addColumn(emailTab);
                        
            TableColumn deptTab = new TableColumn("departmentName",Application.getInstance().getMessage("fms.label.department","Department"),true);               
            addColumn(deptTab);     
            
            TableColumn endTab = new TableColumn("statusDate",Application.getInstance().getMessage("scheduling.label.date","Date"),true);            
            endTab.setFormat(new TableFormat(){
            		public String format(Object obj){					
					
					String strDate = " -";
					if(!(null == obj || "".equals(obj))){
						Date statDate = (Date)obj;
						
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	         	        SimpleDateFormat stf = new SimpleDateFormat("h:mm a");         	        
						
	         	       strDate = sdf.format(statDate) + " ["+stf.format(statDate)+"]";
					}
							    		
		    		return strDate;							
				}
			});
            addColumn(endTab);    
            
            TableFilter userFilter = new TableFilter("userFilter");
            addFilter(userFilter);
            
            
            deptList = new SelectBox("deptList");  
            deptList.addOption("-1",Application.getInstance().getMessage("fms.facility.table.department","Department"));
            try
            {
                cmap = ((FMSDepartmentManager)Application.getInstance().getModule(FMSDepartmentManager.class)).getActiveFMSDepartments();
            } catch (DaoException e)
            {
                Log.getLog(getClass()).error(e);
            }
            for(Iterator i = cmap.keySet().iterator();i.hasNext();)
            {
                String key = (String) i.next();
                deptList.addOption(key,(String)cmap.get(key));
                //System.out.println("KUNCI: "+key);
            }

            
            TableFilter deptFilter = new TableFilter("deptFilter");
            deptFilter.setWidget(deptList);
            addFilter(deptFilter);
                       
            if("0".equals(getName())){
            	addAction(new TableAction("accept", application.getMessage("fms.label.acceptRegistration", "Accept")));
            	addAction(new TableAction("reject", application.getMessage("fms.label.rejectRegistration", "Reject")));
            }
            	            
        }

        public Forward processAction(Event evt, String action, String[] selectedKeys)
        {
			if ("add".equals(action)){
				return new Forward("AddBooking");
        	} else if ("accept".equals(action)) {
				for(int i=0;i<selectedKeys.length;i++) {
					acceptFMSUser(selectedKeys[i],evt);
				}
        	} else if ("reject".equals(action)) {              
				for(int i=0;i<selectedKeys.length;i++) {        				 
					evt.getRequest().setAttribute("userId", selectedKeys[i]);
				}
			
				return new Forward("Reject");
			}
			
			return super.processAction(evt,action,selectedKeys);
        }
        
        public void acceptFMSUser(String userId,Event event){
        	
        	SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
        	FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
        	User user = new User();
        	String groupId = Application.getInstance().getProperty("FMSUserGroup");
        	String[] userIds = {userId};
        	Date today = new Date();
        	        	
        	if (userId != null){
        		try{
        			user = manager.getFMSUser(userId);
        			user.setProperty("active", true);
        			user.setProperty("property1", user.getProperty("staffID"));
        			
    				manager.updateFMSUser(manager.ACCEPTED_FMS_USER, null, today, userId);          				
    				manager.addUser(user, false);
    				ss.assignUsers(groupId, userIds);
    				
    				acceptNofication(event, user);        			
        			
        		}catch(Exception e){
        			Log.getLog(getClass()).error(e);
        		}
        	}
        	
        }
        
        private void acceptNofication(Event event, User user){
        	Application app = Application.getInstance();
    		String body = "";
    		String dept = "";
    		String unit = "";
    		
    		try{
    		dept = (String) user.getProperty("department");
    		unit = (String) user.getProperty("unit");
    		}catch(Exception er){}
    		
    		FMSDepartmentManager deptman = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
    		Map map = new HashMap();
    		if(!(null == dept || null == unit)){
    			
    			try{
    				
    				map =  deptman.getDeptUnitUser(user.getId());
    				
    				for(Iterator it = map.keySet().iterator(); it.hasNext(); ){
    					it.next();
    					unit = (String)map.get("unit");
    					dept = (String)map.get("department");
    					
    				}
    			}catch(Exception er){}
    		}    			
    		
    		try {
    			    	       
    	        body = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
                "</head><body>" +
                "<style>" +
                ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
                "</style>" +
                "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
                "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
                "<b><U>" +
                app.getMessage("fms.notification.yourRegActivated") +
                "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
                "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
                "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                
                "<b>" + app.getMessage("fms.notification.name") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + user.getName() + "</td></tr>" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                "<b>" + app.getMessage("fms.notification.username") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + user.getUsername() + "</td></tr>" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                "<b>" + app.getMessage("fms.notification.department") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + dept + "</td></tr>" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                "<b>" + app.getMessage("fms.notification.unit") + "</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + unit + "</td></tr>" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +                
                "<b>"  + app.getMessage("fms.notification.designation") +"</b></strong></td><td class=\"contentBgColorMail\" width=\"90%\">" + user.getProperty("designation") + "</td></tr>" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +            
                "</td></tr></table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
                "</tr></table><p>&nbsp; </p></body></html>";
    			    					
    			
    		}catch(Exception e){
    			Log.getLog(getClass()).error(e.toString(), e);
    		}		
    		    		
    		String emailTo[] = {(String) user.getProperty("email1")};    		
    		String subject = app.getMessage("fms.notification.yourRegActivated");    		
    		FmsNotification notification = new FmsNotification();
    		notification.send(emailTo, subject, body);    	
    	}
        
        private void rejectNofication(Event event, User user){
    		Application app = Application.getInstance();
    		String body = "";
    		
    		String dept = (String) user.getProperty("department");
    		FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
    		if(!(null == dept)){
    			
    			try{
    				dept = manager.getUserDepartment(user.getId());
    				
    			}catch(Exception er){}
    		}    			
    		
    		try {
    			    	       
    	        body = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">" +
                "</head><body>" +
                "<style>" +
                ".contentBgColorMail {background-color: #FFFFFF; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}" +
                "</style>" +
                "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr valign=\"MIDDLE\">" +
                "<td height=\"22\" bgcolor=\"#003366\" class=\"contentBgColorMail\"><b><font color=\"#000000\" class=\"contentBgColorMail\">" +
                "<b><U>" +
                app.getMessage("fms.notification.yourRegRejected") +
                "</U></b></font></b></td><td align=\"right\" bgcolor=\"#003366\" class=\"contentBgColorMail\">&nbsp;</td>" +
                "</tr><tr><td colspan=\"2\" valign=\"TOP\" bgcolor=\"#EFEFEF\" class=\"contentBgColorMail\">&nbsp;</td>" +
                "</tr><tr><td colspan=\"2\" valign=\"TOP\"><table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"1\">" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +
                
                "<td class=\"contentBgColorMail\" width=\"90%\">" + app.getMessage("fms.notification.yourRegRejected")+app.getMessage("fms.notification.plsContactAdmin") + "</td></tr>" +
                "<tr><td class=\"contentBgColorMail\" width=\"10%\" align=\"right\" valign=\"top\" nowrap><strong>" +            
                "</td></tr></table></td></tr><tr><td colspan=\"2\" valign=\"TOP\"  class=\"contentBgColorMail\">&nbsp;</td>" +
                "</tr></table><p>&nbsp; </p></body></html>";
    			    					
    			
    		}catch(Exception e){
    			Log.getLog(getClass()).error(e.toString(), e);
    		}		
    		
    		
    		String emailTo[] = {(String) user.getProperty("email1")};    		
    		String subject = app.getMessage("fms.notification.yourRegRejected");    	    		
    		FmsNotification notification = new FmsNotification();
    		notification.send(emailTo, subject, body);
    		
    	
    	}

        public Collection getTableRows()
        {
            try{
                FMSRegisterManager manager = (FMSRegisterManager) Application.getInstance().getModule(FMSRegisterManager.class);
                                                
                String keyword = (String)getFilterValue("userFilter");
                List sb = (List)getFilterValue("deptFilter");
              
                String status = null;
                if(sb!=null&&sb.size()>0)
                    status = (String)sb.get(0);
                if(status!=null&&status.equals("-1"))
                    status = null;
                String active = getName();
                       
                totalRows = manager.getFMSWaitingUser(keyword,status,getSort(),isDesc(),0,-1,active).size();
                col = manager.getFMSWaitingUser(keyword,status,getSort(),isDesc(),getStartIndex(),getRows(),active);
                
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
