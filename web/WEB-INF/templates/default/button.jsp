<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="button" value="${widget}"/>
<c:set var="type" value="${param.type}"/>
<c:if test="${type == null}">
    <c:set var="type" value="submit"/>
</c:if>

<input 
<%--    style="background: #6699CC; font-family:Verdana; color:white; border:1px embossed blue; cursor:hand"--%>
    class="button"
    type="<c:out value="${type}"/>"
    name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${button.absoluteName}"/>"
    value="<c:out value="${button.text}"/>"
    onBlur="<c:out value="${button.onBlur}"/>"
    onClick="<c:out value="${button.onClick}"/>"
    onFocus="<c:out value="${button.onFocus}"/>"
<%--
    onMouseOver="this.style.background='#336699'"
    onMouseOut="this.style.background='#6699cc'"
--%>
>


