<%@ include file="/common/header.jsp" %>
<x:template type="TemplateProcessVote" />

<x:config>
    <page name="formMessagePage">
	        <com.tms.collab.formwizard.ui.FormMessagePanel name="msgPanel"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name="formMessagePage.msgPanel" property="formId" value="${param.formId}"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp"  %>
<jsp:include page="includes/header.jsp" flush="true"  />

<table width="100%" cellpadding="3" cellspacing="1">
    <tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr><td class="contentPath"><a href="<c:url value="/ekms/content/"/> " class="contentPathLink"><fmt:message key='cms.label.home'/></a></td></tr>
            </table>
        </td>
    </tr>
	<tr>
		<td valign="top">
			<br>
            <table cellpadding="5" cellspacing="0" width="95%" align="center">
				<tr><td class="contentHeader"><span class="contentName"><fmt:message key='cms.label.submissionMessage'/></span></td></tr>
			</table>
		</td>
	</tr>
    <tr>
        <td class="contentBody" align="center">
            <x:display name="formMessagePage.msgPanel" />
            <br>
        </td>
    </tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
</table>

<jsp:include page="includes/footer.jsp" flush="true"  />
<%@ include file="/ekms/includes/footer.jsp"  %>
