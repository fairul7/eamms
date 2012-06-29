<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="assignmentCheckIn">
    <com.tms.fms.engineering.ui.CheckInRequestForm name="form" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
	<c:set var="id" value="${widgets['fms_todaysassignmentDetailsPage.details'].requestId}" />
  </c:otherwise>
</c:choose>

<x:set name="assignmentCheckIn.form" property="requestId" value="${id}"/>

<c:choose>
	<c:when test="${not empty(param.page) and param.page == 'all'}">
		<c:set var="page" value="${param.page}"/>
		<x:set name="assignmentCheckIn.form" property="page" value="${page}"/>
	</c:when>
	<c:otherwise>
	<c:set var="page" value="${widgets['fms_todaysassignmentDetailsPage.details'].page}"/>
		<x:set name="assignmentCheckIn.form" property="page" value="${page}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == 'more'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckInSuccessful'/>");</script>
	</c:when>
	<c:when test="${forward.name == 'success'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckInSuccessful'/>");document.location = "<c:url value="requestDetails.jsp?requestId=${id}&page=${page}"/>";</script>
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
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="assignmentCheckIn.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>