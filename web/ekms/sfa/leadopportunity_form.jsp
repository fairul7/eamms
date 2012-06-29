<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, kacang.ui.WidgetManager, kacang.services.security.*,
                kacang.Application" %>
<%@page import="com.tms.crm.sales.misc.*, com.tms.crm.sales.model.*, com.tms.crm.sales.ui.*" %>

<x:config>
    <page name="jsp_leadopportunityList">
     	<com.tms.crm.sales.ui.LeadOpportunityForm name="form1" type="Add" width="100%" />
    </page>
</x:config>

<%--
<c:if test="${widgets['jsp_opportunityList.form1'].contacts == null}">
--%>
    <x:set name="jsp_leadopportunityList.form1" property="contacts" value="${widgets['jsp_leadopportunityTiecontact.contactTypeForm'].contacts}" />
<%--
</c:if>
--%>

<c:if test="${! empty param.leadId}">
    <x:set name="jsp_leadopportunityList.form1" property="leadId" value="${param.leadId}"/>
</c:if>

<c:choose>
	<c:when test="${not empty(param.companyID)}">
		<c:set var="companyID" value="${param.companyID}" />
	</c:when>
	<c:otherwise>
		<c:set var="companyID" value="${widgets['jsp_leadopportunityList.table1'].companyID}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${not empty(param.first)}">
		<c:set var="firstTime" value="1" />
	</c:when>
	<c:otherwise>
		<c:set var="firstTime" value="0" />
	</c:otherwise>
</c:choose>
<%--
<x:set name="jsp_opportunityList.table1" property="companyID" value="${companyID}" />
<x:set name="jsp_opportunityList.table1" property="linkUrl" value="opportunity_view.jsp" />
<x:set name="jsp_opportunityList.table1" property="firstTime" value="${firstTime}" />
--%>
<x:set name="jsp_leadopportunityList.form1" property="companyID" value="${companyID}" />
<c:choose>
	<c:when test="${forward.name == 'opportunityAdded'}">
		<c:redirect url="newopportunity_opportunityproduct_list.jsp?opportunityID=${widgets['jsp_leadopportunityList.form1'].justCreatedID}"/>
	</c:when>

</c:choose>


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

	<c:set var="navSelected" value="2" scope="request"/>
	<jsp:include page="includes/navAddOpportunity.jsp"/>

        </td>
        </tr>
        <tr>
        <td class="sfaRow">

	<c:set var="infoIndex" value="0" scope="request"/>
	<c:set var="companyID" value="${companyID}" scope="request"/>
	<jsp:include page="includes/navInfoOpportunity.jsp"/>
    </td>
    </tr>
    </table>
    <br>
   <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">

    <tr>
    <td class="sfaHeader">
    <fmt:message key='sfa.message.newOpportunity'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_leadopportunityList.form1"></x:display>
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
