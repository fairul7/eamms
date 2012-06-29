<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page import="kacang.Application,
                 kacang.ui.WidgetManager,
                 java.util.TreeMap"
%>
<html>
<head>
    <title>debug_system</title>
    <style>
    <!--
    .map {
        font-family: Verdana;
        font-size: 8pt;
        border: 1px solid silver;
    }
    //-->
    </style>
</head>
<body onload="focus()">

<div>
<table border="0" cellspacing="0" cellpadding="5">
    <tr>
        <td class="map">
            JVM Memory Usage
        </td>
        <td class="map">
            <%= (Runtime.getRuntime().freeMemory() / 1000) %> / <%= (Runtime.getRuntime().totalMemory() / 1000) %> K
        </td>
    </tr>
<%
    pageContext.setAttribute("sysprops", new TreeMap(System.getProperties()));
%>
<c:forEach var="prop" items="${sysprops}">
    <tr>
        <td class="map">
            <c:out value="${prop.key}"/>
        </td>
        <td class="map">
            <c:out value="${prop.value}"/>
        </td>
    </tr>
</c:forEach>
</table>
</div>

</body>
</html>


