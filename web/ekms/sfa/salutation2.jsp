<%@include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_salutation2">
     	<com.tms.crm.sales.ui.SalutationForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.salutationID)}">
		<c:set var="salutationID" value="${param.salutationID}" />
	</c:when>
	<c:otherwise>
		<c:set var="salutationID" value="${widgets['jsp_salutation2.form1'].salutationID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_salutation2.form1" property="salutationID" value="${salutationID}" />
<c:if test="${forward.name == 'salutationUpdated'}">
	<c:redirect url="setup_salutation.jsp"/>
</c:if>
<c:if test="${forward.name == 'salutationDuplicate'}"><script>
	<!--
		alert ("<fmt:message key='sfa.message.updateErrorDuplicated'/>.");
		location = "setup_salutation.jsp";
	//-->
	</script>
</c:if>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.message.editSalutation'/>
          </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_salutation2.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
