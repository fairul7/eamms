<%@ include file="/common/header.jsp" %>
<%@ page import="com.tms.cms.digest.ui.ContentTable,
				com.tms.cms.digest.ui.DigestFormOpen"%>
<x:permission permission="com.tms.cms.digest.ManageDigest" module="com.tms.cms.digest.model.DigestModule" url="noPermission.jsp"/>
<x:config>
    <page name="digestcontents">
    	<com.tms.cms.digest.ui.ContentTable name="contenttable" width="100%" />
     	<com.tms.cms.digest.ui.DigestFormOpen name="digestformopen" width="100%" />
     	<com.tms.cms.digest.ui.DigestPath name="digestpath"/>
    </page>
</x:config>
<c:if test="${!(empty param.digestId)}">
    <x:set name="digestcontents.contenttable" property="digestId" value="${param.digestId}"/>
    <x:set name="digestcontents.digestformopen" property="digestId" value="${param.digestId}"/>
    <x:set name="digestcontents.digestpath" property="digestId" value="${param.digestId}"/>
</c:if>
<c-rt:set var="forward_add" value="<%= ContentTable.FORWARD_ADD %>"/>
<c-rt:set var="forward_submit" value="<%= DigestFormOpen.FORWARD_SUBMIT %>"/>
<c:if test="${forward.name == forward_submit}">
    <c:redirect url="/ekms/digest/digest.jsp"/>
</c:if>
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr valign="MIDDLE">
    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
      &nbsp;<x:display name="digestcontents.digestpath"></x:display> > 
      <fmt:message key='digest.label.contentList'/>
    </font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digestcontents.digestformopen"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
	<x:display name="digestcontents.contenttable"></x:display>
    </td>
  </tr>
  <tr>
    <td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
      <img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  </tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@ include file="/ekms/includes/footer.jsp" %>