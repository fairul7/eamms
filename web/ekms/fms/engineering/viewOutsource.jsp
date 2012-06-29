<%@include file="/common/header.jsp" %>

<x:config>
     <page name="viewOutsource">
		<com.tms.fms.engineering.ui.ViewOutsourceTable name="table" width="100%"/>
     </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
  </c:otherwise>
</c:choose>
<x:set name="viewOutsource.table" property="requestId" value="${requestId}"/>

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
      		&nbsp;<fmt:message key='fms.request.label.outsourceRequest'/>  
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
			<x:display name="viewOutsource.table" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />
