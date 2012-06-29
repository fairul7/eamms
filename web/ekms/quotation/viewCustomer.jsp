<!--- viewCustomer.jsp --->
<%@ page import="com.tms.quotation.ui.ViewCustomer" %>

<%@ include file="/common/header.jsp" %>
<%-- <x:permission permission="" module="" /> --%>
<x:config>
  <page name="viewCustomerPage">
    <%--<portlet name="viewCustomerPortlet" text="Customer" width="100%" permanent="true">--%>
    <com.tms.quotation.ui.ViewCustomer name="viewCustomer" width="100%"/>
    <%--</portlet>--%>
  </page>
</x:config>

<c-rt:set var="forward_add" value="<%= ViewCustomer.FORWARD_ADD %>"/>
<c-rt:set var="forward_delete" value="<%= ViewCustomer.FORWARD_DELETE %>"/>

<!--- permissions --->
<x:set name="viewCustomerPage.viewCustomer" property="canAdd" value="1"/>
<x:set name="viewCustomerPage.viewCustomer" property="canEdit" value="1"/>
<x:set name="viewCustomerPage.viewCustomer" property="canDelete" value="1"/>
<x:set name="viewCustomerPage.viewCustomer" property="canActivate" value="1"/>
<c:if test="${forward.name == forward_add}">
  <c:redirect url="addCustomer.jsp"/>
</c:if>
<c:if test="${!empty param.customerId}">
  <c:redirect url="editCustomer.jsp?customerId=${param.customerId}"/>
</c:if>

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
      <td class="contentTitleFont" style="vertical-align: top;">
        <fmt:message key='com.tms.quotation.table.customer'/>
<%--    <hr size="1" color="cccccc">
--%>
      </td>
    </tr>
    <tr>
      <td>    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="darkgreen">
        <tr style="background-color:#E9F5FF;">
          <td style="vertical-align: top;">
            <%--<x:display name="viewCustomerPage.viewCustomerPortlet"/>--%>
            <x:display name="viewCustomerPage.viewCustomer"/>
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
