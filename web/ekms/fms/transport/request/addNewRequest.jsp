 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="request">
        <com.tms.fms.transport.ui.RequestForm name="requestform"/>
		<com.tms.fms.transport.ui.StatusTrail name="statusTrail" width="100%"/>
     </page>
</x:config>

<c:if test="${! empty param.id}">
    <c:set var="requestId" value="${param.id}"></c:set>
	<c:set var="id" value="${param.id}"></c:set>
	<x:set name="request.statusTrail" property="requestId" value="${requestId}"/>
</c:if>

<c:if test="${forward.name == 'Cancel'}" >  
  <c:redirect url="requestListing.jsp"/>   
</c:if>

<c:if test="${forward.name == 'Back'}" >  
  <c:redirect url="requestListing.jsp"/>   
</c:if>

<c:if test="${forward.name == 'Draft'}" >  
  <c:redirect url="requestListing.jsp"/>   
</c:if>

<c:if test="${forward.name == 'SaveDraft'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.requestSavedAsDraft"/>');
		document.location="requestListing.jsp";	
	</script>   
</c:if>

<c:if test="${forward.name == 'SavePending'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.requestSubmitted"/>');
		document.location="requestListing.jsp";	
	</script>   
</c:if>

<c:if test="${forward.name == 'SavePendingFailed'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.vehicleRequired"/>');
		document.location='addNewRequest.jsp?id=<c:out value="${id}" />';	
	</script>   
</c:if>

<c:if test="${forward.name == 'Save'}" >  
  <c:redirect url="detailsRequest.jsp?id=${id}&view=submit"/>   
</c:if>

<c:if test="${forward.name == 'Back to Dept List'}" >  
  <c:redirect url="addNewRequest.jsp?id=${param.id}"/>    
</c:if>

<c:if test="${forward.name == 'Deleted'}" >  
  <script language="VBScript">
		msgAnswer = MsgBox("Are you sure you want to delete this record?", vbYesNo, "Delete Request")
	</script>
	
	<script language="JavaScript">
		if(msgAnswer==6)
		{			     
		     document.location="deleteRequest.jsp?id=<c:out value="${id}"/>";	
		}
		else if (msgAnswer==7)
		{			
			 document.location="editRequest.jsp?id=<c:out value="${id}"/>";	
		}
	</script>
   
   
</c:if>
  
 <%@include file="/ekms/includes/header.jsp" %> 
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">

  <tr valign="MIDDLE">
    <td height="22" class="contentTitleFont">
      &nbsp;<fmt:message key='fms.tran.requestForTransport'/>  
      
    </td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
    <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
	<x:display name="request.requestform"> </x:display>
</td>
  </tr>
	<c:if test="${! empty param.id}">
	  <tr>
	    <td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="request.statusTrail"> </x:display>
		</td>
	  </tr>
	</c:if>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>