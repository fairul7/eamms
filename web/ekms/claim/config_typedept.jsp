<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="claimconfigtypedepart">
        <com.tms.hr.claim.ui.ClaimConfigTypeDepartment name="table" width="100%"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <c:if test="${forward.name == 'notSelected'}">
        <script>
            alert('please select at least  ');
        </script>
    </c:if>

    <c:if test="${!empty param.id}">

        <script>
            window.location = "config_typedeptedit.jsp?id=<c:out value="${param.id}"/>";
        </script>
    </c:if>

    <%@ include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimAdmin"/> > <fmt:message key="claims.label.configTypeAndDep"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="claimconfigtypedepart.table"/>
            <fmt:message key="claims.label.note.typedept.warn"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>
