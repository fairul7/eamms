 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="request">
        <com.tms.fms.transport.ui.AdminForm name="requestform"/>
		<com.tms.fms.transport.ui.StatusTrail name="statusTrail" width="100%"/>
     </page>
</x:config>

<c:if test="${! empty param.id}">
    <c:set var="requestId" value="${param.id}"></c:set>
	<x:set name="request.statusTrail" property="requestId" value="${requestId}"/>
</c:if>


<c:if test="${forward.name == 'Back'}" >
  
  <c:redirect url="pendingListing.jsp"/>
    
</c:if>

<c:if test="${forward.name == 'Approve'}" >
  <script>
  		alert('<fmt:message key="fms.tran.msg.requestApproved"/>');
  		document.location="pendingListing.jsp";	
   </script> 
</c:if>

<c:if test="${forward.name == 'Reject'}" >
  <script>
  		alert('<fmt:message key="fms.tran.msg.requestRejected"/>');
  		document.location="pendingListing.jsp";	
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
	<x:display name="request.requestform" ></x:display>
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