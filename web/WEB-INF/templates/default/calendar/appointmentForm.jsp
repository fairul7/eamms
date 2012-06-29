<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

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

function showDiv(select){
    document.getElementById("daily").style.display = "none";
    document.getElementById("weekly").style.display = "none";
    document.getElementById("monthly").style.display = "none";
    document.getElementById("yearly").style.display = "none";
    document.getElementById("frequency").style.display = "none";
    if(!select.options[0].selected){
        if(select.options[1].selected)
         document.getElementById("daily").style.display = "block";
        else if(select.options[2].selected)
          document.getElementById("weekly").style.display = "block";
        else if(select.options[3].selected)
        {
          document.getElementById("monthly").style.display = "block";
        }
        else if(select.options[4].selected)
          document.getElementById("yearly").style.display = "block";
        document.getElementById("frequency").style.display = "block";
   }
}

function showNthWeekDay(name){
    var day = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*day'];
    var month = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*month'];
    var year = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*year'];
      var update = 0;
   <c:if test="${not empty form.eventId}"> update = 1; </c:if>;
    if(!update){
    d = new Date();
    for(i = 0;i<=day.options.length;i++){
                if(day.options[0].selected){ d.setDate(i+1);
                i = 32;                         }            }
    for(i = 0;i<month.options.length;i++){
                if(month.options[i].selected){
                    d.setMonth(i);
                    i= 13;        }        }
     d.setFullYear(year.value);
     e = FirstXday(d);
     for(i=0;i<5;i++){
        if(e.getDate()==d.getDate()){
            document.forms['<c:out value="${form.absoluteName}"/>'].elements[name].options[i].selected = true;
           i=5;
        }else{
            e.setDate(e.getDate() + 7);               }    }           }
}

 function FirstXday(d) {
    Today = new Date();
    Today.setDate( 1 + ( 90 + (d.getDay()==0?7:d.getDay()) + d.getDate() - d.getDay() ) %7 )
    return Today
  }

function showDay(name){
    var update = 0;
   <c:if test="${not empty form.eventId}"> update = 1; </c:if>;
    if(!update){
             var d = new Date();
            var day = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*day'];
            for(i = 0;i<=day.options.length;i++){
                if(day.options[0].selected){ d.setDate(i+1);
                i = 32;
                }
            }
            var month = document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.startDate.absoluteName}"/>*month'];
            for(i = 0;i<month.options.length;i++){
                if(month.options[i].selected){
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

<table cellpadding="4" cellspacing="1"  width="100%" class="classBackground">
<%--        <tr><td class="classRow"  colspan="2" class="forumFooter">&nbsp;</td></tr>--%>

<c:if test="${!form.newResourceBooking}" >
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Title'/>
        <FONT class="classRowLabel">*</FONT>
    </td>
    <td class="classRow" >
        <x:display name="${form.title.absoluteName}"/>
        <x:display name="${form.validTitle.absoluteName}"/>
    </td>
</tr>
</c:if>
<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.StartDate'/></td>
<td class="classRow" >
<x:display name="${form.startDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.EndDate'/></td>
<td class="classRow" >
<x:display name="${form.endDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.StartTime'/></td>
<td class="classRow" >
<x:display name="${form.startTime.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.EndTime'/></td>
<td class="classRow" >
    <x:display name="${form.endTime.absoluteName}"/>
    <x:display name="${form.allDay.absoluteName}" /><fmt:message key='calendar.label.AllDay'/><%--
    <input type="checkbox" name="allDay" value="true"<c:if test="${calendar_calendar.allDay}"> checked</c:if>>All Day
--%>
</td>
</tr>

<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Description'/></td>
    <td class="classRow" >
        <x:display name="${form.description.absoluteName}"/>
    </td>
</tr>
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Location'/></td>
    <td class="classRow" >
        <x:display name="${form.location.absoluteName}"/>
    </td>
</tr>

<%--<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Universal'/></td>
    <td class="classRow" >
        <x:display name="${form.universal.absoluteName}"/>
    </td>
</tr>--%>

<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Classification'/></td>
    <td class=classRow >
        <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='calendar.label.Public'/><x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='calendar.label.Private'/><%--        <x:display name="${form.radioConfidential.absoluteName}"/>Confidential--%>
    </td>
</tr>
  <c:if test="${!form.newResourceBooking}" >

<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.CompulsoryAttendees'/></td>
    <td class="classRow" >
    <x:display name="${form.compulsory.absoluteName}"/>
    </td>
</tr>

<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Attendees'/></td>
    <td class="classRow" >
    <x:display name="${form.optional.absoluteName}"/>
    </td>
</tr>
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.outsideAttendees'/></td>
    <td class="classRow" >
    <x:display name="${form.outsideAttendee.absoluteName}"/>
    </td>
</tr>
   </c:if>
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Resources'/></td>
    <td class="classRow" >
    <x:display name="${form.resources.absoluteName}"/>
    </td>
</tr>
<tr>
	<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.NotifyMethod'/></td>
	<td class="classRow" ><x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='calendar.label.Memo'/><x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='calendar.label.Email'/></td>
</tr>
</table>
<table cellpadding="0" cellspacing="1" class="forumBackground" width="100%">
<Tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
</td>
 <td class="classRow">
 <a href="javascript:togglePanelVisibility('optional');">
<fmt:message key='calendar.label.optionalField'/>
</a>
 </td>
</Tr>
<Tr>
<td class="classRowLabel" colspan="2" valign="top">
<div id="optional" style="display:none">
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Reminder'/></td>
<td class="classRow" >
    <%--<select name="reminder">
        <option value=""><fmt:message key='calendar.label.None'/></option>
        <option value="1"<c:if test="${calendar_calendar.reminderDays == 1}"> selected</c:if>><fmt:message key='calendar.label.1DayBefore'/></option>
        <option value="3"<c:if test="${calendar_calendar.reminderDays == 3}"> selected</c:if>><fmt:message key='calendar.label.3DaysBefore'/></option>
        <option value="5"<c:if test="${calendar_calendar.reminderDays == 5}"> selected</c:if>><fmt:message key='calendar.label.5DaysBefore'/></option>
        <option value="7"<c:if test="${calendar_calendar.reminderDays == 7}"> selected</c:if>>1 Week Before</option>
        <option value="14"<c:if test="${calendar_calendar.reminderDays == 14}"> selected</c:if>>2 Weeks Before</option>
    </select>--%>
    <x:display name="${form.reminderRadio.absoluteName}" /> <x:display name="${form.reminderQuantity.absoluteName}" />
    <x:display name="${form.reminderModifier.absoluteName}"/><fmt:message key='calendar.label.before'/></td>
</tr>

 <c:if test="${!form.newResourceBooking}" >
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.Agenda'/></td>
    <td class="classRow" >
        <x:display name="${form.agenda.absoluteName}"/>
    </td>
</tr>
</c:if>
<tr>
   <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.Recurrence'/></td>
   <td class="classRow" >
   <div<%-- style="border:0 solid #CCCCCC; width:"--%>>
     <x:display name="${form.recurrence.absoluteName}"/><table>
     <tr> <td class="classRow" >

     <div id="daily" style="display:block">
        <x:display name="${form.everyDay.absoluteName}" /><fmt:message key='calendar.label.Every'/><x:display name="${form.dailyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.days'/><br>
        <x:display name="${form.everyWeekDay.absoluteName}"/><fmt:message key='calendar.label.EveryWeekdays'/></div>

     <div id="weekly" style="display:block"><fmt:message key='calendar.label.Every'/><x:display name="${form.weeklyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.weeks'/></div>

     <div id="monthly" style="display:block">
        <x:display name="${form.everyMonth.absoluteName}" /><x:display name="${form.monthlyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.months'/><br> <x:display name="${form.certainDayOfMonth.absoluteName}" /> The <x:display name="${form.weekDayOrder.absoluteName}" />
        <script type="text/javascript">
<%--           showNthWeekDay("<c:out value="${form.weekDayOrder.absoluteName}" />");--%>
        </script>
        <x:display name="${form.weekDay.absoluteName}" />
        <script type="text/javascript">
<%--              showDay("<c:out value="${form.weekDay.absoluteName}" />");--%>
            </script><fmt:message key='calendar.label.ofevery'/><x:display name="${form.monthlyFrequentTextField2.absoluteName}" /><fmt:message key='calendar.label.month(s)'/></div>

      <div id="yearly" style="display:block"><fmt:message key='calendar.label.Every'/><x:display name="${form.yearlyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.year(s)'/></div>

      <div id="frequency" style="display:block">
        <hr>
        <x:display name="${form.occurenceTimes.absoluteName}" /> <x:display name="${form.occurenceTimesTF.absoluteName}" /><fmt:message key='calendar.label.occurence(s)'/><br><x:display name="${form.endOccurenceDate.absoluteName}" /><x:display name="${form.endOccurenceDateDF.absoluteName}" />
      </div>
         <script type="text/javascript">
            showDiv(document.forms['<c:out value="${form.absoluteName}"/>'].elements['<c:out value="${form.recurrence.absoluteName}"/>']);
        </script>
        </td>

    </tr>
    </table>
    </div>
    <c:if test="${forward.name=='Recurrence Exception'}" >
    <FONT COLOR="#FF0000"><fmt:message key='calendar.message.shorterFrequency'/></FONT>
    </c:if>
   </td>
</tr>

 <c:if test="${!form.newResourceBooking}" >

<tr>
    <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"></td>
    <td class="classRow" >
        <x:display name="${form.exclude.absoluteName}"/><fmt:message key='calendar.label.Excludeyourself'/></td>
</tr>
</c:if>
<c:if test="${!(form.notifyNote.hidden)}">
	<tr>
		<td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.NotifyNote'/></td>
		<td class="classRow"><x:display name="${form.notifyNote.absoluteName}"/></td>
	</tr>
</c:if>
</table></div>
</td></Tr>
</table>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr><td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"></td><td class="classRow"  >
<x:display name="${form.submitButton.absoluteName}"/>
<x:display name="${form.cancelButton.absoluteName}"/>
</td></tr>
<jsp:include page="../form_footer.jsp" flush="true"/>

</table>



