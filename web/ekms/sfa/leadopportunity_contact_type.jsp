<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_leadopportunityTiecontact">
        <com.tms.crm.sales.ui.OpportunityContactTypeForm name="contactTypeForm"  width="100%" />
    </page>
</x:config>

<c:if test="${widgets['jsp_leadopportunityTiecontact.contactTypeForm'].contactList == null}">
    <x:set name="jsp_leadopportunityTiecontact.contactTypeForm" property="contactList" value="${widgets['jsp_leadcontactList.table1'].contactList}" />
</c:if>

<%--
<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_leadopportunityTiecontact.form1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_leadopportunityTiecontact.form1" property="opportunityID" value="${opportunityID}" />
--%>
<%--
		<c:redirect url="haspartner.jsp?opportunityID=${opportunityID}"/>
--%>
<%--<c:choose>
	<c:when test="${forward.name == 'updatedOpportunityContactType'}">

        <c:redirect url="opportunity_list.jsp?companyID=<%=session.getAttribute("companyId")%>"/>
	</c:when>
</c:choose>--%>


<c:if test="${forward.name == 'updatedOpportunityContactType'}" >
       <%
            Object obj = session.getAttribute("companyId");
           if(obj!=null){
               pageContext.setAttribute("companyId",(String)obj);
           }
       %>
       <%
            pageContext.setAttribute("leadId",(String)session.getAttribute("leadId"));
       %>
       <c:redirect url="leadopportunity_form.jsp?companyID=${companyId}&leadId=${leadId}"/>
</c:if>



<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
    <fmt:message key='sfa.message.addinganOpportunity'/><c:set var="navSelected" value="1" scope="request"/>

    </td>
    </tr>
    <tr>
    <td class="sfaRow">
    <jsp:include page="includes/navAddOpportunity.jsp"/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_leadopportunityTiecontact.contactTypeForm"/>

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
