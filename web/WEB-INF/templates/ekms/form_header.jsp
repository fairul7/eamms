<%@ page import="kacang.stdui.Form,
				 java.util.Collection,
				 java.util.Iterator,
				 kacang.ui.Widget,
				 kacang.stdui.FormField"%>
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

<%!
	public boolean childrenHasError(Form form) {
		Collection col = form.getChildren();
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			Widget widget = (Widget) iterator.next();
			if (widget instanceof Form) {
				boolean hasError = childrenHasError((Form) widget);
				if (hasError) {
					return true;
				}
			} else if (widget instanceof FormField) {
				if (((FormField) widget).isInvalid()) {
					return true;
				}
			}
		}
		return false;
	}
%>

<c:if test="${form == form.rootForm}">
<form style="margin: 0px; border: none" name="<c:out value="${form.absoluteName}"/>"
      action="?"
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

<c:if test="${form == form.rootForm && form.invalid}">
	<fmt:message key='general.error.formInvalid.type' var="dispType"/>
	<%
		Form form = (Form) pageContext.getAttribute("form");
		boolean childHasError = childrenHasError(form);
	%>
	<% if (childHasError) { %>
		<c:choose>
			<c:when test="${dispType == 'script'}">
				<script>alert('<fmt:message key='general.error.formInvalid.message'/>');</script>
			</c:when>
			<c:otherwise>
				<font color="red"><fmt:message key='general.error.formInvalid.message'/></font>
			</c:otherwise>
		</c:choose>
	<% } %>
</c:if>
