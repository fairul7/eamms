<%@ page import="kacang.runtime.*, kacang.ui.*,
                 kacang.stdui.TabbedPanel" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="tabbedpanel" value="${widget}"/>
<table cellspacing="0" cellpadding="0" width="<c:out value="${tabbedpanel.width}"/>">
    <tr>
        <td>
            <table border="0" cellspacing="0">
                <tr>
                    <c:forEach items="${tabbedpanel.panels}" var="pane">
                        <c:choose>
                            <c:when test="${pane.absoluteName == tabbedpanel.selectedName}">
                                <td class="panelHeaderSelected">
                                    <x:event name="${tabbedpanel.absoluteName}" param="sc=${pane.absoluteName}">
                                        <span class="panelHeaderSelectedText"><c:out value="${pane.text}"/></span>
                                    </x:event>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="panelHeader">
                                    <x:event name="${tabbedpanel.absoluteName}" param="sc=${pane.absoluteName}">
                                        <span class="panelHeaderText"><c:out value="${pane.text}"/></span>
                                    </x:event>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <td style="width:2px">
                        </td>
                    </c:forEach>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="panelRow">



