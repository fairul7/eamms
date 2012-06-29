<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<table border=0 cellspacing=2 cellpadding=1>
<c:forEach var="co" items="${root[tree.childrenProperty]}" varStatus="coStatus">
<tr>
    <td valign=top>
        <c:if test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0]}">
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">+</span></a>
        </c:if>
        <c:if test="${empty co[tree.childrenProperty] || empty co[tree.childrenProperty][0]}">
            <span class="contentTreeNode">-</span>
        </c:if>
    </td>
    <td>
        <x:event name="${tree.absoluteName}" param="id=${co[tree.displayId]}&companyId=" html="title=\"${co[tree.displayTitle]}\"">
            <span class="contentTreeLinkLite"><c:out value="${co[tree.displayProperty]}"/></span>
        </x:event>
        <c:if test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>" style="display: block">
                <script>
                <!--
                    treeLoad('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>');
                //-->
                </script>
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:set var="root" scope="request" value="${co}"/>
                    <c:catch var="ie">
                        <jsp:include page="folderTreeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</c:forEach>
</table>


