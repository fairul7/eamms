<%@ page import="kacang.ui.WidgetManager,
                 com.tms.crm.sales.misc.AccessUtil"%>
<%@ include file="/common/header.jsp" %>

<%
	WidgetManager wm     = WidgetManager.getWidgetManager(request);
	String        userID = wm.getUser().getId();

	if(!AccessUtil.isSalesPerson(userID)&&!AccessUtil.isSalesManager(userID)&&!AccessUtil.isExternalSalesPerson(userID)&&!AccessUtil.isDashboardUser(userID)&& !AccessUtil.isSalesAdmin(userID))
	{
%>
    <c:redirect url="noPermission.jsp"/>
<%
                                }
%>

<x:config >
        <page name="jsp_viewsales_individual">
         	<com.tms.crm.sales.ui.ViewSalesReportInvidual name="report"  width="100%" />
        </page>
</x:config>


<c:if test="${! empty param.opportunityID}">
    <c:redirect url="closedsale_details.jsp?opportunityID=${param.opportunityID}"/>
</c:if>

<c:if test="${! empty param.companyID}">
    <c:redirect url="/ekms/sfa/company_view_commain.jsp?companyID=${param.companyID}&backURL=view_all_sales.jsp" />
</c:if>

	<%--<c:if test="${! empty param.userId}">
		<x:set name="jsp_viewsales_individual.report" property="userId" value="${param.userId}"/>
	</c:if>--%>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
           <fmt:message key='sfa.message.opportunities'/> > <fmt:message key='sfa.message.allSales'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
    <x:display name="jsp_viewsales_individual.report" />
    </td>
    </tr>
    <tr>
    <td class="sfaFooter">&nbsp;</td></tr>

 </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

