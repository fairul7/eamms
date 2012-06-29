<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<table border=0 cellspacing=2 cellpadding=1>
    <c:set var="cookieName"><c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/></c:set>
    <c:set var="cookieValue"><c:choose><c:when test="${cookie[cookieName].value == 'block'}">block</c:when><c:otherwise>none</c:otherwise></c:choose></c:set>
<c:forEach var="co" items="${root[tree.childrenProperty]}" varStatus="coStatus">
<tr>
    <td valign=top>
        <c:choose>
        <c:when test="${empty co[tree.childrenProperty] || empty co[tree.childrenProperty][0]}">
            <span style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 6px">-</span>
        </c:when>
        <c:when test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0] && cookieValue=='none'}">
            <a style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:16px; line-height: 6px" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">+</span></a>
        </c:when>
        <c:otherwise>
            <a style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 6px" href="#" onclick="treeToggle('<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>'); return false"><span id="icon_<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>">-</span></a>
        </c:otherwise>
        </c:choose>
    </td>
    <td>
        <fmt:message key="cms.label.icon_${co.className}"></fmt:message>

        <c:if test="${co.propertyMap.orphan}">*</c:if>
        <c:choose>
            <c:when test="${co.className == 'com.tms.cms.image.Image'}">
                <c:set var="image" value="${co.contentObject}"/>
                <input TYPE="radio" onclick='return preview("<c:out value="${pageContext.request.contextPath}/cms/storage${image.filePath}"/>")' value="<%=request.getContextPath()%>/cms/storage<c:out value="${image.filePath}"/>" name="choice" title="<c:out value="${co.summary}"/>">
                <c:out value="${co[tree.displayProperty]}"/>
            </c:when>
            <c:otherwise>
                <c:out value="${co[tree.displayProperty]}"/>
            </c:otherwise>
        </c:choose>

        <c:if test="${!empty co[tree.childrenProperty] && !empty co[tree.childrenProperty][0]}">
            <span id="<c:out value="${tree.name}"/><c:out value="${co[tree.displayId]}"/>" style="display: <c:out value="${cookieValue}"/>">
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:set var="root" scope="request" value="${co}"/>
                    <c:catch var="ie">
                        <jsp:include page="imageSelectorTreeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</c:forEach>
</table>


