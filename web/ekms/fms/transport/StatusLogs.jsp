<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.ui.StatusTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Status">
    	<com.tms.fms.transport.ui.StatusTable name="table" width="100%"/>
    </page>
</x:config>

<c-rt:set var="forward_listing_delete" value="<%= StatusTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= StatusTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<c:choose>
  <c:when test="${not empty(param.vid)}">
  	<c:set var="vehicleID" value="${param.vid}"/>
  </c:when>
  <c:otherwise>
	<c:set var="vehicleID" value="${widgets['Status.form'].vehicle_num}"/>
  </c:otherwise>
</c:choose>

<x:set name="Status.table" property="vehicle_num" value="${vehicleID}"/>
<x:set name="Status.table" property="cancelUrl" value="ViewVehicleDetail.jsp?id=${vehicleID}"/>

<c:choose>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("Delete successfully");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("Error! Fail to delete one or more record.");</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.inactiveStatusLogs'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Status.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>