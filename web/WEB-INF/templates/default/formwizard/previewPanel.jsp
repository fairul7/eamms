<%--
<%@ page import="kacang.ui.Widget,
                 kacang.stdui.Label,
                 com.tms.cms.formwizard.model.FormConstants,
                 java.util.Collection,
                 java.util.Iterator,
                 com.tms.cms.formwizard.grid.G2Field"%>
<%@ include file="/common/header.jsp" %>
<%
    Widget widget = (Widget) request.getAttribute("widget");
    String prevTemplate = widget.getTemplate();
    widget.setTemplate(null);
    Widget childWidget = null;

%>

    <x:display name="${widget.absoluteName}" body="custom">

        <c:set var="panel" value="${widget}"/>
        <c:choose>
            <c:when test="${panel.columns == 0}">
                <c:forEach var="child" items="${panel.children}">
                    <x:display name="${child.absoluteName}"/>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:set var="column" value="0"/>
                <c:set var="row" value="0"/>
                <c:forEach var="child" items="${panel.children}">
                <%

                    childWidget = (Widget)pageContext.findAttribute("child");
                    if ( childWidget instanceof Label && childWidget.getAbsoluteName().endsWith(FormConstants.FIELD_TEXT_BLOCK_SUFFIX) ||
                         childWidget instanceof G2Field) {                    
                %>

                        <td valign="top" colspan = "2"><x:display name="${child.absoluteName}"/></td>

                            </tr>
                            <tr>
                            <c:set var="column" value="0"/>
                            <c:set var="row" value="${row + 1}"/>



                <%
                    }
                    else  {
                %>
                        <td valign="top"><x:display name="${child.absoluteName}"/></td>
                        <c:set var="column" value="${column + 1}"/>
                        <c:if test="${column ge panel.columns}">
                        </tr>
                        <tr>
                        <c:set var="column" value="0"/>
                        <c:set var="row" value="${row + 1}"/>
                        </c:if>
                <%
                    }
                %>

                </c:forEach>
                </tr>
            </c:otherwise>
        </c:choose>
    </x:display>

<c:if test="${!empty panel.children}">   
    <tr>
        <td colspan="2">
    		<x:event name="${widget.parent.parent.absoluteName}" type="up" param="childName=${widget.absoluteName}&formId=${param.formId}">Move Up</x:event> |
    		<x:event name="${widget.parent.parent.absoluteName}" type="down" param="childName=${widget.absoluteName}&formId=${param.formId}">Move Down</x:event> |
    		<x:event name="${widget.parent.parent.absoluteName}" type="remove" param="childName=${widget.absoluteName}&formId=${param.formId}">Remove</x:event>
        </td>

</c:if>
<%
    widget.setTemplate(prevTemplate);
%>
--%>
