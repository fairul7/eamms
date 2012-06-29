<%@ page import="com.tms.hr.orgChart.ui.DepartmentForm"%>
<%@include file="/common/header.jsp" %>
<%@include file="includes/accessControl.jsp" %>

<style type="text/css">
.fieldTitle{
	text-align:right;
	font-weight:bold;
}
</style>

<x:config >
	<page name="orgChart">
		<com.tms.hr.orgChart.ui.DepartmentForm name="addDept"/>
        <com.tms.hr.orgChart.ui.DepartmentList name="listDept"/>
    </page>
</x:config>

<c-rt:set var="forward_saved" value="<%=DepartmentForm.FORWARD_SAVED%>"/>
<c-rt:set var="forward_updated" value="<%=DepartmentForm.FORWARD_UPDATED%>"/>
<x:set name="orgChart.addDept" property="code" value=""/>
<c:if test="${!(empty param.code)}">
    <x:set name="orgChart.addDept" property="code" value="${param.code}"/>
</c:if>

<c:if test="${forward.name=='cancel_form_action'}">
    <c:redirect url="listHierachy.jsp"/>
</c:if>
<c:if test="${forward.name==forward_updated}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.department.alert.updated"/>");
    </script>
</c:if>
<c:if test="${forward.name==forward_saved}">
    <script type="text/javascript">
        alert("<fmt:message key="orgChart.department.alert.saved"/>");
    </script>
</c:if>


<%@ include file="/ekms/includes/header.jsp" %>
<table cellpadding="0" cellspacing="0" width="100%" class="contentBgColor">
    <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
      &nbsp;
            <fmt:message key='orgChart.department.label.addDeptTitle'/>
            </font>
        </b>
    </td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
    <tr>
        <td>
            <x:display name="orgChart.addDept"/>
        </td>
    </tr>
    <tr>
    <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont">
        <b>
            <font color="#FFCF63" class="contentTitleFont">
      &nbsp;
            <fmt:message key='orgChart.department.label.listDeptTitle'/>
            </font>
        </b>
    </td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
    <tr>
        <td>
            <x:display name="orgChart.listDept"/>
        </td>
    </tr>
</table>
<%@ include file="/ekms/includes/footer.jsp" %>




