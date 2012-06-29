<%@ page import="kacang.ui.WidgetManager, com.tms.fms.setup.model.ProgramObject, com.tms.fms.setup.ui.ProgramManagementTable" %>
<%@ include file="/common/header.jsp" %>

<x:config>
<page name="programTable">
    <com.tms.fms.setup.ui.ProgramManagementTable name="table" width="100%"/>
</page>
</x:config>

<c-rt:set var="forward_listing_add" value="<%= ProgramManagementTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= ProgramManagementTable.FORWARD_LISTING_DELETE %>"/>
<c-rt:set var="forward_listing_inactive" value="<%= ProgramManagementTable.FORWARD_LISTING_INACTIVE %>"/>

<c:choose>
<c:when test="${forward.name == forward_listing_add}">
<script>document.location = "<c:redirect url="progManagement.jsp"/>";</script>
</c:when>
<c:when test="${forward.name == forward_listing_delete}">
<script>alert("Delete successfully.");</script>
</c:when>
<c:when test="${forward.name == forward_listing_inactive}">
<script>alert("Status Inactive.");</script>
</c:when>
</c:choose>

<c:if test="${!empty param.programId}">
<c:redirect url="progManagement.jsp?action=edit&programId=${param.programId}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.setup.table.programListing'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="programTable.table" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>