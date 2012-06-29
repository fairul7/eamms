package com.tms.fms.department.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.tms.fms.department.model.FMSDepartment;
import com.tms.fms.department.model.FMSDepartmentManager;


public class FMSUnitTable  extends Table
{
    UnitTableModel tm ;
    public void init()
    {
        super.init();
        tm = new UnitTableModel();
        setModel(tm);
        setWidth("100%");
        setNumbering(true);
    }
   
    public void onRequest(Event event)
    {
        super.onRequest(event);
        
    }

    public class UnitTableModel extends TableModel{
        private Collection col = null;
        private boolean displayDeleted = false;
        private Map cmap;
        boolean approvePermission = false;
        int totalRows = 0;
        
       
        TableColumn statusColumn;
        Label lbldateFrom;
        Label lbldateTo;
        private SelectBox deptList;
        
               
        public UnitTableModel()
        {
        	//name,description,HOD,status
        	Application application = Application.getInstance();
        	            
            TableColumn unitColumn = new TableColumn("name",Application.getInstance().getMessage("fms.label.unitName","Name"),true);
            unitColumn.setUrl("");
            unitColumn.setUrlParam("id");
            addColumn(unitColumn);
            
            TableColumn descTab = new TableColumn("description",Application.getInstance().getMessage("fms.label.unitDescription","Date From"),true);
            //descTab.setFormat(new TableDateFormat("dd MMM, yyyy"));
            addColumn(descTab);
                      			
            TableColumn houTab = new TableColumn("HOU",Application.getInstance().getMessage("fms.label.headOfUnit","HOU"),true);
            houTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					
					Application app = Application.getInstance();
					SecurityService service = (SecurityService) app.getService(SecurityService.class);					
					String userId = (String)obj;
					String hodName = "";
					
					if(!(userId.equals(null) || userId.equals(""))){		
						try{
							hodName = service.getUser(userId).getName();
						}catch(Exception e){
							
						}
					}
					return hodName; 
				}
			});
			addColumn(houTab);            
            
			
			////
			TableColumn altAppTab = new TableColumn("id",Application.getInstance().getMessage("fms.label.altenateApproval","Altenate Approval"),true);
            altAppTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					
					FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
					String unitId = (String)obj;
					String approverName = "-";
					Collection colApprover = new ArrayList();
					
					if(!(unitId.equals(null) || unitId.equals(""))){		
						try{
							Application app = Application.getInstance();
							SecurityService service = (SecurityService) app.getService(SecurityService.class);
	            	        
							colApprover = manager.getUnitApprover(unitId);	
							Iterator appIt = colApprover.iterator();
							Map appMap[] = new HashMap[colApprover.size()];
							for (int i = 0; i < colApprover.size(); i++){
								
								appMap[i] = (Map)appIt.next();																					
								String names = service.getUser((String) appMap[i].get("userId")).getName();
								approverName += names+", ";
								if (i==3){
									approverName +="...";
									break;
								}
							}
							
						}catch(Exception e){
							Log.getLog(getClass()).error(e);
						}
							
					}
					
					return approverName;				
				}
			});
			addColumn(altAppTab);
			////
			
			TableColumn deptTab = new TableColumn("department_id",Application.getInstance().getMessage("fms.label.department","Department"),true);               
            deptTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					
					FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
					String deptId = "";
					
					String deptName = "";
					if(!((obj == null)||(obj == ""))){
						deptId = (String)obj;
						try{
							Collection cm = manager.getDeptById(deptId);
							for(Iterator cIt = cm.iterator(); cIt.hasNext(); ){
								FMSDepartment fdept = (FMSDepartment) cIt.next();
								deptName = fdept.getName();
							}
							
						}catch(Exception e){
							Log.getLog(getClass()).error(e);
						}
													
						}					
					return deptName; 				
				}
			});
            addColumn(deptTab);     
            
                                                            
            TableColumn statusTab = new TableColumn("status",Application.getInstance().getMessage("fms.label.status","Status"),true);               
            statusTab.setFormat(new TableFormat(){
				
				public String format(Object obj){
					
					FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
					
					String statusno = (String)obj;
					String statusname = "";
					
					if(!(statusno.equals(null) || statusno.equals(""))){		
						
						if(statusno.equals(manager.ACTIVE_DEPT))
							statusname = "Active";
						else if(statusno.equals(manager.INACTIVE_DEPT))
							statusname = "Inactive";
					}
					return statusname; 
				}
			});
            addColumn(statusTab);           
            ////
            
            TableFilter unitFilter = new TableFilter("unitFilter");
            addFilter(unitFilter);
            
            //------------ START : Status Filter ------------------	
            SelectBox sbStatus = new SelectBox("sbStatus");
            FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
            sbStatus.addOption("-1",application.getMessage("fms.setup.status","Status"));
            sbStatus.addOption(manager.ACTIVE_DEPT,"Active");
            sbStatus.addOption(manager.INACTIVE_DEPT,"Inactive");
            
            TableFilter tf = new TableFilter("statusFilter");
            tf.setWidget(sbStatus);
            addFilter(tf);       
            //------------ END : Status Filter ------------------
                       
            addAction(new TableAction("add", application.getMessage("fms.label.addNewUnit", "Add")));
            addAction(new TableAction("active", application.getMessage("fms.label.setActive", "Active")));
            addAction(new TableAction("inactive", application.getMessage("fms.label.setInActive", "Inactive")));
            	            
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
        
        public void statusDept(String unitId,String status){
        	
        	FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
        	try{
        	manager.updateUnitStatus(unitId, status);
        	}catch(Exception e){
        		Log.getLog(getClass()).error(e);
        	}
        }
        
        
        public String getStatus() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("statusFilter");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
        
        public Collection getTableRows()
        {
            try{
                
                FMSDepartmentManager manager = (FMSDepartmentManager) Application.getInstance().getModule(FMSDepartmentManager.class);
                /*totalRows = manager.getUnit((String)getFilterValue("unitFilter"),getSort(),isDesc(),0,-1).size();
                col = manager.getUnit((String)getFilterValue("unitFilter"),getSort(),isDesc(),getStartIndex(),getRows());*/
                totalRows = manager.getUnit((String)getFilterValue("unitFilter"),getStatus(),getSort(),isDesc(),0,-1).size();
                col = manager.getUnit((String)getFilterValue("unitFilter"),getStatus(),getSort(),isDesc(),getStartIndex(),getRows());
                
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
