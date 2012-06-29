<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="page" value="${widget}"/>

<c:forEach var="child" items="${page.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>
