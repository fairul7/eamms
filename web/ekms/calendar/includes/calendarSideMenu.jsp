<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 com.tms.collab.resourcemanager.model.ResourceManager,
                 kacang.ui.Widget,
                 kacang.ui.WidgetManager,
                 com.tms.collab.calendar.model.CalendarModule"%>
<table width="100%" border="0" cellspacing="0" cellpadding="1" class="menuBgOutline">
    <tr>
        <td>
            <table width="100%" border="0" cellpadding="1" cellspacing="0" class="menuBgBackground">
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="menuBgColor">
                            <%--START: CALENDAR menu title--%>
                            <%
                                WidgetManager wm = (WidgetManager)request.getAttribute("wm");
                                String userId = wm.getUser().getId();

                                SecurityService ss = (SecurityService)Application.getInstance().getService(SecurityService.class);
                                if(ss.hasPermission(userId, CalendarModule.PERMISSION_CALENDARING,
                                        null,null)){
                            %>
                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='calendar.label.calendar'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <%--END: CALENDAR menu title--%>
                            <%--START: New Appointment--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/calendar/appointmentform.jsp?init=1" onclick="document.location ='<c:url value="/ekms" />/calendar/appointmentform.jsp?init=1';"><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.newAppointment'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New Appointment--%>
                            <%--START: New Event--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/calendar/eventform.jsp?init=1" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.newEvent'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New Event--%>
                            <%--START: New E-Meeting--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/calendar/emeetingform.jsp?init=1" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.newMeeting'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New E-Meeting--%>
                            <%--START: Daily View--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:out value="${dailyUrl}" />"><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.dailyView'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Daily View--%>
                            <%--START: Weekly View--%>
                             <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:out value="${weeklyUrl}" />"><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.weeklyView'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Weekly View--%>
                            <%--START: Monthly View--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:out value="${monthlyUrl}" />"><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.monthlyView'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: Monthly View--%>
                            <%--START: Listing View--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms/calendar/calendarEventList.jsp" />"><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.listingView'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <%--END: Listing View--%>
                            <%}%>
                            <%--START: TASK MANAGER menu title--%>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <tr>
                                <td colspan="3" valign="middle" bgcolor="666666" class="menuHeader" height="25">
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td class="menuHeader" width="5%"><img src="<c:url value="/ekms/images/system/blank.gif"/>" width="1" height="1"></td>
                                            <td class="menuHeader" ><fmt:message key='calendar.label.taskManager'/></td>
                                            <td class="menuHeader" width="18"><img src="<c:url value="/ekms/images/arrowdown.gif"/>"></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/system/blank.gif" width="5" height="1"></td></tr>
                            <%--END: TASK MANAGER menu title--%>
                            <%--START: New Task--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/calendar/todotaskform.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.newTask'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New Task--%>
                            <%--START: View All Tasks--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms/" />taskmanager/taskmanager.jsp"><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.viewAllTasks'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: View All Tasks--%>
                            <%--START: New Category--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms" />/taskmanager/taskcatform.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.newCategory'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <%--END: New Category--%>
                            <%--START: Category Listing--%>
                            <tr>
                                <td height="28" width="20%" align="center"><img src="<c:url value="/ekms/"/>images/folder.gif" width="15" height="17"></td>
                                <td height="28" width="75%"><a href="<c:url value="/ekms/" />/taskmanager/taskcategory.jsp" ><font color="FFFFFF" class="menuFont"><b><fmt:message key='calendar.label.categoryListing'/></b></font></a></td>
                                <td height="28" width="5%"><font color="FFFFFF" class="menuFont">&nbsp;</td>
                            </tr>
                            <%--END: Category Listing--%>
                            <tr><td colspan="3" align="center" bgcolor="000000" class="menuBgColorShadow"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" align="center" bgcolor="1059A5" class="menuBgColorHighlight"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="1"></td></tr>
                            <tr><td colspan="3" height="28" align="center">&nbsp;</td></tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
