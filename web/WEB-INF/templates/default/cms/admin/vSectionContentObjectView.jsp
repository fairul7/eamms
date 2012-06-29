<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="vsection" scope="page" value="${widget}"/>
<c:set var="co" scope="page" value="${vsection.contentObject}"/>

<table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
  <tbody>
    <tr>
      <td>
        <span style="font-size:10pt; font-weight:bold">
            <c:out value="${co.name}"/>
        </span>
        <br>
        <span style="font-size:7pt; font-style:italic">
            <c:out value="${co.author}"/> <fmt:formatDate value="${co.date}"/>
        </span>
        <p>
        <span style="font-size:8pt;">
            <c:out value="${co.summary}" escapeXml="false" />
        </span>
        <p>
        <blockquote>
        <c:forEach items="${vsection.contentObjectList}" var="child">
            <hr>
            <b><c:out value="${child.name}"/></b>
            <br>
            <span style="font-size:7pt; font-style:italic">
                <c:out value="${child.author}"/> <fmt:formatDate value="${child.date}"/>
            </span>
            <br>
            <c:out value="${child.summary}" escapeXml="false" />
            <p>
        </c:forEach>
        </blockquote>
        <br>
        <br>
        <br>
      </td>
    </tr>
  </tbody>
</table>

