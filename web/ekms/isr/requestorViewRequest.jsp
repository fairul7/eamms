<%@page import="com.tms.collab.isr.permission.model.ISRGroup" %>
<%@include file="/common/header.jsp" %>

<x:config>
	<page name="isr">
		<com.tms.collab.isr.ui.RequestorRequestView name="requestorRequestView"/>
    </page>
</x:config>

<c:choose>
<c:when test="${!empty param.requestId }">
	<x:set name="isr.requestorRequestView" property="requestId" value="${param.requestId}" />
</c:when>
</c:choose>
<c:if test="${param.suggestedResolutionDesc eq 'true'}">
	<x:set name="isr.requestorRequestView" property="suggestedResolutionDesc" value="${true}" />
</c:if>
<c:if test="${empty param.suggestedResolutionDesc || param.suggestedResolutionDesc eq 'false'}">
	<x:set name="isr.requestorRequestView" property="suggestedResolutionDesc" value="${false}" />
</c:if>
<c:if test="${param.remarksDesc eq 'true'}">
	<x:set name="isr.requestorRequestView" property="remarksDesc" value="${true}" />
</c:if>
<c:if test="${empty param.remarksDesc || param.remarksDesc eq 'false'}">
	<x:set name="isr.requestorRequestView" property="remarksDesc" value="${false}" />
</c:if>
<c:if test="${param.clarificationDesc eq 'true'}">
	<x:set name="isr.requestorRequestView" property="clarificationDesc" value="${true}" />
</c:if>
<c:if test="${empty param.clarificationDesc || param.clarificationDesc eq 'false'}">
	<x:set name="isr.requestorRequestView" property="clarificationDesc" value="${false}" />
</c:if>

<c:set var="requestId" value="${widgets['isr.requestorRequestView'].requestId}" />

<c:choose>
<c:when test="${forward.name=='cancel_form_action'}">
    <c:redirect url="viewRequestListing.jsp"/>
</c:when>
</c:choose>

<%@include file="includes/isrCommon.jsp" %>
<%@include file="/ekms/includes/header.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%" valign="top">
	<tr>
		<td class="contentTitleFont" style="padding:5px;"><fmt:message key="isr.label.viewRequest"/></td>
	</tr>
	<tr>
		<td><x:display name="isr.requestorRequestView" /></td>
	</tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>