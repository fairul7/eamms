<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_contactView">
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="View" width="100%" />
    </page>
</x:config>


<x:set name="jsp_contactView.form1" property="contactID" value="${contactID}" />


<x:display name="jsp_contactView.form1"></x:display>
<input type="button" value="<fmt:message key='sfa.message.edit'/>" class="button"  onClick="location = '<c:out value="${editURL}"/>?contactID=<c:out value="${contactID}"/>'"/>
<%--
<a href="<c:out value="${editURL}"/>?opportunityID=<c:out value="${opportunityID}" />&contactID=<c:out value="${contactID}" />">Edit</a>
--%>
<c:if test="${not empty backURL}" >
        <input type="button" value="<fmt:message key='sfa.message.back'/>" class="button" onClick="location='<c:out value="${backURL}"/>';"/>

</c:if>

