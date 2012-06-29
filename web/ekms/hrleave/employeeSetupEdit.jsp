<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.employee.ManageEmployee" module="com.tms.hr.employee.model.EmployeeModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="employeeSetupEdit">
          <com.tms.hr.employee.ui.NewEditEmployeeForm name="editForm" employeeID="manager"/>
          <com.tms.collab.directory.ui.UserView name="view" />
    </page>
</x:config>

<c:if test="${!empty param.employeeID}">
    <x:set name="employeeSetupEdit.editForm" property="employeeID" value="${param.employeeID}"/>
    <x:set name="employeeSetupEdit.view" property="id" value="${param.employeeID}"/>
</c:if>
<c:if test="${forward.name=='success' || !empty param['button*employeeSetupEdit.editForm.cancel_form_action']}">
    <c:redirect url="employeeSetup.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td class="classHeader" height="22"><fmt:message key='leave.label.Employees'/> > <fmt:message key='leave.label.EditEmployee'/></td></tr>
    <tr><td class="classRow">
        <p>
        <x:display name="employeeSetupEdit.editForm"/>
    </td></tr>
    <tr><td class="classRow" height="22">&nbsp; <p></td></tr>
    <tr><td class="classHeader" height="22"><fmt:message key='leave.label.EmployeeProfile'/></td></tr>
    <tr><td class="classRow">
        <p>
        <x:display name="employeeSetupEdit.view"/>
    </td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
