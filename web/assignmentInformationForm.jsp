<%@ include file="/common/header.jsp" %>

<x:config>
	<page name="assignmentInformation">
    	<com.tms.fms.facility.ui.AssignmentInformationForm name="table" width="100%"/>
	</page>
</x:config>

<%--@include file="/ekms/includes/linkCSS.jsp" --%>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setHeader("Expires", "0");
%>

	<x:display name="assignmentInformation.table"/>

<jsp:include page="includes/footer.jsp" />
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setHeader("Expires", "0");
%>