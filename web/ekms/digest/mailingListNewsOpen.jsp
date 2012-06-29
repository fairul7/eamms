<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.cms.digest.ui.MailingListFormOpen"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="mailinglistnews">
     	<com.tms.cms.digest.ui.MailingListFormOpen name="mailinglistnewsformopen" width="100%" type="news" />	
    </page>
</x:config>
<c-rt:set var="forward_submit" value="<%= MailingListFormOpen.FORWARD_CANCEL %>"/>
<c:if test="${forward.name == forward_submit}">
    <c:redirect url="/ekms/digest/mailingList.jsp"/>
</c:if>
<c:if test="${forward.name == 'successissue'}">
<script>alert('<fmt:message key='digest.label.newsSend'/>');
window.location="/ekms/digest/mailingList.jsp";
</script>
</c:if>
<c:if test="${forward.name == 'print'}">
<script>
var myWin = window.open('printIssueList.jsp?reporttype=news','print','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=800,height=600,left=50,top=50');
  if (myWin != null) {
    myWin.focus();
  }
</script>
</c:if>
<c:if test="${!(empty param.mailingListId)}">
    <x:set name="mailinglistnews.mailinglistnewsformopen" property="mailingListId" value="${param.mailingListId}"/>
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
	<x:display name="mailinglistnews.mailinglistnewsformopen"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>