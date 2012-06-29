<%@include file="includes/taglib.jsp" %>
 <%@ page import="com.tms.collab.messaging.model.MessagingModule"%>
<%@include file="includes/header.jsp" %>



<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.messagingAccountActivated'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <blockquote>
            <fmt:message key='messaging.label.activatedMes'/> <b><a href="index.jsp"><fmt:message key='messaging.label.here'/></a></b> <fmt:message key='messaging.label.tostartusingintranetmessaging'/>.
            <br><br>
            <fmt:message key='messaging.label.intranetmessagingaddress'/>
            <b><c:out value="${currentUser.username}" />@<%= MessagingModule.INTRANET_EMAIL_DOMAIN %></b>
            <br><br>
            <table cellpadding="0" cellspacing="0" width="80%" align="center"><tr><td class="contentBgColor">
            <hr size="1"><i>
            <b class="highlight"><fmt:message key='messaging.label.doyouknowthat'/>.</b><br>
            <fmt:message key='messaging.label.intranetdescription'>
                <fmt:param >
                   <%= MessagingModule.INTRANET_EMAIL_DOMAIN %>
                </fmt:param>
            </fmt:message>
           
            </i><hr size="1">
            </td></tr></table>
            <br><br>
        </blockquote>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@include file="includes/footer.jsp" %>
