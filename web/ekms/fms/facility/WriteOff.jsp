<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.ClosingItemForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Writeoff">
    	<com.tms.fms.facility.ui.ClosingItemForm name="form"/>
    </page>
</x:config>

<c-rt:set var="forward_succ" value="<%= ClosingItemForm.FORWARD_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= ClosingItemForm.FORWARD_ADD_FAIL %>"/>
<c-rt:set var="forward_attachment" value="<%= ClosingItemForm.FORWARD_NO_ATTACHMENT %>"/>

<c:choose>
	<c:when test="${not empty(paramValues.selectedKeys)}">
		<c:set var="selectedKeys" value="${paramValues.selectedKeys}"/>
	</c:when>
	<c:otherwise>
		<c:set var="selectedKeys" value="${widgets['Writeoff.form'].item_code}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty(param.fid)}">
		<c:set var="facilityID" value="${param.fid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="facilityID" value="${widgets['Writeoff.form'].facility_id}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty(param.mode)}">
		<c:set var="mode" value="${param.mode}"/>
	</c:when>
	<c:otherwise>
		<c:set var="mode" value="${widgets['Writeoff.form'].mode}"/>
	</c:otherwise>
</c:choose>

<x:set name="Writeoff.form" property="mode" value="${mode}"/>
<x:set name="Writeoff.form" property="facility_id" value="${facilityID}"/>
<x:set name="Writeoff.form" property="item_code" value="${selectedKeys}"/>
<x:set name="Writeoff.form" property="cancelUrl" value="ViewFacility.jsp?fid=${facilityID}"/>
<c:set var="urlRedirect" value="ViewFacility.jsp?fid=${facilityID}"/>
<c:if test="${mode == 'search'}">
	<x:set name="Writeoff.form" property="cancelUrl" value="BarcodeSearching.jsp"/>
	<c:set var="urlRedirect" value="BarcodeSearching.jsp"/>
</c:if>
<x:set name="Writeoff.form" property="action" value="<%= ClosingItemForm.FORM_ACTION_ADD %>"/>
<x:set name="Writeoff.form" property="type" value="<%= ClosingItemForm.FORM_TYPE_WRITEOFF %>"/>
<x:set name="Writeoff.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Write off successfully!"); document.location = "<c:url value="${urlRedirect}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Fail to write off one or more items!"); document.location = "<c:url value="${urlRedirect}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_attachment}">
		<script>alert("<fmt:message key='fms.facility.msg.pleaseAttachAnAttachment'/>");</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.itemWriteOff'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Writeoff.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>