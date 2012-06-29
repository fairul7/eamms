<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="form" value="${widget}"/>

<c:if test="${form == form.rootForm}">
</form>
</c:if>

<%--
<p>
Debugging Info:
<ul>
<li>Root Form: <c:out value="${form.rootForm}"/></li>
<li>Listeners: <c:out value="${form.formEventListenerList}"/></li>
</ul>
--%>
</div>
