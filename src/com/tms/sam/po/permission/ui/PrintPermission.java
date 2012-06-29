package com.tms.sam.po.permission.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Table;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.ui.WidgetManager;

import com.tms.sam.po.permission.model.PermissionModel;

public class PrintPermission extends Table {
	private int intTotalRow = 0;
	
	public String getDefaultTemplate() {
        return "po/tablePrint";
    }
	
	public void init(){
		super.init();
        setModel(new PrintPermissionModel());
        setWidth("100%");
	}
	
	public class PrintPermissionModel extends TableModel {
		public PrintPermissionModel() {
			Application application = Application.getInstance();
			
			TableColumn groupName = new TableColumn("groupName", application.getMessage("po.label.groupName", "Group Name"));
			groupName.setSortable(false);
			addColumn(groupName);
			
			TableColumn dept = new TableColumn("id", application.getMessage("po.label.dept"));
			dept.setSortable(false);
			dept.setFormat(new TableFormat() {
                public String format(Object arg0) {
                	PermissionModel module = (PermissionModel) Application.getInstance().getModule(PermissionModel.class);
                	
                	String department="";
                	Collection depart = module.getGroupDeptStaff(arg0.toString());
                    for (Iterator departcount = depart.iterator(); departcount.hasNext();) {        			 
                    	User user = (User) departcount.next();
                    	department+=user.getProperty("deptCode")+"<BR>";

                    }
                    return (department);
                }
            });
            addColumn(dept);
            
            TableColumn staff = new TableColumn("id", application.getMessage("po.label.staffName"));
            staff.setSortable(false);
            staff.setFormat(new TableFormat() {
                public String format(Object arg0) {
                	PermissionModel module = (PermissionModel) Application.getInstance().getModule(PermissionModel.class);               	
                	String staffs="";
                	Collection staffcountry = module.getGroupDeptStaff(arg0.toString());
                    for (Iterator staffcount = staffcountry.iterator(); staffcount.hasNext();) {        			 
                    	User user = (User) staffcount.next();
                    	staffs+=user.getName()+"<BR>";

                    }
                    return (staffs);
                }
            });
            addColumn(staff);

			String contextPath = (String)getWidgetManager().getAttribute(WidgetManager.CONTEXT_PATH);
            TableFormat booleanFormat = new TableBooleanFormat("<img src=\"" + contextPath + "/common/table/booleantrue.gif\">", "");
            TableColumn groupActive = new TableColumn("active", application.getMessage("po.label.active", "Active"));
			groupActive.setFormat(booleanFormat);
			addColumn(groupActive);
		}
		
		public int getTotalRowCount(){
			PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
			String searchBy=(String)getWidgetManager().getAttribute("searchBy");
			
			intTotalRow = model.getGroupListingCount(searchBy);
			return intTotalRow;
		}
		
		public Collection getTableRows(){
			PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
			String searchBy=(String)getWidgetManager().getAttribute("searchBy");
			
			return model.getGroupListing(searchBy, getSort(), isDesc(),0,-1);
		}

	}

	public int getIntTotalRow() {
		return intTotalRow;
	}

	public void setIntTotalRow(int intTotalRow) {
		this.intTotalRow = intTotalRow;
	}

}
