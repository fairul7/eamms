<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_standard_type">
          <com.tms.hr.claim.ui.ClaimStandardTypeTable name="table" width="100%"/>
          <com.tms.hr.claim.ui.ClaimStandardTypeForm name="form"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">

			Predefined Expenses Items


			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="jsp_standard_type.form"/>
    </td></tr>
    <tr><td colspan="2" bgcolor="#FFFFFF"><spacer type="block" height="1">
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:display name="jsp_standard_type.table"/>
	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
