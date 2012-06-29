<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.employee.ManageEmployee" module="com.tms.hr.employee.model.EmployeeModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="employeeSetupAdd">
          <com.tms.hr.employee.ui.NewAddEmployeeForm name="addForm"/>
    </page>
</x:config>

<c:if test="${forward.name=='success' || !empty param['button*employeeSetupAdd.addForm.cancel_form_action']}">
    <c:redirect url="employeeSetup.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td class="classHeader" height="22"><fmt:message key='leave.label.Employees'/> > <fmt:message key='leave.label.AddNewEmployee'/></td></tr>
    <tr><td class="classRow">
        <p>
        <x:display name="employeeSetupAdd.addForm"/>
    </td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
