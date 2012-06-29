<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_todaysassignmentDetailsPage">
    <com.tms.fms.engineering.ui.AssignmentDetailsFormByRequest name="details" width="100%"/>
  </page>
</x:config>


<c:if test="${not empty(param.requestId)}">
	<x:set name="fms_todaysassignmentDetailsPage.details" property="requestId" value="${param.requestId}"/>
</c:if>
<c:set var="requestId" value="${widgets['fms_todaysassignmentDetailsPage.details'].requestId}" />

<x:set name="fms_todaysassignmentDetailsPage.details" property="requestId" value="${requestId}"/>

<c:if test="${!empty param.assignmentId}">
	<x:set name="fms_todaysassignmentDetailsPage.details" property="assignmentId" value="${param.assignmentId}"/>
</c:if>

<c:if test="${!empty param.assignmentEquipmentId}">
	<x:set name="fms_todaysassignmentDetailsPage.details" property="assignmentEquipmentId" value="${param.assignmentEquipmentId}"/>
</c:if>

<c:if test="${!empty param.action}">
	<x:set name="fms_todaysassignmentDetailsPage.details" property="action" value="${param.action}"/>
</c:if>

<c:if test="${!empty param.page}">
	<x:set name="fms_todaysassignmentDetailsPage.details" property="page" value="${param.page}"/>
</c:if>


<c:if test="${forward.name == 'notUtilized'}">
    <script>
    	window.open('notUtilized.jsp?requestId=<c:out value="${requestId}"/>', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=250');
    </script>
</c:if>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestUpdated"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
</c:choose>

<c:if test="${forward.name == 'PRINTOUT'}">
		<script>// alert("<fmt:message key='fms.facility.msg.itemCheckOutSuccessful'/>");
		var nX = (screen.availWidth - 700)/2;
		var nY = (screen.availHeight - 500)/2; 
		
		// document.location = "<c:url value="requestDetails.jsp?requestId=${id}&page=${page}&action=${param.action}"/>";
		window.open('<c:url value="printCheckoutByReqId.jsp?requestId=${requestId}"/>', 'print', 'height=500, width=700, screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
		</script>
	</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
	<c:set var="pageText" value="${widgets['fms_todaysassignmentDetailsPage.details'].pageText}" />
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.assignmentDetails"/> (<c:out value="${pageText}"/>)</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
   
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_todaysassignmentDetailsPage.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>