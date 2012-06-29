<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" value="${widget}"/>

<c:forEach var="peanut" items="${panel.list}">
    <li><x:event name="${panel.absoluteName}" param="id=${peanut.id}"><c:out value="${peanut.name}"/></x:event></li>
</c:forEach>
