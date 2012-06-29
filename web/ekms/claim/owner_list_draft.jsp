<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_owner_list_draft">
          <com.tms.hr.claim.ui.ClaimFormIndexTableOwner name="table1" width="100%"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">


<c:if test="${forward.name == 'alertEmptyItems'}">
    <script>
		alert('One or more expenses submission was not submitted because they have empty items! Please double check again. ');
    </script>
</c:if>

<c:if test="${forward.name == 'submitted'}">
   <script>
      alert('Sucessfully submitted the expenses.');
      document.location = "<c:url value="/ekms/claim/owner_list_submitted.jsp" />";
   </script>
</c:if>
<c:if test="${forward.name == 'failSubmit'}">
   <script>
      alert('One or more of the selected expenses was not submitted, please double check if it is empty.');
   </script>
</c:if>
<c:if test="${forward.name == 'selectItem'}">
    <script>
        alert('Could not process your request. No selected item found.');
    </script>
</c:if>



<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">

			<fmt:message key='claims.label.claimListing'/> > <fmt:message key='claims.label.draftExpenses'/>


			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:set name="jsp_owner_list_draft.table1" property="state" value="cre"/>
<x:display name="jsp_owner_list_draft.table1"/>



	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
