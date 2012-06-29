<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_acEdit_2">
        <com.tms.crm.sales.ui.AccountDistributionForm name="form1" />
    </page>
</x:config>


<x:set name="jsp_acEdit_2.form1" property="opportunityID" value="${param.opportunityID}" />
<%
	WidgetManager wm = WidgetManager.getWidgetManager(request);
	AccountDistributionForm adForm = (AccountDistributionForm) wm.getWidget("jsp_acEdit_2.form1");
	request.setAttribute("opportunityID", adForm.getOpportunityID());
%>
<c:choose>
	<c:when test="${forward.name == 'distributionSaved'}">
		<c:redirect url="opportunity_details.jsp?opportunityID=${opportunityID}"/>
	</c:when>
	<c:when test="${forward.name == 'closed'}">
		<c:redirect url="/ekms/sfa/closedsale_details.jsp?opportunityID=${opportunityID}"/>
	</c:when>
</c:choose>

<c:if test="${!empty param.status}">
    <x:set name="jsp_acEdit_2.form1" property="state" value="${param.status}"/>
</c:if>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.editAccountDistribution'/>        </td></tr>
    <tr>
     <td class="sfaRow">
	<x:display name="jsp_acEdit_2.form1"/>
	</td>
    </tr>
    <tr>
        <td class="sfaFooter">&nbsp;
        </td>

    </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
