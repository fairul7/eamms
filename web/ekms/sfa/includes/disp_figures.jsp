<%@ page import="kacang.*, java.util.*, java.text.*" %>
<%@ page import="com.tms.crm.sales.model.*, com.tms.crm.sales.misc.*" %>
<%@ page import="kacang.ui.WidgetManager, kacang.services.security.*, java.net.URLEncoder" %>
<%@ include file="/common/header.jsp" %>

<%
	//variables
	OPPModule opp = (OPPModule)Application.getInstance().getModule(OPPModule.class);
	Collection rows = null;

	//dynamic values	
	WidgetManager wm       = WidgetManager.getWidgetManager(request);
	String        userID   = wm.getUser().getId();
	
	boolean isSalesManager        = AccessUtil.isSalesManager(userID);
	boolean isSalesPerson         = AccessUtil.isSalesPerson(userID);
	boolean isSalesAdmin          = AccessUtil.isSalesAdmin(userID);
	boolean isExternalSalesPerson = AccessUtil.isExternalSalesPerson(userID);
	boolean isDashboardUser       = AccessUtil.isDashboardUser(userID);
	
	boolean showIntelligenceSummary = false;
	if (isSalesManager || isDashboardUser || isSalesAdmin) {
		showIntelligenceSummary = true;
	}
	
	boolean showPersonalSummary = false;
	if (isSalesManager || isSalesPerson || isExternalSalesPerson || isSalesAdmin) {
		showPersonalSummary = true;
	}
	
	Date nowdate = DateUtil.getToday();
	//END: dynamic values
	
	NumberFormat numFormat = DecimalFormat.getNumberInstance();
    numFormat.setMaximumFractionDigits(2);
    numFormat.setMinimumFractionDigits(2);
	request.setAttribute("userID", userID);
	request.setAttribute("nowdate", nowdate);
	request.setAttribute("numFormat", numFormat);
	request.setAttribute("opp", opp);
%>

<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr>
        <td class="sfaHeader">
           <fmt:message key='sfa.message.salesForceAutomation'/> > <fmt:message key='sfa.message.main'/>
        </td>
    </tr>


<% if (showIntelligenceSummary) { %>
	<tr>
        <td class="sfaRow">
    <jsp:include page="disp_figures_manager.jsp" />
    </td>
    </tr>

<Tr>
    <td class="sfaRow">
<jsp:include page="disp_figures_performance.jsp" />
    </td>
 </tr>
<% } %>
<% if (showPersonalSummary) { %>
<%--
	<br><br>
--%>
    <tr>
        <td class="sfaRow">
	<jsp:include page="disp_figures_personal.jsp" />
        </td></tr>
<% } %>

