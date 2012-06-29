<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<x:config>
  <page name="fms_HOUTodaysAssignmentPage">
    <com.tms.fms.engineering.ui.HOUTodayAssignmentTable name="listing" width="100%"/>
  </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
</c:choose>

<c:if test="${!empty param.requestId}">
	<script> 
		//alert('<c:out value="${param.requestId}"/>');
		window.open('<c:url value="manpowerAssignmentDetails.jsp?requestId=${requestId}"/>','houAssignmentPopup','scrollbars=yes,resizable=yes,width=700,height=500');
	</script>
 </c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.unitHead.todayAssignment"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_HOUTodaysAssignmentPage.listing"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>