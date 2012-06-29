<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Task Manager</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">The task manager allows users to create and assign to do tasks. Progress of these tasks are tracked and any change in status will send a notification via memo to the appropriate user(s).</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/calendar/todotaskform.jsp"/>" class="link">Creating Tasks</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/taskmanager/taskcatform.jsp"/>" class="link">Task Category</a></td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>