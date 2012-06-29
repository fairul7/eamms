<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_printReqassignmentDetailsPage">
    <com.tms.fms.engineering.ui.PrintAssignmentByGroupAssignmentId name="details" width="100%"/>
  </page>
</x:config>

 
<c:if test="${not empty(param.requestId)}">
	<x:set name="fms_printReqassignmentDetailsPage.details" property="requestId" value="${param.requestId}"/>
</c:if>

<x:set name="fms_printReqassignmentDetailsPage.details" property="page" value="${widgets['fms_todaysassignmentDetailsPage.details'].page}"/>
<x:set name="fms_printReqassignmentDetailsPage.details" property="reqFrom" value="${widgets['fms_todaysassignmentDetailsPage.details'].startDate}"/>
<x:set name="fms_printReqassignmentDetailsPage.details" property="reqTo" value="${widgets['fms_todaysassignmentDetailsPage.details'].endDate}"/>
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
        <x:display name="fms_printReqassignmentDetailsPage.details"/>
    </td></tr>
    
</table>

<jsp:include page="includes/footer.jsp" />
