<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<link ref="stylesheet" href="images/style.css">
<table cellpadding="3" cellspacing="1" width="100%" class="forumBackground">
    <jsp:include page="../form_header.jsp" flush="true"/>
    <tr><td class="forumHeader" colspan="2"><fmt:message key='forum.label.forumPersonalization'/></td></tr>
    <tr><td class="forumRow" colspan="2">&nbsp;</td></tr>
    <c:choose>
        <c:when test="${!(empty widget.entity)}">
            <tr>
                <td class="forumRowLabel" valign="top" align="right"><fmt:message key='forum.label.forums'/>&nbsp;</td>
                <td class="forumRow"><x:display name="${widget.forums.absoluteName}"/></td>
            </tr>
            <tr>
                <td class="forumRowLabel" valign="top" align="right"><fmt:message key='forum.label.listing'/>&nbsp;</td>
                <td class="forumRow"><x:display name="${widget.listing.absoluteName}"/></td>
            </tr>
            <tr>
                <td class="forumRow">&nbsp;</td>
                <td class="forumRow">
                    <x:display name="${widget.update.absoluteName}"/>
                    <x:display name="${widget.cancel.absoluteName}"/>
                </td>
            </tr>
            <tr><td class="forumLabel" colspan="2">&nbsp;</td></tr>
        </c:when>
        <c:otherwise>
            <tr><td class="forumLabel" colspan="2"><fmt:message key='forum.label.entityIDNotSpecified'/></td></tr>
            <tr><td class="forumRow" colspan="2">&nbsp;</td></tr>
        </c:otherwise>
    </c:choose>
    <jsp:include page="../form_footer.jsp" flush="true"/>
</table>
