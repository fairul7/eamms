<%@ page import="java.util.Date,
                 com.tms.crm.sales.ui.OpportunitySummaryTable,
				 com.tms.crm.sales.model.OpportunityModule,
				 kacang.Application,
				 com.tms.crm.sales.model.Opportunity"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<c:if test="${not empty(headerText)}">
	<c:out value="${headerText}" escapeXml="false"/>
</c:if>
<c:choose>
<c:when test="${form.type == 'View'}">
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.opportunity'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.lbOpportunityName"/></td>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.type'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.lbHasPartner"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.status'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStatus"/></td>
			<td valign="top"><b><fmt:message key='sfa.message.stage'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStage"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.startdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityStart"/></td>
			<td valign="top"><b><fmt:message key='sfa.message.estimatedclosingdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityEnd"/></td>
		</tr>

		<%
			// Getting financial settings : currency symbols
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
		%>

		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.source'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunitySource"/>&nbsp;</td>
			<td valign="top"><b><fmt:message key='sfa.message.value'/>: (<%=csign.getCurrencySymbol()%>)</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.lbOpportunityValue"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.remarks'/>:</b></td>
			<td colspan="3" valign="top"><x:display name="${form.absoluteName}.lbOpportunityLastRemarks"/>&nbsp;</td>
		</tr>

        <c:set value="${form.lastModified}"  var="lastModified" />

        <tr>
            <td valign="top">
                <b><fmt:message key="sfa.label.lastModified"/>:</b>
            </td>
            <td colspan="3" valign="top">
              <%
            Date lastModified =  (Date) pageContext.getAttribute("lastModified");
            Date today = new Date();
            long diff = today.getTime() - lastModified.getTime();
            int day = (int)(diff/(1000*60*60*24));
            if(day>=OpportunitySummaryTable.red){
                %>
                <img src="<c:url value="/ekms/sfa/images/03.gif"/>"/>
             <%
            }else if(day>=OpportunitySummaryTable.orange){%>
                <img src="<c:url value="/ekms/sfa/images/02.gif"/>"/>
            <%}
        %>
               <fmt:formatDate value="${form.lastModified}" pattern="${globalDateLong}" />
            </td>
        </tr>

	</table>
</c:when>
<c:when test="${form.type == 'Edit' or form.type == 'Add'}">
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
		<c:choose>
			<c:when test="${!empty param.status}">
				<td valign="top" width="20%"><b><fmt:message key='sfa.message.sale'/>: &nbsp;*</b></td>
			</c:when>
			<c:otherwise>
				<td valign="top" width="20%"><b><fmt:message key='sfa.message.opportunity'/>: &nbsp;*</b></td>
			</c:otherwise>
		</c:choose>
		
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.tf_OpportunityName"/></td>
			<td valign="top" width="20%"><b><fmt:message key='sfa.message.type'/>:</b></td>
			<td valign="top" width="30%"><x:display name="${form.absoluteName}.sel_HasPartner"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.status'/>:</b></td>
			<td valign="top">
				<c:choose>
					<c:when test="${form.type == 'Add' || form.editStatusUseLabel}">
						<x:display name="${form.absoluteName}.lbOpportunityStatus"/>
					</c:when>
					<c:otherwise>
						<x:display name="${form.absoluteName}.sel_OpportunityStatus"/>
					</c:otherwise>
				</c:choose>
			</td>
			<td valign="top"><b><fmt:message key='sfa.message.stage'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.sel_OpportunityStage"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.startdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.df_OpportunityStart"/></td>
			<td valign="top"><b><fmt:message key='sfa.message.estimated'/> <br><fmt:message key='sfa.message.closingdate'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.df_OpportunityEnd"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.source'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.sel_OpportunitySource"/></td>
			<td valign="top" colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.remarks'/>:</b></td>
			<td colspan="3" valign="top"><x:display name="${form.absoluteName}.tb_LastRemarks"/>&nbsp;</td>
		</tr>
		<tr>
			<td align="center" colspan="4"><x:display name="${form.absoluteName}.submit"/><x:display name="${form.absoluteName}.Cancel"/></td>
		</tr>
	</table>
</c:when>
</c:choose>

<jsp:include page="../form_footer.jsp" flush="true"/>
