<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"  />
<jsp:include page="../form_header.jsp" flush="true"/>
<table>
    <tr>
        <td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.title'/></td>
        <td><x:display name="${form.titleTF.absoluteName}" /></td>
    </tr>
    <tr>
        <td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.content'/></td>
        <td><x:display name="${form.contentTB.absoluteName}" /></td>
    </tr>
    <tr>
        <td valign="top" align="right" class="blogLabel"><fmt:message key='weblog.label.publishTime'/></td>
        <td><x:display name="${form.publishDate.absoluteName}" /><x:display name="${form.publishTime.absoluteName}" /></td>
    </tr>
    <tr>
        <td valign="top" align="right" class="blogLabel"></td>
        <td><x:display name="${form.postButton.absoluteName}" /><x:display name="${form.saveAsDraftButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" /></td>
    </tr>
    <tr>
        <td valign="top" align="right" class="blogLabel"></td>
        <td></td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
