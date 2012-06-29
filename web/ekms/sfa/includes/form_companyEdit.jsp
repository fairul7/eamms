<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>


<x:config>
    <page name="jsp_companyEdit">
     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${empty(companyID)}">
		<c:set var="companyID" value="${widgets['jsp_companyEdit.form1'].companyID}" />
	</c:when>
</c:choose>
<x:set name="jsp_companyEdit.form1" property="companyID" value="${companyID}" />
<c:choose>
	<c:when test="${forward.name == 'companyUpdated'}">
		<script>
		<!--
			location = "<c:out value="${onUpdateURL}?companyID=${companyID}"/>";
		//-->
		</script>
	</c:when>
	<c:when test="${forward.name == 'companyDuplicate'}"><script>
		<!--
			alert ("<fmt:message key='sfa.message.updateRecordErrorDuplicated'/>.");
			location = "<c:out value="${onUpdateURL}?companyID=${companyID}"/>";
		//-->
		</script>
	</c:when>
</c:choose>


<x:display name="jsp_companyEdit.form1"></x:display>

<c:set var="buttonBack" value="${backURL}?companyID=${companyID}" scope="request"/>
