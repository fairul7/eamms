<%@include file="includes/taglib.jsp" %>
 <%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='general.label.messaging'/> > <fmt:message key='messaging.label.downloadPOP3Email'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
<x:template type="com.tms.collab.messaging.ui.QuotaCheck" />
<c:if test="${!exceedQuota}">
        <blockquote>
            <x:template type="com.tms.collab.messaging.ui.CheckEmail" />
            <br><br>
            <input type="button" class="buttonClass" value="<fmt:message key='messaging.message.gottoinbox'/>" onclick="location='messageList.jsp?folder=Inbox'">
            <br><br>
        </blockquote>
</c:if>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>

<c:if test="${!exceedQuota}">
  <tr>
  <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <iframe name="userStatus" id="userStatus" src="<c:url value="/ekms/messaging/userStatus.jsp" />" marginheight="0" marginwidth="0" frameborder="0" height="100" width="100%">
      </iframe>
  </td>
  </tr>
</c:if>

</table>

<%@include file="includes/footer.jsp" %>
