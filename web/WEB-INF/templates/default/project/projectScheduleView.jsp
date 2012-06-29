<%@ include file="/common/header.jsp" %>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" width="100%" align="left">
<tr><td  class="projectSchedule" colspan="2"><fmt:message key="project.label.scheduleView"/> : <x:display name="${widget.daily.absoluteName}"/><x:display name="${widget.weekly.absoluteName}"/><x:display name="${widget.monthly.absoluteName}"/></td></tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>