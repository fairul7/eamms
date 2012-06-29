<%@ page import="kacang.ui.Event,
                 com.tms.collab.calendar.ui.CalendarView,
                 com.tms.collab.emeeting.ui.MeetingFormView,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.emeeting.AgendaItem,
                 com.tms.collab.calendar.ui.UserUtil,
                 kacang.services.security.User,
                 com.tms.collab.calendar.ui.CalendarEventView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>


<c-rt:set var="deleted" value="<%=MeetingFormView.FORWARD_DELETE_SUCCESSFUL%>" />

<c:if test="${forward.name == deleted}">
   <script>
        alert("E-Meeting deleted successfully!");
        document.location = "<%= response.encodeURL(request.getRequestURI()) %>?cn=calendarPage.calendarView&et=view&view=daily";
   </script>
</c:if>


<%
    String url = response.encodeURL(request.getRequestURI());
    url = url.substring(0, url.indexOf('.'));
    url += MeetingFormView.DEFAULT_ACTION_ITEM_PREFIX + ".jsp?" + CalendarView.PARAMETER_KEY_EVENTID + "=";
%>
<c:set var="meeting" value="${widget.meeting}"/>
<c:set var="widget" value="${widget}"/>
<c:if test="${meeting!=null}" >
<c:set var="actionItemsUrl"><%= url %><c:out value="${meeting.eventId}"/></c:set>
</c:if>
<c:set var="deleteUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=<%=MeetingFormView.EVENT_TYPE_DELETE%>
</c:set>


<script language="javascript">
    function openWindow(url, windowName)
    {
        window.open(url, windowName, "height=550,width=640,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");
    }
</script>
<table width="100%" class="emeetingBackground" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" cellpadding="3" cellspacing="1">
                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.title'/>&nbsp;</td>
                    <td width="70%" class="emeetingRow"><c:out value="${meeting.event.title}" /> <c:if test="${meeting.event.recurrence}" >(<fmt:message key='emeeting.label.recurrence'/>)</c:if></td>
                </tr>
                <c:if test="${! empty meeting.event.location}">
                    <tr>
                        <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.location'/>&nbsp;</td>
                        <td width="70%" class="emeetingRow"><c:out value="${meeting.event.location}" /></td>
                    </tr>
                </c:if>
                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.starts'/>&nbsp;</td>
                    <td width="70%" class="emeetingRow">
                      <c:choose>
                        <c:when test="${!meeting.event.allDay}" >
                            <fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDatetimeLong}"/></td>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatDate value="${meeting.event.startDate}" pattern="${globalDateLong}"/></td>
                        </c:otherwise>
                     </c:choose>
                </tr>
                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.ends'/>&nbsp;</td>
                    <td width="70%" class="emeetingRow">
                        <c:choose>
                        <c:when test="${!meeting.event.allDay}" >
                        <fmt:formatDate value="${meeting.event.endDate}" pattern="${globalDatetimeLong}"/></td>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatDate value="${meeting.event.endDate}" pattern="${globalDateLong}"/></td>
                        </c:otherwise>
                        </c:choose>
                </tr>
                <c:if test="${meeting.event.allDay}" >
                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.duration'/></td>
                    <td width="70%" class="emeetingRow"><fmt:message key='emeeting.label.allDay'/>                    </td>

                </c:if>
                <c:if test="${!empty meeting.secretary}" >
                    <c:set var="secretary" value="${meeting.secretary}"></c:set>
                    <tr>
                        <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.secretary'/>&nbsp;</td>
                        <%
                            User user = UserUtil.getUser((String)pageContext.getAttribute("secretary"));
                            if(user!=null)pageContext.setAttribute("secretaryName",user.getName());
                        %>
                        <td width="70%" class="emeetingRow"><c:out value="${secretaryName}"escapeXml="false"/></td>
                    </tr>
                </c:if>
                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.classification'/>&nbsp;</td>
                    <td width="70%" class="emeetingRow">
                        <c:set var="cla" value="${meeting.event.classification}"/>
                        <c:if test="${cla == 'pub'}" > <fmt:message key='emeeting.label.public'/> </c:if>
                        <c:if test="${cla == 'pri'}" > <fmt:message key='emeeting.label.private'/> </c:if>
                        <c:if test="${cla == 'con'}" > <fmt:message key='emeeting.label.confidential'/> </c:if>
                    </td>
                </tr>
                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.compulsoryAttendees'/>&nbsp;</td>
                    <td width="70%" class="emeetingRow">
                        <c:set var="count" value="0"/>
                        <c:forEach var="att" items="${meeting.event.attendees}">
                            <c:if test="${att.compulsory}" >
                                <c:if test="${count > 0}">,</c:if>
                                <c:out value="${att.name}" /> (<c:out value="${att.status}"/>)
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
					<td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.documents'/>&nbsp;</td>
					<td width="70%" class="emeetingRow"><x:display name="${widget.fileListing.absoluteName}" /></td>
				</tr>
                <c:if test="${! empty meeting.event.description}">
					<c:set var="des" value="${meeting.event.description}"/>
					<%
						String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
                        translated = StringUtils.replace(translated, "\r", "<br>");
					%>
					<tr>
						<td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.description'/>&nbsp;</td>
						<td width="70%" class="emeetingRow"><%= translated %></td>
					</tr>
                </c:if>
                <c:if test="${! empty meeting.event.agenda}">
                    <c:set value="${meeting.event.agenda}" var="agenda" />
					<%
						String translated = StringUtils.replace((String)pageContext.getAttribute("des"), "\n", "<br>");
                        translated = StringUtils.replace(translated, "\r", "<br>");
					%>
                    <tr>
                        <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.agendaNotes'/>&nbsp;</td>
                        <td width="70%" class="emeetingRow"><%= translated %></td>
                    </tr>
                </c:if>

                <c:if test="${!empty meeting.event.resources}" >
                <tr>
                    <td class="calendarRowLabel" align="right" valign="top"><fmt:message key='emeeting.label.resources'/> </td> <td class="calendarRow" align="left">
                        <c:set var="count" value="0"/>

                        <c:forEach items="${meeting.event.resources}" var="resource"  >
                        <c:if test="${count>0}" >,</c:if>
                        <c:out value="${resource.name}" />(<c:out value="${resource.status}" />)
                        <c:set var="count" value="${count+1}"/>
                        </c:forEach>

                    </td>
                </tr>
                </c:if>


                <tr>
                    <td width="30%" nowrap align="right" valign="top" class="emeetingRowLabel"><fmt:message key='emeeting.label.lastModified'/>&nbsp;</td>
                    <td width="70%" class="emeetingRow"><fmt:formatDate value="${meeting.event.lastModified}" pattern="${globalDatetimeLong}"/> <fmt:message key='emeeting.label.by'/> <c:out value="${meeting.event.lastModifiedName}"/></td>
                </tr>
                <!-- Action Items -->
                <c:if test="${! empty meeting.meetingAgenda}">
                    <tr><td class="emeetingRow" colspan="2">&nbsp;</td></tr>
                    <tr><td class="emeetingHeader" colspan="2"><b><fmt:message key='emeeting.label.minutesofMeeting'/></b></td></tr>
                    <tr>
                        <td class="emeetingRow" colspan="2">
                            <table width="95%" align="center">
                                <c:set var="parentCount" value="1"/>
                                <c:forEach var="parent" items="${meeting.meetingAgenda}">
                                    <tr>
                                        <td class="emeetingRowLabel" width="1%" nowrap><c:out value="${parentCount}"/>.0.</td>
                                        <td class="emeetingRowLabel"><c:out value="${parent.title}"/></td>
                                    </tr>
                                    <c:if test="${! empty parent.notes}">
                                        <%
                                            String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("parent")).getNotes(), "\n", "<br>");
                                            pageContext.setAttribute("content", content);
                                        %>
                                        <tr>
                                            <td class="emeetingRow" width="1%" nowrap>&nbsp;</td>
                                            <td class="emeetingRow"><font class="emeetingSmall"><fmt:message key='emeeting.label.minutes'/>:</font><br><c:out value="${content}" escapeXml="false"/></td>
                                        </tr>
                                    </c:if>
                                    <c:if test="${! empty parent.action}">
                                        <%
                                            String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("parent")).getAction(), "\n", "<br>");
                                            pageContext.setAttribute("content", content);
                                        %>
                                        <tr>
                                            <td class="emeetingRow" width="1%" nowrap>&nbsp;</td>
                                            <td class="emeetingRow"><br><font class="emeetingSmall"><fmt:message key='emeeting.label.actions'/>:</font><br><c:out value="${content}" escapeXml="false"/></td>
                                        </tr>
                                    </c:if>


                                    <c:if test="${! empty parent.tasks}">
                                        <c:forEach var="task" items="${parent.tasks}">

                                        <tr>
                                            <td class="emeetingRow" width="1%" nowrap>&nbsp;</td>
                                            <td class="emeetingRow">
                                                <br><font class="emeetingSmall"><fmt:message key='emeeting.label.task'/>:</font><br>
                                                <x:event name="${widget.parent.parent.absoluteName}" type="select" param="eventId=${task.id}"><c:out value="${task.title}" /></x:event>
                                                <br>
                                                <c:set var="i" value="1"></c:set><fmt:message key='emeeting.label.assignedto'/>
                                                        <c:forEach var="att" items="${task.attendees}" >
                                                        <c:out value="${att.name}" /><c:if test="${!empty task.attendees[i]}">,</c:if>
                                                        <c:set var="i" value="${i+1}"></c:set>
                                                   </c:forEach>
                                                   <br>
                                               <c:choose>
                                                    <c:when test="${task.completed}"><fmt:message key='emeeting.label.completed'/></c:when>
                                                    <c:otherwise><fmt:message key='emeeting.label.dueon'/> <fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeLong}"/></c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        </c:forEach>
                                    </c:if>



                                    <c:if test="${! empty parent.children}">
                                        <tr>
                                            <td class="emeetingRow" colspan="2">
                                                <table align="right" width="95%">
                                                    <c:set var="childCount" value="1"/>
                                                    <c:forEach var="child" items="${parent.children}">
                                                        <c:set var="item" value="${child.value}"/>
                                                        <tr>
                                                            <td class="emeetingRow" colspan="2"><hr size="1" style="border: dotted 1px silver"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="emeetingRowLabel" width="1%" nowrap><c:out value="${parentCount}.${childCount}"/>.</td>
                                                            <td class="emeetingRowLabel"><c:out value="${item.title}"/></td>
                                                        </tr>
                                                        <c:if test="${! empty item.notes}">
                                                            <%
                                                                String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("item")).getNotes(), "\n", "<br>");
                                                                pageContext.setAttribute("content", content);
                                                            %>
                                                            <tr>
                                                                <td class="emeetingRow" width="1%" nowrap>&nbsp;</td>
                                                                <td class="emeetingRow"><font class="emeetingSmall"><fmt:message key='emeeting.label.minutes'/>:</font><br><c:out value="${content}" escapeXml="false"/></td>
                                                            </tr>
                                                        </c:if>
                                                        <c:if test="${! empty item.action}">
                                                            <%
                                                                String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("item")).getAction(), "\n", "<br>");
                                                                pageContext.setAttribute("content", content);
                                                            %>
                                                            <tr>
                                                                <td class="emeetingRow" width="1%" nowrap>&nbsp;</td>
                                                                <td class="emeetingRow"><br><font class="emeetingSmall"><fmt:message key='emeeting.label.actions'/>:</font><br><c:out value="${content}" escapeXml="false"/></td>
                                                            </tr>
                                                        </c:if>

                                                        <c:if test="${! empty child.value.tasks}">

                                                        <c:forEach var="task" items="${child.value.tasks}" >
                                                            <tr>
                                                                <td class="emeetingRow" width="1%" nowrap>&nbsp;</td>
                                                                <td class="emeetingRow">
                                                                    <br><font class="emeetingSmall"><fmt:message key='emeeting.label.task'/>:</font><br>
                                                                    <x:event name="${widget.parent.parent.absoluteName}" type="select" param="eventId=${task.id}"><c:out value="${task.title}" /></x:event>
                                                                    <br>
                                                                    <c:set var="i" value="1"></c:set><fmt:message key='emeeting.label.assignedto'/>
                                                                    <c:forEach var="att" items="${task.attendees}" >
                                                                        <c:out value="${att.name}" /><c:if test="${!empty task.attendees[i]}">,</c:if>
                                                                        <c:set var="i" value="${i+1}"></c:set>
                                                                   </c:forEach>
                                                                   <br>
                                                                    <c:choose>
                                                                        <c:when test="${task.completed}"><fmt:message key='emeeting.label.completed'/></c:when>
                                                                        <c:otherwise><fmt:message key='emeeting.label.dueon'/> <fmt:formatDate value="${task.dueDate}" pattern="${globalDatetimeLong}"/></c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                            </c:forEach>
                                                        </c:if>


                                                        <c:set var="childCount" value="${childCount + 1}"/>
                                                    </c:forEach>
                                                </table>
                                            </td>
                                        </tr>
                                    </c:if>
                                    <c:set var="parentCount" value="${parentCount + 1}"/>
                                    <tr>
                                        <td class="emeetingRow" colspan="2"><hr size="1" style="border: dotted 1px silver"></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                </c:if>
                <!-- Related Meetings -->
                <c:if test="${! empty widget.relatedMeetings}">
                    <tr><td class="emeetingRow" colspan="2">&nbsp;</td></tr>
                    <tr><td class="emeetingHeader"colspan="2"><b><fmt:message key='emeeting.label.relatedMeetings'/></b></td></tr>
                    <tr>
                        <td class="emeetingRow" colspan="2">
                            <table width="95%" align="center">
                                <c:set var="prefix"><%= CalendarView.PARAMETER_KEY_EVENTID %></c:set>
                                <c:forEach var="item" items="${widget.relatedMeetings}">
                                    <c:if test="${widget.meeting.eventId != item.eventId}">
                                        <tr><td class="emeetingRow"><li><x:event name="${widget.parent.parent.absoluteName}" type="<%= CalendarView.PARAMETER_KEY_EVENT_SELECT %>" param="${prefix}=${item.eventId}"><c:out value="${item.title}"/></x:event> (<fmt:formatDate dateStyle="full" value="${item.startDate}"/>)</li></td></tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${empty widget.relatedMeetings[1]}">
                                    <tr><td class="emeetingRow"><li><fmt:message key='emeeting.label.norelatedmeetings'/></li></td></tr>
                                </c:if>
                            </table>
                        </td>
                    </tr>
                </c:if>
<%--
               <c:if test="${!widget.parent.hiddenAction}" >


                <tr>
                    <td class="emeetingRow">&nbsp;</td>
                    <td class="emeetingRow" >&nbsp;
                        <input type="button" value="Print" class="button" onClick="window.open('<c:url value="/ekms/calendar/emeetingPrint.jsp?eventId="/><c:out value="${meeting.eventId}"/>');"/>
                        <c-rt:set var="EDIT" value="<%=new Integer(CalendarEventView.EDIT)%>"/>
                        <c:if test="${((widget.userId == meeting.event.userId) || (meeting.secretary == widget.userId)) && (widget.state!=EDIT)}" >
                            <input type="button" value="Edit Agenda Items" onClick="openWindow('<c:out value="${actionItemsUrl}"/>', 'meetingActionWindow');" class="button">
                            <input type="button" value="Email" class="button" onClick="location= '<c:url value="/ekms/calendar/externalemail.jsp?meetingId=${meeting.eventId}"/>' ;"/>    
                            <c:if test="${widget.editable}" >
                                <input type="button" value="Edit" class="button"  onClick="document.location = '<c:url value="/ekms/calendar/editemeetingform.jsp?eventId="/><c:out value="${meeting.eventId}"/>'"/>
                            </c:if>
                            <c:if test="${widget.deletable}">
                                <input type="button" class="button" value="Delete" onClick="if(confirm('Click OK to confirm')){document.location = '<c:out value="${deleteUrl}" />'}"/>
                            </c:if>
                        </c:if>
                    </td>
                </tr>
                </c:if>
--%>
            </table>
        </td>
    </tr>
</table>
