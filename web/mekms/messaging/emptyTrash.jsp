<%@include file="includes/taglib.jsp" %>
 <%@include file="includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
        &nbsp;<fmt:message key='messaging.label.emptyTrashFolder'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
      <table width="100%" border="0" cellspacing="0" cellpadding="2">
        <tr>
          <td class="contentBgColor">
            <x:template type="com.tms.collab.messaging.ui.EmptyTrash" />
          </td>
        </tr>
        <tr>
          <td class="contentBgColor">&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr bgcolor="#EFEFEF">
    <td colspan="2" valign="TOP" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="5"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">&nbsp;
      <input type="button" class="buttonClass" value="<fmt:message key='messaging.message.gottoinbox'/>" onclick="location='messageList.jsp?folder=Inbox'">
    </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
