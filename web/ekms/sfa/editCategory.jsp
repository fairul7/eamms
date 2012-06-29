<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<x:config>
    <page name="editCategory">
     	<com.tms.crm.sales.ui.CategoryForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>

  
<c:choose>   
	<c:when test="${not empty(param.categoryID)}">
		<c:set var="categoryID" value="${param.categoryID}" />
	</c:when>
	<c:otherwise>
		<c:set var="categoryID" value="${widgets['editCategory.form1'].categoryID}" />
	</c:otherwise>
</c:choose>
<x:set name="editCategory.form1" property="categoryID" value="${categoryID}" />
<c:if test="${forward.name == 'categoryUpdated'}">
	<c:redirect url="setup_category.jsp"/>
</c:if>
<c:if test="${forward.name == 'cancel'}">
	<c:redirect url = "setup_category.jsp"/>
</c:if>
<c:if test="${forward.name == 'categoryDuplicate'}">
	<script>
	<!--
		alert ("<fmt:message key='sfa.message.updateErrorDuplicated'/>.");
		location = "setup_category.jsp";
	//-->
	</script>
</c:if>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.label.editCategory'/></td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="editCategory.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
