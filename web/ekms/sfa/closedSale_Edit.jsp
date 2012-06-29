<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.crm.sales.ui.*" %>

<x:config>
    <page name="jsp_closesaleedit">
        <com.tms.crm.sales.ui.ClosedSaleEditForm name="form1" width="100%" />
    </page>
</x:config>

<c:if test="${forward.name == 'cancel'}" >
    <c:redirect url="/ekms/sfa/closedsale_details.jsp?opportunityID=${widgets['jsp_closesaleedit.form1'].opportunityID}" />

</c:if>

<c:if test="${forward.name == 'opportunityUpdated' }" >
  <%
    System.out.println("xxxxxxxxxxxxxxxxx");

  %>
    <script>
        alert("<fmt:message key='sfa.message.saleupdated'/>!");
        location =  "closedsale_details.jsp?opportunityID=<c:out value="${widgets['jsp_closesaleedit.form1'].opportunityID}"/>";
    </script>
    <%
        if(true) {
            return;
        }
    %>
</c:if>


<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_closesaleedit.form1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_closesaleedit.form1" property="opportunityID" value="${opportunityID}" />
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



	<x:display name="jsp_closesaleedit.form1"/>

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
