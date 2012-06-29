<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.employee.ManageEmployee" module="com.tms.hr.employee.model.EmployeeModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="serviceClassification">
          <com.tms.hr.leave.ui.ServiceClassSetupPanel name="service"/>
    </page>
</x:config>

<c:if test="${forward.name=='success'}">
<script>
    alert('New Service Class is added.');
</script>
</c:if>
<c:if test="${forward.name=='duplicate'}">
<script>
    alert('Unable to add new service class. Duplicated entry of service code.');
</script>
</c:if>
<c:if test="${forward.name=='fail'}">
<script>
    alert('Fail to add new service code.');
</script>
</c:if>
<c:if test="${forward.name=='editsuccess'}">
<script>
    alert('Service Class is updated.');
</script>
</c:if>
<c:if test="${forward.name=='editfail'}">
<script>
    alert('Fail to update service class.');
</script>
</c:if>
<c:set var="form">
<x:display name="serviceClassification.service"></x:display>
</c:set>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key='leave.label.serviceClassification'/> > <c:out value="${widgets['serviceClassification.service'].title}"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><c:out value="${form}" escapeXml="false" /></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
