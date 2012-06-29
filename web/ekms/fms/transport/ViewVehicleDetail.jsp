<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.model.VehicleObject, com.tms.fms.transport.ui.VehicleForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="ViewVehicle">
    	<com.tms.fms.transport.ui.VehicleForm name="form"/>
    </page>
</x:config>

<x:set name="ViewVehicle.form" property="action" value="<%= VehicleForm.VEHICLE_ACTION_VIEW %>"/>
<x:set name="ViewVehicle.form" property="cancelUrl" value="index.jsp"/>
<x:set name="ViewVehicle.form" property="maintenanceUrl" value="Maintenance.jsp?vid=${param.id}"/>
<x:set name="ViewVehicle.form" property="roadTaxUrl" value="RoadTax.jsp?vid=${param.id}"/>
<x:set name="ViewVehicle.form" property="editUrl" value="EditVehicle.jsp?id=${param.id}"/>
<x:set name="ViewVehicle.form" property="statusUrl" value="StatusLogs.jsp?vid=${param.id}"/>
<x:set name="ViewVehicle.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>
<x:set name="ViewVehicle.form" property="vehicle_num" value="${param.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.vehicleDetails'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="ViewVehicle.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<c:if test="${widgets['ViewVehicle.form'].status == '2'}">
	<script language="JavaScript">
		document.forms['ViewVehicle.form'].elements['button*ViewVehicle.form.pnButton.btnEdit'].style.display = 'none';
		document.forms['ViewVehicle.form'].elements['button*ViewVehicle.form.pnButton.btnMaintenanceService'].style.display = 'none';
		document.forms['ViewVehicle.form'].elements['button*ViewVehicle.form.pnButton.btnRoadTax'].style.display = 'none';
		document.forms['ViewVehicle.form'].elements['button*ViewVehicle.form.pnButton.btnStatus'].style.display = 'none';
	</script>
</c:if>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>