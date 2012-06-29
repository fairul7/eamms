<%@ include file="/common/header.jsp" %>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr>
					<td class="contentPath">
						<a href="weblog.jsp" class="contentPathLink"><fmt:message key='weblog.label.weblog'/></a>
					</td>
				</tr>
            </table>
        </td>
    </tr>
	<tr>
		<td valign="top" class="contentBody">
			<table cellpadding="2" cellspacing="1" width="95%"  align="center">
				<tr><td>&nbsp;</td></tr>
				<tr><td><x:template name="users" type="com.tms.collab.weblog.ui.BlogView" properties="blogId=${param.blogId}&postViewUrl=postView.jsp"/></td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
	</tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true"  />
<%@ include file="/ekms/includes/footer.jsp" %>
