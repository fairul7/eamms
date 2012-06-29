<%@ page import="kacang.services.security.SecurityService,
                 kacang.services.security.User"%>
<%@ include file="/common/header.jsp" %>



<x:config>
    <page name="ownleadtablepage">
        <com.tms.crm.sales.ui.LeadTable name="leadtable"/>
    </page>
</x:config>


<% pageContext.setAttribute("userId",((User)session.getAttribute(SecurityService.SESSION_KEY_USER)).getId());  %>

<c:if test="${!empty param.id}" >
    <c:redirect url="/ekms/sfa/leadView.jsp?leadId=${param.id}" />
</c:if>
<x:set name="ownleadtablepage.leadtable" property="userId" value="${userId}"/>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
           <fmt:message key='sfa.message.leads'/> >  <fmt:message key='sfa.message.myLeadsListing'/>
        </td>
    </tr>
    <tr>
    <td class="sfaRow">	<x:display name="ownleadtablepage.leadtable"></x:display></td>
    </tr>
    <tr>
    <td class="sfaFooter">
                         &nbsp;
    </td>
    </tr>

</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
