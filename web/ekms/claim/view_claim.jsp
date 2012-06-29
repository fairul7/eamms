<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_viewClaim">
        <com.tms.hr.claim.ui.ClaimFormIndexViewForm name="viewForm" width="100%" />
        <com.tms.hr.claim.ui.ClaimFormIndexActionForm name="actionForm" width="100%" />
        <com.tms.hr.claim.ui.ClaimFormItemTable name="formItemTable" width="100%" />
    </page>
</x:config>


<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">
<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<c:if test="${! empty param.formId}">
    <c:set var="formId" value="${param.formId}"></c:set>
</c:if>
<c:if test="${empty param.formId}">
    <c:set var="formId" value="${widgets['jsp_viewClaim.formItemTable'].formId}"></c:set>
</c:if>


<c:if test="${! empty param.cn}">

    <c:set var="cn" value="${param.cn}" ></c:set>
    <x:set name="jsp_viewClaim.formItemTable" property="cn" value="${param.cn}" ></x:set>
</c:if>


<c:if test="${forward.name == 'approveSuccess'}">
    <script>
        alert("<fmt:message key='claims.message.successfullyApprovedExpenses'/>");
        document.location = "<c:url value="/ekms/claim/approver_approve.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name == 'rejectSuccess'}">
    <script>
        //alert('Successfully rejected the expenses.');
        //document.location = "<c:url value="/ekms/claim/approver_approve.jsp" />";
        window.open('rejectForm.jsp?id=<c:out value="${formId}"/>&apptype=approve','','resizable=no,width=500,height=400,menubar=no,toolbar=no');
    </script>
</c:if>

<c:if test="${forward.name == 'assessSuccess'}">
    <script>
        alert("<fmt:message key='claims.message.successfullyProcessedExpenses'/>");
        document.location = "<c:url value="/ekms/claim/approver_process.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name == 'assessRejectSuccess'}">
    <script>
        //alert('Successfully rejected the expenses.');
        //document.location = "<c:url value="/ekms/claim/approver_process.jsp" />";
        window.open('rejectForm.jsp?id=<c:out value="${formId}"/>&apptype=assess','','resizable=no,width=500,height=400,menubar=no,toolbar=no');
    </script>
</c:if>

<c:if test="${forward.name == 'closeSuccess'}">
    <script>
        alert("<fmt:message key='claims.message.successfullyClosedExpenses'/>");
        document.location = "<c:url value="/ekms/claim/owner_list_closed.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name == 'resubSuccess'}">
    <script>
        alert("<fmt:message key='claims.message.expensesResetToDraft'/>");
        document.location = "<c:url value="/ekms/claim/owner_list_draft.jsp" />";
    </script>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key="claims.label.claimListing"/> > <fmt:message key="claims.label.viewExpenses"/>

        </font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">


        <table width="100%">
            <tr valign="top">
                <td>
                    <c:if test="${not empty(param.formId)}">
                        <x:set name="jsp_viewClaim.viewForm" property="id" value="${param.formId}"/>
                        <x:display name="jsp_viewClaim.viewForm"/>
                    </c:if>
                    <c:if test="${empty(param.formId) and not empty(widgets['jsp_viewClaim.viewForm'].id)}">
                        <x:display name="jsp_viewClaim.viewForm"/>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td>
                    <script>
                        <!--
                        function doPrint() {
                            var url = "<c:url value="/ekms/claim/print_claim.jsp?id=${formId}" />";
                            myWin= open(url, "printWin", "width=800,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
                        }
                        //-->
                    </script>
                    <div align="right">&nbsp;&nbsp;<input type="button" class="button" value="Print" onclick="doPrint()"></div>
                </td>
            </tr>
            <tr valign="top">
                <td>
                    <c:if test="${not empty(param.formId)}">
                        <x:set name="jsp_viewClaim.formItemTable"
                               property="formId" value="${param.formId}"/>

                        <x:display name="jsp_viewClaim.formItemTable"/>
                    </c:if>
                    <c:if test="${empty(param.formId) and not empty(widgets['jsp_viewClaim.formItemTable'].formId)}">

                        <x:display name="jsp_viewClaim.formItemTable"/>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td bgcolor="#FFFFFF"><spacer type="block" height="1"></td>
            </tr>
            <tr>
                <td>
                    <c:if test="${not empty(param.formId)}">
                        <x:set name="jsp_viewClaim.actionForm" property="id" value="${param.formId}"/>
                        <x:display name="jsp_viewClaim.actionForm"/>
                    </c:if>
                    <c:if test="${empty(param.formId) and not empty(widgets['jsp_viewClaim.actionForm'].id)}">
                        <x:display name="jsp_viewClaim.actionForm"/>
                    </c:if>
                </td>
            </tr>

        </table>


    </td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
