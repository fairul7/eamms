<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_printAssignmentDetailsPage">
    <com.tms.fms.engineering.ui.PrintAssignmentDetailsForm name="details" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_printAssignmentDetailsPage.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_printAssignmentDetailsPage.details" property="requestId" value="${requestId}"/>

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
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.assignmentDetails"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_printAssignmentDetailsPage.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
