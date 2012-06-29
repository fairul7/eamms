<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td>
        <span style="font-size:10pt; font-weight:bold">
        <c:choose>
            <c:when test="${empty co.target}">
                 <a href="<c:out value="${co.url}"/>"><c:out value="${co.name}" escapeXml="false" /></a>
            </c:when>
            <c:otherwise>
                 <a target="<c:out value="${co.target}"/>" href="<c:out value="${co.url}"/>"><c:out value="${co.name}" escapeXml="false" /></a>
            </c:otherwise>
        </c:choose>
        </span>
        <p>
        <span style="font-size:8pt;">
            <c:out value="${co.summary}" escapeXml="false" />
        </span>
        <p>

        <br>
        <br>
        <br>
      </td>
    </tr>
  </tbody>
</table>

