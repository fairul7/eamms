<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.recipientsName'/> *
        </td><td align="left">
        <x:display name="${form.recipientsName.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.recipientsUser'/> *
        </td><td align="left">
        <x:display name="${form.recipientsUser.absoluteName}"/>
        </td>
    </tr>
    <c:if test="${! empty form.message}">
        <tr>
			<td width="25%" align="right">&nbsp;</td>
            <td align="left"><font color="red"><c:out value="${form.message}"/></font></td>
        </tr>
    </c:if>
    <tr>
        <td width="25%" align="right">&nbsp;</td>
        <td align="left">
            <x:display name="${form.submit.absoluteName}"/>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>