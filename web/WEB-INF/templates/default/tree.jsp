<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="tree" scope="request" value="${widget}"/>
<c:set var="root" scope="request" value="${tree.model}"/>

<script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js">
</script>

<table border=0 cellspacing=2 cellpadding=1>
<tr>
    <td valign=top>
        <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">
            <a style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:16px; line-height: 6px" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>">+</span></a>
        </c:if>
        <c:if test="${empty root[tree.childrenProperty] || empty root[tree.childrenProperty][0]}">
            <span style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 6px">-</span>
        </c:if>
    </td>
    <td>
        <x:event name="${tree.absoluteName}" param="id=${root[tree.displayId]}" html="title=\"${root[tree.displayTitle]}\"">
            <c:choose>
                <c:when test="${tree.selectedId == root[tree.displayId]}"><b><c:out value="${root[tree.displayProperty]}"/></b></c:when>
                <c:otherwise><c:out value="${root[tree.displayProperty]}"/></c:otherwise>
            </c:choose>
        </x:event>
        <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>" style="display: block">
                <script>
                <!--
                    //treeLoad('<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>');
                //-->
                </script>
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:catch var="ie">
                        <jsp:include page="treeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</table>


