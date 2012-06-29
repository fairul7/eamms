<%@ page import="kacang.ui.Event"%>
<%@ include file="/common/header.jsp" %>

<table border=0 cellspacing=2 cellpadding=1>
<c:forEach var="co" items="${root[tree.childrenProperty]}" varStatus="coStatus">
    <c:set var="cookieName"><c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>
<tr>
    <td valign=top>
        <c:choose>
        <c:when test="${empty co[tree.childrenProperty] || empty co[tree.childrenProperty][0]}">
            <span class="contentCollapsed">-</span>
        </c:when>
        <c:when test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0] && cookieValue=='none'}">
            <a class="contentExpanded" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a class="contentExpanded" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
    <td>
        <c:set var="cssClass" value="aclspan"/>
        <c:if test="${co.id == co.aclId}"><c:set var="cssClass" value="aclspan2"/></c:if>
        <span class="<c:out value='${cssClass}'/>">
        <c:set var="acl" value="${aclMap[co.aclId]}"/>
            [ G:
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
            <c:if test="${co.id == co.aclId}">-</c:if>
            <c:if test="${co.id != co.aclId}">I</c:if>
            ]</span>

        <x:event name="${tree.absoluteName}" param="id=${co[tree.displayId]}" html="title=\"${co[tree.displayTitle]}\" class=\"option\"">
            <c:choose>
                <c:when test="${tree.selectedId == co[tree.displayId]}"><b><c:out value="${co[tree.displayProperty]}"/></b></c:when>
                <c:otherwise><c:out value="${co[tree.displayProperty]}"/></c:otherwise>
            </c:choose>
        </x:event>

        <x:config>
            <page name="aclContentTreeRecursion">
                <com.tms.cms.core.ui.AclContentForm name="form"/>
            </page>
        </x:config>

        <c:if test="${param.id == co.id}">
            <x:set name="aclContentTreeRecursion.form" property="key" value="${co.id}"/>
            <x:set name="aclContentTreeRecursion.form" property="principalId" value="${tree.principalId}"/>

            <x:display name="aclContentTreeRecursion.form" />

            <c:if test="${!tree.group}">
                <div class="aclspan3">
                <c:set var="roleGroups" value="${widgets['aclContentTreeRecursion.form'].roleGroups}"/>
                <c:forEach items="${roleGroups}" var="item">
                    <li>
                        <c:out value="${item.key}"/>:
                        <c:forEach items="${item.value}" var="group" varStatus="stat">
                            <c:if test="${stat.index > 0}">, </c:if>
                            <c:out value="${group.name}"/>
                        </c:forEach>
                    </li>
                </c:forEach>
                </div>
                <br><br>
            </c:if>
        </c:if>


        <c:if test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>" style="display: <c:out value="${cookieValue}"/>">
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:set var="root" scope="request" value="${co}"/>
                    <c:catch var="ie">
                        <jsp:include page="aclContentTreeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</c:forEach>
</table>


