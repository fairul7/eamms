<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_opportunityproductEdit">
     	<com.tms.crm.sales.ui.OpportunityProductForm name="form1" type="Edit" width="100%" />
    </page>
</x:config>

<x:set name="jsp_opportunityproductEdit.form1" property="type" value="Edit" />

<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
		<c:set var="productSeq" value="${param.productSeq}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_opportunityproductEdit.form1'].opportunityID}" />
		<c:set var="productSeq" value="${widgets['jsp_opportunityproductEdit.form1'].productSeq}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_opportunityproductEdit.form1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityproductEdit.form1" property="productSeq" value="${productSeq}" />
<c:choose>
    <c:when test="${forward.name == 'cancel'}">
        <c:redirect url="newopportunity_opportunityproduct_list.jsp?opportunityID=${opportunityID}" />
    </c:when>
	<c:when test="${forward.name == 'opportunityProductUpdated'}">
<%--
		<c:redirect url="opportunityproduct_view.jsp?opportunityID=${opportunityID}&productSeq=${productSeq}"/>
--%>
        <c:redirect url="newopportunity_opportunityproduct_list.jsp?opportunityID=${opportunityID}" />
	</c:when>
</c:choose>


<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
        <td align="left" valign="top" class="sfaHeader">
<fmt:message key='sfa.message.editOpportunityProduct'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">
	<x:display name="jsp_opportunityproductEdit.form1"></x:display>
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
