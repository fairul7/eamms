<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_printGroupassignmentDetailsPage">
    <com.tms.fms.engineering.ui.PrintAssignmentByGroupAssignmentId name="details" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_assignmentCheckOutDetails.form'].requestId}"/>
  </c:otherwise>
</c:choose>

<x:set name="fms_printGroupassignmentDetailsPage.details" property="requestId" value="${requestId}"/>
<x:set name="fms_printGroupassignmentDetailsPage.details" property="groupId" value="${widgets['fms_assignmentCheckOutDetails.form'].groupId}"/>
<x:set name="fms_printGroupassignmentDetailsPage.details" property="reqFrom" value="${widgets['fms_assignmentCheckOutDetails.form'].startDate}"/>
<x:set name="fms_printGroupassignmentDetailsPage.details" property="reqTo" value="${widgets['fms_assignmentCheckOutDetails.form'].endDate}"/>
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
	function Print() {
		document.body.offsetHeight;
		window.print();
	}
</script>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<body onload="Print()">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="102">
		&nbsp;
		</td>
        <td align="right">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#FFFFFF">
        <x:display name="fms_printGroupassignmentDetailsPage.details"/>
    </td></tr>
    
</table>

<jsp:include page="includes/footer.jsp" />
