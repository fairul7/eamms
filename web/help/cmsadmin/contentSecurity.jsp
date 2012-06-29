<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Content Permissions</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">This allows the administrator to set/change permission of the particular content.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">To assign content permission select from the “Principal” column and add to the “Managers”, “Editors”, “Authors”, and “Readers” column by clicking on Right button. Use the Left button to remove users from the group columns. See below for roles description.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">The permission groups consist of Manager, Editors, Authors and Readers.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Manager</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">All accesses to manage content including permission assignment.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Editor</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Access to manage content such as edit and approve content submitted by the author.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Author</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Access to compose or contribute new content, but no access to approve and publish content. Approval must be obtained from editor or manager.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Reader</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Access or view the content in the front-end.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Inherit from parent</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Putting a check on the Inherit from parent indicates the content permission will be the same as that of the current parent.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Propagate</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Putting a check on the Propagate indicates the content permission for all the children (and descendants) of the content will be the same as that of the current content.</td></tr>
</table>
<%@ include file="../includes/footer.jsp" %>