<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_companyView">
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="View" width="100%" />
    </page>
</x:config>


<x:set name="jsp_companyView.form1" property="companyID" value="${companyID}" />


<x:display name="jsp_companyView.form1"></x:display>

<c:if test="${not empty(editURL)}">
    <input type="button" class="button" value="<fmt:message key='sfa.message.edit'/>" onClick="location='<c:out value="${editURL}"/>?companyID=<c:out value="${companyID}"/>'"/>

<%--
	<a href="<c:out value="${editURL}"/>?companyID=<c:out value="${companyID}"/>">Edit</a><br><br>
--%>
</c:if>
<c:if test="${not empty(backURL)}">
	<c:set var="buttonBack" value="${backURL}" scope="request"/>
    <input type="button" class="button" value="<fmt:message key='sfa.message.back'/>" onClick="location='<c:out value="${backURL}"/>'"/>
</c:if>
