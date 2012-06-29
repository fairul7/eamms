<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" >
<tr>
    <td align="right" valign="top" width="20%"><b><fmt:message key="claims.label.rejectReason"/></b></td>
    <td ><x:display name="${w.tfReason.absoluteName}"/></td>
</tr>
<tr>
    <td></td>
    <td>
        <x:display name="${w.bnSubmit.absoluteName}"/>
        <x:display name="${w.bnCancel.absoluteName}"/>
    </td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>