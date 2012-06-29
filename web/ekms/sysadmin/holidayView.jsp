<%@ page import="kacang.ui.WidgetManager, com.tms.ekms.manpowertemp.model.SetHolidayLeaveObject, com.tms.ekms.manpowertemp.ui.HolidayTable" %>
<%@ include file="/common/header.jsp" %>

<x:config>
<page name="holidayTable">
    <com.tms.ekms.manpowertemp.ui.HolidayTable name="holidaytable" width="100%"/>
</page>
</x:config>


<c-rt:set var="forward_listing_add" value="<%= HolidayTable.FORWARD_LISTING_ADD %>"/>
<c-rt:set var="forward_listing_delete" value="<%= HolidayTable.FORWARD_LISTING_DELETE %>"/>

<c:choose>
<c:when test="${forward.name == forward_listing_add}">
<script>document.location = "<c:redirect url="holidayLeaveSetup.jsp?page=holiday"/>";</script>
</c:when>
<c:when test="${forward.name == forward_listing_delete}">
<script>alert("Delete successfully.");</script>
</c:when>
</c:choose>

<c:if test="${!empty param.id}">
<c:redirect url="holidayLeaveSetup.jsp?action=edit&id=${param.id}"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
<tr valign="middle">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.manpower.table.holidayView'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
</tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="holidayTable.holidaytable" ></x:display>

</td></tr>
<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>