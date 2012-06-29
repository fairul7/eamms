<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<script language=javascript>
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
                if(day.options[0].selected)
                {
                    d.setDate(i+1);
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
                    e.setDate(e.getDate() + 7);
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
    function changeStartDay(){
    var sday = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*day'];
    var eday = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.endDate.absoluteName}"/>*day'];
    eday.options[sday.selectedIndex].selected = true;
}

function changeStartMonth(){
    var smonth = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*month'];
    var emonth = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.endDate.absoluteName}"/>*month'];
    emonth.options[smonth.selectedIndex].selected = true;
}

function changeStartYear(){
    var syear = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*year'];
    var eyear = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.endDate.absoluteName}"/>*year'];
    eyear.value = syear.value;
}
</script>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" width="100%">
    <c:if test="${!form.newResourceBooking}" >
        <tr>
            <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.title'/></td>
            <td class="emeetingRow">
                <x:display name="${form.title.absoluteName}"/>
                <x:display name="${form.validTitle.absoluteName}"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.meetingCategory'/></td>
        <td class="emeetingRow"><x:display name="${form.category.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right">&nbsp;</td>
        <td class="emeetingRow"><fmt:message key='emeeting.label.newCategory'/>: <x:display name="${form.otherCategory.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.secretary'/></td>
        <td class="emeetingRow"><x:display name="${form.secretary.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.documents'/></td>
        <td class="emeetingRow">
           <x:display name="${form.fileListing.absoluteName}" ></x:display>  <br>
           <x:display name="${form.attachFilesButton.absoluteName}" ></x:display>
        </td>
    </tr>
    <tr>
        <td class="emeetingRow" colspan="2"><hr size="1" width="90%" style="border: dotted 1px silver"></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.startDate'/></td>
        <td class="emeetingRow"><x:display name="${form.startDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.endDate'/></td>
        <td class="emeetingRow"><x:display name="${form.endDate.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.startTime'/></td>
        <td class="emeetingRow"><x:display name="${form.startTime.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.endTime'/></td>
        <td class="emeetingRow">
            <x:display name="${form.endTime.absoluteName}"/>
            <x:display name="${form.allDay.absoluteName}" /> <fmt:message key='emeeting.label.allDay'/>        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.reminder'/></td>
        <td class="emeetingRow">
            <x:display name="${form.reminderRadio.absoluteName}" /> <x:display name="${form.reminderQuantity.absoluteName}" />
            <x:display name="${form.reminderModifier.absoluteName}"/> <fmt:message key='emeeting.label.before'/>        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.description'/></td>
        <td class="emeetingRow"><x:display name="${form.description.absoluteName}"/></td>
    </tr>
    <c:if test="${!form.newResourceBooking}" >
        <tr>
            <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.agenda'/></td>
            <td class="emeetingRow"><x:display name="${form.agenda.absoluteName}"/></td>
        </tr>
    </c:if>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.location'/></td>
        <td class="emeetingRow"><x:display name="${form.location.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.recurrence'/></td>
        <td class="emeetingRow">
            <div >
                <x:display name="${form.recurrence.absoluteName}"/>
                <table>
                    <tr>
                        <td>
                            <div id="daily" style="display:block">
                                <x:display name="${form.everyDay.absoluteName}" /> <fmt:message key='emeeting.label.every'/> <x:display name="${form.dailyFrequentTextField.absoluteName}"/> <fmt:message key='emeeting.label.days'/>.<br>
                                <x:display name="${form.everyWeekDay.absoluteName}"/> <fmt:message key='emeeting.label.everyWeekdays'/>.                            </div>
                            <div id="weekly" style="display:block"><fmt:message key='emeeting.label.every'/> <x:display name="${form.weeklyFrequentTextField.absoluteName}"/> <fmt:message key='emeeting.label.weeks'/>.</div>
                            <div id="monthly" style="display:block">
                                <x:display name="${form.everyMonth.absoluteName}" /><x:display name="${form.monthlyFrequentTextField.absoluteName}"/> <fmt:message key='emeeting.label.months'/>.                               <br>
                                <x:display name="${form.certainDayOfMonth.absoluteName}" /> <fmt:message key='emeeting.label.the'/> <x:display name="${form.weekDayOrder.absoluteName}" />
                                <script type="text/javascript">showNthWeekDay("<c:out value="${form.weekDayOrder.absoluteName}" />");</script>
                                <x:display name="${form.weekDay.absoluteName}" />
                                <script type="text/javascript">showDay("<c:out value="${form.weekDay.absoluteName}" />");</script><fmt:message key='emeeting.label.ofevery'/> <x:display name="${form.monthlyFrequentTextField2.absoluteName}" /> <fmt:message key='emeeting.label.months'/>.                            </div>
                            <div id="yearly" style="display:block"><fmt:message key='emeeting.label.every'/> <x:display name="${form.yearlyFrequentTextField.absoluteName}"/> <fmt:message key='emeeting.label.years'/>.</div>
                            <div id="frequency" style="display:block">
                                <hr>
                                <x:display name="${form.occurenceTimes.absoluteName}" /> <x:display name="${form.occurenceTimesTF.absoluteName}" /><fmt:message key='emeeting.label.occurences'/>.                                <br>
                                <x:display name="${form.endOccurenceDate.absoluteName}" /><x:display name="${form.endOccurenceDateDF.absoluteName}" />.
                            </div>
                            <script type="text/javascript">showDiv(document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.recurrence.absoluteName}"/>']);</script>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <c:if test="${!form.newResourceBooking}" >
        <tr>
            <td class="calendarRowLabel" valign="top" align="right"></td>
            <td class="emeetingRow"><x:display name="${form.exclude.absoluteName}"/><fmt:message key='emeeting.label.excludeyourself'/></td>
        </tr>

    </c:if>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.classification'/></td>
        <td class="emeetingRow">
            <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='emeeting.label.public'/>            <x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='emeeting.label.private'/>        </td>
    </tr>
    <c:if test="${!form.newResourceBooking}" >
        <tr>
            <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.compulsoryAttendees'/></td>
            <td class="emeetingRow"><x:display name="${form.compulsory.absoluteName}"/></td>
        </tr>
        <tr>
            <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.optionalAttendees'/></td>
            <td class="emeetingRow"><x:display name="${form.optional.absoluteName}"/></td>
        </tr>
   </c:if>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.resources'/></td>
        <td class="emeetingRow"><x:display name="${form.resources.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.notifyMethod'/></td>
        <td class="emeetingRow">
            <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='emeeting.label.memo'/>            <x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='emeeting.label.email'/>        </td>
    </tr>
    <tr>
        <td class="calendarRowLabel" valign="top" align="right"><fmt:message key='emeeting.label.notifyNote'/></td>
        <td class="emeetingRow"><x:display name="${form.notifyNote.absoluteName}"/></td>
    </tr>
    <tr><td class="emeetingRow" colspan="2">&nbsp;</td></tr>
    <tr>
        <td class="emeetingRow" colspan="1">
        </td>
        <td class="emeetingRow" colspan="1">
            <x:display name="${form.submitButton.absoluteName}"/>
            <x:display name="${form.cancelButton.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
