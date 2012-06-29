<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.ViewRequestForm"%>

<c-rt:set var="forwardBack" value="<%= ViewRequestForm.FORWARD_BACK %>" />
<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="myRequest.jsp" />
</c:if>



<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewRequestPg">
		<com.tms.sam.po.ui.ViewRequestForm name="ViewRequest"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewRequestPg.ViewRequest" property="ppID" value="${param.id}" />
</c:if>

<x:display name="viewRequestPg.ViewRequest"/>

<%@include file="/ekms/includes/footer.jsp" %>