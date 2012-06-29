<%@ page import="com.tms.crm.sales.ui.ProductForm" %>
<%@include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_product2">
     	<com.tms.crm.sales.ui.ProductForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>

<c-rt:set var="forward_cancel" value="<%= ProductForm.FORWARD_CANCEL %>" />

<c:choose>
	<c:when test="${not empty(param.productID)}">
		<c:set var="productID" value="${param.productID}" />
	</c:when>
	<c:otherwise>
		<c:set var="productID" value="${widgets['jsp_product2.form1'].productID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_product2.form1" property="productID" value="${productID}" />
<c:if test="${forward.name == 'productUpdated'}">
	<c:redirect url="setup_product.jsp"/>
</c:if>
<c:if test="${forward.name == forward_cancel}">
	<c:redirect url="setup_product.jsp"/>
</c:if>
<c:if test="${forward.name == 'productDuplicate'}">
	<script>
	<!--
		alert ("<fmt:message key='sfa.message.updateErrorDuplicated'/>.");
		location = "setup_product.jsp";
	//-->
	</script>
</c:if>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.message.editProduct'/></td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_product2.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
