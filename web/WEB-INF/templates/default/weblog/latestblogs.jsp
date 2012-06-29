<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@include file="/common/header.jsp"%>
<c:set var="blogs" value="${widget.blogs}" />
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td colspan="3" class="blogHeader"><fmt:message key='weblog.label.latestWeblogs'/></td></tr>
	<tr><td colspan="3" class="blogHeader" height="5"></td></tr>
    <tr>
        <td class="blogLabel"><fmt:message key='weblog.label.author'/></td>
        <td class="blogLabel"><fmt:message key='weblog.label.weblog'/></td>
        <td class="blogLabel"><fmt:message key='weblog.label.time'/></td>
    </tr>
	<tr><td colspan="3" class="blogHeader" height="1"><hr size="1"</td></tr>
    <c:forEach var="blog" items="${blogs}">
		<tr>
			<td class="blogRow"><c:out value="${blog.userName}" /></td>
			<td  class="blogRow"><a href="<c:out value="${widget.blogViewUrl}" />?blogId=<c:out value="${blog.id}" />"><c:out value="${blog.title}" /></a></td>
			<td class="blogRow"><fmt:formatDate pattern="${globalDatetimeLong}" value="${blog.creationDate}" /></td>
		</tr>
    </c:forEach>
</table>
