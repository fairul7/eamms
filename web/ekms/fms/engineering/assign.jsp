<%@include file="/common/header.jsp"%>

<x:config>
     <page name="assign">
		<com.tms.fms.engineering.ui.AssignForm name="form" width="100%"/>
     </page>
</x:config>

<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
	<x:set name="assign.form" property="requestId" value="${requestId}" />
  </c:when>
</c:choose>

<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="asgId" value="${param.id}"/>
	<x:set name="assign.form" property="asgId" value="${asgId}" />
  </c:when>
</c:choose>

<c:choose>
  <c:when test="${not empty(param.userId)}">
    <c:set var="userId" value="${param.userId}"/>
	<x:set name="assign.form" property="userId" value="${userId}" />
  </c:when>
</c:choose>

<c:choose>
  <c:when test="${not empty(param.act)}">
    <c:set var="act" value="${param.act}"/>
	<x:set name="assign.form" property="act" value="${act}" />
  </c:when>
</c:choose>

<c:if test="${param.act=='close'}" >

    <script>
    	window.opener.location='assignManpower.jsp?action=close';
        window.close();
    </script>
</c:if>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.label.assignManpower"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="assign.form"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="2"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />