<%@ include file="/common/header.jsp" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="jsp_companyList_commain">
     	<com.tms.crm.sales.ui.CompanySimpleTable name="table1" type="Company_List" width="100%" />
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:if test="${forward.name == 'closedsales'}" >
    <script>
        alert("<fmt:message key='sfa.message.deletecompanyerrormessage'/>.");
    </script>
</c:if>


<c:if test="${forward.name == 'opportunities'}" >
    <script>
        alert("<fmt:message key='sfa.message.deletecompanywifopp'/>.");
    </script>
</c:if>

<c:if test="${forward.name == 'deleted'}" >
    <script>
        alert("<fmt:message key='sfa.message.companydeleted'/>.");
    </script>

</c:if>

<x:set name="jsp_companyList_commain.table1" property="linkUrl" value="companyView.jsp" />
<c:choose>
	<c:when test="${forward.name == 'companyAdded'}">
		<c:set var="justCreatedID" value="${widgets['jsp_companyList_commain.form1'].justCreatedID}" />
		<c:redirect url="contact_list_commain.jsp?companyID=${justCreatedID}"/>
	</c:when>
	<c:when test="${forward.name == 'companyDuplicate'}">
		<script>
		<!--
			alert ("Could not add record. Duplicate record found.");
			location = "company_list_commain.jsp";
		//-->
		</script>
	</c:when>
	<c:when test="${forward.name == 'selectCompany'}">
		<c:redirect url="contact_list_commain.jsp?companyID=${widgets['jsp_companyList_commain.table1'].selectedCompanyID}"/>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="contentBgColor" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
           <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='sfa.message.companiesListing'/>
    	</td>
    </tr>
    <tr>
    	<td class="classRow">
		<x:display name="jsp_companyList_commain.table1"></x:display>
    	</td>
    </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>


