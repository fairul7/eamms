<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.ui.InactiveForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Inactive">
    	<com.tms.fms.transport.ui.InactiveForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forward_succ" value="<%= InactiveForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= InactiveForm.FORWARD_ADD_FAIL %>"/>

<c:choose>
	<c:when test="${not empty(paramValues.selectedKeys)}">
		<c:set var="selectedKeys" value="${paramValues.selectedKeys}"/>
	</c:when>
	<c:otherwise>
		<c:set var="selectedKeys" value="${widgets['Maintenance.form'].vehicle_num}"/>
	</c:otherwise>
</c:choose>

<x:set name="Inactive.form" property="vehicle_num" value="${selectedKeys}"/>
<x:set name="Inactive.form" property="cancelUrl" value="VehicleListing.jsp"/>
<x:set name="Inactive.form" property="action" value="<%= InactiveForm.FORM_ACTION_ADD %>"/>
<x:set name="Inactive.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Inactive successfully!"); document.location = "<c:url value="VehicleListing.jsp"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Fail to inactive one or more vehicle!"); document.location = "<c:url value="VehicleListing.jsp"/>";</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.setVehicleInactive'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Inactive.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>

