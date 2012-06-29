<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="co" scope="request" value="${widget.contentObject}"/>

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
        <hr size="1">
        <p>
        <span style="font-size:8pt;">
            <c:out value="${co.contents}" escapeXml="false" />
        </span>
        <br>
        <br>
        <br>
      </td>
    </tr>
  </tbody>
</table>

