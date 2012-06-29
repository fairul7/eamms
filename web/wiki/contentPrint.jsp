<%@ include file="/common/header.jsp" %>
<%@ include file="includes/security.jsp" %>

<x:template type="TemplateDisplaySetup" name="setup"/>

<html>
<head>
<title><c:out value='${setup.propertyMap.siteName}'/></title>
<link rel="stylesheet" href="<c:out value='${setup.propertyMap.siteStyleSheet}'/>">
</head>

<body>

<pre><i>Printing Instructions</i>: Select <b>File</b> and then <b>Print</b> from your browser's menu
--- Article Information ---
This article was printed from <a href="<c:out value='${setup.propertyMap.siteUrl}'/>"><c:out value='${setup.propertyMap.siteUrl}'/></a>.
Article's URL: <a href="content.jsp?id=<c:out value='${param.id}'/>"><c:out value='${setup.propertyMap.siteUrl}'/>/cms/content.jsp?id=<c:out value='${param.id}'/></a>
---------------------------
</pre>


<%-- TDK: Content --%>
<x:template
    name="article"
    type="TemplateDisplayContent"
    properties="id=${param.id}" />


<pre>--- end ---</pre>
</body>
</html>