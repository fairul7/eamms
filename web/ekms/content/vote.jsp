<%@ include file="/common/header.jsp" %>
<x:template type="TemplateProcessVote" />
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table width="100%" cellpadding="3" cellspacing="1">
    <tr><td class="contentHeader"><fmt:message key="vote.label.vote"></fmt:message></td></tr>
    <tr><td class="contentStrapColor">&nbsp;</td></tr>
    <tr>
        <td class="contentLabel" align="center">
            <x:template type="TemplateDisplayVote" properties="id=${param.VoteID}&showImage=true" />
            <br>
        </td>
    </tr>
    <tr><td class="contentStrapColor">&nbsp;</td></tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>


