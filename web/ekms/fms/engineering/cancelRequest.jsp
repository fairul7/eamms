<%@ include file="/common/header.jsp" %>
<x:config>
	<page name="fms_cancelRequestPage">
    	<com.tms.fms.engineering.ui.CancelRequestForm name="add" width="100%"/>
	</page>
</x:config>

<c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
<x:set name="fms_cancelRequestPage.add" property="requestId" value="${requestId}"/>


<c:choose>
  <c:when test="${forward.name == 'APPLIED-CANCELLATION'}">
	<script>
		alert('<fmt:message key="fms.facility.msg.requestAppliedForCancellation"/>'); 
	    window.opener.location="requestDetails.jsp";
    	window.close();
    </script>
  </c:when>
	<c:when test="${forward.name == 'CANCELLED'}">
	<script>
		alert('<fmt:message key="fms.facility.msg.requestCancelled"/>'); 
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
  <c:when test="${forward.name == 'CANCEL'}">
    <script>window.close();</script>
  </c:when>
</c:choose>
<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.cancelRequest"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_cancelRequestPage.add"/>
    </td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
