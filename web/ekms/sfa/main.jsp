<%@ page import="kacang.ui.WidgetManager,
                 com.tms.crm.sales.misc.AccessUtil,
                 java.util.Date,
                 com.tms.crm.sales.misc.DateUtil,
                 java.util.Calendar,
                 kacang.services.security.SecurityService"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_main">
     	<com.tms.crm.sales.ui.OpportunitySummaryTable name="table1"  width="100%" />
    </page>
</x:config>



<%
	WidgetManager wm     = WidgetManager.getWidgetManager(request);
	String        userID = wm.getUser().getId();

	boolean isSalesManager        = AccessUtil.isSalesManager(userID);
	boolean isSalesAdmin 			  = AccessUtil.isSalesAdmin(userID);
	boolean isSalesPerson         = AccessUtil.isSalesPerson(userID);
	boolean isExternalSalesPerson = AccessUtil.isExternalSalesPerson(userID);
	boolean isDashboardUser       = AccessUtil.isDashboardUser(userID);
%>
<% if (userID.equals(SecurityService.ANONYMOUS_USER_ID)) { %>
	<c:redirect url="../ekms/login.jsp" />
<% } %>
<% if (!isSalesManager && !isSalesPerson && !isExternalSalesPerson && !isDashboardUser && !isSalesAdmin) { %>
	<c:redirect url="noPermission.jsp" />
<% } %>


<c:if test="${! empty param.companyID}">
    <c:redirect url="/ekms/sfa/company_view_commain.jsp?companyID=${param.companyID}&backURL=main.jsp" />
</c:if>


<c:if test="${!empty param.opportunityID}">
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${param.opportunityID}" />
</c:if>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="0" valign="top">
    <tr valign="top">
        <td align="center" valign="top">

    	    <jsp:include page="includes/disp_figures.jsp"/>


            <% if (isSalesManager || isSalesPerson || isExternalSalesPerson || isSalesAdmin) { %>
		    <%
			Date today     = DateUtil.getToday();
			Date startDate = DateUtil.getDate(DateUtil.getYear(today), DateUtil.getMonth(today), 1);
			Date endDate   = DateUtil.dateAdd(DateUtil.dateAdd(startDate, Calendar.MONTH, 3), Calendar.DATE, -1);

			String currRange;
			if (DateUtil.getYear(startDate) == DateUtil.getYear(endDate)) {
				currRange = DateUtil.formatDate("MMM", startDate) + " - " + DateUtil.formatDate("MMM yyyy", endDate);
			} else {
				currRange = DateUtil.formatDate("MMM yyyy", startDate) + " - " + DateUtil.formatDate("MMM yyyy", endDate);
			}
		%>
        <Tr>
        <td class="sfaRow">
		<span class="sfaRowLabel"><fmt:message key='sfa.message.yourCurrentOpportunities'/></span>
		<span class="smallTitleBoldStyle">&gt; <%= currRange %> &gt;
               <input type="button" class="button" value="<fmt:message key='sfa.message.viewAll'/>" onClick="location = 'myopportunitytable.jsp' "/>
               <input type="button" class="button" value="<fmt:message key='sfa.message.newOpportunity'/>" onClick="location = 'newopportunity_company_list.jsp'"/>
<%--
			<a href="company_list.jsp">Add an opportunity</a></span>
--%>
		<br>
		<x:display name="jsp_main.table1"></x:display>
            </td></tr>



	<% } %>

  </table>

        </td>
    </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

