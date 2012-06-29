<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.emeeting.ui.MeetingFormView,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.emeeting.AgendaItem,
                 java.util.Date,
                 com.tms.collab.taskmanager.model.Task,
                 kacang.services.security.SecurityService,
                 kacang.Application,
                 kacang.model.DaoQuery,
                 kacang.model.operator.OperatorIn,
                 kacang.model.operator.DaoOperator,
                 java.util.Collection,
                 java.util.Iterator,
                 kacang.services.security.User,
                 com.tms.collab.calendar.ui.UserUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="meeting" value="${widget.meeting}"/>
<c:set var="widget" value="${widget}"/>

<table width="95%" class="emeetingPrintOutline" cellpadding="0" cellspacing="0" align="center">
    <tr>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table width="100%" cellpadding="3" cellspacing="0" border="1">
                            <tr><td colspan="2" class="emeetingPrintHeader"><c:out value="${meeting.event.title}" /> <c:if test="${meeting.event.recurrence}" >(<fmt:message key='emeeting.label.recurrence'/>)</c:if></td></tr>
                            <c:if test="${! empty meeting.event.location}">
                                <tr>
                                    <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.location'/>&nbsp;</td>
                                    <td width="70%" class="emeetingPrintRow"><c:out value="${meeting.event.location}" /></td>
                                </tr>
                            </c:if>
                            <tr>
                                <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.starts'/>&nbsp;</td>
                                <td width="70%" class="emeetingPrintRow"><fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDatetimeLong}"/></td>
                            </tr>
                            <tr>
                                <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.ends'/>&nbsp;</td>
                                <td width="70%" class="emeetingPrintRow"><fmt:formatDate value="${meeting.event.endDate}" pattern="${globalDatetimeLong}"/></td>
                            </tr>
                            <c:if test="${!empty meeting.secretary}" >
                            <c:set var="secretary" value="${meeting.secretary}"></c:set>
                            <tr>
                            <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.secretary'/>&nbsp;</td>
                            <%
                            User user = UserUtil.getUser((String)pageContext.getAttribute("secretary"));
                            if(user!=null)pageContext.setAttribute("secretaryName",user.getName());
                            %>
                            <td width="70%" class="emeetingPrintRow"><c:out value="${secretaryName}"escapeXml="false"/></td>
                            </tr>
                            </c:if>

                            <tr>
                                <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.classification'/>&nbsp;</td>
                                <td width="70%" class="emeetingPrintRow">
                                    <c:set var="cla" value="${meeting.event.classification}"/>
                                    <c:if test="${cla == 'pub'}" > <fmt:message key='emeeting.label.public'/> </c:if>
                                    <c:if test="${cla == 'pri'}" > <fmt:message key='emeeting.label.private'/> </c:if>
                                    <c:if test="${cla == 'con'}" > <fmt:message key='emeeting.label.confidential'/> </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.compulsoryAttendees'/>&nbsp;</td>
                                <td width="70%" class="emeetingPrintRow">
                                    <c:set var="count" value="0"/>
                                    <c:forEach var="att" items="${meeting.event.attendees}">
                                        <c:if test="${att.compulsory}" >
                                            <c:if test="${count > 0}">,</c:if>
                                            <c:out value="${att.name}" />
                                            <c:set var="count" value="${count + 1}"/>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${count == 0}"> - </c:if>
                                </td>
                            </tr>
                             <c:set var="count" value="0"/>
                        <c:forEach var="att" items="${meeting.event.attendees}">
                            <c:if test="${not att.compulsory}" >
                                <c:if test="${count == 0 }" >
                                    <tr>
                                        <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.optionalAttendees'/>&nbsp;</td>
                                        <td width="70%" class="emeetingRow">
                                </c:if>
                                <c:if test="${count > 0}">,</c:if>
                                <c:out value="${att.name}" />  (<c:out value="${att.status}"/>)
                                <c:set var="count" value="${count + 1}"/>
                            </c:if>
                        </c:forEach>
                        <c:if test="${count> 0}"> </td>
                </tr> </c:if>

                            <tr>
                                <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.documents'/>&nbsp;</td>
                                <td width="70%" class="emeetingPrintRow"><x:display name="${widget.fileListing.absoluteName}"/></td>
                            </tr>

                            <c:if test="${! empty meeting.event.description}">
                                <c:set var="des" value="${meeting.event.description}" />
                   <%
                       String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
                       pageContext.setAttribute("des", translated);
                                    %><tr>
                                    <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.description'/>&nbsp;</td>
                                    <td width="70%" class="emeetingPrintRow"><c:out value="${des}"escapeXml="false"/></td>
                                </tr>
                            </c:if>
                            <c:if test="${! empty meeting.event.agenda}">
                               <c:set value="${meeting.event.agenda}" var="agenda" />
                   <%
                       String translated = StringUtils.replace((String)pageContext.getAttribute("agenda"), "\n", "<br>");
                       pageContext.setAttribute("agenda", translated);
                     %>
 <tr>
                                    <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.agendaNotes'/>&nbsp;</td>
                                    <td width="70%" class="emeetingPrintRow"><c:out value="${agenda}" escapeXml="false"/></td>
                                </tr>
                            </c:if>
                            <tr>
                                <td width="30%" nowrap align="right" valign="top" class="emeetingPrintRowLabel"><fmt:message key='emeeting.label.lastModified'/>&nbsp;</td>
                                <td width="70%" class="emeetingPrintRow"><fmt:formatDate value="${meeting.event.lastModified}" pattern="${globalDatetimeLong}"/> <fmt:message key='emeeting.label.by'/> <c:out value="${meeting.event.lastModifiedName}"/></td>
                            </tr>
                            <!-- Action Items -->
                            <c:if test="${! empty meeting.meetingAgenda}">
                                <tr><td class="emeetingPrintHeader" colspan="2"><b><fmt:message key='emeeting.label.minutesofMeeting'/></b></td></tr>
                                <tr>
                                    <td class="emeetingPrintRow" colspan="2">
                                        <table width="95%" align="center">
                                            <c:set var="parentCount" value="1"/>
                                            <% SecurityService service = (SecurityService) Application.getInstance().getService(SecurityService.class); %>
                                            <c:forEach var="parent" items="${meeting.meetingAgenda}">
                                                <tr>
                                                    <td class="emeetingPrintRowLabel" width="1%" nowrap><c:out value="${parentCount}"/>.0.</td>
                                                    <td class="emeetingPrintRowLabel"><c:out value="${parent.title}"/></td>
                                                </tr>
                                                <c:if test="${! empty parent.notes}">
                                                    <%
                                                        String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("parent")).getNotes(), "\n", "<br>");
                                                        pageContext.setAttribute("content", content);
                                                    %>
                                                    <tr>
                                                        <td class="emeetingPrintRow" width="1%" nowrap>&nbsp;</td>
                                                        <td class="emeetingPrintRow"><font class="emeetingPrintSmall"><fmt:message key='emeeting.label.minutes'/>:<br></font><c:out value="${content}" escapeXml="false"/></td>
                                                    </tr>
                                                </c:if>
                                                <c:if test="${! empty parent.action}">
                                                    <%
                                                        String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("parent")).getAction(), "\n", "<br>");
                                                        pageContext.setAttribute("content", content);
                                                    %>
                                                    <tr>
                                                        <td class="emeetingPrintRow" width="1%" nowrap>&nbsp;</td>
                                                        <td class="emeetingPrintRow"><font class="emeetingPrintSmall"><br><fmt:message key='emeeting.label.actions'/>:<br></font><c:out value="${content}" escapeXml="false"/></td>
                                                    </tr>
                                                </c:if>
                                                <c:if test="${! empty parent.task}">
                                                    <c:set var="task" value="${parent.task}"/>
                                                    <tr>
                                                        <td class="emeetingPrintRow" width="1%" nowrap>&nbsp;</td>
                                                        <td class="emeetingPrintRow">
                                                            <font class="emeetingPrintSmall"><br><fmt:message key='emeeting.label.task'/>:</font><br>
                                                            <c:out value="${task.title}" />
                                                            <br>
                                                            <%
                                                                Task task = (Task) pageContext.getAttribute("task");
                                                                if(task.getAttendeeMap().size() > 0)
                                                                {
                                                                    DaoQuery query = new DaoQuery();
                                                                    query.addProperty(new OperatorIn("id", task.getAttendeeMap().keySet().toArray(), DaoOperator.OPERATOR_AND));
                                                                    Collection list = service.getUsers(query, 0, -1, "username", false);
                                                                    if(list.size() > 0)
                                                                    {
                                                            %><fmt:message key='emeeting.label.assignees'/>:<br>
                                                            <%
                                                                    }
                                                                    for(Iterator i = list.iterator(); i.hasNext();)
                                                                    {
                                                                        User user = (User) i.next();
                                                            %>
                                                            <li><%= user.getProperty("firstName") %> <%= user.getProperty("lastName") %></li><br>
                                                            <%
                                                                    }
                                                                }
                                                            %><fmt:message key='emeeting.label.status'/>
                                                            <c:choose>
                                                                <c:when test="${parent.task.completed}"><fmt:message key='emeeting.label.completed'/></c:when>
                                                                <c:otherwise><fmt:message key='emeeting.label.dueon'/> <fmt:formatDate value="${parent.task.dueDate}" pattern="${globalDatetimeLong}"/></c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                                <c:if test="${! empty parent.children}">
                                                    <tr>
                                                        <td class="emeetingPrintRow" colspan="2">
                                                            <table align="right" width="95%">
                                                                <c:set var="childCount" value="1"/>
                                                                <c:forEach var="child" items="${parent.children}">
                                                                    <c:set var="item" value="${child.value}"/>
                                                                    <tr>
                                                                        <td class="emeetingPrintRow" colspan="2"><hr size="1" style="border: dotted 1px silver"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td class="emeetingPrintRowLabel" width="1%" nowrap><c:out value="${parentCount}.${childCount}"/>.</td>
                                                                        <td class="emeetingPrintRowLabel"><c:out value="${item.title}"/></td>
                                                                    </tr>
                                                                    <c:if test="${! empty item.notes}">
                                                                        <%
                                                                            String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("item")).getNotes(), "\n", "<br>");
                                                                            pageContext.setAttribute("content", content);
                                                                        %>
                                                                        <tr>
                                                                            <td class="emeetingPrintRow" width="1%" nowrap>&nbsp;</td>
                                                                            <td class="emeetingPrintRow"><font class="emeetingPrintSmall"><fmt:message key='emeeting.label.minutes'/>:<br></font><c:out value="${content}" escapeXml="false"/></td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:if test="${! empty item.action}">
                                                                        <%
                                                                            String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("item")).getAction(), "\n", "<br>");
                                                                            pageContext.setAttribute("content", content);
                                                                        %>
                                                                        <tr>
                                                                            <td class="emeetingPrintRow">&nbsp;</td>
                                                                            <td class="emeetingPrintRow"><font class="emeetingPrintSmall"><br><fmt:message key='emeeting.label.actions'/>:<br></font><c:out value="${content}" escapeXml="false"/></td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:if test="${! empty child.value.task}">
                                                                        <c:set var="task" value="${child.value.task}"/>
                                                                        <tr>
                                                                            <td class="emeetingPrintRow" width="1%" nowrap>&nbsp;</td>
                                                                            <td class="emeetingPrintRow">
                                                                                <font class="emeetingPrintSmall"><br><fmt:message key='emeeting.label.task'/>:</font><br>
                                                                                <c:out value="${task.title}" />
                                                                                <br>
                                                                                <%
                                                                                    Task task = (Task) pageContext.getAttribute("task");
                                                                                    if(task.getAttendeeMap().size() > 0)
                                                                                    {
                                                                                        DaoQuery query = new DaoQuery();
                                                                                        query.addProperty(new OperatorIn("id", task.getAttendeeMap().keySet().toArray(), DaoOperator.OPERATOR_AND));
                                                                                        Collection list = service.getUsers(query, 0, -1, "username", false);
                                                                                        if(list.size() > 0)
                                                                                        {
                                                                                %><fmt:message key='emeeting.label.assignees'/>:<br>
                                                                                <%
                                                                                        }
                                                                                        for(Iterator i = list.iterator(); i.hasNext();)
                                                                                        {
                                                                                            User user = (User) i.next();
                                                                                %>
                                                                                <li><%= user.getProperty("firstName") %> <%= user.getProperty("lastName") %></li><br>
                                                                                <%
                                                                                        }
                                                                                    }
                                                                                %><fmt:message key='emeeting.label.status'/>:
                                                                                <c:choose>
                                                                                    <c:when test="${child.value.task.completed}"><fmt:message key='emeeting.label.completed'/></c:when>
                                                                                    <c:otherwise><fmt:message key='emeeting.label.dueon'/> <fmt:formatDate value="${child.value.task.dueDate}" pattern="${globalDatetimeLong}"/></c:otherwise>
                                                                                </c:choose>
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:set var="childCount" value="${childCount + 1}"/>
                                                                </c:forEach>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                                <c:set var="parentCount" value="${parentCount + 1}"/>
                                                <tr>
                                                    <td class="emeetingPrintRow" colspan="2"><hr size="1" style="border: dotted 1px silver"></td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>
                            <!-- Related Meetings -->
                           <%-- <c:if test="${! empty widget.relatedMeetings}">
                                <tr><td class="emeetingPrintRow" colspan="2">&nbsp;</td></tr>
                                <tr><td class="emeetingPrintHeader"><b><fmt:message key='emeeting.label.relatedMeetings'/></b></td></tr>
                                <tr>
                                    <td class="emeetingPrintRow">
                                        <table width="95%" align="center">
                                            <c:set var="prefix"><%= CalendarView.PARAMETER_KEY_EVENTID %></c:set>
                                            <c:forEach var="item" items="${widget.relatedMeetings}">
                                                <c:if test="${widget.meeting.eventId != item.eventId}">
                                                    <tr><td class="emeetingPrintRow"><li><c:out value="${item.title}"/> <i>(<fmt:formatDate value="${item.startDate}" pattern="d MMM yyyy, h:mm a"/>)</i></li></td></tr>
                                                </c:if>
                                            </c:forEach>
                                        </table>
                                    </td>
                                </tr>
                            </c:if>--%>
                            <c-rt:set var="date" value="<%= new Date() %>"/>
                            <tr><td class="emeetingPrintHeader" colspan="2" align="right"><fmt:message key='emeeting.label.printedon'/>: <fmt:formatDate value="${date}" pattern="${globalDatetimeLong}"/></td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<br>
<%--
<table width="95%" cellpadding="0" cellspacing="0" align="center">
    <tr><td align="right"><input type="button" class="button" value="Print" onClick="print();"></td></tr>
</table>--%>
<script>
    print();
</script>
