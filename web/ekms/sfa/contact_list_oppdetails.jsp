<%@ include file="/common/header.jsp"%>
<%@page
	import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*"%>

<x:config>
	<page name="jsp_contactList_oppdetails">
	<com.tms.crm.sales.ui.ContactTable name="table1" type="Company_Contacts" width="100%" />
	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
	</page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID"
			value="${widgets['jsp_contactList_oppdetails.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_contactList_oppdetails.table1" property="opportunityID"
	value="${opportunityID}" />
<x:set name="jsp_contactList_oppdetails.table1" property="linkUrl"
	value="contact_view_oppdetails.jsp?opportunityID=${opportunityID}&lulu=lala" />
<x:set name="jsp_contactList_oppdetails.form1" property="opportunityID"
	value="${opportunityID}" />
	
<c:if test="${!empty param.status}">
    <x:set name="jsp_contactList_oppdetails.table1" property="status" value="${param.status}"/>
    <x:set name="jsp_contactList_oppdetails.form1" property="status" value="${param.status}"/>
</c:if>

<c:choose>
	<c:when test="${forward.name == 'contactsSelected'}">
		<c:redirect	url="opportunity_tiecontact_oppdetails.jsp?opportunityID=${opportunityID}" />
	</c:when>
		<c:when test="${forward.name == 'contactsSelectedClosed'}">
		<c:redirect	url="opportunity_tiecontact_oppdetails.jsp?status=closed&opportunityID=${opportunityID}" />
	</c:when>
	<c:when test="${forward.name == 'cancel'}">
		<c:redirect	url="opportunity_details.jsp?opportunityID=${opportunityID}" />
	</c:when>
	<c:when test="${forward.name == 'cancelClosed'}">
		<c:redirect	url="closedsale_details.jsp?opportunityID=${opportunityID}" />
	</c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground"
	width="100%">
	<tr valign="top">
		<td align="left" valign="top" class="sfaHeader"><fmt:message
			key='sfa.message.editContactDetails' /></td>
	</tr>
	<tr>
		<td class="sfaRow"><x:display name="jsp_contactList_oppdetails.table1"></x:display>
		</td>
	</tr>

</table>

<br>
<table cellpadding="4" cellspacing="1" class="sfaBackground"
	width="100%">
	<tr valign="top">
		<td align="left" valign="top" class="sfaHeader">
        <fmt:message key='sfa.message.newContact'/>
              </td>
    </tr>    <tr>
    <td class="sfaRow">
	<x:display name="jsp_contactList_oppdetails.form1"></x:display>


<%--
	<c:set var="buttonBack" value="opportunity_details.jsp?opportunityID=${opportunityID}" scope="request"/>
	<jsp:include page="includes/navButtons.jsp"/>
--%></td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
