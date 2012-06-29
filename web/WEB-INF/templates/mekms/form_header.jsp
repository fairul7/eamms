<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="form" value="${widget}"/>
<c:choose>
    <c:when test="${form.invalid}">
        <c:set var="style" value="border:0 solid red"/>
    </c:when>
    <c:otherwise>
        <c:set var="style" value="border:0 solid silver"/>
    </c:otherwise>
</c:choose>
<%--    // TODO: KC: how to handle this?
<div style="<c:out value="${style}"/>; width:<c:out value="${form.width}"/>">
--%>

<c:if test="${form == form.rootForm}">
<form style="margin: 0px; border: none" name="<c:out value="${form.absoluteName}"/>"
      action="<c:out value="${pageContext.request.requestURI}"/>"
      method="<c:out value="${form.method}"/>"
      target="<c:out value="${form.target}"/>"
      <c:if test="${!empty form.enctype}">
          enctype="<c:out value="${form.enctype}"/>"
      </c:if>
      onSubmit="<c:out value="${form.attributeMap['onSubmit']}"/>"
      onReset="<c:out value="${form.attributeMap['onReset']}"/>"
>
<input type="hidden" name="cn" value="<c:out value="${form.absoluteName}"/>">
</c:if>

