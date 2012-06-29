<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="validator" value="${widget}"/>
<font color="red"><c:out value="${validator.error}"/></font>