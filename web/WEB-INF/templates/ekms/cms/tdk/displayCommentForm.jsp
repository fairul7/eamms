<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<%-- Display header --%>
<c:choose>
    <c:when test="${param.action=='postComment' && !empty param.title && (!form.scoreRequired || !empty param.score)}">
        <table width="100%" cellpadding="3" cellspacing="0">
            <tr><td colspan="2" class="contentTitleFont"><fmt:message key='comment.label.postComment'/></td></tr>
            <tr><td colspan="2" class="contentBody">&nbsp;</td></tr>
            <tr><td colspan="2" class="contentBody"><fmt:message key='comment.label.thankYou'/>.</td></tr>
            <tr><td colspan="2" class="contentBody">&nbsp;</td></tr>
        </table>
    </c:when>
    <c:otherwise>
        <form name="form1" method="post"<c:if test="${!empty form.url}"> action="<c:out value="${form.url}"/>"</c:if>>
            <table width="100%" cellpadding="3" cellspacing="0">
                <tr><td colspan="2" class="contentTitleFont"><fmt:message key='comment.label.postComment'/></td></tr>
                <tr><td colspan="2" class="contentBody">&nbsp;</td></tr>
                <tr>
                    <td class="contentLabel" align="right"><fmt:message key='comment.label.title'/>&nbsp;</td>
                    <td class="contentBody">
                        <input type="text" name="title" value="<c:out value="${form.title}"/>">
                        <font color="#FF0000">
                        <strong>*</strong>
                        <c:if test="${(param.action == 'postComment') && (empty form.title)}"> <fmt:message key='comment.label.required'/> </c:if>
                    </font>
                    </td>
                </tr>
                <tr>
                    <td class="contentLabel" align="right"><fmt:message key='comment.label.rating'/>&nbsp;</td>
                    <td class="contentBody">
                        <p>
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
                    <td class="contentLabel" align="right" valign="top"><fmt:message key='comment.label.comment'/>&nbsp;</td>
                    <td class="contentBody"><textarea name="comment" cols="40" rows="10" id="comment"><c:out value="${form.comment}"/></textarea></td>
                </tr>
                <tr>
                    <td class="contentLabel" valign="top">&nbsp;</td>
                    <td class="contentBody">
                        <input type="submit" class="button" value="<fmt:message key='comment.label.submit'/>">
                        <input name="action" type="hidden" value="postComment">
                        <input name="id" type="hidden" value="<c:out value="${widget.id}"/>">
                        <input name="commentId" type="hidden" value="<c:out value="${widget.commentId}"/>">
                    </td>
                </tr>
                <tr><td colspan="2" class="contentStrapColor">&nbsp;</td></tr>
            </table>
        </form>
    </c:otherwise>
</c:choose>
<p>
<a class="contentOption" href="content.jsp?id=<c:out value='${form.commentary.parentId}'/>"><fmt:message key='comment.label.back'/></a>
