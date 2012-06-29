<%@include file="/common/header.jsp"%>


<c:set var="task" value="${widget.task}"/>
<c:set var="view" value="${widget}"/>

<c:if test="${forward.name == 'delete successful'}" >
    <script>
        window.opener.location.reload();
        window.close();
    </script>

</c:if>



<table width="100%">
    <thead>
    <tr align ="center">
        <td>
           <b><fmt:message key='taskmanager.label.toDoTask'/></b>
        </td>
    </tr>
    </thead>

    <tr align="center">
        <td>
            <fmt:message key='taskmanager.label.dueDate'/> :  <fmt:formatDate value="${task.dueDate}" pattern="${globalDateLong}"/>
         </td>
    </tr>

    <tr align="center">
        <td>
           <fmt:message key='taskmanager.label.dueTime'/> :  <fmt:formatDate value="${task.dueDate}" pattern="${globalTimeLong}" />
        </td>
    </tr>

    <tr align ="center" valign="top">
        <td>
           <fmt:message key='taskmanager.label.classification'/> :
           <c:set var="cla" value="${task.classification}"/>
           <c:if test="${cla == 'pub'}" > <fmt:message key='taskmanager.label.public'/> </c:if>
           <c:if test="${cla == 'pri'}" > <fmt:message key='taskmanager.label.private'/> </c:if>
           <c:if test="${cla == 'con'}" > <fmt:message key='taskmanager.label.confidential'/> </c:if>
        </td>
    </tr>
  </table>

  <table>

    <c:set var="des" value="${task.description}"/>
   <c:if test="${not empty des}">
    <tr>
        <td align="right" width="25%">
            <fmt:message key='taskmanager.label.description'/> :  </td><td> <c:out value="${des}"escapeXml="true"  /> <%--</td></tr></table>--%>
        </td>
    </tr>
    </c:if>
    <tr>
    <td  align="right">
        <fmt:message key='taskmanager.label.category'/> :  </td><td> <c:out value="${task.category}" />
    </td>

</tr>
  <tr valign="top">
        <td align="right" >
            <fmt:message key='taskmanager.label.createdBy'/> :  </td><td> <c:out value="${task.assigner}"/>
        </td>
    </tr>

    <tr valign="top">
       <td align="right" valign="top">
           <fmt:message key='taskmanager.label.assignedTo'/> :   </td><td>

          <%int i=0;%>
            <b> <%--<c:out value="${task.assigneeFirst}"/>
        <c:out value="${task.assigneeLast}"/>--%> </b>
        <c:forEach items="${task.attendees}" var="att" >
        <%if(i>0){   %>
                ,
             <%}%>
            <c:if test="${view.widgetManager.user.id == att.userId}" >
             <b>
           </c:if>

             <c:out value="${att.name}" />

            <c:if test="${view.widgetManager.user.id == att.userId}" >
             </b>
           </c:if>
            <% i++; %>
        </c:forEach>
        </td>
    </tr>


    <c:if test="${task.reassignments!=null}" >
    <c:forEach items="${task.reassignments}"  var="reassignment" >
      <tr><td>  <c:out value="${reassignment.assignerName}"/></td><td></td></tr>
       <tr><td> <c:out value="${reassignment.assigneeName}"/></td><td></td></tr>
 <c:choose>
            <c:when test="${reassignment.assignerId == widget.userId}">
                <%pageContext.setAttribute("to",Boolean.TRUE);%>
            </c:when>
            <c:when test="${reassignment.assigneeId == widget.userId}" >
                <%pageContext.setAttribute("to",Boolean.FALSE);%>
                <c:out value="${reassignment.assignerName}"/>
                <c:set value="${reassignment.assignerName}" var="reassigner" />
            </c:when>
        </c:choose>
    </c:forEach>
    <tr valign="top">
       <td align="right">
            <c:if test="${to}">
            <fmt:message key='taskmanager.label.reassignedTo'/> : </td>
            </c:if>
            <c:if test="${!to}">
            <fmt:message key='taskmanager.label.reassignedBy'/> : <c:out value="${reassigner}"/></td>
            </c:if>

       <td>
            <%i=0;%>
            <c:forEach items="${task.reassignments}" var="reassignment" >
             <%if(i>0){   %>
                ,
             <%}%>
                <c:out value="${reassignment.propertyMap.name}" /> <% i++; %>
            </c:forEach>
            <%if (i==0){%>
                -
            <%}%>
       </td>
    </tr>
    </c:if>

     <tr>
        <td valign="top" align="right">
            <fmt:message key='taskmanager.label.attachedFiles'/> :
        </td>
        <td cellspacing="0" cellpadding="0" valign="top">
        <x:display name="${view.fileListing.absoluteName}" />
        </td>
     </tr>

     <tr>
        <td align="right">
            <fmt:message key='taskmanager.label.resources'/> :
        </td>
        <td>
        <%i=0;%>
            <c:forEach items="${task.resources}" var="resource" >
             <%if(i>0){   %>
                ,
             <%}%>
                <c:out value="${resource.name}" /> <% i++; %>
            </c:forEach>
            <%if (i==0){%>
                -
            <%}%>
        </td>
     </tr>



     <c:if test="${task.completed}" >
<tr>
    <td align="right">
        <fmt:message key='taskmanager.label.completed'/> :
    </td>
    <td>
       <fmt:formatDate value="${task.completeDate}" pattern="${globalDatetimeLong}" />
    </td>
</tr>
<tr>
<td></td>
</tr>
</c:if>
</table>

<br><br>
<table>
    <tr>
        <td align="right">
         <fmt:message key='taskmanager.label.lastmodified'/> : </td><td><c:out value="${task.lastModified}" /> <fmt:message key='taskmanager.label.by'/> <c:out value="${task.lastModifiedBy}"/>
       </td>
    </tr>
    <c:if test="${task.reassign && widget.widgetManager.user.id == task.assignerId &&!task.completed}">
    <Tr>
        <td align="left">
          <a href="taskreassignform.jsp?taskId=<c:out value="${task.id}" />"><fmt:message key='taskmanager.label.reassign'/></a>
        </td>
    </tr>
    </c:if>


</table>


<c:if test="${!widget.calendarEvent}" >
<table>
<tr>
     <td>
     <br><br><br>
        <c:if test="${widget.editable}" >
            <a href="<c:out value="${widget.editUrl}"/>?id=<c:out value="${task.id}" />" ><fmt:message key='taskmanager.label.edit'/></a>
        </c:if>
        <c:if test="${widget.deletable}" >
           <c:if test="${widget.editable}" >
            | </c:if>
            <x:event name="${widget.absoluteName}" type="delete" param="taskId=${task.id}"><fmt:message key='taskmanager.label.delete'/></x:event>
        </c:if>
     </td>
</tr>
</table>
</c:if>
