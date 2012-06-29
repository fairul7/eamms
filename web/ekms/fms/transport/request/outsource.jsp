 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="request">
        <com.tms.fms.transport.ui.AdminForm name="requestform"/>
     </page>
</x:config>

<c:if test="${! empty param.requestId}">
    <c:set var="requestId" value="${param.requestId}"></c:set>
</c:if>


<c:if test="${forward.name == 'Back' || forward.name == 'Cancel'}" >  
  <c:redirect url="incomingListing.jsp"/>    
</c:if>


<c:if test="${forward.name == 'Outsource'}" > 

	<script>
		alert("Successfully Outsourced!");	
	</script> 
  <c:redirect url="incomingListing.jsp"/>    
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
	<x:display name="request" ></x:display>
</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>