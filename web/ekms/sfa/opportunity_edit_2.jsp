<%@ page import="com.tms.crm.sales.ui.OpportunityForm"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_opportunityEdit_new">
     	<com.tms.crm.sales.ui.OpportunityForm name="form1" type="Edit" />
    </page>
</x:config>


<x:set name="jsp_opportunityEdit_new.form1" property="opportunityID" value="${param.opportunityID}" />
<c-rt:set var="cancel" value="<%=OpportunityForm.FORWARD_CANCEL%>"  />

<c:if test="${forward.name == cancel}" >
    <c:redirect url="/ekms/sfa/opportunity_view.jsp?opportunityID=${widgets['jsp_opportunityEdit_new.form1'].opportunityID}" />
</c:if>

<c:if test="${forward.name == 'opportunityUpdated'}" >
    <c:redirect url="/ekms/sfa/opportunity_view.jsp?opportunityID=${widgets['jsp_opportunityEdit_new.form1'].opportunityID}" />

</c:if>


<c:set var="opportunityID" value="${param.opportunityID}" scope="request"/>
<c:set var="onUpdateURL" value="opportunity_view_2.jsp" scope="request"/>
<c:set var="backURL" value="opportunity_view_2.jsp" scope="request"/>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.editOpportunity'/>
           </td>
    </tr>
    <tr>
    <td class="sfaRow">
	    <x:display name="jsp_opportunityEdit_new.form1"/>

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
