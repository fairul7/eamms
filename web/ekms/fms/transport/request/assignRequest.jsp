 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="request">
        <com.tms.fms.transport.ui.AdminForm name="requestform"/>
        <com.tms.fms.transport.ui.CheckAvailabilityVehicles name="check"/>
     </page>
</x:config>



<c:if test="${! empty param.id}">
    <c:set var="id" value="${param.id}"></c:set>
</c:if>


<c:if test="${forward.name == 'Back'}" >  
  <c:redirect url="incomingListing.jsp"/>    
</c:if>

<c:if test="${forward.name == 'Approve'}" >
  <script>
  		alert('Approved');
  		document.location="pendingListing.jsp";	
   </script> 
</c:if>

<c:if test="${forward.name == 'Reject'}" >
  <script>
  		alert('Rejected');
  		document.location="pendingListing.jsp";	
   </script> 
</c:if>

<c:if test="${forward.name == 'Cancel'}" >  
  <c:redirect url="incomingListing.jsp"/>    
</c:if>
  
<c:choose>
	<c:when test="${forward.name == 'vehicles_empty'}">
		<script>alert("Please select at least one vehicle.");</script>
	</c:when>
</c:choose>


<c:choose>
	<c:when test="${forward.name == 'vehicles'}">
		
			<c:forEach var="key" items="${selectedKeys}" varStatus="i">				
				<c:set var="selectedKeys" value="${selectedKeys}"/>
			</c:forEach>
				
			<c:if test="${! empty selectedUser}" >  
			 <c:set var="drivers" value="${selectedUser}"/>		   
			</c:if>
			
			<c:if test="${! empty startDate}" >  
			 <c:set var="startDate" value="${startDate}"/>		   
			</c:if>
			
			<c:if test="${! empty endDate}" >  
			 <c:set var="endDate" value="${endDate}"/>		   
			</c:if>
			
			<c:if test="${! empty requestId}" >  
			 <c:set var="requestId" value="${requestId}"/>		   
			</c:if>
			

		<script type="text/javascript">
			document.forms['check'].submit();
		</script>	
	</c:when>
</c:choose>

<x:set name="request.check" property="vehicle_num" value="${selectedKeys}"/>
<x:set name="request.check" property="driver_id" value="${drivers}"/>
<x:set name="request.check" property="startDate" value="${startDate}"/>
<x:set name="request.check" property="endDate" value="${endDate}"/>
<x:set name="request.check" property="requestId" value="${requestId}"/>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<style type="text/css">
	select {
		width : 200px;
	}
</style>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.vehicleManpower'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="request.requestform" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.viewAvailability'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="request.check" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>