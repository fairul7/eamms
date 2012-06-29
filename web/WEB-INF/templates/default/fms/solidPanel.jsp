<%@ include file="/common/header.jsp" %>
<c:set var="sPanel" value="${widget}"/>

<c:forEach var="child" items="${sPanel.children}">
	<x:display name="${child.absoluteName}"/>
</c:forEach>
