<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.FacilityTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<c-rt:set var="forward_listing_add" value="<%= FacilityTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= FacilityTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= FacilityTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<c:choose>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="AddFacility.jsp"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("Delete successfully.");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("Error! Fail to delete one or more facility.");</script>
	</c:when>
</c:choose>

<x:config>
	<page name="Facility">
		<com.tms.fms.facility.ui.FacilityTable name="table" width="100%"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<c:redirect url="ViewFacility.jsp?fid=${param.id}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.facilityListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Facility.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>