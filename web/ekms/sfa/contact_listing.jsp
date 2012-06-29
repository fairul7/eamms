<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<x:config>
    <page name="jsp_contactList_oppdetails">
     	<com.tms.crm.sales.ui.ContactCompleteTable name="table1" template="table" width="100%" />
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c-rt:set var="move" value="<%=ContactCompleteTable.FORWARD_MOVE_CONTACT %>"/>

<c:if test="${forward.name == move}" >
    <c:redirect url="/ekms/sfa/contacts_move_check.jsp" />
</c:if>


<c:if test="${! empty param.contactID}" >
    <c:redirect url="/ekms/sfa/contact_details.jsp?contactID=${param.contactID}" />

</c:if>

<%--
<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_contactList_oppdetails.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_contactList_oppdetails.table1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_contactList_oppdetails.table1" property="linkUrl" value="contact_view_oppdetails.jsp?opportunityID=${opportunityID}&lulu=lala" />
<x:set name="jsp_contactList_oppdetails.form1" property="opportunityID" value="${opportunityID}" />

<c:choose>
	<c:when test="${widgets['jsp_contactList_oppdetails.table1'].justTiedContact}">
		<c:redirect url="opportunity_tiecontact_oppdetails.jsp?opportunityID=${opportunityID}" />
	</c:when>
</c:choose>
--%>


<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
               <fmt:message key='sfa.message.menuCompanies'/> >  <fmt:message key='sfa.message.contactListing'/>
                </td>
            </tr>
	 <tr>
    <td class="sfaRow">
	<x:display name="jsp_contactList_oppdetails.table1"></x:display>
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
