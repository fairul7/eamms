<%@ page import="org.apache.commons.lang.StringUtils,
                 java.util.Date,
                 com.tms.collab.taskmanager.ui.TaskResourceForm,
                 java.util.Map,
                 com.tms.collab.resourcemanager.model.ResourceManager,
                 kacang.Application,
                 com.tms.collab.calendar.model.CalendarModule,
                 com.tms.collab.resourcemanager.model.ResourceBooking"%>
<%@include file="/common/header.jsp" %>

<c:set var="form" value="${widget}" />
<c-rt:set var="conflict" value="<%=TaskResourceForm.FORWARD_RESOURCES_CONFLICT%>" />
<jsp:include page="../form_header.jsp" flush="true"/>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <tr>
        <Td class="calendarHeader" colspan="2" align="center"><fmt:message key='taskmanager.label.TaskResources'/></td>
    </tr>
    <tr>
        <Td class="calendarRowLabel" align="right" width="50%"><fmt:message key='taskmanager.label.Title'/></td>
        <td class="calendarRow">
            <c:out value="${form.task.title}"/>
        </td>
    </tr>
    <tr>
        <Td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.Description'/></td>
        <Td class="calendarRow">
            <c:set var="description" value="${form.task.description}" />
            <%
                String description = (String)pageContext.getAttribute("description");
                StringUtils.replace(description,"\n","<br>");
                pageContext.setAttribute("description",description);
            %>
            <c:out value="${description}" />
        </td>
    </tr>
    <tr>
        <Td class="calendarRowLabel" align="right"><fmt:message key='taskmanager.label.DueDate'/></td>
        <Td  class="calendarRow">
            <fmt:formatDate value="${form.task.dueDate}" pattern="${globalDatetimeLong}" />
        </td>
    </tr>

</table>

<table  cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
<tr>
    <Td class="calendarRowLabel"><fmt:message key='taskmanager.label.Resource'/></td>

    <Td class="calendarRowLabel"><fmt:message key='taskmanager.label.StartDate&Time'/></td class="calendarRowLabel">

    <Td class="calendarRowLabel"><fmt:message key='taskmanager.label.EndDate&Time'/></td class="calendarRowLabel">

    <%
        TaskResourceForm form = (TaskResourceForm)pageContext.getAttribute("form");
        Map conflictMap = (Map)session.getAttribute("conflictMap");

        if(conflictMap!=null&&conflictMap.size()>0){
        %>
    <td class="calendarRowLabel">
    </td>
    <Td class="calendarRowLabel"><fmt:message key='taskmanager.label.Confirm'/></td>
            <%}%>

</tr>
<%
   pageContext.setAttribute("date",form.getTask().getStartDate());
%>


<fmt:formatDate value="${date}" pattern="d" var="dateOfMonth" />
<fmt:formatDate value="${date}" var="imonth" pattern="M" />
<fmt:formatDate value="${date}" var="shour" pattern="k" />
<fmt:formatDate value="${date}" var="sminute" pattern="m" />
<fmt:formatDate value="${form.task.dueDate}" pattern="d" var="edateOfMonth" />
<fmt:formatDate value="${form.task.dueDate}" var="emonth" pattern="M" />
<fmt:formatDate value="${form.task.dueDate}" var="ehour" pattern="k" />
<fmt:formatDate value="${form.task.dueDate}" var="eminute" pattern="m" />


<c:forEach items="${form.resourceMap}" var="resource" >
 <c:set var="resourceId" value="${resource.key}"/>
<%
    String resourceId = (String)pageContext.getAttribute("resourceId");
    Date date= null, dueDate =null;
    if(form.isInvalid()||form.isEdit()||(conflictMap!=null&&conflictMap.size()>0)){
        if(form.getsDateMap()!=null){
            date = (Date)form.getsDateMap().get(resourceId);
            dueDate = (Date)form.geteDateMap().get(resourceId);
        } else{
            if(form.isEdit()){
                ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
                ResourceBooking booking = rm.getResourceBooking(resourceId,form.getTaskId(),CalendarModule.DEFAULT_INSTANCE_ID);
                if(booking != null){
                    date = booking.getStartDate();
                    dueDate = booking.getEndDate();
                } else{
                    date = (Date)pageContext.getAttribute("date");
                    dueDate = form.getTask().getDueDate();
                }
            }

        }

        //pageContext.setAttribute("resourceName",rm.getResource(resourceId,true).getName());
        pageContext.setAttribute("date",date);
        pageContext.setAttribute("dueDate",dueDate);
%>
    <fmt:formatDate value="${date}" pattern="d" var="dateOfMonth" />
    <fmt:formatDate value="${date}" var="imonth" pattern="M" />
    <fmt:formatDate value="${date}" var="shour" pattern="k" />
    <fmt:formatDate value="${date}" var="sminute" pattern="m" />
    <fmt:formatDate value="${dueDate}" pattern="d" var="edateOfMonth" />
    <fmt:formatDate value="${dueDate}" var="emonth" pattern="M" />
    <fmt:formatDate value="${dueDate}" var="ehour" pattern="k" />
    <fmt:formatDate value="${dueDate}" var="eminute" pattern="m" />
<%}
    if(form.getInvalidDates()!=null&&form.getInvalidDates().contains(resourceId))
        pageContext.setAttribute("invalid",Boolean.TRUE);
    else
        pageContext.setAttribute("invalid",Boolean.FALSE);

%>

<tr>
<td valign="top" class="calendarRowLabel">
    <c:out value="${resourceName}"/>   <%


          ResourceManager rm = (ResourceManager) Application.getInstance().getModule(ResourceManager.class);
    out.print(rm.getResource(resourceId,true).getName());
    %>
</td>

<td class="calendarRow">


<c:if test="${invalid}">
  !<span style="border:1 solid #de123e">
</c:if>

    <select name="<c:out value="${resource.key}"/>*sdate" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
        <c:forEach begin="1" end="31" var="d">
            <option value="<c:out value="${d}"/>" <c:if test="${d eq dateOfMonth}">selected</c:if>>
            <c:out value="${d}"/></option>
        </c:forEach>
    </select>

    <select name="<c:out value="${resource.key}"/>*smonth" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
        <c:forEach begin="0" end="11" step="1" var="month">
            <option value="<c:out value="${month}"/>"<c:if test="${month eq imonth-1}">selected</c:if>>
            <fmt:parseDate value="${month+1}" pattern="MM" var="fm"/>
            <fmt:formatDate pattern="MMMM" value="${fm}"/></option>
        </c:forEach>
    </select>
    <input type="text" name="<c:out value="${resource.key}"/>*syear" value="<fmt:formatDate pattern="yyyy" value="${date}"/>" maxlength="4" size="4" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">

  <c:if test="${invalid}">
  </span>
</c:if>



</td>

<td  class="calendarRow">
    <select name="<c:out value="${resource.key}"/>*edate" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
        <c:forEach begin="1" end="31" var="d">
            <option value="<c:out value="${d}"/>" <c:if test="${d eq edateOfMonth}">selected</c:if>>
            <c:out value="${d}"/></option>
        </c:forEach>
    </select>

    <select name="<c:out value="${resource.key}"/>*emonth" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
        <c:forEach begin="0" end="11" step="1" var="month">
            <option value="<c:out value="${month}"/>"<c:if test="${month eq emonth-1}">selected</c:if>>
            <fmt:parseDate value="${month+1}" pattern="MM" var="fm"/>
            <fmt:formatDate pattern="MMMM" value="${fm}"/></option>
        </c:forEach>
    </select>
    <input type="text" name="<c:out value="${resource.key}"/>*eyear" value="<fmt:formatDate pattern="yyyy" value="${form.task.dueDate}"/>" maxlength="4" size="4" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">

</td>

 <%
     if(conflictMap!=null&&conflictMap.size()>0){
        %>
    <td class="calendarRow">
        <%
            if(conflictMap.containsKey(resourceId)){
        %>
        <x:event name="${form.absoluteName}" type="viewConflict" param="resourceId=${resourceId}"  ><fmt:message key='taskmanager.label.Conflicts'/></x:event>
        <%-- <a href="" ></a>
       --%>
            <%}%>

    </td>

    <td class="calendarRow">
        <%
            if(conflictMap.containsKey(resourceId)){
        %>
         <input type="checkbox" name="resourcesCB" value="<c:out value="${resource.key}"/>" CHECKED>

            <%}%>
            </td>
            <%}%>

</tr>




<Tr>
    <Td class="calendarRowLabel"> &nbsp;</td>
    <Td class="calendarRow">
    <c:if test="${invalid}">
  !<span style="border:1 solid #de123e">
</c:if>
    <select name="<c:out value="${resource.key}"/>*shour" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="23" var="h">
    <option value="<c:out value="${h}"/>" <c:if test="${h eq shour}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${h}"/></option>
</c:forEach>
</select>

<select name="<c:out value="${resource.key}"/>*sminute" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="45" step="15" var="m">
    <option value="<c:out value="${m}"/>" <c:if test="${m eq sminute || (m < sminute && m+15 >sminute)}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${m}"/></option>
</c:forEach>
</select>
<c:if test="${invalid}">
  </span >
</c:if>
    </td>
    <td class="calendarRow">
<select name="<c:out value="${resource.key}"/>*ehour" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="23" var="h">
    <option value="<c:out value="${h}"/>" <c:if test="${h eq ehour}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${h}"/></option>
</c:forEach>
</select>

<select name="<c:out value="${resource.key}"/>*eminute" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">
<c:forEach begin="0" end="45" step="15" var="m">
    <option value="<c:out value="${m}"/>" <c:if test="${m eq eminute}">selected</c:if>>
        <fmt:formatNumber pattern="00" value="${m}"/></option>
</c:forEach>
</select>

    </td>
    <%
        if(conflictMap!=null&&conflictMap.size()>0){
        %>
        <Td  class="calendarRow">
            &nbsp;
        </td>
        <Td  class="calendarRow">
            &nbsp;
        </td>

        <%}%>

</tr>
</c:forEach>
<tr>
    <td class="calendarRow">
    </td>
    <%
        if(conflictMap!=null&&conflictMap.size()>0){
        %>

    <td class="calendarRow" colspan="4">
    <%}else{%>
    <td class="calendarRow" colspan="2">
    <%}%>
        <x:display name="${form.bookButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
    </td>

</tr>
<tr>
    <%
        if(conflictMap!=null&&conflictMap.size()>0){
        %>

    <td class="calendarFooter" colspan="5">
    <%}else{%>
    <td class="calendarFooter" colspan="3">
    <%}%>
        &nbsp;
   </td>

</tr>

</table>
  <jsp:include page="../form_footer.jsp" flush="true"/>
