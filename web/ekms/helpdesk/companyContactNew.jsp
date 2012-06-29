<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="jsp_contactList_commain">
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
    </page>
</x:config>

<c:if test="${forward.name == 'contactAdded'}" >
    <script>
        alert("<fmt:message key='sfa.message.newcontactadded'/>!");
    </script>
</c:if>

<c:choose>
	<c:when test="${not empty(param.companyID)}">
		<c:set var="companyID" value="${param.companyID}" />
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_contactList_commain.table1'].companyID}" />
	</c:otherwise>
</c:choose>

<c:if test="${forward.name == 'cancel'}">
    <c:redirect url="/ekms/helpdesk/companyView.jsp" />
</c:if>

<x:set name="jsp_contactList_commain.form1" property="companyID" value="${companyID}" />

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='sfa.message.newContact'/>
    </td>
    </tr>

    <tr>
    <td class="sfaRow">

	<x:display name="jsp_contactList_commain.form1"></x:display>
	    </td></tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>
    </table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
