<%@ include file="/common/header.jsp" %>
        </td>
    </tr>
    <tr align="center" valign="bottom">
        <td height="32" colspan="2" background="<c:url value="/mekms/images/bottombg.gif"/>">
            <table width="160" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="4"><img src="<c:url value="/mekms/images/menuleft.gif"/>" width="4" height="28"></td>
                    <td valign="bottom" background="<c:url value="/mekms/images/menubg.gif"/>">
                        <table width="136" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                                <%--<td class="search">To Add:</td>--%>
                                <td width="20" valign="bottom"><a href="<c:url value="/mekms/taskmanager/taskform.jsp" />" class="menu"><img src="<c:url value="/mekms/images/todo.gif"/>" width="20" height="20" border="0"></a></td>
                                <td width="4">&nbsp;</td>
                                <td width="20"><a href="<c:url value="/mekms/calendar/calendarform.jsp" />" class="menu"><img src="<c:url value="/mekms/images/appt.gif"/>" width="20" height="20" border="0"></a></td>
                                <td width="4">&nbsp;</td>
                                <td width="20"><a href="<c:url value="/mekms/calendar/emeetingform.jsp" />" class="menu"><img src="<c:url value="/mekms/images/meeting.gif"/>" width="20" height="20" border="0"></a></td>
                                <td width="4">&nbsp;</td>
                                <td width="20"><a href="<c:url value="/mekms/calendar/eventform.jsp" />" class="menu"><img src="<c:url value="/mekms/images/event.gif"/>" width="20" height="20" border="0"></a></td>
                            </tr>
                        </table>
                    </td>
                    <td width="4"><img src="<c:url value="/mekms/images/menuright.gif"/>" width="4" height="28"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>