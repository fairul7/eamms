<%@ include file="../includes/header.jsp" %>
<table border=0 cellspacing=1 cellpadding=1>
	<tr>
		<td valign=middle align=left width=22><IMG src="/help/images/clear.gif" height=1 width=20 border=0></td>
		<td valign=middle align=right class="header">Resource Manager</td>
	</tr>
	<tr><td valign=top align=left colspan="2"><hr size="1"></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">The tmsEKP Resource Manager Module defines all the resources available for the use of the entire organization, for example:</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
    <tr>
		<td valign=top align=left width=22 class="text">1.</td>
		<td valign=top align=left class="text">Equipment: projectors, camera, notebooks</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">2.</td>
		<td valign=top align=left class="text">Locations: board rooms, meeting rooms</td>
	</tr>
	<tr>
		<td valign=top align=left width=22 class="text">3.</td>
		<td valign=top align=left class="text">Facilities, Service: company driver</td>
	</tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Resources can be booked (reserved) when you create an appointment. You may want to book more than one item in a single appointment. To view on how to book(reserve) a resouce item, see Help instructions on Scheduling An Appointment below.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr><td valign=top align=left colspan="2" class="text">Booking of resources are approved by the Resource Approver. If there is no Resource Approver, the system will approve based on a first-come first-served basis. Resources that are not available will be shown a CONFLICT flag when you try to schedule an appointment.</td></tr>
	<tr><td valign=top align=left colspan="2" class="text"><IMG src="/help/images/clear.gif" height=5 width=1 border=0></td></tr>
	<tr>
		<td valign=top align=left width=22><span class="text">&#187;</span></td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/resourcemanager/addresourceform.jsp"/>" class="link">Creating/Editing A Resource</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22><span class="text">&#187;</span></td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/resourcemanager/resourceview.jsp"/>" class="link">Viewing Resources</a></td>
	</tr>
	<tr>
		<td valign=top align=left width=22><span class="text">&#187;</span></td>
		<td valign=top align=left class="text"><a href="<c:url value="/help/resourcemanager/approvingbooking.jsp"/>" class="link">Approving A Booking</a></td>
	</tr>
</table>
<%@ include file="../includes/footer.jsp" %>
