<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_reassignPartner">
     	<com.tms.crm.sales.ui.CompanyTable name="table1" type="Partner_List" subType="Reassign" width="100%" />
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>

<%-- // TODO: change link or remove edit screens --%>
<x:set name="jsp_reassignPartner.table1" property="linkUrl" value="company_view_2.jsp" />
<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_reassignPartner.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_reassignPartner.table1" property="opportunityID" value="${opportunityID}" />
<c:choose>
	<c:when test="${forward.name == 'selectCompany'}"><script>
			alert("<fmt:message key='sfa.message.partnerReassigned'/>");
			location='partnercontact_list_oppdetails.jsp?opportunityID=<c:out value="${opportunityID}"/>';
		</script>
	</c:when>
</c:choose>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.reassignPartner'/>
</td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_reassignPartner.table1"></x:display>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
