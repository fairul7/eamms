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
                <table cellpadding="5" cellspacing="0" width="90%">
                    <c:set var="firstColumn" value="true"/>
                    <c:forEach var="subsection" items="${widget.subsections}">
                        <c:if test="${firstColumn}"><tr></c:if>
                        <td class="contentBgColor" width="10">
                            <img src="images/ic_folder.gif" border="0">
                        </td>
                        <td class="contentBgColor">
                            <c:if test="${subsection.propertyMap.orphan}">*</c:if>
                            <a class="categorylink" href="content.jsp?id=<c:out value="${subsection.id}"/>"><c:out value="${subsection.name}"/></a>
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
