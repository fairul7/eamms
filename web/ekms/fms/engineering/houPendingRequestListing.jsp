<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<x:config>
  <page name="fms_HOUPendingRequestListingPage">
    <com.tms.fms.engineering.ui.HOUPendingRequestTable name="listing" width="100%"/>
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
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.label.HOUIncomingRequest"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
    	<td bgcolor="#EFEFEF" class="contentBgColor">
    		<table>
    			<tr>
    				<td><fmt:message key='fms.facility.note'/>:</td>
    				<td align="left"><img align="center" src="/ekms/images/icn_check.png"> <fmt:message key='fms.facility.manpowerAssigned'/></td>
    			</tr>
    			<tr>
    				<td>&nbsp;</td>
    				<td align="left"><img align="center" src="/ekms/images/icn_delete.png"> <fmt:message key='fms.facility.manpowerNotAssigned'/></td>
    			</tr>
    		</table>
		</td>
		<td bgcolor="#EFEFEF" class="contentBgColor">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_HOUPendingRequestListingPage.listing"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>