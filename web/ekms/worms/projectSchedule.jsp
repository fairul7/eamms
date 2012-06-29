<%@ page import="com.tms.collab.project.ui.ProjectTable,
                 com.tms.collab.project.ui.ProjectSchedule"%>
<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.worms.project.Project.edit" module="com.tms.collab.project.WormsHandler" url="/ekms/index.jsp"/>
<x:config>
    <page name="wormsProjectSchedule">
        <com.tms.collab.project.ui.ProjectSchedule name="form"/>
    </page>
</x:config>
<%-- Event Handling --%>
<c-rt:set var="forward_success" value="<%= ProjectSchedule.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_failed" value="<%= ProjectSchedule.FORWARD_FAILED %>"/>
<c-rt:set var="forward_cancel" value="<%= ProjectSchedule.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_add_milestone" value="<%= ProjectSchedule.FORWARD_ADD_MILESTONE %>"/>
<c-rt:set var="forward_add_task" value="<%= ProjectSchedule.FORWARD_ADD_TASK %>"/>
<c-rt:set var="forward_add_meeting" value="<%= ProjectSchedule.FORWARD_ADD_MEETING %>"/>
<c-rt:set var="forward_select" value="<%= ProjectSchedule.FORWARD_SELECTION %>"/>
<c:if test="${!empty param.projectId}">
    <x:set name="wormsProjectSchedule.form" property="projectId" value="${param.projectId}"/>
</c:if>
<%--<c:if test="${forward_cancel == forward.name}">
    <c:redirect url="/ekms/worms/projectOpen.jsp?projectId=${widgets['wormsProjectSchedule.form'].projectId}"/>
</c:if>
<c:if test="${forward_add_milestone == forward.name}">
    <script>window.open("<c:url value="/ekms/worms/milestoneAdd.jsp"/>?projectId=<c:out value="${widgets['wormsProjectSchedule.form'].projectId}"/>", "milestoneWindow", "height=300,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<c:if test="${forward_add_task == forward.name}">
    <script>window.open("<c:url value="/ekms/worms/task.jsp"/>?projectId=<c:out value="${widgets['wormsProjectSchedule.form'].projectId}"/>&reset=true", "taskWindow", "height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<c:if test="${forward_add_meeting == forward.name}">
    <script>window.open("<c:url value="/ekms/worms/meetingAdd.jsp"/>?projectId=<c:out value="${widgets['wormsProjectSchedule.form'].projectId}"/>&reset=true", "meetingWindow", "height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
</c:if>
<c:if test="${forward_select == forward.name}">
    <c:choose>
        <c:when test="${!empty param.milestoneId}">
            <script>window.open("<c:url value="/ekms/worms/milestoneOpen.jsp"/>?milestoneId=<c:out value="${param.milestoneId}"/>", "milestoneWindow", "height=450,width=450,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
        </c:when>
        <c:when test="${!empty param.taskId}">
            <script>window.open("<c:url value="/ekms/worms/task.jsp"/>?taskId=<c:out value="${param.taskId}"/>", "taskWindow", "height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
        </c:when>
		<c:when test="${!empty param.meetingId}">
            <script>window.open("<c:url value="/ekms/worms/meetingOpen.jsp"/>?meetingId=<c:out value="${param.meetingId}"/>", "meetingWindow", "height=450,width=550,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes");</script>
        </c:when>
    </c:choose>
</c:if>--%>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">&nbsp;<fmt:message key="project.label.projects"/> > <fmt:message key="project.label.projectSchedule"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="10"></td>
    </tr>
    <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <table cellpadding="0" cellspacing="0" width="95%" align="center">
                <tr><td><x:display name="wormsProjectSchedule.form"/></td></tr>
            </table>
        </td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/"/>images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>
