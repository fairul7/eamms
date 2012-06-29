<%@ include file="/common/header.jsp" %>
<c:set var="cPanel" value="${widget}"/>

<c:forEach var="child" items="${cPanel.children}">
	<x:display name="${child.absoluteName}"/>
</c:forEach>
