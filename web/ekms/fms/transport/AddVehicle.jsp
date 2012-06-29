<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.model.VehicleObject, com.tms.fms.transport.ui.VehicleForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<c-rt:set var="forward_succ" value="<%= VehicleForm.VEHICLE_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= VehicleForm.VEHICLE_ADD_FAIL %>"/>
<c-rt:set var="forward_exist" value="<%= VehicleForm.VEHICLE_ADD_EXIST %>"/>

<x:config>
    <page name="AddVehicle">
    	<com.tms.fms.transport.ui.VehicleForm name="form"/>
    </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("<fmt:message key='fms.tran.msg.recordAdded'/>");document.location = "<c:url value="RoadTax.jsp?vid=${widgets['AddVehicle.form'].vehicle_num}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("<fmt:message key='fms.tran.msg.failToAddRecord'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_exist}">
		<script>alert("<fmt:message key='fms.tran.msg.errorVehicleNumShouldBeUnique'/>");</script>
	</c:when>
</c:choose>

<x:set name="AddVehicle.form" property="action" value="<%= VehicleForm.VEHICLE_ACTION_ADD %>"/>
<x:set name="AddVehicle.form" property="cancelUrl" value="index.jsp"/>
<x:set name="AddVehicle.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<script language="JavaScript">
	function hideShowPM(){
		var radiobtn = document.forms['AddVehicle.form'].elements['AddVehicle.form.maintenanceType'];
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
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.addVehicleForm'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="AddVehicle.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<script language="JavaScript">
	hideShowPM();
</script>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>