
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="edittype">
        <com.tms.hr.claim.ui.Edittype name="form"/>

    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <c:choose>
    
	    <c:when test="${forward.name == 'duplicated'}" >
            <script>
				 alert('<fmt:message key="claims.label.duplicateTypeOrCode"/>');
            </script>
        </c:when>
        <c:when test="${forward.name == 'edit'}" >
            <script>
	            alert('<fmt:message key="claims.message.updatedType"/>');
                window.location="config_type.jsp";
            </script>
        </c:when>
		<c:when test="${forward.name == 'cancel'}" >
            <script>
                window.location="config_type.jsp";
            </script>
        </c:when>
    </c:choose>

    <c:if test="${!empty param.id}">
        <x:set name="edittype.form" property="id" value="${param.id}" ></x:set>
    </c:if>

    <%@include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp" />

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimAdmin"/> > <fmt:message key="claim.type.edit"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="edittype.form"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>