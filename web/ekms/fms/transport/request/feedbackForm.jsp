 <%@include file="/common/header.jsp" %>

<x:config>
     <page name="request">
        <com.tms.fms.transport.ui.RequestDetailFeedbackForm name="requestform"/>
		<com.tms.fms.transport.ui.FeedbackForm name="feedback" width="80%"/>
     </page>
</x:config>

<c:if test="${! empty param.requestId}">
    <c:set var="requestId" value="${param.requestId}"></c:set>
</c:if>

<c:if test="${! empty param.requestId}">
    <c:set var="requestId" value="${param.requestId}"></c:set>
	<c:set var="view" value="view"></c:set>
	<x:set name="request.feedback" property="requestId" value="${param.requestId}"/>
</c:if>
<c:choose>
	<c:when test="${forward.name == 'SUBMITTED'}">
		<script>
    		alert('<fmt:message key="fms.facility.msg.feedbackSubmitted"/>'); 
   	 		document.location = "<c:url value="requestListing.jsp"/>";
		</script>
  	</c:when>
	<c:when test="${forward.name == 'EXIST'}">
		<script>
  	  		alert('<fmt:message key="fms.tran.alert.exist"/>'); 
		</script>
	</c:when>
</c:choose>


  
 <%@include file="/ekms/includes/header.jsp" %> 
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">

  <tr valign="MIDDLE">
    <td height="22" class="contentTitleFont">
      &nbsp;<fmt:message key='fms.tran.requestDetail'/>  
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
   <tr valign="MIDDLE">
    <td height="22" class="contentTitleFont">
      &nbsp;<fmt:message key='fms.tran.feedbackForm'/>  
    </td>
    <td align="right" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
	<td colspan="2" valign="TOP" class="contentBgColor">
		<br>
		<x:display name="request.feedback"> </x:display>
	</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" class="contentBgColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />



<%@include file="/ekms/includes/footer.jsp" %>