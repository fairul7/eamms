<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="co" value="${widget.contentObject}"/>
<table cellpadding="3" cellspacing="0" width="100%">
    <c:if test="${!widget.noHeader}">
        <tr><td class="contentHeader"><span class="contentName"><c:out value="${co.name}" escapeXml="false" /></span></td></tr>
        <tr><td class="contentBody">&nbsp;</td></tr>
    </c:if>
    <tr><td class="contentBody"><c:out value="${co.summary}" escapeXml="false" /></td></tr>
    <tr>
        <td class="contentBody" align="center">
            <table cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td>
                        <c:set var="children" value="${co.contentObjectList}"/>
                        <c:forEach var="child" items="${children}">
                            <a href="content.jsp?id=<c:out value="${child.id}"/>" class="contentChildName"><c:out value="${child.name}"/></a><br>
                            <span class="contentChildDate"><fmt:formatDate pattern="${globalDateLong}" value="${child.date}"/></span><br>
                            <span class="contentChildAuthor"><c:out value="${child.author}"/></span>
                            <p><span class="contentChildSummary"><c:out value="${child.summary}" escapeXml="false" /></span></p>
                        </c:forEach>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
</table>
