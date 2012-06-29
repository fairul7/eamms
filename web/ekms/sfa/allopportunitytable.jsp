<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.misc.*, java.util.*,
                 kacang.ui.WidgetManager"%>
<%
	WidgetManager wm     = WidgetManager.getWidgetManager(request);
	String        userID = wm.getUser().getId();
	if(!AccessUtil.isSalesManager(userID)  && !AccessUtil.isSalesAdmin(userID))
	{
%>
    <c:redirect url="noPermission.jsp"/>
<%
	}
%>

<x:config>
    <page name="jsp_main">
     	<com.tms.crm.sales.ui.OpportunityCompleteTable name="table1"  width="100%" />
    </page>
</x:config>

<c:if test="${!empty param.opportunityID}">
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${param.opportunityID}" />
</c:if>
<c:if test="${! empty param.companyID}">
    <c:redirect url="/ekms/sfa/company_view_commain.jsp?companyID=${param.companyID}&backURL=allopportunitytable.jsp" />
</c:if>
<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
	<tr valign="top">
		<td align="left" valign="top" class="sfaHeader">
			<fmt:message key='sfa.message.opportunities'/> > <fmt:message key='sfa.message.allOpportunities'/>
		</td>
	</tr>
    <tr><td class="sfaRow"><x:display name="jsp_main.table1"/></td></tr>
	<tr>
		<td class="sfaRow">
		<c:if test="${!widgets['jsp_main.table1'].cb_showClosed.checked}">
			<table cellpadding="2" cellspacing="1" width="95%" align="center">
            	<tr>
					<td width="5"><img src="<c:url value="/ekms/sfa/images/01.gif"/>"></td>
					<td><fmt:message key="sfa.label.recentlyUpdated"/></td>
				</tr>
				<tr>
					<td width="5"><img src="<c:url value="/ekms/sfa/images/02.gif"/>"></td>
					<td>15 <fmt:message key="sfa.label.daysSinceLastUpdate"/></td>
				</tr>
				<tr>
					<td width="5"><img src="<c:url value="/ekms/sfa/images/03.gif"/>"></td>
					<td>30 <fmt:message key="sfa.label.daysSinceLastUpdate"/></td>
				</tr>
			</table>
			</c:if>
			<br>
		</td>
	</tr>
    <tr><td class="sfaFooter">&nbsp;</td></tr>
</table>
 <%-- 
<c:if test="${empty param.checking}">
<c:choose>
<c:when test="${widgets['jsp_main.table1'].cb_showClosed.checked}">
	<x:set name="jsp_main.table1" property="checked" value="${true}"/>
	<script>
	var test = false;
	if(document.forms['jsp_main.table1'].elements['jsp_main.table1.filterForm.cbshowclosed'].checked==true){
		test = true;
	}
	
	if(test){
		window.location="allopportunitytable.jsp?checking=yes";
		test=false;
	}
	</script>
	
</c:when>
<c:otherwise>
	<script>
		alert("");
	</script>
	<x:set name="jsp_main.table1" property="checked" value="${false}"/>
</c:otherwise>
</c:choose>
</c:if>
--%>
<%-- 
<c:if test="${widgets['jsp_main.table1'].cb_showClosed.checked}">
	<x:set name="jsp_main.table1" property="checked" value="${false}"/>
</c:if>
--%>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>