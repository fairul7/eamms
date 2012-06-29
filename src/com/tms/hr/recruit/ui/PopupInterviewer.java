package com.tms.hr.recruit.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import kacang.Application;
import kacang.services.security.SecurityService;
import kacang.services.security.User;
import kacang.stdui.PopupSelectBox;
import kacang.stdui.PopupSelectBoxTable;
import kacang.stdui.PopupSelectBoxTableModel;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.util.Log;

import org.apache.axis.collections.SequencedHashMap;

import com.tms.hr.recruit.model.RecruitAppModule;

public class PopupInterviewer extends PopupSelectBox{
	
	public PopupInterviewer(){
		  super();
	}
	
	public PopupInterviewer(String name){
		 super(name);
	}
	
	protected Table initPopupTable() {
       return new PopupInterviewerListedTable();
   }
	
	protected Map generateOptionMap(String[] ids) {
        Map itemsMap = new SequencedHashMap();
        if (ids == null || ids.length == 0) {
            return itemsMap;
        }

        Application app = Application.getInstance();
        RecruitAppModule ram = (RecruitAppModule) app.getModule(RecruitAppModule.class);
        SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
        try{
	        	 Collection userListCol = service.getUsersByPermission("recruit.permission.recruitInterviewer",Boolean.TRUE, null, false, 0, -1);
	        	 //build users map
	        	 Map tmpMap = new SequencedHashMap();
	        	 for(Iterator ite=userListCol.iterator(); ite.hasNext();){
	        		 User user = (User) ite.next();
	        		 tmpMap.put(user.getId(), user.getName());
	        	 }
        	    
	             // sort
	             for (int j=0; j<ids.length; j++) {
	                  String name = (String)tmpMap.get(ids[j]);
	                  if (name == null) {
	                      name = "---";
	                  }
	                  itemsMap.put(ids[j], name);
	             }	 
        }catch (Exception e){
        	Log.getLog(getClass()).error(e);
        }
        
        //Collection userList = ram.loadShortlisted(ids);
        
        return itemsMap;
    }
	
	public class PopupInterviewerListedTable extends PopupSelectBoxTable{
		public PopupInterviewerListedTable() {
        }

        public PopupInterviewerListedTable(String name) {
            super(name);
        }

        public void init() {
            super.init();
            setWidth("100%");
            setModel(new PopupInterviewerListedTableModel());
        }
        
        public class PopupInterviewerListedTableModel extends PopupSelectBoxTableModel {
        	Collection userListCol= new ArrayList();
        	
        	public Collection getUserListCol() {
				return userListCol;
			}

			public void setUserListCol(Collection userListCol) {
				this.userListCol = userListCol;
			}

			public PopupInterviewerListedTableModel() {
                super();
                Application app = Application.getInstance();
             
    			TableColumn applicantName = new TableColumn("name", app.getMessage("recruit.general.label.interviewer"));
    			addColumn(applicantName);
    			
    			// filter textbox 
    			addFilter(new TableFilter("userFilter"));
    			
    			//button
    			addAction(new TableAction(FORWARD_SELECT,  app.getMessage("general.label.select", "Select")));
        	}	
        	
        	public Collection getTableRows() {
        		Application app = Application.getInstance();
        	    SecurityService service = (SecurityService)Application.getInstance().getService(SecurityService.class);
        	    
        	    String userName;
        	    if((String)getFilterValue("userFilter")!=null)
        	    	 userName = (String)getFilterValue("userFilter");
        	    else 
        	    	 userName ="";
        	    
        	    try{
        	    	userListCol = service.getUsersByPermission("recruit.permission.recruitInterviewer",Boolean.TRUE, null, false, 0, -1);
        	    	if(userListCol!=null && !userListCol.equals("")){
		    	    	for(Iterator ite=userListCol.iterator(); ite.hasNext();){
		    	    		User user = (User)ite.next();
		    	    		if(!user.getName().contains(userName))
		    	    			ite.remove();
		    	    	}
        	    	}
        		}catch (Exception e){
        	        	Log.getLog(getClass()).error(e);
        	    }
        		
        		return userListCol;
        	}
        	
        	public int getTotalRowCount() {
        		Collection col = userListCol;
        		int total=0;
        		if(col!=null && !col.equals("")){
        			total = col.size();
        		}
        		
    			return total ;
        	}
        	
        	public String getTableRowKey() {
                return "id";
            }
        	
        }
	}
}
