<%@ page import="com.tms.collab.emeeting.ui.MeetingActionItemsForm,
                 org.apache.commons.lang.StringUtils,
                 com.tms.collab.emeeting.AgendaItem,
                 kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>


<c:set var="moveDownUrl" >
<%=response.encodeURL(request.getRequestURI())%>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=movedown
</c:set>


<c:set var="moveUpUrl" >
<%=response.encodeURL(request.getRequestURI())%>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${widget.absoluteName}"/>&<%= Event.PARAMETER_KEY_EVENT_TYPE %>=moveup
</c:set>





<c:if test="${! empty widget.eventId}">
    <jsp:include page="../form_header.jsp" flush="true"/>
    <table width="100%" cellspacing="1" cellpadding="3" width="100%">
        <tr><td class="emeetingHeader" colspan="2"><c:out value="${widget.meeting.title}"/></td></tr>
        <tr>
            <td class="emeetingRowLabel" valign="top" align="right" width="30%" nowrap><fmt:message key='emeeting.label.start'/>&nbsp;</td>
            <td class="emeetingRow" valign="top" width="70%"><fmt:formatDate value="${widget.meeting.event.startDate}" pattern="${globalDatetimeLong}"/></td>
        </tr>
        <tr>
            <td class="emeetingRowLabel" valign="top" align="right" width="30%" nowrap><fmt:message key='emeeting.label.end'/>&nbsp;</td>
            <td class="emeetingRow" valign="top" width="70%"><fmt:formatDate value="${widget.meeting.event.endDate}" pattern="${globalDatetimeLong}"/></td>
        </tr>
<%--
        <tr>
            <td class="emeetingRowLabel" valign="top" align="right" width="30%" nowrap><fmt:message key='emeeting.label.classification'/>&nbsp;</td>
            <td class="emeetingRow" valign="top" width="70%">
               <c:set var="cla" value="${widget.meeting.event.classification}"/>
               <c:if test="${cla == 'pub'}" > <fmt:message key='emeeting.label.public'/> </c:if>
               <c:if test="${cla == 'pri'}" > <fmt:message key='emeeting.label.private'/> </c:if>
               <c:if test="${cla == 'con'}" > <fmt:message key='emeeting.label.confidential'/> </c:if>
            </td>
        </tr>
--%>
        <c:set var="des" value="${widget.meeting.event.description}"/>
        <c:if test="${! empty des}">
            <tr>
                <td class="emeetingRowLabel" valign="top" align="right" width="30%" nowrap><fmt:message key='emeeting.label.description'/>&nbsp;</td>
                <td class="emeetingRow" valign="top" width="70%"><c:out value="${des}"escapeXml="true"/></td>
            </tr>
        </c:if>
        <c:set var="agenda" value="${widget.meeting.event.agenda}"/>
        <c:if test="${! empty agenda}">
            <tr>
                <td class="emeetingRowLabel" valign="top" align="right" width="30%" nowrap><fmt:message key='emeeting.label.agenda'/>&nbsp;</td>
                <td class="emeetingRow" valign="top" width="70%"><c:out value="${agenda}" escapeXml="true"/></td>
            </tr>
        </c:if>
        <tr><td class="emeetingFooter" colspan="2"><b><fmt:message key='emeeting.label.agenda'/></b></td></tr>
        <c:choose>
            <c:when test="${! empty widget.meeting.meetingAgenda}">
                <tr>
                    <td class="emeetingRow" colspan="2">
                        <table width="95%" align="center" cellpadding="0" cellspacing="0">
                            <c:set var="parentCount" value="1"/>
                            <c:forEach var="parent" items="${widget.meeting.meetingAgenda}" varStatus="status" >
                                <c:choose>
                                    <c:when test="${parent.itemId == widget.itemId}">
                                        <tr><td><b><c:out value="${parentCount}"/>.0. <c:out value="${parent.title}"/></b>
                                            <c:if test="${!status.first}">
                                                <INPUT TYPE="button" CLASS="themeButton" VALUE="^" onClick="location = '<c:out value="${moveUpUrl}"/>&itemId=<c:out value="${parent.itemId}" />' ;"/>
                                            </c:if>
                                            <c:if test="${!status.last}" >
                                                <INPUT TYPE="button" CLASS="themeButton"  VALUE="v" onClick="location = '<c:out value="${moveDownUrl}"/>&itemId=<c:out value="${parent.itemId}" />' ;"/>
                                            </c:if>

                                        </td></tr>
                                        <tr><td align="center"><jsp:include page="meetingActionItemsForm.jsp" flush="true"/></td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td><c:out value="${parentCount}"/>.0. <b><x:event name="${widget.absoluteName}" type="<%= MeetingActionItemsForm.ITEM_SELECTION %>" param="itemId=${parent.itemId}" ><c:out value="${parent.title}"/></x:event></b>
                                            <c:if test="${!status.first}">
                                                <INPUT TYPE="button" CLASS="themeButton"  VALUE="^" onClick="location = '<c:out value="${moveUpUrl}"/>&itemId=<c:out value="${parent.itemId}" />' ;"/>
                                            </c:if>
                                            <c:if test="${!status.last}" >
                                                <INPUT TYPE="button" CLASS="themeButton"  VALUE="v" onClick="location = '<c:out value="${moveDownUrl}"/>&itemId=<c:out value="${parent.itemId}" />' ;"/>
                                            </c:if>

                                        </td></tr>
                                        <c:if test="${! empty parent.notes}">
                                            <%
                                                String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("parent")).getNotes(), "\n", "<br>");
                                                pageContext.setAttribute("content", content);
                                            %>
                                            <tr><td><font class="emeetingSmall"><fmt:message key='emeeting.label.minutes'/>:</font><br><c:out value="${content}" escapeXml="false" /></td></tr>
                                        </c:if>
                                        <c:if test="${! empty parent.action}">
                                            <%
                                                String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("parent")).getAction(), "\n", "<br>");
                                                pageContext.setAttribute("content", content);
                                            %>
                                            <tr><td><font class="emeetingSmall"><br><fmt:message key='emeeting.label.actions'/>:</font><br><c:out value="${content}" escapeXml="false" /></td></tr>
                                        </c:if>
                                        <c:if test="${! empty parent.task}">
                                            <tr>
                                                <td>
                                                    <font class="emeetingSmall"><br><fmt:message key='emeeting.label.task'/>:</font><br>
                                                        <c:out value="${parent.task.title}" />
                                                        <br>
                                                        <c:set var="i" value="1"></c:set><fmt:message key='emeeting.label.assignedto'/><c:forEach var="att" items="${parent.task.attendees}" >
                                                            <c:out value="${att.name}" /><c:if test="${!empty parent.task.attendees[i]}">,</c:if>
                                                            <c:set var="i" value="${i+1}"/>
                                                        </c:forEach>
                                                        <br>
                                                    <c:choose>
                                                        <c:when test="${parent.task.completed}"><fmt:message key='emeeting.label.completed'/></c:when>
                                                        <c:otherwise><fmt:message key='emeeting.label.dueon'/> <fmt:formatDate value="${parent.task.dueDate}" pattern="${globalDatetimeLong}"/></c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                                <%-- Rendering Child --%>
                                <c:if test="${! empty parent.children}">
                                    <tr>
                                        <td>
                                            <table align="right" width="95%" cellspacing="0">
                                                <c:set var="childCount" value="1"/>
                                                <c:forEach var="child" items="${parent.children}" varStatus="childStatus" >
                                                    <tr>
                                                        <td class="emeetingRow" colspan="2"><hr size="1" style="border: dotted 1px silver"></td>
                                                    </tr>
                                                    <c:set var="item" value="${child.value}"/>
                                                    <c:choose>
                                                        <c:when test="${child.value.itemId == widget.itemId}">
                                                            <tr><td><b><c:out value="${parentCount}.${childCount}"/>. <c:out value="${child.value.title}"/></b>
                                                                <c:if test="${!childStatus.first}">
                                                                    <INPUT TYPE="button" CLASS="themeButton"  VALUE="^" onClick="location = '<c:out value="${moveUpUrl}"/>&itemId=<c:out value="${child.value.itemId}" />' ;"/>
                                                                </c:if>
                                                                <c:if test="${!childStatus.last}" >
                                                                    <INPUT TYPE="button" CLASS="themeButton"  VALUE="v" onClick="location = '<c:out value="${moveDownUrl}"/>&itemId=<c:out value="${child.value.itemId}" />' ;"/>
                                                                </c:if>

                                                            </td></tr>
                                                            <tr><td><jsp:include page="meetingActionItemsForm.jsp" flush="true"/></td></tr>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr><td><c:out value="${parentCount}.${childCount}"/>. <b><x:event name="${widget.absoluteName}" type="<%= MeetingActionItemsForm.ITEM_SELECTION %>" param="itemId=${child.value.itemId}" ><c:out value="${child.value.title}"/></x:event></b>
                                                                <c:if test="${!childStatus.first}">
                                                                    <INPUT TYPE="button" CLASS="themeButton"  VALUE="^" onClick="location = '<c:out value="${moveUpUrl}"/>&itemId=<c:out value="${child.value.itemId}" />' ;"/>
                                                                </c:if>
                                                                <c:if test="${!childStatus.last}" >
                                                                    <INPUT TYPE="button" CLASS="themeButton"  VALUE="v" onClick="location = '<c:out value="${moveDownUrl}"/>&itemId=<c:out value="${child.value.itemId}" />' ;"/>
                                                                </c:if>

                                                            </td></tr>
                                                            <c:if test="${! empty child.value.notes}">
                                                            <%
                                                                String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("item")).getNotes(), "\n", "<br>");
                                                                pageContext.setAttribute("content", content);
                                                            %>
                                                                <tr><td><font class="emeetingSmall"><fmt:message key='emeeting.label.minutes'/>:</font><br><c:out value="${content}" escapeXml="false" /></td></tr>
                                                            </c:if>
                                                            <c:if test="${! empty child.value.action}">
                                                                <%
                                                                    String content = StringUtils.replace(((AgendaItem) pageContext.getAttribute("item")).getAction(), "\n", "<br>");
                                                                    pageContext.setAttribute("content", content);
                                                                %>
                                                                <tr><td><font class="emeetingSmall"><br><fmt:message key='emeeting.label.actions'/>:</font><br><c:out value="${content}" escapeXml="false" /></td></tr>
                                                            </c:if>
                                                            <c:if test="${! empty child.value.task}">
                                                                <tr>
                                                                    <td>
                                                                        <font class="emeetingSmall"><br><fmt:message key='emeeting.label.task'/>:</font><br>
                                                                        <c:out value="${child.value.task.title}" />
                                                                        <br><fmt:message key='emeeting.label.assignedto'/>                                                                    <c:forEach var="att" items="${child.value.task.attendees}" >
                                                                        <c:out value="${att.name}" /><c:if test="${!empty child.value.task.attendees[i]}">,</c:if>
                                                                        <c:set var="i" value="${i+1}"></c:set>
                                                                    </c:forEach>
                                                                        <br>
                                                                        <c:choose>
                                                                            <c:when test="${child.value.task.completed}"><fmt:message key='emeeting.label.completed'/></c:when>
                                                                            <c:otherwise><fmt:message key='emeeting.label.dueon'/> <fmt:formatDate value="${child.value.task.dueDate}" pattern="${globalDatetimeLong}"/></c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </tr>
                                                            </c:if>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:set var="childCount" value="${childCount + 1}"/>
                                                </c:forEach>
                                            </table>
                                        </td>
                                    </tr>
                                </c:if>
                                <%-- END: Rendering Child --%>
                                <c:set var="parentCount" value="${parentCount + 1}"/>
                                <tr>
                                    <td class="emeetingRow" colspan="2"><hr size="1" style="border: dotted 1px silver"></td>
                                </tr>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <tr><td class="emeetingRow" colspan="2"><fmt:message key='emeeting.label.noAgendaItemsFound'/></td></tr>
            </c:otherwise>
        </c:choose>

        <tr><td class="emeetingRow" colspan="2">
            <script>
            <!--
                function meetingActionItemsDone() {
                    window.close();
                    window.opener.location.reload();
                }
            //-->
            </script>
            &nbsp;<input class="button" type="button" value="<fmt:message key='emeeting.label.doneButton'/>" onclick="meetingActionItemsDone()">
            <br><br>
<%--                style="border-width:1pt; background-color:#E1E1E1; border-style:solid; border-color:#AAAAAA; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal">--%>


        </td></tr>

        <c:if test="${empty widget.itemId}">
            <tr><td class="emeetingFooter" colspan="2"><fmt:message key='emeeting.label.addNewAgendaItem'/></td></tr>
            <tr><td class="emeetingRow" colspan="2"><br><jsp:include page="meetingActionItemsForm.jsp" flush="true"/><br></td></tr>
        </c:if>
        <tr><td class="emeetingLabel" colspan="2">&nbsp;</td></tr>
    </table>
    <jsp:include page="../form_footer.jsp" flush="true"/>
</c:if>
