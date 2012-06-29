<%@include file="includes/taglib.jsp" %>
<%@ page import="com.tms.collab.messaging.ui.Pop3AccountTable"%>

<x:config>
    <page name="pop3AccountListPage">
        <com.tms.collab.messaging.ui.Pop3AccountTable name="table"/>
    </page>
</x:config>

<c:if test="${!empty param.accountId}">
    <c:redirect url="editPop3Account.jsp?accountId=${param.accountId}" />
</c:if>

<c-rt:set var="forwardNewPop3Account" value="<%= Pop3AccountTable.FORWARD_NEW_POP3_ACCOUNT %>"/>
<c:if test="${forward.name eq forwardNewPop3Account}">
    <c:redirect url="newPop3Account.jsp" />
</c:if>

<%@include file="includes/header.jsp" %>


<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<fmt:message key='messaging.label.options'/> > <fmt:message key='messaging.label.pOP3AccountListing'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="pop3AccountListPage.table" />
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>



<%@include file="includes/footer.jsp" %>
