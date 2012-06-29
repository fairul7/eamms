<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="assignmentCheckIn">
    <com.tms.fms.engineering.ui.AssignmentDirectCheckIn name="form" width="100%"/>
    
    <com.tms.fms.facility.ui.AssignmentPreCheckin name="formPre" width="100%" />
    <com.tms.fms.facility.ui.AssignmentInfo name="info" width="100%" />
     <com.tms.fms.facility.ui.ItemNotCheckinTable name="table" width="100%" />
    
  </page>
</x:config>

<c:if test="${!empty param.requestId}">
	<c:set var="groupId" value="${param.requestId}"/>
  	<x:set name="assignmentCheckIn.table" property="requestId" value="${param.requestId}"/>
  	<x:set name="assignmentCheckIn.info" property="requestId" value="${param.requestId}"/>
</c:if>

<c:choose>
	<c:when test="${forward.name == 'more'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckInSuccessful'/>");</script>
	</c:when>
	<c:when test="${forward.name == 'success'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckInSuccessful'/>");
		document.location = "<c:url value="assignmentListing.jsp"/>";</script>
	</c:when>
	<c:when test="${forward.name == 'fail'}">
		<script>alert("<c:out value="${message}"/>");</script>
	</c:when>
</c:choose>

<script>

function nextbox(fldobj,nbox) {
	if(fldobj.value.length>=16) {
	document.getElementsByName(nbox)[0].focus()
	}
}

</script>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont">
		<b><font color="#FFCF63" class="contentTitleFont">
			<fmt:message key='fms.label.assignmentCheckIn'/>
		</font></b>
		</td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
     	<td valign="top" width="50%">
     		<table>
     			<tr><td><x:display name="assignmentCheckIn.formPre" ></x:display></td></tr>
     			<tr><td><x:display name="assignmentCheckIn.info" ></x:display></td></tr>
     			<tr><td><x:display name="assignmentCheckIn.table" ></x:display></td></tr>
     		</table>
		  </td>
	    <td valign="top">
		    <table>
			    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
			    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
			
			        <x:display name="assignmentCheckIn.form" ></x:display>
			    
			    </td></tr>
			  </table>
		  </td>
		 
	  </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>