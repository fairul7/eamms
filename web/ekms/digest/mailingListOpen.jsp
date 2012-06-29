<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.cms.digest.ui.MailingListFormOpen"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="mailinglistopen">
    	<com.tms.cms.digest.ui.MailingDigestTable name="mailingdigesttable" width="100%" />
     	<com.tms.cms.digest.ui.MailingListFormOpen name="mailinglistformopen" width="100%" type="digest" />	
    </page>
</x:config>
<c-rt:set var="forward_submit" value="<%= MailingListFormOpen.FORWARD_CANCEL %>"/>
<c:if test="${forward.name == forward_submit}">
    <c:redirect url="/ekms/digest/mailingList.jsp"/>
</c:if>
<c:if test="${forward.name == 'success'}">
<script>alert('<fmt:message key='digest.label.summariesSend'/>');
</script>
</c:if>
<c:if test="${forward.name == 'successissue'}">
<script>alert('<fmt:message key='digest.label.issueSend'/>');
</script>
</c:if>
<c:if test="${forward.name == 'no_select_print'}">
<script>alert('<fmt:message key='digest.label.emptyPrintMessage'/>');
</script>
</c:if>
<c:if test="${forward.name == 'no_select'}">
<script>alert('<fmt:message key='digest.label.emptyMessage'/>');
</script>
</c:if>
<c:if test="${forward.name == 'print'}">
<script>
var myWin = window.open('printIssueList.jsp?reporttype=digest','print','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=600,height=800,left=50,top=50');
  if (myWin != null) {
    myWin.focus();
  }
</script>
</c:if>
<c:if test="${forward.name == 'print_summary'}">
<script>
var myWin = window.open('printSummaryList.jsp','print','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600,left=50,top=50');
  if (myWin != null) {
    myWin.focus();
  }
</script>
</c:if>
<c:if test="${!(empty param.mailingListId)}">
    <x:set name="mailinglistopen.mailingdigesttable" property="mailingListId" value="${param.mailingListId}"/>
    <x:set name="mailinglistopen.mailinglistformopen" property="mailingListId" value="${param.mailingListId}"/>
</c:if>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
	&nbsp;<fmt:message key='digest.label.sendOutDigest'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="mailinglistopen.mailinglistformopen"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
    <x:display name="mailinglistopen.mailingdigesttable"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>