<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<jsp:include page="../form_header.jsp" flush="true"/>
<c:set var="form" value="${widget}"/>

<table>
    <tr>
        <td>
            <table>
                <tr>
                <Td>
                    <b>Selected Users</b> <br>
                    <x:display name="${form.users.absoluteName}"/>
                </td>
                <Td>
                    <x:display name="${form.removeButton.absoluteName}"/>
                </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <x:display name="${form.backButton.absoluteName}" />
            <x:display name="${form.submitButton.absoluteName}" />
        </td>
    </tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>