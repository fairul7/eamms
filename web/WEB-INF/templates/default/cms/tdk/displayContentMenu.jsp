<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="tree" scope="request" value="${widget}"/>
<c:set var="root" scope="request" value="${tree.root}"/>

<script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js">
</script>

<%-- Display Edit Mode Options --%>
<x:template type="TemplateDisplayFrontEndEdit" properties="id=${root.id}" />

<table border=0 cellspacing=2 cellpadding=1>
<tr>
    <td valign=top>
        <c:if test="${!empty root.children && !empty root.children[0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${root.id}"/>" style="display: block">
                <script>
                <!--
                    //treeLoad('<c:out value="${tree.name}"/><c:out value="${root.id}"/>');
                //-->
                </script>
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:catch var="ie">
                        <jsp:include page="displayContentTreeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</table>


