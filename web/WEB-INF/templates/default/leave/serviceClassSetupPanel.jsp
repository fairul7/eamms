<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="panel" value="${widget}"/>
<table width="100%">
    <tr>
        <td>
            <x:display name="${panel.childMap.setupTable.absoluteName}"/>
            <x:display name="${panel.childMap.addForm.absoluteName}"/>
            <x:display name="${panel.childMap.editForm.absoluteName}"/>
        </td>
    </tr>
</table>