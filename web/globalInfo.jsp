<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-store"); 
response.setContentType("text/html");
response.setDateHeader ("Expires", -1);
%>
<%@ include file="/common/header.jsp" %>
<x:config>
	<page name="assignmentInformation">
    	<com.tms.fms.facility.ui.AssignmentInformationForm name="form" width="100%"/>
	</page>
</x:config>
	<x:display name="assignmentInformation.form" body="/WEB-INF/templates/default/fms/globalInfoTpl.jsp"/>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setHeader("Expires", "0");
%>