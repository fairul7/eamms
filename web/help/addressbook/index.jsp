<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Contacts</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Contacs module is used to store personal contacts and share all business contacts. It is a company wide address book which keeps track of contacts in direct association with the organization such as clients, suppliers, partners etc. You can search for Business Contacts, Intranet Users and Personal Contacts in this module.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/abNewContact.jsp"/>" class="link">Adding A Contact</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/dbContactList.jsp"/>" class="link">Managing Contacts</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/bdCompanyList.jsp"/>" class="link">Managing Companies</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/bdNewContactGroup.jsp"/>" class="link">Creating A Distribution List</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/bdCompanyApprovalList.jsp"/>" class="link">Approving A New Contact/Company</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/abFolderList.jsp"/>" class="link">Managing Folders</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/bdOptions.jsp"/>" class="link">Importing Business Contacts</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/bdContactList.jsp"/>" class="link">Managing Personal Contacts</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/addressbook/udContactList.jsp"/>" class="link">Viewing Intranet Users</a></td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>