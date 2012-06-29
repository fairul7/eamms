<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.employee.ManageEmployee" module="com.tms.hr.employee.model.EmployeeModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="employeeSetup">
        <com.tms.hr.employee.ui.NewEmployeeTable name="table"/>
    </page>
</x:config>

<c:if test="${forward.name == 'add'}">
    <c:redirect url="employeeSetupAdd.jsp"/>
</c:if>
<c:if test="${!empty param.employeeID}">
    <c:redirect url="employeeSetupEdit.jsp?employeeID=${param.employeeID}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td class="classHeader" height="22"><fmt:message key='leave.label.HRMenu'/> > <fmt:message key='leave.label.Employees'/></td></tr>
    <tr><td class="classRow"><x:display name="employeeSetup.table" /></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
