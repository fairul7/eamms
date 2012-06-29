<%@include file="/common/header.jsp" %>

<x:config>
     <page name="bulkAssignmentUpdate">
		<com.tms.fms.transport.ui.BulkAssignmentUpdateListForm name="form" width="100%"/>
     </page>
</x:config>

<c:if test="${forward.name == 'invalid-date'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.invalidDate"/>');
	</script>   
</c:if>

<c:if test="${forward.name == 'invalid-petrol-card'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.invalidPetrolCard"/>');
	</script>   
</c:if>

<c:if test="${forward.name == 'invalid-speedo-meter'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.invalidSpeedoMeter"/>');
	</script>   
</c:if>

<c:if test="${forward.name == 'invalid-number'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.invalidNumber"/>');
	</script>   
</c:if>

<c:if test="${forward.name == 'update-nothing'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.updateNothing"/>');
	</script>   
</c:if>

<c:if test="${forward.name == 'Update'}" >  
	<script>
		alert('<fmt:message key="fms.tran.msg.recordUpdated"/>');
		document.location="bulkAssignment.jsp";	
	</script>   
</c:if>

  	<c:if test="${not empty(param.requestId)}">
    	<x:set name="bulkAssignmentUpdate.form" property="requestId" value="${param.requestId}"/>
  	</c:if>
  	<c:if test="${not empty(param.startDate)}">
    	<x:set name="bulkAssignmentUpdate.form" property="startDate" value="${param.startDate}"/>
  	</c:if>
	<c:if test="${not empty(param.endDate)}">
		<x:set name="bulkAssignmentUpdate.form" property="endDate" value="${param.endDate}"/>	
	</c:if>
	<c:if test="${not empty(param.dept)}">
		<x:set name="bulkAssignmentUpdate.form" property="dept" value="${param.dept}"/>
	</c:if>
	<c:if test="${not empty(param.prog)}">
		<x:set name="bulkAssignmentUpdate.form" property="prog" value="${param.prog}"/>
	</c:if>


<%@include file="/ekms/includes/header.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.tran.bulkAssignmentUpdate'/>
        </td>
    	<td align="right" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
    		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="bulkAssignmentUpdate.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
