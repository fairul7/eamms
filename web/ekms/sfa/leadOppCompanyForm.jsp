<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_leadcompanyform">
     	<com.tms.crm.sales.ui.LeadCompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:if test="${!empty param.leadId}" >
    <c:set var="leadId" value="${param.leadId}" scope="session"/>
    <x:set name="jsp_leadcompanyform.form1" property="id" value="${param.leadId}" />
</c:if>




<%--
<x:set name="jsp_leadcompanyform.table1" property="linkUrl" value="company_view_2.jsp" />
--%>
<c:choose>
	<c:when test="${forward.name == 'companyAdded'}">
		<c:set var="justCreatedID" value="${widgets['jsp_leadcompanyform.form1'].justCreatedID}" />
        <c:redirect url="leadopportunity_contact_list.jsp?companyId=${justCreatedID}&leadId=${widgets['jsp_leadcompanyform.form1'].id}"/>

<%--		<c:redirect url="opportunity_list.jsp?companyID=${justCreatedID}&first=1"/>--%>

	</c:when>
	<c:when test="${forward.name == 'companyDuplicate'}">
		<script>
		<!--
			alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
<%--
			location = "company_list.jsp";
--%>
		//-->
		</script>
	</c:when>
	<c:when test="${forward.name == 'selectCompany'}">
        <c:redirect url="newopportunity_contact_list.jsp?companyId=${widgets['jsp_leadcompanyform.table1'].selectedCompanyID}&clear=1"/>
<%--
		<c:redirect url="opportunity_list.jsp?companyID=${widgets['jsp_leadcompanyform.table1'].selectedCompanyID}&first=1"/>
--%>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
	<c:set var="navSelected" value="0" scope="request"/>
    <fmt:message key='sfa.message.addinganOpportunity'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
    <jsp:include page="includes/navAddOpportunity.jsp"/>
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

	<x:display name="jsp_leadcompanyform.form1"></x:display>
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
