<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td>
        <x:display name="${form.sortableSelectBox.absoluteName}"/>
        </td>
    </tr>   
    <tr>
        <td>
            <x:display name="${form.submitButton.absoluteName}"/>
            <x:display name="${form.reorderButton.absoluteName}"/>
            <x:display name="${form.cancelButton.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>