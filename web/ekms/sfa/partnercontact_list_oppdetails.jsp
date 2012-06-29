<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, com.tms.crm.sales.misc.*" %>

<x:config>
    <page name="jsp_partnercontactList_oppdetails">
     	<com.tms.crm.sales.ui.ContactTable name="table1" type="Partner_Contacts" width="100%" />
     	<com.tms.crm.sales.ui.ContactForm name="form1" type="Add" subType="Partner" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_partnercontactList_oppdetails.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_partnercontactList_oppdetails.table1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_partnercontactList_oppdetails.table1" property="linkUrl" value="partnercontact_view_oppdetails.jsp?opportunityID=${opportunityID}&lulu=lala" />
<x:set name="jsp_partnercontactList_oppdetails.form1" property="opportunityID" value="${opportunityID}" />

<c:choose>
	<c:when test="${widgets['jsp_partnercontactList_oppdetails.table1'].justTiedContact}">
		<c:redirect url="opportunity_tiepartnercontact_oppdetails.jsp?opportunityID=${opportunityID}" />
	</c:when>
</c:choose>
		

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.editPartnerContactDetails'/>

                        </td>
                    </tr>
                    <tr>
                    <td class="sfaRow">

	<x:display name="jsp_partnercontactList_oppdetails.table1"></x:display>
            </td>
        </tr>
        </table>
        <br>
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.newPartnerContact'/>
                            </td>
                        </tr>
                        <tr>
                        <td class="sfaRow">
	<x:display name="jsp_partnercontactList_oppdetails.form1"></x:display>
	
<%--
	<c:set var="buttonBack" value="opportunity_details.jsp?opportunityID=${opportunityID}" scope="request"/>
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
