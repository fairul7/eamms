<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Schedular</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">EKP Scheduler provide a single source to:</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">Schedule an appointment and book resources at the same time</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Schedule an event</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Schedule and track a meeting via the agenda, which links to the task manager</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">4.</td>
		<td valign=top align=left class="text">Manage your To Do tasks</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=10 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/calendar/appointmentform.jsp"/>" class="link">Scheduling An Appointment</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/calendar/eventform.jsp"/>" class="link">Scheduling An Event</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">&#187;</td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/calendar/emeetingform.jsp"/>" class="link">Scheduling A Meeting</a></td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>