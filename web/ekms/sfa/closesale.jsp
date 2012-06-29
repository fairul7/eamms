<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.ui.*" %>

<x:config>
    <page name="jsp_closesale">
        <com.tms.crm.sales.ui.CloseSaleForm name="form1" width="100%" />
    </page>
</x:config>

<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_closesale.form1'].opportunityID}" />
	</c:otherwise>
</c:choose>


<c:if test="${forward.name == 'cancel'}">
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${opportunityID}" />
</c:if>

<x:set name="jsp_closesale.form1" property="opportunityID" value="${opportunityID}" />
<c:choose>
	<c:when test="${forward.name == 'opportunityUpdated'}">
		<c:redirect url="main.jsp"/>
	</c:when>
</c:choose>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
    <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
        <tr valign="top">
            <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.closeSale'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<c:set var="infoIndex" value="1" scope="request"/>
	<c:set var="opportunityID" value="${opportunityID}" scope="request"/>
	<jsp:include page="includes/navInfoOpportunity.jsp"/>
 </td>
         </tr>
         <tr>   
   <td class="sfaRow">


	<x:display name="jsp_closesale.form1"/>
     </td>
         </tr>

		<tr><td class="sfaFooter">&nbsp;</td></tr>


     </table>
     <jsp:include page="includes/footer.jsp" />
     <%@include file="/ekms/includes/footer.jsp"%>
