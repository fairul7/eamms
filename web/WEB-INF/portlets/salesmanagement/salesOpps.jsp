<%@ page import="com.tms.crm.sales.model.OpportunityModule,
                 kacang.Application,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.crm.sales.model.Opportunity,
                 com.tms.crm.sales.model.AccountDistributionModule,
                 java.util.Calendar,
                 java.util.Date"%>

<%@ include file="/common/header.jsp" %>


<%
    OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY,0);
    cal.set(Calendar.MINUTE,0);
    cal.set(Calendar.SECOND,0);
    Date startDate = cal.getTime();
    cal.add(Calendar.MONTH,1);
    cal.add(Calendar.DAY_OF_MONTH,1);
    cal.add(Calendar.SECOND,-1);
    Date endDate = cal.getTime();
    Collection col = om.listOpportunities(null,null,null,"4",-100,false,false,startDate,endDate,"opportunityEnd",false,0,-1);
    AccountDistributionModule accountDistributionModule = (AccountDistributionModule) Application.getInstance().getModule(AccountDistributionModule.class);

    for (Iterator iterator = col.iterator(); iterator.hasNext();) {
        Opportunity opportunity = (Opportunity) iterator.next();
        opportunity.setProperty("names" ,accountDistributionModule.getUsers(opportunity.getOpportunityID())); //
    }

    pageContext.setAttribute("col",col);

%>

<table cellspacing="0" cellpadding="4" width="100%" >

    <Tr>
       <%-- <td>&nbsp;</td>--%>
        <td valign="top"><b><fmt:message key='sfa.label.opportunityDescription'/></b></td>
        <td valign="top"><b><fmt:message key='sfa.message.company'/></b></td>
        <td valign="top"><b><fmt:message key='sfa.label.closingDate'/></b></td>
        <td valign="top"><b><fmt:message key='sfa.label.accManager'/></b></td>
    </tr>
    <c:forEach var="opportunity" items="${col}">
        <tr> <%--<TD align="right">&nbsp;</TD>--%>
            <Td valign="top">
              <%--<a href="<c:url value="/ekms/sfa/opportunity_details.jsp?opportunityID=${opportunity.opportunityID}"/>">--%>
                    <c:out value="${opportunity.opportunityName}" />
                <%--</a>--%></b>
            </td>
            <td  valign="top">
                <c:out value="${opportunity.propertyMap['companyName']}" />
            </td>
            <td  valign="top">
               <fmt:formatDate pattern="${globalDateLong}" value="${opportunity.opportunityEnd}"/>
            </td>
            <td  valign="top">
                <c:out value="${opportunity.propertyMap['names']}" />
            </td>
        </tr>

    </c:forEach>

    <TR>   <%-- <TD> &nbsp;</TD>--%>
        <TD colspan="4">
            &nbsp;
        </TD>
    </TR>

    <tr>   <%--<TD class="sfaFooter"> &nbsp;&nbsp; </TD>--%>
        <td class="sfaFooter" colspan="4">
            &nbsp;
        </td>
    </tr>

</table>