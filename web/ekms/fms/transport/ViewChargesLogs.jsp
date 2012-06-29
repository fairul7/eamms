<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.ui.RateCardForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.admin" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
	<page name="RateCard">
		<com.tms.fms.transport.ui.RateCardForm name="form"/>
		<com.tms.fms.transport.ui.ChargesLogsTable name="table"/>
	</page>
</x:config>

<c:choose>
	<c:when test="${not empty(param.id)}">
		<c:set var="setupId" value="${param.id}"/>
	</c:when>
	<c:otherwise>
		<c:set var="setupId" value="${widgets['EditVehicle.form'].id}"/>
	</c:otherwise>
</c:choose>

<x:set name="RateCard.form" property="id" value="${setupId}"/>
<x:set name="RateCard.table" property="id" value="${setupId}"/>
<x:set name="RateCard.form" property="action" value="<%= RateCardForm.FORM_ACTION_VIEW %>"/>
<x:set name="RateCard.form" property="cancelUrl" value="RateCard.jsp"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.transportChargesDetails'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="RateCard.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr valign="middle">
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.logHistory'/></font></b></td>
	    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
	</tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	
		<x:display name="RateCard.table" ></x:display>
	
	</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>