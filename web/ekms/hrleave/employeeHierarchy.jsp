<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.hr.employee.ManageEmployee" module="com.tms.hr.employee.model.EmployeeModule" url="/ekms/index.jsp"/>
<x:config>
    <page name="employeePage">
          <com.tms.hr.employee.ui.HierarchySetupPanel name="hierarchy"/>
    </page>
</x:config>
<c:set var ="form">
    <x:display name="employeePage.hierarchy"/>
</c:set>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='leave.label.hierarchy'/> > <c:out value="${widgets['employeePage.hierarchy'].title}"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><c:out value="${form}" escapeXml="false" /></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp" %>
