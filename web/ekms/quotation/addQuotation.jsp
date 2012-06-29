<!--- addQuotation.jsp --->
<%@ page import="kacang.ui.WidgetManager, com.tms.quotation.ui.AddQuotation" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="addQuotationPage">
    <%--<portlet name="addQuotationPortlet" text="Add Quotation" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.AddQuotation name="addQuotation" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= AddQuotation.QUOTATION_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= AddQuotation.QUOTATION_ADD_FAIL %>"/>
<c-rt:set var="forward_exists" value="<%= AddQuotation.QUOTATION_ADD_EXISTS %>"/>

<!--- permissions --->
<x:set name="addQuotationPage.addQuotation" property="canAdd" value="1"/>
<x:set name="addQuotationPage.addQuotation" property="canEdit" value="1"/>
<x:set name="addQuotationPage.addQuotation" property="canDelete" value="1"/>

<c:if test="${!empty param.companyId }">
  <x:set name="addQuotationPage.addQuotation" property="companyId" value="${param.companyId}"/>
</c:if>
<c:if test="${!empty param.contactId }">
  <x:set name="addQuotationPage.addQuotation" property="customerId" value="${param.contactId}"/>
</c:if>

<c:if test="${!empty param.customerId }">
  <x:set name="addQuotationPage.addQuotation" property="customerId" value="${param.customerId}"/>
</c:if>

<%-- Handle events --%>
<c:choose>
  <c:when test="${forward.name == forward_succ}">
    <c:set var="quotationId" value="${widgets['addQuotationPage.addQuotation'].quotationId }" />
    <c:set var="tableId" value="${ widgets['addQuotationPage.addQuotation'].tableId }" />
    <script>
      alert("<fmt:message key='com.tms.quotation.quotation.alert.added'/>");
<%--    document.location = "<c:url value='newQuotation_item.jsp?quotationId=${quotationId}'/>"; --%>
      document.location = "<c:url value='qtnItemForm.jsp'>
        <c:param name='quotationId' value='${quotationId}'/>
<%--    <c:param name='tableId' value='${tableId}'/> --%>
      </c:url>";
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
            <%--<x:display name="addQuotationPage.addQuotationPortlet"/>--%>
            <x:display name="addQuotationPage.addQuotation"/>
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
