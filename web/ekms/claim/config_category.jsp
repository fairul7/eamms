<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_config_category">
          <com.tms.hr.claim.ui.ClaimFormItemCategoryForm name="form"/>
          <com.tms.hr.claim.ui.ClaimFormItemCategoryTable name="table" width="100%"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${forward.name == 'success'}">
    <script>
        document.location = "<c:url value="/ekms/claim/user_editClaim.jsp" />";
    </script>
</c:if>

<c:if test="${forward.name == 'submit'}">
    <script>
        alert("<fmt:message key='claims.category.submit.added'/>")
        window.location="config_category.jsp";
    </script>
</c:if>




<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
            <fmt:message key="claims.label.claimAdmin"/> > <fmt:message key="claims.category.title"/>

			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">



<x:display name="jsp_config_category.form"/>
    </td></tr>
    <tr>
        <td colspan="2" bgcolor="#FFFFFF"><spacer type="block" height="1"></td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="jsp_config_category.table"/>




	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>


 </x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
