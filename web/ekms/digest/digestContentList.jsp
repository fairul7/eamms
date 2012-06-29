<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="digestcontent">
    	<com.tms.cms.digest.ui.DigestContentTable name="digestcontenttable" width="100%" />
    </page>
</x:config>
<c:if test="${!(empty param.digestId)}">
    <x:set name="digestcontent.digestcontenttable" property="digestId" value="${param.digestId}"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      <fmt:message key='digest.label.contentList'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
    <fmt:message key='digest.label.note'/>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digestcontent.digestcontenttable"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>

<%@ include file="/ekms/includes/footer.jsp" %>