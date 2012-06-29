<%@include file="/common/header.jsp"%>
<%@ page import="com.tms.sam.po.ui.DepartmentForm"%>
<x:permission var="isAuthorized" module="com.tms.sam.po.model.PrePurchaseModule" permission="com.tms.sam.po.AddDept"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<c:if test="${!empty param.deptID}">
	<c:redirect url="department.jsp?id=${param.deptID}"/>
</c:if>

<c-rt:set var="forwardSuccess" value="<%= DepartmentForm.FORWARD_SUCCESS %>" />

<c:if test="${forward.name eq forwardSuccess}">
    <script>
    	document.location = "department.jsp";
    </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="dptPg">
		<com.tms.sam.po.ui.DepartmentForm name="Department"/>
	</page>
</x:config>

<c:if test="${!empty param.id}" >
	<x:set name="dptPg.Department" property="deptID" value="${param.id}" />
</c:if>


<table width="100%">
	<tr>
		<td width="80%" style="vertical-align:top">
			<x:display name="dptPg.Department"/>
		</td>
	</tr>
</table>

<%@include file="/ekms/includes/footer.jsp" %>