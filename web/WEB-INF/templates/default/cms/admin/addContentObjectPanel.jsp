<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="panel" scope="request" value="${widget}"/>

<%--Display Content Select Form--%>
<x:display name="${panel.childMap.contentSelectForm.absoluteName}" />

<span style="color:red; font-weight:bold">
	<x:display name="${panel.childMap.invalidKeyLabel.absoluteName}"/>
</span>

<%--Display Content Object Form--%>
<x:display name="${panel.childMap.containerForm.absoluteName}" />


