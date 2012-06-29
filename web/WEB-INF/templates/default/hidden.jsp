<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<c:set var="hidden" value="${widget}"/>
    <input
        type="hidden"
        name="<c:out value="${hidden.absoluteName}"/>"
        value="<c:out value="${hidden.value}"/>" />


