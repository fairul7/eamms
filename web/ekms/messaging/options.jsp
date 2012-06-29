<%@include file="includes/taglib.jsp" %>
<%@include file="includes/header.jsp" %>

<script>
<!--
    function doDeleteIntranetAccount() {
        if(confirm('<fmt:message key='messaging.label.deleteAllMessagesWarning'/>')) {
            document.location = 'deleteIntranetAccount.jsp';
            return false;
        } else {
            alert('Cancelled!');
            return false;
        }
    }
//-->
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='general.label.messaging'/> > <fmt:message key='messaging.label.options'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <ul>
                <li><a href="editIntranetAccount.jsp"><fmt:message key='messaging.label.intranetMessagingOptions'/></a></li><br>
                <li><a href="folderList.jsp"><fmt:message key='messaging.label.manageFolders'/></a></li><br>
                <li><a href="pop3AccountList.jsp"><fmt:message key='messaging.label.managePOP3Accounts'/></a></li><br>
				<li><a href="emailFilterListing.jsp"><fmt:message key='messaging.label.manageFilters'/></a></li><br><br>
                <li><a href="" onclick="return doDeleteIntranetAccount();"><fmt:message key='messaging.label.deleteIntranetMessagingAccount'/></a></li><br>
				<fmt:message key='messaging.label.warning!ThisoptionwillremoveALLyourmessagingdata'/>.
            <br><br>
            <input type="button" class="buttonClass" value="<fmt:message key='messaging.label.cancelButton'/>" onclick="location='messageList.jsp'">
            <br><br>
        </ul>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>



