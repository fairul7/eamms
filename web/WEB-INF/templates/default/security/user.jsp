<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../form_header.jsp"/>
<table>
    <tr>
        <td>
            <x:display name="${widget.addUser.absoluteName}"/>
            <x:display name="${widget.editUser.absoluteName}"/>
            <x:display name="${widget.viewUser.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp"/>