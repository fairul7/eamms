<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_companyList">
     	<com.tms.crm.sales.ui.CompanyTable name="table1" type="Company_List" width="100%" />
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>




<x:set name="jsp_companyList.table1" property="linkUrl" value="company_view_2.jsp" />
<c:choose>
	<c:when test="${forward.name == 'companyAdded'}">
		<c:set var="justCreatedID" value="${widgets['jsp_companyList.form1'].justCreatedID}" />
        <c:redirect url="newopportunity_contact_list.jsp?companyId=${justCreatedID}"/>

<%--		<c:redirect url="opportunity_list.jsp?companyID=${justCreatedID}&first=1"/>--%>

	</c:when>
	<c:when test="${forward.name == 'companyDuplicate'}">
		<script>
		<!--
			alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
			location = "company_list.jsp";
		//-->
		</script>
	</c:when>
	<c:when test="${forward.name == 'selectCompany'}">
        <c:redirect url="newopportunity_contact_list.jsp?companyId=${widgets['jsp_companyList.table1'].selectedCompanyID}&clear=1"/>
<%--
		<c:redirect url="opportunity_list.jsp?companyID=${widgets['jsp_companyList.table1'].selectedCompanyID}&first=1"/>
--%>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
	<c:set var="navSelected" value="0" scope="request"/><fmt:message key='sfa.message.opportunities'/> > <fmt:message key='sfa.message.addNewOpportunity'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
    <jsp:include page="includes/navAddOpportunity.jsp"/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_companyList.table1"/>
     </td>
        </tr>
        </table>
        <br>

   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">

    <tr>
    <td class="sfaHeader">

<fmt:message key='sfa.message.newCompany'/>
        </td>
        </tr>
        <tr>
        <td class="sfaRow">

	<x:display name="jsp_companyList.form1"></x:display>
    </td>
    </tr>
    <tr>
    <td class="sfaFooter">
    &nbsp;
<%--
	<c:set var="buttonBack" value="main.jsp" scope="request"/>
--%>
    </td>
    </tr>
    </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
