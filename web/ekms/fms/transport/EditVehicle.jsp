<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.model.VehicleObject, com.tms.fms.transport.ui.VehicleForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<c-rt:set var="forward_succ" value="<%= VehicleForm.VEHICLE_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= VehicleForm.VEHICLE_ADD_FAIL %>"/>

<x:config>
    <page name="EditVehicle">
    	<com.tms.fms.transport.ui.VehicleForm name="form"/>
    </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.id)}">
  	<c:set var="vehicleID" value="${param.id}"/>
</c:when>
<c:otherwise>
	<c:set var="vehicleID" value="${widgets['EditVehicle.form'].vehicle_num}"/>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${forward.name == forward_succ}">
	<script>alert("<fmt:message key='fms.tran.msg.recordUpdated'/>");document.location = "<c:url value="ViewVehicleDetail.jsp?id=${vehicleID}"/>";</script>
</c:when>
<c:when test="${forward.name == forward_fail}">
	<script>alert("<fmt:message key='fms.tran.msg.failToUpdateRecord'/>");</script>
</c:when>
</c:choose>

<x:set name="EditVehicle.form" property="action" value="<%= VehicleForm.VEHICLE_ACTION_EDIT %>"/>
<x:set name="EditVehicle.form" property="cancelUrl" value="ViewVehicleDetail.jsp?id=${vehicleID}"/>
<x:set name="EditVehicle.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>
<x:set name="EditVehicle.form" property="vehicle_num" value="${vehicleID}"/>


<script language="JavaScript">
	function hideShowPM(){
		var radiobtn = document.forms['EditVehicle.form'].elements['EditVehicle.form.maintenanceType'];
		var month = document.getElementById("monthField");
		var km = document.getElementById("kmField"); 
			
		if(radiobtn[0].checked){
			km.style.display = '';
			month.style.display = 'none';
		}else{
			km.style.display = 'none';
			month.style.display = '';
		}
			
	}
</script>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.editVehicleDetails'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="EditVehicle.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<script language="JavaScript">
	hideShowPM();
</script>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>