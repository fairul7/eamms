<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<table cellpadding="5" cellspacing="0" width="100%">
    <c:if test="${!widget.noHeader}">
        <tr><td class="contentHeader"><span class="contentName"><c:out value="${widget.currentObject.name}"/></span></td></tr>
    </c:if>
    <c:if test="${!empty widget.subsections}">
        <tr>
            <td class="contentBgColor">
                <table cellpadding="5" cellspacing="0">
                    <c:set var="firstColumn" value="true"/>
                    <c:forEach var="subsection" items="${widget.subsections}">
                        <c:if test="${firstColumn}"><tr></c:if>
                        <td class="contentBgColor" width="50%">
                            <li><c:if test="${subsection.propertyMap.orphan}">*</c:if>
                                <a href="content.jsp?id=<c:out value="${subsection.id}"/>"><c:out value="${subsection.name}"/></a>
                                <c:if test="${widget.showCount && !empty subsection.propertyMap.childCount}">
                                    <span class="articleSmall">(<span class="articleRead"><c:out value="${subsection.propertyMap.childCount}"/></span>)</span>
                                </c:if>
                            </li>
                        </td>
                        <c:choose>
                            <c:when test="${!firstColumn}">
                                </tr>
                                <c:set var="firstColumn" value="true"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="firstColumn" value="false"/>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </c:if>
</table>
