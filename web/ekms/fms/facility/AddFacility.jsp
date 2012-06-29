<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.FacilityForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_succ" value="<%= FacilityForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= FacilityForm.FORWARD_ADD_FAIL %>"/>

<x:config>
    <page name="AddFacility">
    	<com.tms.fms.facility.ui.FacilityForm name="form"/>
    </page>
</x:config>


<script language="JavaScript">
	function hideShowChild(){
		var radiobtn = document.forms['AddFacility.form'].elements['AddFacility.form.pnChild.rdChildGroup'];
		var tr = document.getElementById("child");
	
		if(radiobtn[0].checked){
			tr.style.display = '';
		}else{
			tr.style.display = 'none';
		}
			
	}
	function hideShowPM(){
		var radiobtn = document.forms['AddFacility.form'].elements['AddFacility.form.pnPM.rdPMGroup1'];
		var tr = document.getElementById("pm");
		var radiobtn2 = document.forms['AddFacility.form'].elements['AddFacility.form.pnPM1.rdPMGroup2'];
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
		var radiobtn = document.forms['AddFacility.form'].elements['AddFacility.form.pnPM1.rdPMGroup2'];
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
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Facility Added!");document.location = "<c:url value="FacilityListing.jsp"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Error! Fail to add facility.");</script>
	</c:when>
</c:choose>

<x:set name="AddFacility.form" property="action" value="<%= FacilityForm.FORM_ACTION_ADD %>"/>
<x:set name="AddFacility.form" property="cancelUrl" value="index.jsp"/>
<x:set name="AddFacility.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.addFacilityEquipmentForm'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="AddFacility.form" ></x:display>
    
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