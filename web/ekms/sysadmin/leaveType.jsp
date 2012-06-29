<%@include file="/common/header.jsp"%>

<x:config>
    <page name="leave">
    	<com.tms.ekms.manpowertemp.ui.LeaveTypeSetup name="typeform"/>
    </page>
</x:config>

<c:if test="${forward.name=='save'}" >
<script>
	alert("Leave Type Added");   
	document.location = "leaveTypeView.jsp";
</script>
</c:if>

<c:if test="${forward.name=='edit'}" >
<script>
	alert("Updated Leave Type");   
	document.location = "leaveTypeView.jsp";
</script>
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
<script>
    document.location = "leaveTypeView.jsp";
</script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="contentBgColor">
  <tr valign="MIDDLE">
    	<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
    		<fmt:message key='fms.manpower.form.leaveTypeSetup'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
    <x:display name="leave.typeform" ></x:display>
 </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
