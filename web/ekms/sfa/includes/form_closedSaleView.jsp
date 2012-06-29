<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_closedSaleView">
     	<com.tms.crm.sales.ui.ClosedSaleView name="view1" type="View"  />
    </page>
</x:config>


<x:set name="jsp_closedSaleView.view1" property="opportunityID" value="${opportunityID}" />


<x:display name="jsp_closedSaleView.view1"></x:display>

<c:if test="${not empty(editURL)}">
    <input type="button" value="<fmt:message key='sfa.message.edit'/>" onClick="location = '<c:out value="${editURL}" />?opportunityID=<c:out value="${opportunityID}" />'"/>

<%--
	<a href="<c:out value="${editURL}" />?opportunityID=<c:out value="${opportunityID}" />">Edit</a><br><br>
--%>
</c:if>
