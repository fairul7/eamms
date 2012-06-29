<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="digestorder">
    	<com.tms.cms.digest.ui.ReorderDigest name="reorderdigest" width="100%" />
    	<com.tms.cms.digest.ui.DigestIssuePath name="digestissuepathorder"/>
    </page>
</x:config>

<c:if test="${!(empty param.digestIssueId)}">
    <x:set name="digestorder.reorderdigest" property="digestIssueId" value="${param.digestIssueId}"/>
    <x:set name="digestorder.digestissuepathorder" property="digestIssueId" value="${param.digestIssueId}"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<x:display name="digestorder.digestissuepathorder"></x:display> >
      <fmt:message key='digest.label.orderDigest'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digestorder.reorderdigest"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>