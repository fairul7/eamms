<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_config_project_edit">
        <com.tms.hr.claim.ui.ClaimProjectEditForm name="form"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <%@include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp" />

    <c:if test="${forward.name == 'updateSuccess'}">
        <script>
           	alert('<fmt:message key="claims.message.updatedProject"/>');
            document.location = "<c:url value="/ekms/claim/config_project.jsp" />";
        </script>
    </c:if>

	<c:if test="${forward.name == 'cancel'}">
		<script>
       		document.location = "<c:url value="/ekms/claim/config_project.jsp" />";
		</script>
    </c:if>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b>
                <font color="#FFCF63" class="contentTitleFont">

                    <fmt:message key="claims.label.claimAdmin"/> > <fmt:message key="claims.label.editProject"/>

                </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <div align="right"><a href="config_project.jsp">Project Listing</a><div>

            <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

                <c:if test="${not empty(param.id)}">
                    <x:set name="jsp_config_project_edit.form" property="id" value="${param.id}"/>
                    <x:display name="jsp_config_project_edit.form"/>
                </c:if>
                <c:if test="${empty(param.id) and not empty(widgets['jsp_config_project_edit.form'].id)}">
                    <x:display name="jsp_config_project_edit.form"/>
                </c:if>

            </td></tr>

            <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>


