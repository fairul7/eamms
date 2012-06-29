<%@include file="/common/header.jsp"%>

<%@ page import="com.tms.sam.po.ui.ViewRequestForm"%>

<c-rt:set var="forwardBack" value="<%= ViewRequestForm.FORWARD_BACK %>" />

<c:if test="${forward.name eq forwardBack}">
    <c:redirect url="userRequest.jsp" />
</c:if>

<%@include file="/ekms/includes/header.jsp" %>

<x:config>
	<page name="viewUserRequestPg">
		<com.tms.sam.po.ui.ViewRequestForm name="ViewUserRequest"/>
	</page>
</x:config>

<c:if test="${!empty param.id}">
	<x:set name="viewUserRequestPg.ViewUserRequest" property="ppID" value="${param.id}" />
</c:if>

<x:display name="viewUserRequestPg.ViewUserRequest"/>

<%@include file="/ekms/includes/footer.jsp" %>