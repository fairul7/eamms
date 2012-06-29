<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="ClaimHistory">
          <com.tms.hr.claim.ui.ClaimHistory name="showHistory"/>

    </page>
</x:config>




<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">


<c:if test="${!empty param.id}">
<script>
    window.location="view_claim.jsp?cn=jsp_owner_list_closed.table1&et=sel&formId=<c:out value="${param.id}"/>";
</script>

</c:if>




<table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='claims.label.claimhistory'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>

              <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>


                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">


<x:display name="ClaimHistory.showHistory"/>

            </table>


      </x:permission>

    <jsp:include page="includes/footer.jsp"/>
    <%@include file="/ekms/includes/footer.jsp"%>



