<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.cms.digest.ui.DigestForm"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="digests">
     	<com.tms.cms.digest.ui.DigestForm name="digestform" width="100%" />
     	<com.tms.cms.digest.ui.DigestIssuePath name="digestissuepathNew"/>
    </page>
</x:config>
<c-rt:set var="forward_cancel" value="<%= DigestForm.FORWARD_CANCEL %>"/>
<c-rt:set var="forward_success" value="<%= DigestForm.FORWARD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= DigestForm.FORWARD_FAILED %>"/>
<c:if test="${!(empty param.digestIssueId)}">
    <x:set name="digests.digestform" property="digestIssueId" value="${param.digestIssueId}"/>
    <x:set name="digests.digestissuepathNew" property="digestIssueId" value="${param.digestIssueId}"/>
</c:if>
<c:if test="${forward.name == forward_cancel}">
<script>
        document.location = "digest.jsp";
    </script>
</c:if>
<c:if test="${forward.name == forward_success}">
    <script>
        alert("<fmt:message key='digest.label.newDigestAdded'/>");
        document.location = "content.jsp?digestId=<c:out value="${param.digestId}"/>";
    </script>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<x:display name="digests.digestissuepathNew"></x:display> > 
      <fmt:message key='digest.label.newDigest'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digests.digestform"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>