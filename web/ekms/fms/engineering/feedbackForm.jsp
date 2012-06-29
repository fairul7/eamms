<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_requestDetailsPage">
    <com.tms.fms.engineering.ui.FeedBackForm name="details" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_requestDetailsPage.details" property="requestId" value="${requestId}"/>

<c:choose>
  <c:when test="${forward.name == 'SUBMITTED'}">
    <script>alert('<fmt:message key="fms.facility.msg.feedbackSubmitted"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.feedBackForm"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_requestDetailsPage.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>