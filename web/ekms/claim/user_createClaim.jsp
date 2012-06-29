<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="createDraft">
          <com.tms.hr.claim.ui.ClaimFormIndexForm name="createClaim"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${forward.name == 'edit'}">
		<c:redirect url="user_editClaim.jsp?formId=${widgets['createDraft.createClaim'].justCreatedId}"/>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
			<fmt:message key="claims.menu.header"/> > <fmt:message key="claims.label.createClaim"/>
			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">


<x:display name="createDraft.createClaim"/>
    <fmt:message key='claims.label.new.info'/>
    </td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
