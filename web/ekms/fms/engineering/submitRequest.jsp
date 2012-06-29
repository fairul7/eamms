<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_submitRequest">
    <com.tms.fms.engineering.ui.SubmitRequestForm name="submit" width="100%"/>
  </page>
</x:config>

<c-rt:if test="${forward.name == 'error'}">
	<script type="text/javascript">
		alert("<fmt:message key='fms.facility.copyRequest.alert'/>");
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'continue'}">
	<c:set var="requestId" value="${widgets['fms_submitRequest.submit'].requestId}"/>
	<script type="text/javascript">
		alert("<fmt:message key='fms.facility.copyRequest.reminder'/>");
		document.location = "<c:url value='requestAdd.jsp?requestId=${requestId}'/>";
	</script>
</c-rt:if>
<c-rt:if test="${forward.name == 'continueNewRequest'}">
	<script type="text/javascript">
		document.location = "<c:url value='requestAdd.jsp?requestId='/>";
	</script>
</c-rt:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.submitRequest"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_submitRequest.submit"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>