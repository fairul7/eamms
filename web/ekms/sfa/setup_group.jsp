<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<x:config>
    <page name="jsp_group">
     	<com.tms.crm.sales.ui.SalesGroupTable name="table" type="Add" width="100%" />
     	<com.tms.crm.sales.ui.SalesGroupAddForm name="form1"  width="100%" />
    </page>
</x:config>


<c:if test="${! empty param.id}" >
    <c:redirect url="/ekms/sfa/setup_group_edit.jsp?id=${param.id}" />
</c:if>
<c:if test="${forward.name == 'salesGroupsUpdated'}"><script>
	<!--
		alert ("<fmt:message key='sfa.message.salesgroupsupdated'/>.");
	//-->
	</script>
</c:if>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.group'/> </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_group.table"></x:display>
 </td></tr>
  </table>
  <br>

        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.newGroup'/>
                </td>
                </tr>

	                 </tr>
                    <tr>
                    <td class="sfaRow">
                	<x:display name="jsp_group.form1"></x:display>
                </td>
                </tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
