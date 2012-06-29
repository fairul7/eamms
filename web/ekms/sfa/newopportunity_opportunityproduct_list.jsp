<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_opportunityproductList">
     	<com.tms.crm.sales.ui.OpportunityProductTable name="table1" width="100%" />
     	<com.tms.crm.sales.ui.OpportunityProductForm name="form1" type="Add" width="100%" />
    </page>
</x:config>


<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_opportunityproductList.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_opportunityproductList.table1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityproductList.table1" property="linkUrl" value="opportunityproduct_edit.jsp?opportunityID=${opportunityID}&lulu=lala" />
<x:set name="jsp_opportunityproductList.form1" property="opportunityID" value="${opportunityID}" />


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
        <fmt:message key='sfa.message.addinganOpportunity'/>

    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<c:set var="navSelected" value="3" scope="request"/>
	<jsp:include page="includes/navAddOpportunity.jsp"/>
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

	<x:display name="jsp_opportunityproductList.table1"></x:display>
    <input type="button" class="button" value="<fmt:message key='sfa.message.next'/>" onClick="location = 'newopportunity_acdistribution_edit.jsp?opportunityID=<c:out value="${opportunityID}"/>'" />
  </td>
    </tr>
    </table>
    <br>
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">

    <tr>
    <td class="sfaHeader"><fmt:message key='sfa.message.newProducts'/>

     </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_opportunityproductList.form1"></x:display>
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
