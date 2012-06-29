<%@ page import="com.tms.crm.sales.model.OpportunityModule,
				 kacang.Application,
				 com.tms.crm.sales.model.Opportunity"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
	<table border="1" cellpadding="2" cellspacing="0" class="formStyle" width="<c:out value="${form.width}"/>">
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.dateofSale'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.df_OpportunityEnd"/></td>
		</tr>

		<%
			// Getting financial settings : currency symbols
			OpportunityModule om = (OpportunityModule) Application.getInstance().getModule(OpportunityModule.class);
			Opportunity csign = om.getFinancilSetting();
		%>

		<tr>
		<td valign="top"><b><fmt:message key='sfa.message.actualamount'/>: (<%=csign.getCurrencySymbol()%>)</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.tf_OpportunityValue"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.referenceNo'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.tf_CloseReferenceNo"/></td>
		</tr>
		<tr>
			<td valign="top"><b><fmt:message key='sfa.message.remarks'/>:</b></td>
			<td valign="top"><x:display name="${form.absoluteName}.tb_LastRemarks"/>&nbsp;</td>
		</tr>
		<tr>
			<td align="center" colspan="2"><x:display name="${form.absoluteName}.submit"/>
<%--
            <c:if test="${form.cancel != null}" >
--%>
                <x:display name="${form.cancel.absoluteName}" />

<%--
            </c:if>
--%>
            </td>
		</tr>
	</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
