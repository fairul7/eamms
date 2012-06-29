<%@ page import="kacang.Application,
                 com.tms.hr.orgChart.model.OrgChartHandler,
                 com.tms.hr.orgChart.model.DepartmentCountryAssociativityObject"%>
<%@include file="/common/header.jsp" %>

<% 
Application app1 = Application.getInstance();
String userId1 = app1.getCurrentUser().getId();
OrgChartHandler orgChartHandler1 = (OrgChartHandler) app1.getModule(OrgChartHandler.class);

DepartmentCountryAssociativityObject associatedDeptCount = orgChartHandler1.getAssociatedCountryDept(userId1);
boolean hasOrgChartSetup = false;
if(associatedDeptCount != null) {
	hasOrgChartSetup = true;
}
%>

<c-rt:if test="<%=!hasOrgChartSetup%>">
	<c:redirect url="noOrgChartSetup.jsp" />
</c-rt:if>

<script type="text/javascript">
function confirmSubmit() {
	if(confirm("<fmt:message key='isr.message.confirmSave'/>")) {
		return true;
	}
	else {
		return false;
	}
}

function confirmCancel() {
	if(confirm("<fmt:message key='isr.message.confirmCancel'/>")) {
		return true;
	}
	else {
		return false;
	}
}

function confirmWithdraw() {
	if(confirm("<fmt:message key='isr.message.confirmWithdraw'/>")) {
		return true;
	}
	else {
		return false;
	}
}

function confirmReject() {
	if(confirm("<fmt:message key='isr.message.confirmReject'/>")) {
		return true;
	}
	else {
		return false;
	}
}
</script>

<style type="text/css">
#altItem{
	background-color:#E0E0E0;
	width:100%;
}
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>