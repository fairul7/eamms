<%@ include file="/common/header.jsp" %>
<%@ page import="kacang.Application,
				com.tms.ekms.setup.model.SetupModule,
				com.tms.ekms.setup.model.SetupException,
				org.apache.commons.lang.StringUtils,
				kacang.util.Log,
				com.tms.cms.digest.model.DigestModule"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>

<%
	SetupModule setup = (SetupModule) Application.getInstance().getModule(SetupModule.class);
	String serverUrl = new String();
	try
	{
	    serverUrl = setup.get("siteUrl");
	}
	catch (SetupException e)
	{
	    Log.getLog(DigestModule.class).error(e);
	}
	serverUrl = serverUrl + request.getParameter("url");
%>

<script>
this.focus();
window.location.href = "<%=serverUrl%>";
</script>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />


<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>