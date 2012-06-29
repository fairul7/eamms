<%@ page import="kacang.runtime.*, kacang.ui.*,
                 kacang.stdui.TabbedPanel" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="tabbedpanel" value="${widget}"/>

<table cellspacing="0" cellpadding="0" width="<c:out value="${tabbedpanel.width}"/>">
<tr>
<td style="padding:0">

    <table border="0" cellspacing="0">
        <tr>

<c:forEach items="${tabbedpanel.panels}" var="pane">
<c:choose>
    <c:when test="${pane.absoluteName == tabbedpanel.selectedName}">
        <td style="border:1px ridge silver; margin:4; padding:8; background: #336699; font-weight: bold">
        <x:event name="${tabbedpanel.absoluteName}" param="sc=${pane.absoluteName}" html="style=\"color:white\"">
            <c:out value="${pane.text}"/>
        </x:event>
        </td>
    </c:when>
    <c:otherwise>
        <td style="border:1px ridge #dddddd; margin:4; padding:8; background: #dddddd">
        <x:event name="${tabbedpanel.absoluteName}" param="sc=${pane.absoluteName}" html="style=\"color:blue\"">
            <c:out value="${pane.text}"/>
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
<td style="border: 1px ridge silver; padding:2; margin:0">



