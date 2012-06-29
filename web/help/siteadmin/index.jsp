<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Site Admin</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Site Design manages the site template. Besides the main site look-and-feel, each content object (sections, pages, and virtual sections) can have different templates.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">This only apply to website with few templates.</td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">Select a template from Custom Template menu.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Select the content(s) and click on the Add button.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Added content(s) will be listed in the Selected Content.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">4.</td>
		<td valign=top align=left class="text">To remove the conten(s), select the content(s) and click on the Remove Selected button.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/siteadmin/ad_index.jsp"/>" class="link">Managing Ads</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/siteadmin/pollAdmin.jsp"/>" class="link">Managing Votes</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/siteadmin/eventList.jsp"/>" class="link">Managing Events</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/siteadmin/ml_index.jsp"/>" class="link">Managing Mailing Lists</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/siteadmin/mobileChannelList.jsp"/>" class="link">Managing Mobile</a></td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>