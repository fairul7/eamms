<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="selectbox" value="${widget}"/>

<c:if test="${selectbox.invalid}">!</c:if><select 
    class="WCHhider" style="<c:if test="${selectbox.invalid}">border:1px solid #de123e;</c:if>background-color:#ffffff; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8pt; font-weight:normal"
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

<c:forEach var="child" items="${selectbox.children}">
    <x:display name="${child.absoluteName}"/>
</c:forEach>

