<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.calendar.model.CalendarModule
                 ,
                 kacang.util.Log"%>
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
function <c:out value="${form.compulsory.absoluteNameForJavaScript}"/>submit() {
}

function <c:out value="${form.optional.absoluteNameForJavaScript}"/>submit() {
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
<table cellpadding="4" cellspacing="1" class="classBackground" width="100%">

<c:if test="${!form.newResourceBooking}" >
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.title'/>
        <FONT class="classRowLabel">*</FONT>
    </td>
    <td class="classRow">
        <x:display name="${form.title.absoluteName}"/>
        <x:display name="${form.validTitle.absoluteName}"/>
    </td>
</tr>
</c:if>
<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.startDate'/></td>
<td class="classRow">
<x:display name="${form.startDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.endDate'/></td>
<td class="classRow">
<x:display name="${form.endDate.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.startTime'/></td>
<td class="classRow">
<x:display name="${form.startTime.absoluteName}"/>
</td>
</tr>

<tr>
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.endTime'/></td>
<td class="classRow">
    <x:display name="${form.endTime.absoluteName}"/>
       <x:display name="${form.allDay.absoluteName}" /><fmt:message key='calendar.label.allDay'/><%--
    <input type="checkbox" name="allDay" value="true"<c:if test="${calendar_calendar.allDay}"> checked</c:if>>All Day
--%>
</td>
</tr>




<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.location'/></td>
    <td class="classRow">
        <x:display name="${form.location.absoluteName}"/>
    </td>
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
<td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.reminder'/></td>
<td class="classRow">
    <%--<select name="reminder">
        <option value="">None</option>
        <option value="1"<c:if test="${calendar_calendar.reminderDays == 1}"> selected</c:if>>1 Day Before</option>
        <option value="3"<c:if test="${calendar_calendar.reminderDays == 3}"> selected</c:if>>3 Days Before</option>
        <option value="5"<c:if test="${calendar_calendar.reminderDays == 5}"> selected</c:if>>5 Days Before</option>
        <option value="7"<c:if test="${calendar_calendar.reminderDays == 7}"> selected</c:if>>1 Week Before</option>
        <option value="14"<c:if test="${calendar_calendar.reminderDays == 14}"> selected</c:if>>2 Weeks Before</option>
    </select>--%>
    <x:display name="${form.reminderRadio.absoluteName}" /> <x:display name="${form.reminderQuantity.absoluteName}" />
    <x:display name="${form.reminderModifier.absoluteName}"/><fmt:message key='calendar.label.before'/></td>
</tr>

<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.description'/></td>
    <td class="classRow">
        <x:display name="${form.description.absoluteName}"/>
    </td>
</tr>
<tr>
   <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.Recurrence'/></td>
   <td class="classRow">
   <div<%-- style="border:1 solid #CCCCCC; width:"--%>>
     <x:display name="${form.recurrence.absoluteName}"/><table>
     <tr> <td class="classRow">

     <div id="daily" style="display:block">
        <x:display name="${form.everyDay.absoluteName}" /><fmt:message key='calendar.label.every'/><x:display name="${form.dailyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.day(s)'/>.<br>
        <x:display name="${form.everyWeekDay.absoluteName}"/><fmt:message key='calendar.label.everyWeekdays'/>.</div>

     <div id="weekly" style="display:block"><fmt:message key='calendar.label.every'/><x:display name="${form.weeklyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.week(s)'/>.</div>

     <div id="monthly" style="display:block">
        <x:display name="${form.everyMonth.absoluteName}" /><x:display name="${form.monthlyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.month(s)'/>.<br> <x:display name="${form.certainDayOfMonth.absoluteName}" /><fmt:message key='calendar.label.the'/><x:display name="${form.weekDayOrder.absoluteName}" />
        <script type="text/javascript">
<%--           showNthWeekDay("<c:out value="${form.weekDayOrder.absoluteName}" />");--%>
        </script>
        <x:display name="${form.weekDay.absoluteName}" />
        <script type="text/javascript">
<%--              showDay("<c:out value="${form.weekDay.absoluteName}" />");--%>
            </script><fmt:message key='calendar.label.ofevery'/><x:display name="${form.monthlyFrequentTextField2.absoluteName}" /><fmt:message key='calendar.label.month(s)'/>.</div>

      <div id="yearly" style="display:block"><fmt:message key='calendar.label.every'/><x:display name="${form.yearlyFrequentTextField.absoluteName}"/><fmt:message key='calendar.label.year(s)'/>.</div>

      <div id="frequency" style="display:block">
        <hr>
        <x:display name="${form.occurenceTimes.absoluteName}" /> <x:display name="${form.occurenceTimesTF.absoluteName}" /><fmt:message key='calendar.label.occurence(s)'/>.<br><x:display name="${form.endOccurenceDate.absoluteName}" /><x:display name="${form.endOccurenceDateDF.absoluteName}" />.
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


 <c:set var="userId" value="${widget.widgetManager.user.id}" ></c:set>
    <%
    SecurityService  ss = (SecurityService )Application.getInstance().getService(SecurityService .class);
    try
    {
        String userId = (String)pageContext.getAttribute("userId");
        if(ss.hasPermission(userId,CalendarModule.PERMISSION_MANAGE_EVENTS,null,null))
        {
    %>
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.classification'/></td>
    <td class="classRow">
        <x:display name="${form.radioPublic.absoluteName}"/><fmt:message key='calendar.label.public'/>
        <x:display name="${form.radioPrivate.absoluteName}"/><fmt:message key='calendar.label.private'/><%--
        <x:display name="${form.radioConfidential.absoluteName}"/>Confidential
--%>
    </td>
</tr>
    <%
            }
        } catch (SecurityException e)
        {
            Log.getLog(getClass()).error(e);  //To change body of catch statement use Options | File Templates.
        }
     %>
<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='calendar.label.resources'/></td>
    <td class="classRow" >
    <x:display name="${form.resources.absoluteName}"/>
    </td>
</tr>

<%--<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.notifyMethod'/></td>
    <td class="classRow">
        <x:display name="${form.notifyMemo.absoluteName}"/><fmt:message key='calendar.label.memo'/><x:display name="${form.notifyEmail.absoluteName}"/><fmt:message key='calendar.label.email'/></td>
</tr>

<tr>
    <td class="classRowLabel" width="23%" height="20" valign="top" align="right"><fmt:message key='calendar.label.notifyNote'/></td>
    <td class="classRow">
        <x:display name="${form.notifyNote.absoluteName}"/>
    </td>
</tr>--%>
</table></div>
</td></Tr>
</table>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr><td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"></td><td  class="classRow">
<x:display name="${form.submitButton.absoluteName}"/>
<x:display name="${form.cancelButton.absoluteName}"/>
</td></tr>

<jsp:include page="../form_footer.jsp" flush="true"/>
</table>



