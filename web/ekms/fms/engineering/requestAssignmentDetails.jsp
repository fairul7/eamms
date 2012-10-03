<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="fms_requestAssignmentDetailsPage">
    <com.tms.fms.engineering.ui.AssignmentDetailsForm name="details" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestAssignmentDetailsPage.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_requestAssignmentDetailsPage.details" property="requestId" value="${requestId}"/>

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
</script>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestUpdated"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
	<c:when test="${forward.name == 'PRINT' }">
		<script>
			pops('mains', 'printAssignmentDetails.jsp?requestId=<c:out value="${requestId}" />', 600, 756)
		</script>
	</c:when>
	<c:when test="${forward.name == 'AUTOASSIGN' }">
		<script>
			alert('<fmt:message key="fms.facility.autoAssign.alert"/>');
			document.location = "<c:url value='requestAssignmentDetails.jsp?requestId=${requestId}'/>";
		</script>
	</c:when>
	<c:when test="${forward.name == 'AUTOASSIGNSKIP' }">
        <script>
            alert('<fmt:message key="fms.facility.autoAssignSkip.alert"/>');
            document.location = "<c:url value='requestAssignmentDetails.jsp?requestId=${requestId}'/>";
        </script>
    </c:when>
	<c:when test="${forward.name == 'AUTOASSIGNNOTFOUND' }">
        <script>
            alert('<fmt:message key="fms.facility.autoAssignNotFound.alert"/>');
            document.location = "<c:url value='requestAssignmentDetails.jsp?requestId=${requestId}'/>";
        </script>
    </c:when>
	<c:when test="${forward.name == 'FAILED' }">
		<script>
			alert('<fmt:message key="fms.facility.autoAssign.alert.failed"/>');
		</script>
	</c:when>
</c:choose>

<script src="<c:url value="/ekms/includes/jquery-1.4.2.min.js"/>" type="text/javascript"></script>

<script >
$(function() {
	$(".test").click(function() {
		//alert("masupppp"); 
		var coba = $(this).attr('id'); 
		var reqId = $("#reqId").val();
		var servType = $("#serviceId_"+coba).val();
		var facilityId =$("#facilityId_"+coba).val();
		var statusLbl =$("#statusLab_"+coba).val();
		var dataString = 'assignmentId='+ coba+"&serviceType="+servType+"&facilityId="+facilityId+"&status="+statusLbl;
		//var dataString = 'assignmentId='+ coba+"&serviceType="+servType+"&facilityId="+facilityId;
		//alert(coba);
		//alert('request --'+reqId);  
		//alert('facility --'+facilityId);  
		//alert(servType);  
		//alert(dataString);  

		var doIt=confirm('This will cancel the selected assignment.\nDo you want to continue?');
		if(doIt){
			$.ajax({
				type: "POST",
				url: "deleteConfirmation.jsp",
				data: dataString,
				cache: false,
				success: function(html){
				location = "requestAssignmentDetails.jsp?requestId="+reqId; 
				}
			});
			
			}	
		})
	
});

</script>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.assignmentDetails"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_requestAssignmentDetailsPage.details"/>
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>