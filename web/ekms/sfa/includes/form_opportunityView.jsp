<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_opportunityView">
     	<com.tms.crm.sales.ui.OpportunityForm name="form1" type="View" width="100%" />
    </page>
</x:config>


<x:set name="jsp_opportunityView.form1" property="opportunityID" value="${opportunityID}" />


<x:display name="jsp_opportunityView.form1"></x:display>

<c:if test="${not empty(editURL)}">
    <input type="button" value="<fmt:message key='sfa.message.edit'/>" class="button"  onClick="location = '<c:out value="${editURL}" />?opportunityID=<c:out value="${opportunityID}" />'"/>
    <input type="button" value="<fmt:message key='sfa.message.back'/>" class="button" onClick="location= '<c:out value="${backURL}"/> '"   />

<%--
	<a href="<c:out value="${editURL}" />?opportunityID=<c:out value="${opportunityID}" />">Edit</a><br><br>
--%>
</c:if>
<c:if test="${not empty(backURL)}">
	<c:set var="buttonBack" value="${backURL}?opportunityID=${opportunityID}" scope="request"/>
	<jsp:include page="navButtons.jsp"/>
</c:if>
