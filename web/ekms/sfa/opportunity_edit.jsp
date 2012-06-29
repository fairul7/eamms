<%@ page import="com.tms.crm.sales.ui.OpportunityForm"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_opportunityEdit">
     	<com.tms.crm.sales.ui.OpportunityForm name="form1" type="Edit" />
    </page>
</x:config>


<c:set var="opportunityID" value="${param.opportunityID}" scope="request"/>
<c:set var="onUpdateURL" value="opportunity_details.jsp" scope="request"/>
<c:set var="onChangedToChannelURL" value="partner_list_2.jsp" scope="request"/>
<c:set var="backURL" value="opportunity_details.jsp" scope="request"/>

<c-rt:set var="cancel" value="<%=OpportunityForm.FORWARD_CANCEL%>"  />
<c-rt:set var="closed" value="<%=OpportunityForm.FORWARD_CLOSED%>"  />
<c:if test="${forward.name == closed}" >
	<c:redirect url="/ekms/sfa/closedsale_details.jsp?opportunityID=${widgets['jsp_opportunityEdit.form1'].opportunityID}"/>
</c:if>

<c:if test="${forward.name == cancel}" >
    <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${widgets['jsp_opportunityEdit.form1'].opportunityID}" />
</c:if>

<c:if test="${!empty param.status}">
    <x:set name="jsp_opportunityEdit.form1" property="state" value="${param.status}"/>
</c:if>
<c:choose>
	<c:when test="${empty(opportunityID)}">
		<c:set var="opportunityID" value="${widgets['jsp_opportunityEdit.form1'].opportunityID}" />
	</c:when>
</c:choose>
<x:set name="jsp_opportunityEdit.form1" property="opportunityID" value="${opportunityID}" />
<c:choose>
	<c:when test="${forward.name == 'opportunityUpdated'}">
		<c:set var="changedToChannel" value="${widgets['jsp_opportunityEdit.form1'].changedToChannel}" />
		<script>
		<!--
			<c:choose>
				<c:when test="${not empty(onChangedToChannelURL) && changedToChannel}">
					location = "<c:out value="${onChangedToChannelURL}?opportunityID=${opportunityID}"/>";
				</c:when>
				<c:otherwise>
					location = "<c:out value="${onUpdateURL}?opportunityID=${opportunityID}"/>";
				</c:otherwise>
			</c:choose>
		//-->
		</script>
	</c:when>
</c:choose>




<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp" />
<table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
    <tr valign="top">
    <c:choose>
	    <c:when test="${!empty param.status}">
	     <td align="left" valign="top" class="sfaHeader"><fmt:message key='sfa.message.editSaleDetails'/>
	     </td>
	    </c:when>
	    <c:otherwise>
	    	 <td align="left" valign="top" class="sfaHeader"><fmt:message key='sfa.message.editOpportunity'/>
	    	 </td>
	    </c:otherwise>
	    
    </c:choose>
       
   </tr>
    <tr>
     <td class="sfaRow">
	<x:display name="jsp_opportunityEdit.form1"/>
     </td>
    </tr>
    <tr>
        <td class="sfaFooter">&nbsp;
        </td>

    </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
