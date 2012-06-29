<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.digestIssueName'/>
        </td><td align="left">
        <x:display name="${form.digestIssueName.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td width="25%" valign="top" align="right">&nbsp;</td>
        <td align="left">
            <x:display name="${form.submit.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>