<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page import="kacang.Application,
                 kacang.ui.WidgetManager,
                 bsh.Interpreter,
                 kacang.runtime.filter.RuntimeFilter"
%>
<html>
<head>
    <title>debug</title>
    <script language="Javascript" src="tree.js"></script>
    <style>
    <!--
.map {
    font-family: Verdana;
    font-size: 8pt;
    border: 1px solid silver;
}

body
{
  font-family:Arial, Verdana, Helvetica, sans-serif;
  font-size:12px;
  background-color:#ffffff;
}

td
{
  font-family:Arial, Verdana, Helvetica, sans-serif;
  font-size:12px;
}

a
{
  font-family:Arial, Verdana, Helvetica, sans-serif;
  font-size:12px;
}

a:link, a:visited
{
  text-decoration: none;
}

a:hover
{
  text-decoration: underline;
}

    //-->
    </style>
</head>
<body>

<h3>
<img src="logo.gif">
Debug Page
<hr size="1">
</h3>

<p>
<table border="0" cellspacing="0" cellpadding="5">
    <tr>
        <td class="map">
            JVM Free Memory
            <br>
            [<a href="#" onclick="window.open('debug_system.jsp','debug_system','width=640,height=480,status=yes,scrollbars=yes');return false">system properties</a>]
        </td>
        <td class="map">
            <%= (Runtime.getRuntime().freeMemory() / 1000) %> / <%= (Runtime.getRuntime().totalMemory() / 1000) %> K
        </td>
    </tr>
</table>
</p>

<%
    WidgetManager widgetManager = WidgetManager.getWidgetManager(request);
    pageContext.setAttribute("widgetManager", widgetManager);
    Interpreter interpreter = new Interpreter();
    interpreter.setClassLoader(getClass().getClassLoader());
    interpreter.set("application", Application.getInstance());
    interpreter.set("wm", widgetManager);
%>

<%
    String code = request.getParameter("code");
    if (code != null && code.trim().length() > 0) {
        try {
            out.println(interpreter.eval(code));
        }
        catch(Exception e) {
            out.println(e.toString());
        }
    }
%>

<form>
    <textarea name="code" rows="5" cols="50"><c:out value="${param.code}"/></textarea>
    <input type="submit" class="button" value="Evaluate">
</form>

<div style="float: left">
<table border="0" cellspacing="0" cellpadding="5" width="100%">
<c:forEach var="pageEntry" items="${widgetManager.pageMap}">
    <c:set var="widget" scope="request" value="${widgetManager.widgetMap[pageEntry.value]}"/>
    <tr>
        <td style="background: silver; border: 1px solid gray; font:bold 10pt Arial">
            <a target="debug_details" href="debug_details.jsp?name=<c:out value="${widget.absoluteName}"/>">
                <c:out value="${widget.name}"/></a>
            [<c:out value="${widget.url}"/>]
        </td>
    </tr>
    <tr>
        <td>
            <jsp:include page="tree.jsp" flush="true"/>
        </td>
    </tr>
</c:forEach>
</table>
</div>

</body>
</html>


