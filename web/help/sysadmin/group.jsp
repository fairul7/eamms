<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Group</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Group manages user security, roles and access levels by assigning users to groups. By creating and defining groups, you will be able to control how users manage the different modules in EKP</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Creating A Group</td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">To create a new group, click on the New Group menu.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">
			Fill up the fields, as describe below:<br>
			<li>Name - The new group name.</li>
			<li>Description - The description for the new group name.</li>
			<li>Active - Indicate an active group.</li>
			<li>Users - Select or deselect users from a group. Use the Right button to add Users to or the Left button to remove Users from the Selected column.</li>
			<li>Permissions - Displays a list of responsibilities that determine what the specified group of users can do in the system. Put a tick mark in the appropriate checkboxes to indicate the selections. See Group Permission below for more information.</li>
		</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Lastly, click on the Save button to save the new group.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Edit Group</td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">To edit an existing group, click on the group name under the Group Listing.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Changes can be made to all fields. Click on the Save button to save the changes.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Delete Group</td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">To delete any existing group, click on the Groups Listing menu.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">To deletes users individually or by selecting records. Put a tick mark in the checkbox next to the group name under group listing.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Click on the Delete button, a confirmation pop-up message will appear before the group is deleted. Click on the OK button to delete or Cancel button to cancel.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">See Also:</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><a href="<c:url value="/help/sysadmin/permission.jsp"/>">Assigning Permissions</a></td></tr>
</table>
<%@ include file="../includes/footer.jsp" %>
