package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tms.fms.department.model.*;
import com.tms.fms.facility.model.*;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.SelectBox;
import kacang.stdui.Table;
import kacang.stdui.TableAction;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TableStringFormat;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.ui.Forward;
import kacang.util.Log;

public class CategoryTable extends Table{
	public static String FORWARD_LISTING_ADD="listing.add";
	public static String FORWARD_LISTING_DELETE="listing.delete";
	public static String FORWARD_LISTING_DELETE_FAIL="listing.delete.fail";
	
	private ViewCategoryModel model;
	private String parent_id="-1";
	private boolean isParent=true;
	
	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public CategoryTable(){}
	
	public CategoryTable(String s){
		super(s);
	}
	
	public void init() {
		super.init();
		setPageSize(10);
		model = new ViewCategoryModel();
		model.setParent_id(parent_id);
		model.setParent(isParent);
		model.resetTable();
		setModel(model);
		setWidth("100%");
	}
	
	public void onRequest(Event event) {
		init();
	}
	
	class ViewCategoryModel extends TableModel{
		private String parent_id="-1";
		private boolean isParent;
		
		public String getParent_id() {
			return parent_id;
		}

		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}

		public boolean isParent() {
			return isParent;
		}

		public void setParent(boolean isParent) {
			this.isParent = isParent;
		}

		public ViewCategoryModel(){}
		
		public void resetTable(){
			removeChildren();
			
			addAction(new TableAction("add", Application.getInstance().getMessage("fms.facility.table.addNewCategory", "Add New Categoty")));
			addAction(new TableAction("setActive", Application.getInstance().getMessage("fms.facility.table.setActive", "Set Active")));
			addAction(new TableAction("setInactive", Application.getInstance().getMessage("fms.facility.table.setInactive", "Set Inactive")));
			
			TableColumn tcName = new TableColumn("name", Application.getInstance().getMessage("fms.facility.table.categoryName", "Category Name"));
			tcName.setUrlParam("id");
			addColumn(tcName);
			
			TableColumn tcDescription = new TableColumn("description", Application.getInstance().getMessage("fms.facility.table.categoryDescription", "Category Description"));
			tcDescription.setSortable(false);
			addColumn(tcDescription);
			
			TableColumn tcDepartment = new TableColumn("department_name", Application.getInstance().getMessage("fms.facility.table.department", "Department"));
			addColumn(tcDepartment);
			
			TableColumn tcUnit = new TableColumn("unit_name", Application.getInstance().getMessage("fms.facility.table.unit", "Unit"));
			addColumn(tcUnit);
			
			TableColumn tcStatus = new TableColumn("status", Application.getInstance().getMessage("fms.facility.table.status", "Status"));
			Map mapIsActive = new HashMap(); mapIsActive.put("1", Application.getInstance().getMessage("fms.facility.table.active", "Active")); mapIsActive.put("0", Application.getInstance().getMessage("fms.facility.table.inactive", "Inactive"));
			tcStatus.setFormat(new TableStringFormat(mapIsActive));
			addColumn(tcStatus);
			
			TableFilter tfSearchText = new TableFilter("tfSearchText");
			TextField searchText = new TextField("searchText");
			searchText.setSize("20");
			tfSearchText.setWidget(searchText);
			addFilter(tfSearchText);
			
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
			
			TableFilter tfUnit = new TableFilter("tfUnit");
			SelectBox sbUnit = new SelectBox("sbUnit");
			sbUnit.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.allUnit", "All Unit"));
		    try {
		    	FMSDepartmentDao dao = (FMSDepartmentDao)Application.getInstance().getModule(FMSDepartmentManager.class).getDao();
				Collection lstUnit = dao.selectUnit();
			    if (lstUnit.size() > 0) {
			    	for (Iterator i=lstUnit.iterator(); i.hasNext();) {
			        	FMSUnit o = (FMSUnit)i.next();
			        	sbUnit.setOptions(o.getId()+"="+o.getName());
			        }
			    }
			}catch (Exception e) {
			    Log.getLog(getClass()).error(e.toString());
			}
			tfUnit.setWidget(sbUnit);
			addFilter(tfUnit);
			
			TableFilter tfStatus = new TableFilter("tfStatus");
			SelectBox statusIsActive = new SelectBox("statusIsActive");
			statusIsActive.setOptions("-1=" + Application.getInstance().getMessage("fms.facility.table.status", "Status"));
			statusIsActive.setOptions("1=" + Application.getInstance().getMessage("fms.facility.table.active", "Active"));
			statusIsActive.setOptions("0=" + Application.getInstance().getMessage("fms.facility.table.inactive", "Inactive"));
			tfStatus.setWidget(statusIsActive);
			addFilter(tfStatus);
		}
		
		public String getSearchText() {
			return (String)getFilterValue("tfSearchText");
		}
		
		public String getStatusIsActive() {
			String returnValue = "-1";
			List lstStatusIsActive = (List)getFilterValue("tfStatus");
			if (lstStatusIsActive.size() > 0) {returnValue = (String)lstStatusIsActive.get(0);}
			return returnValue;
		}
		
		public String getDepartment() {
			String returnValue = "-1";
			List lstDepartment = (List)getFilterValue("tfDepartment");
			if (lstDepartment.size() > 0) {returnValue = (String)lstDepartment.get(0);}
			return returnValue;
		}
		
		public String getUnit() {
			String returnValue = "-1";
			List lstUnit = (List)getFilterValue("tfUnit");
			if (lstUnit.size() > 0) {returnValue = (String)lstUnit.get(0);}
			return returnValue;
		}
		
		public Collection getTableRows() {
			FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
		    	return dao.selectCategory(getSearchText(), getDepartment(), getUnit(), getParent_id(), isParent(), getStatusIsActive(), getSort(), isDesc(), getStart(), getRows());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return new ArrayList();
		    }
		}
		
		public int getTotalRowCount() {
			FacilityDao dao = (FacilityDao)Application.getInstance().getModule(FacilityModule.class).getDao();
			try {
		    	return dao.selectCategoryCount(getSearchText(), getDepartment(), getUnit(), getParent_id(), isParent(), getStatusIsActive());
		    } catch (DaoException e) {
		        Log.getLog(getClass()).error(e.toString());
		        return 0;
		    }
		}
		
		public String getTableRowKey() {
		    return "id";
		}
		
		public Forward processAction(Event event, String action, String[] selectedKeys) {
			FacilityModule mod = (FacilityModule)Application.getInstance().getModule(FacilityModule.class);
			
			if ("add".equals(action)) {
				return new Forward(FORWARD_LISTING_ADD);
			}else if ("setActive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					CategoryObject o = mod.getCategory(selectedKeys[i]);
					o.setStatus("1");
					mod.updateCategory(o);
			    }
		    }else if ("setInactive".equals(action)) {
		    	for (int i=0; i<selectedKeys.length; i++) {
					CategoryObject o = mod.getCategory(selectedKeys[i]);
					o.setStatus("0");
					mod.updateCategory(o);
			    }
		    }
			
			return super.processAction(event, action, selectedKeys);
		}
	}
}
