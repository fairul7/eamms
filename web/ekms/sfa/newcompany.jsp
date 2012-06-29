<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_new_company">
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:if test="${forward.name =='companyAdded' }">
    <script>
        alert("<fmt:message key='sfa.message.newcompanyadded'/>!");
        location = "<c:url value="/ekms/sfa/company_view_commain.jsp"/>?companyID=<c:out value="${widgets['jsp_new_company.form1'].justCreatedID}" />";
    </script>    
</c:if>

<c:if test="${forward.name == 'cancel'}">
	<script>
		location = "companies_listing.jsp";
	</script>
</c:if>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.menuCompanies'/> > <fmt:message key='sfa.message.addNewCompany'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_new_company.form1"></x:display>
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
