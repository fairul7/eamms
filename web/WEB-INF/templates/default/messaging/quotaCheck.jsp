<%@include file="/common/header.jsp" %>
<c:if test="${exceedQuota}">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
      </tr>
      <tr>
        <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            <blockquote>
            <fmt:message key='messaging.label.quotaExceedMessage' />
            </blockquote>
        </td>
      </tr>
      <tr>
        <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
          <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
      </tr>
    </table>
</c:if>