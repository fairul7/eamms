<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.misc.*, kacang.services.security.*" %>

<%
	User user = (User) session.getAttribute("currentUser");
	String userID = user.getId();
	String opportunityID = request.getParameter("opportunityID");
	boolean isSalesManager        = AccessUtil.isSalesManager(userID);
	boolean isExternalSalesPerson = AccessUtil.isExternalSalesPerson(userID);
	boolean isOwner               = AccessUtil.isOpportunityOwner(userID, opportunityID);
	
	boolean canView = false;
	boolean canEdit = false;


	if (isSalesManager||isOwner) {
		canView = true;
	}
	
	if (isSalesManager || isOwner) {
		canEdit = true;
	}
%>

<% if (canView) { %>
<c:set var="opportunityID" value="${param.opportunityID}" scope="request"/>
<% if (canEdit) { %>
	<c:set var="editURL" value="opportunity_edit_2.jsp" scope="request"/>
<% } %>
<c:set var="backURL" value="newopportunity_opportunity_list.jsp" scope="request"/>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.viewOpportunity'/>
                    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<jsp:include page="includes/form_opportunityView.jsp"/>
    </td>
    </tr>

     <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
    <% } else { %>
	<script>
		alert('<fmt:message key='sfa.message.noaccesstoview'/>.');
		location = 'newopportunity_opportunity_list.jsp';
	</script>
<% } %>
