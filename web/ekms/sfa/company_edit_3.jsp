<%@ include file="/common/header.jsp" %>
<%@page import="com.tms.crm.sales.ui.*" %>

<c:set var="companyID" value="${param.companyID}" scope="request"/>
<c:set var="opportunityID" value="${param.opportunityID}" scope="request"/>

<c:set var="onUpdateURL" value="opportunity_details.jsp" scope="request"/>
<c:set var="backURL" value="opportunity_details.jsp" scope="request"/>


	<x:config>
	    <page name="jsp_companyEdit_3">
	     	<com.tms.crm.sales.ui.CompanyForm name="form1" type="Edit" width="100%" />
	    </page>
	</x:config>
	<c:if test="${forward.name == 'closed'}" >
		<c:redirect url="/ekms/sfa/closedsale_details.jsp?opportunityID=${widgets['jsp_companyEdit_3.form1'].opportunityID}"/>
	</c:if>
    
    <c:if test="${forward.name == 'cancel'}" >
        <c:redirect url="/ekms/sfa/opportunity_details.jsp?opportunityID=${widgets['jsp_companyEdit_3.form1'].opportunityID}" />            
    </c:if>
    
	<c:if test="${empty(companyID)}">
		<c:set var="companyID" value="${widgets['jsp_companyEdit_3.form1'].companyID}" />
	</c:if>
	<c:if test="${empty(opportunityID)}">
		<c:set var="opportunityID" value="${widgets['jsp_companyEdit_3.form1'].opportunityID}" />
	</c:if>
	
	<x:set name="jsp_companyEdit_3.form1" property="companyID" value="${companyID}" />
	<x:set name="jsp_companyEdit_3.form1" property="opportunityID" value="${opportunityID}" />
	
	<c:if test="${!empty param.status}">
    	<x:set name="jsp_companyEdit_3.form1" property="status" value="${param.status}"/>
	</c:if>
	<c:choose>
		<c:when test="${forward.name == 'companyUpdated'}">
			<script>
			<!--
				location = "<c:out value="${onUpdateURL}?opportunityID=${opportunityID}"/>";
			//-->
			</script>
		</c:when>
		<c:when test="${forward.name == 'companyDuplicate'}"><script>
			<!--
				alert ("<fmt:message key='sfa.message.duplicatedError'/>.");
				location = "<c:out value="${onUpdateURL}?opportunityID=${opportunityID}"/>";
			//-->
			</script>
		</c:when>
	</c:choose>
	
    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="top">
                <td align="left" valign="top" class="sfaHeader">
                <fmt:message key='sfa.message.editCompany'/>
    </td>
    </tr>
    <tr>
    <td class="sfaRow">

	<x:display name="jsp_companyEdit_3.form1"></x:display>
	
	</td></tr>

    <tr>
        <td class="sfaFooter">
            &nbsp;
        </td>
    </tr>
    </table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>
