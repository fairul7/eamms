<%@include file="/common/header.jsp" %>
<x:permission var="isAuthorized" module="com.tms.crm.sales.model.AccountManagerModule" permission="com.tms.crm.sales.SalesAdmin"/>

<c:if test="${!isAuthorized}">
    <c:redirect url="noPermission.jsp"/>
</c:if>
<x:config>
    <page name="jsp_source">
     	<com.tms.crm.sales.ui.SourceTable name="table1" template="table" width="100%" />
     	<com.tms.crm.sales.ui.SourceForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<x:set name="jsp_source.table1" property="linkUrl" value="source2.jsp" />
<c:if test="${forward.name == 'sourceDuplicate'}"><script>
	<!--
		alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
		location = "setup_source.jsp";
	//-->
	</script>
</c:if>
<c:if test="${forward.name == 'delete'}" >
    <script>
        alert("<fmt:message key='sfa.message.sourceDelete'/>.");
    </script>
</c:if>

<c:if test="${forward.name == 'notdelete'}" >
    <script>
        alert("<fmt:message key='sfa.message.sourceNoDelete'/>.");
    </script>

</c:if>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.source'/> </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_source.table1"></x:display>
	</td>
     </tr>
     </table>

	<br>
 <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader"><fmt:message key='sfa.message.newSource'/></td>
    </tr>
    <tr>
    <td class="sfaRow"><x:display name="jsp_source.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
