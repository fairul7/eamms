<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="quote_jsp_companyView">
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="View" width="100%" />
    </page>
</x:config>


<x:set name="quote_jsp_companyView.form1" property="companyID" value="${companyID}" />


<x:display name="quote_jsp_companyView.form1"></x:display>

<c:if test="${not empty(editURL)}">
    <input type="button" class="button" value="<fmt:message key='sfa.message.edit'/>" onClick="location='<c:out value="${editURL}"/>?companyID=<c:out value="${companyID}"/>'"/>
</c:if>
<c:if test="${not empty(backURL)}">
	<c:set var="buttonBack" value="${backURL}" scope="request"/>
    <input type="button" class="button" value="<fmt:message key='sfa.message.back'/>" onClick="location='<c:out value="${backURL}"/>'"/>
</c:if>
