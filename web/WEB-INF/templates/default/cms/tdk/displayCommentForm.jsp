<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<%-- Display header --%>
<div class="contentHeader">
    <fmt:message key='comment.label.postComment'/>
</div>

<c:choose>
<c:when test="${param.action=='postComment' && !empty param.title && (!form.scoreRequired || !empty param.score)}">

    <p>
    <fmt:message key='comment.label.thankYou'/>.

</c:when>
<c:otherwise>

<form name="form1" method="post"<c:if test="${!empty form.url}"> action="<c:out value="${form.url}"/>"</c:if>>
  <table width="50%" cellpadding="2" cellspacing="0">
    <tr>
      <td valign="top"><fmt:message key='comment.label.title'/></td>
      <td>
        <input type="text" name="title" value="<c:out value="${form.title}"/>">
        <font color="#FF0000">
            <strong>*</strong>
            <c:if test="${(param.action == 'postComment') && (empty form.title)}"> <fmt:message key='comment.label.required'/> </c:if>
        </font>
      </td>
    </tr>
    <tr>
      <td valign="top"><fmt:message key='comment.label.rating'/></td>
      <td><p>
        <c:forEach var="count" begin="1" end="5">
           <input type="radio" name="score" value="<c:out value="${count}"/>"<c:if test="${count == form.score}"> selected</c:if>>
           <c:out value="${count}"/>
        </c:forEach>
        <c:if test="${form.scoreRequired}">
        <font color="#FF0000">
            <strong>*</strong>
            <c:if test="${(param.action == 'postComment') && (empty form.score)}"> <fmt:message key='comment.label.required'/> </c:if>
        </c:if>
        <br>
        </p>
      </td>
    </tr>
    <tr>
      <td valign="top"><fmt:message key='comment.label.comment'/></td>
      <td><textarea name="comment" cols="40" rows="10" id="comment"><c:out value="${form.comment}"/></textarea></td>
    </tr>
    <tr>
      <td valign="top">&nbsp;</td>
      <td>
        <input type="submit" class="button" value="<fmt:message key='comment.label.submit'/>">
        <input name="action" type="hidden" value="postComment">
        <input name="id" type="hidden" value="<c:out value="${widget.id}"/>">
        <input name="commentId" type="hidden" value="<c:out value="${widget.commentId}"/>">
      </td>
    </tr>
  </table>

</form>

</c:otherwise>
</c:choose>

<p>
<a class="contentOption" href="content.jsp?id=<c:out value='${form.commentary.parentId}'/>"><fmt:message key='comment.label.back'/></a>
