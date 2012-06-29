<%@ page import="kacang.Application, java.util.*, java.sql.*, com.tms.fms.transport.model.*"%>
<%!
	public void deleteData(String tableName) {
		TransportDao dao = (TransportDao)Application.getInstance().getModule(TransportModule.class).getDao();
		try{		
			dao.update("DELETE FROM "+tableName,null);
			System.out.println("All records "+tableName+" is deleted"); 
		}catch(Exception e){}
		
	}
%>
<%
deleteData("fms_department");
deleteData("fms_department_alternate_approver");
deleteData("fms_facility");
deleteData("fms_facility_category");
deleteData("fms_facility_inactive");
deleteData("fms_facility_inactive_reason");
deleteData("fms_facility_item");
deleteData("fms_facility_item_checkout");
deleteData("fms_facility_location");
deleteData("fms_facility_missing");
deleteData("fms_facility_related_item");
deleteData("fms_facility_writeoff");
deleteData("fms_leaveType_setup");
deleteData("fms_manpow_leave_setup");
deleteData("fms_manpower_leave");
deleteData("fms_prog_setup");
deleteData("fms_register_user");
deleteData("fms_request");
deleteData("fms_services");
deleteData("fms_tran_bodytype");
deleteData("fms_tran_category");
deleteData("fms_tran_channel");
deleteData("fms_tran_fueltype");
deleteData("fms_tran_inactive");
deleteData("fms_tran_inactive_reason");
deleteData("fms_tran_insurance");
deleteData("fms_tran_maintenance");
deleteData("fms_tran_maketype");
deleteData("fms_tran_outsourcepanel");
deleteData("fms_tran_petrolcard");
deleteData("fms_tran_ratecard");
deleteData("fms_tran_ratecard_detail");
deleteData("fms_tran_vehicle");
deleteData("fms_tran_workshop");
deleteData("fms_tran_writeoff");
deleteData("fms_unit");
deleteData("fms_unit_alternate_approver");

%>
done