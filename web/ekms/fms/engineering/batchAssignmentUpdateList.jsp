<%@include file="/common/header.jsp" %>

<x:config>
     <page name="batchAssignmentUpdate">
		<com.tms.fms.engineering.ui.BatchAssignmentUpdateListForm name="form" width="100%"/>
     </page>
</x:config>


  	<c:if test="${not empty(param.requestId)}">
    	<x:set name="batchAssignmentUpdate.form" property="requestId" value="${param.requestId}"/>
  	</c:if>
  	<c:if test="${not empty(param.startDate)}">
    	<x:set name="batchAssignmentUpdate.form" property="startDate" value="${param.startDate}"/>
  	</c:if>
	<c:if test="${not empty(param.endDate)}">
		<x:set name="batchAssignmentUpdate.form" property="endDate" value="${param.endDate}"/>	
	</c:if>
	<c:if test="${not empty(param.dept)}">
		<x:set name="batchAssignmentUpdate.form" property="dept" value="${param.dept}"/>
	</c:if>
	<c:if test="${not empty(param.prog)}">
		<x:set name="batchAssignmentUpdate.form" property="prog" value="${param.prog}"/>
	</c:if>


<%@include file="/ekms/includes/header.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.label.setBatchAssignmentUpdate'/>
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
			<x:display name="batchAssignmentUpdate.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
