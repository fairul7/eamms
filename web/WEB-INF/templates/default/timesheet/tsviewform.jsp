<%--
  Created by IntelliJ IDEA.
  User: oilai
  Date: Apr 26, 2005
  Time: 3:29:06 PM
  To change this template use File | Settings | File Templates.
--%>

<%@include file="/common/header.jsp"%>
<%@page import="com.tms.collab.taskmanager.model.TaskManager,
				kacang.Application,
				kacang.ui.WidgetManager,
				java.util.*,
				com.tms.collab.taskmanager.model.Task" %>
<c:set var="w" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="4" cellspacing="0" class="contentBgColor">
    <tr>
        <td class="calendarHeader">
        <c:choose>
        <c:when test="${w.set}">
        <fmt:message key='timesheet.label.timesheet'/> >
        <fmt:message key="timesheet.label.add"/>   
        </c:when>
        <c:otherwise>
        <fmt:message key='timesheet.label.timesheet'/> > 
        <fmt:message key="timesheet.label.viewbytask"/>    
        </c:otherwise>
        </c:choose>
        </td>
    </tr>
    <tr>
        <td>
    <table width="100%" cellpadding="4" cellspacing="1" class="classBackground">
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selectproject"/></td>
        <td class="classRow"><x:display name="${w.sbProject.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" width="100" align="right"><fmt:message key="timesheet.label.selecttask"/></td>
        <td class="classRow"><x:display name="${w.sbTask.absoluteName}"/></td>
    </tr>
    <tr>
        <td class="classRowLabel" align="right">&nbsp;</td>
        <td class="classRow"><x:display name="${w.btSubmit.absoluteName}"/></td>
    </tr>
    </table>
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>