<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_companyList_commain">
     	<com.tms.crm.sales.ui.CompanyTable name="table1" type="Company_List" width="100%" />
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<x:set name="jsp_companyList_commain.table1" property="linkUrl" value="company_view_commain.jsp" />
<c:choose>
	<c:when test="${forward.name == 'companyAdded'}">
		<c:set var="justCreatedID" value="${widgets['jsp_companyList_commain.form1'].justCreatedID}" />
		<c:redirect url="newcontact_contact_list.jsp?companyID=${justCreatedID}"/>
	</c:when>
	<c:when test="${forward.name == 'companyDuplicate'}"><script>
		<!--
			alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
			location = "company_list_commain.jsp";
		//-->
		</script>
	</c:when>
	<c:when test="${forward.name == 'selectCompany'}">
		<c:redirect url="newcontact_contact_list.jsp?companyID=${widgets['jsp_companyList_commain.table1'].selectedCompanyID}"/>
	</c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.newContact'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<c:set var="navSelected" value="0" scope="request"/>
	<jsp:include page="includes/navAddCompany.jsp"/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_companyList_commain.table1"></x:display>
    </td>
    </tr>
    </table>

    <br>


<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.newCompany'/>
         </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_companyList_commain.form1"></x:display>
    </td></tr>
<tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>
    </table>
<jsp:include page="includes/footer.jsp"/>
