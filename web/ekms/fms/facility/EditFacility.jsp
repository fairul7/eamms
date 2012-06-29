<%@ page import="java.util.ArrayList,
				 kacang.Application,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.WidgetManager,
		         com.tms.fms.facility.ui.FacilityForm,
		         com.tms.fms.department.model.*" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_succ" value="<%= FacilityForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= FacilityForm.FORWARD_EDIT_FAIL %>"/>

<x:config>
    <page name="EditFacility">
    	<com.tms.fms.facility.ui.FacilityForm name="form"/>
    </page>
</x:config>

<script language="JavaScript">
	function hideShowChild(){
		var radiobtn = document.forms['EditFacility.form'].elements['EditFacility.form.pnChild.rdChildGroup'];
		var tr = document.getElementById("child");
	
		if(radiobtn[0].checked){
			tr.style.display = '';
		}else{
			tr.style.display = 'none';
		}
			
	}
	function hideShowPM(){
		var radiobtn = document.forms['EditFacility.form'].elements['EditFacility.form.pnPM.rdPMGroup1'];
		var tr = document.getElementById("pm");
		var radiobtn2 = document.forms['EditFacility.form'].elements['EditFacility.form.pnPM1.rdPMGroup2'];
		if(radiobtn[0].checked){
			tr.style.display = '';
			radiobtn2[0].checked = true;
		}else{
			tr.style.display = 'none';
			radiobtn2[0].checked = true;
			radiobtn2[1].checked = false;
		}
			
	}
	
	function hideShowPMChild(){
		var radiobtn = document.forms['EditFacility.form'].elements['EditFacility.form.pnPM1.rdPMGroup2'];
		var month = document.getElementById("monthField");
		var year = document.getElementById("yearField"); 
			
		if(radiobtn[0].checked){
			month.style.display = '';
			year.style.display = 'none';
		}else{
			month.style.display = 'none';
			year.style.display = '';
		}
			
	}
</script>

<c:choose>
	<c:when test="${not empty(param.fid)}">
		<c:set var="facilityID" value="${param.fid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="facilityID" value="${widgets['EditFacility.form'].id}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Facility updated!");document.location = "<c:url value="ViewFacility.jsp?fid=${facilityID}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Error! Fail to update facility.");</script>
	</c:when>
</c:choose>

<x:set name="EditFacility.form" property="id" value="${facilityID}"/>
<x:set name="EditFacility.form" property="action" value="<%= FacilityForm.FORM_ACTION_EDIT %>"/>
<x:set name="EditFacility.form" property="cancelUrl" value="ViewFacility.jsp?fid=${facilityID}"/>
<x:set name="EditFacility.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>



<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.editFacilityEquipmentDetail'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="EditFacility.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<script language="JavaScript">
	hideShowChild();
	hideShowPM();
	hideShowPMChild();
</script>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>