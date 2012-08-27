<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_requestDetailsPage">
    <com.tms.fms.engineering.ui.RequestDetailsForm name="details" width="100%"/>
    <com.tms.fms.engineering.ui.StatusTrail name="statusTrail" width="100%"/>
  </page>
</x:config>

<x:set name="fms_requestDetailsPage.details" property="oldRequestId" value="${widgets['fms_requestPage.add'].oldRequestId}"/>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_requestDetailsPage.details" property="requestId" value="${requestId}"/>
<x:set name="fms_requestDetailsPage.statusTrail" property="requestId" value="${requestId}"/>

<c:choose>
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
  <c:when test="${forward.name == 'INVALID_RATE_CARD'}">
    <script>alert('<fmt:message key="fms.facility.msg.invalidRateCardCannotAccept"/>');</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<script language="JavaScript">
	function pops(nama,jangkar,h,w){  
		self.name = nama;
		var nX = (screen.availWidth - w)/2;
		var nY = (screen.availHeight - h)/2;
		viewWin = window.open(jangkar , "viewWin", 'height='+ h + ', width='+ w + 
			', screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +
			', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
		viewWin.focus();  
	}

	function closeRemote() {	
		window.opener.location.reload();
		timer = setTimeout('window.close();', 10);		
	}

	<!--script>closeRemote();</script-->
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.requestDetails"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_requestDetailsPage.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_requestDetailsPage.statusTrail"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <jsp:include page="../eamms/feedRequisitionStatus.jsp">
            <jsp:param name="requestId" value="${requestId}"/>
        </jsp:include>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>