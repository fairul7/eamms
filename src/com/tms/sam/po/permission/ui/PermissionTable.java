package com.tms.sam.po.permission.ui;

import java.util.Collection;
import java.util.Iterator;

import kacang.Application;
import kacang.services.security.User;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableBooleanFormat;
import kacang.stdui.TableColumn;
import kacang.stdui.TableFilter;
import kacang.stdui.TableFormat;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.ui.WidgetManager;

import com.tms.sam.po.permission.model.PermissionModel;

public class PermissionTable extends Table{
	private int intTotalRow = 0;
	public static final String FORWARD_ADD = "add";
	public static final String FORWARD_PRINT = "print";
	public static final String FORWARD_ERROR = "error";
	public static final String FORWARD_DELETED = "deleted";
	
	public String getDefaultTemplate() {
        return "po/table";
    }
	
	public void init(){
		setWidth("100%");
		setModel(new PermissionTableModel());
	}
	
	public class PermissionTableModel extends TableModel{
		public PermissionTableModel(){
			Application application = Application.getInstance();
			
			TableColumn groupName = new TableColumn("groupName", application.getMessage("po.label.groupName", "Group Name"));
			groupName.setUrlParam("id");
			groupName.setSortable(true);
			groupName.setUrl("editGroup.jsp");
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
			
			TextField tfSearch = new TextField("tfSearch");
            tfSearch.setSize("20");
            TableFilter searchFilter = new TableFilter("searchFilter");
            searchFilter.setWidget(tfSearch);
            addFilter(searchFilter);
			
			addAction(new TableAction("add", application.getMessage("po.label.add", "Add")));
			addAction(new TableAction("print", application.getMessage("po.label.print","Print")));			
            addAction(new TableAction("delete", application.getMessage("po.label.delete", "Delete"), 
            		application.getMessage("po.message.confirmDelete")));
		}
		
		public int getTotalRowCount(){
			PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
			String searchBy = (String) getFilterValue("searchFilter");
			
			intTotalRow = model.getGroupListingCount(searchBy);
			return intTotalRow;
		}

		public Collection getTableRows(){
			PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
			String searchBy = (String) getFilterValue("searchFilter");
			
			return model.getGroupListing(searchBy, getSort(), isDesc(), getStart(), getRows());
		}
		
		public Forward processAction(Event evt, String action, String[] selectedKeys) {
			
			if (action.equalsIgnoreCase("add")) {
				return new Forward(FORWARD_ADD);
			}else if (action.equalsIgnoreCase("print")) {
				String searchBy = (String) getFilterValue("searchFilter");
				getWidgetManager().removeAttribute("searchBy");
				getWidgetManager().setAttribute("searchBy",searchBy);
				return new Forward(FORWARD_PRINT);
			}
			else if(action.equalsIgnoreCase("delete")) {
				PermissionModel model = (PermissionModel)Application.getInstance().getModule(PermissionModel.class);
				
				if(selectedKeys != null && selectedKeys.length > 0) {
					model.deleteGroups(selectedKeys);
				}
				
				return new Forward(FORWARD_DELETED);
				
			} 
			else {
				return new Forward(FORWARD_ERROR);
				
			}
		}
		
		public String getTableRowKey() {
            return "id";
        }
	}

	public int getIntTotalRow() {
		return intTotalRow;
	}

	public void setIntTotalRow(int intTotalRow) {
		this.intTotalRow = intTotalRow;
	}
	
	
}
