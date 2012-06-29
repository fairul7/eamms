<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.StatusTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.transport.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
    <page name="Status">
    	<com.tms.fms.facility.ui.StatusTable name="table" width="100%"/>
    </page>
</x:config>

<c-rt:set var="forward_listing_delete" value="<%= StatusTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= StatusTable.FORWARD_LISTING_DELETE_FAIL %>"/>

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
		<c:set var="mode" value="${widgets['Status.table'].mode}"/>
	</c:otherwise>
</c:choose>

<x:set name="Status.table" property="mode" value="${mode}"/>
<x:set name="Status.table" property="item_barcode" value="${itemID}"/>
<x:set name="Status.table" property="facilityId" value="${facilityID}"/>
<x:set name="Status.table" property="cancelUrl" value="ViewItem.jsp?fid=${facilityID}&rif=${itemID}&mode=${mode}"/>

<c:choose>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("Delete successfully");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("Error! Fail to delete one or more record.");</script>
	</c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.inactiveStatusLogs'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Status.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>