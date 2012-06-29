<%@include file="/common/header.jsp" %>

<x:config>
     <page name="engineeringAssignment">
        <com.tms.fms.engineering.ui.UnfulfilledAssignmentForm name="form"/>
     </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['engineeringAssignment.form'].id}" />
  </c:otherwise>
</c:choose>

<x:set name="engineeringAssignment.form" property="id" value="${id}"/>

<c:choose>
  <c:when test="${forward.name == 'UNFULFILLED'}">
    <script>alert('ASSIGNMENT UNFULFILLED'); 
    document.location = "<c:url value="myEngineeringAssignment.jsp"/>";</script>
  </c:when>
</c:choose>
<!-- fmt:message key="fms.facility.msg.requestUpdated"/-->
<%@include file="/ekms/includes/header.jsp" %>

<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.label.engineering.dutyRoster'/> > <fmt:message key="fms.label.engineering.myAssignment"/>
        </td>
    	<td align="right" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="engineeringAssignment.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
