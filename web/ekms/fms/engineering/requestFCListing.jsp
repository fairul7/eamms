<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<x:config>
  <page name="fms_requestListingPage">
    <com.tms.fms.engineering.ui.RequestFCTable name="listing" width="100%"/>
  </page>
</x:config>

<c:if test="${forward.name == 'ADD'}">
  <script> window.location="requestAdd.jsp"</script>
</c:if>

<c:if test="${!empty param.requestId}">
	<script> window.location='requestEdit.jsp?requestId=<c:out value="${param.requestId}"/>'</script>
 </c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.requestListingFC"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_requestListingPage.listing"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>