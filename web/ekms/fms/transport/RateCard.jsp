<%@ page import="kacang.ui.WidgetManager, com.tms.fms.transport.ui.RateCardForm, com.tms.fms.transport.ui.RateCardTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.transport.admin" module="com.tms.fms.transport.model.TransportModule" url="/ekms/home.jsp"/>

<x:config>
	<page name="RateCard">
		<com.tms.fms.transport.ui.RateCardForm name="form"/>
		<com.tms.fms.transport.ui.RateCardTable name="table"/>
	</page>
</x:config>

<c-rt:set var="forward_succ" value="<%= RateCardForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= RateCardForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_exist" value="<%= RateCardForm.FORWARD_ADD_EXIST %>"/>
<c-rt:set var="forward_edit_succ" value="<%= RateCardForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_edit_fail" value="<%= RateCardForm.FORWARD_EDIT_FAIL %>"/>
<c-rt:set var="forward_listing_add" value="<%= RateCardTable.FORWARD_LISTING_ADD %>"/>

<x:set name="RateCard.form" property="id" value="driver"/>
<x:set name="RateCard.form" property="action" value="<%= RateCardForm.FORM_ACTION_DRIVER %>"/>
<x:set name="RateCard.form" property="historyUrl" value="ViewChargesLogs.jsp?id=default"/>
<x:set name="RateCard.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<c:choose>
	<c:when test="${forward.name == forward_edit_succ}">
		<script>alert("<fmt:message key='fms.tran.msg.recordUpdated'/>");document.location = "<c:url value="RateCard.jsp"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_edit_fail}">
		<script>alert("<fmt:message key='fms.tran.msg.failToUpdateRecord'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_add}">
		<script>document.location = "<c:url value="AddTransportCharges.jsp"/>";</script>
</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.driverCharges'/></font></b></td>
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
	    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.tran.setup.transportChargesListing'/></font></b></td>
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












