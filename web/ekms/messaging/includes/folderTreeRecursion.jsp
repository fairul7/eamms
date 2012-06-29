<%@ page import="kacang.ui.Event,
                 com.tms.collab.messaging.model.Util,
                 com.tms.collab.messaging.model.Folder"%>
<%@ include file="/common/header.jsp" %>

<c-rt:set var="sentFolderName" value="<%= Folder.FOLDER_SENT %>"/>
<c-rt:set var="trashFolderName" value="<%= Folder.FOLDER_TRASH %>"/>
<table border=0 cellspacing=2 cellpadding=1>
<c:forEach var="co" items="${root[tree.childrenProperty]}" varStatus="coStatus">
    <c:set var="cookieName"><c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>
<tr>
<c:choose>
<c:when test="${co.specialFolder}">
    <c:set var="tdStyle" value="border-bottom: dotted 1px gray"/>
    <td style="<c:out value="${tdStyle}"/>" valign=top style="padding-top:5px; padding-bottom:5px">
        <img src="<c:url value="/ekms/images/ic_folderop.gif"/>" width="18" height="14">
    </td>
</c:when>
<c:otherwise>
    <c:set var="tdStyle" value=""/>
    <td style="<c:out value="${tdStyle}"/>" valign=top style="padding-top:5px; padding-bottom:5px">
        <c:choose>
        <c:when test="${empty co[tree.childrenProperty] || empty co[tree.childrenProperty][0]}">
            <span class="contentTreeNode">-</span>
        </c:when>
        <c:when test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0] && cookieValue=='none'}">
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a class="contentTreeNode" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
</c:otherwise>
</c:choose>
    <td style="<c:out value="${tdStyle}"/>">
        <c:set var="dropFolderList" scope="request"><c:out value="${dropFolderList},\"folder_${co[tree.displayId]}\"+NO_DRAG" escapeXml="false" /></c:set>
        <c:set var="isSentFolder" value="${co.specialFolder && co.name == sentFolderName}"/>
        <c:set var="isTrashFolder" value="${co.specialFolder && co.name == trashFolderName}"/>
        <c:if test="${!isSentFolder}">
            <c-rt:set var="unreadMessages"><%= Util.countFolderUnreadMailsByFolderId(request, ((Folder)pageContext.getAttribute("co")).getFolderId()) %></c-rt:set>
        </c:if>
        <c:choose><c:when test="${co.specialFolder}"><c:set var="folderParam" value="folderId=${co.folderId}&folder=${co.name}"/></c:when><c:otherwise><c:set var="folderParam" value="folderId=${co.folderId}"/></c:otherwise></c:choose>
        <div id="folder_<c:out value="${co[tree.displayId]}"/>" style="padding-top:5px; padding-bottom:5px;">
            <x:event name="${tree.absoluteName}" param="${folderParam}" html="title=\"${co[tree.displayTitle]}\" class=\"folderlink\"">
                <c:if test="${unreadMessages > 0}"><b></c:if>
                <c:choose>
                    <c:when test="${co.specialFolder}">
                        <c:out value="${co[tree.displayProperty1]}"/>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${co[tree.displayProperty]}"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${unreadMessages > 0}"></b></c:if> <c:if test="${!isSentFolder}">(<c:out value="${unreadMessages}"/>)</c:if></x:event>
                <c:if test="${isTrashFolder}">&nbsp;[<a class="folderMenuLink" href="emptyTrash.jsp" onclick="if(confirm('<fmt:message key='messaging.label.confirmEmptyTrash'/>')) return true; else return false;"><fmt:message key='messaging.label.empty'/></a>]</c:if>
        </div>
        <c:if test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>" style="display: <c:out value="${cookieValue}"/>">
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

