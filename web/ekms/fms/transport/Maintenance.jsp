<%@ page import="java.util.ArrayList,
		         kacang.ui.WidgetManager, 
		         java.util.Collection,
		         java.util.Iterator,
		         com.tms.fms.transport.model.*,
		         com.tms.fms.transport.ui.MaintenanceForm, 
		         com.tms.fms.transport.ui.MaintenanceTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.manager" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Maintenance">
    	<com.tms.fms.transport.ui.MaintenanceForm name="form"/>
        <com.tms.fms.transport.ui.MaintenanceTable name="table" width="100%"/>
    </page>
</x:config>

<c-rt:set var="forward_succ" value="<%= MaintenanceForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= MaintenanceForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_edit_succ" value="<%= MaintenanceForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_edit_fail" value="<%= MaintenanceForm.FORWARD_EDIT_FAIL %>"/>
<c-rt:set var="forward_listing_add" value="<%= MaintenanceTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= MaintenanceTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= MaintenanceTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<%
	Collection lstWorkshop = null;
	TransportDao dao = (TransportDao) Application.getInstance().getModule(TransportModule.class).getDao();
	try{lstWorkshop = dao.selectSetupObject(SetupObject.SETUP_WORKSHOP, "", "1", "name", false, 0, -1);}catch(Exception e){}
%>

<script language="JavaScript">
	var lstSize = <%=lstWorkshop.size() + 1%>;
	var lstWorkshopId = new Array(lstSize);
	var lstWSAddress = new Array(lstSize);
	
	<%
	if (lstWorkshop.size() > 0) {
		int j=0;
		for (Iterator i=lstWorkshop.iterator(); i.hasNext();) {
	    	SetupObject o = (SetupObject)i.next();
	    	%>
				lstWorkshopId[<%=j%>] = "<%=o.getSetup_id()%>";
				lstWSAddress[<%=j%>] = "<%=o.getDescription()%>";
			<%
			j++;
	    }
	}
	%>
	
	function updateAddress(){
		var WorkshopSelectBox = document.forms['Maintenance.form'].elements['Maintenance.form.sbWorkshop'];
		var WSAddressLabel = document.forms['Maintenance.form'].elements['Maintenance.form.tbWSAddress'];
		
		var selectedWorkshop = WorkshopSelectBox.options[WorkshopSelectBox.selectedIndex].value;
		
		if(selectedWorkshop != ""){
			for(i=0; i<lstSize; i++){
				if(selectedWorkshop == lstWorkshopId[i]){
					WSAddressLabel.value=lstWSAddress[i];
				}
			}
		}
	}
</script>

<c:choose>
  <c:when test="${not empty(param.vid)}">
  	<c:set var="vehicleID" value="${param.vid}"/>
  </c:when>
  <c:otherwise>
	<c:set var="vehicleID" value="${widgets['Maintenance.form'].vehicle_num}"/>
  </c:otherwise>
</c:choose>

<x:set name="Maintenance.form" property="vehicle_num" value="${vehicleID}"/>
<x:set name="Maintenance.table" property="vehicle_num" value="${vehicleID}"/>

<c:set var="formAction" value="${widgets['Maintenance.form'].action}"/>
<x:set name="Maintenance.form" property="cancelUrl" value="ViewVehicleDetail.jsp?id=${vehicleID}"/>
<c:choose>
  <c:when test="${(param.action == 'edit' || formAction == 'form.action.edit') && param.action != 'add'}">
  	<x:set name="Maintenance.form" property="action" value="<%= MaintenanceForm.FORM_ACTION_EDIT %>"/>
  	<x:set name="Maintenance.form" property="cancelUrl" value="Maintenance.jsp?action=add&vid=${vehicleID}"/>
  	<c:choose>
  	  <c:when test="${not empty(param.rid)}">
  	  	<c:set var="recordID" value="${param.rid}"/>
      </c:when>
      <c:otherwise>
      	<c:set var="recordID" value="${widgets['Maintenance.form'].id}"/>
      </c:otherwise>
    </c:choose>
    <x:set name="Maintenance.form" property="id" value="${recordID}"/>
  </c:when>
  <c:otherwise>
  	<x:set name="Maintenance.form" property="action" value="<%= MaintenanceForm.FORM_ACTION_ADD %>"/>
  </c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == forward_edit_succ}">
		<x:set name="Maintenance.form" property="action" value="<%= MaintenanceForm.FORM_ACTION_ADD %>"/>
		<script>alert("Maintenance service record updated!"); document.location = "<c:url value="Maintenance.jsp?action=add&vid=${vehicleID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_edit_fail}">
		<script>alert("Error! Fail to update maintenance service record.");</script>
	</c:when>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Maintenance service record added!"); document.location = "<c:url value="Maintenance.jsp?action=add&vid=${vehicleID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Error! Fail to add maintenance service record.");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="Maintenance.jsp?action=add&vid=${vehicleID}"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("Delete successfully");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("Error! Fail to delete one or more record.");</script>
	</c:when>
</c:choose>

<c:if test="${!empty param.id}">
	<c:redirect url="Maintenance.jsp?action=edit&vid=${vehicleID}&rid=${param.id}"/>
</c:if>

<x:set name="Maintenance.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.maintenanceService'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Maintenance.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.maintenanceServiceListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Maintenance.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>

<script language="JavaScript">
	document.forms['Maintenance.form'].elements['Maintenance.form.tbWSAddress'].readOnly=true;
</script>