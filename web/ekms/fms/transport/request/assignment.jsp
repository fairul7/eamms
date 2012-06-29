 				
 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="editrequest">
        <com.tms.fms.transport.ui.AssignmentForm name="form"/>
     </page>
</x:config>

<c:if test="${! empty param.id}">
    <c:set var="id" value="${param.id}"></c:set>
</c:if>

<c:if test="${!empty param.flagId}" >
    <x:set name="editrequest.form" property="flagId" value="${param.flagId}" />
</c:if>

<c:choose>
	<c:when test="${!empty param.source}" >
		<x:set name="editrequest.form" property="source" value="${param.source}" />
	</c:when>
	<c:otherwise>
		<x:set name="editrequest.form" property="source" value="search" />
	</c:otherwise>
</c:choose>

<c:set var="source" value="${widgets['editrequest.form'].source}"></c:set>

<c:if test="${forward.name == 'Back'}" >
	<c:choose>
		<c:when test="${source == 'today'}">
			<c:redirect url="todayListing.jsp"/>
		</c:when>
  		<c:otherwise>
  			<c:redirect url="searchListing.jsp"/>
  		</c:otherwise>
   </c:choose>
</c:if>

<c:if test="${forward.name == 'Ok'}" >
  
  <c:redirect url="assignment.jsp"/>
   
</c:if>

<c:if test="${forward.name == 'invalid-end-meter'}">
	<script>
		alert("Invalid value of 'End Speedo Meter' field"); 
	</script>
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
	<x:display name="editrequest"> </x:display>
</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>