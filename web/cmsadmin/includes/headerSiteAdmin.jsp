<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<%@ include file="/cmsadmin/includes/headerCommon.jsp" %>

<TABLE width="100%" cellpadding="0" height="35" bgcolor="#336699" border="0" cellspacing="0">
	<TR>
		<TD valign="middle"><img width="5" height="1" src="../images/clear.gif"></TD>
		<TD valign="middle">
			<TABLE cellpadding="2" bgcolor="#4779AB" border="0" cellspacing="0" width="100%">
				<TR>
					<TD valign="middle" width="150"> | <a href="systemSettings.jsp" class="<%= (startsWith(request, "/cmsadmin/siteadmin/system")) ? "submenulink2" : "submenulink" %>"><fmt:message key='siteadmin.label.systemSettings'/></a></TD>
					<TD valign="middle" width="100"> | <a href="siteDesign.jsp" class="<%= (startsWith(request, "/cmsadmin/siteadmin/site")) ? "submenulink2" : "submenulink" %>"><fmt:message key='siteadmin.label.siteDesign'/></a></TD>
					<TD valign="middle" width="100"> | <a href="mobileChannelList.jsp" class="<%= (startsWith(request, "/cmsadmin/siteadmin/mobile")) ? "submenulink2" : "submenulink" %>"><fmt:message key='siteadmin.label.mobile'/></a></TD>
					<TD valign="middle" width="150"> | <a href="scheduledTasks.jsp" class="<%= (startsWith(request, "/cmsadmin/siteadmin/schedule")) ? "submenulink2" : "submenulink" %>"><fmt:message key='siteadmin.label.scheduledTasks'/></a></TD>
					<TD valign="middle" width="150"> | <a href="logs.jsp" class="<%= (startsWith(request, "/cmsadmin/siteadmin/logs")) ? "submenulink2" : "submenulink" %>">Logs</a></TD>
					<TD valign="middle" align="right">&nbsp;</TD>
				</TR>
			</TABLE>
		</TD>
		<TD align="right" valign="middle">
			<TABLE cellpadding="2" border="0" cellspacing="0">
				<TR><TD valign="middle" colspan="5">&nbsp;</TD></TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
