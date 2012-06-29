package com.tms.fms.facility.ui;

import java.util.ArrayList;
import java.util.Collection;

import kacang.Application;
import kacang.model.DaoException;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableDateFormat;
import kacang.stdui.TableFilter;
import kacang.stdui.TableModel;
import kacang.stdui.TextField;
import kacang.ui.Event;
import kacang.util.Log;

import com.tms.fms.facility.model.FacilityDao;
import com.tms.fms.facility.model.FacilityModule;
import com.tms.fms.facility.model.SetupModule;

public class AssignmentCheckOutDetails extends Table {
	public String groupId;
	public String requestId;
	
	public AssignmentCheckOutDetails() {
	}

	public AssignmentCheckOutDetails(String s) {
		super(s);
	}

	public void init() {
		super.init();
		setPageSize(20);
		setModel(new AssignmentCheckOutDetailsModel());
		setWidth("100%");
	}

	public void onRequest(Event event) {
		setModel(new AssignmentCheckOutDetailsModel());
		setCurrentPage(1);
		init();
	}

	class AssignmentCheckOutDetailsModel extends TableModel {
		AssignmentCheckOutDetailsModel() {
			removeChildren();
			Application app = Application.getInstance();
			
			// table columns
			TableColumn tcFacilityName = new TableColumn("facilityName", app.getMessage("fms.facility.table.itemName"));
			addColumn(tcFacilityName);
			
			TableColumn tcBarcode = new TableColumn("barcode", app.getMessage("fms.facility.table.itemCode"));
			addColumn(tcBarcode);
			
			TableColumn tcCheckedOutDate = new TableColumn("checkedOutDate", app.getMessage("fms.facility.table.checkOutDate"));
			tcCheckedOutDate.setFormat(new TableDateFormat(SetupModule.DATE_TIME_FORMAT));
			addColumn(tcCheckedOutDate);
			
			TableColumn tcCheckedOutBy = new TableColumn("checkedOutBy", app.getMessage("fms.facility.table.checkOutBy"));
			addColumn(tcCheckedOutBy);
			
			TableColumn tcCheckedInDate = new TableColumn("checkedInDate", app.getMessage("fms.facility.table.checkInDate"));
			tcCheckedInDate.setFormat(new TableDateFormat(SetupModule.DATE_TIME_FORMAT));
			addColumn(tcCheckedInDate);

			TableColumn requiredTo = new TableColumn("checkedInBy", app.getMessage("fms.facility.table.checkInBy"));
			addColumn(requiredTo);
			
			TableFilter tfSearchBy = new TableFilter("searchBy");
			TextField tfSearchText = new TextField("tfSearchText");
			tfSearchText.setSize("20");
			tfSearchBy.setWidget(tfSearchText);
			addFilter(tfSearchBy);

		}

		public Collection getTableRows() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			
			try {
				return dao.selectAssignmentCheckOutDetailsUnion(requestId, searchBy, getSort(), isDesc(), getStart(), getRows());
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return new ArrayList();
			}
		}

		public int getTotalRowCount() {
			String searchBy = "";
			searchBy = (String) getFilterValue("searchBy");
			
			FacilityDao dao = (FacilityDao) Application.getInstance().getModule(FacilityModule.class).getDao();
			
			try {
				return dao.countAssignmentCheckOutDetailsAll(requestId, searchBy,"");
			} catch (DaoException e) {
				Log.getLog(getClass()).error(e.toString());
				return 0;
			}
		}

		public String getTableRowKey() {
			return "groupId";
		}
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
}
