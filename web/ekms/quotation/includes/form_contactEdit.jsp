<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*" %>

<x:config>
    <page name="jsp_contactEdit">
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${empty(contactID)}">
		<c:set var="opportunityID" value="${widgets['jsp_contactEdit.form1'].opportunityID}" />
		<c:set var="contactID" value="${widgets['jsp_contactEdit.form1'].contactID}" />
	</c:when>
</c:choose>
<x:set name="jsp_contactEdit.form1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_contactEdit.form1" property="contactID" value="${contactID}" />
<c:choose>
	<c:when test="${forward.name == 'contactUpdated'}">
		<script>
		<!--
			location = "<c:out value="${onUpdateURL}?opportunityID=${opportunityID}&contactID=${contactID}"/>";
		//-->
		</script>
	</c:when>
</c:choose>


<x:display name="jsp_contactEdit.form1"></x:display>

<c:set var="buttonBack" value="${backURL}?opportunityID=${opportunityID}&contactID=${contactID}" scope="request"/>
<jsp:include page="navButtons.jsp"/>