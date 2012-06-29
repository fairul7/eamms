<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskListing"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="view" value="${widget}"/>

<c:set var="completeUrl">
<%=response.encodeURL(request.getRequestURI())%>?<%=Event.PARAMETER_KEY_WIDGET_NAME%>=<c:out value="${view.absoluteName}"/>&<%=Event.PARAMETER_KEY_EVENT_TYPE%>=<%=TaskListing.PARAMETER_EVENT_COMPLETE%>

</c:set>

   <jsp:include page="../form_header.jsp" flush="true"/>


<%--
  <table border="0" cellpadding="2" cellspacing="1" width="100%">
--%>
                <script>
                <!--
                    function taskcategorytable_toggle(obj) {
                        if (obj.checked) {
                            taskcategorytable_selectAll(document.forms['<c:out value="${widget.absoluteName}" />']['<c:out value="${widget.sel.absoluteName}"/>']);
                        }
                        else {
                            taskcategorytable_deselectAll(document.forms['<c:out value="${widget.absoluteName}" />']['<c:out value="${widget.sel.absoluteName}"/>']);
                        }
                    }
                    function taskcategorytable_selectAll(obj) {
                        if (!obj) {
                            return;
                        }
                        if (!obj.length) {
                            obj.checked = true;
                        }
                        for(i=0; i<obj.length; i++) {
                            obj[i].checked = true;
                        }
                    }
                    function taskcategorytable_deselectAll(obj) {
                        if (!obj) {
                            return;
                        }
                        if (!obj.length) {
                            obj.checked = false;
                        }
                        for(i=0; i<obj.length; i++) {
                            obj[i].checked = false;
                        }
                    }
                //-->
                </script>
<script>

function viewTask(id,userid)
{
    <c:choose>
        <c:when test="${view.popupViewUrl != null}" >
         window.open('<c:out value="${view.popupViewUrl}"/>?id='+id+'&<%=TaskListing.PARAMETER_EVENT_USERID%>='+userid,'viewtask','scrollbars=yes,resizable=yes,width=480,height=450');

        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>
}
</script>
        <tr >
            <td align="left" colspan="3"  class="calendarFooter" >
             <c:choose>
               <c:when test="${view.desc}" >
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=description"  >
                                  <B>   Tasks</B>
                    </x:event>
               </c:when>
               <c:otherwise>
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=description&desc=true"  >
                             <B>Tasks          </B>
                    </x:event>
               </c:otherwise>
              </c:choose>
           <c:if test="${!empty view.sort && view.sort == 'description'}">
              <c:choose>
                <c:when test="${view.desc}">
                    <img src="<%= request.getContextPath() %>/common/table/sortup.gif">
                </c:when>
                <c:otherwise>
                    <img src="<%= request.getContextPath() %>/common/table/sortdown.gif">
                </c:otherwise>
              </c:choose>
          </c:if>

            </td>
            <td align="left" colspan="1"  class="calendarFooter" >
             <c:choose>
               <c:when test="${view.desc}" >
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=assigner"  >
                                      <b>Assigned By</b></x:event>
               </c:when>
               <c:otherwise>
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=assigner&desc=true"  >
                     <b>Assigned By</b></x:event>
               </c:otherwise>
              </c:choose>
               <c:if test="${!empty view.sort && view.sort == 'assigner'}">
              <c:choose>
                <c:when test="${view.desc}">
                    <img src="<%= request.getContextPath() %>/common/table/sortup.gif">
                </c:when>
                <c:otherwise>
                    <img src="<%= request.getContextPath() %>/common/table/sortdown.gif">
                </c:otherwise>
              </c:choose>
          </c:if>
            </td>
            <td align="left" colspan="1"  class="calendarFooter" >
             <c:choose>
               <c:when test="${view.desc}" >
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=assigneeFirst"  >
             <b>To</b> </x:event>
               </c:when>
               <c:otherwise>
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=assigneeFirst&desc=true"  >
             <b>To</b> </x:event>
               </c:otherwise>
              </c:choose>
               <c:if test="${!empty view.sort && view.sort == 'assigneeFirst'}">
              <c:choose>
                <c:when test="${view.desc}">
                    <img src="<%= request.getContextPath() %>/common/table/sortup.gif">
                </c:when>
                <c:otherwise>
                    <img src="<%= request.getContextPath() %>/common/table/sortdown.gif">
                </c:otherwise>
              </c:choose>
          </c:if>
            </td>
            <td align="left" colspan="1"  class="calendarFooter" >
             <c:choose>
               <c:when test="${view.desc}" >
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=dueDate"  >
             <b>Deadline</b></x:event>
               </c:when>
               <c:otherwise>
                    <x:event name="${view.absoluteName}"  type="sort" param="sort=dueDate&desc=true"  >
               <b>Deadline</b></x:event>

               </c:otherwise>
              </c:choose>
              <c:if test="${!empty view.sort && view.sort == 'dueDate'}">
              <c:choose>
                <c:when test="${view.desc}">
                    <img src="<%= request.getContextPath() %>/common/table/sortup.gif">
                </c:when>
                <c:otherwise>
                    <img src="<%= request.getContextPath() %>/common/table/sortdown.gif">
                </c:otherwise>
              </c:choose>
          </c:if>
            </td>

            <Td class="calendarFooter" colspan="1">
              <input type="checkbox" onClick="taskcategorytable_toggle(this)" />
            </td>
        </tr>



        <c:forEach items="${view.tasks}" var="task"  >
            <tr class="row">
                <td align="left" colspan="1" class="calendarRow">
                    <c:if test="${task.completed}" > <FONT COLOR="#3130FF">   </c:if>
                    <c:if test="${!task.completed}" > <FONT COLOR="#FF0000">   </c:if>
                    &#149;</FONT>
                </td>
                <td align="left" colspan="1"  class="calendarRow">
                  <a href="" onClick="viewTask('<c:out value="${task.id}"/>','<c:out value="${task.assigneeId}" />');return false;" >
                    <c:out value="${task.description}" />
                  </a>
                </td>
                <td align="left" colspan="1" class="calendarRow">
                    <c:if test="${!task.completed && task.assigneeId==view.widgetManager.user.id}" >
                    <a href="<c:out value="${completeUrl}"/>&<%=TaskListing.PARAMETER_EVENT_COMPLETE_TASKID%>=<c:out value="${task.id }"/>">
                    <IMG src="<c:url value="/ekms/"/>images/checkbox.gif" border="0" > </a></c:if>
                </td>
                <td align="left" colspan="1" class="calendarRow">
                    <c:out value="${task.assigner}" ></c:out>
                </td>
                <td align="left" colspan="1" class="calendarRow">
                   <c:out value="${task.assigneeFirst}" /> <c:out value="${task.assigneeLast}" ></c:out>
                </td>
                <td align="left" colspan="1" class="calendarRow">
                    <fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeLong}" />
                </td>

                <td colspan="1" class="calendarRow">
                    <input
                                        type="checkbox"
                                        name="<c:out value="${widget.sel.absoluteName}" />"
                                        value="<c:out value="${task.id}"/>"
                                    />
                </td>
            </tr>


        </c:forEach>


        <tr>
            <td class="calendarRow" align="right" colspan="7">
           <input class="button" type="submit" value="<fmt:message key='project.label.addTask'/>" onClick="document.location = '<c:url value="/ekms/"/>calendar/todotaskform.jsp';return false;"/>
 <x:display name="${widget.deleteButton.absoluteName}" ></x:display>

            </td>
        </tr>

 <c:if test="${view.pageCount > 1}" >
  <tfooter>
  <tr>
    <td colspan="7" align="center"  class="calendarRow">
        <c:set var="pageCount" value="${view.pageCount}"/>
        <c:if test="${view.currentPage > 1}">
        [<x:event name="${view.absoluteName}" type="page" param="page=${view.currentPage - 1}">&lt;</x:event>]
        </c:if>

        Page
        <select id="<c:out value='${view.absoluteName}page'/>" name="<c:out value='${view.absoluteName}page'/>" onchange="goToPage('<c:out value='${view.absoluteName}'/>')">
        <c:set var="end" value="${pageCount}" />
            <c:forEach begin="1" end="${end}" var="pg">
              <option<c:if test='${pg == view.currentPage}'> selected</c:if>><c:out value="${pg}" /></option>
            </c:forEach>
         </select>
        <script>
        <!--
            function goToPage(id) {
                var selectbox = document.getElementById(id + 'page');
                var page = selectbox.options[selectbox.selectedIndex].text;
                location.href='<%= request.getRequestURI() %>?cn=' + id + '&et=page&page=' + page;
            }
        //-->
        </script>

        <c:if test="${view.currentPage < pageCount}">
        [<x:event name="${view.absoluteName}" type="page" param="page=${view.currentPage + 1}">&gt;</x:event>]
        </c:if>
    </td>
  </tr>


</tfooter>
 </c:if>

 <tr>
     <TD colspan="7" class="calendarRow">
	  <FONT COLOR="#FF0000">&#149;</FONT>&nbsp;Incomplete&nbsp;&nbsp;&nbsp;
	  <FONT COLOR="#3130FF">&#149;</FONT>&nbsp;Completed
	 </td>
    </tr><jsp:include page="../form_footer.jsp" flush="true"/>

<%--
</table>
--%>
