<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.ItemForm" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/index.jsp"/>

<c-rt:set var="forward_succ" value="<%= ItemForm.FORWARD_EDIT_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= ItemForm.FORWARD_EDIT_FAIL %>"/>

<x:config>
	<page name="EditItem">
		<com.tms.fms.facility.ui.ItemForm name="form"/>
	</page>
</x:config>

<c:choose>
	<c:when test="${not empty(param.fid)}">
		<c:set var="facilityID" value="${param.fid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="facilityID" value="${widgets['ViewItem.form'].facilityId}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${not empty(param.rid)}">
		<c:set var="itemID" value="${param.rid}"/>
	</c:when>
	<c:otherwise>
		<c:set var="itemID" value="${widgets['ViewItem.form'].itemCode}"/>
	</c:otherwise>
</c:choose>


<c:choose>
	<c:when test="${not empty(param.mode)}">
		<c:set var="mode" value="${param.mode}"/>
	</c:when>
	<c:otherwise>
		<c:set var="mode" value="${widgets['EditItem.form'].mode}"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${forward.name == forward_succ}">
		<script>alert("Item updated!");document.location = "<c:url value="ViewItem.jsp?fid=${facilityID}&rif=${itemID}&mode=${mode}"/>";</script>
	</c:when>
	<c:when test="${forward.name == forward_fail}">
		<script>alert("Error! Fail to update item.");</script>
	</c:when>
</c:choose>

<x:set name="EditItem.form" property="mode" value="${mode}"/>
<x:set name="EditItem.form" property="itemCode" value="${itemID}"/>
<x:set name="EditItem.form" property="facilityId" value="${facilityID}"/>
<x:set name="EditItem.form" property="action" value="<%= ItemForm.FORM_ACTION_EDIT %>"/>
<x:set name="EditItem.form" property="cancelUrl" value="ViewItem.jsp?fid=${facilityID}&rif=${itemID}&mode=${mode}"/>
<x:set name="EditItem.form" property="whoModifyId" value="${sessionScope.currentUser.id}"/>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.editItemDetail'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="EditItem.form" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>