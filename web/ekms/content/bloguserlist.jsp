<%@ page import="com.tms.collab.weblog.ui.BlogForm"%>
<%@include file="/common/header.jsp" %>
<x:config >
	<page name="userPage">
		<com.tms.collab.weblog.ui.UsersList name="userList"/>
	</page>
</x:config>
<c:if test="${!empty param.id}" >
	<c:redirect url="blogview.jsp?blogId=${param.id}" />
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr>
					<td class="contentPath">
						<a href="weblog.jsp"  class="contentPathLink"><fmt:message key='weblog.label.weblog'/></a> >
						<a href=""  class="contentPathLink" onClick="return false;"><fmt:message key='weblog.label.usersBlog'/></a>
					</td>
				</tr>
            </table>
        </td>
    </tr>
	<tr>
		<td valign="top" class="contentBody">
			<table cellpadding="2" cellspacing="1" width="95%"  align="center">
				<tr><td>&nbsp;</td></tr>
				<tr><td><x:display name="userPage"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
<br>
<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>

