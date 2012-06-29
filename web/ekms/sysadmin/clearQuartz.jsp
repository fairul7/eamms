<%@ page import="kacang.services.security.SecurityService,
                 kacang.Application,
                 javax.sql.DataSource,
                 kacang.services.security.SecurityDao"%>
<%@ include file="/common/header.jsp" %>

<%@ taglib uri="http://java.sun.com/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jstl/sql_rt" prefix="sql-rt" %>

<%
	if (Boolean.valueOf(request.getParameter("clear")).booleanValue()) {
    	Application app = Application.getInstance();
    	SecurityService ss = (SecurityService)app.getService(SecurityService.class);
    	SecurityDao dao = (SecurityDao)ss.getDao();
    	DataSource ds = dao.getDataSource();
%>

<c:set var="upgradeSql">

DELETE FROM QRTZ_BLOB_TRIGGERS;
DELETE FROM QRTZ_CALENDARS;
DELETE FROM QRTZ_CRON_TRIGGERS;
DELETE FROM QRTZ_FIRED_TRIGGERS;
DELETE FROM QRTZ_JOB_DETAILS;
DELETE FROM QRTZ_JOB_LISTENERS;
DELETE FROM QRTZ_PAUSED_TRIGGER_GRPS;
DELETE FROM QRTZ_SCHEDULER_STATE;
DELETE FROM QRTZ_SIMPLE_TRIGGERS;
DELETE FROM QRTZ_TRIGGERS;
DELETE FROM QRTZ_TRIGGER_LISTENERS;

</c:set>

<c:forTokens items="${upgradeSql}" delims=";" var="stmt">
    <sql-rt:update dataSource="<%= ds %>">
        <c:out value="${stmt}" escapeXml="false" />
    </sql-rt:update>
</c:forTokens>

<%
		response.sendRedirect(response.encodeRedirectURL("systemSettings.jsp"));
	}
%>
