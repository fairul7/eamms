<%@page import="java.util.*, java.text.*, com.tms.crm.sales.model.*, com.tms.crm.sales.misc.*" %>
<%@page import="kacang.*, kacang.services.security.*, kacang.ui.WidgetManager" %>
<%@ include file="/common/header.jsp" %>

<%
    String userId = ((User)session.getAttribute(SecurityService.SESSION_KEY_USER)).getId();
    if(AccessUtil.isExternalSalesPerson(userId)||AccessUtil.isSalesManager(userId)||AccessUtil.isSalesPerson(userId))
    {
    OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
    Collection col = om.listOpportunities(null,null,userId,null,1,false,false,null,null,"opportunityEnd",false,0,-1);
    pageContext.setAttribute("col",col);



%>


<table cellspacing="0" cellpadding="4" width="100%" >
    <c:forEach var="opportunity" items="${col}">
        <tr> <TD align="right">&nbsp;</TD>
            <Td>
              <b><a href="<c:url value="/ekms/sfa/opportunity_details.jsp?opportunityID=${opportunity.opportunityID}"/>">
                    <c:out value="${opportunity.opportunityName}" />
                </a></b> - <c:out value="${opportunity.propertyMap['companyName']}" />
                 - <fmt:formatDate pattern="${globalDateLong}" value="${opportunity.opportunityEnd}"/>
            </td>
        </tr>

    </c:forEach>

    <TR>    <TD> &nbsp;</TD>
        <TD>
            &nbsp;
        </TD>
    </TR>

    <tr>   <TD class="sfaFooter"> &nbsp;&nbsp; </TD>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>

</table>
<%}
    else{

%>
  &nbsp;  You have no access to this portlet.
<%}%>