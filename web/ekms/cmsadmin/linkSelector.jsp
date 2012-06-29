<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:config>
    <page name="linkSelector">
         <com.tms.cms.core.ui.ContentListTable name="contentTable" customSelection="true" showPreview="true" sort="date" desc="true" />
    </page>
</x:config>

<html>
<head>
<style>
td {
    font-family: Arial;
    font-size: 8pt;
    color: black;
}
</style>
<script language="javascript">
<!--
function select_url(url) {
        window.returnValue = url;
        window.close();
}
//-->
</script>
</head>
<body>

<c:choose>
    <c:when test="${!empty param.url}">
        <script>
        <!--
            select_url("<c:out value='${param.url}'/>");
        //-->
        </script>
    </c:when>
    <c:when test="${empty param.preview && !empty param.id}">
        <script>
        <!--
            select_url("content.jsp?id=<c:out value='${param.id}'/>");
        //-->
        </script>
    </c:when>
    <c:otherwise>
        <c:if test="${param.preview && !empty param.id}">
            <script>
            <!--
                <%
                    String url = response.encodeRedirectURL("/cmspreview/content.jsp");
                %>
                window.open('<%= url %>?id=<c:out value="${param.id}"/>', 'linkPreview');
//                window.open('<c:url value="/cmspreview/content.jsp"/>?id=<c:out value="${param.id}"/>', 'linkPreview');
            //-->
            </script>
        </c:if>
       <form>
          <fmt:message key='general.label.url'/> <input name="url" type="text" size="50" value="http://">
          <input type="submit" class="button" value="<fmt:message key='general.label.done'/>">
       </form>
       <x:display name="linkSelector.contentTable" />
    </c:otherwise>
</c:choose>

</body>
</html>


