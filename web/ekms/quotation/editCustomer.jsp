<!--- editCustomer.jsp --->
<%@ page import="kacang.ui.WidgetManager, com.tms.quotation.ui.AddCustomer" %>

<%@ include file="/common/header.jsp" %>

<x:config>
  <page name="editCustomerPage">
    <%--<portlet name="editCustomerPortlet" text="Edit Customer" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.AddCustomer name="editCustomer" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>
<c-rt:set var="forward_succ" value="<%= AddCustomer.CUSTOMER_ADD_SUCCESS %>"/>
<c-rt:set var="forward_fail" value="<%= AddCustomer.CUSTOMER_ADD_FAIL %>"/>
<c-rt:set var="forward_exists" value="<%= AddCustomer.CUSTOMER_ADD_EXISTS %>"/>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.customerId)}">
    <c:set var="customerId" value="${param.customerId}"/>
  </c:when>
  <c:otherwise>
    <%--<c:set var="customerId" value="${widgets['editCustomerPage.editCustomerPortlet.editCustomer'].customerId}"/>--%>
    <c:set var="customerId" value="${widgets['editCustomerPage.editCustomer'].customerId}"/>
  </c:otherwise>
</c:choose>
<%--<x:set name="editCustomerPage.editCustomerPortlet.editCustomer" property="customerId" value="${customerId}"/>
<x:set name="editCustomerPage.editCustomerPortlet.editCustomer" property="type" value="${type}"/>--%>
<x:set name="editCustomerPage.editCustomer" property="customerId" value="${customerId}"/>
<x:set name="editCustomerPage.editCustomer" property="type" value="${type}"/>

<x:set name="editCustomerPage.editCustomer" property="whoModified" value="${sessionScope.currentUser.username}"/>
<!--- permissions --->
<x:set name="editCustomerPage.editCustomer" property="canAdd" value="1"/>
<x:set name="editCustomerPage.editCustomer" property="canEdit" value="1"/>
<x:set name="editCustomerPage.editCustomer" property="canDelete" value="1"/>

<c:choose>
  <c:when test="${forward.name == forward_succ}">
    <script>
      alert("<fmt:message key='com.tms.quotation.customer.alert.updated'/>");
      document.location = "<c:url value='viewCustomer.jsp'/>";
    </script>
  </c:when>
  <c:when test="${forward.name == forward_exists}">
    <script>alert("<fmt:message key='com.tms.quotation.customer.alert.exists'/>");</script>
  </c:when>
  <c:when test="${forward.name == forward_fail}">
    <script>alert("<fmt:message key='com.tms.quotation.customer.alert.error'/>");</script>
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
            <%--<x:display name="editCustomerPage.editCustomerPortlet"/>--%>
            <x:display name="editCustomerPage.editCustomer"/>
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
