<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.cms.digest.ui.DigestTable,
				com.tms.cms.digest.ui.DigestIssueFormOpen"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="digest">
    	<com.tms.cms.digest.ui.DigestTable name="digesttable" width="100%" />
     	<com.tms.cms.digest.ui.DigestIssueFormOpen name="digestissueformopen" width="100%" />
     	<com.tms.cms.digest.ui.DigestIssuePath name="digestissuepath" />   	
    </page>
</x:config>
<c-rt:set var="forward_add" value="<%= DigestTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_submit" value="<%= DigestIssueFormOpen.FORWARD_SUBMIT %>"/>
<c:if test="${forward.name == forward_add}">
	<script>
        document.location = "/ekms/digest/digestNew.jsp";
    </script>
</c:if>
<c:if test="${forward.name == forward_submit}">
    <c:redirect url="/ekms/digest/digestIssue.jsp"/>
</c:if>
<c:if test="${!(empty param.digestId)}">
	<script>
        document.location = "content.jsp?digestId=<c:out value="${param.digestId}"/>";
    </script>
</c:if>
<c:if test="${!(empty param.digestIssueId)}">
    <x:set name="digest.digesttable" property="digestIssueId" value="${param.digestIssueId}"/>
    <x:set name="digest.digestissueformopen" property="digestIssueId" value="${param.digestIssueId}"/>
    <x:set name="digest.digestissuepath" property="digestIssueId" value="${param.digestIssueId}"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
&nbsp;<x:display name="digest.digestissuepath"></x:display> > <fmt:message key='digest.label.digestList'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digest.digestissueformopen"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digest.digesttable"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>