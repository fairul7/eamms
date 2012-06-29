<%@ include file="/common/header.jsp" %>

<%--
<%@ include file="includes/checkHelpdeskPermission.jsp"%>
--%>

<x:config>
    <page name="quotationCompany">
        <com.tms.crm.sales.ui.CompanyTable name="table1" type="Company_List" width="100%" />
        <com.tms.crm.sales.ui.CompanyForm name="form1" type="Add" width="100%" />
    </page>
</x:config>

<x:set name="quotationCompany.table1" property="linkUrl" value="incidentCompanyView.jsp" />
<c:choose>
    <c:when test="${forward.name == 'companyAdded'}">
        <c:set var="justCreatedID" value="${widgets['quotationCompany.form1'].justCreatedID}" />
        <c:redirect url="incidentContact.jsp?companyId=${justCreatedID}"/>
    </c:when>
    <c:when test="${forward.name == 'companyDuplicate'}">
        <script>
            <!--
            alert ("<fmt:message key='sfa.message.addRecordErrorDuplicated'/>.");
            location = "incidentCompany.jsp";
            //-->
        </script>
    </c:when>
    <c:when test="${forward.name == 'selectCompany'}">
        <c:redirect url="incidentContact.jsp?companyId=${widgets['quotationCompany.table1'].selectedCompanyID}&clear=1"/>
    </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <c:set var="navSelected" value="0" scope="request"/>
            <fmt:message key="com.tms.quotation.quotation.add"/>
        </td>
    </tr>
    <tr>
        <td class="sfaRow">
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <b>&nbsp;<fmt:message key="helpdesk.label.customer"/></b>
                        <font color="#990000">&gt; <fmt:message key="helpdesk.label.selectCompany"/></font>
                        
                    </td>
                    <td>
                        
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="sfaRow">

            <x:display name="quotationCompany.table1"/>
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

            <x:display name="quotationCompany.form1"></x:display>
        </td>
    </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
