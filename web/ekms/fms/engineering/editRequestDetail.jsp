<%@include file="/common/header.jsp"%>

<x:config>
  <page name="fms_requestPage">
    <com.tms.fms.engineering.ui.editRequestForm name="edit" width="100%"/>
  </page>
</x:config>
<c:if test="${not empty(param.requestId)}">
	<x:set name="fms_requestPage.edit" property="requestId" value="${param.requestId}"/>
</c:if>
<c:if test="${forward.name=='cancel'}" >
    <script>
    	window.opener.location='requestDetails.jsp';
        window.close();
    </script>
</c:if>

<c:if test="${forward.name=='edited'}" >
    <script>
    	window.opener.location='requestDetails.jsp';
        window.close();
    </script>
</c:if>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.editRequest"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_requestPage.edit"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
