<%@ page import="com.tms.crm.helpdesk.ui.ProductForm"%>
<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskAdminPermission.jsp"%>

<x:config>
	<page name="helpdeskProductOpen">
		<com.tms.crm.helpdesk.ui.ProductOpen name="form"/>
	</page>
</x:config>
<c-rt:set var="forward_success" value="<%= ProductForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_cancel" value="<%= ProductForm.FORWARD_CANCEL %>"/>
<c:if test="${!empty param.productId}">
	<x:set name="helpdeskProductOpen.form" property="productId" value="${param.productId}"/>
</c:if>
<c:if test="${forward_success == forward.name}">
	<script>
		alert("<fmt:message key="helpdesk.message.productUpdated"/>");
		document.location="<c:url value="/ekms/helpdesk/product.jsp"/>";
	</script>
</c:if>
<c:if test="${forward_cancel == forward.name}">
	<script>document.location="<c:url value="/ekms/helpdesk/product.jsp"/>";</script>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="003366" class="contentTitleFont"><b><font color="FFCF63" class="contentTitleFont">&nbsp;&nbsp;&nbsp;
            <fmt:message key="sfa.message.setup"/> > <fmt:message key="helpdesk.message.updatingProduct"/></font></b></td>
        <td align="right" bgcolor="003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="top" bgcolor="EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="top"><x:display name="helpdeskProductOpen.form"/></td></tr>
    <tr><td colspan="2" valign="top" bgcolor="CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>