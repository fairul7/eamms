<%@ page import="com.tms.crm.sales.ui.SalesGroupEditForm"%>

<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<x:config>
    <page name="jsp_group_edit">
     	<com.tms.crm.sales.ui.SalesGroupEditForm name="form1"  width="100%" />
    </page>
</x:config>


<c:if test="${! empty param.id}" >
    <x:set name="jsp_group_edit.form1" property="id" value="${param.id}" />
</c:if>

<c-rt:set var="updated" value="<%= SalesGroupEditForm.FORWARD_UPDATED %>" />
<c-rt:set var="cancel" value="<%= SalesGroupEditForm.FORWARD_CANCEL %>" />


<c:if test="${forward.name == updated}"><script>
	<!--
		alert ("<fmt:message key='sfa.message.salesgroupsupdated'/>.");
        location = 'setup_group.jsp';

	//-->
	</script>
</c:if>

<c:if test="${forward.name == cancel}" >
    <script>
        location = "setup_group.jsp";
    </script>
</c:if>



    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.message.editGroup'/> </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_group_edit.form1"></x:display>
 </td></tr>
    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
