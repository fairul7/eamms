<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_owner_list_submitted">
        <com.tms.hr.claim.ui.ClaimFormIndexTableOwner name="table1" width="100%"/>
    </page>
</x:config>




<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">


    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimListing"/> > <fmt:message key="claims.label.submittedExpenses"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:set name="jsp_owner_list_submitted.table1" property="state" value="sub"/>
            <x:display name="jsp_owner_list_submitted.table1"/>



        </td></tr>


        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>


<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
