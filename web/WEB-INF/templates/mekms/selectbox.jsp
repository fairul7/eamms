<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="selectbox" value="${widget}"/>
<c:if test="${selectbox.invalid}">
  !<span style="border:1 solid #de123e">
</c:if>
<select class="selectbox"
    name="<c:out value="${selectbox.absoluteName}"/>"
    size="<c:out value="${selectbox.rows}"/>"
    onBlur="<c:out value="${selectbox.onBlur}"/>"
    onChange="<c:out value="${selectbox.onChange}"/>"
    onFocus="<c:out value="${selectbox.onFocus}"/>"
    <c:if test="${selectbox.multiple}"> multiple</c:if>
>
<c:forEach items="${selectbox.optionMap}" var="option">
    <option value="<c:out value="${option.key}"/>"<c:if test="${!empty selectbox['selectedOptions'][option.key]}"> selected</c:if>>
        <c:out value="${option.value}"/></option>
</c:forEach>
</select>
<c:if test="${selectbox.invalid}">
  </span>
</c:if>


