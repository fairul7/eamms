<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.ekmsadmin.ekplog.model.EkpLogModule,
				kacang.Application" %>

<%
	EkpLogModule module = (EkpLogModule) Application.getInstance().getModule(EkpLogModule.class);
	boolean isTomcatLogsFolderReadable = module.isTomcatLogsFolderReadable();
%>

<x:config>
    <page name="ekpLog">
    	<com.tms.ekmsadmin.ekplog.ui.EkpLogTable name="logTable" />
    </page>
</x:config>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>

<table valign="top" width="100%" border="0" cellspacing="0" cellpadding="5">
	<tr>
		<td class="contentTitleFont" valign="top">
			<fmt:message key="ekplog.label.ekpLogs" />
		</td>
	</tr>
	<% if(!isTomcatLogsFolderReadable) { %>
	<tr>
		<td class="contentBgColor" valign="top">
			<span style="color:#CC0000; font-weight:bold;">Warning: The Tomcat logs folder does not exist or not readable.</span>
		</td>
	</tr>
	<% } %>
	<tr>
		<td class="contentBgColor" valign="top">
			<x:display name="ekpLog.logTable"/>
		</td>
	</tr>
	<tr>
		<td><iframe name="dummyEkpLogZipDownloadFrame" width="100%" frameborder="0"></iframe></td>
	</tr>
</table>

<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>

<script type="text/javascript">
<%
if(session.getAttribute("logZipDownloadObject") != null) {
%>
	//document.location = "/ekplog/download";
	top.frames['dummyEkpLogZipDownloadFrame'].location = "/ekplog/download";
<%
}
%>
</script>
