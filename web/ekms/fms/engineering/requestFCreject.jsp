<%@include file="/common/header.jsp" %>
<x:config>
     <page name="RequestFCreject">
		<com.tms.fms.engineering.ui.RequestFCRejectForm name="form" width="100%"/>
     </page>
</x:config>

<c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
<x:set name="RequestFCreject.form" property="requestId" value="${requestId}"/>

<c:choose>
  <c:when test="${forward.name == 'UPDATE'}">
	<script>
		alert('<fmt:message key="fms.facility.msg.requestUpdated"/>'); 
	    window.opener.location="requestDetails.jsp";
    	window.close();
    </script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/linkCSS.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.request.label.requestReject'/>  
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
			<x:display name="RequestFCreject.form" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />