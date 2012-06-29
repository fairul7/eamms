<!--- editQuotation.jsp --->
<%@ page import="kacang.ui.WidgetManager, com.tms.quotation.ui.AddQuotation" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="editQuotationPage">
    <%--<portlet name="editQuotationPortlet" text="Edit Quotation" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.AddQuotation name="editQuotation" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= AddQuotation.QUOTATION_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= AddQuotation.QUOTATION_ADD_FAIL %>"/>
<c-rt:set var="forward_exists" value="<%= AddQuotation.QUOTATION_ADD_EXISTS %>"/>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.quotationId)}">
    <c:set var="quotationId" value="${param.quotationId}"/>
  </c:when>
  <c:otherwise>
    <%--<c:set var="quotationId" value="${widgets['editQuotationPage.editQuotationPortlet.editQuotation'].quotationId}"/>--%>
    <c:set var="quotationId" value="${widgets['editQuotationPage.editQuotation'].quotationId}"/>
  </c:otherwise>
</c:choose>
<%--<x:set name="editQuotationPage.editQuotationPortlet.editQuotation" property="quotationId" value="${quotationId}"/>
<x:set name="editQuotationPage.editQuotationPortlet.editQuotation" property="type" value="${type}"/>--%>
<x:set name="editQuotationPage.editQuotation" property="type" value="${type}"/>
<x:set name="editQuotationPage.editQuotation" property="quotationId" value="${quotationId}"/>
<x:set name="editQuotationPage.editQuotation" property="whoModified" value="${sessionScope.currentUser.username}"/>

<!--- permissions --->
<x:set name="editQuotationPage.editQuotation" property="canAdd" value="1"/>
<x:set name="editQuotationPage.editQuotation" property="canEdit" value="1"/>
<x:set name="editQuotationPage.editQuotation" property="canDelete" value="1"/>

<c:choose>
  <c:when test="${forward.name == forward_succ}">
    <script>
    alert("<fmt:message key='com.tms.quotation.quotation.alert.updated'/>");
    document.location = "<c:url value='viewQuotation.jsp'/>";
    </script>
  </c:when>
  <c:when test="${forward.name == forward_exists}">
    <script>alert("<fmt:message key='com.tms.quotation.quotation.alert.exists'/>");</script>
  </c:when>
  <c:when test="${forward.name == forward_fail}">
    <script>alert("<fmt:message key='com.tms.quotation.quotation.alert.error'/>");</script>
  </c:when>
</c:choose>

<%--<x:permission permission="com.tms.cms.ManageContent" module="com.tms.cms.core.model.ContentManager" url="noPermission.jsp" />--%>

<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />

<table cellpadding="0" cellspacing="0" border="0" style="text-align: left; width: 100%;">
<tbody>
<tr>
  <%--<td style="vertical-align: top; width: 250px;">
    <jsp:include page="/cmsadmin/includes/sideContentModules.jsp" flush="true" />
  </td>--%>
  <td style="vertical-align: top;">
    <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
    <tbody>
    <tr>
      <td style="vertical-align: top;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr>
          <td style="vertical-align: top;">
            <%--<x:display name="editQuotationPage.editQuotationPortlet"/>--%>
            <x:display name="editQuotationPage.editQuotation"/>
          </td>
        </tr>
        </table>
      </td>
    </tr>
    </tbody>
    </table>
  </td>
</tr>
</tbody>
</table>

<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
