<%@ page import="kacang.ui.WidgetManager, com.tms.fms.facility.ui.CategoryTable" %>
<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.admin" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<c-rt:set var="forward_listing_add" value="<%= CategoryTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= CategoryTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_delete_fail" value="<%= CategoryTable.FORWARD_LISTING_DELETE_FAIL %>"/>

<c:choose>
	<c:when test="${forward.name == forward_listing_add}">
		<c:redirect url="AddCategory.jsp"/>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete}">
		<script>alert("<fmt:message key='fms.facility.msg.recordDeleted'/>");</script>
	</c:when>
	<c:when test="${forward.name == forward_listing_delete_fail}">
		<script>alert("<fmt:message key='fms.facility.msg.failToDeleteOneOrMoreRecord'/>");</script>
	</c:when>
</c:choose>

<x:config>
	<page name="Category">
		<com.tms.fms.facility.ui.CategoryTable name="table" width="100%"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<c:redirect url="EditCategory.jsp?cid=${param.id}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.facility.title.categorySetupListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="Category.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>