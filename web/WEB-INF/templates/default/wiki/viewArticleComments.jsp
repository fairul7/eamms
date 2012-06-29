<%@ page import="kacang.services.security.SecurityService,
               kacang.Application,
               kacang.services.security.User"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="comments" value="${widget.comments}"/>

<script>
	function deleteComment(id){
		document.articleComments.commentId.value=id;
		document.articleComments.submit();			
	}
		
</script>
<%
  SecurityService security = (SecurityService)Application.getInstance().getService(SecurityService.class);
  User user = security.getCurrentUser(request); 
  boolean isWikiManager = security.hasPermission(user.getId(), "com.tms.wiki.ManageWiki", null, null);
%>

<form name="articleComments" action='viewArticle.jsp?articleId=<c:out value="${widget.articleId}"/>'>
<input type='hidden' name='commentId' value=''/>
<input type='hidden' name='articleId' value='<c:out value="${widget.articleId}"/>'/>
<c:forEach items="${comments}" var="comment" varStatus="status">
<table width="100%" cellspacing="1" cellpadding="2" class=discusson1>
	<tr>
	<td class="discusson_td"><font class="tsBody3">commented by <c:out value="${comment.name}"/> </font></td>
	</tr>
	<tr>
	<td class="discusson_td"><font class="font_text"><c:out value="${comment.comments}" escapeXml="false"/></font></td>
	<% if(isWikiManager) {%>
	<td align="right">	
		<input type="button" onClick="deleteComment('<c:out value="${comment.commentId}"/>');" class="button" value="Delete"/> </td>
	<% } %>
	</tr>		
	<!--<tr>
		<td align="right" class="discusson_td"> <a href="" class=next>reply</a>&nbsp;<img src="images/comment.gif"/>	</td>								
	</tr>-->	
</table>
</c:forEach>
</form>

