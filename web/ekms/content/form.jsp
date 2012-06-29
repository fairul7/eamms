<%@ include file="/common/header.jsp" %>
<x:template type="TemplateProcessVote" />


<%@ page import="kacang.ui.WidgetManager,
                 com.tms.collab.formwizard.ui.ViewForm"%>
<x:config>
    <page name="viewFormPage">
        <com.tms.collab.formwizard.ui.ViewForm name="viewForm"/>
    </page>
</x:config>

<c:if test="${!empty param.formId}">
	<x:set name="viewFormPage.viewForm" property="formId" value="${param.formId}"/>
</c:if>

<c:if test="${forward.name == 'submissionMessage'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewForm form = (ViewForm)wm.getWidget("viewFormPage.viewForm");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="formMessage.jsp?reload=true&formId=${formId}" />
<%
}
%>
</c:if>

<c:if test="${forward.name == 'formLink'}">
<%
WidgetManager wm = (WidgetManager)session.getAttribute("WidgetManager");
if (wm != null){
	ViewForm form = (ViewForm)wm.getWidget("viewFormPage.viewForm");
%>
<c-rt:set var="formId" value="<%=form.getFormId()%>"/>
<c:redirect url="form.jsp?reload=true&formId=${formId}" />
<%
}
%>
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
				<tr><td class="contentHeader"><span class="contentName"><fmt:message key='cms.label.submitForm'/></span></td></tr>
			</table>
		</td>
	</tr>
    <tr>
        <td class="contentBody" align="center">
            <x:display name="viewFormPage.viewForm" />
            <br>
        </td>
    </tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
</table>

<jsp:include page="includes/footer.jsp" flush="true"  />
<%@ include file="/ekms/includes/footer.jsp"  %>





