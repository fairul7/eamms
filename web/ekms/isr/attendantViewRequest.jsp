<%@page import="com.tms.collab.isr.permission.model.ISRGroup" %>
<%@include file="/common/header.jsp" %>

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.AttendantViewRequest name="attendantViewRequest"/>
    </page>
</x:config>

<c:choose>
<c:when test="${!empty param.requestId }">
	<x:set name="isr.attendantViewRequest" property="requestId" value="${param.requestId}" />
</c:when>
</c:choose>
<c:if test="${param.clarificationDesc eq 'true'}">
	<x:set name="isr.attendantViewRequest" property="clarificationDesc" value="${true}" />
</c:if>
<c:if test="${empty param.clarificationDesc || param.clarificationDesc eq 'false'}">
	<x:set name="isr.attendantViewRequest" property="clarificationDesc" value="${false}" />
</c:if>
<c:if test="${param.assignmentRemarksDesc eq 'true'}">
	<x:set name="isr.attendantViewRequest" property="assignmentRemarksDesc" value="${true}" />
</c:if>
<c:if test="${empty param.assignmentRemarksDesc || param.assignmentRemarksDesc eq 'false'}">
	<x:set name="isr.attendantViewRequest" property="assignmentRemarksDesc" value="${false}" />
</c:if>
<c:if test="${param.suggestedResolutionDesc eq 'true'}">
	<x:set name="isr.attendantViewRequest" property="suggestedResolutionDesc" value="${true}" />
</c:if>
<c:if test="${empty param.suggestedResolutionDesc || param.suggestedResolutionDesc eq 'false'}">
	<x:set name="isr.attendantViewRequest" property="suggestedResolutionDesc" value="${false}" />
</c:if>

<c:choose>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="viewRequestListing.jsp"/>
</c:when>
</c:choose>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.permission.viewProcessRequest"/></td>
	</tr>
	<tr>
		<td><x:display name="isr.attendantViewRequest" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>