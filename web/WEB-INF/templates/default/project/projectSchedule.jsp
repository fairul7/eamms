<%@ page import="com.tms.collab.project.ui.ProjectSchedule,
                 java.util.Collection,
                 org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/header.jsp" %>
<jsp:include page="../form_header.jsp" flush="true"/>
<script language="javascript">
    function toggleTaskLayer(layer)
    {
        if(document.getElementById("details_" + layer).style.display == "none" || document.getElementById("details_" + layer).style.display == "")
            document.getElementById("details_" + layer).style.display = "block";
        else
            document.getElementById("details_" + layer).style.display = "none";
    }
    <c:if test="${!widget.currentlyTemplate}">
        function expandAll()
        {
            <c:forEach var="milestone" items="${widget.milestones}">
                <c:forEach var="task" items="${milestone.tasks}">
                    document.getElementById("details_<c:out value="${task.id}"/>").style.display = "block";
                </c:forEach>
				<c:forEach var="meeting" items="${milestone.meetings}">
                    document.getElementById("details_<c:out value="${meeting.eventId}"/>").style.display = "block";
                </c:forEach>
            </c:forEach>
        }
        function hideAll()
        {
            <c:forEach var="milestone" items="${widget.milestones}">
                <c:forEach var="task" items="${milestone.tasks}">
                    document.getElementById("details_<c:out value="${task.id}"/>").style.display = "none";
                </c:forEach>
				<c:forEach var="meeting" items="${milestone.meetings}">
                    document.getElementById("details_<c:out value="${meeting.eventId}"/>").style.display = "none";
                </c:forEach>
            </c:forEach>
        }
    </c:if>
</script>
<table cellpadding="4" cellspacing="1" width="100%">
    <tr>
        <td colspan="4" class="contentStrapColor" align="left">
            <c:choose>
                <c:when test="${empty widget.templateId}">
                    <input type="button" class="button" value="<fmt:message key="project.label.addMilestone"/>" onClick="window.open('<c:url value="/ekms/worms/milestoneAdd.jsp"/>?projectId=<c:out value="${widget.projectId}"/>', 'milestoneWindow', 'height=300,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                    <input type="button" class="button" value="<fmt:message key="project.label.addTask"/>" onClick="window.open('<c:url value="/ekms/worms/task.jsp"/>?projectId=<c:out value="${widget.projectId}"/>&reset=true', 'taskWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                    <input type="button" class="button" value="<fmt:message key="project.label.addMeeting"/>" onClick="window.open('<c:url value="/ekms/worms/meetingAdd.jsp"/>?projectId=<c:out value="${widget.projectId}"/>&reset=true', 'meetingWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                </c:when>
                <c:otherwise>
                    <input type="button" class="button" value="<fmt:message key="project.label.addMilestone"/>" onClick="window.open('<c:url value="/ekms/worms/milestoneAdd.jsp"/>?projectId=<c:out value="${widget.templateId}"/>&currentlyTemplate=true', 'milestoneWindow', 'height=300,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                    <input type="button" class="button" value="<fmt:message key="project.label.addTask"/>" onClick="window.open('<c:url value="/ekms/worms/descriptorAdd.jsp"/>?projectId=<c:out value="${widget.templateId}"/>&currentlyTemplate=true&reset=true', 'taskWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                    <input type="button" class="button" value="<fmt:message key="project.label.addMeeting"/>" onClick="window.open('<c:url value="/ekms/worms/meetingAdd.jsp"/>?projectId=<c:out value="${widget.templateId}"/>&currentlyTemplate=true&reset=true', 'meetingWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
                </c:otherwise>
            </c:choose>

            <%--<x:display name="${widget.addMilestone.absoluteName}"/>
            <x:display name="${widget.addTask.absoluteName}"/>
			<x:display name="${widget.addMeeting.absoluteName}"/>--%>
            <x:display name="${widget.delete.absoluteName}"/>
			<%--<x:display name="${widget.cancel.absoluteName}"/>--%>
            <input type="button" class="button" value="<fmt:message key="project.label.cancel"/>" onClick="document.location='<c:url value="/ekms/worms/projectOpen.jsp?projectId=${widget.projectId}"/>';">
        </td>
    </tr>
    <tr>
        <td width="2%" class="tableHeader" nowrap>&nbsp;</td>
        <td width="38%" class="tableHeader" nowrap><fmt:message key="project.label.milestone"/></td>
        <td width="50%" class="tableHeader" nowrap><fmt:message key="project.label.task"/></td>
        <td width="10%" class="tableHeader" nowrap><fmt:message key="project.label.contribution"/></td>
    </tr>
    <c:choose>
        <c:when test="${!empty widget.milestones}">
            <c:if test="${!widget.currentlyTemplate}">
                <tr>
                    <td colspan="4" class="tableRow">
						<input type="button" class="button" value="<fmt:message key="project.label.hideAll"/>" onClick="hideAll(); return false;">
						<input type="button" class="button" value="<fmt:message key="project.label.expandAll"/>" onClick="expandAll(); return false;">
                        <%--[<a href="" onClick="hideAll(); return false;"><fmt:message key="project.label.hideAll"/></a>] [<a href="" onClick="expandAll(); return false;"><fmt:message key="project.label.expandAll"/></a>]--%>
                    </td>
                </tr>
            </c:if>
            <%-- Constant Initialization --%>
            <c-rt:set var="currentUrl" value="<%= request.getRequestURI() %>"/>
            <c-rt:set var="event_moveup" value="<%= ProjectSchedule.EVENT_MOVE_UP %>"/>
            <c-rt:set var="event_movedown" value="<%= ProjectSchedule.EVENT_MOVE_DOWN %>"/>
            <c-rt:set var="event_select" value="<%= ProjectSchedule.FORWARD_SELECTION %>"/>
            <c-rt:set var="key_milestone" value="<%= ProjectSchedule.KEY_MILESTONE %>"/>
            <c:set var="total" value="0"/>
            <%
                Collection milestones = ((ProjectSchedule)request.getAttribute("widget")).getMilestones();
                pageContext.setAttribute("collectionSize", new Integer(milestones.size()));
            %>
            <c:forEach var="milestone" varStatus="currentRow" items="${widget.milestones}">
                <tr>
                    <td width="2%" class="tableRow" valign="top">
                        <c:set var="checkbox_name" value="chk${milestone.milestoneId}"/>
                        <x:display name="${widget.childMap[checkbox_name].absoluteName}"/>
                    </td>
                    <td width="38%" class="tableRow" valign="top">
                        <%--<b><x:event name="${widget.absoluteName}" type="${event_select}" param="milestoneId=${milestone.milestoneId}">
                            <c:out value="${milestone.milestoneName}"/>
                        </x:event></b>--%>
                        <b>
                        <a href="" onClick="window.open('<c:url value="/ekms/worms/milestoneOpen.jsp"/>?milestoneId=<c:out value="${milestone.milestoneId}"/>', 'milestoneWindow', 'height=450,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes'); return false;">
                            <c:out value="${milestone.milestoneName}"/>
                        </a>
                        </b>
                        <c:if test="${!empty milestone.milestoneDescription}">
                            <br><br>
                            <c:set var="description" value="${milestone.milestoneDescription}"/>
                            <%
                                String translated = StringUtils.replace((String)pageContext.getAttribute("description"), "\n", "<br>");
                                pageContext.setAttribute("translated", translated);
                            %>
                            <c:out value="${translated}" escapeXml="false" />
                        </c:if>
                        <br><br>
                        <c:if test="${currentRow.count > 1}">
                            <input type="button" class="button" value="<fmt:message key="project.label.moveup"/>" onClick="document.location='<c:out value="${currentUrl}?cn=${widget.absoluteName}&et=${event_moveup}&${key_milestone}=${milestone.milestoneId}"/>';">
                        </c:if>
                        <c:if test="${currentRow.count < collectionSize}">
                            <input type="button" class="button" value="<fmt:message key="project.label.movedown"/>" onClick="document.location='<c:out value="${currentUrl}?cn=${widget.absoluteName}&et=${event_movedown}&${key_milestone}=${milestone.milestoneId}"/>';">
                        </c:if>
                    </td>
                    <td width="50%" class="tableRow" valign="top">
						<c:choose>
							<c:when test="${!widget.currentlyTemplate}">
								<%-- Rendering Tasks --%>
								<c:choose>
									<c:when test="${!empty milestone.tasks}">
										<c:forEach var="task" items="${milestone.tasks}">
											<li>
												[<a href="" onClick="toggleTaskLayer('<c:out value="${task.id}"/>'); return false;"><fmt:message key="project.label.details"/></a>]&nbsp;
												<%--<x:event name="${widget.absoluteName}" type="${event_select}" param="taskId=${task.id}">
													<c:out value="${task.title}"/>
												</x:event>--%>
                                                <a href="" onClick="window.open('<c:url value="/ekms/worms/task.jsp"/>?taskId=<c:out value="${task.id}"/>', 'taskWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes'); return false;">
                                                    <c:out value="${task.title}"/>
                                                </a>
											</li><br>
											<div id="details_<c:out value="${task.id}"/>" style="display:block">
												<table cellpadding="0" cellspacing="0" align="center">
													<tr><td colspan="2" height="5"><img src="<c:url value='/ekms/images/clear.gif'/>" height="1" width="1"></td></tr>
													<tr>
														<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key="project.label.assignedTo"/>:&nbsp;</b></td>
														<td class="classTextSmall">
															<c:forEach var="attendee" items="${task.attendees}">
																<c:out value="${attendee.name}"/><br>
															</c:forEach>
														</td>
													</tr>
													<tr>
														<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key="project.label.starts"/>:&nbsp;</b></td>
														<td class="classTextSmall"><fmt:formatDate value="${task.startDate}" pattern="${globalDateLong}"/></td>
													</tr>
													<tr>
														<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key="project.label.ends"/>:&nbsp;</b></td>
														<td class="classTextSmall"><fmt:formatDate value="${task.dueDate}" pattern="${globalDateLong}"/></td>
													</tr>
												</table>
												<br>
											</div>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<fmt:message key="project.message.noTasksAssigned"/>
									</c:otherwise>
								</c:choose>
								<%-- Listing Meetings --%>
								<c:forEach var="meeting" items="${milestone.meetings}">
									<li>
										[<a href="" onClick="toggleTaskLayer('<c:out value="${meeting.eventId}"/>'); return false;"><fmt:message key="project.label.details"/></a>]&nbsp;
										<%--<x:event name="${widget.absoluteName}" type="${event_select}" param="meetingId=${meeting.eventId}">
											<c:out value="${meeting.title}"/>
										</x:event>--%>
                                        <a href="" onClick="window.open('<c:url value="/ekms/worms/meetingOpen.jsp"/>?meetingId=<c:out value="${meeting.eventId}"/>', 'meetingWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes'); return false;">
                                            <c:out value="${meeting.title}"/>
                                        </a>
									</li><br>
									<div id="details_<c:out value="${meeting.eventId}"/>" style="display:block">
										<table cellpadding="0" cellspacing="0" align="center">
											<tr><td colspan="2" height="5"><img src="<c:url value='/ekms/images/clear.gif'/>" height="1" width="1"></td></tr>
											<tr>
												<td class="classTextSmall" width="10%" nowrap valign="top"><b>Attendees:&nbsp;</b></td>
												<td class="classTextSmall">
													<c:forEach var="attendee" items="${meeting.event.attendees}">
														<c:out value="${attendee.name}"/><br>
													</c:forEach>
												</td>
											</tr>
											<tr>
												<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key="project.label.starts"/>:&nbsp;</b></td>
												<td class="classTextSmall"><fmt:formatDate value="${meeting.startDate}" pattern="${globalDateLong}"/></td>
											</tr>
											<tr>
												<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key="project.label.ends"/>:&nbsp;</b></td>
												<td class="classTextSmall"><fmt:formatDate value="${meeting.endDate}" pattern="${globalDateLong}"/></td>
											</tr>
										</table>
										<br>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<%-- Rendering Descriptors --%>
								<c:choose>
									<c:when test="${!empty milestone.descriptors}">
										<table cellpadding="0" cellspacing="0" align="center">
											<c:forEach var="descriptor" items="${milestone.descriptors}">
												<tr>
													<td colspan="2">
														<li>
															<x:event name="${widget.absoluteName}" type="${event_select}" param="descId=${descriptor.descId}">
																<c:out value="${descriptor.descName}"/>
															</x:event>
														</li>
													</td>
												</tr>
												<tr>
													<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key='emeeting.label.starts'/>:&nbsp;</b></td>
													<td class="classTextSmall"><fmt:message key='emeeting.label.day'/> <c:out value="${descriptor.descStart}"/></td>
												</tr>
												<tr>
													<td class="classTextSmall" width="10%" nowrap valign="top"><b><fmt:message key='emeeting.label.duration'/>:&nbsp;</b></td>
													<td class="classTextSmall"><c:out value="${descriptor.descDuration}"/> <fmt:message key='emeeting.label.days'/></td>
												</tr>
												<tr><td class="classTextSmall" colspan="2">&nbsp;</td></tr>
											</c:forEach>
										</table>
									</c:when>
									<c:otherwise>
										<fmt:message key='emeeting.label.noDescriptorsFound'/>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>

                    </td>
                    <td width="10%" class="tableRow" valign="top" align="center"><c:out value="${milestone.milestoneProgress}"/>%</td>
                    <c:set var="total" value="${total+milestone.milestoneProgress}"/>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="3" class="tableRow" align="right"><fmt:message key="project.message.totalProjectAllocated"/>: </td>
                <td width="10%" class="tableRow" valign="top" align="center"><b><c:out value="${total}"/>%</b></td>
            </tr>
            <c:if test="${!widget.currentlyTemplate}">
                <tr>
                    <td colspan="4" class="tableRow">
						<input type="button" class="button" value="<fmt:message key="project.label.hideAll"/>" onClick="hideAll(); return false;">
						<input type="button" class="button" value="<fmt:message key="project.label.expandAll"/>" onClick="expandAll(); return false;">
                        <%--[<a href="" onClick="hideAll(); return false;"><fmt:message key="project.label.hideAll"/></a>] [<a href="" onClick="expandAll(); return false;"><fmt:message key="project.label.expandAll"/></a>]--%>
                    </td>
                </tr>
            </c:if>
        </c:when>
        <c:otherwise>
            <tr><td colspan="4" class="tableRow"><fmt:message key="project.message.noMilestonesFound"/></td></tr>
        </c:otherwise>
    </c:choose>
	<tr>
        <td colspan="4" class="contentStrapColor" align="left">
            <%--<x:display name="${widget.addMilestone.absoluteName}"/>
            <x:display name="${widget.addTask.absoluteName}"/>
			<x:display name="${widget.addMeeting.absoluteName}"/>--%>
            <input type="button" class="button" value="<fmt:message key="project.label.addMilestone"/>" onClick="window.open('<c:url value="/ekms/worms/milestoneAdd.jsp"/>?projectId=<c:out value="${widget.projectId}"/>', 'milestoneWindow', 'height=300,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
            <input type="button" class="button" value="<fmt:message key="project.label.addTask"/>" onClick="window.open('<c:url value="/ekms/worms/task.jsp"/>?projectId=<c:out value="${widget.projectId}"/>&reset=true', 'taskWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
            <input type="button" class="button" value="<fmt:message key="project.label.addMeeting"/>" onClick="window.open('<c:url value="/ekms/worms/meetingAdd.jsp"/>?projectId=<c:out value="${widget.projectId}"/>&reset=true', 'meetingWindow', 'height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');">
            <x:display name="${widget.delete.absoluteName}"/>
			<%--<x:display name="${widget.cancel.absoluteName}"/>--%>
            <input type="button" class="button" value="<fmt:message key="project.label.cancel"/>" onClick="document.location='<c:url value="/ekms/worms/projectOpen.jsp?projectId=${widget.projectId}"/>';">
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>