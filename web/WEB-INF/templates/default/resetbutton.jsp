<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<c:set var="button" value="${widget}"/>

<input
<%--    style="background: #6699CC; font-family:Verdana; color:white; border:1px embossed blue; cursor:hand"--%>
    class="button"
    type="reset"
    name="<%= kacang.stdui.Button.PREFIX_BUTTON %><c:out value="${button.absoluteName}"/>"
    value="<c:out value="${button.text}"/>">


