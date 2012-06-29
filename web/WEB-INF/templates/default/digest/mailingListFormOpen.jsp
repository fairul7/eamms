<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%">
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.mailingListName'/></td>
        <td align="left">
        <x:display name="${form.mailingListName.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.digestIssueName'/>
        </td><td align="left">
        <x:display name="${form.digestIssueList.absoluteName}"/>       
        </td>
    </tr>
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.emailFormat'/></td>
        <td align="left">
        <x:display name="${form.emailFormat.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td width="25%" valign="top" align="right" class="forumRowLabel">
        <fmt:message key='digest.label.recipients'/></td>
        <td align="left">
        <x:display name="${form.recipients.absoluteName}"/>
        </td>
    </tr>
    <tr>
        <td width="25%" align="right">&nbsp;</td>
        <td align="left">
            <x:display name="${form.submit.absoluteName}"/>
            <x:display name="${form.send.absoluteName}"/>
            <input type="button" class="button" onclick="window.open('printIssueList.jsp?mailingListId=<c:out value="${form.mailingListId}"/>','print','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=600,height=800,left=50,top=50');" 
            	value="<c:choose><c:when test="${form.type =='digest' }"><fmt:message key='digest.label.printIssue'/></c:when><c:when test="${form.type=='news' }"><fmt:message key='digest.label.printNews'/></c:when></c:choose>">
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>