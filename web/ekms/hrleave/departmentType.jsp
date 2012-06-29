<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.employee.ManageEmployee" module="com.tms.hr.employee.model.EmployeeModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="departmentType">
          <com.tms.hr.leave.ui.DepartmentSetupPanel name="department"/>
    </page>
</x:config>

<c:if test="${forward.name=='success'}">
<script>
    alert("New Department Type is added.");
</script>
</c:if>
<c:if test="${forward.name=='duplicate'}">
<script>
    alert("Unable to add new Department Type. Duplicated entry of Department Code.");
</script>
</c:if>
<c:if test="${forward.name=='fail'}">
<script>
    alert("Fail to add new Department Type.");
</script>
</c:if>
<c:if test="${forward.name=='editsuccess'}">
<script>
    alert("Department Type is updated.");
</script>
</c:if>
<c:if test="${forward.name=='editfail'}">
<script>
    alert("Fail to update Department Type.");
</script>
</c:if>
<c:set var ="form">
<x:display name="departmentType.department" ></x:display>
</c:set>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='leave.label.Department'/> > <c:out value="${widgets['departmentType.department'].title}"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><c:out value="${form}" escapeXml="false" /></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
