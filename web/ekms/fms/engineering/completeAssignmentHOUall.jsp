<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_completeAssignmentHOUall">
    	<com.tms.fms.engineering.ui.CompleteAssignmentHOU name="update" width="100%"/>
  </page>
</x:config>

<c:choose>
	<c:when test="${forward.name == 'COMPLETE'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.itemCompletedSuccessful'/>");
			window.opener.location='assignmentListing.jsp';		
			window.close();
		</script>
	</c:when>
	<c:when test="${forward.name == 'INVALID-COMPLETION-DATE'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.invalidCompletionDate'/>");
		</script>
	</c:when>
	<c:when test="${forward.name == 'UNFULFILLED'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.itemUnfulfilledSuccessful'/>");
			window.opener.location='assignmentListing.jsp';		
			window.close();
		</script>
	</c:when>
	<c:when test="${forward.name == 'INVALID-REMARKS'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.invalidReasonUnfulfilled'/>");
		</script>
	</c:when>
	<c:when test="${forward.name == 'CANCEL'}">
		<script>
			window.close();
		</script>
	</c:when>
</c:choose>

<c:set var="type" value="View"/>
<c:choose>
  <c:when test="${not empty(param.groupId)}">
    <c:set var="groupId" value="${param.groupId}"/>
  </c:when>
</c:choose>
<x:set name="fms_completeAssignmentHOUall.update" property="groupId" value="${groupId}"/>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.label.assignmentDetails"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_completeAssignmentHOUall.update"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
