<%@include file="/common/header.jsp" %>

<x:config>
     <page name="checkAvailabilityFCfacility">
		<com.tms.fms.engineering.ui.CheckAvailabilityFCFacilityForm name="form" mode="coordinator" width="100%"/>
     </page>
</x:config>


<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="id" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['checkAvailabilityFCfacility.form'].requestId}"/>
  </c:otherwise>
</c:choose>

<x:set name="checkAvailabilityFCfacility.form" property="requestId" value="${id}"/>

<script language="JavaScript">
	function goToManpower(){
		window.location='coordCheckAvailabilityFCmanpower.jsp?requestId=<c:out value="${id}" />'
	}
</script>

<%@include file="/ekms/includes/header.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.request.label.checkAvailability'/>  
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
			<x:display name="checkAvailabilityFCfacility.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
