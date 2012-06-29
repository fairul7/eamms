<%@ page import="kacang.ui.WidgetManager,
                 com.tms.crm.sales.misc.AccessUtil"%>
<%@ include file="/common/header.jsp" %>

<%
                            	WidgetManager wm     = WidgetManager.getWidgetManager(request);
                            	String        userID = wm.getUser().getId();

                                if(!AccessUtil.isSalesPerson(userID)&&!AccessUtil.isSalesManager(userID)&&!AccessUtil.isExternalSalesPerson(userID)&&!AccessUtil.isDashboardUser(userID)&& !AccessUtil.isSalesAdmin(userID))
                                {
%>
    <c:redirect url="ekms/noPermission.jsp"/>
<%
                                }
%>


<x:config >
        <page name="jsp_closedsales_my">
         	<com.tms.crm.sales.ui.ClosedSaleCompleteTable name="table1"  userId="${currentUser.id}" width="100%" />
        </page>
</x:config>

<c:if test="${! empty param.opportunityID}">
    <c:redirect url="closedsale_details.jsp?opportunityID=${param.opportunityID}"/> 
</c:if>

<c:if test="${! empty param.companyID}">
    <c:redirect url="/ekms/sfa/company_view_commain.jsp?companyID=${param.companyID}&backURL=view_my_sales.jsp" />
</c:if>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
           <fmt:message key='sfa.message.opportunities'/> > <fmt:message key='sfa.message.mySales'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
    <x:display name="jsp_closedsales_my.table1" />
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
