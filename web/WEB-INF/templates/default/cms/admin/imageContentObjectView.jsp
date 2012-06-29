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
        <p>
        <c:choose>
            <c:when test="${co.contentType=='image/gif' || co.contentType=='image/jpeg' || co.contentType=='image/png'}">
                <img src="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>">
            </c:when>
            <c:otherwise>
                <span style="font-size:8pt;">
                    <a target="_blank" href="<%= request.getContextPath() %>/storage<c:out value="${co.filePath}"/>"><c:out value="${co.fileName}"/></a>
                    <br>
                    <c:out value="${co.contentType}"/>
                    <br>
                    <c:out value="${co.fileSize}"/> <fmt:message key='general.label.bytes'/>
                </span>
            </c:otherwise>
        </c:choose>

        <br>
        <br>
        <br>
      </td>
    </tr>
  </tbody>
</table>

