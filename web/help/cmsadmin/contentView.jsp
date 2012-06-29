<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Content Details</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Content details shows the details of current content being accessed. Content details display the following information about the content:</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Name</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Name of the content.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Type</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Types of content, e.g., section, article, document, image, bookmark, virtual section, commentary, page or spot.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">ID</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">The ID of the content. The content ID is generated automatically by system or can be defined by the author.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Version</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">The version of the content. The version number will have increment of one whenever the content is checked-out. However there is no version increment for “undo-checkout”.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Profile</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">The profile of the content. The profile only applies to Section, Article and Page.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Status</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Status of the content such as whether content is already checked-out, approved and published.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Content Children Button</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">The status of all the content children can be changed at this stage by using the available buttons to perform different actions on the content children.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Put a tick mark in the checkbox next to the content name under content children listing, and click on the appropriate button.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Publish</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Publishes all selected content or ticked. The content will be displayed at the front-end for viewing.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Withdraw</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Withdraws the published status of the content and change the content back to unpublished. The content will not be published at the front-end.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Archive</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Archives the content by flagging it.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Unarchive</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Unarchives content that has been archived previously.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Move</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Moves content to a selected location in the content tree structure.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Delete</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Deletes the content, moving it into the system recycle bin. The content can be recovered if the content is not removed from the recycle bin.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><i>Note: All button functions only apply to the content children and NOT to the content that is being viewed (Content Details).</i></td></tr>
</table>
<%@ include file="../includes/footer.jsp" %>