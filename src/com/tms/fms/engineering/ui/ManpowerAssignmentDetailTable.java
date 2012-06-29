package com.tms.fms.engineering.ui;

import java.util.Collection;

import com.tms.fms.engineering.model.UnitHeadModule;
import com.tms.fms.engineering.ui.HOUTodayAssignmentTable.HOUTodayAssignmentTableModel;

import kacang.Application;
import kacang.stdui.Table;
import kacang.stdui.TableColumn;
import kacang.stdui.TableModel;
import kacang.ui.Event;

public class ManpowerAssignmentDetailTable extends Table {
	private String requestId;
	public void onRequest(Event event) {
		setModel(new ManpowerAssignmentDetailTableModel());
		setCurrentPage(1);
	}
	
	public void init() {
		super.init();
		setPageSize(20);
		setModel(new ManpowerAssignmentDetailTableModel());
		setWidth("100%");
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestId() {
		return requestId;
	}

	class ManpowerAssignmentDetailTableModel extends TableModel {
		public ManpowerAssignmentDetailTableModel(){
			Application app = Application.getInstance();
			
			TableColumn assignmentId = new TableColumn("assignmentCode", app.getMessage("fms.facility.label.assignmentId"));
			addColumn(assignmentId);
			
			TableColumn manpowerType = new TableColumn("competencyName", app.getMessage("fms.facility.table.manpower"));
			addColumn(manpowerType);
			
			TableColumn manpower = new TableColumn("fullName", app.getMessage("fms.facility.msg.service.4"));
			addColumn(manpower);
			
			TableColumn callTime = new TableColumn("requiredTime", app.getMessage("fms.facility.assignment.callTime"));
			addColumn(callTime);
			
			TableColumn status = new TableColumn("status", app.getMessage("fms.facility.form.status"));
			addColumn(status);
		}
		public Collection getTableRows() {
			UnitHeadModule mod = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			Application app = Application.getInstance();
			String userId = app.getCurrentUser().getId();
			return mod.getManpowerAssignment(userId, requestId, getSort(), isDesc(), getStart(), getRows());
		}

		public int getTotalRowCount() {
			UnitHeadModule mod = (UnitHeadModule) Application.getInstance().getModule(UnitHeadModule.class);
			Application app = Application.getInstance();
			String userId = app.getCurrentUser().getId();
			return mod.countManpowerAssignment(userId, requestId);
		}
		
	}
}
