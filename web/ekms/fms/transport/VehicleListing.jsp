<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.ui.VehicleTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<c-rt:set var="forward_listing_add" value="<%= VehicleTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_writeoff" value="<%= VehicleTable.FORWARD_LISTING_WRITEOFF %>"/>
<c-rt:set var="forward_listing_inactive" value="<%= VehicleTable.FORWARD_LISTING_INACTIVE %>"/>
<c-rt:set var="forward_listing_error" value="<%= VehicleTable.FORWARD_LISTING_ERROR %>"/>

<c:choose>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="AddVehicle.jsp"/>
	</c:when>
</c:choose>

<x:config>
	<page name="Vehicle">
		<com.tms.fms.transport.ui.VehicleTable name="table" width="100%"/>
	</page>
</x:config>

<c:if test="${!empty param.vehicle_num}">
	<c:redirect url="ViewVehicleDetail.jsp?id=${param.vehicle_num}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<c:choose>
	<c:when test="${forward.name == forward_listing_error}">
		<script type="text/javascript">
			alert("<c:out value='${errorMsg}'/>");
		</script>	
	</c:when>
	<c:when test="${forward.name == forward_listing_writeoff}">
		<form name="writeoff" action="WriteOff.jsp" method="post">
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">
				<input type="hidden" name="selectedKeys" value="${key}">
			</c:forEach>
		</form>
		
		<script type="text/javascript">
			document.forms['writeoff'].submit();
		</script>	
	</c:when>
	<c:when test="${forward.name == forward_listing_inactive}">
		<form name="inactive" action="Inactive.jsp" method="post">
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">
				<input type="hidden" name="selectedKeys" value="${key}">
			</c:forEach>
		</form>
		
		<script type="text/javascript">
			document.forms['inactive'].submit();
		</script>	
	</c:when>
</c:choose>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.vehicleListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Vehicle.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>