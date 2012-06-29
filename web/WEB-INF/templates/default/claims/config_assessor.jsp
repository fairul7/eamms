<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="1" class="forumBackground" >
<tr>
    <td colspan="2">&nbsp;</td>
</tr>
<tr>
    <td align="right" valign="top" width="20%"><b><fmt:message key="claims.label.defaultassessors"/></b></td>
    <td ><x:display name="${w.assessors.absoluteName}"/></td>
</tr>
<tr>
    <td  ></td>
    <td  ><x:display name="${w.bn_Submit.absoluteName}"/></td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>