<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<c:set var="mugshotpath" value="${widget.mugshotpath}"/>
<link ref="stylesheet" href="images/style.css">
<jsp:include page="../form_header.jsp" flush="true"/>
<c:if test="${!empty mugshotpath}">
<tr>
    <td class="profileRow"></td>
    <td class="profileRow"><IMG SRC="<c:out value="${mugshotpath}"/>" height=120 width=90 border=0></td>
</tr>
</c:if>
<tr>
    <td class="profileRow"></td>
    <td class="profileRow">
        <x:display name="${widget.fileUpload.absoluteName}"/>
        <c:if test="${!empty mugshotpath}">
            <x:display name="${widget.remove.absoluteName}"/>
        </c:if>
        <br>
        <font size="1"><fmt:message key="mugshot.warning.filesize"/></font>
    </td>
</tr>
<jsp:include page="../form_footer.jsp" flush="true"/>
