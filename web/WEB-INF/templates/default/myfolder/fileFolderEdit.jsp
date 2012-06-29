<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

 <jsp:include page="/WEB-INF/templates/ekms/form_header.jsp" />
<table width="100%" border="0" cellpadding="5" cellspacing="1">

    <%--name--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='mf.label.fileName'/></strong></td>
    <td class="contentBgColor">
		<x:display name="${w.txtName.absoluteName}"/>
    </td>
    </tr>

    <%--description--%>
    <tr>
    <td class="contentBgColor" align="right" valign="top" nowrap width="30%"><strong><fmt:message key='mf.label.desc'/></strong></td>
    <td class="contentBgColor">

    </td>
    </tr>

</table>
<jsp:include page="/WEB-INF/templates/ekms/form_footer.jsp" />