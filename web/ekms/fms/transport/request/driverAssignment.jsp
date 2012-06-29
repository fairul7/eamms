 				
 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="drivers">
        <com.tms.fms.transport.ui.DriversForm name="form"/>
     </page>
</x:config>

<c:if test="${! empty param.id}">
    <c:set var="id" value="${param.id}"></c:set>
</c:if>

<c:if test="${!empty param.flagId}" >
    <x:set name="drivers.form" property="flagId" value="${param.flagId}" />
</c:if>

<c:set var="id" value="${widgets['drivers.form'].id}"></c:set>

<c:if test="${! empty param.userId}">
    <c:set var="userId" value="${param.userId}"></c:set>
</c:if>

<c:if test="${forward.name == 'Back'}" >
  
  <c:redirect url="searchDriverAssignmentList.jsp"/>
   
</c:if>

<c:if test="${forward.name == 'Completed'}">    
  <script>
     document.location="completeAssignment.jsp?id=<c:out value="${id}"/>&userId=<c:out value="${userId}"/>";
  </script>  
</c:if>

<c:if test="${forward.name == 'Ok'}" >
  
  <c:redirect url="assignment.jsp"/>
   
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
	<x:display name="drivers"> </x:display>
</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

  
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>