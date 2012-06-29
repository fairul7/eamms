<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>

<x:config>
    <page name="category">
     	<com.tms.crm.sales.ui.CategoryTable name="table1" template="table" width="100%" />
     	<com.tms.crm.sales.ui.CategoryForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<x:set name="category.table1" property="linkUrl" value="editCategory.jsp" />
<c:if test="${forward.name == 'categoryDuplicate'}"><script>
	
		alert ("<fmt:message key='sfa.message.updateRecordErrorDuplicated'/>.");
		location = "setup_category.jsp";  
	//
	</script>
</c:if>
<c:if test="${forward.name == 'delete'}" >
    <script>
        alert("<fmt:message key='sfa.label.categoryDelete'/>");
    </script>
</c:if>

<c:if test="${forward.name == 'notdelete'}" >
    <script>
        alert("<fmt:message key='sfa.label.categoryNoDelete'/>");
    </script>

</c:if>

<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.label.category'/> </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="category.table1"></x:display>

  </td>
     </tr>
     </table>

	<br>
 <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.label.newCategory'/>
      </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="category.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
