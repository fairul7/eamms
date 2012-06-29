<%@ page import="kacang.ui.WidgetManager" %> 
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="fms">         
         <com.tms.fms.engineering.ui.MyEngineeringAssignmentTable name="table" />        
    </page>
</x:config>

<c:if test="${!empty param.assignmentId}">
	<c:redirect url="myAssignmentDetails.jsp?id=${param.assignmentId}"/> 
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>
<c:set var="bodyTitle" scope="request">
	<fmt:message key='fms.label.engineering.dutyRoster'/> > <fmt:message key="fms.label.engineering.myAssignment"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <x:display name="fms" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>
