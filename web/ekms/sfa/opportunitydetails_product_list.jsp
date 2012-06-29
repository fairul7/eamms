<%@ include file="/common/header.jsp" %>
<%@page import="java.util.*, com.tms.crm.sales.ui.*, kacang.ui.WidgetManager" %>

<x:config>
    <page name="jsp_opportunityproductList_2">
     	<com.tms.crm.sales.ui.OpportunityProductTable name="table1" width="100%" />
     	<com.tms.crm.sales.ui.OpportunityProductForm name="form1" type="Add" width="100%" />
    </page>
</x:config>

<c:if test="${! empty param.productSeq}">
    <x:set name="jsp_opportunityproductList_2.form1" property="type" value="Edit" />
    <x:set name="jsp_opportunityproductList_2.form1" property="productSeq" value="${param.productSeq}" />
</c:if>

<c-rt:set var="cancel" value="<%= OpportunityProductForm.FORWARD_CANCEL %>"/>
<c-rt:set var="closed" value="<%=OpportunityProductForm.FORWARD_CLOSED%>"  />
<c:if test="${forward.name == closed}" >
	<c:redirect url="/ekms/sfa/closedsale_details.jsp?opportunityID=${widgets['jsp_opportunityproductList_2.table1'].opportunityID}"/>
</c:if>
<c:if test="${forward.name == cancel}" >

    <c:set var="type" value="${widgets['jsp_opportunityproductList_2.form1'].type}"/>
    <c:if test="${type == 'Add'}" >
      <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${widgets['jsp_opportunityproductList_2.table1'].opportunityID}"  />
    </c:if>
</c:if>

<c:if test="${!empty param.status}">
    <x:set name="jsp_opportunityproductList_2.form1" property="state" value="${param.status}"/>
    <x:set name="jsp_opportunityproductList_2.form1" property="state" value="${param.status}"/>
</c:if>

<c:choose>
	<c:when test="${not empty(param.opportunityID)}">
		<c:set var="opportunityID" value="${param.opportunityID}" />
	</c:when>
	<c:otherwise>
		<c:set var="opportunityID" value="${widgets['jsp_opportunityproductList_2.table1'].opportunityID}" />
	</c:otherwise>
</c:choose>
<x:set name="jsp_opportunityproductList_2.table1" property="opportunityID" value="${opportunityID}" />
<x:set name="jsp_opportunityproductList_2.form1" property="opportunityID" value="${opportunityID}" />

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
            <fmt:message key='sfa.message.editProductDistribution'/>
            </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_opportunityproductList_2.table1"/>

            </td>
    </tr>
    <tr>
    <td class="sfaRow">
	    <c:choose>
	    	<c:when test="${!empty param.status}">
	    		<input type="button" class="button" value="<fmt:message key='sfa.message.back'/>" onClick="location = 'closedsale_details.jsp?opportunityID=<c:out value="${widgets['jsp_opportunityproductList_2.table1'].opportunityID}"/>'"/>
	    	</c:when>
	    	<c:otherwise>
	    		<input type="button" class="button" value="<fmt:message key='sfa.message.back'/>" onClick="location = 'opportunity_details.jsp?opportunityID=<c:out value="${widgets['jsp_opportunityproductList_2.table1'].opportunityID}"/>'"/>
	    	</c:otherwise>
	    </c:choose>
	   
        
    </td></tr>
    </table>
          <br>

        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">

<c:choose>
    <c:when test="${! empty param.productSeq}" >
        <fmt:message key='sfa.message.editProduct'/>
    </c:when>
    <c:otherwise>
        <fmt:message key='sfa.message.newProducts'/>
    </c:otherwise>
</c:choose>
              </td>
                </tr>
                <tr>
            <td class="sfaRow">
        	<x:display name="jsp_opportunityproductList_2.form1"></x:display>
                </td>
        </tr>
        <tr>
        <td class="sfaFooter">&nbsp;</td></tr>
        </table>

<%@include file="/ekms/includes/footer.jsp"%>
<jsp:include page="includes/footer.jsp"/>
