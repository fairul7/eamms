<%@ include file="/common/header.jsp" %>

<c:set var="companyID" value="${param.companyID}" scope="request"/>
<c:set var="backURL" value="newopportunity_partner_list.jsp" scope="request"/>
<c:set var="editURL" value="newopportunity_partner_edit.jsp" scope="request"/> 


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.viewPartner'/></td></tr>
            <Tr>
            <td class="sfaRow">
	<jsp:include page="includes/form_companyView.jsp"/>
    </td></tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>
    </table>
<jsp:include page="includes/footer.jsp"/>
