<%@ include file="/common/header.jsp"  %>
<c:if test="${widget.invalid}">
	<div style="border:1px solid #de123e">
</c:if>
<c:set var="select" value="${widget.name}_select"/>
<c:set var="text" value="${widget.name}_text"/>
<table cellpadding="0" cellspacing="0">
	<tr><td><x:display name="${widget.childMap[select].absoluteName}"/></td></tr>
	<tr><td><fmt:message key="helpdesk.label.newOption"/><x:display name="${widget.childMap[text].absoluteName}"/></td></tr>
</table>
<c:if test="${widget.invalid}">
	</div>
</c:if>