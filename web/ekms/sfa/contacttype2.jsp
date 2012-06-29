<%@include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_contacttype2">
     	<com.tms.crm.sales.ui.ContactTypeForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.contactTypeID)}">
		<c:set var="contactTypeID" value="${param.contactTypeID}" />
	</c:when>
	<c:otherwise>
		<c:set var="contactTypeID" value="${widgets['jsp_contacttype2.form1'].contactTypeID}" />
	</c:otherwise>
</c:choose>

<x:set name="jsp_contacttype2.form1" property="contactTypeID" value="${contactTypeID}" />

<c:if test="${forward.name == 'contactTypeUpdated'}">
	<c:redirect url="setup_contacttype.jsp"/>
</c:if>

<c:if test="${forward.name == 'contactTypeDuplicate'}"><script>
	<!--
		alert ("<fmt:message key='sfa.message.duplicatedRecordError'/>.");
		location = "setup_contacttype.jsp";
	//-->
	</script>
</c:if>

<c:if test="${forward.name == 'cancel'}">
	<c:redirect url = "setup_contacttype.jsp"/>
</c:if>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.setup'/> > <fmt:message key='sfa.message.editContactType'/>
                    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_contacttype2.form1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
