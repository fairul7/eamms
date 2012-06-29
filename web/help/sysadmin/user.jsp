<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Managing Users</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Users module manages all system users, both website members and administrative users. The user access to content can be managed from here.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Create New User</td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">To create a new user, click on the New User button.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">
			Fill up the fields, as describe below:<br>
			<li>First Name - The user’s first name</li>
			<li>Last Name - The user’s last name.</li>
			<li>Email - Provide a valid email address for contact and update purposes. This must be a unique address. The system will return an error message if the address has been used by another user.</li>
			<i>*Note: This email address will also be used for forum subscription and mass email purpose.</i>
			<li>Address</li>
			<li>Postcode</li>
			<li>City</li>
			<li>State</li>
			<li>Country</li>
			<li>Office Phone</li>
			<li>Home Phone</li>
			<li>Mobile Phone</li>
			<li>Fax</li>
			<li>Username - Type in the new username. This username will be required for login purposes into the CMS system. This must be a unique entry. The system will return an error message if the entry has been used by another user.</li>
			<li>Password - Assign a password to the new user.</li>
			<i>*Note: The user may change the password after he/she has logged on.</i>
			<li>Confirm Password - Re-type the password for confirmation.</li>
			<li>Active - Put a tick mark to indicate an active user. An inactive user cannot login to the system.</li>
			<li>Group - Determine the user’s access levels in the system by assigning a group access. To assign the user to a group, select from the “Available” column and add to the “Selected” column by clicking on the Right button. Use the Left button to remove users from the “Selected” column.</li>
		</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Lastly, click on the Save button to save the new user details.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Update User Information</td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">To edit an existing user, click on the username under the User Listing.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Changes can be made to all fields. Click on the Save button to save the changes.</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Delete User</td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">To delete any existing user, click on the User Listing menu.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">To deletes users individually or by selecting records. Put a tick mark in the checkbox next to the users name under users listing.</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Click on the Delete button, a confirmation pop-up message will appear the user is deleted. Click on the OK button to delete or Cancel button to cancel.</td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>