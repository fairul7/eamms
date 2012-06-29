<%@ page import="kacang.ui.Event,
                 kacang.Application,
                 kacang.services.security.SecurityService,
                 com.tms.cms.core.model.ContentManager,
                 kacang.services.security.User,
                 java.util.Map,
                 com.tms.cms.section.Section"%>
<%@ include file="/common/header.jsp" %>


<c:set var="tree" scope="request" value="${widget}"/>
<c:set var="root" scope="request" value="${tree.model}"/>
<c:set var="aclMap" scope="request" value="${tree.aclMap}"/>

<link rel="stylesheet" href="<%= request.getContextPath() %>/common/tree/tree.css">

<script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js">
</script>

<style>
.aclspan { font-size:10px; color: red; font-weight: bold }
.aclspan2 { font-size:10px; color: red; background:yellow; font-weight: bold }
<%--.aclspan2 { font-size:10px; color: black; font-weight: normal }--%>
</style>

<table border=0 cellspacing=2 cellpadding=1>
<tr>
    <td valign=top>
        <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">
            <a style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:12px; line-height: 8px" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>">-</span></a>
        </c:if>
        <c:if test="${empty root[tree.childrenProperty] || empty root[tree.childrenProperty][0]}">
            <span style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 8px">-</span>
        </c:if>
    </td>
    <td>
<%--        <fmt:message key="cms.label.icon_${root.className}"></fmt:message>--%>

        <span class="aclspan2">
        <c:set var="acl" value="${aclMap[root.aclId]}"/>
            [G:
            <c:if test="${!tree.group}">
            <c:if test="${!empty acl.propertyMap.group_manager}">M</c:if>
            <c:if test="${empty acl.propertyMap.group_manager}">-</c:if>
            <c:if test="${!empty acl.propertyMap.group_editor}">E</c:if>
            <c:if test="${empty acl.propertyMap.group_editor}">-</c:if>
            <c:if test="${!empty acl.propertyMap.group_author}">A</c:if>
            <c:if test="${empty acl.propertyMap.group_author}">-</c:if>
            <c:if test="${!empty acl.propertyMap.group_reader}">R</c:if>
            <c:if test="${empty acl.propertyMap.group_reader}">-</c:if>
            | U:
            </c:if>
            <c:if test="${!empty acl.propertyMap.user_manager}">M</c:if>
            <c:if test="${empty acl.propertyMap.user_manager}">-</c:if>
            <c:if test="${!empty acl.propertyMap.user_editor}">E</c:if>
            <c:if test="${empty acl.propertyMap.user_editor}">-</c:if>
            <c:if test="${!empty acl.propertyMap.user_author}">A</c:if>
            <c:if test="${empty acl.propertyMap.user_author}">-</c:if>
            <c:if test="${!empty acl.propertyMap.user_reader}">R</c:if>
            <c:if test="${empty acl.propertyMap.user_reader}">-</c:if>
            |
            <c:if test="${root.id == root.aclId}">-</c:if>
            <c:if test="${root.id != root.aclId}">I</c:if>
            ]</span>

        <x:event name="${tree.absoluteName}" param="id=${root[tree.displayId]}" html="title=\"${root[tree.displayTitle]}\" class=\"option\"">
            <c:choose>
                <c:when test="${tree.selectedId == root[tree.displayId]}"><b><c:out value="${root[tree.displayProperty]}"/></b></c:when>
                <c:otherwise><c:out value="${root[tree.displayProperty]}"/></c:otherwise>
            </c:choose>
        </x:event>

<%--
        <span class="aclspan">
        <c:set var="acl" value="${aclMap[root.id]}"/>
        <c:choose>
        <c:when test="${!empty acl && (acl.propertyMap.aclUserRole || acl.propertyMap.aclGroupRole)}">
            <c:choose>
                <c:when test="${!empty acl.propertyMap.user_manager}">manager</c:when>
                <c:when test="${!empty acl.propertyMap.user_editor}">editor</c:when>
                <c:when test="${!empty acl.propertyMap.user_author}">author</c:when>
                <c:when test="${!empty acl.propertyMap.user_reader}">reader</c:when>
            </c:choose>
            <c:if test="${!empty acl.propertyMap.group_manager}">G_manager</c:if>
            <c:if test="${!empty acl.propertyMap.group_editor}">G_editor</c:if>
            <c:if test="${!empty acl.propertyMap.group_author}">G_author</c:if>
            <c:if test="${!empty acl.propertyMap.group_reader}">G_reader</c:if>
        </c:when>
        <c:otherwise>
            <c:if test="${root.id == root.aclId}">X</c:if>
        </c:otherwise>
        </c:choose>
        </span>
--%>

        <c:if test="${!empty root[tree.childrenProperty] && !empty root[tree.childrenProperty][0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>" style="display: block">
                <script>
                <!--
                    //treeLoad('<c:out value="${tree.name}"/><c:out value="${root[tree.displayId]}"/>');
                //-->
                </script>
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:catch var="ie">
                        <jsp:include page="aclContentTreeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</table>
