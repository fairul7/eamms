<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_requestModify">
    <com.tms.fms.engineering.ui.RequestDetailsForm name="details" modifyMode="true" width="100%"/>
    <com.tms.fms.engineering.ui.StatusTrail name="statusTrail" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestModify.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_requestModify.details" property="requestId" value="${requestId}"/>
<x:set name="fms_requestModify.statusTrail" property="requestId" value="${requestId}"/>
<x:set name="fms_requestModify.details" property="fcEditMode" value="${true}"/>

<c:choose>
  <c:when test="${forward.name == 'consistencyError'}">
    <script type="text/javascript">
      alert("Consistency Error: Please do not use multi-tab.");
      document.location = "<c:url value="requestServiceModify.jsp?requestId=${requestScope.postedRequestId}"/>";
    </script>
  </c:when>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestUpdated"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestNotUpdate"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'REJECTED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestRejected"/>');
    document.location = "<c:url value="requestDetails.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'APPROVED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestApproved"/>');
    document.location = "<c:url value="hodPendingRequestListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'SUBMITTED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestSubmitted"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'ACCEPTED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestAccepted"/>'); 
    document.location = "<c:url value="incomingRequestFCListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'COPY'}">
    <script>document.location = "<c:url value='requestEdit.jsp?requestId=${requestId}'/>";</script>
  </c:when>
  <c:when test="${forward.name == 'ASSIGNMENT_PREPARED'}">
    <script>document.location = "<c:url value='requestAssignmentDetails.jsp?requestId=${requestId}'/>";</script>
  </c:when>
  <c:when test="${forward.name == 'checkAvailability'}">
    <script>document.location = "<c:url value="checkAvailabilityFCfacility.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'serviceNotAvailable'}">
    <script>
    	window.open('serviceNotAvailable.jsp?id=<c:out value="${requestId}" />', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450');
    </script>
  </c:when>
  <c:when test="${forward.name == 'cancelRequest'}">
    <script>
    	window.open('cancelRequest.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=250');
    </script>
  </c:when>
  <c:when test="${forward.name == 'assignFC'}">
    <script>
    	window.open('assignFC.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=270');
    </script>
  </c:when>
  <c:when test="${forward.name == 'rejectFC'}">
    <script>
    	window.open('requestFCreject.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=400');
    </script>
  </c:when>
  <c:when test="${forward.name == 'lateFC'}">
    <script>
    	window.open('requestFClate.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=250');
    </script>
  </c:when>
  <c:when test="${forward.name == 'cancelFC'}">
    <script>
    	window.open('requestFCcancel.jsp', 'serviceADDOpener', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=250');
    </script>
  </c:when>
  <c:when test="${forward.name == 'viewOutsource'}">
    <script>
    	window.open('viewOutsource.jsp', 'viewOutsource', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=550, height=300');
    </script>
  </c:when>
  <c:when test="${forward.name == 'serviceEmpty'}">
    <script>alert('<fmt:message key="fms.facility.msg.serviceEmpty"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'invalidServiceDate'}">
    <script>alert('<fmt:message key="fms.facility.msg.invalidServiceDate"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'invalidServiceItem'}">
    <script>alert('<fmt:message key="fms.facility.msg.invalidServiceItem"/>');</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.requestDetails"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_requestModify.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_requestModify.statusTrail"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>