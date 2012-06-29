<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Viewing Content</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">The Content module in tmsEKP is the display of all content (articles, documents, pages and sections, spots and votes (a.k.a. Quick Poll). The content displayed is dependant on the content permission that is given to you. Each content object will have a permission setting and is set or controlled by the content manager of the content object. If you are a content manager of a content object, you can refer to Help topics in the Manage Content menu for further description.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Click on links below for further description on the topic.</td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/content/contentSections.jsp"/>" class="link">Content</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/content/contentOptions.jsp"/>" class="link">Options</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/content/contentMainHome.jsp"/>" class="link">Main/Home Content</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/content/contentLatestArticles.jsp"/>" class="link">Latest Articles</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/content/contentLatestDocuments.jsp"/>" class="link">Latest Documents</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/content/contentQuickPoll.jsp"/>" class="link">Quick Poll</a></td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>