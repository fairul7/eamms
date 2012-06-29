<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_requestPage">
    <com.tms.fms.engineering.ui.RequestForm name="add" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="Add"/>
<x:set name="fms_requestPage.add" property="type" value="${type}"/>

<x:set name="fms_requestPage.add" property="requestId" value="${param.requestId}"/>


<c:choose>

  <c:when test="${forward.name == 'NoServices'}">
    <script>alert('<fmt:message key="fms.facility.msg.needToSelectServices"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestAdded"/>'); 
    document.location = "<c:url value="requestListing.jsp"/>";</script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.requestNotAdded"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'INVALID-DATE-FROM'}">
    <script>alert('<fmt:message key="fms.facility.msg.dateRequiredFromToInvalid"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'INVALID-DATE-TO'}">
    <script>alert('<fmt:message key="fms.facility.msg.dateRequiredFromToInvalid"/>');</script>
  </c:when>
</c:choose>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.request.label.submitRequest"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_requestPage.add"/>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>