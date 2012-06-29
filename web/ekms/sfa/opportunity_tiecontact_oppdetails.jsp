<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_opportunityTiecontact_oppdetails">
        <com.tms.crm.sales.ui.OpportunityContactTieForm name="form1" type="Company_Tie" width="100%" />
    </page>
</x:config>

<x:set name="jsp_opportunityTiecontact_oppdetails.form1" property="contactList" value="${widgets['jsp_contactList_oppdetails.table1'].contactList}"/>


<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_opportunityTiecontact_oppdetails.form1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_opportunityTiecontact_oppdetails.form1" property="opportunityID" value="${opportunityID}" />
<c:choose>
	<c:when test="${forward.name == 'updatedOpportunityContactType'}">
		<c:redirect url="opportunity_details.jsp?opportunityID=${opportunityID}"/>
	</c:when>
		<c:when test="${forward.name == 'closed'}">
		<c:redirect url="/ekms/sfa/closedsale_details.jsp?opportunityID=${opportunityID}"/>
	</c:when>
</c:choose>

<c:if test="${!empty param.status}">
    <x:set name="jsp_opportunityTiecontact_oppdetails.form1" property="state" value="${param.status}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.editContactDetails'/>

        </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_opportunityTiecontact_oppdetails.form1"/>
	
<%--
	<c:set var="buttonBack" value="contact_list_oppdetails.jsp?opportunityID=${opportunityID}" scope="request"/>
	<jsp:include page="includes/navButtons.jsp"/>
--%>
</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
