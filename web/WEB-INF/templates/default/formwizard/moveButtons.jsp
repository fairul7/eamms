<%@ page import="kacang.ui.Widget"%>
<%@ include file="/common/header.jsp" %>
<%
    Widget w = (Widget) request.getAttribute("widget");
    String prevTemplate = w.getTemplate();
    w.setTemplate(null);
%>
        <x:display name="${widget.absoluteName}" />
    </td>
</tr>

<tr>
    <td colspan="2">
        <x:event name="${widget.parent.parent.absoluteName}" type="up" param="childName=${widget.absoluteName}&formId=${param.formId}&formTemplateId=${param.formTemplateId}">Move Up</x:event> |
        <x:event name="${widget.parent.parent.absoluteName}" type="down" param="childName=${widget.absoluteName}&formId=${param.formId}&formTemplateId=${param.formTemplateId}">Move Down</x:event> |
<c:if test="${!empty param.formId}">
    <a href="frwEditField.jsp?childName=<c:out value="${widget.absoluteName}"/>&formId=<c:out value="${param.formId}"/>"/>Edit</a> |
    <x:event name="${widget.parent.parent.absoluteName}" type="remove" param="childName=${widget.absoluteName}&formId=${param.formId}">Remove</x:event>
</c:if>

<c:if test="${!empty param.formTemplateId}">
    <a href="frwEditTemplateField.jsp?childName=<c:out value="${widget.absoluteName}"/>&formTemplateId=<c:out value="${param.formTemplateId}"/>"/>Edit</a> |
    <x:event name="${widget.parent.parent.absoluteName}" type="remove" param="childName=${widget.absoluteName}&formTemplateId=${param.formTemplateId}}">Remove</x:event>
</c:if>

<%
    w.setTemplate(prevTemplate);
%>

