<%@include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*" %>

<%@ include file="includes/checkHelpdeskPermission.jsp"%>

<x:config>
    <page name="jsp_contactEdit">
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>


<c:if test="${forward.name =='cancel'}" >
    <c:redirect url="/ekms/helpdesk/contactDetails.jsp?contactID=${widgets['jsp_contactEdit.form1'].contactID}" />

</c:if>

<c:choose>
	<c:when test="${not empty(param.contactID)}">
		<c:set var="contactID" value="${param.contactID}" />
	</c:when>
	<c:otherwise>
		<c:set var="contactID" value="${widgets['jsp_contactEdit.form1'].contactID}" />
	</c:otherwise>
</c:choose>

<x:set name="jsp_contactEdit.form1" property="contactID" value="${contactID}" />
<c:choose>
	<c:when test="${forward.name == 'contactUpdated'}">
		<script>
		<!--
			location = "contactDetails.jsp?contactID=<c:out value="${contactID}"/>";
		//-->
		</script>
	</c:when>
</c:choose>


    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key="helpdesk.label.customer"/> > <fmt:message key='sfa.message.editContact'/>
                </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_contactEdit.form1"></x:display>
	
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
