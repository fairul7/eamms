<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Profile</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="<%= request.getContextPath() %>/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">My Profile</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">My Profile describes your personal profile. The setup of your profile is described within. You can update your information anytime.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="<%= request.getContextPath() %>/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Changing Your Password</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">Fill in the password fields and click on the Update button.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="<%= request.getContextPath() %>/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Subscribing To A Forum</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">You can subscribe to a forum by selecting the forum(s) of your choice and move it(them) to the Selected box. If a message is posted to the forum that you have subscribed to, a message (memo) will sent to you to inform you of the posting. To unsubscribe, move it(them) back to the Available box. Click on the Update button.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="<%= request.getContextPath() %>/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Forum Type</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">The methods of listing the forum is Threaded and Non-Threaded. Threaded forum will display message postings in a tree from. You are able to know the branch of the respose. The Non-threaded type will list in a linear form base on time of posting.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="<%= request.getContextPath() %>/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="subheader">Personal Competencies</td></tr>
    <tr><td valign=top align=left colspan="2" class="text">You can select the competencies that you have. This is useful for the Project Module where the project manager can search for users who have the required competency to carry out a task. The setting of competency is done by the System Administrator.</td></tr>
</table>
<%@ include file="../includes/footer.jsp" %>
