<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.cms.digest.ui.RecipientTable"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="recipients">
     	<com.tms.cms.digest.ui.RecipientTable name="recipientstable" width="100%" />
    </page>
</x:config>
<c-rt:set var="forward_add" value="<%= RecipientTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_error" value="<%= RecipientTable.FORWARD_ERROR %>"/>
<c:if test="${forward.name == forward_add}">
    <c:redirect url="/ekms/digest/recipientsNew.jsp"/>
</c:if>
<c:if test="${forward.name == forward_error}">
    <script>
    alert('<fmt:message key='digest.label.selectedItemsInUsed'/>');
    </script>
</c:if>
<c:if test="${!(empty param.recipientId)}">
	<script>
        document.location = "recipientsOpen.jsp?recipientId=<c:out value="${param.recipientId}"/>";
    </script>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;
      <fmt:message key='digest.label.recipientsList'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="recipients.recipientstable"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>