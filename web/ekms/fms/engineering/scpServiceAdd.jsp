<%@ include file="/common/header.jsp" %>
<x:config>
	<page name="fms_scpPage">
    	<com.tms.fms.engineering.ui.ScpForm name="add" width="100%"/>
	</page>
</x:config>
<c:set var="type" value="Add"/>
<x:set name="fms_scpPage.add" property="type" value="${type}"/>

<c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
<c:set var="dtRequiredFrom" value="${widgets['fms_requestDetailsPage.details'].request.requiredFrom}"/>
<c:set var="dtRequiredTo" value="${widgets['fms_requestDetailsPage.details'].request.requiredTo}"/>

<x:set name="fms_scpPage.add" property="requestId" value="${requestId}"/>
<x:set name="fms_scpPage.add" property="dtRequiredFrom" value="${dtRequiredFrom}" />
<x:set name="fms_scpPage.add" property="dtRequiredTo" value="${dtRequiredTo}" />

<c:choose>
  <c:when test="${not empty(param.serviceId)}">
    <c:set var="serviceId" value="${param.serviceId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="serviceId" value="${widgets['fms_scpPage.add'].serviceId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_scpPage.add" property="serviceId" value="${serviceId}"/>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
	<script>
		alert('<fmt:message key="fms.facility.msg.bookingAdded"/>'); 
	    window.opener.location="requestDetails.jsp";
    	window.close();
    </script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingNotAdded"/>');</script>
  </c:when>
   <c:when test="${forward.name == 'MODIFIED'}">
    <script>
		alert('<fmt:message key="fms.facility.msg.bookingAdded"/>'); 
	    window.opener.location="requestServiceModify.jsp";
    	window.close();
    </script>
  </c:when>
</c:choose>
<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.msg.scpBooking"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_scpPage.add"/>
    </td>
</tr>
	<tr>
		<td colspan="2" align="right" bgcolor="#EFEFEF" class="contentBgColor">
			<b>Note</b> : Rehearsal Time apply for OB and MCP only  
		</td>
	</tr>
</table>
<jsp:include page="includes/footer.jsp" />
