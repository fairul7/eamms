<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<table width="100%">
    <tr>
        <td>
            <x:display name="${widget.addSubscription.absoluteName}"/>
            <x:display name="${widget.editSubscription.absoluteName}"/>
            <x:display name="${widget.subscriberForm.absoluteName}"/>
            <x:display name="${widget.viewSubscription.absoluteName}"/>
        </td>
    </tr>
</table>
