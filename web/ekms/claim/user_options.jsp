<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_user_options">
          <com.tms.hr.claim.ui.ClaimConfigAssistant name="assistant" width="100%"/>
    </page>
</x:config>
<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

<c:if test="${forward.name == 'success'}">
    <script>
        alert("Assistants Saved");
        document.location = "<c:url value="/ekms/claim/owner_list_draft.jsp" />";
    </script>
</c:if>


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
<fmt:message key="claims.menu.header"/> > <fmt:message key="claims.label.assistants"/>
		
			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

	<p><fmt:message key="claims.label.enableUserSubmitClaim"/></p>
	<table align="left" width="100%">
		<tr valign="top">
			<td>
<x:display name="jsp_user_options.assistant" />
			</td>
		</tr>
	</table>



	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
