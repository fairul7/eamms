<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_config_category_edit">
        <com.tms.hr.claim.ui.ClaimFormItemCategoryEditForm name="form"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <%@include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp" />

    <c:if test="${forward.name == 'updated'}">
        <script>
        	alert('<fmt:message key="claims.message.updatedCategory"/>');
            document.location = "<c:url value="/ekms/claim/config_category.jsp" />";
        </script>
    </c:if>
	
	<c:if test="${forward.name == 'cancel'}">
		<script>
            document.location = "<c:url value="/ekms/claim/config_category.jsp" />";
        </script>
	</c:if>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimAdmin"/> > <fmt:message key="claim.category.edit"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <c:if test="${not empty(param.id)}">
                <x:set name="jsp_config_category_edit.form" property="id" value="${param.id}"/>
                <x:display name="jsp_config_category_edit.form"/>
            </c:if>
            <c:if test="${empty(param.id) and not empty(widgets['jsp_config_category_edit.form'].id)}">
                <x:display name="jsp_config_category_edit.form"/>
            </c:if>
        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
