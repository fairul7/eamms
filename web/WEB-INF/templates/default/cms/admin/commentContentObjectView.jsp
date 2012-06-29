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
            <c:set var="comments" value="${co.publishedCommentList}" />
            <c:if test="${co.score > 0}">
                <fmt:message key='comment.label.rating'/> [<b><c:forEach begin="0" end="${co.score - 1}">*</c:forEach></b>]
            </c:if>
        </span>
        <p>
        <span style="font-size:8pt;">
            <c:out value="${co.summary}" escapeXml="false" />
        </span>
        <p>
        <span style="font-size:8pt;">
            <blockquote>
            <c:forEach var="comment" items="${comments}">
                <hr>
                <b><c:out value="${comment.name}" /></b> [<b><c:forEach begin="1" end="${comment.score}">*</c:forEach></b>]
                <br>
                <c:out value="${comment.submissionUser}" /> <fmt:formatDate value="${comment.date}"/>
                <br>
                <br>
                <c:out value="${comment.summary}" escapeXml="false" />
                <br>
            </c:forEach>
            </blockquote>
        </span>

        <br>
        <br>
        <br>
      </td>
    </tr>
  </tbody>
</table>

