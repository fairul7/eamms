<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<script language=javascript>
var ie4 = false;
if(document.all) {
	ie4 = true;
}
function getObject(id) {
	if (ie4) {
		return document.all[id];
	}
	else {
		return document.getElementById(id);
	}
}
function showDiv(select)
{
	document.getElementById("daily").style.display = "none";
	document.getElementById("weekly").style.display = "none";
	document.getElementById("monthly").style.display = "none";
	document.getElementById("yearly").style.display = "none";
	document.getElementById("frequency").style.display = "none";
	if(!select.options[0].selected)
	{
		if(select.options[1].selected)
			document.getElementById("daily").style.display = "block";
		else if(select.options[2].selected)
			document.getElementById("weekly").style.display = "block";
		else if(select.options[3].selected)
			document.getElementById("monthly").style.display = "block";
		else if(select.options[4].selected)
			document.getElementById("yearly").style.display = "block";
		document.getElementById("frequency").style.display = "block";
	}
}

function showNthWeekDay(name)
{
	var day = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*day'];
	var month = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*month'];
	var year = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*year'];
	var update = 0;
	<c:if test="${not empty form.eventId}"> update = 1; </c:if>;
	if(!update)
	{
		d = new Date();
		for(i = 0;i<=day.options.length;i++)
		{
			if(day.options[0].selected){ d.setDate(i+1);
				i = 32;
		}
	}
	for(i = 0;i<month.options.length;i++)
	{
		if(month.options[i].selected)
		{
			d.setMonth(i);
			i= 13;
		}
	}
	d.setFullYear(year.value);
	e = FirstXday(d);
	for(i=0;i<5;i++)
	{
		if(e.getDate()==d.getDate())
		{
			document.forms['<c:out value="${form.absoluteName}"/>'].elements[name].options[i].selected = true;
			i=5;
		}
		else
		{
			e.setDate(e.getDate() + 7);
		}
		}
	}
}

function FirstXday(d)
{
	Today = new Date();
	Today.setDate( 1 + ( 90 + (d.getDay()==0?7:d.getDay()) + d.getDate() - d.getDay() ) %7 )
	return Today
}

function showDay(name)
{
	var update = 0;
	<c:if test="${not empty form.eventId}"> update = 1; </c:if>;
	if(!update)
	{
		var d = new Date();
		var day = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*day'];
		for(i = 0;i<=day.options.length;i++)
		{
			if(day.options[0].selected)
			{
				d.setDate(i+1);
				i = 32;
			}
	}
	var month = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*month'];
	for(i = 0;i<month.options.length;i++)
	{
		if(month.options[i].selected)
		{
			d.setMonth(i);
			i= 13;
		}
	}
	var year = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*year'];
	d.setFullYear(year.value);
	document.forms['<c:out value="${form.absoluteName}"/>'].elements[name].options[d.getDay()].selected = true;
	}
}

function changeStartDay()
{
	var sday = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*day'];
	var eday = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.endDate.absoluteName}"/>*day'];
	eday.options[sday.selectedIndex].selected = true;
}

function changeStartMonth()
{
	var smonth = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*month'];
	var emonth = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.endDate.absoluteName}"/>*month'];
	emonth.options[smonth.selectedIndex].selected = true;
}

function changeStartYear()
{
	var syear = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*year'];
	var eyear = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.endDate.absoluteName}"/>*year'];
	eyear.value = syear.value;
}

function togglePanelVisibility(divId) {
    var divObj = getObject(divId);
    if(divObj.style.display == 'none') {
       divObj.style.display = 'block';       
    }
    else if(divObj.style.display == 'block') {
        divObj.style.display = 'none';       
    }
}
</script>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.milestone'/> *</td>
	<td class="calendarRow"><x:display name="${form.milestones.absoluteName}"/></td>
</tr>
<c:if test="${!form.newResourceBooking}" >
	<tr>
		<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.title'/> *</td>
		<td class="calendarRow">
			<x:display name="${form.title.absoluteName}"/>
			<x:display name="${form.validTitle.absoluteName}"/>
		</td>
	</tr>
</c:if>
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.startDate'/></td>
	<td class="calendarRow" ><x:display name="${form.startDate.absoluteName}"/></td>
</tr>
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.endDate'/></td>
	<td class="calendarRow"><x:display name="${form.endDate.absoluteName}"/></td>
</tr>
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.startTime'/></td>
	<td class="calendarRow"><x:display name="${form.startTime.absoluteName}"/></td>
</tr>
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.endTime'/></td>
	<td class="calendarRow">
		<x:display name="${form.endTime.absoluteName}"/>
		<x:display name="${form.allDay.absoluteName}" /><fmt:message key='project.label.allDay'/>
	</td>
</tr>

<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.location'/></td>
    <td class="calendarRow"><x:display name="${form.location.absoluteName}"/></td>
</tr>
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.classification'/></td>
	<td class="calendarRow">
		<x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='project.label.public'/>
		<x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='project.label.private'/>
	</td>
</tr>
<c:if test="${!form.newResourceBooking}" >
	<tr>
		<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.CompulsoryAttendees'/></td>
		<td class="calendarRow"><x:display name="${form.compulsory.absoluteName}"/></td>
	</tr>
	<tr>
		<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.attendees'/></td>
		<td class="calendarRow"><x:display name="${form.optional.absoluteName}"/></td>
	</tr>
</c:if>
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.resources'/></td>
    <td class="calendarRow"><x:display name="${form.resources.absoluteName}"/></td>
</tr>
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.notifyMethod'/></td>
    <td class="calendarRow">
        <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='project.label.memo'/>
        <x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='project.label.email'/>
    </td>
</tr>
</table>
<table cellpadding="0" cellspacing="1" class="forumBackground" width="100%">
<Tr>
<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
</td>
 <td class="calendarRow">
 <a href="javascript:togglePanelVisibility('optional');">
<fmt:message key='calendar.label.optionalField'/>
</a>
 </td>
</Tr>
<Tr>
<td class="calendarRowLabel" colspan="2" valign="top">
<div id="optional" style="display:none">
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.reminder'/></td>
	<td class="calendarRow">
		<x:display name="${form.reminderRadio.absoluteName}" /> <x:display name="${form.reminderQuantity.absoluteName}" />
		<x:display name="${form.reminderModifier.absoluteName}"/> <fmt:message key='project.label.before'/>
	</td>
</tr>
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.description'/></td>
    <td class="calendarRow"><x:display name="${form.description.absoluteName}"/></td>
</tr>
<c:if test="${!form.newResourceBooking}" >
	<tr>
		<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.agenda'/></td>
		<td class="calendarRow"><x:display name="${form.agenda.absoluteName}"/></td>
	</tr>
</c:if>
<tr>
   <td  class="calendarRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='project.label.recurrence'/></td>
   <td class="calendarRow">
		<div>
			<x:display name="${form.recurrence.absoluteName}"/>
			<table>
				<tr>
					<td class="calendarRow">
						<div id="daily" style="display:block">
							<x:display name="${form.everyDay.absoluteName}" /> <fmt:message key='project.label.every'/> <x:display name="${form.dailyFrequentTextField.absoluteName}"/> <fmt:message key='project.label.days'/><br>
							<x:display name="${form.everyWeekDay.absoluteName}"/> <fmt:message key='project.label.everyWeekdays'/>
						</div>
						<div id="weekly" style="display:block"><fmt:message key='project.label.every'/> <x:display name="${form.weeklyFrequentTextField.absoluteName}"/> <fmt:message key='project.label.weeks'/></div>
						<div id="monthly" style="display:block">
							<x:display name="${form.everyMonth.absoluteName}" /><x:display name="${form.monthlyFrequentTextField.absoluteName}"/> <fmt:message key='project.label.months'/>
							<br> <x:display name="${form.certainDayOfMonth.absoluteName}" /> <fmt:message key='project.label.the'/> <x:display name="${form.weekDayOrder.absoluteName}" />
							<x:display name="${form.weekDay.absoluteName}" />
							<script></script><fmt:message key='project.label.ofEvery'/> <x:display name="${form.monthlyFrequentTextField2.absoluteName}" /> <fmt:message key='project.label.months'/>
						</div>
						<div id="yearly" style="display:block"><fmt:message key='project.label.every'/> <x:display name="${form.yearlyFrequentTextField.absoluteName}"/> <fmt:message key='project.label.years'/></div>
						<div id="frequency" style="display:block">
							<hr>
							<x:display name="${form.occurenceTimes.absoluteName}" /> <x:display name="${form.occurenceTimesTF.absoluteName}" /><fmt:message key='project.label.occurences'/>
							<br><x:display name="${form.endOccurenceDate.absoluteName}" /><x:display name="${form.endOccurenceDateDF.absoluteName}" />
						</div>
						<script>showDiv(document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.recurrence.absoluteName}"/>']);</script>
					</td>
				</tr>
			</table>
		</div>
		<c:if test="${forward.name=='Recurrence Exception'}" >
    		<font color="FF0000"><fmt:message key='calendar.message.shorterFrequency'/></font>
		</c:if>
   </td>
</tr>
<c:if test="${!form.newResourceBooking}" >
	<tr>
		<td  class="calendarRowLabel" width="23%" height="20" valign="top" align="right"></td>
		<td class="calendarRow"><x:display name="${form.exclude.absoluteName}"/><fmt:message key='emeeting.label.excludeyourself'/></td>
	</tr>
</c:if>
<tr>
    <td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='project.label.notifyNote'/></td>
    <td class="calendarRow"><x:display name="${form.notifyNote.absoluteName}"/></td>
</tr>
</table></div>
</td></Tr>
</table>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr>
	<td class="calendarRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">&nbsp;</td>
	<td class="calendarRow">
		<x:display name="${form.submitButton.absoluteName}"/>
		<x:display name="${form.cancelButton.absoluteName}"/>
	</td>
</tr>
<jsp:include page="../form_footer.jsp" flush="true"/>
</table>



