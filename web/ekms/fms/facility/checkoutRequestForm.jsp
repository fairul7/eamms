<%@ include file="/common/header.jsp" %>



<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<x:config>
  <page name="assignmentCheckOut">
    <com.tms.fms.engineering.ui.CheckOutRequestForm name="form" width="100%"/>
  </page>
</x:config>


<c:if test="${not empty(param.id)}">
	<x:set name="assignmentCheckOut.form" property="requestId" value="${param.id}"/>
</c:if>
<c:set var="id" value="${widgets['assignmentCheckOut.form'].requestId}" />

<c:if test="${not empty(param.action)}">
	<x:set name="assignmentCheckOut.form" property="action" value="${param.action}"/>
</c:if>
<c:set var="action" value="${widgets['assignmentCheckOut.form'].action}" />

<c:if test="${not empty(param.page)}">
	<x:set name="assignmentCheckOut.form" property="page" value="${param.page}"/>
</c:if>
<c:set var="page" value="${widgets['assignmentCheckOut.form'].page}" />


<c:choose>
	<c:when test="${forward.name == 'consistencyError'}">
		<script type="text/javascript">
			alert("Consistency Error: Please do not use multi-tab.");
			document.location = "<c:url value="checkoutRequestForm.jsp?id=${requestScope.postedRequestId}&page=${page}&action=${action}"/>";
		</script>
	</c:when>
	<c:when test="${forward.name == 'more'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.itemCheckOutSuccessful'/>");
			//document.location = "<c:url value="checkoutRequestForm.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		</script>
	</c:when>
	<c:when test="${forward.name == 'more_prepare'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemPrepareCheckOutSuccessful'/>");</script>
	</c:when>
	<c:when test="${forward.name == 'success'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckOutSuccessful'/>");
		var nX = (screen.availWidth - 700)/2;
		var nY = (screen.availHeight - 500)/2;
		
		document.location = "<c:url value="requestDetails.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		window.open('<c:url value="printCheckoutByRequestId.jsp?requestId=${id}&page=${page}"/>', 'print', 'height=500, width=700, screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
		</script>
	</c:when>
	<c:when test="${forward.name == 'success_extra'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemCheckOutWithExtraNotice'/>");
		var nX = (screen.availWidth - 700)/2;
		var nY = (screen.availHeight - 500)/2;
		
		document.location = "<c:url value="requestDetails.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		window.open('<c:url value="printCheckoutByRequestId.jsp?requestId=${id}&page=${page}"/>', 'print', 'height=500, width=700, screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
		</script>
	</c:when>
	<c:when test="${forward.name == 'successPrepare'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemPrepareCheckOutSuccessful'/>");
		var nX = (screen.availWidth - 700)/2;
		var nY = (screen.availHeight - 500)/2;
		
		document.location = "<c:url value="requestDetails.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		
		</script>
	</c:when>
	<c:when test="${forward.name == 'success_extra_prepare'}">
		<script>alert("<fmt:message key='fms.facility.msg.itemPrepareCheckOutWithExtraNotice'/>");
		var nX = (screen.availWidth - 700)/2;
		var nY = (screen.availHeight - 500)/2;
		
		document.location = "<c:url value="requestDetails.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		
		</script>
	</c:when>
	<c:when test="${forward.name == 'fail'}">
		<script>alert("<c:out value="${message}"/>");</script>
	</c:when>
	<c:when test="${forward.name == 'fail_extra_prepare'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.itemPrepareCheckOutWithExtraNoticeFail'/>");
			document.location = "<c:url value="checkoutRequestForm.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		</script>
	</c:when>
	<c:when test="${forward.name == 'fail_extra_checkout'}">
		<script>
			alert("<fmt:message key='fms.facility.msg.itemCheckOutWithExtraNoticeFail'/>");
			document.location = "<c:url value="checkoutRequestForm.jsp?requestId=${id}&page=${page}&action=${action}"/>";
		</script>
	</c:when>
</c:choose>



<script>

function nextbox(fldobj,nbox) {
	if(fldobj.value.length>=16) {
	document.getElementsByName(nbox)[0].focus()
	}
}

</script>
<c:out value="${itemName}"></c:out>
<!-- document.location = "<c:url value="InternalCheckOut.jsp?"/>"; -->

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
        	<fmt:message key="fms.label.assignmentCheckOut"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="assignmentCheckOut.form"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>