<%@ include file="/common/header.jsp" %>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" width="95%" align="center">
    <tr><td colspan="2"><x:display name="${widget.projects.absoluteName}"/></td></tr>
    <tr><td colspan="2" align="right">&nbsp;</td></tr>
    <c:if test="${!(empty widget.project)}">
    	<tr><td colspan="2" class="projectSchedule"><fmt:message key="project.label.scheduleView"/> : <x:display name="${widget.daily.absoluteName}"/><x:display name="${widget.weekly.absoluteName}"/><x:display name="${widget.monthly.absoluteName}"/></td></tr>
        <tr><td colspan="2"><x:template type="com.tms.collab.project.ui.ProjectChart" properties="projectId=${widget.projectId}&hideDetails=false&documentLink=${widget.documentLink}&type=${widget.type}&viewType=all"/></td></tr>
    </c:if>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>