 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="request">
        <com.tms.fms.transport.ui.RequestForm name="requestform"/>
		<com.tms.fms.transport.ui.StatusTrail name="statusTrail" width="100%"/>
     </page>
</x:config>

<c:if test="${! empty param.requestId}">
    <c:set var="requestId" value="${param.requestId}"></c:set>
	<x:set name="request.statusTrail" property="requestId" value="${requestId}"/>
</c:if>


<c:if test="${forward.name == 'Back'}" >
  
  <c:redirect url="requestListing.jsp"/>
    
</c:if>

<c:if test="${forward.name == 'Cancel Request'}">
    <script>        
        window.open('cancelForm.jsp?requestId=<c:out value="${requestId}"/>','','resizable=no,width=500,height=400,menubar=no,toolbar=no');              
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
	<c:if test="${! empty param.requestId}">
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