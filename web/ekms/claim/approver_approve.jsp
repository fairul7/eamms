<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_approver_approve">
        <com.tms.hr.claim.ui.ClaimFormIndexTableApprover name="pending" width="100%" />
        <com.tms.hr.claim.ui.ClaimFormIndexTableApprover name="approved" width="100%" />
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <c:if test="${forward.name=='selectItem'}">
        <script>
            alert("Couldn't process your request. No selected item found.");
        </script>
    </c:if>

    <%@include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp" />

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimApproval"/> > <fmt:message key="claims.;abe;.Approve/RejectExpenses"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <p>Expenses Pending Approval</p>
            <x:set name="jsp_approver_approve.pending" property="state" value="sub"/>
            <x:display name="jsp_approver_approve.pending"/>

            <p>Approved Expenses, Pending Assessor Checking</p>
            <x:set name="jsp_approver_approve.approved" property="state" value="app"/>
            <x:display name="jsp_approver_approve.approved"/>
        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
