<%@include file="/common/header.jsp"%>

<x:config>
    <page name="holeavePage">
    	<com.tms.ekms.manpowertemp.ui.SetHolidayLeaveForm name="holeaveForm"/>
    </page>
</x:config>

<!-- x:set name="holeavePage.holeaveForm" property="cancelUrl" value="holidayView.jsp"/-->

<c:if test="${forward.name=='saveLeave'}" >
<script>
	alert("Leave Added");
	document.location = "/ekms/fms/dutyroster/leaveView.jsp";
</script>
</c:if> 

<c:if test="${forward.name=='saveHoliday'}" >
<script>
	alert("Holiday Added");   
	document.location = "holidayView.jsp";
</script>
</c:if> 

<c:if test="${forward.name=='duplicateHoliday'}" >
<script>
	alert("Duplicate Holiday");   
</script>
</c:if> 

<c:if test="${forward.name=='saveLeaveFailed'}" >
<script>
	alert("Please select at least one manpower !");   
</script>
</c:if> 

<c:if test="${forward.name=='updateLeave'}" >
<script>
	alert("Leave Updated");   
	document.location = "/ekms/fms/dutyroster/leaveView.jsp";
</script>
</c:if> 

<c:if test="${forward.name=='updateHoliday'}" >
<script>
	alert("Holiday Updated");   
	document.location = "holidayView.jsp";
</script>
</c:if> 

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contentBgColor">
  <tr valign="MIDDLE">
    	<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
    		<fmt:message key='fms.manpower.form.holidayLeaveSetup'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
    <x:display name="holeavePage.holeaveForm" ></x:display>
 </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
