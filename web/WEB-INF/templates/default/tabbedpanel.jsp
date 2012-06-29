<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="tabbedpanel" value="${widget}"/>

<c:forEach var="pane" items="${tabbedpanel.panels}">
    <c:if test="${!pane.hidden && pane.absoluteName == tabbedpanel.selectedName}">
        <x:display name="${pane.absoluteName}"/>
     </c:if>
</c:forEach>
