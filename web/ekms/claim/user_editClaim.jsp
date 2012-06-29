<%@ page import="com.tms.hr.claim.ui.ClaimFormItemTable"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_editClaim">

        <com.tms.hr.claim.ui.ClaimFormIndexEditForm name="editForm" width="100%" />
        <com.tms.hr.claim.ui.ClaimFormItemForm name="addFormItem" width="100%" />
        <com.tms.hr.claim.ui.ClaimFormIndexActionForm name="actionForm" width="100%" />
        <tabbedpanel name="tab1" width="100%">
            <panel name="general" text="General">
                <com.tms.hr.claim.ui.ClaimFormItemGeneralForm name="addFormItemGeneral" width="100%" />
            </panel>
            <panel name="travel" text="Travel">
                <com.tms.hr.claim.ui.ClaimFormItemTravelForm name="addFormItemTravel" width="100%"/>
            </panel>
                <%--<panel name="standard" text="Predefined">
          <com.tms.hr.claim.ui.ClaimFormItemStandardForm name="addFormItemStandard" width="100%" />
                </panel>--%>
        </tabbedpanel>
        <com.tms.hr.claim.ui.ClaimFormItemTable name="formItemTable" width="100%" />
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<script>
    function doPrint() {
        var url = "<c:url value="/ekms/claim/print_claim.jsp?id=${widgets['jsp_editClaim.formItemTable'].formId}" />";
        myWin= open(url, "printWin", "width=800,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
    }

</script>

<c:if test="${forward.name == 'print'}">
    <script>
        doPrint();
    </script>
</c:if>

<c:if test="${forward.name == 'submitted'}">
    <script>
        alert('<fmt:message key="claims.message.submitExpenses"/>');
        document.location = "<c:url value="/ekms/claim/owner_list_submitted.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name == 'failSubmit'}">
    <script>
        alert('<fmt:message key="claims.message.unableSubmitExpenses"/>');    
    </script>
</c:if>

<c:if test="${!empty param.id}" >
    <script>
        popUp('popDraftEdit.jsp?id=<c:out value="${param.id}"/>');
        function popUp(URL) {
            day = new Date();
            //id = day.getTime();
            id = "same_window";
            eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=1,location=0,statusbar=1,menubar=0,resizable=0,width=700,height=500,left = 226,top = 182');");
        }
    </script>
</c:if>

<%
    ClaimFormItemTable tbl = (ClaimFormItemTable)Application.getInstance().getWidget(request,"jsp_editClaim.formItemTable");
    String formId = tbl.getFormId();
    if (formId!=null && !formId.equals(""))
        pageContext.setAttribute("fId",formId);
%>
<c:if test="${forward.name=='add'}">
    <x:set name="jsp_editClaim.tab1.travel.addFormItemTravel" property="claimant" value="${widgets['jsp_editClaim.editForm'].claimant}" ></x:set>

    <script>
        // window.open('user_addClaimsItem.jsp?formId=<c:out value="${fId}"/>','','width=650,height=500,toolbar=no,menubar=no,resizable=yes');

        popUp('user_addClaimsItem.jsp?formId=<c:out value="${fId}"/>');
        function popUp(URL) {
            day = new Date();
            //id = day.getTime();
            id = "same_window";
            eval("page" + id + " = window.open(URL, '" + id + "', 'width=650,height=500,toolbar=no,menubar=no,resizable=yes');");
        }
    </script>

</c:if>

<%
    try
    {
%>

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key="claims.label.claimListing"/> > <fmt:message key="claims.label.editExpenses"/>
        </font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <table width="100%" bgcolor="#FFFFFF" cellpadding="0" cellspacing="0"><tr><td>

            <table width="100%" cellspacing="1" cellpadding="5">
                <tr valign="top">
                    <td bgcolor="#EFEFEF" class="contentBgColor">
                        <c:if test="${not empty(param.formId)}">
                            <x:set name="jsp_editClaim.editForm" property="id" value="${param.formId}"/>
                            <x:display name="jsp_editClaim.editForm"/>
                        </c:if>
                        <c:if test="${empty(param.formId) and not empty(widgets['jsp_editClaim.editForm'].id)}">
                            <x:display name="jsp_editClaim.editForm"/>
                        </c:if>
                    </td>
                </tr>
                <tr valign="top">
                    <td bgcolor="#EFEFEF" class="contentBgColor">
                        <b>Expenses Item(s)</b><br>
                        <c:if test="${not empty(param.formId)}">
                            <x:set name="jsp_editClaim.formItemTable"
                                   property="formId" value="${param.formId}"/>
                            <x:display name="jsp_editClaim.formItemTable"/>
                        </c:if>
                        <c:if test="${empty(param.formId) and not empty(widgets['jsp_editClaim.editForm'].id)}">
                            <x:set name="jsp_editClaim.formItemTable"
                                   property="formId" value="${widgets['jsp_editClaim.editForm'].id}"/>
                            <x:display name="jsp_editClaim.formItemTable"/>
                        </c:if>
                    </td>
                </tr>

                <!-- <tr valign="top" bgcolor="#cccccc"> <td><spacer type="block" height="1"></td> </tr>-->
                <tr valign="top">
                    <td align="right" bgcolor="#EFEFEF" class="contentBgColor">
                        <c:if test="${not empty(param.formId)}">
                            <x:set name="jsp_editClaim.actionForm" property="id" value="${param.formId}"/>
                            <x:display name="jsp_editClaim.actionForm"/>
                        </c:if>
                        <c:if test="${empty(param.formId) and not empty(widgets['jsp_editClaim.editForm'].id)}">
                            <x:set name="jsp_editClaim.actionForm" property="id"
                                   value="${widgets['jsp_editClaim.editForm'].id}"/>
                            <x:display name="jsp_editClaim.actionForm"/>
                        </c:if>
                    </td>
                </tr>

            </table>

        </td></tr></table>

    </td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<%
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }

%>

</x:permission>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
